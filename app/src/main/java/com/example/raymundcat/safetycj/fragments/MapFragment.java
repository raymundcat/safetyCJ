package com.example.raymundcat.safetycj.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.raymundcat.safetycj.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Raymund on 21/05/2016.
 */
@EFragment(R.layout.fragment_map)
public class MapFragment extends Fragment {

    public static MapFragment getInstance(){
        MapFragment fragment = new MapFragment_();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("curChoice", mCurCheckPosition);
    }

}
