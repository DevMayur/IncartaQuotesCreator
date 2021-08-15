package com.incarta.quotescreator;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.incarta.quotescreator.database.DataBaseHandler;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;


public class PhotosAdapter extends RecyclerView.Adapter implements ActivityCompat.OnRequestPermissionsResultCallback{

    private Context context;
    private List<Quote> wallpaperList;
    private int[] images;
    private int imagesIndex = 0;
    private int STORAGE_PERMISSION_CODE = 1;
    SharedPreferences sharedPrefs;
    PrefManager prf;
    private final int VIEW_PROG = -1;
    private AdLoader adLoader = null;

    private InterstitialAd mInterstitialAd;

    MainActivity mainActivity;


    public PhotosAdapter(Context context, List<Quote> wallpaperList, MainActivity activity) {
        this.context = context;
        this.wallpaperList = wallpaperList;
        this.mainActivity = activity;
        loadInterstitialAds();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        if (viewType >= 1000) {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ads, parent, false);
//            return new ADViewHolder(itemView);
//        } else
            {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_items, parent, false);
            return new WallpaperViewHolder(itemView);
        }

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof WallpaperViewHolder) {


            final Quote picture = wallpaperList.get(position);

            if (picture != null) {

                ((WallpaperViewHolder) holder).db = new DataBaseHandler(context);
                ((WallpaperViewHolder) holder).prf = new PrefManager(context);
                prf = new PrefManager(context);
                ((WallpaperViewHolder) holder).fav = picture.getFav();

                ((WallpaperViewHolder) holder).txtQuote.setText(picture.getQuote());

                //Quote Maker Button
                ((WallpaperViewHolder) holder).quote_maker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent favorites = new Intent(context,
                                MakerActivity.class);
                        favorites.putExtra("quote", picture.getQuote());
                        context.startActivity(favorites);
                    }
                });

                String[] mColors = {
                        "#1D5E95",
                        "#13D6EB",
                        "#9055F7",
                        "#D60B77",
                        "#7ACB9F",
                        "#158BB2",
                        "#D75A6C",
                        "#F27DBD",
                        "#F07146",
                        "#7EAB1D",
                        "#4F9A9F",
                        "#E707A5",
                        "#4E0698",
                        "#2E5AE1",
                        "#25685D",
                        "#5C5DBB",
                        "#FE6106",
                        "#920363",
                        "#ECB10B",
                        "#0587D5",
                        "#C02642",
                        "#B50DF9",
                        "#E7A1D5",
                        "#673AB7"
                };


                ((WallpaperViewHolder) holder).relativeLayout.setBackgroundColor(Color.parseColor(mColors[position % 24]));

                //Change Random Backgrounds
                ((WallpaperViewHolder) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        int numOfImages = 31;
                        images = new int[numOfImages];
                        images[0] = R.drawable.img1;
                        images[1] = R.drawable.img2;
                        images[2] = R.drawable.img3;
                        images[3] = R.drawable.img4;
                        images[4] = R.drawable.img5;
                        images[5] = R.drawable.img6;
                        images[6] = R.drawable.img7;
                        images[7] = R.drawable.img8;
                        images[8] = R.drawable.img9;
                        images[9] = R.drawable.img10;
                        images[10] = R.drawable.img11;
                        images[11] = R.drawable.img12;
                        images[12] = R.drawable.img13;
                        images[13] = R.drawable.img14;
                        images[14] = R.drawable.img15;
                        images[15] = R.drawable.img16;
                        images[16] = R.drawable.img17;
                        images[17] = R.drawable.img18;
                        images[18] = R.drawable.img19;
                        images[19] = R.drawable.img20;
                        images[20] = R.drawable.img21;
                        images[21] = R.drawable.img22;
                        images[22] = R.drawable.img23;
                        images[23] = R.drawable.img24;
                        images[24] = R.drawable.img25;
                        images[25] = R.drawable.img26;
                        images[26] = R.drawable.img27;
                        images[27] = R.drawable.img28;
                        images[28] = R.drawable.img29;
                        images[29] = R.drawable.img30;
                        images[30] = R.drawable.img31;

                        ((WallpaperViewHolder) holder).relativeLayout.setBackgroundResource(images[imagesIndex]);
                        ++imagesIndex;  // update index, so that next time it points to next resource
                        if (imagesIndex == images.length - 1)
                            imagesIndex = 0; // if we have reached at last index of array, simply restart from beginning
                        allSound();
                    }
                });


                if (((WallpaperViewHolder) holder).fav.equals("0")) {
                    //finalHolder.favBtn.setImageResource(R.mipmap.not_fav);
                    ((WallpaperViewHolder) holder).favBtn.setLiked(false);
                    ((WallpaperViewHolder) holder).likeText.setText("Like");

                }
                if (((WallpaperViewHolder) holder).fav.equals("1")) {
                    //finalHolder.favBtn.setImageResource(R.mipmap.fav);
                    ((WallpaperViewHolder) holder).favBtn.setLiked(true);
                    ((WallpaperViewHolder) holder).likeText.setText("Liked");

                }


                ((WallpaperViewHolder) holder).favBtn.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {

                        if (picture.getFav().equals("0")) {
                            picture.setFav("1");
                            ((WallpaperViewHolder) holder).db.updateQuote(picture);
                            ((WallpaperViewHolder) holder).favBtn.setLiked(true);
                            ((WallpaperViewHolder) holder).likeText.setText("Liked");
                            startSound();
                        } else if (picture.getFav().equals("1")) {
                            picture.setFav("0");
                            ((WallpaperViewHolder) holder).db.updateQuote(picture);
                            ((WallpaperViewHolder) holder).favBtn.setLiked(false);
                            ((WallpaperViewHolder) holder).likeText.setText("Like");

                            startSound();
                        }

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                        if (picture.getFav().equals("0")) {
                            picture.setFav("1");
                            ((WallpaperViewHolder) holder).db.updateQuote(picture);
                            ((WallpaperViewHolder) holder).favBtn.setLiked(true);
                            ((WallpaperViewHolder) holder).likeText.setText("Liked");
                        } else if (picture.getFav().equals("1")) {
                            picture.setFav("0");
                            ((WallpaperViewHolder) holder).db.updateQuote(picture);
                            ((WallpaperViewHolder) holder).favBtn.setLiked(false);
                            ((WallpaperViewHolder) holder).likeText.setText("Like");
                            startSound();
                        }

                    }
                });

                //when you press save button

                ((WallpaperViewHolder) holder).ll_quote_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            ((WallpaperViewHolder) holder).tv_quotes_watermark.setVisibility(View.VISIBLE);
                            ((WallpaperViewHolder) holder).quote_maker.setVisibility(View.INVISIBLE);
                            Bitmap bitmap = Bitmap.createBitmap(((WallpaperViewHolder) holder).relativeLayout.getWidth(), ((WallpaperViewHolder) holder).relativeLayout.getHeight(),
                                    Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            ((WallpaperViewHolder) holder).relativeLayout.draw(canvas);

                            OutputStream fos;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                ContentResolver resolver = context.getContentResolver();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
                                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                                Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show();
                                ((WallpaperViewHolder) holder).tv_save_quote.setText("Saved");
                                ((WallpaperViewHolder) holder).iv_save_quote.setImageResource(R.drawable.ic_menu_check);
                                try {
                                    fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                                    fos.flush();
                                    fos.close();


                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                ((WallpaperViewHolder) holder).tv_quotes_watermark.setVisibility(View.INVISIBLE);
                                ((WallpaperViewHolder) holder).quote_maker.setVisibility(View.INVISIBLE);
                                startSound();
                            } else {

                                FileOutputStream outputStream = null;

                                File sdCard = Environment.getExternalStorageDirectory();

                                File directory = new File(sdCard.getAbsolutePath() + "/Latest Quotes");
                                directory.mkdir();

                                String filename = String.format("%d.jpg", System.currentTimeMillis());

                                File outFile = new File(directory, filename);

                                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                                ((WallpaperViewHolder) holder).tv_save_quote.setText("Saved");
                                ((WallpaperViewHolder) holder).iv_save_quote.setImageResource(R.drawable.ic_menu_check);


                                try {
                                    outputStream = new FileOutputStream(outFile);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                                    outputStream.flush();
                                    outputStream.close();

                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    intent.setData(Uri.fromFile(outFile));
                                    context.sendBroadcast(intent);


                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ((WallpaperViewHolder) holder).tv_quotes_watermark.setVisibility(View.INVISIBLE);
                                ((WallpaperViewHolder) holder).quote_maker.setVisibility(View.VISIBLE);
                                startSound();

                            }

                        } else {

                            //show permission popup
                            requestStoragePermission();

                        }

                    }
                });

                //When You Press copy Botton
                ((WallpaperViewHolder) holder).ll_copy_quote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", picture._quote);
                        assert clipboard != null;
                        clipboard.setPrimaryClip(clip);
                        startSound();
                        Toast.makeText(context, "Quotes Copied", Toast.LENGTH_SHORT).show();
                    }
                });

                //When You Press Share Button
                ((WallpaperViewHolder) holder).ll_quote_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup();
                        startSound();
                    }

                    private void popup() {
                        PopupMenu popup = new PopupMenu(context, ((WallpaperViewHolder) holder).ll_quote_share);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.sub_text:
                                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                        shareIntent.setType("text/plain");
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, picture._quote + "\n https://play.google.com/store/apps/details?id=" + context.getPackageName());
                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Premium Quotes");
                                        context.startActivity(Intent.createChooser(shareIntent, "Share Quote"));
                                        Toast.makeText(context, "Share as Text", Toast.LENGTH_SHORT).show();
                                        return true;
                                    case R.id.sub_image:
                                        ((WallpaperViewHolder) holder).tv_quotes_watermark.setVisibility(View.VISIBLE);
                                        Bitmap bitmap = Bitmap.createBitmap(((WallpaperViewHolder) holder).relativeLayout.getWidth(), ((WallpaperViewHolder) holder).relativeLayout.getHeight(),
                                                Bitmap.Config.ARGB_8888);
                                        Canvas canvas = new Canvas(bitmap);
                                        ((WallpaperViewHolder) holder).relativeLayout.draw(canvas);
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("*/*");
                                        intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + context.getPackageName());
                                        context.startActivity(Intent.createChooser(intent, "Premium Quotes"));
                                        ((WallpaperViewHolder) holder).tv_quotes_watermark.setVisibility(View.INVISIBLE);
                                        Toast.makeText(context, "Share as Image", Toast.LENGTH_SHORT).show();

                                        return true;
                                }
                                return false;
                            }
                        });
                        popup.inflate(R.menu.menu_item);

                        popup.show();
                        startSound();
                    }
                });
            }
        }
    }

    //Share image tool
    private Uri getLocalBitmapUri(Bitmap bitmap) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "wallpaper" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    //Like , Save , Copy , share - Sound Effect
    private void startSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(context,R.raw.water);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean speaker = sharedPrefs.getBoolean("prefSpeaker", true);

        if (prf.getBoolean("SOUND")==true) {

            if (speaker.equals(true)) {

                likeSound.start();

            } else {

                likeSound.stop();
            }
        }else {

        }

    }

    //Sound Effect
    private void allSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(context,R.raw.all);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean speaker = sharedPrefs.getBoolean("prefSpeaker", true);

        if (prf.getBoolean("SOUND")==true) {

            if (speaker.equals(true)) {

                likeSound.start();

            } else {

                likeSound.stop();
            }
        }else {

        }

    }

    //Permisssion for save images
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(context, "Permission ok", Toast.LENGTH_SHORT).show();

            }else

                Toast.makeText(context, "Permission not allow", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public boolean isHeader(int position) {
        return position == wallpaperList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return VIEW_PROG;
        } else if (wallpaperList.get(position) == null) {
            return 1000 + position;
        } else {
            return position;
        }
    }

    class WallpaperViewHolder extends RecyclerView.ViewHolder{

        TextView txtQuote;
        ImageView imageView;
        TextView   tv_quotes_watermark , tv_save_quote;
        TextView likeText;
        TextView txtCategory;
        ImageView iv_save_quote, quote_maker;
        RelativeLayout relativeLayout;
        LinearLayout ll_quote_save, ll_copy_quote, ll_quote_share;
        ImageView imgIcon;
        LikeButton favBtn;
        String fav;
        private DataBaseHandler db;
        PrefManager prf;


        public WallpaperViewHolder(View itemView) {
            super(itemView);

            txtQuote = itemView.findViewById(R.id.txtQuote);
            relativeLayout = itemView.findViewById(R.id.llBackground);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tv_quotes_watermark = itemView.findViewById(R.id.tv_quotes_watermark);
            likeText = itemView.findViewById(R.id.tv_like_quote_text);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            ll_copy_quote = itemView.findViewById(R.id.ll_copy_quote);
            ll_quote_save = itemView.findViewById(R.id.ll_quote_save);
            ll_quote_share = itemView.findViewById(R.id.ll_quote_share);
            tv_save_quote = itemView.findViewById(R.id.tv_save_quote);
            iv_save_quote = itemView.findViewById(R.id.iv_save_quote);
            quote_maker = itemView.findViewById(R.id.quote_maker);

            favBtn = itemView.findViewById(R.id.favBtn);


        }


    }


    private void loadInterstitialAds() {
        if (Config.ADS_NETWORK) {
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(context,Config.INTER_ID, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            mInterstitialAd.show(((Activity)context));
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            mInterstitialAd = null;
                        }
                    });
        }
    }



    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(context)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else {
            ActivityCompat.requestPermissions((Activity)context,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }
}
