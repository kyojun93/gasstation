package com.example.kyo.gasstation;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class gas_station_if extends location
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    DrawerLayout drawer;
    TextView name,address,gasoline,diesel,gasolinedate, dieseldate;
    Button btn1, btn2;
    Location userLocation;
    Intent i;
    HashMap<String, String> hash;
    ArrayList<HashMap<String,String>> hashlist;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_station_if);
        init();
        hash = new HashMap<>();
        hash = hashlist.get(pos);
        name.setText(hash.get("name"));
        address.setText(hash.get("address"));
        gasoline.setText(hash.get("gasoline")+"원");
        diesel.setText(hash.get("diesel")+"원");
        gasolinedate.setText(hash.get("date"));
        dieseldate.setText(hash.get("date"));
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    public void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.GSif);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        settingGPS();
        userLocation = getMyLocation();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        name = (TextView)findViewById(R.id.gsname);
        address = (TextView)findViewById(R.id.gsaddress);
        gasoline = (TextView)findViewById(R.id.gasoline);
        gasolinedate = (TextView)findViewById(R.id.gasolinedate);
        diesel = (TextView)findViewById(R.id.diesel);
        dieseldate = (TextView)findViewById(R.id.dieseldate);

        i = getIntent();
        hashlist = new ArrayList<>();
        hashlist = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("hashlist");
        pos = (Integer)i.getSerializableExtra("center");

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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()){
            case R.id.btn1:
                i = new Intent(gas_station_if.this, map.class);
                i.putExtra("hashlist", hashlist);
                i.putExtra("center", pos);
                break;
            case R.id.btn2:
                String url = "daummaps://route?sp="+userLocation.getLatitude()+","+userLocation.getLongitude()+"&ep="+hash.get("y")+","+hash.get("x")+"&by=CAR";
                Log.e("skdkd",url);
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //i = new Intent(MainActivity.this, GasIf.class);
                break;
        }
        startActivity(i);
    }
}
