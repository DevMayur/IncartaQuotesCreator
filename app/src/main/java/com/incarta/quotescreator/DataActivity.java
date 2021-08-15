package com.incarta.quotescreator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.incarta.quotescreator.database.DataBaseHandler;

import java.util.ArrayList;
import java.util.List;




public class DataActivity extends AppCompatActivity {
    private ArrayList<Quote> imageArry = new ArrayList<Quote>();
    private DataListAdapter adapter;
    private DataBaseHandler db;
    private ListView dataList;
    SearchView searchView;
    private AdView adView;

    EditText searchedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auteurs);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        db = new DataBaseHandler(this);

        List<Quote> authors = db.getAllAuthors("");
        for (Quote cn : authors) {

            imageArry.add(cn);

        }

        searchedit = findViewById(R.id.searchedit);

        searchedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                imageArry.clear();

                List<Quote> authors = db.getAllAuthors(searchedit.getText());
                for (Quote cn : authors) {

                    imageArry.add(cn);
                }
                dataList.setAdapter(adapter);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        adapter = new DataListAdapter(this, R.layout.author_items, imageArry);

        dataList = (ListView) findViewById(R.id.listView1);
        dataList.setAdapter(adapter);
        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long idInDB) {

                Quote srr = imageArry.get(position);
                Intent i = new Intent(getApplicationContext(),
                        QuotesActivity.class);
                i.putExtra("name", srr.getName());
                i.putExtra("mode", "isAuthor");
                startActivity(i);

            }
        });

        adView = new AdView(this);
        adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layAdsAuthors);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


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
