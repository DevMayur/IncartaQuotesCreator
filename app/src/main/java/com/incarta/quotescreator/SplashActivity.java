package com.incarta.quotescreator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;



public class SplashActivity extends Activity implements Runnable {

    PrefManager prf;
    TextView textView;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        prf = new PrefManager(this);
        utils = new Utils(this);

        textView = findViewById(R.id.textview);

            loadSplashScreen();
            load();
        findViewById(android.R.id.content).postDelayed(this, 3000);

    }

    private void load () {
        /*if (prf.getString("VPN").equals(BuildConfig.APPLICATION_ID)){
        }else {
            finish();
        }*/
    }

    private void loadSplashScreen () {
        //mQueue = Volley.newRequestQueue(this);

        //String uri = PrefManager.URL+Config.USERNAME;

        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray= response.getJSONArray("Json");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);

                        String PC = data.getString("PC");
                        String DN = data.getString("DN");
                        String PN = data.getString("PN");
                        String UN = data.getString("UN");
                        String MD = data.getString("MD");
                        prf.setString("VPC", PC);
                        prf.setString("VDN", DN);
                        prf.setString("VPN", PN);
                        prf.setString("VUN", UN);
                        prf.setString("VMD", MD);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);*/
    }

    @Override
    public void run() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
