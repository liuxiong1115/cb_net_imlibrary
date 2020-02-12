//package com.netease.nim.uikit.business.session.activity;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewCompat;
//import android.support.v4.view.ViewPager;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.RequestBuilder;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.netease.nim.uikit.R;
//import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
//import com.netease.nim.uikit.common.activity.ToolBarOptions;
//import com.netease.nim.uikit.common.activity.UI;
//import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
//import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog.onSeparateItemClickListener;
//import com.netease.nim.uikit.common.ui.imageview.BaseZoomableImageView;
//import com.netease.nim.uikit.common.ui.imageview.ImageGestureListener;
//import com.netease.nim.uikit.common.util.C;
//import com.netease.nim.uikit.common.util.file.AttachmentStore;
//import com.netease.nim.uikit.common.util.media.BitmapDecoder;
//import com.netease.nim.uikit.common.util.media.ImageUtil;
//import com.netease.nim.uikit.common.util.storage.StorageUtil;
//import com.netease.nim.uikit.common.util.sys.ScreenUtil;
//import com.netease.nim.uikit.common.util.sys.TimeUtil;
//import com.netease.nimlib.sdk.AbortableFuture;
//import com.netease.nimlib.sdk.NIMClient;
//import com.netease.nimlib.sdk.Observer;
//import com.netease.nimlib.sdk.RequestCallback;
//import com.netease.nimlib.sdk.msg.MessageBuilder;
//import com.netease.nimlib.sdk.msg.MsgService;
//import com.netease.nimlib.sdk.msg.MsgServiceObserve;
//import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
//import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
//import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
//import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
//import com.netease.nimlib.sdk.msg.model.IMMessage;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//
///*
// * 查看聊天消息原图
// * Created by huangjun on 2015/3/6.
// */
//
//
//public class WatchMessagePictureActivity extends UI {
//
//    private static final String TAG = WatchMessagePictureActivity.class.getSimpleName();
//    private static final String INTENT_EXTRA_IMAGE = "INTENT_EXTRA_IMAGE";
//    private static final String INTENT_EXTRA_MENU = "INTENT_EXTRA_MENU";
//
//    private static final int MODE_NOMARL = 0;
//    private static final int MODE_GIF = 1;
//
//    private Handler handler;
//    private IMMessage message;
//    private boolean isShowMenu;
//    private List<IMMessage> imageMsgList = new ArrayList<>();
//    private int firstDisplayImageIndex = 0;
//
//    private boolean newPageSelected = false;
//
//    private View loadingLayout;
//    private BaseZoomableImageView image;
//    private ImageView simpleImageView;
//    private int mode;
//    protected CustomAlertDialog alertDialog;
//    private ViewPager imageViewPager;
//    private PagerAdapter adapter;
//    private AbortableFuture downloadFuture;
//
//    public static void start(Context context, IMMessage message) {
//        Intent intent = new Intent();
//        intent.putExtra(INTENT_EXTRA_IMAGE, message);
//        intent.setClass(context, WatchMessagePictureActivity.class);
//        context.startActivity(intent);
//    }
//
//    public static void start(Context context, IMMessage message, boolean isShowMenu) {
//        Intent intent = new Intent();
//        intent.putExtra(INTENT_EXTRA_IMAGE, message);
//        intent.putExtra(INTENT_EXTRA_MENU, isShowMenu);
//        intent.setClass(context, WatchMessagePictureActivity.class);
//        context.startActivity(intent);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.nim_watch_picture_activity);
//
//        ToolBarOptions options = new NimToolBarOptions();
//        options.titleString = "图片";
////        options.navigateId = R.drawable.nim_actionbar_white_back_icon;
//        setToolBar(R.id.toolbar, options);
//
//        handleIntent();
//
//        initActionbar();
//        findViews();
//
//        loadMsgAndDisplay();
//
//        handler = new Handler();
//        registerObservers(true);
//    }
//
//    private void handleIntent() {
//        this.message = (IMMessage) getIntent().getSerializableExtra(INTENT_EXTRA_IMAGE);
//        mode = ImageUtil.isGif(((ImageAttachment) message.getAttachment()).getExtension()) ? MODE_GIF : MODE_NOMARL;
//        setTitle(message);
//        isShowMenu = getIntent().getBooleanExtra(INTENT_EXTRA_MENU, true);
//    }
//
//    @Override
//    protected void onDestroy() {
//        registerObservers(false);
//        imageViewPager.setAdapter(null);
//        if (downloadFuture != null) {
//            downloadFuture.abort();
//            downloadFuture = null;
//        }
//        super.onDestroy();
//        //清除glide缓存
//        Glide.get(this.getApplicationContext()).clearMemory();
//    }
//
//    private void setTitle(IMMessage message) {
//        if (message == null) {
//            return;
//        }
//        super.setTitle(String.format("图片发送于%s", TimeUtil.getDateString(message.getTime())));
//    }
//
//    private void initActionbar() {
////        TextView menuBtn = findView(R.id.actionbar_menu);
////        if (isShowMenu) {
////            menuBtn.setVisibility(View.VISIBLE);
////            menuBtn.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    WatchPicAndVideoMenuActivity.startActivity(WatchMessagePictureActivity.this, message);
////                }
////            });
////        } else {
////            menuBtn.setVisibility(View.GONE);
////        }
//    }
//
//    private void findViews() {
//        alertDialog = new CustomAlertDialog(this);
//        loadingLayout = findViewById(R.id.loading_layout);
//
//        imageViewPager = (ViewPager) findViewById(R.id.view_pager_image);
//        simpleImageView = (ImageView) findViewById(R.id.simple_image_view);
//
//        if (mode == MODE_GIF) {
//            simpleImageView.setVisibility(View.VISIBLE);
//            simpleImageView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (isOriginImageHasDownloaded(message)) {
//                        showWatchPictureAction();
//                    }
//                    return true;
//                }
//            });
//
//            imageViewPager.setVisibility(View.GONE);
//        } else if (mode == MODE_NOMARL) {
//            simpleImageView.setVisibility(View.GONE);
//            imageViewPager.setVisibility(View.VISIBLE);
//        }
//    }
//
//    // 加载并显示
//    private void loadMsgAndDisplay() {
//        if (mode == MODE_NOMARL) {
//            queryImageMessages();
//        } else if (mode == MODE_GIF) {
//            displaySimpleImage();
//        }
//    }
//
//    // 显示单个gif图片
//    private void displaySimpleImage() {
//        String path = ((ImageAttachment) message.getAttachment()).getPath();
//        String thumbPath = ((ImageAttachment) message.getAttachment()).getThumbPath();
//        if (!TextUtils.isEmpty(path)) {
//            Glide.with(this).asGif().load(new File(path)).into(simpleImageView);
//            return;
//        }
//        if (!TextUtils.isEmpty(thumbPath)) {
//            Glide.with(this).asGif().load(new File(thumbPath)).into(simpleImageView);
//        }
//
//        if (message.getDirect() == MsgDirectionEnum.In) {
//            requestOriImage(message);
//        }
//    }
//
//
//    // 查询并显示图片，带viewPager
//    private void queryImageMessages() {
//        IMMessage anchor = MessageBuilder.createEmptyMessage(message.getSessionId(), message.getSessionType(), 0);
//        NIMClient.getService(MsgService.class).queryMessageListByType(MsgTypeEnum.image, anchor, Integer.MAX_VALUE).setCallback(new RequestCallback<List<IMMessage>>() {
//            @Override
//            public void onSuccess(List<IMMessage> param) {
//                for (IMMessage imMessage : param) {
//                    if (!ImageUtil.isGif(((ImageAttachment) imMessage.getAttachment()).getExtension())) {
//                        imageMsgList.add(imMessage);
//                    }
//                }
//                // imageMsgList.addAll(param);
//                Collections.reverse(imageMsgList);
//                setDisplayIndex();
//                setViewPagerAdapter();
//            }
//
//            @Override
//            public void onFailed(int code) {
//                Log.i(TAG, "query msg by type failed, code:" + code);
//            }
//
//            @Override
//            public void onException(Throwable exception) {
//
//            }
//        });
//    }
//
//    // 设置第一个选中的图片index
//    private void setDisplayIndex() {
//        for (int i = 0; i < imageMsgList.size(); i++) {
//            IMMessage imageObject = imageMsgList.get(i);
//            if (compareObjects(message, imageObject)) {
//                firstDisplayImageIndex = i;
//                break;
//            }
//        }
//    }
//
//    protected boolean compareObjects(IMMessage t1, IMMessage t2) {
//        return (t1.getUuid().equals(t2.getUuid()));
//    }
//
//    private void setViewPagerAdapter() {
//        adapter = new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return imageMsgList == null ? 0 : imageMsgList.size();
//            }
//
//            @Override
//            public void notifyDataSetChanged() {
//                super.notifyDataSetChanged();
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                View layout = (View) object;
//                BaseZoomableImageView iv = (BaseZoomableImageView) layout.findViewById(R.id.watch_image_view);
//                iv.clear();
//                container.removeView(layout);
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return (view == object);
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                ViewGroup layout;
//                layout = (ViewGroup) LayoutInflater.from(WatchMessagePictureActivity.this).inflate(R.layout.nim_image_layout_multi_touch, null);
//                layout.setBackgroundColor(Color.BLACK);
//
//                container.addView(layout);
//                layout.setTag(position);
//
//                if (position == firstDisplayImageIndex) {
//                    onViewPagerSelected(position);
//                }
//
//                return layout;
//            }
//
//            @Override
//            public int getItemPosition(Object object) {
//                return POSITION_NONE;
//            }
//        };
//
//        imageViewPager.setAdapter(adapter);
//        imageViewPager.setOffscreenPageLimit(2);
//        imageViewPager.setCurrentItem(firstDisplayImageIndex);
//        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (positionOffset == 0f && newPageSelected) {
//                    newPageSelected = false;
//                    onViewPagerSelected(position);
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                newPageSelected = true;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
//
//    private void onViewPagerSelected(int position) {
//        if (downloadFuture != null) {
//            downloadFuture.abort();
//            downloadFuture = null;
//        }
//        setTitle(imageMsgList.get(position));
//        updateCurrentImageView(position);
//        onImageViewFound(image);
//    }
//
//    // 初始化每个view的image
//    protected void updateCurrentImageView(final int position) {
//        View currentLayout = imageViewPager.findViewWithTag(position);
//        if (currentLayout == null) {
//            ViewCompat.postOnAnimation(imageViewPager, new Runnable() {
//
//                @Override
//                public void run() {
//                    updateCurrentImageView(position);
//                }
//            });
//            return;
//        }
//        image = (BaseZoomableImageView) currentLayout.findViewById(R.id.watch_image_view);
//        requestOriImage(imageMsgList.get(position));
//    }
//
//    // 若图片已下载，直接显示图片；若图片未下载，则下载图片
//    private void requestOriImage(IMMessage msg) {
//        if (isOriginImageHasDownloaded(msg)) {
//            onDownloadSuccess(msg);
//            return;
//        }
//        // async download original image
//        onDownloadStart(msg);
//        message = msg; // 下载成功之后，判断是否是同一条消息时需要使用
//        downloadFuture = NIMClient.getService(MsgService.class).downloadAttachment(msg, false);
//    }
//
//    private boolean isOriginImageHasDownloaded(final IMMessage message) {
//        if (message.getAttachStatus() == AttachStatusEnum.transferred &&
//                !TextUtils.isEmpty(((ImageAttachment) message.getAttachment()).getPath())) {
//            return true;
//        }
//        return false;
//    }
//
///** ******************************** 设置图片 *********************************/
//
//    private void setThumbnail(IMMessage msg) {
//        String thumbPath = ((ImageAttachment) msg.getAttachment()).getThumbPath();
//        String path = ((ImageAttachment) msg.getAttachment()).getPath();
//
//        Bitmap bitmap = null;
//        if (!TextUtils.isEmpty(thumbPath)) {
//            bitmap = BitmapDecoder.decodeSampledForDisplay(thumbPath);
//            bitmap = ImageUtil.rotateBitmapInNeeded(thumbPath, bitmap);
//        } else if (!TextUtils.isEmpty(path)) {
//            bitmap = BitmapDecoder.decodeSampledForDisplay(path);
//            bitmap = ImageUtil.rotateBitmapInNeeded(path, bitmap);
//        }
//
//        if (bitmap != null) {
//            image.setImageBitmap(bitmap);
//            return;
//        }
//
//        image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnLoading()));
//    }
//
//    private void setImageView(final IMMessage msg) {
//        //    String path = ((ImageAttachment) msg.getAttachment()).getPath();
//        String path = ((ImageAttachment) msg.getAttachment()).getUrl();
//        if (TextUtils.isEmpty(path)) {
//            image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnLoading()));
//            return;
//        }
//
//        /*Bitmap bitmap = BitmapDecoder.decodeSampledForDisplay(path, false);
//        bitmap = ImageUtil.rotateBitmapInNeeded(path, bitmap);
//        if (bitmap == null) {
//            Toast.makeText(this, R.string.picker_image_error, Toast.LENGTH_LONG).show();
//            image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnFailed()));
//        } else {*/
//        RequestBuilder builder;
//        RequestOptions options = new RequestOptions()
//                .override(getImageMaxEdge(), getImageMinEdge())
//                .fitCenter()
//                .placeholder(R.drawable.nim_image_default)
//                .error(R.drawable.nim_image_default)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .dontAnimate();
//        builder = Glide.with(this.getApplicationContext())
//                .asBitmap()
//                .load(path)
//                .apply(options)
//                ;
//        builder.into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                image.setImageBitmap(resource);
//            }
//        });
//        //  .load(new File(path));
//        //    }
//
//        //  image.setImageBitmap(bitmap);
//
//        //    }
//    }
//    public static int getImageMaxEdge() {
//        return ScreenUtil.screenWidth;
//    }
//
//    public static int getImageMinEdge() {
//        return ScreenUtil.screenHeight;
//    }
//    private int getImageResOnLoading() {
//        return R.drawable.nim_image_default;
//    }
//
//    private int getImageResOnFailed() {
//        return R.drawable.nim_image_download_failed;
//    }
//
///*
//     * ********************************* 下载 ****************************************/
//
//
//    private void registerObservers(boolean register) {
//        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(statusObserver, register);
//    }
//
//    private Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
//        @Override
//        public void onEvent(IMMessage msg) {
//            if (!msg.isTheSame(message) || isDestroyedCompatible()) {
//                return;
//            }
//
//            if (isOriginImageHasDownloaded(msg)) {
//                onDownloadSuccess(msg);
//            } else if (msg.getAttachStatus() == AttachStatusEnum.fail) {
//                onDownloadFailed();
//            }
//        }
//    };
//
//    private void onDownloadStart(final IMMessage msg) {
//        if (TextUtils.isEmpty(((ImageAttachment) msg.getAttachment()).getPath())) {
//            loadingLayout.setVisibility(View.VISIBLE);
//        } else {
//            loadingLayout.setVisibility(View.GONE);
//        }
//        if (mode == MODE_NOMARL) {
//            setThumbnail(msg);
//        }
//    }
//
//    private void onDownloadSuccess(final IMMessage msg) {
//        loadingLayout.setVisibility(View.GONE);
//        if (mode == MODE_NOMARL) {
//            handler.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    setImageView(msg);
//                }
//            });
//        } else if (mode == MODE_GIF) {
//            displaySimpleImage();
//        }
//    }
//
//    private void onDownloadFailed() {
//        loadingLayout.setVisibility(View.GONE);
//        if (mode == MODE_NOMARL) {
//            image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnFailed()));
//        } else if (mode == MODE_GIF) {
//            simpleImageView.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnFailed()));
//        }
//        Toast.makeText(this, R.string.download_picture_fail, Toast.LENGTH_LONG).show();
//    }
//
///*
//     * ***********************************图片点击事件*******************************************/
//
//
//    // 设置图片点击事件
//    protected void onImageViewFound(BaseZoomableImageView imageView) {
//        imageView.setImageGestureListener(new ImageGestureListener() {
//
//            @Override
//            public void onImageGestureSingleTapConfirmed() {
//                onImageViewTouched();
//            }
//
//            @Override
//            public void onImageGestureLongPress() {
//                showWatchPictureAction();
//            }
//
//            @Override
//            public void onImageGestureFlingDown() {
//                finish();
//            }
//        });
//    }
//
//    // 图片单击
//    protected void onImageViewTouched() {
//        finish();
//    }
//
//    // 图片长按
//    protected void showWatchPictureAction() {
//        if (alertDialog.isShowing()) {
//            alertDialog.dismiss();
//            return;
//        }
//        alertDialog.clearData();
//        String path = ((ImageAttachment) message.getAttachment()).getThumbPath();
//        if (TextUtils.isEmpty(path)) {
//            return;
//        }
//        String title;
//        if (!TextUtils.isEmpty(((ImageAttachment) message.getAttachment()).getPath())) {
//            title = getString(R.string.save_to_device);
//            alertDialog.addItem(title, new onSeparateItemClickListener() {
//
//                @Override
//                public void onClick() {
//                    savePicture();
//                }
//            });
//        }
//        alertDialog.show();
//    }
//
//    // 保存图片
//    public void savePicture() {
//        ImageAttachment attachment = (ImageAttachment) message.getAttachment();
//        String path = attachment.getPath();
//        if (TextUtils.isEmpty(path)) {
//            return;
//        }
//
//        String srcFilename = attachment.getFileName();
//        //默认jpg
//        String extension = TextUtils.isEmpty(attachment.getExtension()) ? "jpg" : attachment.getExtension();
//        srcFilename += ("." + extension);
//
//        String picPath = StorageUtil.getSystemImagePath();
//        String dstPath = picPath + srcFilename;
//        if (AttachmentStore.copy(path, dstPath) != -1) {
//            try {
//                ContentValues values = new ContentValues(2);
//                values.put(MediaStore.Images.Media.MIME_TYPE, C.MimeType.MIME_JPEG);
//                values.put(MediaStore.Images.Media.DATA, dstPath);
//                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                Toast.makeText(WatchMessagePictureActivity.this, getString(R.string.picture_save_to), Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                // may be java.lang.UnsupportedOperationException
//                Toast.makeText(WatchMessagePictureActivity.this, getString(R.string.picture_save_fail), Toast.LENGTH_LONG).show();
//            }
//        } else {
//            Toast.makeText(WatchMessagePictureActivity.this, getString(R.string.picture_save_fail), Toast.LENGTH_LONG).show();
//        }
//    }
//}


