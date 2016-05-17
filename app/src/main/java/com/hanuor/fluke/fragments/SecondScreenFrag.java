package com.hanuor.fluke.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.andtinder.view.CardContainer;
import com.hanuor.fluke.R;
import com.hanuor.fluke.adapters.ResultAdapter;
import com.hanuor.fluke.apihits.MusicHits;
import com.hanuor.fluke.gettersetters.JSONServerGS;
import com.hanuor.fluke.interfaces.ServerInterface;
import com.hanuor.fluke.serverhandler.ServerTasker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Set;

public class SecondScreenFrag extends Fragment{
    CardContainer mcardContainer;
    int checkoir;
    ArrayList<String> mIdstore;
    ArrayList<String> mfinalStore;
    JSONServerGS  gs = new JSONServerGS();



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


/*
            Log.d("moasa",""+keys.size()+"Bundle "+bundle);
            for(int y=0;y<keys.size();y++){
                Log.d("moasa",""+keys.toString());

            }
*/


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
        final RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
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
                Toast.makeText(getActivity(), "Yipeee", Toast.LENGTH_SHORT).show();
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
                    fetchDetails.add(fbName);
                    String fbPic = Lister[1];
                    good.add(fbPic);
                    String track = Lister[3];
                    String email = Lister[2];
                    String artist = Lister[4];


                    //model.setTitle(fbName);
                    Picasso.with(getActivity()).load(fbPic).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Drawable mD = new BitmapDrawable(getActivity().getResources(),bitmap);

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });


                }

            ResultAdapter rS = new ResultAdapter(getActivity(),good,fetchDetails);
                recList.setAdapter(rS);



            }
        }).execute();




        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
       // onDestroy();
    }


}
