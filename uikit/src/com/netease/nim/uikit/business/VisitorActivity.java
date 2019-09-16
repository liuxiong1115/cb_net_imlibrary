package com.netease.nim.uikit.business;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.recent.VistorFragment;
import com.netease.nim.uikit.common.activity.UI;

/**
 * 访客列表
 */
public class VisitorActivity extends UI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        initView();
    }

    public void initView() {
        NimToolBarOptions toolBarOptions = new NimToolBarOptions();
        toolBarOptions.titleId =R.string.visitor;
        setToolBar(R.id.toolbar,toolBarOptions);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        VistorFragment fragment = new VistorFragment();
        transaction.add(R.id.framelayout, fragment);
        transaction.commit();
    }
}
