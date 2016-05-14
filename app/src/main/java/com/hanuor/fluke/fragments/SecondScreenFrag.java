package com.hanuor.fluke.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andtinder.view.CardContainer;
import com.hanuor.fluke.R;

public class SecondScreenFrag extends Fragment {
    CardContainer mcardContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_screen, container, false);
        mcardContainer = (CardContainer) view.findViewById(R.id.layoutview);



        return view;
    }
}
