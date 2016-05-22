package com.example.raymundcat.safetycj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.raymundcat.safetycj.activities.MapActivity_;
import com.example.raymundcat.safetycj.fragments.MapFragment;
import com.example.raymundcat.safetycj.fragments.ReportFragment;
import com.example.raymundcat.safetycj.http.APIConstants;
import com.example.raymundcat.safetycj.http.PostApiInterface;
import com.example.raymundcat.safetycj.managers.SMSManager;
import com.example.raymundcat.safetycj.managers.SharedPreferenceHelper;
import com.example.raymundcat.safetycj.managers.StringConverterFactory;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements LocationListener {

    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA_REQUEST = 2;

    private String selectedImagePath;
//    private Constants.ReportType selectedReportType;

    @ViewById(R.id.main_toolbar_burger)
    Button burgerButton;

    @ViewById(R.id.home_button_catcall)
    ImageButton buttonCatcall;

    @ViewById(R.id.home_button_stalking)
    ImageButton buttonStalking;

    @ViewById(R.id.home_button_environment)
    ImageButton buttonEnvironment;

    @ViewById(R.id.home_text_incident)
    TextView incidentTitle;

    @ViewById(R.id.home_text_attach)
    TextView attachTitle;

    @ViewById(R.id.report_text)
    EditText reportText;

    protected LocationManager locationManager;
    double latitude,longitude;

    @Click(R.id.home_button_catcall)
    void didPressButtonCatcall() {
        if (buttonCatcall.isSelected()) {
            buttonCatcall.setSelected(false);
        } else {
            incidentTitle.setText("Incident: Catcall");
            buttonCatcall.setSelected(true);
            buttonStalking.setSelected(false);
            buttonEnvironment.setSelected(false);
        }
    }

    @Click(R.id.home_button_stalking)
    void didPreddButtonStalking() {
        if (buttonStalking.isSelected()) {
            buttonStalking.setSelected(false);
        } else {
            incidentTitle.setText("Incident: Stalking");
            buttonCatcall.setSelected(false);
            buttonStalking.setSelected(true);
            buttonEnvironment.setSelected(false);
        }
    }

    @Click(R.id.home_button_environment)
    void didPressButtonEnvironment() {
        if (buttonEnvironment.isSelected()) {
            buttonEnvironment.setSelected(false);
        } else {
            incidentTitle.setText("Incident: Environment Report");
            buttonCatcall.setSelected(false);
            buttonStalking.setSelected(false);
            buttonEnvironment.setSelected(true);
        }
    }

    @Click(R.id.home_button_camera)
    void didPressCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Click(R.id.home_button_gallery)
    void didPressGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                attachTitle.setText("Attachment: Photo from Gallery");
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri selectedImageUri = getImageUri(getApplicationContext(), photo);
            String selectedImagePathFromPhoto = getPath(selectedImageUri);
            attachTitle.setText("Attachment: Photo from Camera");
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void sendReport() {
        Constants.ReportType selectedReportType = getSelectedReportType();

        RequestBody fbody = null;
        if (selectedImagePath != null) {
            File file = new File(selectedImagePath);
            fbody = RequestBody.create(MediaType.parse("image/*"), file);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostApiInterface apiInterface = retrofit.create(PostApiInterface.class);

        String facebookId = SharedPreferenceHelper.getInstance().getString("facebookId");

        // add geolocation
        Call<ResponseBody> createReportCall = apiInterface.createReport(
                selectedReportType.name().toString(),
                facebookId,
                reportText.getText().toString(),
                latitude,
                longitude,
                System.currentTimeMillis(),
                fbody
        );
        createReportCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                SToast.showShortToast("Your Report has been submitted!");
                // TODO: Clear all input
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                SToast.showShortToast("Sorry, There is something wrong in processing you report. PLease try again");
            }
        });
    }

    Constants.ReportType getSelectedReportType() {
        Constants.ReportType selectedReportType;
        if (buttonCatcall.isSelected()) {
            selectedReportType = Constants.ReportType.CATCALL;
        }
        else if (buttonStalking.isSelected()) {
            selectedReportType = Constants.ReportType.STALKING;
        }
        else if (buttonEnvironment.isSelected()) {
            selectedReportType = Constants.ReportType.ENVIRONMENT;
        }
        else {
            selectedReportType = Constants.ReportType.ENVIRONMENT;
        }

        return selectedReportType;
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    @Click(R.id.home_button_wifi)
    void sendViaWifi() {
        sendReport();
    }

    @Click(R.id.home_button_sms)
    void sendViaSMS() {
        SMSManager smsManager = new SMSManager(this, true);
        //add geolocation
        String facebookId = SharedPreferenceHelper.getInstance().getString("facebookId");
        smsManager.createSMS(latitude, longitude, facebookId, getSelectedReportType(), reportText.getText().toString());
        smsManager.sendMessage();
    }

    @AfterViews
    void afterViews() {
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }catch (Exception e){

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("","wot " + location.getLatitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        try{
            locationManager.removeUpdates(this);
        }catch (Exception e){

        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

//    @Click(R.id.testbutton)
//    void testButtonClick() {
//        SMSManager smsManager = new SMSManager(this, true);
//        smsManager.sendMessage();
//    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private String tabTitles[] = new String[] { "Report", "Map" };
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ReportFragment.getInstance();
                case 1:
                    return MapFragment.getInstance();
                default:
                    return MapFragment.getInstance();
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

    }

    @Click(R.id.main_toolbar_burger)
    void didPressMainBurger(){
        MapActivity_.intent(this).start();
        Log.i("","im clicked");
    }

    @Override
    protected void onPause() {

        try{
            locationManager.removeUpdates(this);
        }catch (Exception e){

        }

        super.onPause();
    }
}
