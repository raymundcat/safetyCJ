package com.example.raymundcat.safetycj;

import android.app.Activity;
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
