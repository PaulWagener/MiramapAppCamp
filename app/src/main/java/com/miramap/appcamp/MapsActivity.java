package com.miramap.appcamp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.ujuizi.ramani.api.android.RSMapServices;
import com.ujuizi.ramani.api.android.RamaniListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, RamaniListener.onAPICheckDone {

    private GoogleMap map;
    private LatLng startLocation = new LatLng(52.1850394,4.6355009);
    private RSMapServices mRSMapServices = new RSMapServices();

    private static final String layerID = "public.g22pl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 12.0f));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mRSMapServices.apiKey("2824ecdb27bbc418b073883b7f0d3e1b", "paulwagener", getApplicationContext(), this);
    }

    @Override
    public void APICheckDone(boolean isSuccess) {
        if (isSuccess) {
            TileProvider tp = mRSMapServices.getMap(layerID);
            if (tp != null) {
                map.addTileOverlay(new TileOverlayOptions().fadeIn(true).tileProvider(tp));
            }
        }
    }

    int getTrafficlightColor(double value){
        return android.graphics.Color.HSVToColor(new float[]{(float)value*120f,1f,1f});
    }
}
