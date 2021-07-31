package com.incarta.quotescreator;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mig35.carousellayoutmanager.CarouselLayoutManager;
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener;

import java.util.ArrayList;
import java.util.List;


public class QuotesActivity extends AppCompatActivity {

    private ArrayList<Quote> imageArry;
    private ArrayList<Quote> favlist;
    //private QuotesListAdapter adapter;
    private PhotosAdapter adapter;
    private String Activitytype;
    private DataBaseHandler db;
    //private ListView dataList;
    private RecyclerView dataList;
    private int count;
    private ImageView noQuotes;
    private TextView NoQuotesText;
    private AdView adView;
    RelativeLayout noQuotesLayout;
    Toolbar toolbar;
    LinearLayout layout_quote_header;
    PrefManager prf;
    private int nativeAdPos = Config.NATIVE_ADS_POSITION;
    private final String TAG = QuotesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        if (Config.SHOW_ADS){
            loadAds();
            loadFullScreenAds();
        }

        prf = new PrefManager(this);

        Toast.makeText(this, "Tap to Change Background", Toast.LENGTH_SHORT).show();

        String categoryValue = getIntent().getExtras()
                .getString("category");
        Activitytype = getIntent().getExtras().getString("mode");

        toolbar = findViewById(R.id.toolbar);
        if (Activitytype.equals("isFavorite")) {
            toolbar.setTitle(getResources().getText(R.string.title_activity_favorites));
        } else
            toolbar.setTitle(categoryValue);
        setSupportActionBar(toolbar);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);   // set status text dark


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        db = new DataBaseHandler(this);
        noQuotes = (ImageView)findViewById(R.id.NoQuotes);
        NoQuotesText = findViewById(R.id.NoQuotesText);
        noQuotesLayout = findViewById(R.id.noQuotesLayout);
        layout_quote_header = findViewById(R.id.layout_quote_header);


        imageArry = new ArrayList<>();
        favlist = new ArrayList<>();



        dataList = findViewById(R.id.quotesList);
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);
        layoutManager.setCircleLayout(true);
        dataList.setLayoutManager(layoutManager);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());



        Button btnLoadMore = new Button(this);

        btnLoadMore.setBackgroundResource(R.drawable.round_background);
        btnLoadMore.setText(getResources().getText(R.string.btn_LoadMore));
        btnLoadMore.setTextColor(0xffffffff);
        Activitytype = getIntent().getExtras().getString("mode");

        if (Activitytype.equals("isCategory")) {
            categoryValue = getIntent().getExtras()
                    .getString("category");
            List<Quote> contacts = db.getQuotesByCategory(categoryValue);
            for (Quote cn : contacts) {

                imageArry.add(cn);

                int abc = imageArry.lastIndexOf(null);
                if ((imageArry.size() - (abc + 1)) % nativeAdPos == 0) {
                    imageArry.add(null);
                }
            }
        }

        if (Activitytype.equals("isAuthor")) {
            String authorValue = getIntent().getExtras().getString("name");
            List<Quote> contacts = db.getQuotesByAuthor(authorValue);
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }
            ;

        }

        if (Activitytype.equals("isFavorite")) {
                toolbar.setTitle(getResources().getText(R.string.title_activity_favorites));
                List<Quote> contacts = db.getFavorites();
                for (Quote cn : contacts) {

                    imageArry.add(cn);
                }
            ;
            if (imageArry.isEmpty()){

                noQuotes.setVisibility(View.VISIBLE);
                NoQuotesText.setVisibility(View.VISIBLE);
                noQuotesLayout.setVisibility(View.VISIBLE);
            }

        }
        if (Activitytype.equals("allQuotes")) {

            List<Quote> contacts = db.getAllQuotes(" LIMIT 50");
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }
            ;
            //dataList.addFooterView(btnLoadMore);
        }


        dataList.setHasFixedSize(true);
//        dataList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhotosAdapter(this, imageArry, null);

        dataList.setAdapter(adapter);


    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quotes, menu);
        return true;
    }

    private void loadAds () {
        if (Config.ADS_NETWORK){
            AdView adView = new AdView(this);
            adView.setAdUnitId(Config.BANNER_ID);
            adView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
            LinearLayout layout = (LinearLayout) findViewById(R.id.adView);
            layout.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }else{
            com.facebook.ads.AdView mAdView = new com.facebook.ads.AdView(this, Config.FACEBOOK_BANNER_ID, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.adView);
            adContainer.addView(mAdView);
            mAdView.loadAd();
        }
    }

    private void loadFullScreenAds () {
        if (Config.ADS_NETWORK){
            loadAdmobAds();
        }else {
            loadFacebookAds();
        }
    }

    private void loadAdmobAds () {
        final com.google.android.gms.ads.InterstitialAd interstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        interstitialAd.setAdUnitId(Config.INTER_ID);
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
    }

    private void loadFacebookAds () {
        final InterstitialAd interstitialAd = new InterstitialAd(this, Config.FACEBOOK_INTER_ID);
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
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
