package com.netease.nim.uikit.business.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.ui.widget.MyToolbar;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nim.uikit.model.ClassbroUserInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import org.apache.lucene.util.LongValues;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity {

    private boolean isResume = false;
    public static WeakReference<P2PMessageActivity> instance;
    private DrawerLayout drawerLayout;

    public AppCompatAutoCompleteTextView nickName, tel, wxNo,  account, password;
    public AppCompatTextView activaUseer;
    public int isActiva = 0;  //是否激活  0--未激活  1--已激活
    public MyToolbar toolbar;
    public Spinner country, school, major, grade, education;
    public String session;
    private boolean isFrist = true;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = new WeakReference<P2PMessageActivity>(this);
        session = sessionId;
        initView();
        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        displayOnlineState();
        registerObservers(true);
        registerOnlineStateChangeListener(true);
        initData();
        setListener();

        CommonUtil.AddUserInfoListener listener = CommonUtil.addUserInfoListener;
        if (listener != null) {
            listener.addUserInfo(this);
        }
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawerlayout);
        nickName = findViewById(R.id.userName);
        tel = findViewById(R.id.userTel);
        wxNo = findViewById(R.id.userWx);
        country = findViewById(R.id.userCountry);
        school = findViewById(R.id.userSchool);
        major = findViewById(R.id.userMajor);
        grade = findViewById(R.id.userGrade);
        education = findViewById(R.id.userEducation);
        account = findViewById(R.id.userAccount);
        password = findViewById(R.id.userPassword);
        activaUseer = findViewById(R.id.activaUser);
        toolbar = findViewById(R.id.toolbar);
    }

    private void initData() {
    /*    //初始化年级
        List<String> list = new ArrayList<>();
        list.add("1年");
        list.add("2年");
        list.add("3年");
        list.add("4年");
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(adapter);*/
        if (CommonUtil.role == CommonUtil.SELLER) {
            if (!CommonUtil.classbroRobot.equals(sessionId) || !CommonUtil.systemNotify.equals(sessionId)) {
                if (sessionId.toLowerCase().startsWith("visi")) {
                    toolbar.setMenuText("结束咨询");
                }
            }
        }

        //如果是学生 禁止滑动侧边栏
        if (CommonUtil.role == CommonUtil.TEAC || CommonUtil.role == CommonUtil.STUD) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            if (!TextUtils.isEmpty(sessionId)) {
            if (sessionId.startsWith("stud")) {
                NimUserInfo nimUserInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(sessionId);
                if (nimUserInfo != null) {
                    String content = nimUserInfo.getExtension();
                    if (!TextUtils.isEmpty(content)) {
                        try {
                            Log.e("userInfo", content.toString());
                            org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                            Integer activa = jsonObject.optInt("activa");
                            Integer type = jsonObject.optInt("isInternal");
                            if (type == 0) {  //内部
                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                isActiva = 1;
                            } else {
                                if (activa == 0) { //未激活
                                    toolbar.setMenuDrawable(getResources().getDrawable(R.drawable.action_bar_black_more_icon));
                                    isActiva = activa;
                                    setUserInfo();
                                } else{
                                    isActiva = 1;
                                }
                            }
                        } catch (Exception e) {
                            /*toolbar.setMenuDrawable(getResources().getDrawable(R.drawable.action_bar_black_more_icon));
                            setUserInfo();*/
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (isActiva != 0) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
        }
    }

    /**
     * 创建数据库  保存数据
     */
    public void setUserInfo() {
        if (!TextUtils.isEmpty(sessionId)) {
                String substring = sessionId.substring(4, sessionId.length());
                List<ClassbroUserInfo> classbroUserInfos = ClassbroUserInfo.find(ClassbroUserInfo.class, "user_id=?", substring);
                if (classbroUserInfos == null || classbroUserInfos.size() == 0) {
                    ClassbroUserInfo classbroUserInfo = new ClassbroUserInfo();
                    classbroUserInfo.setUserId(Long.valueOf(substring));
                    classbroUserInfo.save();
                } else {
                    ClassbroUserInfo info = classbroUserInfos.get(0);
                    nickName.setText(info.getStudentName() == null ? "" : info.getStudentName());
                    tel.setText(info.getMobile() == null ? "" : info.getMobile());
                    wxNo.setText(info.getWxAccount() == null ? "" : info.getWxAccount());
                    country.setPrompt(info.getCountry() == null ? "请选择国家" : info.getCountry());
                    school.setPrompt(info.getSchool() == null ? "请选择学校" : info.getSchool());
                    major.setPrompt(info.getMajor() == null ? "请选择专业" : info.getMajor());
                    grade.setPrompt(info.getGrade() == null ? "请选择年级" : info.getGrade());
                    education.setPrompt(info.getEducation()== null ? "请选择学历": info.getEducation());
                    account.setText(info.getSchoolAccount() == null ? "" : info.getSchoolAccount());
                    password.setText(info.getSchoolPws() == null ? "" : info.getSchoolPws());
                }
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
    }

    private void setListener() {
        //名称
        TextWatcher nameText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<ClassbroUserInfo> classbroUserInfo = ClassbroUserInfo.find(ClassbroUserInfo.class, "user_id=?", sessionId.substring(4, sessionId.length()));
                if (classbroUserInfo == null) {
                    ClassbroUserInfo classbroUserInfo1 = new ClassbroUserInfo();
                    classbroUserInfo1.setUserId(Long.valueOf(sessionId.substring(4,sessionId.length())));
                    classbroUserInfo1.setStudentName(nickName.getText().toString());
                    classbroUserInfo1.save();
                } else {
                    classbroUserInfo.get(0).setStudentName(nickName.getText().toString());
                    classbroUserInfo.get(0).save();
                }
            }
        };
        nickName.addTextChangedListener(nameText);
        //手机号
        final TextWatcher telText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<ClassbroUserInfo> classbroUserInfo = ClassbroUserInfo.find(ClassbroUserInfo.class, "user_id=?", sessionId.substring(4, sessionId.length()));
                if (classbroUserInfo == null) {
                    ClassbroUserInfo classbroUserInfo1 = new ClassbroUserInfo();
                    classbroUserInfo1.setUserId(Long.valueOf(sessionId.substring(4,sessionId.length())));
                    classbroUserInfo1.setMobile(tel.getText().toString());
                    classbroUserInfo1.save();
                } else {
                    classbroUserInfo.get(0).setMobile(tel.getText().toString());
                    classbroUserInfo.get(0).save();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        tel.addTextChangedListener(telText);
        //微信号
        TextWatcher wxText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<ClassbroUserInfo> classbroUserInfo = ClassbroUserInfo.find(ClassbroUserInfo.class, "user_id=?", sessionId.substring(4, sessionId.length()));
                if (classbroUserInfo == null) {
                    ClassbroUserInfo classbroUserInfo1 = new ClassbroUserInfo();
                    classbroUserInfo1.setUserId(Long.valueOf(sessionId.substring(4,sessionId.length())));
                    classbroUserInfo1.setWxAccount(wxNo.getText().toString());
                    classbroUserInfo1.save();
                } else {
                    classbroUserInfo.get(0).setWxAccount(wxNo.getText().toString());
                    classbroUserInfo.get(0).save();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        wxNo.addTextChangedListener(wxText);
        //账号
        TextWatcher accountText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<ClassbroUserInfo> classbroUserInfo = ClassbroUserInfo.find(ClassbroUserInfo.class, "user_id=?", sessionId.substring(4, sessionId.length()));
                if (classbroUserInfo == null) {
                    ClassbroUserInfo classbroUserInfo1 = new ClassbroUserInfo();
                    classbroUserInfo1.setUserId(Long.valueOf(sessionId.substring(4,sessionId.length())));
                    classbroUserInfo1.setSchoolAccount(account.getText().toString());
                    classbroUserInfo1.save();
                } else {
                    classbroUserInfo.get(0).setSchoolAccount(account.getText().toString());
                    classbroUserInfo.get(0).save();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        account.addTextChangedListener(accountText);
        //密码
        TextWatcher passwordText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<ClassbroUserInfo> classbroUserInfo = ClassbroUserInfo.find(ClassbroUserInfo.class, "user_id=?", sessionId.substring(4, sessionId.length()));
                if (classbroUserInfo == null) {
                    ClassbroUserInfo classbroUserInfo1 = new ClassbroUserInfo();
                    classbroUserInfo1.setUserId(Long.valueOf(sessionId.substring(4,sessionId.length())));
                    classbroUserInfo1.setSchoolPws(password.getText().toString());
                    classbroUserInfo1.save();
                } else {
                    classbroUserInfo.get(0).setSchoolPws(password.getText().toString());
                    classbroUserInfo.get(0).save();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        password.addTextChangedListener(passwordText);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.e("11", "1111");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (isActiva == 1) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
                Log.e("11", "2222");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.e("11", "3333");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.e("11", newState + "");
            }
        });

        toolbar.setOnOptionItemClickListener(new MyToolbar.OnOptionItemClickListener() {
            @Override
            public void onOptionItemClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
       /* grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<ClassbroUserInfo> classbroUserInfos = ClassbroUserInfo.find(ClassbroUserInfo.class, "user_id=?", sessionId.substring(4, sessionId.length()));
                if (isFrist) {
                    isFrist = false;
                    if (classbroUserInfos != null) {
                        grade.setPrompt(classbroUserInfos.get(0).getGrade());
                    } else {
                        grade.setPrompt("请选择");
                    }
                } else {
                    String s = parent.getItemAtPosition(position).toString(); //这里就是将条目所包含的字符串赋给s
                    grade.setPrompt(s);
                    if (classbroUserInfos == null) {
                        ClassbroUserInfo userInfo = new ClassbroUserInfo();
                        userInfo.setUserId(Long.valueOf(sessionId.substring(4, sessionId.length())));
                        userInfo.setGrade(s);
                        userInfo.setSchoolYear(position + 1);
                        userInfo.save();
                    } else {
                        ClassbroUserInfo userInfo = classbroUserInfos.get(0);
                        userInfo.setGrade(s);
                        userInfo.setSchoolYear(position + 1);
                        userInfo.save();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerOnlineStateChangeListener(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    private void requestBuddyInfo() {
        setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        if (CommonUtil.role == CommonUtil.SELLER) {
            NimUserInfo userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(sessionId);
            if (userInfo == null) {
                return;
            }
            try {
                String content = userInfo.getExtension();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                org.json.JSONObject jsonObject = new org.json.JSONObject(content);
                String source = jsonObject.getString("wxNo");
                MyToolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setSubtitleVisible(true);
                toolbar.setSubtitleTextSize(14);
                toolbar.setSubtitleTextColor(getResources().getColor(R.color.color_aaaaaa_content_text));
                toolbar.setSubtitle(source == null ? "" : source);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObserver uinfoObserver;

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
                displayOnlineState();
            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
        //setSubTitle(detailContent);
    }

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);

    }

    @Override
    public void menuItemClick(View v) {
        super.menuItemClick(v);
        CommonUtil.MenuDeleteListener listener = CommonUtil.menuDeleteListener;
        if (listener != null) {
            listener.deleted(sessionId);
        }
    }

    @Override
    protected boolean enableSensor() {
        return true;
    }
}
