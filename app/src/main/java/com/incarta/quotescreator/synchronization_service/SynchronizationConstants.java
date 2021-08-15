package com.incarta.quotescreator.synchronization_service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.incarta.quotescreator.MainActivity;
import com.incarta.quotescreator.database.DataBaseHandler;
import com.mayurkakade.networkcall.NetworkTask;
import com.mayurkakade.networkcall.TaskRunner;
import com.mayurkakade.networkcall.iOnDataFetched;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SynchronizationConstants {

    public static final boolean isSynchronizationEnabled = false;
//    public static final String synchronizationApiUrl = "https://i2a.app/quotes_app/";
    public static final String synchronizationApiUrl = "https://your_base_url.com/";
    public static String Field_ID = "id";
    public static String Field_FILENAME = "filename";
    public static String Field_DATE = "date";
    public static String Field_DB_VERSION = "1";
    public static String Field_DB_PREFS_TAG = "sync_prefs";
    public static String Field_DB_VERSION_PREF = "db_version_sync";
    public static String Author_Name = "enter author name here";

    static TaskRunner runner = new TaskRunner();

    public static void getDatabaseSynchronizerConfig(Context context, OnUpdateDatabase onUpdateDatabase) {
        Log.d(TAG, "getDatabaseSynchronizerConfig: ");
        onUpdateDatabase.onStart();
        //progressBar.setVisibility(View.VISIBLE);
        //progressBar.setVisibility(View.GONE);
        iOnDataFetched onDataFetched = new iOnDataFetched() {
            @Override
            public void showProgressBar() {
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideProgressBar() {
                //progressBar.setVisibility(View.GONE);
            }

            @Override
            public void setDataInPageWithResult(Object result) {
                if (result != null) {
                    String jsonData = result.toString();
                    processData(jsonData, context, onUpdateDatabase);
                } else {
                    onUpdateDatabase.onUpdateFailed();
                    Log.d(TAG, "setDataInPageWithResult: ");
                }
            }
        };
        
        runner.executeAsync(new NetworkTask(onDataFetched,SynchronizationConstants.synchronizationApiUrl + "synchronizer.php"));
        
    }

    public static String TAG = "NetworkTask";

    private static void processData(String jsonData, Context context, OnUpdateDatabase onUpdateDatabase) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int i = 0;
            JSONArray jsonArray = jsonObject.getJSONArray("index");
            if (i < jsonArray.length()) {

                JSONObject data = jsonArray.getJSONObject(i);
                String Local_Field_DB_VERSION = data.getString("db_version");
                Field_FILENAME = data.getString("filename");
                Field_DB_VERSION = data.getString("db_version");

                SharedPreferences sharedPreferences = context.getSharedPreferences(Field_DB_PREFS_TAG, Context.MODE_PRIVATE);
                int oldVersion = sharedPreferences.getInt(Field_DB_VERSION_PREF,1);

                if ( oldVersion < Integer.parseInt(Local_Field_DB_VERSION) ) {
                    onUpdateDatabase.onUpdateAvailable();
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            downloadDatabase(context, onUpdateDatabase);
                        }
                    });
                    t.start();
                } else {
                    onUpdateDatabase.onAlreadyUpdated();
                }


            }
        }catch(Exception e) {
            Log.d(TAG, " exception : " + e.getMessage());
        }
    }

    private static boolean downloadDatabase(Context context, OnUpdateDatabase onUpdateDatabase) {

        File tempFile = context.getDatabasePath(new DataBaseHandler(context).getDatabaseName());
        Log.d(TAG, "downloadDatabase: " + tempFile.getAbsolutePath());


        try {
            onUpdateDatabase.onUpdateDownloading();
            // Log.d(TAG, "downloading database");
            URL url = new URL( synchronizationApiUrl + Field_FILENAME);
            /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();
            /*
             * Define InputStreams to read from the URLConnection.
             */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            /*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             */
            FileOutputStream fos = new FileOutputStream(tempFile);
            int current = 0;
            while ((current = bis.read()) != -1) {
                fos.write(current);
            }
            fos = context.openFileOutput(Field_FILENAME, Context.MODE_PRIVATE);
            fos.close();


            SharedPreferences sharedPreferences = context.getSharedPreferences(Field_DB_PREFS_TAG, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Field_DB_VERSION_PREF,Integer.parseInt(Field_DB_VERSION));
            editor.apply();

            onUpdateDatabase.onUpdateSucceed();

            // Log.d(TAG, "downloaded");
        } catch (Exception e) {
            onUpdateDatabase.onUpdateFailed();
            return false;
        }
        return true;
    }

}
