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
    int ico[] = {R.drawable.ic_search_unsel,R.drawable.ic_action_slideshare_logo,R.drawable.ic_action_menu_button_of_three_lines,R.drawable.sd,R.drawable.ic_search_sel,R.drawable.ic_action_dsshare_logo};

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

        tabLayout.addTab(tabLayout.newTab(),true);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        tabLayout.getTabAt(0).setIcon(ico[4]);
        tabLayout.getTabAt(1).setIcon(ico[1]);

        tabLayout.getTabAt(2).setIcon(ico[2]);
    }

    private void bindWidgetsWithAnEvent() {

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()== 2){

                    tabLayout.getTabAt(2).setIcon(ico[3]);
                    //tabLayout.getTabAt(1).setIcon(ico[1]);
                    tabLayout.getTabAt(0).setIcon(ico[0]);

                }else if(tab.getPosition() == 1){
                    tabLayout.getTabAt(1).setIcon(ico[5]);
                   // tabLayout.getTabAt(2).setIcon(ico[2]);
                    tabLayout.getTabAt(0).setIcon(ico[0]);

                }else{
                    tabLayout.getTabAt(0).setIcon(ico[4]);

                }
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                if(tab.getPosition()== 2){

                    tabLayout.getTabAt(2).setIcon(ico[2]);
                }else if(tab.getPosition() == 0){
                    tabLayout.getTabAt(0).setIcon(ico[0]);
                }else{
                    tabLayout.getTabAt(1).setIcon(ico[1]);
                }

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
