package com.netease.nim.demo.fileManager.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.demo.R;
import com.netease.nim.demo.fileManager.model.FileInfo;
import com.netease.nim.demo.fileManager.utils.FileUtil;

import java.util.List;


/**
 * 使用遍历文件夹的方式
 * Created by yis on 2018/4/17.
 */

public class FolderDataRecycleAdapter extends com.chad.library.adapter.base.BaseQuickAdapter<FileInfo,BaseViewHolder> {

    private Context mContext;
    private boolean isPhoto = false;

    public FolderDataRecycleAdapter (List<FileInfo> data,Context mContext,boolean isPhoto) {
        super(R.layout.adapter_folder_data_rv_item,data);
        this.mContext = mContext;
        this.isPhoto = isPhoto;
    }


    @Override
    protected void convert(BaseViewHolder helper, FileInfo item) {
        helper.setText(R.id.tv_content,item.getFileName());
        helper.setText(R.id.tv_size,FileUtil.FormetFileSize(item.getFileSize()));
        helper.setText(R.id.tv_time,item.getTime());

        //封面图
        if (isPhoto) {
            Glide.with(mContext).load(item.getFilePath()).into((ImageView) helper.getView(R.id.iv_cover));
        } else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.fitCenter();
            Glide.with(mContext).load(FileUtil.getFileTypeImageId(mContext, item.getFilePath())).apply(requestOptions).into((ImageView) helper.getView(R.id.iv_cover));
        }
    }
}
