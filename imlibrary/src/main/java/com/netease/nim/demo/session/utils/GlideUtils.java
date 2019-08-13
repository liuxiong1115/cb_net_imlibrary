package com.netease.nim.demo.session.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.common.CommonUtil;

/**
 * Created by wangqiuxia on 2018/10/24.
 */
public class GlideUtils {
    public static void lxGlide(Context context, String url, ImageView imageView, RequestOptions requestOptions) {
        String dealUrl = url.substring(0,4).equals("http") ?url: CommonUtil.BaseUrl +url;
        Glide.with(context).load(dealUrl).apply(requestOptions).into(imageView);
    }
    public static void lxGlide(Context context, String url, ImageView imageView) {
        String dealUrl = url.substring(0,4).equals("http") ?url: CommonUtil.BaseUrl +url;
        Glide.with(context).load(dealUrl).into(imageView);
    }
}
