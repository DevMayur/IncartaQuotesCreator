package com.incarta.quotescreator;

import android.content.SharedPreferences;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.incarta.quotescreator.database.DataBaseHandler;

import java.util.ArrayList;
import java.util.List;



public class CategoryActivity extends AppCompatActivity {
    private ArrayList<Category> imageArry = new ArrayList<Category>();
    private CategoriesListAdapter adapter;
    private DataBaseHandler db;
    private RecyclerView dataList;
    private AdView adView;

    Toolbar toolbar;

    EditText searchedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        db = new DataBaseHandler(this);

        List<Category> categories = db.getAllCategories("");
        for (Category cat : categories) {

            imageArry.add(cat);

        }

        searchedit = findViewById(R.id.searchedit);

        searchedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                imageArry.clear();

                List<Category> authors = db.getAllCategories(searchedit.getText());
                for (Category cn : authors) {

                    imageArry.add(cn);
                }
                dataList.setAdapter(adapter);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter = new CategoriesListAdapter(this, R.layout.category_items,
                imageArry);

        dataList = findViewById(R.id.categoryList);
        dataList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        dataList.setAdapter(adapter);
//        adapter.setOnItemClickListner(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View viewClicked,
//                                    int position, long idInDB) {
//
//                Category srr = imageArry.get(position);
//                Intent i = new Intent(getApplicationContext(),
//                        QuotesActivity.class);
//                startSound();
//                i.putExtra("category", srr.getName());
//                i.putExtra("mode", "isCategory");
//                startActivity(i);
//
//            }
//        });

        adView = new AdView(this);
        adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layAdsCategories);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    //Sound Effect
    public void startSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(this,R.raw.button_tap);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean speaker = sharedPrefs.getBoolean("prefSpeaker", true);
        if (speaker.equals(true)) {
            likeSound.start();
        }else{
            likeSound.stop();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }
}
