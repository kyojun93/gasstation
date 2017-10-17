package com.example.kyo.gasstation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends drawer
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private Button LocalGS,InterestGS,NearbyGS,CardIF,GasIF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        LocalGS.setOnClickListener(this);
        InterestGS.setOnClickListener(this);
        CardIF.setOnClickListener(this);
        GasIF.setOnClickListener(this);
        NearbyGS.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                    i = new Intent(MainActivity.this, map.class);
                    break;
            case R.id.InterestGS:
                i = new Intent(MainActivity.this, InterestGS.class);
                break;
            case R.id.GasIF:
                i = new Intent(MainActivity.this, GasIf.class);
                break;
        }
        startActivity(i);
    }
    public void init(){
        LocalGS=(Button)findViewById(R.id.LocalGS);
        InterestGS=(Button)findViewById(R.id.InterestGS);
        NearbyGS=(Button)findViewById(R.id.NearbyGS);
        CardIF=(Button)findViewById(R.id.Card);
        GasIF=(Button)findViewById(R.id.GasIF);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
