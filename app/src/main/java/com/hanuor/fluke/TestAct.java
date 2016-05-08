package com.hanuor.fluke;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.FoldingCube;

/**
 * Created by Shantanu Johri on 07-05-2016.
 */
public class TestAct extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        SpinKitView spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        FoldingCube fc = new FoldingCube();
        CubeGrid cb = new CubeGrid();
        spinKitView.setIndeterminateDrawable(cb);
        }
}
