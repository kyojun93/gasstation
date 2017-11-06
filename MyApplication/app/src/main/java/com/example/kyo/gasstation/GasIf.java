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
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

import static com.example.kyo.gasstation.R.id.gasif;

public class GasIf extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    DrawerLayout drawer;
    String ch;
    TextView oil, date, price;
    Button gasolien, diesel, gasif_local,gasif_ch;
    GetData task;
    GraphView graph;
    int a = 0;
    boolean ok;
    String s, mJsonString, oil_str, oil_str2;
    ArrayList<HashMap<String, String>> hashlist;
    ArrayList<HashMap<String, Double>> quarterlist;
    CharSequence local[] = new CharSequence[]{"서울 강남구", "서울 강동구", "서울 강북구", "서울 강서구", "서울 관악구", "서울 광진구",
            "서울 구로구", "서울 금천구", "서울 노원구", "서울 도봉구", "서울 동대문구", "서울 동작구", "서울 마포구",
            "서울 서대문구", "서울 서초구", "서울 성동구", "서울 성북구", "서울 송파구",
            "서울 양천구", "서울 영등포구", "서울 용산구", "서울 은평구", "서울 종로구", "서울 중구", "서울 중랑구"};
    CharSequence asd[] = new CharSequence[]{"월별","분기별", "년별"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_if);
        init();
        diesel.setOnClickListener(this);
        gasolien.setOnClickListener(this);
        gasif_local.setOnClickListener(this);
        gasif_ch.setOnClickListener(this);
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        graph = (GraphView) findViewById(R.id.graph);
        drawer = (DrawerLayout) findViewById(gasif);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        date = (TextView) findViewById(R.id.gasif_date);
        oil = (TextView) findViewById(R.id.gasif_oil);
        price = (TextView) findViewById(R.id.gasif_price);
        gasolien = (Button) findViewById(R.id.btngasoline);
        diesel = (Button) findViewById(R.id.btndiesel);
        gasif_local = (Button) findViewById(R.id.gasif_local);
        gasif_ch = (Button) findViewById(R.id.gasif_ch);
        hashlist = new ArrayList<>();
        quarterlist = new ArrayList<>();
        oil_str = "gasolien";
        oil_str2 = "gas";
        ch = "_quarter";
        ok = true;
        newurl();
    }

    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (v.getId()) {
            case R.id.btngasoline:
                oil_str = "gasoline";
                oil_str2 = "gas";
                a = getprice(hashlist, oil_str);
                oil.setText("휘발유");
                date.setText(hashlist.get(0).get("date"));
                price.setText(a / hashlist.size() + "원");
                newurl2();
                break;
            case R.id.btndiesel:
                oil_str = "diesel";
                oil_str2 = "die";
                a = getprice(hashlist, oil_str);
                oil.setText("경유");
                date.setText(hashlist.get(0).get("date"));
                price.setText(a / hashlist.size() + "원");
                newurl2();
                break;
            case R.id.gasif_local:
                builder.setItems(local, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gasif_local.setText(local[which]);
                        newurl2();
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.gasif_ch:
                builder.setItems(asd, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gasif_ch.setText(asd[which]);
                        switch (which){
                            case 0:
                                ch = "_month";
                                newurl2();
                                break;
                            case 1:
                                ch = "_quarter";
                                newurl2();
                                break;
                            case 2:
                                ch = "_year";
                                newurl2();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public int getprice(ArrayList<HashMap<String, String>> gasif, String str) {
        int p = 0;
        for (int i = 0; i < gasif.size(); i++) {
            HashMap<String, String> hash = new HashMap<>();
            hash = gasif.get(i);
            p += Integer.parseInt(hash.get(str));
        }
        return p;
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
        } else if (id == R.id.nav_manage2) {
            i = new Intent(this, map.class);
            i.putExtra("hashlist", hashlist);
            i.putExtra("center", -1);
        }
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void newurl() {
        s = "http://124.80.191.179:3000/";
        Log.e("kyo", s);
        hashlist.clear();
        task = new GetData();
        task.execute(s);
    }

    public void newurl2() {
        ok = false;
        s = "http://124.80.191.179:3000/kyo/'" + gasif_local.getText()+"'/" +  oil_str2 + "/" + ch;
        Log.e("kyo", s);
        quarterlist.clear();
        GetData task2 = new GetData();
        task2.execute(s);
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
            Log.d("kyo", mJsonString + "\n"+ok);
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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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
            Log.e("sksk", String.valueOf(jsonArray.length()));
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);
                if(ok) {
                    String area = item.getString("area");
                    String address = item.getString("address");
                    String brand = item.getString("brand");
                    String date = item.getString("date");
                    String gasoline = item.getString("gasoline");
                    String diesel = item.getString("diesel");
                    String name = item.getString("name");
                    String x = Double.toString(item.getDouble("x"));
                    String y = Double.toString(item.getDouble("y"));


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
                else if(ch.equals("_month")){
                    Double QT1 = item.getDouble("12monAgoAvg");
                    Double QT2 = item.getDouble("11monAgoAvg");
                    Double QT3 = item.getDouble("10monAgoAvg");
                    Double QT4 = item.getDouble("9monAgoAvg");
                    Double QT5 = item.getDouble("8monAgoAvg");
                    Double QT6 = item.getDouble("7monAgoAvg");
                    Double QT7 = item.getDouble("6monAgoAvg");
                    Double QT8 = item.getDouble("5monAgoAvg");
                    Double QT9 = item.getDouble("4monAgoAvg");
                    Double QT10 = item.getDouble("3monAgoAvg");
                    Double QT11 = item.getDouble("2monAgoAvg");
                    Double QT12 = item.getDouble("1monAgoAvg");

                    LineGraphSeries series = new LineGraphSeries();
                    //series.setThickness();
                    DataPoint dp[] = new DataPoint[]{
                            new DataPoint(1, QT12),
                            new DataPoint(2, QT11),
                            new DataPoint(3, QT10),
                            new DataPoint(4, QT9),
                            new DataPoint(5, QT8),
                            new DataPoint(6, QT7),
                            new DataPoint(7, QT6),
                            new DataPoint(8, QT5),
                            new DataPoint(9, QT4),
                            new DataPoint(10, QT3),
                            new DataPoint(11, QT2),
                            new DataPoint(12, QT1)};
                    series.resetData(dp);
                    graph.getGridLabelRenderer().setNumHorizontalLabels(12);
                    graph.setTitle(gasif_local.getText()+ " "+ gasif_ch.getText()+" 그래프");
                    graph.removeAllSeries();
                    graph.addSeries(series);
                }
                else if(ch.equals("_quarter")){
                    Double QT1 = item.getDouble("1qtAvg");
                    Double QT2 = item.getDouble("2qtAvg");
                    Double QT3 = item.getDouble("3qtAvg");
                    Double QT4 = item.getDouble("4qtAvg");
                    LineGraphSeries series = new LineGraphSeries();
                    //series.setThickness(4);
                    DataPoint dp[] = new DataPoint[]{
                            new DataPoint(1, QT1),
                            new DataPoint(2, QT2),
                            new DataPoint(3, QT3),
                            new DataPoint(4, QT4)};
                    series.resetData(dp);
                    graph.getGridLabelRenderer().setNumHorizontalLabels(4);
                    graph.setTitle(gasif_local.getText()+ " "+ gasif_ch.getText()+" 그래프");
                    graph.removeAllSeries();
                    graph.addSeries(series);
                }
                else if(ch.equals("_year")){
                    Double QT1 = item.getDouble("1yearAgoAvg");
                    Double QT2 = item.getDouble("2yearAgoAvg");
                    Double QT3 = item.getDouble("3yearAgoAvg");
                    LineGraphSeries series = new LineGraphSeries();
                    //series.setThickness(4);
                    DataPoint dp[] = new DataPoint[]{
                            new DataPoint(1, QT1),
                            new DataPoint(2, QT2),
                            new DataPoint(3, QT3)};
                    series.resetData(dp);
                    graph.getGridLabelRenderer().setNumHorizontalLabels(3);
                    graph.setTitle(gasif_local.getText()+ " "+ gasif_ch.getText()+" 그래프");
                    graph.removeAllSeries();
                    graph.addSeries(series);
                }
            }


        } catch (JSONException e) {

        }
    }
}