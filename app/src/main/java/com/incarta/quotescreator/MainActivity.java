package com.incarta.quotescreator;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    DataBaseHandler db;
    private AlertDialog dialog;
    public static final int IntialQteOfDayId = 8;
    private LinearLayout ll_liked_quotes, ll_todays_quote, llMaker, ll_sounds, ll_about, ll_contact_us, ll_rate_app, ll_share_app, ll_like_fb, ll_follow_insta,ll_privacy_policy ;
    final Context context = this;
    SharedPreferences preferences;
    private static final int RESULT_SETTINGS = 1;
    private ArrayList<Category> imageArry = new ArrayList<Category>();
    private CategoriesListAdapter adapter;
    private RecyclerView dataList;
    private AdView adView;
    EditText searchedit;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
//    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private final int STORAGE_PERMISSION_CODE = 1;
    PrefManager prf;
    private Boolean DialogOpened = false;


    TextView mainTitle;
    CardStackView cardStackView;
    ArrayList<Quote> quoteList;
    PhotosAdapter quoteAdapter;
//    CardStackListener cardStackListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this);
        AudienceNetworkAds.initialize(this);

        db = new DataBaseHandler(this);
        prf = new PrefManager(this);
        db.openDataBase() ;
        loadAds();

        //Custom Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        if (drawerLayout != null)
