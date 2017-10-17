package com.example.kyo.gasstation;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kyo.gasstation.naverapi.NMapCalloutCustomOldOverlay;
import com.example.kyo.gasstation.naverapi.NMapCalloutCustomOverlayView;
import com.example.kyo.gasstation.naverapi.NMapViewerResourceProvider;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;


public class map extends NMapActivity
        implements OnMapStateChangeListener, OnCalloutOverlayListener{
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "2znl8l7Mc4jZ8ifHUIwC";
    NMapController mMapController ;
    LinearLayout MapContainer;
    private NMapMyLocationOverlay mMyLocationOverlay;
    private NMapLocationManager mMapLocationManager;
    private NMapCompassManager mMapCompassManager;
    private NMapOverlayManager mOverlayManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapLocationManager = new NMapLocationManager(this);
        MapContainer = (LinearLayout) findViewById(R.id.MapContainer);
        mMapView = new NMapView(this);
        mMapController = mMapView.getMapController();
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        MapContainer.addView(mMapView);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.setScalingFactor(3.0f);
        mMapView.setBuiltInZoomControls(true, null);
        //mMapLocationManager.enableMyLocation(true);
        mMapView.setOnMapStateChangeListener(this);
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        // register callout overlay listener to customize it.
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
        // register callout overlay view listener to customize it.
        mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);
        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
// compass manager
        mMapCompassManager = new NMapCompassManager(this);
// create my location overlay
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
    }

    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        if (nMapError == null) { // success
            startMyLocation();
        } else { // fail
            android.util.Log.e("NMAP", "onMapInitHandler: error="
                    + nMapError.toString());
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    @Override
    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay nMapOverlay, NMapOverlayItem nMapOverlayItem, Rect rect) {
        return null;
    }
    private void startMyLocation() {

        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager
                .setOnLocationChangeListener(onMyLocationChangeListener);

        boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
        if (!isMyLocationEnabled) {
            Toast.makeText(
                    map.this,
                    "Please enable a My Location source in system settings",
                    Toast.LENGTH_LONG).show();
            Intent goToSettings = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(goToSettings);
            finish();
        }else{

        }
    }



    private void stopMyLocation() {
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();
            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);
                mMapCompassManager.disableCompass();
                mMapView.setAutoRotateEnabled(false, false);
                MapContainer.requestLayout();
            }
        }
    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {
        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
            if (errInfo != null) {
                Log.e("myLog", "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());
                Toast.makeText(map.this, errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            }else{
                Toast.makeText(map.this, placeMark.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener =
            new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
            if (mMapController != null) {
                mMapController.animateTo(myLocation);
            }
            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
            Toast.makeText(map.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
            Toast.makeText(map.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
            stopMyLocation();
        }
    };
    private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

        @Override
        public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
                                                         Rect itemBounds) {

            // handle overlapped items
            if (itemOverlay instanceof NMapPOIdataOverlay) {
                NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay)itemOverlay;

                // check if it is selected by touch event
                if (!poiDataOverlay.isFocusedBySelectItem()) {
                    int countOfOverlappedItems = 1;

                    NMapPOIdata poiData = poiDataOverlay.getPOIdata();
                    for (int i = 0; i < poiData.count(); i++) {
                        NMapPOIitem poiItem = poiData.getPOIitem(i);

                        // skip selected item
                        if (poiItem == overlayItem) {
                            continue;
                        }

                        // check if overlapped or not
                        if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
                            countOfOverlappedItems++;
                        }
                    }

                    if (countOfOverlappedItems > 1) {
                        String text = countOfOverlappedItems + " overlapped items for " + overlayItem.getTitle();
                        Toast.makeText(map.this, text, Toast.LENGTH_LONG).show();
                        return null;
                    }
                }
            }

            // use custom old callout overlay
            if (overlayItem instanceof NMapPOIitem) {
                NMapPOIitem poiItem = (NMapPOIitem)overlayItem;

                if (poiItem.showRightButton()) {
                    return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
                            mMapViewerResourceProvider);
                }
            }

            // use custom callout overlay
            return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

            // set basic callout overlay
            //return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);
        }
    };

    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {

        @Override
        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {

            if (overlayItem != null) {
                // [TEST] 말풍선 오버레이를 뷰로 설정함
                String title = overlayItem.getTitle();
                if (title != null && title.length() > 5) {
                    return new NMapCalloutCustomOverlayView(map.this, itemOverlay, overlayItem, itemBounds);
                }
            }

            // null을 반환하면 말풍선 오버레이를 표시하지 않음
            return null;
        }

    };

}
