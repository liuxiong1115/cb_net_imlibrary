package com.netease.nim.uikit.business.session.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.model.CollectionEmoji;

public class CollectAdapter extends BaseAdapter {

    private Context context;

    private int startIndex;
    private CollectionEmoji collectionEmoji;

    public CollectAdapter(Context mContext, CollectionEmoji collectionEmoji,int index) {
        this.context = mContext;
        this.collectionEmoji = collectionEmoji;
        this.startIndex = index;
    }

    public int getCount() {
        int size = collectionEmoji == null ? 10: collectionEmoji.getBody() == null ? 0 :
                collectionEmoji.getBody().getList() == null ? 0 :collectionEmoji.getBody().getList().size();
        int count = size - startIndex;
        count = Math.min(count, EmoticonView.COLLECTION_PER_PAGE);
        return count;
     //   return collectionEmoji.getBody().getList() == null ? 0 : collectionEmoji.getBody().getList().size();
    }

    @Override
    public Object getItem(int position) {
        return collectionEmoji.getBody().getList().get(startIndex + position);
    }

    @Override
    public long getItemId(int position) {
        return startIndex + position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CollectionViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.nim_collection_item, null);
            viewHolder = new CollectionViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.collectionThumb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CollectionViewHolder) convertView.getTag();
        }

        int index = startIndex + position;
        if (index >= collectionEmoji.getBody().getList().size()) {
            return convertView;
        }

        CollectionEmoji.BodyBean.ListBean item =  collectionEmoji.getBody().getList().get(index);
        if (item == null) {
            return convertView;
        }

        Glide.with(context)
                .load(item.getEmojiUrl() == null ? "" : item.getEmojiUrl())
                .apply(new RequestOptions()
                        .error(com.netease.nim.uikit.R.drawable.nim_default_img_failed)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .dontAnimate())
                .into(viewHolder.imageView);
        return convertView;
    }
    class CollectionViewHolder {
        public ImageView imageView;
    }
}