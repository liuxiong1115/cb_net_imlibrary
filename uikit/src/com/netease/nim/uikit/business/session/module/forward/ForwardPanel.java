package com.netease.nim.uikit.business.session.module.forward;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.session.module.Container;

/**
 * 转发
 * Created by sherry on 2015/6/16.
 */
public class ForwardPanel {

    protected Container container;
    protected View view;
    public MessageFragment messageFragment;
    public LinearLayout linearLayout;
    public AppCompatTextView stepForward,mergeForward;
    private boolean isBack = false;

    public ForwardPanel(Container container, View view) {
        this.container = container;
        this.view = view;
    }

    public boolean onBackPressed() {
        if (isBack) {
            hideLayout();
            return true;
        }
        return false;
    }

    private void initView(final ForWardInterface forWardInterface) {
        linearLayout = view.findViewById(R.id.forward);
        stepForward = view.findViewById(R.id.forward_step);
        mergeForward = view.findViewById(R.id.forward_merge);
        stepForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forWardInterface.onStepClick();
            }
        });
        mergeForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forWardInterface.onMergeClick();
            }
        });
        linearLayout.setVisibility(View.VISIBLE);
    }


    /**
     * 隐藏布局
     */
    public void hideLayout () {
        isBack = false;
        linearLayout.setVisibility(View.GONE);
    }

    /**
     * 显示布局
     */
    public void showLayout(ForWardInterface forWardInterface) {
        isBack = true;
        initView(forWardInterface);
        linearLayout.setVisibility(View.VISIBLE);
    }
}
