package com.netease.nim.uikit.business.session.util.download;

import java.io.File;

/**
 * Created by Shawn on 2018/4/26.
 */

public interface DownloadListener {

    void onStart(DownLoadThread thread, long fileSize);

    void onStop(File downloadFile);

    void onProgress(long fileSize, long downFileSize, int progress);

    void onSuccess(File downloadFile);

    void onError(Exception ex);
}
