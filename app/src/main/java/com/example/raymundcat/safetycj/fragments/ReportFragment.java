package com.example.raymundcat.safetycj.fragments;

import android.support.v4.app.Fragment;

import com.example.raymundcat.safetycj.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Raymund on 21/05/2016.
 */
@EFragment(R.layout.fragment_report)
public class ReportFragment extends Fragment{

    public static ReportFragment getInstance(){
        ReportFragment fragment = new ReportFragment_();
        return fragment;
    }
}
