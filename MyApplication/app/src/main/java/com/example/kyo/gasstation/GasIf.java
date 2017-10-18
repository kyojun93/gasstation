package com.example.kyo.gasstation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class GasIf extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    DrawerLayout drawer;
    TextView oil,date,price;
    Button gasolien,diesel;
    GetData task;
    int a;
    String s,mJsonString,oil_str;
    ArrayList<HashMap<String,String>> hashlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_if);
        init();
        newurl();
        a = getprice(hashlist, oil_str);
        oil.setText("휘발유");
        date.setText(hashlist.get(0).get("date"));
        price.setText(a);
        diesel.setOnClickListener(this);
        gasolien.setOnClickListener(this);
    }
    public void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.gasif);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        date = (TextView)findViewById(R.id.gasif_date);
        oil = (TextView)findViewById(R.id.gasif_oil);
        price = (TextView)findViewById(R.id.gasif_price);
        gasolien = (Button)findViewById(R.id.btngasoline);
        diesel = (Button)findViewById(R.id.btndiesel);
        hashlist = new ArrayList<>();
        oil_str = "gasolien";
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btngasoline:
                oil_str = "gasolien";
                a = getprice(hashlist, oil_str);
                oil.setText("휘발유");
                date.setText(hashlist.get(0).get("date"));
                price.setText(a);
                break;
            case R.id.btndiesel:
                oil_str = "diesel";
                a = getprice(hashlist, oil_str);
                oil.setText("경유");
                date.setText(hashlist.get(0).get("date"));
                price.setText(a);
                break;
        }
    }
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.gasif);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public int getprice (ArrayList<HashMap<String, String>> gasif, String str){
        int p = 0;
        int z = 0;
        for(int i = 0; i<gasif.size(); i++){
            HashMap<String,String> hash = new HashMap<>();
            hash = gasif.get(i);
            p += Integer.parseInt(hash.get(str));
            z++;
        }
        return p/z;
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent i = new Intent();
        if (id == R.id.nav_camera) {
            i = new Intent(this, NearbyGS.class);
        } else if (id == R.id.nav_gallery) {
            i = new Intent(this, LocalGS.class);
        } else if (id == R.id.nav_slideshow) {
            i = new Intent(this, CardIF.class);
        } else if (id == R.id.nav_manage1) {
            i = new Intent(this, GasIf.class);
        }else if (id == R.id.nav_manage2) {
            i = new Intent(this, map.class);
            i.putExtra("hashlist", hashlist);
            i.putExtra("center", -1);
        }
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void newurl(){
        s = "http://124.80.191.179:3000/";
        Log.e("kyo",s);
        hashlist.clear();
        task = new GetData();
        task.execute(s);
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(GasIf.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mJsonString = result;
            Log.d("kyo", mJsonString);
            showResult();
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                errorString = e.toString();
                return null;
            }

        }
    }


    private void showResult() {
        try {
            JSONArray jsonArray = new JSONArray(mJsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String area = item.getString("area");
                String address = item.getString("address");
                String brand = item.getString("brand");
                String date = item.getString("date");
                String gasoline = item.getString("gasoline");
                String diesel = item.getString("diesel");
                String name = item.getString("name");
                String x = Double.toString(item.getDouble("x"));
                String y = Double.toString(item.getDouble("y"));
                ;

                HashMap<String, String> gashash = new HashMap<>();

                gashash.put("name", name);
                gashash.put("x", x);
                gashash.put("y", y);
                gashash.put("area", area);
                gashash.put("address", address);
                gashash.put("brand", brand);
                gashash.put("date", date);
                gashash.put("gasoline", gasoline);
                gashash.put("diesel", diesel);

                hashlist.add(gashash);
            }

        } catch (JSONException e) {

        }

    }
}
