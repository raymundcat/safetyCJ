package com.example.raymundcat.safetycj;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.raymundcat.safetycj.activities.MapActivity_;
import com.example.raymundcat.safetycj.fragments.MapFragment;
import com.example.raymundcat.safetycj.fragments.ReportFragment;
import com.example.raymundcat.safetycj.managers.SMSManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA_REQUEST = 2;

    private String selectedImagePath;

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

    @Click(R.id.home_button_catcall)
    void didPressButtonCatcall(){
        if (buttonCatcall.isSelected()){
            buttonCatcall.setSelected(false);
        }else {
            incidentTitle.setText("Incident: Catcall");
            buttonCatcall.setSelected(true);
            buttonStalking.setSelected(false);
            buttonEnvironment.setSelected(false);
        }
    }

    @Click(R.id.home_button_stalking)
    void didPreddButtonStalking(){
        if (buttonStalking.isSelected()){
            buttonStalking.setSelected(false);
        }else {
            incidentTitle.setText("Incident: Stalking");
            buttonCatcall.setSelected(false);
            buttonStalking.setSelected(true);
            buttonEnvironment.setSelected(false);
        }
    }

    @Click(R.id.home_button_environment)
    void didPressButtonEnvironment(){
        if (buttonEnvironment.isSelected()){
            buttonEnvironment.setSelected(false);
        }else {
            incidentTitle.setText("Incident: Environment Report");
            buttonCatcall.setSelected(false);
            buttonStalking.setSelected(false);
            buttonEnvironment.setSelected(true);
        }
    }

    @Click(R.id.home_button_camera)
    void didPressCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Click(R.id.home_button_gallery)
    void didPressGallery(){
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
            attachTitle.setText("Attachment: Photo from Camera");
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    @AfterViews
    void afterViews(){
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


}
