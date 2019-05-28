package com.netease.nim.demo.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.demo.R;
import com.netease.nim.demo.file.FileIcons;
import com.netease.nim.demo.session.utils.AppKit;
import com.netease.nim.demo.session.utils.download.DownLoadManager;
import com.netease.nim.demo.session.utils.download.DownLoadThread;
import com.netease.nim.demo.session.utils.download.DownloadListener;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.widget.MyToolbar;
import com.netease.nim.uikit.common.util.file.FileUtil;
import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

/**
 * Created by hzxuwen on 2016/12/14.
 */

public class FileDownloadActivity extends UI {
    private static final String INTENT_EXTRA_DATA = "INTENT_EXTRA_DATA";

    private TextView fileNameText;
    private Button fileDownloadBtn;
    private ImageView imageView;

    private IMMessage message;
    private MyToolbar toolbar;
    private boolean isSucceed;

    public static void start(Context context, IMMessage message) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_DATA, message);
        intent.setClass(context, FileDownloadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_download_activity);

        onParseIntent();
        findViews();

        updateUI();
        registerObservers(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }

    private void onParseIntent() {
        this.message = (IMMessage) getIntent().getSerializableExtra(INTENT_EXTRA_DATA);
    }

    private void findViews() {
        final FileAttachment attachment = (FileAttachment) message.getAttachment();
        fileNameText = findView(R.id.file_name);
        fileDownloadBtn = findView(R.id.download_btn);
        imageView = findView(R.id.file_icon);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "文件";
        setToolBar(R.id.toolbar, options);
        boolean isExit = FileUtils.isFileExist(attachment.getDisplayName());
        if (isExit) {
            isSucceed = true;
            fileDownloadBtn.setText("打开");
            ToolBarOptions options1 = new NimToolBarOptions();
            setToolBar(R.id.toolbar, options1);
            //  fileDownloadBtn.setBackgroundResource(R.drawable.g_white_btn_pressed);
            fileDownloadBtn.setBackgroundResource(R.drawable.nim_team_create_btn_selector);
        } else {
            isSucceed = false;
            fileDownloadBtn.setText("下载");
            fileDownloadBtn.setBackgroundResource(R.drawable.nim_team_create_btn_selector);
        }

        fileDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachment != null) {
                    boolean isExit = FileUtils.isFileExist(attachment.getDisplayName());
                    if (isExit) {
                        AppKit.openFile(getApplicationContext(), FileUtil.createFile(attachment.getDisplayName()));
                    } else {
//                        if (isOriginDataHasDownloaded(message)) {
//                            return;
//                        }
//                        downloadFile();
                        showDownloadDialog(attachment.getUrl(), attachment.getDisplayName());
                    }
                } else {
                    Toast.makeText(FileDownloadActivity.this, "发生未知错误，暂不能使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI() {
        FileAttachment attachment = (FileAttachment) message.getAttachment();
        if (attachment != null) {
            fileNameText.setText(attachment.getDisplayName());
            int iconResId = FileIcons.smallIcon(attachment.getDisplayName());
            imageView.setImageResource(iconResId);
        }
/*
        if (isOriginDataHasDownloaded(message)) {
            onDownloadSuccess();
        } else {
            onDownloadFailed();
        }*/
    }

    private boolean isOriginDataHasDownloaded(final IMMessage message) {
        if (!TextUtils.isEmpty(((FileAttachment) message.getAttachment()).getPath())) {
            return true;
        }

        return false;
    }

    private void downloadFile() {
        DialogMaker.showProgressDialog(this, "loading");
        NIMClient.getService(MsgService.class).downloadAttachment(message, false);
    }

    /**
     * 显示下载对话框  并开启下载
     *
     * @param url
     * @param fileName
     */
    private void showDownloadDialog(String url, String fileName) {
        String dealUrl = url.substring(0, 4).equals("http") ? url : CommonUtil.BaseUrl + "/" + url;
        final DownLoadManager downLoadManager = new DownLoadManager();
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_downloading_progress, null);
        final AlertDialog downloadDialog = new android.support.v7.app.AlertDialog.Builder(FileDownloadActivity.this).setTitle("下载文件").setView(contentView).setCancelable(false).create();
        AppCompatButton button = contentView.findViewById(R.id.button);
        final ProgressBar progressBar = contentView.findViewById(R.id.progressBar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadManager.stop();
                downloadDialog.dismiss();
            }
        });
        downloadDialog.show();
        downLoadManager.start(dealUrl, fileName, new DownloadListener() {
            @Override
            public void onStart(DownLoadThread thread, long fileSize) {

            }

            @Override
            public void onStop(File downloadFile) {
                Toast.makeText(FileDownloadActivity.this, "取消下载", Toast.LENGTH_SHORT).show();
                if (downloadFile != null) {
                    downloadFile.delete();
                }
            }

            @Override
            public void onProgress(long fileSize, long downFileSize, final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                        if (progress >= 100) {
                            downloadDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onSuccess(final File downloadFile) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadDialog.dismiss();
                        AppKit.openFile(FileDownloadActivity.this, downloadFile);
                        Log.e("filePath", downloadFile + "");
                        isSucceed = true;
                        fileDownloadBtn.setText("打开");
                        ToolBarOptions options1 = new NimToolBarOptions();
                        setToolBar(R.id.toolbar, options1);
                        //  fileDownloadBtn.setBackgroundResource(R.drawable.g_white_btn_pressed);
                        fileDownloadBtn.setBackgroundResource(R.drawable.nim_team_create_btn_selector);
                    }
                });
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FileDownloadActivity.this, "下载文件异常，请检查权限是否开启", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * ********************************* 下载 ****************************************
     */

    private void registerObservers(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(statusObserver, register);
    }

    private Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage msg) {
            if (!msg.isTheSame(message) || isDestroyedCompatible()) {
                return;
            }

          /*  if (msg.getAttachStatus() == AttachStatusEnum.transferred && isOriginDataHasDownloaded(msg)) {
                DialogMaker.dismissProgressDialog();
                onDownloadSuccess();
            } else if (msg.getAttachStatus() == AttachStatusEnum.fail) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(FileDownloadActivity.this, "download failed", Toast.LENGTH_SHORT).show();
                onDownloadFailed();
            }*/
        }
    };

  /*  private void onDownloadSuccess() {
        isSucceed = true;
        fileDownloadBtn.setText("打开");
        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);
        //  fileDownloadBtn.setBackgroundResource(R.drawable.g_white_btn_pressed);
        fileDownloadBtn.setBackgroundResource(R.drawable.nim_team_create_btn_selector);
    }

    private void onDownloadFailed() {
        isSucceed = false;
        fileDownloadBtn.setText("下载");
        fileDownloadBtn.setBackgroundResource(R.drawable.nim_team_create_btn_selector);
    }*/
}
