package com.incarta.quotescreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import android.Manifest;
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
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
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
import android.widget.ArrayAdapter;
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

import com.incarta.quotescreator.database.DataBaseHandler;
import com.like.LikeButton;
import com.like.OnLikeListener;


public class QuotesListAdapter extends ArrayAdapter<Quote> implements ActivityCompat.OnRequestPermissionsResultCallback{
    Context context;
    int layoutResourceId;
    private int lastPosition = -1;
    private RoundImage roundedImage;
    ArrayList<Quote> data = new ArrayList<Quote>();
    private ArrayList<Quote> myList = new ArrayList<Quote>();
    SharedPreferences sharedPrefs;
    private int[] images;
    private int imagesIndex = 0;
    private int STORAGE_PERMISSION_CODE = 1;


    public QuotesListAdapter(Context context, int layoutResourceId,
                             ArrayList<Quote> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View row = convertView;

        ImageHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ImageHolder();
            //holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
            holder.relativeLayout = row.findViewById(R.id.llBackground);
            holder.imgIcon = row.findViewById(R.id.imgIcon);
            holder.txtQuote = row.findViewById(R.id.txtQuote);
            holder.tv_quotes_watermark = row.findViewById(R.id.tv_quotes_watermark);
            holder.likeText = row.findViewById(R.id.tv_like_quote_text);
            holder.txtCategory = row.findViewById(R.id.txtCategory);
            holder.ll_copy_quote = row.findViewById(R.id.ll_copy_quote);
            holder.ll_quote_save = row.findViewById(R.id.ll_quote_save);
            holder.ll_quote_share = row.findViewById(R.id.ll_quote_share);
            holder.tv_save_quote = row.findViewById(R.id.tv_save_quote);
            holder.iv_save_quote = row.findViewById(R.id.iv_save_quote);
            holder.quote_maker = row.findViewById(R.id.quote_maker);

            holder.favBtn = row.findViewById(R.id.favBtn);

            Typeface font = Typeface.createFromAsset(context.getAssets(),
                    "fonts/montserrat_bold.ttf");
            holder.txtQuote.setTypeface(font);
            holder.txtCategory.setTypeface(font);
            row.setTag(holder);
        } else {
            holder = (ImageHolder) row.getTag();
        }


        final Quote picture = data.get(position);

        holder.db = new DataBaseHandler(context);
        holder.fav = picture.getFav();
        holder.txtQuote.setText(picture.getQuote());
        holder.txtCategory.setText(picture.getCategory());

        final ImageHolder finalHolder1 = holder;

