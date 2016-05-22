package com.hanuor.fluke.launcher;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hanuor.fluke.R;
import com.hanuor.fluke.fragments.FirstScreenFrag;
import com.hanuor.fluke.fragments.SecondScreenFrag;
import com.hanuor.fluke.fragments.ThirdScreenFrag;

/**
 * Created by Shantanu Johri on 07-05-2016.
 */
public class FragHandler extends AppCompatActivity {
    Toolbar toolbar;
    Fragment fragOne;
    Fragment fragTwo;
    Fragment fragThree;
    TabLayout tabLayout;
    int ico[] = {R.drawable.ic_stat_music_search,R.drawable.ic_action_slideshare_logo};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);


        toolbar = (Toolbar) findViewById(R.id.toolbar_header); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        bindWidgetsWithAnEvent();
        setupTabLayout();
    }

    private void setupTabLayout() {

        fragOne = new FirstScreenFrag();
        fragTwo = new SecondScreenFrag();
        fragThree = new ThirdScreenFrag();

        tabLayout.addTab(tabLayout.newTab().setText("One"),true);
        tabLayout.addTab(tabLayout.newTab().setText("Two"));
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));

        tabLayout.getTabAt(0).setIcon(ico[0]);
    }

    private void bindWidgetsWithAnEvent() {

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int position) {


        switch (position)
        {
            case 0 :
                replaceFragment(fragOne);
                break;
            case 1 :
                replaceFragment(fragTwo);
                break;
            case 2 :
                replaceFragment(fragThree);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }


}
