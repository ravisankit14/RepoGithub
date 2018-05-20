package com.repogithub;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.repogithub.R;
import com.repogithub.restapicall.ApiService;
import com.repogithub.restapicall.RestClientApi;
import com.repogithub.restapicall.model.ModelUsername;
import com.repogithub.restapicall.model.Search;
import com.repogithub.ui.ItemFragment;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText mEditText;
    private ListView mListView;
    private CardView mCardView;
    private TextView mErrorMsg;

    private Search search;
    private List<String> mList;
    private ArrayAdapter<String> adapter;

    private Handler handler;
    private FragmentManager fm;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        search = new Search();
        mList = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                android.R.id.text1, mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        handler = new Handler();

    }

    TextWatcher myTextWatcher  = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(final CharSequence s, int i, int i1, int i2) {

            if(s.length() > 2){

                mCardView.setVisibility(View.VISIBLE);
                getUsername(s.toString());

            }else{
                mCardView.setVisibility(View.GONE);
                //TextKeyListener.clear(mEditText.getText());
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void getUsername(String search){

        mList.clear();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        ApiService restClientApi = RestClientApi.getClient(client).create(ApiService.class);

        Call<ModelUsername> call = restClientApi.getUsername(search);

        call.enqueue(new Callback<ModelUsername>() {
            @Override
            public void onResponse(@NonNull Call<ModelUsername> call, @NonNull Response<ModelUsername> response) {

                if(response.isSuccessful()){

                    ModelUsername model =  response.body();
                    assert model != null;
                    String login = model.getLogin();

                    Log.d("response ", " " + login);
                    if(login != null){
                        mList.add(login);
                    }else{
                        mList.clear();
                        mList.add("no result found");
                    }

                }else{
                    Toast.makeText(getBaseContext(),"Query limit reached",Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@Nullable Call<ModelUsername> call, @Nullable Throwable t) {
                if(t != null)
                    Log.e("response",t.toString());
            }
        });

    }

    private void initialize(){
        mEditText = (EditText) findViewById(R.id.editSearch);
        mListView = (ListView) findViewById(R.id.usernameList);
        mCardView = (CardView) findViewById(R.id.cardList);
        mErrorMsg = (TextView) findViewById(R.id.errorMsg);
        frameLayout = (FrameLayout) findViewById(R.id.fragment);

        mEditText.addTextChangedListener(myTextWatcher);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d("position", " " + i);

        if(!mList.get(i).equalsIgnoreCase("no result found")){
            //Intent intent  = new Intent(getBaseContext(),RepoActivity.class);
            //intent.putExtra("username",mList.get(i));
            //startActivity(intent);

            mCardView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            Bundle bundle = new Bundle();
            bundle.putString("username", mList.get(i));

            FragmentTransaction ft = fm.beginTransaction();
            ItemFragment itemFragment = new ItemFragment();
            itemFragment.setArguments(bundle);
            ft.add(R.id.fragment,itemFragment);
            ft.commit();
        }

    }
}
