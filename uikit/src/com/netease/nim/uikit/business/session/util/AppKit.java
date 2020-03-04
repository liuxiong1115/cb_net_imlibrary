package com.netease.nim.uikit.business.session.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

/**
 *
 */

public class AppKit {


    public static void openFile(Context context,File file){
        try{
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
//            intent.setData(/*uri*/uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider.fileProvider" , file);
//                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                intent.setDataAndType(contentUri , "text/plain");
            } else {
//                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");//打开APK文件
//                intent.setDataAndType(Uri.fromFile(file), "text/html");
//                intent.setDataAndType(Uri.fromFile(file), "image/*");
//                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setDataAndType(Uri.fromFile(file), "text/plain");
//                intent.setDataAndType(Uri.fromFile(file), "audio/*");
//                intent.setDataAndType(Uri.fromFile(file), "video/*");
//                intent.setDataAndType(Uri.fromFile(file), "application/x-chm");
//                intent.setDataAndType(Uri.fromFile(file), "application/msword");
//                intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
//                intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-powerpoint");/
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            Intent.createChooser(intent, "请选择对应的软件打开该附件！");
        }catch (ActivityNotFoundException e) {
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }
}
