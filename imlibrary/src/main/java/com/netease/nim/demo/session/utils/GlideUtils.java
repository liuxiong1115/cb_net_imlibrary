package com.netease.nim.demo.session.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by wangqiuxia on 2018/10/24.
 */
public class GlideUtils {
    public static void lxGlide(Context context, String Url, ImageView imageView, RequestOptions requestOptions) {
        String dealUrl = Url.substring(0,4).equals("http") ?Url: "https://classbro-oss.oss-cn-hongkong.aliyuncs.com/" +Url;
        Glide.with(context).load(dealUrl).apply(requestOptions).into(imageView);
    }
    public static void lxGlide(Context context, String Url, ImageView imageView) {
        String dealUrl = Url.substring(0,4).equals("http") ?Url: "https://classbro-oss.oss-cn-hongkong.aliyuncs.com/" +Url;
        Glide.with(context).load(dealUrl).into(imageView);
    }
}
