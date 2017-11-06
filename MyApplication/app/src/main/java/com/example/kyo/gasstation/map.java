package com.example.kyo.gasstation;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;


public class map extends location {
    MapPoint map;
    MapView mapView;
    Location userLocation;
    Intent i;
    double a,b;
    int pos;
    private LocationManager locationManager;
    ArrayList<HashMap<String, String>> hashlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.activity_map);
        init();
        mapView = new MapView(this);
        if(pos == -1){
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(userLocation.getLatitude(), userLocation.getLongitude()), 2, true);
        }else {
            HashMap<String, String> hash = new HashMap<>();
            hash = hashlist.get(pos);
            a = Double.parseDouble(hash.get("x"));
            b = Double.parseDouble(hash.get("y"));
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(b, a), 2, true);
        }
        mapView.setDaumMapApiKey("706ec6d42baa9e78485586178cd9e2e2");
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mark();
    }
    public void init(){
        settingGPS();
        userLocation = getMyLocation();
        i = getIntent();
        hashlist = new ArrayList<>();
        hashlist = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("hashlist");
        pos = (Integer)i.getSerializableExtra("center");

    }
    public void mark(){
        for(int i = 0; i < hashlist.size();i++) {
            HashMap<String, String> hash = new HashMap<>();
            hash = hashlist.get(i);
            double x = Double.parseDouble(hash.get("x"));
            double y = Double.parseDouble(hash.get("y"));
            map = MapPoint.mapPointWithGeoCoord(y,x);
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(hash.get("name"));
            marker.setTag(0);
            marker.setMapPoint(map);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);// 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.\
            mapView.addPOIItem(marker);
            if(y == b && x == a) {
                mapView.selectPOIItem(marker, true);
            }
        }
    }
}