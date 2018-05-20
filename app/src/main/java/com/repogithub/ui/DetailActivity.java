package com.repogithub.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.repogithub.R;
import com.repogithub.adapter.MyItemRecyclerViewAdapter;
import com.repogithub.model.GetRepo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class DetailActivity extends AppCompatActivity {

    private TextView projName, desc;
    private ImageView itemImage;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        GetRepo item = getIntent().getExtras().getParcelable(MyItemRecyclerViewAdapter.ITEM_KEY);
        imageUrl = getIntent().getStringExtra(MyItemRecyclerViewAdapter.ITEM_KEY_2);

        if (item == null) {
            throw new AssertionError("Null data item received!");
        }

        projName = (TextView) findViewById(R.id.textProjectName);
        desc = (TextView) findViewById(R.id.textviewDesc);
        itemImage = (ImageView) findViewById(R.id.avatarImage);

        projName.setText(item.getName());
        desc.setText(item.getDescription());
        Log.e("bitmap ",imageUrl);
        Glide.with(itemImage.getContext()).load(imageUrl)
               .into(itemImage);

        //imageLoader(item.getOwner().getAvatar_url() + ".png");
    }

    private void imageLoader (final String imageUrl ){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                InputStream in = null;

                try {
                    in = (InputStream) new URL(imageUrl).getContent();
                    Drawable d = Drawable.createFromStream(in, null);
                    itemImage.setImageDrawable(d);
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
        });
        thread.start();
    }
}