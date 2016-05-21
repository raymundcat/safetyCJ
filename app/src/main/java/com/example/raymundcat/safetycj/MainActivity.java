package com.example.raymundcat.safetycj;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Button;

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
