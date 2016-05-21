package com.example.raymundcat.safetycj.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.Button;
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
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    @ViewById(R.id.map_button_morning)
    Button buttonMorning;

    @ViewById(R.id.map_button_noon)
    Button buttonNoon;

    @ViewById(R.id.map_button_night)
    Button buttonNight;

    @ViewById(R.id.map_button_3months)
    Button button3Month;

    @ViewById(R.id.map_button_1months)
    Button button1Month;

    @ViewById(R.id.map_button_1week)
    Button button1Week;

    @ViewById(R.id.map_button_catcalls)
    Button buttonCatcalls;

    @ViewById(R.id.map_button_stalking)
    Button buttonStalking;

    @ViewById(R.id.map_button_environment)
    Button buttonEnvironment;

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
//                Log.i("", "Response lol" + response.body().locations.size());

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
        if(buttonMorning.isSelected()){
            buttonMorning.setSelected(false);
        }else {
            buttonMorning.setSelected(true);
            buttonNoon.setSelected(false);
            buttonNight.setSelected(false);
        }
        updateMapOverlay();
    }

    @Click(R.id.map_button_noon)
    void filterTimeNoon(){
        if(buttonNoon.isSelected()){
            buttonNoon.setSelected(false);
        }else{
            buttonMorning.setSelected(false);
            buttonNoon.setSelected(true);
            buttonNight.setSelected(false);
        }
        updateMapOverlay();
    }

    @Click(R.id.map_button_night)
    void filterTimeNight(){
        if (buttonNight.isSelected()){
            buttonNight.setSelected(false);
        }else {
            buttonMorning.setSelected(false);
            buttonNoon.setSelected(false);
            buttonNight.setSelected(true);
        }
        updateMapOverlay();
    }

    @Click(R.id.map_button_3months)
    void filter3Month(){
        if (button3Month.isSelected()){
            button3Month.setSelected(false);
        }else {
            button3Month.setSelected(true);
            button1Month.setSelected(false);
            button1Week.setSelected(false);
        }
        updateMapOverlay();
    }

    @Click(R.id.map_button_1months)
    void filter1Month(){
        if(button1Month.isSelected()){
            button1Month.setSelected(false);
        }else{
            button3Month.setSelected(false);
            button1Month.setSelected(true);
            button1Week.setSelected(false);
        }
        updateMapOverlay();
    }

    @Click(R.id.map_button_1week)
    void filter1Week(){
        if(button1Week.isSelected()){
            button1Week.setSelected(false);
        }else{
            button3Month.setSelected(false);
            button1Month.setSelected(false);
            button1Week.setSelected(true);
        }
        updateMapOverlay();
    }

    @Click(R.id.map_button_catcalls)
    void filterCatcalls(){
        if(buttonCatcalls.isSelected()){
            buttonCatcalls.setSelected(false);
        }else{
            buttonCatcalls.setSelected(true);
            buttonStalking.setSelected(false);
            buttonEnvironment.setSelected(false);
        }
        updateMapOverlay();
    }

    @Click(R.id.map_button_stalking)
    void filterStalking(){
        if(buttonStalking.isSelected()){
            buttonStalking.setSelected(false);
        }else{
            buttonCatcalls.setSelected(false);
            buttonStalking.setSelected(true);
            buttonEnvironment.setSelected(false);
        }
        updateMapOverlay();
    }

    @Click(R.id.map_button_environment)
    void filterEnvironment(){
        if(buttonEnvironment.isSelected()){
            buttonEnvironment.setSelected(false);
        }else{
            buttonCatcalls.setSelected(false);
            buttonStalking.setSelected(false);
            buttonEnvironment.setSelected(true);
        }
        updateMapOverlay();
    }

    void updateMapOverlay(){
        //remove the tile first
        mOverlay.remove();

        //filter all the timestamps per morning
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();

        //remove overlays first
        list.removeAll(list);

        ArrayList<EventLocations.EventLocation> dummyCopyLocations = new ArrayList<EventLocations.EventLocation>();

        //filtering per time
        if(buttonMorning.isSelected()){
            for (EventLocations.EventLocation location : origCopyLocations) {

                //add the checker for time here
                calendar.setTimeInMillis((long) (location.timestamp * 1000));
                if (calendar.get(Calendar.HOUR) < 9){
                    dummyCopyLocations.add(location);
                }
            }
        }else if(buttonNoon.isSelected()){
            for (EventLocations.EventLocation location : origCopyLocations) {

                //add the checker for time here
                calendar.setTimeInMillis((long) (location.timestamp * 1000));
                if (calendar.get(Calendar.HOUR) >= 9 && calendar.get(Calendar.HOUR) < 17){
                    dummyCopyLocations.add(location);
                }
            }
        }else if(buttonNight.isSelected()){
            for (EventLocations.EventLocation location : origCopyLocations) {

                //add the checker for time here
                calendar.setTimeInMillis((long) (location.timestamp * 1000));
                if (calendar.get(Calendar.HOUR) < 6 || calendar.get(Calendar.HOUR) > 16){
                    dummyCopyLocations.add(location);
                }
            }
        }else{
            for (EventLocations.EventLocation location : origCopyLocations) {
                dummyCopyLocations.add(location);
            }
        }

        ArrayList<EventLocations.EventLocation> dummyCopyLocations2 = new ArrayList<EventLocations.EventLocation>();

        //beginning time
        Calendar startCalendar = new GregorianCalendar();
        //this is today
        Calendar endCalendar = new GregorianCalendar();

        if (button3Month.isSelected()){
            for (EventLocations.EventLocation location : dummyCopyLocations) {

                //add the checker for time here
                startCalendar.setTimeInMillis((long) (location.timestamp * 1000));
                int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
                int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

                if(diffMonth > 1){
                    dummyCopyLocations2.add(location);
                }
            }
        }else if (button1Month.isSelected()){
            for (EventLocations.EventLocation location : dummyCopyLocations) {

                //add the checker for time here
                startCalendar.setTimeInMillis((long) (location.timestamp * 1000));
                int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
                int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

                if(diffMonth < 1){
                    dummyCopyLocations2.add(location);
                }
            }
        }if (button1Week.isSelected()){
            for (EventLocations.EventLocation location : dummyCopyLocations) {

                //add the checker for time here
                startCalendar.setTimeInMillis((long) (location.timestamp * 1000));
                int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
                int diffMonth = diffYear * 12 + endCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR);

                if(diffMonth < 7){
                    dummyCopyLocations2.add(location);
                }
            }
        }else{
            for (EventLocations.EventLocation location : dummyCopyLocations) {
                dummyCopyLocations2.add(location);
            }
        }

        ArrayList<EventLocations.EventLocation> dummyCopyLocations3 = new ArrayList<EventLocations.EventLocation>();

        //now filter type
        if(buttonCatcalls.isSelected()){
            for (EventLocations.EventLocation location : dummyCopyLocations2) {
                if (location.type.equalsIgnoreCase("CATCALLING")){
                    dummyCopyLocations3.add(location);
                }
            }
        }else if(buttonStalking.isSelected()){
            for (EventLocations.EventLocation location : dummyCopyLocations2) {
                if (location.type.equalsIgnoreCase("STALKING")){
                    dummyCopyLocations3.add(location);
                }
            }
        }else if(buttonEnvironment.isSelected()){
            for (EventLocations.EventLocation location : dummyCopyLocations2) {
                if (location.type.equalsIgnoreCase("ENVIRONMENT")){
                    dummyCopyLocations3.add(location);
                }
            }
        }else {
            for (EventLocations.EventLocation location : dummyCopyLocations2) {
                dummyCopyLocations3.add(location);
            }
        }

        //now convert all dummy locations to long lats
        for (EventLocations.EventLocation location : dummyCopyLocations3) {
            LatLng latlng = new LatLng(location.lat, location.lng);
            list.add(latlng);
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
