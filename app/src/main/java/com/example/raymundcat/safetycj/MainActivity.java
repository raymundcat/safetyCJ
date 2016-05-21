package com.example.raymundcat.safetycj;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.raymundcat.safetycj.activities.MapActivity_;
import com.example.raymundcat.safetycj.fragments.MapFragment;
import com.example.raymundcat.safetycj.fragments.ReportFragment;

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

    @Click(R.id.main_toolbar_burger)
    void didPressMainBurger(){
        MapActivity_.intent(this).start();
        Log.i("","im clicked");
    }


}
