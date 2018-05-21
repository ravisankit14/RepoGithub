package com.repogithub.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.repogithub.R;
import com.repogithub.adapter.MyItemRecyclerViewAdapter;
import com.repogithub.database.DataSource;
import com.repogithub.model.GetRepo;
import com.repogithub.restapicall.RepoService;
import com.repogithub.restapicall.RestClientApi;
import com.repogithub.utility.NetworkHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemFragment extends Fragment {

    public OnFragmentItemListener mItemListener;
    private static final String KEY =  "REPO_KEY";

    private int PAGE_SIZE = 20;

    private List<GetRepo> mList;

    private RecyclerView mItemList;
    private MyItemRecyclerViewAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String size = "1";
    private static String username;

    private ProgressBar progressBar;
    private boolean network;


    public ItemFragment() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();

        if(savedInstanceState != null){
            mList = savedInstanceState.getParcelableArrayList(KEY);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mItemList = (RecyclerView) view.findViewById(R.id.searchRecycleList);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mItemList.setLayoutManager(mLayoutManager);

        mItemList.addOnScrollListener(recyclerViewOnScrollListener);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        network = NetworkHelper.hasNetworkAccess(getContext());
        Bundle bundle = getArguments();
        if(bundle != null){

            username = bundle.getString("username");
            if(username != null ){
                if(network){
                    getUsername(username, size, String.valueOf(PAGE_SIZE));
                }else{
                    Toast.makeText(getContext(),"No network connectivity",Toast.LENGTH_SHORT).show();
                }

            }
        }
        displayDataItems();
        mItemList.setAdapter(adapter);

    }

    private void displayDataItems() {
        adapter = new MyItemRecyclerViewAdapter(getActivity(), mList);
        mItemList.setAdapter(adapter);
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY, (ArrayList<? extends Parcelable>) mList);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mList = savedInstanceState.getParcelableArrayList(KEY);
        }
    }

    private void loadMoreItems() {
        isLoading = true;

        PAGE_SIZE += 20;

        getUsername2(username, size, String.valueOf(PAGE_SIZE));
    }


    private void getUsername(String search, final String size, String length){

        RepoService restClientApi = RestClientApi.getClientRepo().create(RepoService.class);

        LinkedHashMap<String,String> map = new LinkedHashMap();
        map.put("page",size);
        map.put("per_page",length);

        Call call = restClientApi.getUsername(search, map);

        call.enqueue(new Callback<List<GetRepo>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetRepo>> call, @NonNull Response<List<GetRepo>> response) {
                isLoading = false;
                if(response.isSuccessful()){

                    List<GetRepo> model =  response.body();

                    if(model != null){
                        Log.d("response ", " " + model.size());

                        adapter.addAll(model);

                        if(mList.size() >= PAGE_SIZE){
                            adapter.addFooter();
                        }else{
                            isLastPage = true;
                        }

                        //addToDatabase(model);
                    }

                }else{
                    Toast.makeText(getActivity(),"Query limit reached",Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@Nullable Call<List<GetRepo>> call, @Nullable Throwable t) {
                if(t != null)
                    Log.e("response",t.toString());
            }
        });
    }

    private void getUsername2(String search, final String size, String length){

        RepoService restClientApi = RestClientApi.getClientRepo().create(RepoService.class);

        LinkedHashMap<String,String> map = new LinkedHashMap();
        map.put("page",size);
        map.put("per_page",length);

        Call call = restClientApi.getUsername(search, map);

        call.enqueue(new Callback<List<GetRepo>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetRepo>> call, @NonNull Response<List<GetRepo>> response) {

                adapter.removeLoadingFooter();
                isLoading = false;
                if(response.isSuccessful()){

                    List<GetRepo> model =  response.body();

                    if(model != null){
                        Log.d("response ", " " + model.size());

                        adapter.addAll(model);

                        if(mList.size() >= PAGE_SIZE){
                            adapter.addFooter();
                        }else{
                            isLastPage = true;
                        }

                        //addToDatabase(model);
                    }

                }else{
                    Toast.makeText(getActivity(),"Query limit reached",Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@Nullable Call<List<GetRepo>> call, @Nullable Throwable t) {
                if(t != null)
                    Log.e("response",t.toString());
            }
        });
    }

    //data storage
    private void addToDatabase(final List<GetRepo> dataList){
        final DataSource dataSource = new DataSource(getActivity());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dataSource.seedDatabase(dataList);

            }
        }); thread.start();
    }

    public interface OnFragmentItemListener {
        void onFragmentItem(int position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
