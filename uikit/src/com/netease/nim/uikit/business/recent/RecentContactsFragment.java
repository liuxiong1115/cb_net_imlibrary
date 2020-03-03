
package com.netease.nim.uikit.business.recent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.VisitorActivity;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.business.recent.adapter.SwipeItemLayout;
import com.netease.nim.uikit.business.recent.model.UserInfoExtension;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.badger.Badger;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog.onSeparateItemClickListener;

/**
 * 最近联系人列表(会话列表)
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class RecentContactsFragment extends TFragment {

    // 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段
    public static final long RECENT_TAG_STICKY = 0x0000000000000001; // 联系人置顶tag

    // view
    private RecyclerView recyclerView;

    private View emptyBg;

    private TextView emptyHint;

    // data
    private List<RecentContact> items;

    private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）

    public RecentContactAdapter adapter;

    private boolean msgLoaded = false;

    private RecentContactsCallback callback;

    private UserInfoObserver userInfoObserver;
    public View visitorLayout;
    public DropFake dropFake;

    public int visiUnreadNum = 0;

    public View view;
    public static RecentContactsFragment instance() {
        RecentContactsFragment recentContactsFragment = new RecentContactsFragment();
        return recentContactsFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = View.inflate(getContext(),R.layout.recent_head,null);
        findViews();
        initMessageList();
        requestMessages(true);
        registerObservers(true);
        registerDropCompletedListener(true);
        registerOnlineStateChangeListener(true);
        if (CommonUtil.role == CommonUtil.SELLER) {
            addHeaderView(view);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nim_recent_contacts, container, false);
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;
        if (empty) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
        if (CommonUtil.role == CommonUtil.STUD) {
            emptyHint.setHint("还没有消息哦，快去找我们课程顾问聊聊吧");
        } else {
            emptyHint.setHint("暂无消息");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerDropCompletedListener(false);
        registerOnlineStateChangeListener(false);
    }


    public void addHeaderView(View view) {
        adapter.addHeaderView(view);
    }


    /**
     * 查找页面控件
     */
    private void findViews() {
        recyclerView = findView(R.id.recycler_view);
        emptyBg = findView(R.id.emptyBg);
        emptyHint = findView(R.id.message_list_empty_hint);
        visitorLayout = view.findViewById(R.id.visitor);
        dropFake = view.findViewById(R.id.unread_number);
        dropFake.setVisibility(View.GONE);

        visitorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VisitorActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    /**
     * 未读数
     *
     * @param unread
     * @return
     */
    protected String unreadCountShowRule(int unread) {
        unread = Math.min(unread, 99);
        return String.valueOf(unread);
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {

        if (CommonUtil.role != CommonUtil.SELLER) {
            visitorLayout.setVisibility(View.GONE);
        }
        items = new ArrayList<>();
        cached = new HashMap<>(3);

        // adapter
        adapter = new RecentContactAdapter(recyclerView, items);
        initCallBack();
        adapter.setCallback(callback);
        // recyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(touchListener);
        //侧滑监听
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        // ios style
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        // drop listener
        DropManager.getInstance().setDropListener(new DropManager.IDropListener() {
            @Override
            public void onDropBegin() {
                touchListener.setShouldDetectGesture(false);
            }

            @Override
            public void onDropEnd() {
                touchListener.setShouldDetectGesture(true);
            }
        });
    }

    private void initCallBack() {
        if (callback != null) {
            return;
        }
        callback = new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded(List<RecentContact> items) {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.Team) {
                    NimUIKit.startTeamSession(getActivity(), recent.getContactId());
                } else if (recent.getSessionType() == SessionTypeEnum.P2P) {
                    NimUIKit.startP2PSession(getActivity(), recent.getContactId());
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        };
    }

    private SimpleClickListener<RecentContactAdapter> touchListener = new SimpleClickListener<RecentContactAdapter>() {
        @Override
        public void onItemClick(RecentContactAdapter adapter, View view, int position) {
            if (callback != null) {
                RecentContact recent = adapter.getItem(position);
                callback.onItemClick(recent);
            }
        }

        @Override
        public void onItemLongClick(RecentContactAdapter adapter, View view, int position) {
            showLongClickMenu(adapter.getItem(position), position);
        }

        @Override
        public void onItemChildClick(RecentContactAdapter adapter, View view, int position) {
            RecentContact recentContact = adapter.getItem(position);
            if (view.getId() == R.id.delete) {
                if (CommonUtil.role == CommonUtil.SELLER) {
                    CommonUtil.DeletedItemListener listener = CommonUtil.delectedItemListener;
                    if (listener != null) {
                        listener.deleted(adapter, position, recentContact);
                        refreshMessages(true);
                    }
                } else {
                    // 删除会话，删除后，消息历史被一起删除
                    NIMClient.getService(MsgService.class).deleteRecentContact2(recentContact.getContactId(), recentContact.getSessionType());
                    //   NIMClient.getService(MsgService.class).clearChattingHistory(recentContact.getContactId(), recentContact.getSessionType());
                    adapter.remove(position);
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            refreshMessages(true);
                        }
                    });
                }
            }
        }

        @Override
        public void onItemChildLongClick(RecentContactAdapter adapter, View view, int position) {

        }
    };

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            notifyDataSetChanged();
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void showLongClickMenu(final RecentContact recent, final int position) {
        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        alertDialog.setTitle(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));
        String title = getString(R.string.main_msg_list_delete_chatting);
        alertDialog.addItem(title, new onSeparateItemClickListener() {
            @Override
            public void onClick() {
                // 删除会话，删除后，消息历史被一起删除
                NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
                adapter.remove(position);

                postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        refreshMessages(true);
                    }
                });
            }
        });
        String titleStr = null;
        UserInfoExtension userInfoExtension = null;
        NimUserInfo userInfo = (NimUserInfo) NimUIKitImpl.getUserInfoProvider().getUserInfo(CommonUtil.userAccount);
        if (userInfo != null) {
            String extension = userInfo.getExtension();
            if (!TextUtils.isEmpty(extension)) {
                userInfoExtension = JSON.parseObject(extension.toString(), UserInfoExtension.class);
                // 先比较置顶tag
                if (userInfoExtension != null && userInfoExtension.getToplist() != null) {
                    for (String s : userInfoExtension.getToplist()) {
                        if (s.equals(recent.getContactId())) {
                            titleStr = s;
                        }
                    }
                }
            }
        }

        //    title = (CommonUtil.isTagSet(recent, RECENT_TAG_STICKY) ? getString(R.string.main_msg_list_clear_sticky_on_top) : getString(R.string.main_msg_list_sticky_on_top));
        title = titleStr == null ? getString(R.string.main_msg_list_sticky_on_top) : getString(R.string.main_msg_list_clear_sticky_on_top);
        final String finalTitleStr = titleStr;
        if (userInfoExtension == null) {
            userInfoExtension = new UserInfoExtension();
        }
        final UserInfoExtension finalUserInfoExtension = userInfoExtension;
        alertDialog.addItem(title, new onSeparateItemClickListener() {
            @Override
            public void onClick() {
                if (finalTitleStr == null) {
                    if (finalUserInfoExtension.getToplist() == null) {
                        List<String> list = new ArrayList<>();
                        list.add(0, recent.getContactId());
                        finalUserInfoExtension.setToplist(list);
                    } else {
                        finalUserInfoExtension.getToplist().add(0, recent.getContactId());
                    }
                } else {
                    for (int i = 0; i < finalUserInfoExtension.getToplist().size(); i++) {
                        if (recent.getContactId().equals(finalUserInfoExtension.getToplist().get(i))) {
                            finalUserInfoExtension.getToplist().remove(i);
                        }
                    }
                }
                Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
                fields.put(UserInfoFieldEnum.EXTEND, new Gson().toJson(finalUserInfoExtension));
                NIMClient.getService(UserService.class).updateUserInfo(fields).setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int code, Void result, Throwable exception) {
                        if (code == ResponseCode.RES_SUCCESS) {
                            NIMClient.getService(MsgService.class).updateRecent(recent);
                            refreshMessages(false);
                        } else {
                            if (exception != null) {

                            }
                        }
                        if (callback != null) {
                        }
                    }
                });

