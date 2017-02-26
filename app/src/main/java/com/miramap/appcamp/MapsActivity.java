package com.miramap.appcamp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.ujuizi.ramani.api.android.RSMapServices;
import com.ujuizi.ramani.api.android.RamaniListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.miramap.appcamp.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RamaniListener.onAPICheckDone {

    private GoogleMap mMap;

    private SupportMapFragment mapFragment;
    private TileProvider tp;

    LatLng amsterdam = new LatLng(52.312716, 4.769712);

    //    private static final String layerID = "ddl.simS1seriesTwenteNetherlands.smc";
    private static final String layerID = "public.xcold";
    private RSMapServices mRSMapServices = new RSMapServices();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mContext = this;

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Amsterdam, Netherlands.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(amsterdam).title("Marker in Holland"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(amsterdam, 12.0f));

        // Request for satellite data
        mRSMapServices.apiKey("2824ecdb27bbc418b073883b7f0d3e1b", "paulwagener", getApplicationContext(), this);

    }


    @Override
    public void APICheckDone(boolean isSuccess) {
        //when request success
        if (isSuccess) {
            tp = mRSMapServices.getMap(layerID);
            if (tp != null) {
                mTileOverlay1 = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tp));
            }
        }
    }

    private TileOverlay mTileOverlay1;
}
