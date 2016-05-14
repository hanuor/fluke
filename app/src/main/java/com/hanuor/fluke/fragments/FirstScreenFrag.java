package com.hanuor.fluke.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hanuor.fluke.R;
import com.hanuor.fluke.apihits.ApiName;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shantanu Johri on 08-05-2016.
 */
public class FirstScreenFrag extends Fragment {
    private TextSwitcher mtextswitch;
    ImageView coverImage;
    TextView artistName;
    TextView artistSong;
    TextView linesep;
    TextView genre, genrename;
    TextView coo, cooname;
    FloatingActionButton fab;
    int oldback = 0, oldtext = 0;
    String texts[] = {"Fluke searches and displays the current playing song automatically","Try changing the track if searching is taking a long time"};
    LinearLayout bottom_desc;
    CircleImageView artistImage;


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

            Long ai = (Long) bundle.get("albumId");
            Log.d("Toaster",""+ai);
            String ais = String.valueOf(ai);
            //Long mela = Long.getLong(ai);
            if(album!=null) {
                Cursor cursor = getActivity().managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID+ "=?",
                        new String[] {String.valueOf(ais)},
                        null);

                if (cursor.moveToFirst()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                   Log.d("PAther",""+path);
                    // do whatever you need to do
                }
                // Log.d("INERER",res.print()) ;
                hotswapping(artist, track);
            }

            Log.d("Music",artist+":"+album+":     SADASDASDA  "+track+"ablum id"+" "+bundle.get("albumId"));
           // iv.setText("Music"+artist+":"+album+":"+track);

        }
    };

    private void hotswapping(String album, String track) {
        if(album!=null) {
            StringBuilder mstay = new StringBuilder();
            mstay.append(ApiName.MUSICGRAPH_ARTIST);
            mstay.append(album);
            String momo = mstay.toString().replaceAll(" ","%20");


            //Api Appending for artist picture
            artistName.setText(""+album);
            artistSong.setText(""+track);
            StringBuilder m = new StringBuilder();
            m.append(ApiName.LASTFM_ARTIST);
            m.append(album);
            m.append(ApiName.LASTFM_APIFORMAT);

            //Api appending for album picture
            StringBuilder ck = new StringBuilder();
            ck.append(ApiName.LASTFM_TRACKINFO);
            ck.append(""+album+"&");
            ck.append(""+"track="+track+"&format=json");
            String meso = ck.toString();
            String newmeso = meso.replaceAll(" ", "%20");

            String adler = m.toString();
            String newadler = adler.replaceAll(" ", "%20");
            Log.d("ERRORORORROR",""+newadler);

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, newmeso, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONObject resu;
                                resu  = response.getJSONObject("track");
                                JSONObject resul  = resu.getJSONObject("album");

                                JSONArray arry = resul.getJSONArray("image");
                                JSONObject omk = arry.getJSONObject(3);

                                String getad = omk.getString("#text");
                                //Log.d("IMAGELOAd",""+getad);
                                // artistimage.setColorFilter(Color.parseColor("#33ffffff"));

                                Picasso.with(getActivity()).load(getad).into(coverImage);
                              //  Picasso.with(getActivity()).load(getad).into(targetq);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("MYMY",""+e);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            Volley.newRequestQueue(getActivity()).add(jsonRequest);
            //THE BEGINNNING

            JsonObjectRequest artistRequest = new JsonObjectRequest
                    (Request.Method.GET, newadler, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONObject resu;

                                resu = response.getJSONObject("results");
                                Log.d("MEMYSELFANDI",""+resu.length());
                                JSONObject arr = resu.getJSONObject("artistmatches");
                                JSONArray otb = arr.getJSONArray("artist");
                                for(int i = 0;i<otb.length();i++){
                                    JSONObject mom = otb.getJSONObject(i);
                                    Log.d("MYMYMY",""+mom.length());
                                    JSONArray and = mom.getJSONArray("image");
                                    String ads = and.getString(2);

                                    //    String amd = mom.getString("#text");
                                    JSONObject amd = and.getJSONObject(3);

                                    String ass = amd.getString("#text");
                                    Log.d("MYMYMYMY",""+ass);
                                    if(ass!=null){

                                        Picasso.with(getActivity()).load(ass).into(artistImage);
                                        Picasso.with(getActivity()).load(ass).into(targetq);
                                        break;
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("MYMY",""+e);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            Volley.newRequestQueue(getActivity()).add(artistRequest);
       //Artist Info
            JsonObjectRequest Info = new JsonObjectRequest
                    (Request.Method.GET, momo, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                            JSONArray abc = response.getJSONArray("data");
                               // JSONObject imp = abc.getJSONObject(1);
                                for(int h=0;h<abc.length();h++){
                                    JSONObject vs = abc.getJSONObject(h);
                                    String gen = vs.getString("main_genre");
                                    cooname.setText(gen);
                                    genrename.setText(vs.getString("country_of_origin"));

                                }
                               // String coun = imp.getString("country_of_origin");
                               // coo.setText(coun);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("MYMY",""+e);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            Volley.newRequestQueue(getActivity()).add(Info);


        }

    }
    Target imageload = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    Target targetq = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
           // Toast.makeText(getActivity(), "Loaded", Toast.LENGTH_SHORT).show();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrantSwatch = palette.getMutedSwatch();
                    int oldc =vibrantSwatch.getRgb();
                    int oldt = vibrantSwatch.getTitleTextColor();
                    coo.setTextColor(vibrantSwatch.getTitleTextColor());
                    cooname.setTextColor(vibrantSwatch.getTitleTextColor());
                    linesep.setBackgroundColor(vibrantSwatch.getBodyTextColor());
                    genrename.setTextColor(vibrantSwatch.getTitleTextColor());
                    genre.setTextColor(vibrantSwatch.getTitleTextColor());
                        if(oldback == 0 && oldtext == 0){
                            ValueAnimator colorAnimati = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#B0BEC5"), oldt);
                            colorAnimati.setDuration(2500); // milliseconds
                            colorAnimati.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    //songLoaded.setAlpha((int) valueAnimator.getAnimatedValue());
                                    artistName.setTextColor((int) valueAnimator.getAnimatedValue());
                                    artistSong.setTextColor((int) valueAnimator.getAnimatedValue());
                                    //songLoaded.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                                }
                            });
                            colorAnimati.start();

                            ValueAnimator colorAnimatio = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#B0BEC5"), oldc);
                            colorAnimatio.setDuration(2500); // milliseconds
                            colorAnimatio.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    //songLoaded.setAlpha((int) valueAnimator.getAnimatedValue());
                                    bottom_desc.setBackgroundColor((int) valueAnimator.getAnimatedValue());

                                    //songLoaded.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                                }
                            });
                            colorAnimatio.start();

                            handlecolorvalue(oldc, oldt);
                        }else{

                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), oldback, oldc);
                            colorAnimation.setDuration(2500); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    bottom_desc.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                                    // songLoaded.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();

                            ValueAnimator colorAni = ValueAnimator.ofObject(new ArgbEvaluator(), oldtext, oldt);
                            colorAni.setDuration(2500); // milliseconds
                            colorAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    artistName.setTextColor((int) valueAnimator.getAnimatedValue());
                                    artistSong.setTextColor((int) valueAnimator.getAnimatedValue());
                                    // songLoaded.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                                }
                            });
                            colorAni.start();
                            handlecolorvalue(oldc,oldt);



                        }
                        //vamos.setBackgroundColor(vibrantSwatch.getTitleTextColor());
                        //Toast.makeText(getActivity(), "FALSE", Toast.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private void handlecolorvalue(int oldyc, int oldyt) {
        oldback = oldyc;
        oldtext = oldyt;

    }
@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchSong();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_frag, container, false);
        // ivs = (ImageView) view.findViewById(R.id.ivs);
        coverImage = (ImageView) view.findViewById(R.id.coverImage);
        // mtextswitch = (TextSwitcher) view.findViewById(R.id.textswitcher);
        artistName = (TextView) view.findViewById(R.id.artistName);
        artistSong = (TextView) view.findViewById(R.id.songName);
        coo = (TextView) view.findViewById(R.id.coo);
        cooname = (TextView) view.findViewById(R.id.cooname);
        genre = (TextView) view.findViewById(R.id.genre);
        genrename = (TextView) view.findViewById(R.id.genrename);
        linesep = (TextView) view.findViewById(R.id.lineseparator);
        artistImage = (CircleImageView) view.findViewById(R.id.artistImage);
        bottom_desc = (LinearLayout) view.findViewById(R.id.bottom_desc);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_action_slideshare_logo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /*mtextswitch.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.parseColor("#90A4AE"));
                return textView;
            }
        });
       *///

      /*  Animation fadeIn = new AlphaAnimation(0, 1);
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
    }*/
    return view;
    }
}