package com.netease.nim.uikit.business.session.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog.onSeparateItemClickListener;
import com.netease.nim.uikit.common.ui.imageview.BaseZoomableImageView;
import com.netease.nim.uikit.common.ui.imageview.ImageGestureListener;
import com.netease.nim.uikit.common.util.C;
import com.netease.nim.uikit.common.util.file.AttachmentStore;
import com.netease.nim.uikit.common.util.media.BitmapDecoder;
import com.netease.nim.uikit.common.util.media.ImageUtil;
import com.netease.nim.uikit.common.util.storage.StorageUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 查看聊天消息原图
 * Created by huangjun on 2015/3/6.
 * <p>
 * ******************************** 设置图片 *********************************
 * <p>
 * ********************************* 下载 ****************************************
 * <p>
 * ***********************************图片点击事件*******************************************
 * <p>
 * ******************************** 设置图片 *********************************
 * <p>
 * ********************************* 下载 ****************************************
 * <p>
 * ***********************************图片点击事件
 ********************************************/


public class WatchMessagePictureActivity extends UI {

    private static final String TAG = WatchMessagePictureActivity.class.getSimpleName();
    private static final String INTENT_EXTRA_IMAGE = "INTENT_EXTRA_IMAGE";
    private static final String INTENT_EXTRA_MENU = "INTENT_EXTRA_MENU";

