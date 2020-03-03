package com.netease.nim.uikit.business.session.emoji;

import android.content.Context;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;
import com.netease.nim.uikit.business.session.activity.TeamMessageActivity;
import com.netease.nim.uikit.business.session.module.list.MsgAdapter;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;
import com.netease.nim.uikit.model.CollectionEmoji;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

import static com.netease.nim.uikit.business.session.actions.BaseAction.sessionId;

/**
 * 贴图显示viewpager
 */
public class EmoticonView {

    private ViewPager emotPager;
    private LinearLayout pageNumberLayout;
    /**
     * 总页数.
     */
    private int pageCount;

    /**
     * 每页显示的数量，Adapter保持一致.
     */
    public static final int EMOJI_PER_PAGE = 27; // 最后一个是删除键
    public static final int STICKER_PER_PAGE = 8;
    public static final int COLLECTION_PER_PAGE = 18; // 收藏
    private Context context;
    private IEmoticonSelectedListener listener;
    private EmoticonViewPaperAdapter pagerAdapter = new EmoticonViewPaperAdapter();
    public CollectionEmoji collectionEmoji;
    public List<CollectionEmoji> emojis;

    /**
     * 所有表情贴图支持横向滑动切换
     */
    private int categoryIndex;                           // 当套贴图的在picker中的索引
    private boolean isDataInitialized = false;             // 数据源只需要初始化一次,变更时再初始化
    private List<StickerCategory> categoryDataList;       // 表情贴图数据源
    private List<Integer> categoryPageNumberList;           // 每套表情贴图对应的页数
    private int[] pagerIndexInfo = new int[2];           // 0：category index；1：pager index in category
    private IEmoticonCategoryChanged categoryChangedCallback; // 横向滑动切换时回调picker


