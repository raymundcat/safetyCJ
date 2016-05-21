package com.example.raymundcat.safetycj.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.Toast;

import com.example.raymundcat.safetycj.InputStreamCallback;
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
import java.util.List;

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

    @AfterViews
    void afterViews(){
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        float zoomLevel = (float) 17.0; //This goes up to 21
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
//        Call<ResponseBody> getLocationsCall = apiInterface.getLocations();
//        getLocationsCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    Log.i("","Response lol" + response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });

        Call<EventLocations> getLocationsCall = apiInterface.getLocations();
        getLocationsCall.enqueue(new Callback<EventLocations>() {
            @Override
            public void onResponse(Call<EventLocations> call, Response<EventLocations> response) {
                Log.i("","Response lol" + response.body().locations.size());

                List<LatLng> list = new ArrayList<LatLng>();

                for (EventLocations.EventLocation location: response.body().locations){
                    LatLng latlng = new LatLng(location.lat,location.lng);
                    list.add(latlng);
//                    Log.i("","new latlng " + latlng.latitude + " " + latlng.longitude);
                }

                // Get the data: latitude/longitude positions of police stations.


                // Create a heat map tile provider, passing it the latlngs of the police stations.
                HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                        .data(list)
                        .build();
                mProvider.setRadius(50);
                // Add a tile overlay to the map, using the heat map tile provider.
                TileOverlay mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            }

            @Override
            public void onFailure(Call<EventLocations> call, Throwable t) {

            }
        });
    }
}
