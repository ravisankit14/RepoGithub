package com.repogithub.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.repogithub.R;
import com.repogithub.model.GetRepo;
import com.repogithub.ui.ItemFragment.OnFragmentItemListener;

import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<GetRepo> mValues;
    private Context mContext;
    private OnFragmentItemListener mItemListListner;
    private String mFlag;

    public MyItemRecyclerViewAdapter(Context context, @NonNull List<GetRepo> items, OnFragmentItemListener listener) {
        mContext = context;
        mValues = items;
        mItemListListner = listener;
        //mFlag = flag;
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

        holder.mProjectName.setText(mValues.get(position).getName());
        holder.mHttpLink.setText(mValues.get(position).getHtml_url());
        holder.mSize.setText(mValues.get(position).getSize());
        holder.mWatcher.setText(mValues.get(position).getWatchers_count());
        holder.mIssues.setText(mValues.get(position).getOpen_issues_count());
        //holder.mProjectName.setText(mValues.get(position).getmProjectName());

    }

    @Override
    public int getItemCount() {

        if(mValues.size() > 0){
            return mValues.size();
        }
        return 0;
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
}
