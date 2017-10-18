package com.example.kyo.gasstation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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


public class NearbyGS extends location implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    public Button SLGas, SLO, SLKm;
    ListView GasIFLV;
    Intent i;
    int km = 3;
    String sort ;
    String oil = "gasoline";
    String mJsonString,s;
    ArrayList<HashMap<String, String>> list;
    ArrayList<HashMap<String, String>> hashlist;

    Location userlocation;
    DrawerLayout drawer;
    CharSequence gasinfo[] = new CharSequence[] {"휘발유", "경유"};
    CharSequence KM[] = new CharSequence[] {"1Km","3km", "5km", "10km" };
    CharSequence O[] = new CharSequence[] {"최저가순", "거리순" };
    GetData task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_gs);
        init();
        newurl();
        SLGas.setOnClickListener(this);
        SLO.setOnClickListener(this);
        SLKm.setOnClickListener(this);
        GasIFLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i = new Intent(NearbyGS.this, map.class);
                i.putExtra("hashlist", hashlist);
                i.putExtra("center", position);
                startActivity(i);
            }
        });
        GasIFLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                i = new Intent(NearbyGS.this, gas_station_if.class);
                i.putExtra("hashlist", hashlist);
                i.putExtra("center", position);
                startActivity(i);
                return true;
            }
        });
    }
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.NearbyGSLa);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void onClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (v.getId()){
            case R.id.SelectGas:
                builder.setItems(gasinfo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                SLGas.setText(gasinfo[0]);
                                oil = "gasoline";
                                newurl();
                                break;
                            case 1:
                                SLGas.setText(gasinfo[1]);
                                oil = "diesel";
                                newurl();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();

                break;
            case R.id.SelectKM:
                builder.setItems(KM, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                SLKm.setText(KM[0]);
                                km = 1;
                                newurl();
                                break;
                            case 1:
                                SLKm.setText(KM[1]);
                                km = 3;
                                newurl();
                                break;
                            case 2:
                                SLKm.setText(KM[2]);
                                km = 5;
                                newurl();
                                break;
                            case 3:
                                SLKm.setText(KM[3]);
                                km = 10;
                                newurl();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.SelectO:
                builder.setItems(O, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                SLO.setText(O[0]);
                                sort = oil;
                                newurl();
                                break;
                            case 1:
                                SLO.setText(O[1]);
                                sort = "distance";
                                newurl();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }
    public void newurl(){
        s = "http://124.80.191.179:3000/gasstation/"+userlocation.getLongitude()+"/"+userlocation.getLatitude()+"/"+km+"/"+oil+"/" + sort;
        Log.e("kyo",s);
        list.clear();
        task = new GetData();
        task.execute(s);
    }
    public void init(){
        sort = oil;
        GasIFLV = (ListView)findViewById(R.id.GasIFLV);
        SLGas =(Button)findViewById(R.id.SelectGas);
        SLO=(Button)findViewById(R.id.SelectO);
        SLKm=(Button)findViewById(R.id.SelectKM);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = new ArrayList<>();
        hashlist = new ArrayList<>();
        drawer = (DrawerLayout) findViewById(R.id.NearbyGSLa);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        settingGPS();
        userlocation = getMyLocation();

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
        } else if (id == R.id.nav_manage) {
            i = new Intent(this, InterestGS.class);
        }else if (id == R.id.nav_manage1) {
            i = new Intent(this, GasIf.class);
        }else if (id == R.id.nav_manage2) {
            i = new Intent(this, map.class);
        }
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(NearbyGS.this,
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

                String area = item.getString("area");
                String address = item.getString("address");
                String brand = item.getString("brand");
                String date = item.getString("date");
                String gasoline =item.getString("gasoline");
                String diesel = item.getString("diesel");

                String name = item.getString("name");
                String price = Integer.toString(item.getInt(oil))+"원";
                String distance = String.format("약 %.2fkm" , item.getDouble("distance"));
                HashMap<String,String> gashash = new HashMap<>();
                HashMap<String,String> hashMap = new HashMap<>();

                gashash.put("area",area);
                gashash.put("address",address);
                gashash.put("brand",brand);
                gashash.put("date",date);
                gashash.put("gasoline",gasoline);
                gashash.put("diesel",diesel);
                gashash.put("name", name);
                gashash.put("price", price);
                gashash.put("x",x);
                gashash.put("y",y);

                hashMap.put("name", name);
                hashMap.put("price", price);
                hashMap.put("distance", distance);

                hashlist.add(gashash);
                list.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    NearbyGS.this, list, R.layout.listview_custom,
                    new String[]{"name","price", "distance"},
                    new int[]{R.id.name, R.id.price, R.id.distance}
            );

            GasIFLV.setAdapter(adapter);

        } catch (JSONException e) {

        }

    }

}