//        drawerLayout.useCustomBehavior(GravityCompat.START);
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
//        actionBarDrawerToggle.syncState();
        mainTitle = findViewById(R.id.tv_title_main);
        mainTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        cardStackView = findViewById(R.id.cardStackView);
        quoteList = new ArrayList<>();

        List<Quote> allQuotes = db.getAllQuotes(" LIMIT 100");
        quoteList.addAll(allQuotes);
        quoteAdapter = new PhotosAdapter(this, quoteList,this);
        CardStackLayoutManager cardStackLayoutManager = new CardStackLayoutManager(this);
        setupCardStackLayoutManager(cardStackLayoutManager);
        cardStackView.setLayoutManager(cardStackLayoutManager);
        cardStackView.setAdapter(quoteAdapter);
        quoteAdapter.notifyDataSetChanged();

        //db = new DataBaseHandler(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("IS_FIRST_TIME", true)) {

            givePermission();

            sharedPreferences.edit().putBoolean("IS_FIRST_TIME", false).apply();
        }

        final List<Category> categories = db.getAllCategories("");
        for (Category cat : categories) {
            imageArry.add(cat);
        }

        //Adapter Code
        adapter = new CategoriesListAdapter(this, R.layout.category_items,imageArry);
        dataList = findViewById(R.id.categoryList);
        dataList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        dataList.setAdapter(adapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(dataList);



        //Search Box
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


        //Drawer Menu Click Listner
        ll_liked_quotes= findViewById(R.id.ll_liked_quotes);
        ll_todays_quote= findViewById(R.id.ll_todays_quote);
        llMaker= findViewById(R.id.ll_quote_maker);
        ll_sounds= findViewById(R.id.ll_setting);
        ll_about= findViewById(R.id.ll_about);
        ll_contact_us=  findViewById(R.id.ll_contact_us);
        ll_rate_app= findViewById(R.id.ll_rate_app);
        ll_share_app=  findViewById(R.id.ll_share_app);
        ll_privacy_policy =  findViewById(R.id.ll_privacy_policy);

        //show your favorite quotes
        ll_liked_quotes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent favorites = new Intent(MainActivity.this,
                        QuotesActivity.class);
                favorites.putExtra("mode", "isFavorite");
                startActivity(favorites);
                //startActivity(new Intent(MainActivity.this, FavoriteListActivity.class));

            }
        });

        //quote of the day
        ll_todays_quote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                preferences = PreferenceManager
                        .getDefaultSharedPreferences(context);

                Intent qteDay = new Intent(MainActivity.this,
                        QuoteActivity.class);
                qteDay.putExtra("id",
                        preferences.getInt("id", IntialQteOfDayId));
                qteDay.putExtra("mode", "qteday");
                startActivity(qteDay);
            }
        });

        //show your favorite quotes
        llMaker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent favorites = new Intent(context,
                        MakerActivity.class);
                favorites.putExtra("quote", "");
                context.startActivity(favorites);

            }
        });

        //setting tab
        ll_sounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
            }
        });

        //about my app
        ll_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        //Contact us
        ll_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{Config.contactUsEmail});
                intent.putExtra(Intent.EXTRA_SUBJECT, Config.emailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, Config.emailBodyText);
                try {
                    startActivity(Intent.createChooser(intent, "Send mail"));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //rate my app
        ll_rate_app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setMessage(getResources().getString(
                        R.string.ratethisapp_msg));
                builder.setTitle(getResources().getString(
                        R.string.ratethisapp_title));
                builder.setPositiveButton(
                        getResources().getString(R.string.rate_it),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                Intent fire = new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName()));           //dz.amine.thequotesgarden"));
                                startActivity(fire);

                            }
                        });

                builder.setNegativeButton(
                        getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();

                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });

        //share app on social media
        ll_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBodyText = "https://play.google.com/store/apps/details?id="+getPackageName();
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT,shareBodyText);
                startActivity(Intent.createChooser(intent,"share via"));
            }
        });

        //read our privacy policy
        ll_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
            }
        });
    }

    private void reloadCards() {
        quoteList.addAll(db.getAllQuotes(" limit 20"));
        quoteAdapter.notifyDataSetChanged();
    }

    private void loadAds () {
        if (Config.SHOW_ADS) {
            if (Config.ADS_NETWORK) {
                AdView adView = new AdView(this);
                adView.setAdUnitId(Config.BANNER_ID);
                adView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
                LinearLayout layout = (LinearLayout) findViewById(R.id.adView);
                layout.addView(adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            } else {
                com.facebook.ads.AdView mAdView = new com.facebook.ads.AdView(this, Config.FACEBOOK_BANNER_ID, AdSize.BANNER_HEIGHT_50);
                LinearLayout adContainer = (LinearLayout) findViewById(R.id.adView);
                adContainer.addView(mAdView);
                mAdView.loadAd();
            }
        }
    }

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_about);

        Button dialog_btn=dialog.findViewById(R.id.btn_done);
        dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void givePermission() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_permission);

        TextView tv_policy_decline =dialog.findViewById(R.id.tv_policy_decline);
        TextView tv_give_per_dialog =dialog.findViewById(R.id.tv_give_per_dialog);

        tv_policy_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tv_give_per_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else {
            ActivityCompat.requestPermissions((Activity)this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    private void showAbout() {

        final Dialog customDialog;
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.layout_exit, null);
        customDialog = new Dialog(this, R.style.CustomDialog);
        customDialog.setContentView(customView);
        TextView no = customDialog.findViewById(R.id.tv_no);
        TextView yes = customDialog.findViewById(R.id.tv_yes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(context, "Exit", Toast.LENGTH_SHORT).show();
            }
        });


        customDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        initCheck();
        load();
    }

    private void initCheck() {
        if (prf.loadNightModeState()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void load () {
        /*if (prf.getString("VPN").equals(BuildConfig.APPLICATION_ID)){
        }else {
            finish();
        }*/
    }

    public void showDialog_pay() {
        @SuppressLint("WrongConstant") View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.dialog_subscribe, (ViewGroup) null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(inflate);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(R.color.white);
        TextView text_view_go_pro = (TextView) bottomSheetDialog.findViewById(R.id.text_view_go_pro);
        ((RelativeLayout) bottomSheetDialog.findViewById(R.id.relativeLayout_close_rate_gialog)).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        text_view_go_pro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(context, "Disable in Demo App", Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override

            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return true;
                }
                bottomSheetDialog.dismiss();
                return true;
            }
        });
        bottomSheetDialog.show();
        DialogOpened = true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            showDialog_pay();
        }

        return super.onOptionsItemSelected(item);

    }

    private void setupCardStackLayoutManager(CardStackLayoutManager cardStackLayoutManager) {
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setTranslationInterval(8);
        cardStackLayoutManager.setScaleInterval(0.95f);
        cardStackLayoutManager.setSwipeThreshold(0.3f);
        cardStackLayoutManager.setMaxDegree(20.0f);
        cardStackLayoutManager.setStackFrom(StackFrom.Right);
        cardStackLayoutManager.setCanScrollHorizontal(true);
        cardStackLayoutManager.setCanScrollVertical(false);
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL);
    }

    @Override
    public void onBackPressed() {
       showAbout();
    }

}
