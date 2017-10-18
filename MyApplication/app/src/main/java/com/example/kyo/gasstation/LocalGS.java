package com.example.kyo.gasstation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LocalGS extends MainActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    DrawerLayout drawer;
    Button Selectlocal, ok;
    Intent i;
    CharSequence local[] = new CharSequence[] {"서울 강남구","서울 강동구","서울 강북구","서울 강서구","서울 관악구","서울 광진구",
            "서울 구로구","서울 금천구","서울 노원구","서울 도봉구","서울 동대문구","서울 동작구","서울 마포구",
            "서울 서대문구","서울 서초구","서울 성동구","서울 성북구","서울 송파구",
            "서울 양천구","서울 영등포구","서울 용산구","서울 은평구","서울 종로구","서울 중구","서울 중랑구"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_gs);
        init();
        Selectlocal.setOnClickListener(this);
        ok.setOnClickListener(this);
    }
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void init(){
        Selectlocal = (Button)findViewById(R.id.Selectlocal);
        ok = (Button)findViewById(R.id.Localok);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.LocalGSla);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (v.getId()){
            case R.id.Selectlocal:
                builder.setItems(local, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Selectlocal.setText(local[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.Localok:
                if(Selectlocal.getText().toString().equals("지역 선택")){
                    Toast.makeText(getApplicationContext(), "지역을 선택해 주세요", Toast.LENGTH_LONG).show();
                }
                else {
                    i = new Intent(LocalGS.this, LocalGSResult.class);
                    i.putExtra("local", Selectlocal.getText().toString());
                    startActivity(i);
                }
                break;
        }
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
}
