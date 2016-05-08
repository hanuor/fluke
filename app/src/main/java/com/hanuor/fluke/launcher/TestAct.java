package com.hanuor.fluke.launcher;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.hanuor.fluke.R;
import com.hanuor.fluke.adapters.ToolbarViewPager;

/**
 * Created by Shantanu Johri on 07-05-2016.
 */
public class TestAct extends AppCompatActivity {
    Toolbar toolbar;

    TextView iv;
    int ico[] = {R.drawable.ic_stat_music_search};


    SpinKitView spinKitView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);


        toolbar = (Toolbar) findViewById(R.id.toolbar_header); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
       // spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        //FoldingCube fc = new FoldingCube();
        //CubeGrid cb = new CubeGrid();
        iv = (TextView) findViewById(R.id.tv);
        //spinKitView.setIndeterminateDrawable(cb);
        viewPager.setAdapter(new ToolbarViewPager(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(ico[0]);
        tabLayout.getTabAt(1).setIcon(ico[0]);
        tabLayout.getTabAt(2).setIcon(ico[0]);
        tabLayout.getTabAt(3).setIcon(ico[0]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search){

        }
        return super.onOptionsItemSelected(item);

    }
}
