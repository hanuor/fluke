package com.hanuor.fluke.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hanuor.fluke.R;
import com.hanuor.fluke.adapters.ResultAdapter;
import com.hanuor.fluke.apihits.MusicHits;
import com.hanuor.fluke.gettersetters.JSONServerGS;
import com.hanuor.fluke.interfaces.ServerInterface;
import com.hanuor.fluke.launcher.FragHandler;
import com.hanuor.fluke.serverhandler.ServerTasker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

public class SecondScreenFrag extends AppCompatActivity{

    JSONServerGS  gs = new JSONServerGS();
    String texts[] = {"A list of people listening to the same music goes here","Can't find anyone here? Did you hit the match button?","Try another song if you can't find anyone here"};
    TextSwitcher mtextswitch;
    RelativeLayout loading;
    RecyclerView cardList;
    Toolbar toolbar;
    TextView notfound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Listening to this");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        MusicHits mh = new MusicHits();
        IntentFilter ifla = mh.searchsong();
        registerReceiver(mReceiver, ifla);

        //
        notfound = (TextView) findViewById(R.id.notfound);
        notfound.setTextColor(Color.parseColor("#cce8ebd8"));
        notfound.setText("Seems like no one is listening to this song right now. Try another one or check after some time.");
        notfound.setTextSize(17);
        final RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);

        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(SecondScreenFrag.this);
        mtextswitch = (TextSwitcher) findViewById(R.id.textswitcher);
        loading = (RelativeLayout) findViewById(R.id.loading);
        cardList = (RecyclerView) findViewById(R.id.cardList);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mtextswitch.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(SecondScreenFrag.this);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.parseColor("#e8ebd8"));
                return textView;
            }
        });
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
        fadeOut.setDuration(3000);
        mtextswitch.setInAnimation(fadeIn);
        mtextswitch.setOutAnimation(fadeOut);

        final Handler h = new Handler();
        final int delay = 3000; //milliseconds

        h.postDelayed(new Runnable(){
            public void run(){
                //do something
                Random r = new Random();
                int i1 = r.nextInt(3 - 0) + 0;
                mtextswitch.setText(texts[i1]);
                h.postDelayed(this, delay);
            }
        }, delay);


        ServerTasker s = (ServerTasker) new ServerTasker(SecondScreenFrag.this, new ServerInterface() {
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
                if(fetchAll.size()==0){
                    notfound.setVisibility(View.VISIBLE);
                    recList.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                }else
                {
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
                            }else if(difference > 100000 && difference <10000000){
                                fbtimey.add("Minutes ago");
                            }else if (difference >10000000){
                                fbtimey.add("Long time ago");
                            }else{
                                Log.d("NNN",""+difference);
                                fbtimey.add("NNN");
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    Log.d("MMMMMMM",""+fbtimey.size());

                    ResultAdapter rS = new ResultAdapter(SecondScreenFrag.this,good,fetchDetails,artistIma,fbtimey);
                    recList.setAdapter(rS);
                    recList.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }


                // 2016-05-19T16:39:54.322Z

            }
        }).execute();





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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            // close this activity and return to preview activity (if there is any)
            Intent sar = new Intent(SecondScreenFrag.this, FragHandler.class);
            sar.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(sar);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
//        unregisterReceiver(mReceiver);
        // onDestroy();
    }


}
