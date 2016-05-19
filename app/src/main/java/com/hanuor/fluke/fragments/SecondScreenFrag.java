package com.hanuor.fluke.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.hanuor.fluke.R;
import com.hanuor.fluke.adapters.ResultAdapter;
import com.hanuor.fluke.apihits.MusicHits;
import com.hanuor.fluke.gettersetters.JSONServerGS;
import com.hanuor.fluke.interfaces.ServerInterface;
import com.hanuor.fluke.serverhandler.ServerTasker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

public class SecondScreenFrag extends Fragment{
    int checkoir;
    ArrayList<String> mIdstore;
    ArrayList<String> mfinalStore;
    JSONServerGS  gs = new JSONServerGS();
    Toolbar tb;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MusicHits mh = new MusicHits();
        IntentFilter ifla = mh.searchsong();
        getActivity().registerReceiver(mReceiver, ifla);


    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = null;
            try {
                String action = intent.getAction();
                String cmd = intent.getStringExtra("command");
                Log.d("mIntentReceiver.onR", action + " / " + cmd+" URI "+intent.getLongExtra("id",-1));
                bundle = intent.getExtras();
                Set<String> keys = intent.getExtras().keySet();
                Log.d("taggy",""+bundle);
                Animation fadeIn = new AlphaAnimation(1, 0);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(1000);
                // spinKitView.setAnimation(fadeIn);
                //spinKitView.setVisibility(View.INVISIBLE);
                String artist = intent.getStringExtra("artist");
                String album = intent.getStringExtra("album");
                String track = intent.getStringExtra("track");
                gs.setArtist(artist);
                gs.setTrack(track);
                Long ai = (Long) bundle.get("albumId");
                Log.d("Toaster",""+ai);
                String ais = String.valueOf(ai);

                Log.d("Music",artist+":"+album+":     SADASDASDA  "+track+"ablum id"+" "+bundle.get("albumId"));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_screen, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_header);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        /*
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */final RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ServerTasker s = (ServerTasker) new ServerTasker(getActivity(), new ServerInterface() {
            @Override
            public void fetchalldetails(ArrayList<String> fetchAll) {

                ArrayList<String> fetchDetails  = new ArrayList<String>();
                String Lister[];
                ArrayList<String> good = new ArrayList<String>();
                ArrayList<String> artistIma = new ArrayList<String>();

                ArrayList<String> fbtimey = new ArrayList<String>();
               // Toast.makeText(getActivity(), "Yipeee", Toast.LENGTH_SHORT).show();
                Log.d("STRRR",""+fetchAll.size());
                //Do your task here
                for(int i=0;i<fetchAll.size();i++) {
                    Lister = fetchAll.get(i).split(" ");

                    //  fetchDetails = Arrays.asList(fetchAll.get(i).split(FlukeApp42Database.separator));
                    // Lister = fetchAll.get(i).split(FlukeApp42Database.separator);
                    Log.d("STRINGER",Lister[1]);
                    //String mos[] = Lister[0].split(FlukeApp42Database.separator);
                    // Log.d("SSSS",""+mos.length);
                    String fbName = Lister[0];
                    String rename = fbName.replace("%20"," ");
                    fetchDetails.add(rename);
                    String fbPic = Lister[1];
                    good.add(fbPic);

                    String email = Lister[2];
                    String track = Lister[3];
                    String artist = Lister[4];
                    String artistIm = Lister[5];
                    artistIma.add(artistIm);
                    String albumIm = Lister[6];
                    String timers = Lister[7];
                    Log.d("TIME",""+timers);
                    String listme = timers.replace("."," ");
                    String ala[] = listme.split(" ");
                    String mas = ala[0].replace("T"," ");


                    try
                    {
                        String ourDate = mas
                                ;
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date value = formatter.parse(ourDate);

                        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm"); //this format changeable
                        dateFormatter.setTimeZone(TimeZone.getDefault());
                        ourDate = dateFormatter.format(value);
                        String vall[] = ourDate.split(" ");
                        String omk = vall[1];
                        Log.d("OMK",omk);


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        String kow = new SimpleDateFormat("HH:mm").format(new Date()).toString();

                       // String omk1 = new SimpleDateFormat("HH:mm").format(calendar.getTime()).toString();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        Date date1 = format.parse(kow);
                        Log.d("Origi",""+kow);
                        Date date2 = format.parse(omk);
                        long difference = date2.getTime() - date1.getTime();
                        Log.d("OurDate", ""+difference);
                        if(difference<0){
                            difference = 0-difference;
                        }
                        if(difference >= 0 && difference < 10000){
                            fbtimey.add("Listening to this now");
                        }else if(difference >10000 && difference < 100000){
                            fbtimey.add("Just a few seconds ago");
                        }else if(difference > 100000 && difference <1000000){
                            fbtimey.add("Minutes ago");
                        }else if (difference >10000000){
                            fbtimey.add("Long time ago");
                        }else{
                            fbtimey.add("NNN");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                Log.d("MMMMMMM",""+fbtimey.size());

             ResultAdapter rS = new ResultAdapter(getActivity(),good,fetchDetails,artistIma,fbtimey);
                recList.setAdapter(rS);

               // 2016-05-19T16:39:54.322Z

            }
        }).execute();




        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStop() {
        super.onStop();
   // getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
        // onDestroy();
    }


}
