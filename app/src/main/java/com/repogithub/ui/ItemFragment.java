package com.repogithub.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repogithub.R;
import com.repogithub.adapter.MyItemRecyclerViewAdapter;
import com.repogithub.model.GetRepo;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {

    public OnFragmentItemListener mItemListener;
    private static final String KEY =  "REPO_KEY";

    private RecyclerView mItemList;
    private MyItemRecyclerViewAdapter adapter;

    private List<GetRepo> repoList;
    private String flag = "0";


    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repoList = new ArrayList<>();

        if(savedInstanceState != null){
            repoList = savedInstanceState.getParcelableArrayList(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mItemList = (RecyclerView) view.findViewById(R.id.searchRecycleList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mItemList.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            repoList = bundle.getParcelableArrayList("repo_list");
        }
        //adapter = new MyItemRecyclerViewAdapter(getActivity(), repoList,mItemListener);
        mItemList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentItemListener) {
            mItemListener = (OnFragmentItemListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY, (ArrayList<? extends Parcelable>) repoList);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            repoList = savedInstanceState.getParcelableArrayList(KEY);
        }
    }

    public interface OnFragmentItemListener {
        void onFragmentItem(int position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
