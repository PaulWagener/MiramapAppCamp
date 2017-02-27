package com.miramap.appcamp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.ujuizi.ramani.api.android.RSMapServices;
import com.ujuizi.ramani.api.android.RamaniListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, RamaniListener.onAPICheckDone, SeekBar.OnSeekBarChangeListener {

    private GoogleMap map;
    private LatLng startLocation = new LatLng(52.1850394,4.6355009);
    private RSMapServices mRSMapServices = new RSMapServices();

    private static final String layerID = "public.g27qb";
    private static final String layerID2 = "public.gftea";

    // g27qb // 8 september 2016
    // gftea // 15 februari 2017

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        SeekBar seekbar = (SeekBar)findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 12.0f));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setRotateGesturesEnabled(false);

        //map.addMarker(new MarkerOptions().)

        mRSMapServices.apiKey("2824ecdb27bbc418b073883b7f0d3e1b", "paulwagener", getApplicationContext(), this);
    }

    private TileOverlay septemberOverlay;

    @Override
    public void APICheckDone(boolean isSuccess) {
        if (isSuccess) {
            TileProvider tp = mRSMapServices.getMap(layerID);
            if (tp != null) {
                map.addTileOverlay(new TileOverlayOptions().transparency(0).fadeIn(true).tileProvider(tp));
            }

            TileProvider tp2 = mRSMapServices.getMap(layerID2);
            if (tp2 != null) {
                septemberOverlay = map.addTileOverlay(new TileOverlayOptions().fadeIn(true).tileProvider(tp2));
            }
        }
    }
    public static double map(double value, double domain_a, double domain_b,
                             double range_a, double range_b) {
        return (value - domain_a) * (range_b - range_a) / (domain_b - domain_a)
                + range_a;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Date date1 = new Date(2016, 9, 8, 0, 0, 0);
        Date date2 = new Date(2017, 2, 15, 0, 0, 0);

        long september = 1473292800;
        long february = 1487116800;

        double inbetween = map(progress, 0, 100, september, february);
        String text = SimpleDateFormat.getDateInstance(DateFormat.LONG).format(new Date((long)inbetween*1000L));

        TextView textview = (TextView)findViewById(R.id.datetext);
        if(textview != null) {
            textview.setText(text);
        }

        //Date date2 =
        if(septemberOverlay != null) {
            septemberOverlay.setTransparency((100-progress) / 100.0f);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
