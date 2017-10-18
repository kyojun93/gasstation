package com.example.kyo.gasstation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    String s,mJsonString;
    GetData task;
    ArrayList <HashMap<String,String>> hashlist;
    private Button LocalGS,NearbyGS,CardIF,GasIF;
    DrawerLayout drawer;
    int REQUEST_CODE_LOCATION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        newurl();
        LocalGS.setOnClickListener(this);
        CardIF.setOnClickListener(this);
        GasIF.setOnClickListener(this);
        NearbyGS.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public void onClick(View v){
            Intent i = new Intent();
            switch (v.getId()){
                case R.id.NearbyGS:
                    i = new Intent(MainActivity.this, com.example.kyo.gasstation.NearbyGS.class);
                    break;
                case R.id.LocalGS:
                    i = new Intent(MainActivity.this, LocalGS.class);
                    break;
                case R.id.Card:
                    i = new Intent(MainActivity.this, CardIF.class);
                    break;
            case R.id.GasIF:
                i = new Intent(MainActivity.this, GasIf.class);
                //i = new Intent(MainActivity.this, GasIf.class);
                break;
        }
        startActivity(i);
    }
    private void init(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 사용자 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        }
        hashlist = new ArrayList<>();
        LocalGS=(Button)findViewById(R.id.LocalGS);
        NearbyGS=(Button)findViewById(R.id.NearbyGS);
        CardIF=(Button)findViewById(R.id.Card);
        GasIF=(Button)findViewById(R.id.GasIF);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
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

            progressDialog = ProgressDialog.show(MainActivity.this,
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


    private void showResult(){
        try {
            JSONArray jsonArray = new JSONArray(mJsonString);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String x = Double.toString(item.getDouble("x"));
                String y = Double.toString(item.getDouble("y"));
                String name = item.getString("name");
                HashMap<String,String> gashash = new HashMap<>();
                gashash.put("name", name);
                gashash.put("x",x);
                gashash.put("y",y);

                hashlist.add(gashash);
            }

        } catch (JSONException e) {

        }

    }

}
