package com.example.raymundcat.safetycj.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.example.raymundcat.safetycj.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Raymund on 21/05/2016.
 */
@EFragment(R.layout.fragment_map)
public class MapFragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;

    private GoogleMap mMap;

    @AfterViews
    void afterViews(){

        FragmentManager fm = getFragmentManager();
        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment!= null) {
            mapFragment.getMapAsync(this);
        }
    }

    public static MapFragment getInstance(){
        MapFragment fragment = new MapFragment_();
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

//    private void addHeatMap() {
//        List<LatLng> list = null;
//
//        // Get the data: latitude/longitude positions of police stations.
//        try {
////            list = readItems(R.raw.police_stations);
//        } catch (JSONException e) {
//            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
//        }
//
//        // Create a heat map tile provider, passing it the latlngs of the police stations.
//        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
//                .data(list)
//                .build();
//        // Add a tile overlay to the map, using the heat map tile provider.
//        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
//    }
}
