package com.netease.nim.demo.main.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by mike on 2019/11/20.
 */

public class NestListView extends ListView {
    public NestListView(Context context) {
        super(context);
    }

    public NestListView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量的大小由一个32位的数字表示，前两位表示测量模式，后30位表示大小，这里需要右移两位才能拿到测量的大小
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);

    }
}
