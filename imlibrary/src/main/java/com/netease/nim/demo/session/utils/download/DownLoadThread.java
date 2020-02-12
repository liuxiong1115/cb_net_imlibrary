package com.netease.nim.demo.session.utils.download;


import android.accounts.NetworkErrorException;
import android.util.Log;


import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadThread extends Thread {

    private String path;

    private String fileName;

    private boolean isStop = false;
    private File downloadFile;
    private long fileSize = 0;//文件总大小
    private long downFileSize = 0;//已经下载的文件的大小

    private HttpURLConnection urlConn;
    private InputStream inputStream;
    private FileOutputStream fileOutputStream = null;//文件输出流

    private int downloadtimes = 0;//当前尝试请求的次数
    private int maxdownloadtimes = 10;//失败重新请求次数

    private DownloadListener listener;

    public DownLoadThread() {
    }

    @Override
    public void run() {
        while(downloadtimes < maxdownloadtimes) {
            try {
                URL url = new URL(path);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setReadTimeout(5000);
                urlConn.setConnectTimeout(5000);
                urlConn.setRequestProperty("Charset", "UTF-8");
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 6.viewpager_1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
                urlConn.setRequestProperty("Accept-Encoding", "identity");
                int code = urlConn.getResponseCode();
                if(301 == code || 302 == code) {
                    String redirectUrl = urlConn.getHeaderField("Location");
                    if(redirectUrl != null && !redirectUrl.isEmpty()) {
                        path = redirectUrl;
                    }
                }

                fileSize = urlConn.getContentLength();
                if(listener != null){
                    listener.onStart(DownLoadThread.this , fileSize);
                }

                if (urlConn.getResponseCode() == 200) {
                    inputStream = urlConn.getInputStream();//获取输入流
                    if (inputStream != null) {
                        FileUtils fileUtils = new FileUtils();
                        fileUtils.existsFile();
                        downloadFile = fileUtils.createFile(fileName);
                        fileOutputStream = new FileOutputStream(downloadFile);
                        byte[] buf = new byte[1024];
                        int ch =-1;
                        while ((ch = inputStream.read(buf)) != -1 && !isStop) {
                            fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            downFileSize += ch;
                            int nowProgress = (int)((100 * downFileSize)/fileSize);
                            if(listener != null){
                                listener.onProgress(fileSize , downFileSize , nowProgress);
                            }
                        }
                        //下载完了
                        if(downFileSize == fileSize ){
                            downloadtimes = maxdownloadtimes;
                            if(listener != null){
                                listener.onSuccess(downloadFile);
                            }
                        }
                    }
                }else{
                    throw new NetworkErrorException(urlConn.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                downloadtimes ++;
                if(downloadtimes >= maxdownloadtimes){
                    if(listener != null){
                        listener.onError(e);
                    }
                }
            }finally {
                try {
                    if(urlConn != null){
                        urlConn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(inputStream != null){
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void stopDown(){
        isStop = true;
        if(listener != null){
            listener.onStop(downloadFile);
        }
    }

    public void startDown(String path, String fileName , DownloadListener listener){
        this.listener = listener;
        this.path = path;
        this.fileName = fileName;
        this.fileSize = 0;//文件总大小
        this.downFileSize = 0;//已经下载的文件的大小
        this.downloadtimes = 0;
        isStop = false;
        start();
    }

}
