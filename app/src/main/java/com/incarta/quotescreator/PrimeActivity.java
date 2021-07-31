package com.incarta.quotescreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.billingclient.api.BillingClient;
import com.google.android.gms.ads.AdView;

public class PrimeActivity extends AppCompatActivity{

    private static final String TAG = "PrimeActivity";
    static final String ITEM_SKU_ADREMOVAL = "ad_remove_item";
    private Button btn_in_app_premium_upgrade, btn_skip_in_app;
    private SharedPreferences mSharedPreferences;
    private BillingClient mBillingClient;
    private AdView mAdView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);   // set status text dark


        toolbar  = findViewById(R.id.toolbar);
        toolbar.setTitle("Premium Features");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
