package com.testlib.netease;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.netease.nim.demo.DemoCache;
import com.netease.nim.demo.config.preference.Preferences;
import com.netease.nim.demo.config.preference.UserPreferences;
import com.netease.nim.demo.login.LoginActivity;
import com.netease.nim.demo.session.extension.CardAttachment;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;

import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_IMAGE_PAGER;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_ONLY_SINGLE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                com.netease.nim.demo.main.activity.MainActivity.start(MainActivity.this);
            }
        });
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickPhoto();
            }
        });
    }


    /**
     * 选择图片
     */
    public void onPickPhoto() {
        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(IS_NEED_IMAGE_PAGER, false);
        intent1.putExtra(IS_ONLY_SINGLE, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    private AbortableFuture<LoginInfo> loginRequest;
    private void login() {
        DialogMaker.showProgressDialog(this, null, getString(com.netease.nim.demo.R.string.logining), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (loginRequest != null) {
                    loginRequest.abort();
                    onLoginDone();
                }
            }
        }).setCanceledOnTouchOutside(false);

        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
//        final String account = "STUD111";
//        final String token = "ca3b942d-864f-4656-b6d6-cd33192dcc95";
//        final String account = "TEAC125";
//        final String token = "6ebaf776-1074-4a1f-8a44-028d2b9dcfe1";
//        final String account = "CRM2";
//        final String token = "38360e6aa7216897fc02732b9ef280e8";
//        final String account = "STUD805";
//        final String token = "1222f909-01a2-457e-b3ae-9911d63f61e6";
//        final String account = "TEAC10065";
//        final String token = "d5d814fd-9917-4419-9e6d-86b71e76cce9";
       /* final String account = "CRM62";
        final String token = "e0fd8d49ce019f1b914d24e4a743989b";*/
//        final String account = "uc1t91";
//        final String token = "9cfbae36106c512a66200073c9618e16";
//        final String account = "crm72";
//        final String token = "59e30eac77ebff7d09579857b5ccfff2";
        final String account = "uc1t101";
        final String token = "531734c25c8bd5d81c1f13b42649a11b";
        // 登录
        loginRequest = NimUIKit.login(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success");

                onLoginDone();

                DemoCache.setAccount(account.toLowerCase());
                saveLoginInfo(account.toLowerCase(), token);

                // 初始化消息提醒配置
                initNotificationConfig();

                // 进入主界面
                com.netease.nim.demo.main.activity.MainActivity.start(MainActivity.this, null);
                finish();
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(MainActivity.this, com.netease.nim.demo.R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(MainActivity.this, com.netease.nim.demo.R.string.login_exception, Toast.LENGTH_LONG).show();
                onLoginDone();
            }
        });
//        CommonUtil.setonGetMediaUrlListener(new CommonUtil.onGetMediaUrlListener() {
//            @Override
//            public void onMediaUrl(IMMessage imMessage, Context context, String wxMsgId) {
//                VideoAttachment fileAttachment = (VideoAttachment) imMessage.getAttachment();
//                fileAttachment.setUrl("http://siyouyunsy-1253559996.cos.ap-guangzhou.myqcloud.com/msg/HVYST/20200206/1133419432832179938_wxid_t9bw330z0dou22_1580978891256_.mp4?sign=q-sign-algorithm%3Dsha1%26q-ak%3DAKIDBi7d3I4UK7iDXkAhQyQsDMNGxY2KmlCY%26q-sign-time%3D1580978891%3B1667292491%26q-key-time%3D1580978891%3B1667292491%26q-header-list%3D%26q-url-param-list%3D%26q-signature%3D506911a5c23cb212d2a5902f499b696361a045bd");
//                CommonUtil.onDealVideoMediaUrlListener listener = CommonUtil.dealVideoMediaUrlListener;
//                if (listener != null) {
//                    listener.onDealVideoMediaUrl(imMessage);
//                }
//            }
//        });
//        CommonUtil.setOnAddCardMessageListener(new CommonUtil.onAddCardMessageListener() {
//            @Override
//            public void onAddCardMessage(Context context, MsgAttachment msgAttachment) {
//                CardAttachment cardAttachment = (CardAttachment) msgAttachment;
//                ToastHelper.showToast(context,cardAttachment.getType()+"");
//            }
//        });
    }


    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }


    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }


    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }


}