    public EmoticonView(Context context, IEmoticonSelectedListener mlistener,
                        ViewPager mCurPage, LinearLayout pageNumberLayout) {
        this.context = context.getApplicationContext();
        this.listener = mlistener;
        this.pageNumberLayout = pageNumberLayout;
        this.emotPager = mCurPage;

        emotPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (categoryDataList != null) {
                    // 显示所有贴图表情
                    setCurStickerPage(position);
                    if (categoryChangedCallback != null) {
                        int currentCategoryChecked = pagerIndexInfo[0];// 当前那种类别被选中
                        categoryChangedCallback.onCategoryChanged(currentCategoryChecked);
                    }
                } else if (collectionEmoji != null) {
                    setCurStickerPage(position);
                    if (categoryChangedCallback != null) {
                        int currentCategoryChecked = pagerIndexInfo[0];// 当前那种类别被选中
                        categoryChangedCallback.onCategoryChanged(currentCategoryChecked);
                    }
                }else {
                    // 只显示表情
                    setCurEmotionPage(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        emotPager.setAdapter(pagerAdapter);
        emotPager.setOffscreenPageLimit(1);
    }

    public void setCategoryDataReloadFlag() {
        isDataInitialized = false;
    }

    public void showStickers(int index) {
        this.categoryIndex = index;
        showCollectionGridView();
        // 判断是否需要变化
//        if (isDataInitialized && getPagerInfo(emotPager.getCurrentItem()) != null
//                && pagerIndexInfo[0] == index && pagerIndexInfo[1] == 0) {
//            return;
//        }
//
//        this.categoryIndex = index;
//        showStickerGridView();
    }

    public void showEmojis() {
        showEmojiGridView();
    }

    private int getCategoryPageCount(StickerCategory category) {
        if (category == null) {
            return (int) Math.ceil(EmojiManager.getDisplayCount() / (float) EMOJI_PER_PAGE);
        } else {
            if (category.hasStickers()) {
                List<StickerItem> stickers = category.getStickers();
                return (int) Math.ceil(stickers.size() / (float) STICKER_PER_PAGE);
            } else {
                return 1;
            }
        }
    }

    private void setCurPage(int page, int pageCount) {
        int hasCount = pageNumberLayout.getChildCount();
        //    int pageNumber = categoryPageNumberList.get(categoryIndex);
//        int forMax = Math.max(hasCount, pageNumber);

        int forMax = Math.max(hasCount, pageCount);
        ImageView imgCur = null;
        for (int i = 0; i < forMax; i++) {
//            if (pageNumber <= hasCount) {
            if (pageCount <= hasCount) {
                if (i >= pageCount) {
                    pageNumberLayout.getChildAt(i).setVisibility(View.GONE);
                    continue;
                } else {
                    imgCur = (ImageView) pageNumberLayout.getChildAt(i);
                }
            } else {
                if (i < hasCount) {
                    imgCur = (ImageView) pageNumberLayout.getChildAt(i);
                } else {
                    imgCur = new ImageView(context);
                    imgCur.setBackgroundResource(R.drawable.nim_view_pager_indicator_selector);
                    pageNumberLayout.addView(imgCur);
                }
            }

            imgCur.setId(i);
            imgCur.setSelected(i == page); // 判断当前页码来更新
            imgCur.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ******************************** 表情  *******************************
     */
    private void showEmojiGridView() {
        pageCount = (int) Math.ceil(EmojiManager.getDisplayCount() / (float) EMOJI_PER_PAGE);
        pagerAdapter.notifyDataSetChanged();
        resetEmotionPager();
    }

    private void resetEmotionPager() {
        setCurEmotionPage(0);
        emotPager.setCurrentItem(0, false);
    }

    private void setCurEmotionPage(int position) {
    //    setCurPage(position, pageCount);
        setCurPage(position,categoryPageNumberList.get(categoryIndex));
    }

    public OnItemClickListener emojiListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            int position = emotPager.getCurrentItem();
            int pos = position; // 如果只有表情，那么用默认方式计算
            if (categoryDataList != null && categoryPageNumberList != null) {
                // 包含贴图
                getPagerInfo(position);
                pos = pagerIndexInfo[1];
            }

            int index = arg2 + pos * EMOJI_PER_PAGE;

            if (listener != null) {
                int count = EmojiManager.getDisplayCount();
                if (arg2 == EMOJI_PER_PAGE || index >= count) {
                    listener.onEmojiSelected("/DEL");
                } else {
                    String text = EmojiManager.getDisplayText((int) arg3);
                    if (!TextUtils.isEmpty(text)) {
                        listener.onEmojiSelected(text);
                    }
                }
            }
        }
    };

    /**
     * ******************************** 贴图  *******************************
     */

    private void showStickerGridView() {
        initData();
        pagerAdapter.notifyDataSetChanged();

        // 计算起始的pager index
        int position = 0;
        for (int i = 0; i < categoryPageNumberList.size(); i++) {
            if (i == categoryIndex) {
                break;
            }
            position += categoryPageNumberList.get(i);
        }

        setCurStickerPage(position);
        emotPager.setCurrentItem(position, false);
    }

    /**
     * 收藏表情
     */
    public void showCollectionGridView() {
        initData();
        updateCollection();
        pagerAdapter.notifyDataSetChanged();

        // 计算起始的pager index
        int position = 0;
        for (int i = 0; i < categoryPageNumberList.size(); i++) {
            if (i == categoryIndex) {
                break;
            }
            position += categoryPageNumberList.get(i);
        }

        setCurStickerPage(position);
        emotPager.setCurrentItem(position, false);
    }

    private void initData() {

//        if (isDataInitialized) {//数据已经初始化，未变动不重新加载数据
//            return;
//        }
//
//        if (categoryDataList == null) {
//            categoryDataList = new ArrayList<>();
//        }
//
        if (categoryPageNumberList == null) {
            categoryPageNumberList = new ArrayList<>();
        }
        if (emojis == null) {
            emojis = new ArrayList<>();
        }
//
//        categoryDataList.clear();
        categoryPageNumberList.clear();
//
//        final StickerManager manager = StickerManager.getInstance();
//
//        categoryDataList.add(null); // 表情
        emojis.add(null);
        categoryPageNumberList.add(getCategoryPageCount(null));
        if (CommonUtil.role == CommonUtil.SELLER) {
            collectionEmoji = CommonUtil.getCollectionEmoji();
            //收藏
            if (collectionEmoji != null && collectionEmoji.getBody() != null) {
                emojis.add(collectionEmoji);
                categoryPageNumberList.add(collectionEmoji.getBody().getTotalCount()/COLLECTION_PER_PAGE+1);
            } else {
                categoryPageNumberList.add(0);
            }
        }

//
//        List<StickerCategory> categories = manager.getCategories();
//
//        categoryDataList.addAll(categories); // 贴图
//        for (StickerCategory c : categories) {
//            categoryPageNumberList.add(getCategoryPageCount(c));
//        }
//
        pageCount = 0;//总页数
        for (Integer count : categoryPageNumberList) {
            pageCount += count;
        }
        isDataInitialized = true;
    }

    // 给定pager中的索引，返回categoryIndex和positionInCategory
    private int[] getPagerInfo(int position) {
//        if (categoryDataList == null || categoryPageNumberList == null) {
//            return pagerIndexInfo;
//        }

        if (categoryPageNumberList == null) {
            return pagerIndexInfo;
        }
        int cIndex = categoryIndex;
        int startIndex = 0;
        int pageNumberPerCategory = 0;
        for (int i = 0; i < categoryPageNumberList.size(); i++) {
            pageNumberPerCategory = categoryPageNumberList.get(i);
            if (position < startIndex + pageNumberPerCategory) {
                cIndex = i;
                break;
            }
            startIndex += pageNumberPerCategory;
        }

        this.pagerIndexInfo[0] = cIndex;
        this.pagerIndexInfo[1] = position - startIndex;

        return pagerIndexInfo;
    }

    private void setCurStickerPage(int position) {
        getPagerInfo(position);
        int categoryIndex = pagerIndexInfo[0];
        int pageIndexInCategory = pagerIndexInfo[1];
        int categoryPageCount = categoryPageNumberList.get(categoryIndex);

        setCurPage(pageIndexInCategory, categoryPageCount);
    }

    public void setCategoryChangCheckedCallback(IEmoticonCategoryChanged callback) {
        this.categoryChangedCallback = callback;
    }

    private OnItemClickListener stickerListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            int position = emotPager.getCurrentItem();
            getPagerInfo(position);
            int cIndex = pagerIndexInfo[0];
            int pos = pagerIndexInfo[1];
            StickerCategory c = categoryDataList.get(cIndex);
            int index = arg2 + pos * STICKER_PER_PAGE; // 在category中贴图的index

            if (index >= c.getStickers().size()) {
                LogUtil.i("sticker", "index " + index + " larger than size " + c.getStickers().size());
                return;
            }

            if (listener != null) {
                StickerManager manager = StickerManager.getInstance();
                List<StickerItem> stickers = c.getStickers();
                StickerItem sticker = stickers.get(index);
                StickerCategory real = manager.getCategory(sticker.getCategory());

                if (real == null) {
                    return;
                }

                listener.onStickerSelected(sticker.getCategory(), sticker.getName());
            }
        }
    };

