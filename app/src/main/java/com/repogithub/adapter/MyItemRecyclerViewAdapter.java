package com.repogithub.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.repogithub.R;
import com.repogithub.model.GetRepo;
import com.repogithub.ui.DetailActivity;
import com.repogithub.ui.ItemFragment;
import com.repogithub.ui.ItemFragment.OnFragmentItemListener;
import com.repogithub.utility.ImageCacheManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    public static final String ITEM_KEY = "item_key";
    public static final String ITEM_KEY_2 = "item_key2";

    private final List<GetRepo> mValues;
    private Context mContext;
    private OnFragmentItemListener mItemListListner;
    private String mFlag;
    private Map<String, Bitmap> mBitmap;

    public MyItemRecyclerViewAdapter(Context context, @NonNull List<GetRepo> items,Map<String, Bitmap> mBitmap) {
        mContext = context;
        mValues = items;
        this.mBitmap = mBitmap;
        //mItemListListner = listener;
        //mFlag = flag;
    }

    public MyItemRecyclerViewAdapter(Context context, @NonNull List<GetRepo> items) {
        mContext = context;
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        Log.d("response 2", " " + holder.mItem.getName());
        try {
            holder.mProjectName.setText(holder.mItem.getName());
            holder.mHttpLink.setText(holder.mItem.getHtml_url());
            holder.mSize.setText(holder.mItem.getSize());
            holder.mWatcher.setText(holder.mItem.getWatchers_count());
            holder.mIssues.setText(holder.mItem.getOpen_issues_count());

            //Glide.with(holder.mAvatarIcon.getContext()).load(holder.mItem.getOwner().getAvatar_url())
            //        .into(holder.mAvatarIcon);

            Bitmap bitmap = ImageCacheManager.getBitmap(mContext, holder.mItem);
            if (bitmap == null) {
                ImageDownloadTask task = new ImageDownloadTask();
                task.setViewHolder(holder);
                task.execute(holder.mItem);
            } else {
                holder.mAvatarIcon.setImageBitmap(bitmap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(ITEM_KEY, holder.mItem);
                intent.putExtra(ITEM_KEY_2, holder.mItem.getOwner().getAvatar_url());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        if(mValues.size() > 0){
            return mValues.size();
        }
        return 0;
    }

    public void add(GetRepo r) {
        mValues.add(r);
        notifyItemInserted(mValues.size() - 1);
    }

    public void addAll(List<GetRepo> mValues){
        for (GetRepo result : mValues) {
            add(result);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mProjectName;
        private final TextView mHttpLink;
        private final ImageView mAvatarIcon;
        private final TextView mSize;
        private final TextView mWatcher;
        private final TextView mIssues;
        private GetRepo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProjectName = (TextView) view.findViewById(R.id.text_project_name);
            mHttpLink = (TextView) view.findViewById(R.id.text_http_url);

            mAvatarIcon = (ImageView) view.findViewById(R.id.imageView);
            mSize = (TextView) view.findViewById(R.id.text_size);
            mWatcher = (TextView) view.findViewById(R.id.text_watcher);
            mIssues = (TextView) view.findViewById(R.id.text_issues_count);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProjectName.getText() + "'";
        }
    }

    private class ImageDownloadTask extends AsyncTask<GetRepo, Void, Bitmap> {

        private GetRepo mGetRepo;
        private ViewHolder mHolder;

        public void setViewHolder(ViewHolder holder) {
            mHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(GetRepo... dataItems) {

            mGetRepo = dataItems[0];
            String imageUrl = mGetRepo.getOwner().getAvatar_url() +".png";
            InputStream in = null;

            try {
                in = (InputStream) new URL(imageUrl).getContent();
                return BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mHolder.mAvatarIcon.setImageBitmap(bitmap);
            ImageCacheManager.putBitmap(mContext, mGetRepo, bitmap);
        }
    }
}
