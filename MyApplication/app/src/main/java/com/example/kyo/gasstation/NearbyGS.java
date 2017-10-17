package com.example.kyo.gasstation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.kyo.gasstation.R;
import com.example.kyo.gasstation.drawer;

public class NearbyGS extends drawer implements View.OnClickListener {
    public Button SLGas, SLO, SLKm;
    ListView GasIFLV;
    Intent i;
    CharSequence gasstation[] = new CharSequence[] {"주유소"};
    CharSequence gasinfo[] = new CharSequence[] {"휘발유", "경유"};
    CharSequence KM[] = new CharSequence[] {"1Km","3km", "5km", "10km" };
    CharSequence O[] = new CharSequence[] {"최저가순" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_gs);
        init();
        SLGas.setOnClickListener(this);
        SLO.setOnClickListener(this);
        SLKm.setOnClickListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gasstation) ;
        GasIFLV.setAdapter(adapter) ;
        GasIFLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i = new Intent(NearbyGS.this, map.class);
                startActivity(i);
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
                                break;
                            case 1:
                                SLGas.setText(gasinfo[1]);
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
                                break;
                            case 1:
                                SLKm.setText(KM[1]);
                                break;
                            case 2:
                                SLKm.setText(KM[2]);
                                break;
                            case 3:
                                SLKm.setText(KM[3]);
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
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }
    public void init(){
        GasIFLV = (ListView)findViewById(R.id.GasIFLV);
        SLGas =(Button)findViewById(R.id.SelectGas);
        SLO=(Button)findViewById(R.id.SelectO);
        SLKm=(Button)findViewById(R.id.SelectKM);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.NearbyGSLa);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
}
