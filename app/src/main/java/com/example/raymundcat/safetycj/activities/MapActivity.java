package com.example.raymundcat.safetycj.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.Toast;

import com.example.raymundcat.safetycj.R;
import com.example.raymundcat.safetycj.http.APIConstants;
import com.example.raymundcat.safetycj.http.PostApiInterface;
import com.example.raymundcat.safetycj.models.EventLocations;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.SSLException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Raymund on 21/05/2016.
 */
@EActivity(R.layout.activity_map)
public class MapActivity extends Activity{
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;
    List<LatLng> list = new ArrayList<LatLng>();
    ArrayList<EventLocations.EventLocation> origCopyLocations = new ArrayList<EventLocations.EventLocation>();
    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;

    @AfterViews
    void afterViews(){
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        float zoomLevel = (float) 16.8; //This goes up to 21
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(14.700127,121.033723), zoomLevel));
        addHeatMap();
    }

    @Click(R.id.map_toolbar_back)
    void didPressBack(){
        finish();
    }

    private void addHeatMap() {
        String API = APIConstants.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostApiInterface apiInterface = retrofit.create(PostApiInterface.class);

        Call<EventLocations> getLocationsCall = apiInterface.getLocations();
        getLocationsCall.enqueue(new Callback<EventLocations>() {
            @Override
            public void onResponse(Call<EventLocations> call, Response<EventLocations> response) {
                Log.i("", "Response lol" + response.body().locations.size());

                origCopyLocations = response.body().locations;

                for (EventLocations.EventLocation location : origCopyLocations) {
                    LatLng latlng = new LatLng(location.lat, location.lng);
                    list.add(latlng);
                }

                // Create a heat map tile provider, passing it the latlngs of the police stations.
                mProvider = new HeatmapTileProvider.Builder()
                        .data(list)
                        .build();
                mProvider.setRadius(50);

                // Add a tile overlay to the map, using the heat map tile provider.
                mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            }

            @Override
            public void onFailure(Call<EventLocations> call, Throwable t) {

            }
        });
    }

    @Click(R.id.map_button_morning)
    void filterTimeMorning(){
        //remove the tile first
        mOverlay.remove();

        //filter all the timestamps per morning
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
//        calendar.setTimeInMillis(timestamp * 1000);
//        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

        list.removeAll(list);
        for (EventLocations.EventLocation location : origCopyLocations) {

            //add the checker for time here
            calendar.setTimeInMillis((long) (location.timestamp * 1000));
            Log.i("", "Hour: " + calendar.get(Calendar.HOUR_OF_DAY));
            if (calendar.get(Calendar.HOUR) < 9){
                LatLng latlng = new LatLng(location.lat, location.lng);
                list.add(latlng);
            }
        }

        if(list.size() > 0){
            // Create a heat map tile provider, passing it the latlngs of the police stations.
            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();
            mProvider.setRadius(50);

            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    @Click(R.id.map_button_noon)
    void filterTimeNoon(){
        //remove the tile first
        mOverlay.remove();

        //filter all the timestamps per morning
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
//        calendar.setTimeInMillis(timestamp * 1000);
//        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

        list.removeAll(list);
        for (EventLocations.EventLocation location : origCopyLocations) {

            //add the checker for time here
            calendar.setTimeInMillis((long) (location.timestamp * 1000));
            Log.i("", "Hour: " + calendar.get(Calendar.HOUR_OF_DAY));
            if (calendar.get(Calendar.HOUR) >= 9 && calendar.get(Calendar.HOUR) < 17){
                LatLng latlng = new LatLng(location.lat, location.lng);
                list.add(latlng);
            }
        }

        if(list.size() > 0){
            // Create a heat map tile provider, passing it the latlngs of the police stations.
            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();
            mProvider.setRadius(50);

            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    @Click(R.id.map_button_night)
    void filterTimeNight(){
        //remove the tile first
        mOverlay.remove();

        //filter all the timestamps per morning
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
//        calendar.setTimeInMillis(timestamp * 1000);
//        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

        list.removeAll(list);
        for (EventLocations.EventLocation location : origCopyLocations) {

            //add the checker for time here
            calendar.setTimeInMillis((long) (location.timestamp * 1000));
            Log.i("", "Hour: " + calendar.get(Calendar.HOUR_OF_DAY));
            if (calendar.get(Calendar.HOUR) < 6 || calendar.get(Calendar.HOUR) > 16){
                LatLng latlng = new LatLng(location.lat, location.lng);
                list.add(latlng);
            }
        }

        if(list.size() > 0){
            // Create a heat map tile provider, passing it the latlngs of the police stations.
            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();
            mProvider.setRadius(50);

            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }
}
