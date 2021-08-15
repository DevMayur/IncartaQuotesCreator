package com.incarta.quotescreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.incarta.quotescreator.synchronization_service.OnUpdateDatabase;
import com.incarta.quotescreator.synchronization_service.SynchronizationConstants;


public class SplashActivity extends Activity {
    public static final String TAG = "NetworkTask";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating New Quotes");
        progressDialog.setTitle("Please Wait!");

        ((TextView)findViewById(R.id.tv_author)).setText(SynchronizationConstants.Author_Name);

        OnUpdateDatabase onUpdateDatabase = new OnUpdateDatabase() {
            @Override
            public void onStart() {
                Toast.makeText(SplashActivity.this, "Checking for updates ...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateAvailable() {
                progressDialog.show();
            }

            @Override
            public void onAlreadyUpdated() {
                Log.d(TAG, "onAlreadyUpdated: ");
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onUpdateDownloading() {
                Log.d(TAG, "onUpdateDownloading: ");
            }

            @Override
            public void onUpdateFailed() {
                Log.d(TAG, "onUpdateFailed: ");
                progressDialog.cancel();
                showFailedDialogues();
            }

            @Override
            public void onUpdateSucceed() {
                Log.d(TAG, "onUpdateSucceed: ");
                progressDialog.cancel();
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        
        if (SynchronizationConstants.isSynchronizationEnabled) {
            SynchronizationConstants.getDatabaseSynchronizerConfig(SplashActivity.this, onUpdateDatabase);
        } else {
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void showFailedDialogues() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(SplashActivity.this);
        builder1.setMessage(" Failed to update ! ");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                " Ok ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


}