    private OnItemClickListener collectionListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            int position = emotPager.getCurrentItem();
            getPagerInfo(position);
            int cIndex = pagerIndexInfo[0];
            int pos = pagerIndexInfo[1];
            CollectionEmoji collectionEmoji = emojis.get(cIndex);
            int index = arg2 + pos * COLLECTION_PER_PAGE; // 在category中贴图的index
            boolean isExit = FileUtils.isFileExist(collectionEmoji.getBody().getList().get(index).getEmojiDesc());
            IMMessage message;
            if (isExit) {
                message = MessageBuilder.createImageMessage(sessionId, context instanceof P2PMessageActivity ? SessionTypeEnum.P2P: SessionTypeEnum.Team,
                        FileUtils.createFile(collectionEmoji.getBody().getList().get(index).getEmojiDesc()));
                NIMClient.getService(MsgService.class).sendMessage(message, false);
                RecyclerView messageListView;
                if (context instanceof P2PMessageActivity) {
                    messageListView = P2PMessageActivity.instance.get().findViewById(R.id.messageListView);
                } else {
                    messageListView = TeamMessageActivity.instance.get().findViewById(R.id.messageListView);
                }
                List<IMMessage> addedListItems = new ArrayList<>(1);
                addedListItems.add(message);
                MsgAdapter adapter = (MsgAdapter) messageListView.getAdapter();
                adapter.updateShowTimeItem(addedListItems, false, true);
                adapter.appendData(message);
                messageListView.scrollToPosition(adapter.getBottomDataPosition());
            } else {
                ToastHelper.showToast(context,"该表情包发送有误");
            }
        }
    };

    /**
     * ***************************** PagerAdapter ****************************
     */
    private class EmoticonViewPaperAdapter extends PagerAdapter {
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return pageCount == 0 ? 1 : pageCount;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            StickerCategory category = null;
            CollectionEmoji temp = null;
            int pos;
//            if (categoryDataList != null && categoryDataList.size() > 0 && categoryPageNumberList != null
//                    && categoryPageNumberList.size() > 0) {
            if (collectionEmoji != null  && categoryPageNumberList != null
                    && categoryPageNumberList.size() > 0) {
                // 显示所有贴图&表情
                getPagerInfo(position);
                int cIndex = pagerIndexInfo[0];
             //   category = categoryDataList.get(cIndex);
                temp = emojis.get(cIndex);
                pos = pagerIndexInfo[1];
            } else{
           //  只显示表情
                category = null;
                pos = position;
            }
            if (temp != null) {
                pageNumberLayout.setVisibility(View.VISIBLE);
                GridView gridView = new GridView(context);
                gridView.setOnItemClickListener(collectionListener);
                gridView.setPadding(10, 0, 10, 0);
                gridView.setAdapter(new CollectAdapter(context, collectionEmoji,pos*COLLECTION_PER_PAGE));
                gridView.setNumColumns(6);
                gridView.setHorizontalSpacing(4);
                gridView.setGravity(Gravity.CENTER);
                gridView.setSelector(R.drawable.nim_emoji_item_selector);
                container.addView(gridView);
                return gridView;
            } else if (category == null) {
                pageNumberLayout.setVisibility(View.VISIBLE);
                GridView gridView = new GridView(context);
                gridView.setOnItemClickListener(emojiListener);
                gridView.setAdapter(new EmojiAdapter(context, pos * EMOJI_PER_PAGE));
                gridView.setNumColumns(7);
                gridView.setHorizontalSpacing(5);
                gridView.setVerticalSpacing(5);
                gridView.setGravity(Gravity.CENTER);
                gridView.setSelector(R.drawable.nim_emoji_item_selector);
                container.addView(gridView);
                return gridView;
            } else {
                pageNumberLayout.setVisibility(View.VISIBLE);
                GridView gridView = new GridView(context);
                gridView.setPadding(10, 0, 10, 0);
                gridView.setOnItemClickListener(stickerListener);
                gridView.setAdapter(new StickerAdapter(context, category, pos * STICKER_PER_PAGE));
                gridView.setNumColumns(4);
                gridView.setHorizontalSpacing(5);
                gridView.setGravity(Gravity.CENTER);
                gridView.setSelector(R.drawable.nim_emoji_item_selector);
                container.addView(gridView);
                return gridView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View layout = (View) object;
            container.removeView(layout);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void updateCollection () {
        CommonUtil.setOnUpdateCollectionEmojiListener(new CommonUtil.onUpdateCollectionEmojiListener() {
            @Override
            public void onUpdateEmojiAdapter(CollectionEmoji data) {
                CommonUtil.setCollectionEmoji(data);
                emotPager.setCurrentItem(0);
                showCollectionGridView();
//                collectionEmoji = data;
//                pagerAdapter.notifyDataSetChanged();
            }
        });
    }
}
