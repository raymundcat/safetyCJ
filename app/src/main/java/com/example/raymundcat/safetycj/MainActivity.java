package com.example.raymundcat.safetycj;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.raymundcat.safetycj.fragments.MapFragment;
import com.example.raymundcat.safetycj.fragments.ReportFragment;
import com.example.raymundcat.safetycj.http.APIConstants;
import com.example.raymundcat.safetycj.http.PostApiInterface;
import com.example.raymundcat.safetycj.models.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    ScreenSlidePagerAdapter mScreenSlidePagerAdapter;
    ViewPager mViewPager;

    @AfterViews
    void afterViews(){
//        mScreenSlidePagerAdapter =
//                new ScreenSlidePagerAdapter(
//                        getSupportFragmentManager());
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(mScreenSlidePagerAdapter);

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(viewPager);
    }

    @Click(R.id.testbutton)
    void testButtonClick() {
        String API = APIConstants.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostApiInterface getInt = retrofit.create(PostApiInterface.class);
        User user1 = new User();
        user1.facebookId = "facebook:test";
        user1.birthday = "01/01/1992";
        user1.name = "CJ TEST1";

        Call<User> call = getInt.createUser(user1);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("RETROFIT S:", response.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("RETROFIT F:", t.getMessage());
            }
        });
    }

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
}
