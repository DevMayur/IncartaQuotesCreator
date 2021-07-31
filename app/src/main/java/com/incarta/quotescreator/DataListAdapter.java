package com.incarta.quotescreator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.res.AssetManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;




/**
 * Created by Amine on 19/08/2015.
 */
public class DataListAdapter extends ArrayAdapter<Quote> {

    Context context;
    int layoutResourceId;
    private int lastPosition = -1;
    private RoundImage roundedImage;
    ArrayList<Quote> data = new ArrayList<Quote>();

    public DataListAdapter(Context context, int layoutResourceId,
                           ArrayList<Quote> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        ImageHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ImageHolder();
            holder.txtName = (TextView) row.findViewById(R.id.txtName);
            holder.imgAuth = (ImageView) row.findViewById(R.id.imgAuth);
            holder.txtCounter = (TextView) row.findViewById(R.id.AuthCounter);
            Typeface font = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Light.ttf");
            holder.txtName.setTypeface(font);
            holder.txtCounter.setTypeface(font);
            row.setTag(holder);
        } else {
            holder = (ImageHolder) row.getTag();
        }

        Quote picture = data.get(position);
        holder.txtName.setText(picture.getName());
        holder.txtCounter.setText("   " + picture.getCount() + "   ");

        // AssetManager assetManager = context.getAssets();
        boolean isExist = false;
        AssetManager assetManager = context.getAssets();
        InputStream imageStream = null;
        try {
            imageStream = assetManager.open("authors/" + picture.getFileName()
                    + ".jpg");

            isExist = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (isExist != false) {
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            roundedImage = new RoundImage(theImage);
            holder.imgAuth.setImageDrawable(roundedImage);
        } else {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.author);
            roundedImage = new RoundImage(bm);
            holder.imgAuth.setImageDrawable(roundedImage);
        }

        return row;
    }

    static class ImageHolder {
        TextView txtCounter;
        ImageView imgAuth;
        TextView txtName;

    }
}
