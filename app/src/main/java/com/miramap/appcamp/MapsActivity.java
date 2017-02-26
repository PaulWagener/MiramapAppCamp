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
import com.google.android.gms.maps.model.CameraPosition;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RamaniListener.onAPICheckDone,
        RamaniListener.onSearchDone, RamaniListener.onGetFeatureInfoDone, RamaniListener.onGetTransectDone,
        RamaniListener.onGetVerticleProfileDone, RamaniListener.onGetAnimation, RamaniListener.onGetAreaDone,
        RamaniListener.onGetTimeSeriesProfile ,RamaniListener.onGetMetadataDone,
        RamaniListener.onStorePointDone, RamaniListener.onStorePolygonDone,RamaniListener.onStoreLineDone, SeekBar.OnSeekBarChangeListener {

    private GoogleMap mMap;

    private SupportMapFragment mapFragment;
    private SeekBar mSeekBar;

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

        Button btnGetArea = (Button) findViewById(R.id.getArea_btn);
        Button btnTransect = (Button) findViewById(R.id.getTransect_btn);
        Button btnSearch = (Button) findViewById(R.id.search_btn);
        Button btnStorePoint = (Button) findViewById(R.id.store_point_btn);
        Button btnStorePoligon = (Button) findViewById(R.id.store_polygon_btn);

        Button btnStoreLine = (Button) findViewById(R.id.store_line_btn);
        Button btnGetAnimation = (Button) findViewById(R.id.btn_get_animation);
        Button btnGetLayerAttributes = (Button) findViewById(R.id.btn_get_layer_attributes);
        Button btnGetDates = (Button) findViewById(R.id.btn_get_dates_for_time_series);
        Button btnSetLayerAttributes = (Button) findViewById(R.id.btn_set_layer_attributes);


        btnSetLayerAttributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map params = new HashMap();
                params.put("ELEVATION", "1");
                params.put("TIME", "2006-07-28T00:00:00.000Z/2006-08-01T00:00:00.000Z");
                mRSMapServices.setParams(params);
                LatLng point = new LatLng(52.4024, 6.0370);
                mRSMapServices.getTimeseriesProfile(point, "LAYER_ID", MapsActivity.this);
            }
        });

        btnGetDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map params = new HashMap();
                params.put("item", "dates");
                params.put("format", "rss");
                mRSMapServices.setParams(params);
                mRSMapServices.getLayerAttributes(layerID,MapsActivity.this);
            }
        });

        btnGetLayerAttributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRSMapServices.getLayerAttributes(layerID, MapsActivity.this);
            }
        });


        btnGetAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] timeStep = {
                        "2010-09-08T00:00:00.000Z",
                        "2010-09-15T00:00:00.000Z",
                        "2010-09-27T00:00:00.000Z",
                        "2010-10-04T00:00:00.000Z",
                        "2010-10-14T00:00:00.000Z",
                        "2010-10-26T00:00:00.000Z",
                        "2010-11-05T00:00:00.000Z",
                        "2010-11-12T00:00:00.000Z",
                        "2010-11-19T00:00:00.000Z",
                        "2010-11-22T00:00:00.000Z",
                        "2010-11-24T00:00:00.000Z",
                        "2010-11-29T00:00:00.000Z"
                };

                mRSMapServices.getAnimation(layerID,timeStep, MapsActivity.this);



            }
        });

        btnStoreLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map fields = new HashMap();
                fields.put("id", "50");
                ArrayList<LatLng> markers = new ArrayList<>();
                markers.add(new LatLng(52.4024, 6.0370));
                markers.add(new LatLng(52.5960, 6.2952));
                markers.add(new LatLng(52.5028, 6.7044));
                markers.add(new LatLng(52.3269, 6.9077));
                markers.add(new LatLng(52.1840, 6.6742));
                markers.add(new LatLng(52.2160, 6.2787));
                mRSMapServices.storeLine("LAYER_ID_DATABASE",fields,markers, MapsActivity.this);
            }
        });

        btnStorePoligon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map fields = new HashMap();
                fields.put("id", "50");

                ArrayList<LatLng> markers = new ArrayList<>();
                markers.add(new LatLng(52.4024, 6.0370));
                markers.add(new LatLng(52.5960, 6.2952));
                markers.add(new LatLng(52.5028, 6.7044));
                markers.add(new LatLng(52.3269, 6.9077));
                markers.add(new LatLng(52.1840, 6.6742));
                markers.add(new LatLng(52.2160, 6.2787));
                mRSMapServices.storePolygon("LAYER_ID_DATABASE", fields, markers, MapsActivity.this);
            }
        });

        btnStorePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap fields = new HashMap();
                fields.put("id", "50");
                fields.put("id", "51");
                LatLng point = new LatLng(52.4024, 6.0370);
                mRSMapServices.storePoint("LAYER_ID_DATABASE", fields, point, MapsActivity.this);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject keywords = new JSONObject();
                    keywords.put("field_name", "value");

                    mRSMapServices.search(mMap,"LAYER_ID_DATABASE", keywords, true, MapsActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        btnGetArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<LatLng> rectOptions = new ArrayList<>();
                rectOptions.add(new LatLng(52.4024, 6.0370));
                rectOptions.add(new LatLng(52.5960, 6.2952));
                rectOptions.add(new LatLng(52.5028, 6.7044));
                rectOptions.add(new LatLng(52.3269, 6.9077));
                rectOptions.add(new LatLng(52.1840, 6.6742));
                rectOptions.add(new LatLng(52.2160, 6.2787));

                mRSMapServices.getArea(rectOptions, layerID, MapsActivity.this);
                mRSMapServices.getAreaJson(rectOptions,layerID,"all",MapsActivity.this);
            }
        });

        btnTransect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<LatLng> bbox = new ArrayList<>();
                bbox.add(new LatLng(52.3756, 6.4819));
                bbox.add(new LatLng(52.315266, 6.301601));

                mRSMapServices.getTransect(bbox, layerID, MapsActivity.this);
                mRSMapServices.getTransectJson(bbox, layerID, MapsActivity.this);


            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);


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


    private void setOnClickListener(GoogleMap mMap, final String layerID) {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(mContext, "get Feature Info" + layerID, Toast.LENGTH_SHORT).show();
                mRSMapServices.getFeatureInfo(latLng, layerID, MapsActivity.this);
                mRSMapServices.getFeatureInfoJson(latLng, layerID, MapsActivity.this);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(mContext, "get Verticle Profile", Toast.LENGTH_SHORT).show();

                mRSMapServices.getVerticleProfile(latLng, layerID, MapsActivity.this);
                mRSMapServices.getVerticleProfileJson(latLng, layerID, MapsActivity.this);

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(mContext, "get Time Series Profile", Toast.LENGTH_SHORT).show();
                LatLng latLng = marker.getPosition();
                mRSMapServices.getTimeseriesProfile(latLng, layerID, MapsActivity.this);
                mRSMapServices.getTimeseriesProfileJson(latLng, layerID, MapsActivity.this);

                return true;
            }
        });
    }

    @Override
    public void getSearch(JSONObject result) {

    }

    @Override
    public void featureInfo(String data) {

    }

    @Override
    public void featureInfoJson(JSONObject dataJson) {

    }

    @Override
    public void getTransectBitmap(Bitmap result) {

    }

    @Override
    public void getTransectJson(JSONObject result) {

    }

    @Override
    public void getVerticleProfile(Bitmap result) {

    }

    @Override
    public void getVerticleProfileJSONObject(JSONObject result) {

    }

    @Override
    public void getAnimationList(ArrayList<Bitmap> bitmaps) {

    }

    @Override
    public void getArea(Bitmap result) {

    }

    @Override
    public void getAreaJSONObject(JSONObject result) {

    }

    @Override
    public void getTimeSeriesProfile(Bitmap result) {

    }

    @Override
    public void getTimeSeriesProfileJSONObject(JSONObject result) {

    }

    @Override
    public void storePoint(JSONObject result) {

    }

    @Override
    public void storePolygon(JSONObject result) {

    }

    @Override
    public void storeLine(JSONObject result) {

    }

    @Override
    public void getMetadata(JSONObject metadata) {
        if (metadata!=null){
            try{
                String numColorBands = metadata.getString("numColorBands");
                String time = metadata.getString("nearestTimeIso");
                String  defaultPalette = metadata.getString("defaultPalette");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int mapWidth = mapFragment.getView().getWidth();

        mSeekBar.setMax(mapWidth);
        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                (mapWidth-progress), ViewGroup.LayoutParams.MATCH_PARENT);
        rel_btn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