    private static final int MODE_NOMARL = 0;
    private static final int MODE_GIF = 1;

    final Bitmap[] bitmapFile = {null};

    private Handler handler;
    private IMMessage message;
    private boolean isShowMenu;
    private List<IMMessage> imageMsgList = new ArrayList<>();
    private int firstDisplayImageIndex = 0;

    private boolean newPageSelected = false;

    private View loadingLayout;
    private BaseZoomableImageView image;
    private ImageView simpleImageView;
    private int mode;
    protected CustomAlertDialog alertDialog;
    private ViewPager imageViewPager;
    private PagerAdapter adapter;
    private AbortableFuture downloadFuture;

    public static void start(Context context, IMMessage message) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_IMAGE, message);
        intent.setClass(context, WatchMessagePictureActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, IMMessage message, boolean isShowMenu) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_IMAGE, message);
        intent.putExtra(INTENT_EXTRA_MENU, isShowMenu);
        intent.setClass(context, WatchMessagePictureActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_watch_picture_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "图片";
//        options.navigateId = R.drawable.nim_actionbar_white_back_icon;
        setToolBar(R.id.toolbar, options);

        handleIntent();

        initActionbar();
        findViews();

        loadMsgAndDisplay();

        handler = new Handler();
        registerObservers(true);
    }

    private void handleIntent() {
        this.message = (IMMessage) getIntent().getSerializableExtra(INTENT_EXTRA_IMAGE);
        mode = ImageUtil.isGif(((ImageAttachment) message.getAttachment()).getExtension()) ? MODE_GIF : MODE_NOMARL;
        setTitle(message);
        isShowMenu = getIntent().getBooleanExtra(INTENT_EXTRA_MENU, true);
    }

    @Override
    protected void onDestroy() {
        registerObservers(false);
        imageViewPager.setAdapter(null);
        if (downloadFuture != null) {
            downloadFuture.abort();
            downloadFuture = null;
        }
        super.onDestroy();
    }

    private void setTitle(IMMessage message) {
        if (message == null) {
            return;
        }
        super.setTitle(String.format("图片发送于%s", TimeUtil.getDateString(message.getTime())));
    }

    private void initActionbar() {
//        TextView menuBtn = findView(R.id.actionbar_menu);
//        if (isShowMenu) {
//            menuBtn.setVisibility(View.VISIBLE);
//            menuBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    WatchPicAndVideoMenuActivity.startActivity(WatchMessagePictureActivity.this, message);
//                }
//            });
//        } else {
//            menuBtn.setVisibility(View.GONE);
//        }
    }

    private void findViews() {
        alertDialog = new CustomAlertDialog(this);
        loadingLayout = findViewById(R.id.loading_layout);

        imageViewPager = (ViewPager) findViewById(R.id.view_pager_image);
        simpleImageView = (ImageView) findViewById(R.id.simple_image_view);

        if (mode == MODE_GIF) {
            simpleImageView.setVisibility(View.VISIBLE);
            simpleImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (isOriginImageHasDownloaded(message)) {
                        showWatchPictureAction();
                    }
                    return true;
                }
            });

            imageViewPager.setVisibility(View.GONE);
        } else if (mode == MODE_NOMARL) {
            simpleImageView.setVisibility(View.GONE);
            imageViewPager.setVisibility(View.VISIBLE);
        }
    }

    // 加载并显示
    private void loadMsgAndDisplay() {
        if (mode == MODE_NOMARL) {
            queryImageMessages();
        } else if (mode == MODE_GIF) {
            displaySimpleImage();
        }
    }

    // 显示单个gif图片
    private void displaySimpleImage() {
        String path = ((ImageAttachment) message.getAttachment()).getPath();
        String thumbPath = ((ImageAttachment) message.getAttachment()).getThumbPath();
        if (!TextUtils.isEmpty(path)) {
            Glide.with(this).asGif().load(new File(path)).into(simpleImageView);
            return;
        }
        if (!TextUtils.isEmpty(thumbPath)) {
            Glide.with(this).asGif().load(new File(thumbPath)).into(simpleImageView);
        }

        if (message.getDirect() == MsgDirectionEnum.In) {
            requestOriImage(message);
        }
    }


    // 查询并显示图片，带viewPager
    private void queryImageMessages() {
        IMMessage anchor = MessageBuilder.createEmptyMessage(message.getSessionId(), message.getSessionType(), 0);
        NIMClient.getService(MsgService.class).queryMessageListByType(MsgTypeEnum.image, anchor, Integer.MAX_VALUE).setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> param) {
                for (int i = 0; i < param.size(); i++) {
                    if (!ImageUtil.isGif(((ImageAttachment) param.get(i).getAttachment()).getExtension())) {
                        if (message.getUuid() == param.get(i).getUuid()) {
                            imageMsgList.add(message);
                        } else {
                            imageMsgList.add(param.get(i));
                        }
                    }
                }
                // imageMsgList.addAll(param);
                Collections.reverse(imageMsgList);
                setDisplayIndex();
                setViewPagerAdapter();
            }

            @Override
            public void onFailed(int code) {
                Log.i(TAG, "query msg by type failed, code:" + code);
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    // 设置第一个选中的图片index
    private void setDisplayIndex() {
        for (int i = 0; i < imageMsgList.size(); i++) {
            IMMessage imageObject = imageMsgList.get(i);
            if (compareObjects(message, imageObject)) {
                firstDisplayImageIndex = i;
                break;
            }
        }
    }

    protected boolean compareObjects(IMMessage t1, IMMessage t2) {
        return (t1.getUuid().equals(t2.getUuid()));
    }

    private void setViewPagerAdapter() {
        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return imageMsgList == null ? 0 : imageMsgList.size();
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View layout = (View) object;
                BaseZoomableImageView iv = (BaseZoomableImageView) layout.findViewById(R.id.watch_image_view);
                iv.clear();
                container.removeView(layout);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return (view == object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ViewGroup layout;
                layout = (ViewGroup) LayoutInflater.from(WatchMessagePictureActivity.this).inflate(R.layout.nim_image_layout_multi_touch, null);
                layout.setBackgroundColor(Color.BLACK);

                container.addView(layout);
                layout.setTag(position);

                if (position == firstDisplayImageIndex) {
                    onViewPagerSelected(position);
                }

                return layout;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        imageViewPager.setAdapter(adapter);
        imageViewPager.setOffscreenPageLimit(2);
        imageViewPager.setCurrentItem(firstDisplayImageIndex);
        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0f && newPageSelected) {
                    newPageSelected = false;
                    onViewPagerSelected(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                newPageSelected = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void onViewPagerSelected(int position) {
        if (downloadFuture != null) {
            downloadFuture.abort();
            downloadFuture = null;
        }
        setTitle(imageMsgList.get(position));
        updateCurrentImageView(position);
        onImageViewFound(image);
    }

    // 初始化每个view的image
    protected void updateCurrentImageView(final int position) {
        View currentLayout = imageViewPager.findViewWithTag(position);
        if (currentLayout == null) {
            ViewCompat.postOnAnimation(imageViewPager, new Runnable() {

                @Override
                public void run() {
                    updateCurrentImageView(position);
                }
            });
            return;
        }
        image = (BaseZoomableImageView) currentLayout.findViewById(R.id.watch_image_view);

        requestOriImage(imageMsgList.get(position));

    }

    // 若图片已下载，直接显示图片；若图片未下载，则下载图片
    private void requestOriImage(IMMessage msg) {
        ImageAttachment imageAttachment = (ImageAttachment) msg.getAttachment();
        imageAttachment.setUrl( imageAttachment.getUrl().replace("https","http"));
        if (CommonUtil.role == CommonUtil.SELLER) {
            Map<String, Object> map = msg.getRemoteExtension();
            if (map != null) {
                String wxMsgId = (String) map.get("wxMsgId");
                if (!TextUtils.isEmpty(wxMsgId)) {
                    onDownloadSuccess(msg);
                    return;
                }
            }
        }
        if (isOriginImageHasDownloaded(msg)) {
            onDownloadSuccess(msg);
            return;
        }

        // async download original image
        onDownloadStart(msg);
        message = msg; // 下载成功之后，判断是否是同一条消息时需要使用
        downloadFuture = NIMClient.getService(MsgService.class).downloadAttachment(msg, false);
    }

    private boolean isOriginImageHasDownloaded(final IMMessage message) {
        if (message.getAttachStatus() == AttachStatusEnum.transferred &&
                !TextUtils.isEmpty(((ImageAttachment) message.getAttachment()).getPath())) {
            return true;
        }

        return false;
    }
//
//*
// * ******************************** 设置图片 *********************************


    private void setThumbnail(IMMessage msg) {
        String thumbPath = ((ImageAttachment) msg.getAttachment()).getThumbPath();
        String path = ((ImageAttachment) msg.getAttachment()).getPath();

        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(thumbPath)) {
            bitmap = BitmapDecoder.decodeSampledForDisplay(thumbPath);
            bitmap = ImageUtil.rotateBitmapInNeeded(thumbPath, bitmap);
        } else if (!TextUtils.isEmpty(path)) {
            bitmap = BitmapDecoder.decodeSampledForDisplay(path);
            bitmap = ImageUtil.rotateBitmapInNeeded(path, bitmap);
        }

        if (bitmap != null) {
            image.setImageBitmap(bitmap);
            return;
        }

        image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnLoading()));
    }

    private void setImageView(final IMMessage msg) {
        String path = null;
        path = ((ImageAttachment) msg.getAttachment()).getPath();
        if (CommonUtil.role == CommonUtil.SELLER) {
            Map<String, Object> map = msg.getRemoteExtension();
            if (map != null) {
                String wxMsgId = (String) map.get("wxMsgId");
                if (!TextUtils.isEmpty(wxMsgId)) {
                    path = ((ImageAttachment) msg.getAttachment()).getUrl();
                    returnBitMap(path);
//                    image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnFailed()));
                    return;
                }
            }
        }
        if (TextUtils.isEmpty(path)) {
            image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnLoading()));
            return;
        }

        Bitmap bitmap = BitmapDecoder.decodeSampledForDisplay(path, false);
        bitmap = ImageUtil.rotateBitmapInNeeded(path, bitmap);
        if (bitmap == null) {
            Toast.makeText(this, R.string.picker_image_error, Toast.LENGTH_LONG).show();
            image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnFailed()));
        } else {
            image.setImageBitmap(bitmap);
        }
    }

    private Handler handlerFile = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            image.setImageBitmap(bitmapFile[0]);
        }
    };

    /*
    *    get image from network
    *    @param [String]imageURL
    *    @return [BitMap]image
    */
    public void returnBitMap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmapFile[0] = BitmapFactory.decodeStream(is);
                    //这是一个一步请求，不能直接返回获取，要不然永远为null
                    //在这里得到BitMap之后记得使用Hanlder或者EventBus传回主线程，不过现在加载图片都是用框架了，很少有转化为Bitmap的需求
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handlerFile.sendEmptyMessage(1);
            }
        }).start();
    }

    private int getImageResOnLoading() {
        return R.drawable.nim_image_default;
    }

    private int getImageResOnFailed() {
        return R.drawable.nim_image_download_failed;
    }

