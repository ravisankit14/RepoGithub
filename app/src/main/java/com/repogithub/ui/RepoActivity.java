package com.repogithub.ui;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.repogithub.R;
import com.repogithub.model.GetRepo;
import com.repogithub.restapicall.RepoService;
import com.repogithub.restapicall.RestClientApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoActivity extends AppCompatActivity {

    private FragmentManager fm;

    private ItemFragment itemFragment;
    private List<GetRepo> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);

        fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();

        if(getIntent().hasExtra("username")){

            String username = getIntent().getStringExtra("username");
            if(username != null ){
                getUsername(username);
            }


        }
    }

    private void getUsername(String search){

        //mList.clear();
        //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder()
        //        .addInterceptor(logging)
        //        .build();

        RepoService restClientApi = RestClientApi.getClientRepo().create(RepoService.class);

        Call<List<GetRepo>> call = restClientApi.getUsername(search);

        call.enqueue(new Callback<List<GetRepo>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetRepo>> call, @NonNull Response<List<GetRepo>> response) {

                if(response.isSuccessful()){

                    List<GetRepo> model =  response.body();
                    assert model != null;
                    Log.d("response ", " " + model.get(0));

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("repo_list", (ArrayList<? extends Parcelable>) model);
                    FragmentTransaction ft2 = fm.beginTransaction();
                    itemFragment = new ItemFragment();
                    itemFragment.setArguments(bundle);
                    ft2.add(R.id.listFragment,itemFragment);
                    ft2.commit();

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



}