//                if (CommonUtil.isTagSet(recent, RECENT_TAG_STICKY)) {
//                    CommonUtil.removeTag(recent, RECENT_TAG_STICKY);
//                } else {
//                    CommonUtil.addTag(recent, RECENT_TAG_STICKY);
//                }
//                NIMClient.getService(MsgService.class).updateRecent(recent);
//                refreshMessages(false);
            }
        });

        if (CommonUtil.role == CommonUtil.SELLER || CommonUtil.role == CommonUtil.TEAC) {  // 复制订单号
            if (recent.getSessionType() == SessionTypeEnum.Team) {
                alertDialog.addItem("复制订单号", new onSeparateItemClickListener() {
                    @Override
                    public void onClick() {
                        StringUtil.copyToClipBoard(getContext(), UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));
                    }
                });
            }
        }
       /* alertDialog.addItem("删除该聊天（仅服务器）", new onSeparateItemClickListener() {
            @Override
            public void onClick() {
                NIMClient.getService(MsgService.class)
                        .deleteRoamingRecentContact(recent.getContactId(), recent.getSessionType())
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                Toast.makeText(getActivity(), "delete success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(int code) {
                                Toast.makeText(getActivity(), "delete failed, code:" + code, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onException(Throwable exception) {

                            }
                        });
            }
        });*/
        alertDialog.show();
    }

    private List<RecentContact> loadedRecents;

    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        getHandler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts(200).setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        if (CommonUtil.role == CommonUtil.SELLER) {
                            List<RecentContact> list = new ArrayList<>();
                            List<RecentContact> visiList = new ArrayList<>();
                            int unreadNum = 0;
                            for (int i = 0; i < recents.size(); i++) {
                                if (!recents.get(i).getContactId().startsWith("visi")) {
                                    list.add(recents.get(i));
                                } else {
                                    visiList.add(recents.get(i));
                                    Log.e("recent", recents.get(i).getContactId());
                                }
                            }
                            loadedRecents = list;
                            for (int i = 0; i < visiList.size(); i++) {
                                unreadNum += visiList.get(i).getUnreadCount();
                            }
                            visiUnreadNum = unreadNum;
                            dropFake.setVisibility(visiUnreadNum > 0 ? View.VISIBLE : View.GONE);
                            dropFake.setText(unreadCountShowRule(visiUnreadNum));
                        } else {
                            loadedRecents = recents;
                        }
                        // 初次加载，更新离线的消息中是否有@我的消息
                        for (RecentContact loadedRecent : loadedRecents) {
                            if (loadedRecent.getSessionType() == SessionTypeEnum.Team) {
                                updateOfflineContactAited(loadedRecent);
                            }
                        }
                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        //
                        msgLoaded = true;
                        if (isAdded()) {
                            onRecentContactsLoaded();
                        }
                    }
                });
            }
        }, delay ? 250 : 0);
    }

    private void onRecentContactsLoaded() {
        items.clear();
        if (loadedRecents != null) {
            items.addAll(loadedRecents);
            loadedRecents = null;
        }
        refreshMessages(true);

        if (callback != null) {
            callback.onRecentContactsLoaded(items);
        }
    }

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();

        if (unreadChanged) {

            // 方式一：累加每个最近联系人的未读（快）

            int unreadNum = 0;
            for (RecentContact r : items) {
                if (CommonUtil.role == CommonUtil.SELLER) {
                    if (r.getSessionType() == SessionTypeEnum.Team) {
                        Team team = NimUIKit.getTeamProvider().getTeamById(r.getContactId());
                        if (team != null) {
                            if (team.getMessageNotifyType() == TeamMessageNotifyTypeEnum.Mute) {
                                continue;
                            }
                        }
                    }
                    unreadNum += r.getUnreadCount();
                } else {
                    unreadNum += r.getUnreadCount();
                }
            }

            // 方式二：直接从SDK读取（相对慢）
            //int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();

            if (callback != null) {
                callback.onUnreadCountChange(unreadNum + visiUnreadNum);
            }
            Badger.updateBadgerCount(unreadNum + visiUnreadNum);
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            NimUserInfo userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(CommonUtil.userAccount);
            if (userInfo != null) {
                String extension = userInfo.getExtension();
                if (!TextUtils.isEmpty(extension)) {
                    UserInfoExtension userInfoExtension = JSON.parseObject(extension.toString(), UserInfoExtension.class);
                    // 先比较置顶tag
                    if (userInfoExtension != null && userInfoExtension.getToplist() != null) {
                        boolean tag1 = false, tag2 = false;
                        for (String s : userInfoExtension.getToplist()) {
                            if (s.equals(o1.getContactId())) {
                                tag1 = true;
                            }
                            if (s.equals(o2.getContactId())) {
                                tag2 = true;
                            }
                        }
                        if (tag1 && tag2) {
                            long time = o1.getTime() - o2.getTime();
                            return time == 0 ? 0 : (time > 0 ? -1 : 1);
                        } else if (tag1) {
                            return -1;
                        } else if (tag2) {
                            return 1;
                        }
                    }
                }
            }
            long time = o1.getTime() - o2.getTime();
            return time == 0 ? 0 : (time > 0 ? -1 : 1);
//             先比较置顶tag
//            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
//            if (sticky != 0) {
//                return sticky > 0 ? -1 : 1;
//            } else {
//                long time = o1.getTime() - o2.getTime();
//                return time == 0 ? 0 : (time > 0 ? -1 : 1);
//            }
        }

    };

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, register);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);

        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        if (register) {
            // 注册/注销观察者
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
    }

    private void registerDropCompletedListener(boolean register) {
        if (register) {
            DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
        } else {
            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
        }
    }

    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                for (IMMessage imMessage : imMessages) {
                    if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
                        continue;
                    }
                    Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
                    if (cacheMessageSet == null) {
                        cacheMessageSet = new HashSet<>();
                        cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
                    }
                    cacheMessageSet.add(imMessage);
                }
            }
        }
    };

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            if (!DropManager.getInstance().isTouchable()) {
                // 正在拖拽红点，缓存数据
                for (RecentContact r : recentContacts) {
                    cached.put(r.getContactId(), r);
                }

                return;
            }
            if (CommonUtil.role == CommonUtil.SELLER) {
                List<RecentContact> list = new ArrayList<>();
                List<RecentContact> visiList = new ArrayList<>();
                for (int i = 0; i < recentContacts.size(); i++) {
                    if (!recentContacts.get(i).getContactId().startsWith("visi")) {
                        list.add(recentContacts.get(i));
                    } else {
                        visiList.add(recentContacts.get(i));
                    }
                }
                int unreadNum = 0;
                for (int i = 0; i < visiList.size(); i++) {
                    unreadNum += visiList.get(i).getUnreadCount();
                }
                visiUnreadNum = unreadNum;
                dropFake.setVisibility(visiUnreadNum > 0 ? View.VISIBLE : View.GONE);
                dropFake.setText(unreadCountShowRule(visiUnreadNum));
                onRecentContactChanged(list);
            } else {
                onRecentContactChanged(recentContacts);
            }

        }
    };

    private void onRecentContactChanged(List<RecentContact> recentContacts) {
        int index;
        for (RecentContact r : recentContacts) {
            index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (r.getContactId().equals(items.get(i).getContactId())
                        && r.getSessionType() == (items.get(i).getSessionType())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                items.remove(index);
            }
            items.add(r);
            if (r.getSessionType() == SessionTypeEnum.Team && cacheMessages.get(r.getContactId()) != null) {
                TeamMemberAitHelper.setRecentContactAited(r, cacheMessages.get(r.getContactId()));
            }
        }

        cacheMessages.clear();

        refreshMessages(true);
    }

    DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {
        @Override
        public void onCompleted(Object id, boolean explosive) {
            if (cached != null && !cached.isEmpty()) {
                // 红点爆裂，已经要清除未读，不需要再刷cached
                if (explosive) {
                    if (id instanceof RecentContact) {
                        RecentContact r = (RecentContact) id;
                        cached.remove(r.getContactId());
                    } else if (id instanceof String && ((String) id).contentEquals("0")) {
                        cached.clear();
                    }
                }

                // 刷cached
                if (!cached.isEmpty()) {
                    List<RecentContact> recentContacts = new ArrayList<>(cached.size());
                    recentContacts.addAll(cached.values());
                    cached.clear();

                    onRecentContactChanged(recentContacts);
                }
            }
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };

    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {
        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {

        }
    };

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }
        return -1;
    }

    protected void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyItemChanged(index);
            }
        });
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    //TODO 个人信息更改  防止recyclerivew一直刷新
                    for (String s : accounts) {
                        if (s.equals(CommonUtil.userAccount)) {
                            refreshMessages(false);
                            return;
                        }
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
        }
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

    private void updateOfflineContactAited(final RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team
                || recentContact.getUnreadCount() <= 0) {
            return;
        }

        // 锚点
        List<String> uuid = new ArrayList<>(1);
        uuid.add(recentContact.getRecentMessageId());

        List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);

        if (messages == null || messages.size() < 1) {
            return;
        }
        final IMMessage anchor = messages.get(0);

        // 查未读消息
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                recentContact.getUnreadCount() - 1, false).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && result != null) {
                    result.add(0, anchor);
                    Set<IMMessage> messages = null;
                    // 过滤存在的@我的消息
                    for (IMMessage msg : result) {
                        if (TeamMemberAitHelper.isAitMessage(msg)) {
                            if (messages == null) {
                                messages = new HashSet<>();
                            }
                            messages.add(msg);
                        }
                    }
                    // 更新并展示
                    if (messages != null) {
                        TeamMemberAitHelper.setRecentContactAited(recentContact, messages);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
