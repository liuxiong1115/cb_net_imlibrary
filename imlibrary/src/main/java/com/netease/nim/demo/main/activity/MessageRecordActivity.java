package com.netease.nim.demo.main.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.netease.nim.demo.R;
import com.netease.nim.demo.main.adapter.MessageRecordAdapter;
import com.netease.nim.demo.session.activity.FileDownloadActivity;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.module.model.Message;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.widget.MyToolbar;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.Serializable;
import java.util.List;

/**
 * 查看聊天记录界面
 */
public class MessageRecordActivity extends UI {

    MyToolbar toolbar;
    RecyclerView recyclerView;
    private List<Message> list;
    private String title;
    private IMMessage message;
    public static void start(Context context, String title, List<Message> list,IMMessage message) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("message",message);
        intent.setClass(context, MessageRecordActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_record);
        this.list = (List<Message>) getIntent().getSerializableExtra("list");
        this.title = getIntent().getStringExtra("title");
        this.message = (IMMessage) getIntent().getSerializableExtra("message");
        initView();
    }
    private void initView () {
        recyclerView = findViewById(R.id.recyclerView);
        NimToolBarOptions toolBarOptions = new NimToolBarOptions();
        toolBarOptions.titleString = title;
        setToolBar(R.id.toolbar,toolBarOptions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        MessageRecordAdapter adapter = new MessageRecordAdapter(list,this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if (view.getId() == R.id.message_item_file_detail_layout) {
               //     FileDownloadActivity.start(MessageRecordActivity.this,message,"reply");
                }
            }
        });
    }
}
