package com.example.kyo.gasstation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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


public class LocalGSResult extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    public Button LocalSelectGas, LocalSelectO;
    ListView locallist;
    Intent i;
    String local;
    String sort;
    String oil = "gasoline";
    String mJsonString,s;
    ArrayList<HashMap<String, String>> list;
    ArrayList<HashMap<String, String>> hashlist;
    DrawerLayout drawer;
    CharSequence gasinfo[] = new CharSequence[] {"휘발유", "경유"};
    CharSequence O[] = new CharSequence[] {"최저가순" };
    GetData task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_gsresult);
        init();
        newurl();
        task = new GetData();
        task.execute(s);
        LocalSelectGas.setOnClickListener(this);
        LocalSelectO.setOnClickListener(this);
        locallist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i = new Intent(LocalGSResult.this, map.class);
                i.putExtra("hashlist", hashlist);
                i.putExtra("center", position);
                startActivity(i);
            }
        });
        locallist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                i = new Intent(LocalGSResult.this, gas_station_if.class);
                i.putExtra("hashlist", hashlist);
                i.putExtra("center", position);
                startActivity(i);
                return true;
            }
        });
    }
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void onClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (v.getId()){
            case R.id.LocalSelectGas:
                builder.setItems(gasinfo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                LocalSelectGas.setText(gasinfo[0]);
                                oil = "gasoline";
                                newurl();
                                break;
                            case 1:
                                LocalSelectGas.setText(gasinfo[1]);
                                oil = "diesel";
                                newurl();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.LocalSelectO:
                builder.setItems(O, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                LocalSelectO.setText(O[0]);
                                sort = oil;
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
        s = "http://124.80.191.179:3000/local/'"+local+"'/"+sort;
        Log.e("kyo",s);
        list.clear();
        hashlist.clear();
        task = new GetData();
        task.execute(s);
    }
    public void init(){
        sort = oil;
        locallist = (ListView)findViewById(R.id.Locallist);
        hashlist = new ArrayList<>();
        LocalSelectGas =(Button)findViewById(R.id.LocalSelectGas);
        LocalSelectO=(Button)findViewById(R.id.LocalSelectO);
        i = getIntent();
        local = i.getStringExtra("local");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = new ArrayList<>();
        drawer = (DrawerLayout) findViewById(R.id.local_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LocalGSResult.this,
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

                String area = item.getString("area");
                String address = item.getString("address");
                String brand = item.getString("brand");
                String date = item.getString("date");
                String gasoline =item.getString("gasoline");
                String diesel = item.getString("diesel");
                String name = item.getString("name");
                String x = Double.toString(item.getDouble("x"));
                String y = Double.toString(item.getDouble("y"));
                String price = Integer.toString(item.getInt(oil))+"원";
                HashMap<String,String> hashMap = new HashMap<>();
                HashMap<String,String> gashash = new HashMap<>();
                gashash.put("name", name);
                gashash.put("price", price);
                gashash.put("x",x);
                gashash.put("y",y);

                gashash.put("area",area);
                gashash.put("address",address);
                gashash.put("brand",brand);
                gashash.put("date",date);
                gashash.put("gasoline",gasoline);
                gashash.put("diesel",diesel);

                hashMap.put("name", name);
                hashMap.put("price", price);

                hashlist.add(gashash);
                list.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    LocalGSResult.this, list, R.layout.listview_custom,
                    new String[]{"name","price"},
                    new int[]{R.id.name, R.id.price}
            );

            locallist.setAdapter(adapter);

        } catch (JSONException e) {

        }
    }
}
