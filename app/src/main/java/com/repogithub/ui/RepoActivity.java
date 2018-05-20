package com.repogithub.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.repogithub.R;
import com.repogithub.adapter.MyItemRecyclerViewAdapter;
import com.repogithub.database.DataSource;
import com.repogithub.model.GetRepo;
import com.repogithub.restapicall.RepoService;
import com.repogithub.restapicall.RestClientApi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoActivity extends AppCompatActivity {

    private FragmentManager fm;
    private static int PAGE_SIZE = 20;

    private ItemFragment itemFragment;
    private List<GetRepo> mList;

    private RecyclerView mItemList;
    private MyItemRecyclerViewAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static String size = "1";
    private static String username;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);

        mList = new ArrayList<>();

        fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();

        if(getIntent().hasExtra("username")){

            username = getIntent().getStringExtra("username");
            if(username != null ){
                getUsername(username, size, String.valueOf(PAGE_SIZE));
            }
        }

        mItemList = (RecyclerView) findViewById(R.id.searchRecycleList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mLayoutManager = new LinearLayoutManager(this);
        mItemList.setLayoutManager(mLayoutManager);

        mItemList.addOnScrollListener(recyclerViewOnScrollListener);
        displayDataItems();
    }

    private void displayDataItems(Map<String,Bitmap> mBitmap) {
        if (mItemList != null) {
            adapter = new MyItemRecyclerViewAdapter(this, mList,mBitmap);
            mItemList.setAdapter(adapter);

        }
    }

    private void displayDataItems() {
        adapter = new MyItemRecyclerViewAdapter(this, mList);
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

    private void loadMoreItems() {
        isLoading = true;

        PAGE_SIZE += 20;

        getUsername(username, size, String.valueOf(PAGE_SIZE));
    }

    private void getUsername(String search, final String size, String length){

        RepoService restClientApi = RestClientApi.getClientRepo().create(RepoService.class);

        LinkedHashMap map = new LinkedHashMap();
        map.put("page",size);
        map.put("per_page",length);

        Call call = restClientApi.getUsername(search, map);

        call.enqueue(new Callback<List<GetRepo>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetRepo>> call, @NonNull Response<List<GetRepo>> response) {

                if(response.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    isLoading = false;
                    List<GetRepo> model =  response.body();

                    if(model != null){
                        Log.d("response ", " " + model.size());

                        adapter.addAll(model);

                        if(mList.size() >= PAGE_SIZE){
                            //loadMoreItems();
                        }else{
                            isLastPage = true;
                        }

                        addToDatabase(model);
                    }

                }else{
                    Toast.makeText(getBaseContext(),"Query limit reached",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@Nullable Call<List<GetRepo>> call, @Nullable Throwable t) {
                if(t != null)
                    Log.e("response",t.toString());
            }
        });
    }


    private void addToDatabase(final List<GetRepo> dataList){
        final DataSource dataSource = new DataSource(getBaseContext());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dataSource.seedDatabase(dataList);
            }
        }); thread.start();
    }

    //not in use
    private Map<String, Bitmap> imageLoader (final List<GetRepo> model ){
        final Map<String, Bitmap> map = new HashMap<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (GetRepo item : model) {
                    String imageUrl = item.getOwner().getAvatar_url() + ".png";
                    Log.e("response",imageUrl);
                    InputStream in = null;

                    try {
                        in = (InputStream) new URL(imageUrl).getContent();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        map.put("avatar", bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }); thread.start();

        //displayDataItems(map);
        return map;
    }


}
