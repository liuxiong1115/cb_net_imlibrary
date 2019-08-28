package com.netease.nim.demo.fileManager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.netease.nim.demo.R;
import com.netease.nim.demo.fileManager.adapter.FolderDataRecycleAdapter;
import com.netease.nim.demo.fileManager.model.FileInfo;


import java.util.List;


/**
 * Created by yis on 2018/4/17.
 */

public class FolderDataFragment extends Fragment {

    private RecyclerView rvDoc;
    private AppCompatTextView noData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doc, container, false);
        rvDoc = rootView.findViewById(R.id.rv_doc);
        noData = rootView.findViewById(R.id.noData);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        Bundle bundle = this.getArguments();

        List<FileInfo> data = bundle.getParcelableArrayList("file_data");
        boolean isImage = bundle.getBoolean("is_image");
        if (data == null || data.size() == 0) {
            rvDoc.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        } else {
            rvDoc.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            //设置RecyclerView 布局
            rvDoc.setLayoutManager(linearLayoutManager);
            FolderDataRecycleAdapter pptListAdapter = new FolderDataRecycleAdapter(data,getActivity(), isImage);
            rvDoc.setAdapter(pptListAdapter);
        }
        rvDoc.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                FileInfo fileInfo = (FileInfo) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("EXTRA_DATA_PATH", fileInfo.getFilePath());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }
}
