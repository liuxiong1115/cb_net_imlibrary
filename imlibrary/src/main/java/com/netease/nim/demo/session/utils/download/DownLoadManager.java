package com.netease.nim.demo.session.utils.download;

/**
 * Created by Shawn on 2018/4/26.
 */

public class DownLoadManager {

   /* static DownLoadManager downLoadManager;
    static DownLoadThread downLoadThread;*/
   private DownLoadManager downLoadManager;
    private DownLoadThread downLoadThread;

    public DownLoadManager(){
        if(downLoadThread == null) {
            downLoadThread = new DownLoadThread();
        }
    }

  /*  public synchronized static DownLoadManager getInstance(){
        if(downLoadManager == null){
            downLoadManager = new DownLoadManager();
        }
        return downLoadManager;
    }*/

    public void start(String url , String name , DownloadListener listener){
        downLoadThread.startDown(url , name , listener);
    }


    public void stop(){
        downLoadThread.stopDown();
    }


//    /**
//     * 显示下载对话框  并开启下载
//     * @param url
//     * @param fileName
//     */
//    private void showDownloadDialog(Activity context , String url , String fileName){
//        final DownLoadManager downLoadManager = DownLoadManager.getInstance();
//        LayoutInflater inflater = context.getLayoutInflater();
//        View contentView = inflater.inflate(R.layout.dialog_downloading_progress , null);
//        final AlertDialog downloadDialog = new android.support.v7.app.AlertDialog.Builder(context).setTitle("下载文件").setView(contentView).setCancelable(false).create();
//        AppCompatButton button = ButterKnife.findById(contentView , R.id.button);
//        final ProgressBar progressBar = ButterKnife.findById(contentView , R.id.progressBar);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                downLoadManager.stop();
//                downloadDialog.dismiss();
//            }
//        });
//        downloadDialog.show();
//        downLoadManager.start(url, fileName , new DownloadListener() {
//            @Override
//            public void onStart(DownLoadThread thread, long fileSize) {
//
//            }
//
//            @Override
//            public void onStop(File downloadFile) {
//
//            }
//
//            @Override
//            public void onProgress(long fileSize, long downFileSize, final int progress) {
//                Handler uiHadler = new Handler();
//                uiHadler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setProgress(progress);
//                        if(progress >= 100){
//                            downloadDialog.dismiss();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onSuccess(File downloadFile) {
//
//            }
//
//            @Override
//            public void onError(Exception ex) {
//
//            }
//        });
//    }
}