//*
// * ********************************* 下载 ****************************************


    private void registerObservers(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(statusObserver, register);
    }

    private Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage msg) {
            if (!msg.isTheSame(message) || isDestroyedCompatible()) {
                return;
            }

            if (isOriginImageHasDownloaded(msg)) {
                onDownloadSuccess(msg);
            } else if (msg.getAttachStatus() == AttachStatusEnum.fail) {
                onDownloadFailed();
            }
        }
    };

    private void onDownloadStart(final IMMessage msg) {
        if (TextUtils.isEmpty(((ImageAttachment) msg.getAttachment()).getPath())) {
            loadingLayout.setVisibility(View.VISIBLE);
        } else {
            loadingLayout.setVisibility(View.GONE);
        }
        if (mode == MODE_NOMARL) {
            setThumbnail(msg);
        }
    }

    private void onDownloadSuccess(final IMMessage msg) {
        loadingLayout.setVisibility(View.GONE);
        if (mode == MODE_NOMARL) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    setImageView(msg);
                }
            });
        } else if (mode == MODE_GIF) {
            displaySimpleImage();
        }
    }

    private void onDownloadFailed() {
        try {
            loadingLayout.setVisibility(View.GONE);
            if (mode == MODE_NOMARL) {
                image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnFailed()));
            } else if (mode == MODE_GIF) {
                simpleImageView.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(getImageResOnFailed()));
            }
            Toast.makeText(this, R.string.download_picture_fail, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//*
// * ***********************************图片点击事件*******************************************


    // 设置图片点击事件
    protected void onImageViewFound(BaseZoomableImageView imageView) {
        imageView.setImageGestureListener(new ImageGestureListener() {

            @Override
            public void onImageGestureSingleTapConfirmed() {
                onImageViewTouched();
            }

            @Override
            public void onImageGestureLongPress() {
                showWatchPictureAction();
            }

            @Override
            public void onImageGestureFlingDown() {
                finish();
            }
        });
    }

    // 图片单击
    protected void onImageViewTouched() {
        finish();
    }

    // 图片长按
    protected void showWatchPictureAction() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
            return;
        }
        alertDialog.clearData();
        String path = ((ImageAttachment) message.getAttachment()).getThumbPath();
        if (TextUtils.isEmpty(path)) {
            return;
        }
        String title;
        if (!TextUtils.isEmpty(((ImageAttachment) message.getAttachment()).getPath())) {
            title = getString(R.string.save_to_device);
            alertDialog.addItem(title, new onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    savePicture();
                }
            });
        }
        alertDialog.show();
    }

    // 保存图片
    public void savePicture() {
        ImageAttachment attachment = (ImageAttachment) message.getAttachment();
        String path = attachment.getPath();
        if (TextUtils.isEmpty(path)) {
            return;
        }

        String srcFilename = attachment.getFileName();
        //默认jpg
        //  String extension = TextUtils.isEmpty(attachment.getExtension()) ? "jpg" : attachment.getExtension();
        srcFilename += ("." + "jpg");

        String picPath = StorageUtil.getSystemImagePath();
        String dstPath = picPath + srcFilename;
        if (AttachmentStore.copy(path, dstPath) != -1) {
            try {
                ContentValues values = new ContentValues(2);
                values.put(MediaStore.Images.Media.MIME_TYPE, C.MimeType.MIME_JPEG);
                values.put(MediaStore.Images.Media.DATA, dstPath);
                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Toast.makeText(WatchMessagePictureActivity.this, getString(R.string.picture_save_to), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // may be java.lang.UnsupportedOperationException
                Toast.makeText(WatchMessagePictureActivity.this, getString(R.string.picture_save_fail), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(WatchMessagePictureActivity.this, getString(R.string.picture_save_fail), Toast.LENGTH_LONG).show();
        }
    }
}

