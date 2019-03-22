package com.netease.nim.demo.coustomnotify;

import android.content.Context;

/**
 * Created by mike on 2019/3/22.
 */

public class CoustomNotifyClient {
    private static boolean init;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        init = true;
        CoustomNotifyFilter.startFilter(context);
    }

}
