package com.example.kyo.gasstation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Card_info extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView img;
    TextView name,info,annualfee;
    DrawerLayout drawer;
    Intent i;
    Bitmap bit;
    String url;
    ArrayList<HashMap<String, String>> hashlist;
    ArrayList<HashMap<String, String>> maplist;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);
        init();
       HashMap<String, String> hash = new HashMap<>();
        hash = hashlist.get(pos);
       url = hash.get("img");
        Log.e("kyo", url);
        new Thread(new Runnable() {
            public void run() {
                try {
                    bit = getBitmap(url);
                }catch(Exception e) {

                }finally {
                    if(bit!=null) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                img.setImageBitmap(bit);
                            }
                        });
                    }
                }
            }
        }).start();
        name.setText(hash.get("name"));
        info.setText(hash.get("info"));
        annualfee.setText(hash.get("annualfee"));
    }
    public void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

       drawer = (DrawerLayout) findViewById(R.id.Cardinfo_dr);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        hashlist = new ArrayList<>();
        i = getIntent();
        hashlist = new ArrayList<>();
        maplist = new ArrayList<>();
        hashlist = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("hashlist");
        maplist = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("maplist");
        pos = (Integer)i.getSerializableExtra("center");
        img = (ImageView)findViewById(R.id.cardimg);
        name = (TextView)findViewById(R.id.cardname);
        info = (TextView)findViewById(R.id.cardinfo2);
        annualfee = (TextView)findViewById(R.id.annualfee);
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
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
            i.putExtra("hashlist", maplist);
            i.putExtra("center", -1);
        }
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try{
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true); //url로 input받는 flag 허용
            connection.connect(); //연결
            is = connection.getInputStream(); // get inputstream
            retBitmap = BitmapFactory.decodeStream(is);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(connection!=null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }
}