        //Quote Maker Button
        holder.quote_maker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent favorites = new Intent(context,
                        MakerActivity.class);
                favorites.putExtra("quote", picture.getQuote());
                context.startActivity(favorites);
            }
        });

        //Change Random Backgrounds
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {

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

                finalHolder1.relativeLayout.setBackgroundResource(images[imagesIndex]);
                ++imagesIndex;  // update index, so that next time it points to next resource
                if (imagesIndex == images.length - 1)
                    imagesIndex = 0; // if we have reached at last index of array, simply restart from beginning
                allSound();
            }
        });


        final ImageHolder finalHolder = holder;

        if (holder.fav.equals("0")) {
            //finalHolder.favBtn.setImageResource(R.mipmap.not_fav);
            finalHolder.favBtn.setLiked(false);
            finalHolder.likeText.setText("Like");

        }
        if (holder.fav.equals("1")) {
            //finalHolder.favBtn.setImageResource(R.mipmap.fav);
            finalHolder.favBtn.setLiked(true);
            finalHolder.likeText.setText("Liked");

        }


        holder.favBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                if (picture.getFav().equals("0")) {
                    picture.setFav("1");
                    finalHolder.db.updateQuote(picture);
                    finalHolder.favBtn.setLiked(true);
                    finalHolder.likeText.setText("Liked");
                    startSound();
                } else if (picture.getFav().equals("1")) {
                    picture.setFav("0");
                    finalHolder.db.updateQuote(picture);
                    finalHolder.favBtn.setLiked(false);
                    finalHolder.likeText.setText("Like");

                    startSound();
                }

            }

            @Override
            public void unLiked(LikeButton likeButton) {

                if (picture.getFav().equals("0")) {
                    picture.setFav("1");
                    finalHolder.db.updateQuote(picture);
                    finalHolder.favBtn.setLiked(true);
                    finalHolder.likeText.setText("Liked");
                } else if (picture.getFav().equals("1")) {
                    picture.setFav("0");
                    finalHolder.db.updateQuote(picture);
                    finalHolder.favBtn.setLiked(false);
                    finalHolder.likeText.setText("Like");
                    startSound();
                }

            }
        });

        //when you press save button
        final ImageHolder finalHolder2 = holder;
        holder.ll_quote_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    finalHolder.tv_quotes_watermark.setVisibility(View.VISIBLE);
                    finalHolder2.quote_maker.setVisibility(View.INVISIBLE);
                    Bitmap bitmap = Bitmap.createBitmap(finalHolder.relativeLayout.getWidth(), finalHolder.relativeLayout.getHeight(),
                            Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    finalHolder.relativeLayout.draw(canvas);

                    OutputStream fos;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ContentResolver resolver = context.getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                        Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show();
                        finalHolder.tv_save_quote.setText("Saved");
                        finalHolder.iv_save_quote.setImageResource(R.drawable.ic_menu_check);
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
                        finalHolder.tv_quotes_watermark.setVisibility(View.INVISIBLE);
                        finalHolder2.quote_maker.setVisibility(View.INVISIBLE);
                        startSound();
                    } else {

                        FileOutputStream outputStream = null;

                        File sdCard = Environment.getExternalStorageDirectory();

                        File directory = new File(sdCard.getAbsolutePath() + "/Latest Quotes");
                        directory.mkdir();

                        String filename = String.format("%d.jpg", System.currentTimeMillis());

                        File outFile = new File(directory, filename);

                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                        finalHolder.tv_save_quote.setText("Saved");
                        finalHolder.iv_save_quote.setImageResource(R.drawable.ic_menu_check);



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

                        finalHolder.tv_quotes_watermark.setVisibility(View.INVISIBLE);
                        finalHolder2.quote_maker.setVisibility(View.VISIBLE);
                        startSound();

                    }

                }else{

                    //show permission popup
                    requestStoragePermission();

                }

            }
        });

        //When You Press copy Botton
        holder.ll_copy_quote.setOnClickListener(new View.OnClickListener() {
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
        holder.ll_quote_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
                startSound();
            }

            private void popup() {
                PopupMenu popup = new PopupMenu(context, finalHolder.ll_quote_share);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.sub_text:
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, picture._quote + "\n https://play.google.com/store/apps/details?id="+context.getPackageName());
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Premium Quotes");
                                context.startActivity(Intent.createChooser(shareIntent, "Share Quote"));
                                Toast.makeText(context, "Share as Text", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.sub_image:
                                finalHolder.tv_quotes_watermark.setVisibility(View.VISIBLE);
                                Bitmap bitmap = Bitmap.createBitmap(finalHolder.relativeLayout.getWidth(), finalHolder.relativeLayout.getHeight(),
                                        Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmap);
                                finalHolder.relativeLayout.draw(canvas);
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+context.getPackageName());
                                context.startActivity(Intent.createChooser(intent, "Premium Quotes"));
                                finalHolder.tv_quotes_watermark.setVisibility(View.INVISIBLE);
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

        boolean isExist = false;
        AssetManager assetManager = context.getAssets();
        InputStream imageStream = null;
        try {
            imageStream = assetManager.open("categories/"+picture.getCategory()+".jpg");

            isExist =true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (isExist != false){
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            roundedImage = new RoundImage(theImage);
            holder.imgIcon.setImageDrawable(roundedImage );
        }
        else {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.author);
            roundedImage = new RoundImage(bm);
            holder.imgIcon.setImageDrawable(roundedImage);
        }
        return row;
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

        if (speaker.equals(true)) {

            likeSound.start();

        }else{

            likeSound.stop();
        }

    }

    //Sound Effect
    private void allSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(context,R.raw.all);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean speaker = sharedPrefs.getBoolean("prefSpeaker", true);

        if (speaker.equals(true)) {

            likeSound.start();

        }else{

            likeSound.stop();
        }

    }

    //View Image Item Type
    @Override
    public int getViewTypeCount() {
        if (getCount() > 0) {
            return getCount();
        } else {
            return super.getViewTypeCount();
        }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position)
    {
        return position;
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

    static class ImageHolder {
        TextView   tv_quotes_watermark , tv_save_quote;
        TextView txtQuote , likeText;
        TextView txtCategory;
        ImageView iv_save_quote, quote_maker;
        RelativeLayout relativeLayout;
        private Quote qte;
        LinearLayout ll_quote_save, ll_copy_quote, ll_quote_share;
        ImageView imgIcon;
        LikeButton favBtn;
        private int ID;
        private String mode,fav,text;

        private DataBaseHandler db;

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
