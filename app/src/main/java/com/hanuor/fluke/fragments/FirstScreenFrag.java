package com.hanuor.fluke.fragments;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hanuor.fluke.R;
import com.hanuor.fluke.apihits.ApiName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Random;
import java.util.Set;

/**
 * Created by Shantanu Johri on 08-05-2016.
 */
public class FirstScreenFrag extends Fragment {
    private TextSwitcher mtextswitch;
    String texts[] = {"Fluke searches and displays the current playing song automatically","Try changing the track if searching is taking a long time"};

    int mpos = 0;
    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDSTOP = "stop";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    private RequestQueue mQueue;
    ImageView ivs;


    private void searchSong(){
        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");
        iF.addAction("com.android.music.metachanged");

        iF.addAction("com.htc.music.metachanged");

        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");

        getActivity().registerReceiver(mReceiver, iF);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");
            Log.d("mIntentReceiver.onR", action + " / " + cmd+" URI "+intent.getLongExtra("id",-1));
            Bundle bundle = intent.getExtras();
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
            //String ai = bundle.get("albumid");
            //Long mela = Long.getLong(ai);
            if(album!=null) {
                hotswapping(artist);
            }

            Log.d("Music",artist+":"+album+":"+track+"ablum id"+" "+bundle.get("albumId"));
           // iv.setText("Music"+artist+":"+album+":"+track);

        }
    };

    private void hotswapping(String album) {
        if(album!=null) {
            Log.d("resssss",""+album);

            StringBuilder m = new StringBuilder();
            m.append(ApiName.MUSICGRAPH_ARTIST);
            m.append(album);
            String adler = m.toString();
            String newadler = adler.replaceAll(" ", "%20");
            Log.d("ERRORORORROR",""+newadler);
           // URL sourceUrl = new URL(temp);
            Log.d("ERROROROROROR",""+adler);
            String append = ApiName.MUSICGRAPH_ARTIST + "" + album;
            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, newadler, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONObject resu;
                                if(response.has("data")){
                                    Log.d("TRUE","tre");
                                    JSONArray arr = response.getJSONArray("data");
                                    for(int i = 0; i<arr.length();i++){
                                        JSONObject  mov = arr.getJSONObject(i);
                                        String genre = mov.getString("main_genre");
                                        Log.d("VAMOS",""+mov+" "+genre);
                                    }
                                }
                                response = response.getJSONObject("status");

                               // resu = response.getJSONArray("data").getJSONObject(2);
                               // String reu = resu.getString("id");
                                String site = response.getString("message"),
                                        network = response.getString("api");
                               // System.out.println("Site: "+site+"\nNetwork: "+network+" ID "+" "+resu);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
    //FlukeInit.getInstance().addToRequestQueue(jreq,"jreq_array_req");

           // FlukeInit.getInstance().addToReqQueue(jreq, "jreq");
            Volley.newRequestQueue(getActivity()).add(jsonRequest);

        }

    }
    private Bitmap getArtistImage(String albumid) {
        Bitmap artwork = null;
        try {
            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri,
                    Long.valueOf(albumid));
            ContentResolver res = getActivity().getContentResolver();
            InputStream in = res.openInputStream(uri);
            artwork = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Exceptionlala", e.toString());
        }
        return artwork;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchSong();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_screen_frag, container, false);
       ivs = (ImageView) view.findViewById(R.id.ivs);
        mtextswitch = (TextSwitcher) view.findViewById(R.id.textswitcher);
        mtextswitch.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.parseColor("#90A4AE"));
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
                int i1 = r.nextInt(2 - 0) + 0;
                mtextswitch.setText(texts[i1]);
                h.postDelayed(this, delay);
            }
        }, delay);
        return view;
    }
}
