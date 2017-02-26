package com.miramap.appcamp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.ujuizi.ramani.api.android.RSMapServices;
import com.ujuizi.ramani.api.android.RamaniListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RamaniListener.onAPICheckDone {

    private GoogleMap mMap;
    private LatLng amsterdam = new LatLng(52.312716, 4.769712);
    private RSMapServices mRSMapServices = new RSMapServices();

    private static final String layerID = "public.xcold";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(amsterdam).title("Marker in Holland"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(amsterdam, 12.0f));

        mRSMapServices.apiKey("2824ecdb27bbc418b073883b7f0d3e1b", "paulwagener", getApplicationContext(), this);
    }

    @Override
    public void APICheckDone(boolean isSuccess) {
        if (isSuccess) {
            TileProvider tp = mRSMapServices.getMap(layerID);
            if (tp != null) {
                mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tp));
            }
        }
    }
}
