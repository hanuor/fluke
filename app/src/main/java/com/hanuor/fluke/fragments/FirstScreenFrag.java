package com.hanuor.fluke.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Random;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shantanu Johri on 08-05-2016.
 */
public class FirstScreenFrag extends Fragment {
    private TextSwitcher mtextswitch;
    RelativeLayout cvy;
    RelativeLayout songLoaded;


    RelativeLayout songNotLoaded;
    ImageView vamos;
    TextView musica;

    int oldback, oldtext;
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
     CircleImageView artistimage;


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
            songLoaded.setVisibility(View.VISIBLE);
            songNotLoaded.setVisibility(View.INVISIBLE);
            StringBuilder m = new StringBuilder();
            m.append(ApiName.LASTFM_ARTIST);
            m.append(album);
            m.append(ApiName.LASTFM_APIFORMAT);
            StringBuilder ck = new StringBuilder();
            ck.append(ApiName.LASTFM_TRACKINFO);
            ck.append(""+album+"&");
            ck.append(""+"track="+track+"&format=json");
            String meso = ck.toString();
            String newmeso = meso.replaceAll(" ", "%20");

            String adler = m.toString();
            String newadler = adler.replaceAll(" ", "%20");
            Log.d("ERRORORORROR",""+newadler);
           // URL sourceUrl = new URL(temp);
          //  Log.d("ERROROROROROR",""+adler);
           // String append = ApiName.MUSICGRAPH_ARTIST + "" + album;
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
                                Log.d("IMAGELOAd",""+getad);
                                artistimage.setColorFilter(Color.parseColor("#33ffffff"));

                                Picasso.with(getActivity()).load(getad).into(artistimage);
                                Picasso.with(getActivity()).load(getad).into(target);

                                /*resu = response.getJSONObject("results");
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

                                        Picasso.with(getActivity()).load(ass).into(artistimage);
                                        Picasso.with(getActivity()).load(ass).into(target);
                                        break;
                                    }else{
                                        Picasso.with(getActivity()).load(R.drawable.headset).into(target);
                                      }


                                }
                                *//*if(bitmap!=null){

                                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            Palette.Swatch vib =palette.getMutedSwatch();
                                            if(vib!=null){
                                                cvy.setBackgroundColor(vib.getBodyTextColor());
                                            }

                                        }*/
                                   // });

                               // }
                                     /*
                                if(response.has("results")){
                                    Log.d("TRUE","tre");
                                    JSONArray arr = response.getJSONArray("artist");
                                    String imageurl = null;
                                    Log.d("HEY",""+arr.length());


                                    //Picasso.with(getActivity()).load(imageurl).into(artistimage);
                                }else{
                                    Log.d("HEY","JOKOKO");
                                }
                               */// response = response.getJSONObject("status");

                               // resu = response.getJSONArray("data").getJSONObject(2);
                               // String reu = resu.getString("id");
                               // String site = response.getString("message"),
                                      //  network = response.getString("api");
                               // System.out.println("Site: "+site+"\nNetwork: "+network+" ID "+" "+resu);
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
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
           // Toast.makeText(getActivity(), "Loaded", Toast.LENGTH_SHORT).show();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrantSwatch = palette.getMutedSwatch();
                    int oldc =vibrantSwatch.getRgb();
                    int oldt = vibrantSwatch.getTitleTextColor();
                        if(oldback == 0 && oldtext == 0){
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#eeeeee"), oldc);
                            colorAnimation.setDuration(2500); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    songLoaded.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();

                            handlecolorvalue(oldc, oldt);
                        }else{
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), oldback, oldc);
                            colorAnimation.setDuration(2500); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    songLoaded.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();
                            handlecolorvalue(oldc,oldt);



                        }
                        vamos.setBackgroundColor(vibrantSwatch.getTitleTextColor());
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
      // ivs = (ImageView) view.findViewById(R.id.ivs);
        cvy = (RelativeLayout) view.findViewById(R.id.relLayout);
        songLoaded = (RelativeLayout) view.findViewById(R.id.songloaded);
        songNotLoaded = (RelativeLayout) view.findViewById(R.id.songnotloaded);
        musica = (TextView) view.findViewById(R.id.musica);
        vamos = (ImageView) view.findViewById(R.id.vamos);
        mtextswitch = (TextSwitcher) view.findViewById(R.id.textswitcher);
        artistimage = (CircleImageView) view.findViewById(R.id.circleImage);
        mtextswitch.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.parseColor("#90A4AE"));
                return textView;
            }
        });
       //

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
