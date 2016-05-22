package com.hanuor.fluke.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.google.gson.Gson;
import com.hanuor.fluke.R;
import com.hanuor.fluke.apihits.ApiName;
import com.hanuor.fluke.apihits.MusicHits;
import com.hanuor.fluke.database.FlukeApp42Database;
import com.hanuor.fluke.gettersetters.JSONServerGS;
import com.hanuor.fluke.serverhandler.JSONManager;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
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
    int insert_flag;
    String fbImageURL = null;
    AccessToken otken;
    FABProgressCircle fabProgressCircle;
    String Doc_id = null;
    int oldback = 0, oldtext = 0;
    public String playingnow_song = null, playingnow_artist = null;
    String texts[] = {"Fluke searches and displays the current playing song automatically","Try changing the track if searching is taking a long time","Matching over music. Please wait while we load the currently playing song"};
    LinearLayout bottom_desc;
    public String song_track  = null;
    CircleImageView artistImage;
    RelativeLayout initLayout;
    LinearLayout finLayout;
    JSONManager jsonManager = new JSONManager();
    JSONServerGS jsonServerGS = new JSONServerGS();

    Gson gson = new Gson();


    private void searchSong(){
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

            Long ai = (Long) bundle.get("albumId");
            Log.d("Toaster",""+ai);
            String ais = String.valueOf(ai);
            //Long mela = Long.getLong(ai);
            if(album!=null) {

                initLayout.setVisibility(View.INVISIBLE);
                finLayout.setAnimation(fadeIn);
                finLayout.setVisibility(View.VISIBLE);
                playingnow_song = track;
                song_track = track;
                playingnow_artist = artist;
                jsonServerGS.setArtist(artist);
                jsonServerGS.setTrack(track);
                hotswapping(artist, track);
            }

            Log.d("Music",artist+":"+album+":     SADASDASDA  "+track+"ablum id"+" "+bundle.get("albumId"));
           // iv.setText("Music"+artist+":"+album+":"+track);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){

        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        mReceiver.abortBroadcast();
    }

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
                                jsonServerGS.setAlbumimage(getad);
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
                                        jsonServerGS.setArtistimage(ass);
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
    setRetainInstance(true);
    otken = AccessToken.getCurrentAccessToken();
    getFBINFO(otken);

    searchSong();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.first_frag, container, false);
        insert_flag = 0;
        final String UID = otken.getUserId();
        // ivs = (ImageView) view.findViewById(R.id.ivs);
        coverImage = (ImageView) view.findViewById(R.id.coverImage);
        mtextswitch = (TextSwitcher) view.findViewById(R.id.textswitcher);
        artistName = (TextView) view.findViewById(R.id.artistName);
        artistSong = (TextView) view.findViewById(R.id.songName);
        coo = (TextView) view.findViewById(R.id.coo);
        cooname = (TextView) view.findViewById(R.id.cooname);
        genre = (TextView) view.findViewById(R.id.genre);
        genrename = (TextView) view.findViewById(R.id.genrename);
        linesep = (TextView) view.findViewById(R.id.lineseparator);
        artistImage = (CircleImageView) view.findViewById(R.id.artistImage);
        bottom_desc = (LinearLayout) view.findViewById(R.id.bottom_desc);
        initLayout = (RelativeLayout) view.findViewById(R.id.initial_layout);
        finLayout = (LinearLayout) view.findViewById(R.id.final_layout);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_action_slideshare_logo);
        fabProgressCircle = (FABProgressCircle) view.findViewById(R.id.fabProgressCircle);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                fabProgressCircle.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       // my_button.setBackgroundResource(R.drawable.defaultcard);
                        fabProgressCircle.beginFinalAnimation();
                       fabProgressCircle.attachListener(new FABProgressListener() {
                           @Override
                           public void onFABProgressAnimationEnd() {
                               Snackbar.make(view,"Posted song on server successfully",Snackbar.LENGTH_SHORT).show();
                               SecondScreenFrag fragment = new SecondScreenFrag();

                               FragmentManager fm = getActivity().getSupportFragmentManager();
                               FragmentTransaction ft = fm.beginTransaction();
                               ft.replace(R.id.frame_container, fragment);
                               ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                               ft.commit();
                            }
                       });
                        // fabProgressCircle.hide();
                    }
                }, 2000);
                final String collection_name = UID;
                //final StringBuilder vs = new StringBuilder();
                jsonManager.setArtistImage(jsonServerGS.getArtistimage());
                jsonManager.setArtist(jsonServerGS.getArtist());
                jsonManager.setAlbumImage(jsonServerGS.getAlbumimage());
                jsonManager.setTrack(jsonServerGS.getTrack());
                jsonManager.setEbemail(jsonServerGS.getEbemail());
                jsonManager.setFbName(jsonServerGS.getFbName());
                jsonManager.setId(jsonServerGS.getId());
                jsonManager.setFbUserpic(jsonServerGS.getFbUserpic());
                Log.d("FBI JSS",""+jsonServerGS.getFbName());
                final String jsons = gson.toJson(jsonManager,JSONManager.class);
          FlukeApp42Database.ss.findAllDocuments(FlukeApp42Database.database, FlukeApp42Database.datacollectionId, new App42CallBack() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Log.d("Success",""+o.toString());
                                        Storage  storage  = (Storage )o;
                                        //This will return JSONObject list, however since Object Id is unique, list will only have one object
                                        final ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
                                         int inserter_checker = 1;
                                        if(jsonDocList.size()==0){
                                            FlukeApp42Database.ss.insertJSONDocument(FlukeApp42Database.database,FlukeApp42Database.datacollectionId, jsons, new App42CallBack() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Log.d("DoneY","VAMOS");


                                                }

                                                @Override
                                                public void onException(Exception e) {
                                                    Log.d("DoneY",""+e);
                                                }
                                            });


                                        }else {
                                            for (int i = 0; i < jsonDocList.size(); i++) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
                                                    String id = jsonObject.getString("id");
                                                    Log.d("TOTL", "" + id);
                                                    String track = jsonObject.getString("track");
                                                    if (id.contentEquals(UID)) {
                                                        inserter_checker = 1;
                                                        insert_flag = 0;
                                                        Log.d("TOTLA", "India");
                                                        String DOCid = jsonDocList.get(i).getDocId();
                                                        final int finalI = i;
                                                        FlukeApp42Database.ss.deleteDocumentById(FlukeApp42Database.database, FlukeApp42Database.datacollectionId, DOCid, new App42CallBack() {
                                                            @Override
                                                            public void onSuccess(Object o) {
                                                                Log.d("DeletedI", "Success");
                                                                inserter_meth(jsons, jsonDocList.size(), finalI);
                                                            }

                                                            @Override
                                                            public void onException(Exception e) {
                                                                Log.d("DeletedI", "" + e);
                                                            }
                                                        });
                                                    } else {
                                                        insert_flag = 1;
                                                        // break;


                                                    }
                                                    Log.d("Succ", "" + id + " and " + track);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        if(insert_flag == 1){
                                            Log.d("TOTLA","insertis");

                                            FlukeApp42Database.ss.insertJSONDocument(FlukeApp42Database.database,FlukeApp42Database.datacollectionId, jsons, new App42CallBack() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Log.d("DoneY","VAMOS");

                                                }

                                                @Override
                                                public void onException(Exception e) {
                                                    Log.d("DoneY",""+e);
                                                }
                                            });
                                            insert_flag = 0;

                                        }
                                        }

                                    @Override
                                    public void onException(Exception e) {

                                        FlukeApp42Database.ss.insertJSONDocument(FlukeApp42Database.database,FlukeApp42Database.datacollectionId, jsons, new App42CallBack() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Log.d("DoneY","VAMOS");

                                            }

                                            @Override
                                            public void onException(Exception e) {
                                                Log.d("DoneY",""+e);
                                            }
                                        });


                                        Log.d("SuccessNOT",""+e);

                                    }
                                });

                            }
                        });



        mtextswitch.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.parseColor("#4d490027"));
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
        return view;
    }


    private void getFBINFO(AccessToken otken) {

        Log.d("FBI","A"+otken);
        try {
            GraphRequest request = GraphRequest.newMeRequest(otken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        String useridss = object.getString("id");
                        String rem = "https://graph.facebook.com/" + useridss+ "/picture?type=large";
                        jsonServerGS.setId(object.getString("id"));
                        jsonServerGS.setFbName(object.getString("name"));
                        jsonServerGS.setEbemail(object.getString("email"));
                        jsonServerGS.setFbUserpic(rem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (Exception es){
                        es.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void inserter_meth(String jsonString, int total, int current) {
        Log.d("TOTLA",""+total+" and C "+current);
        if(total == current+1) {

            FlukeApp42Database.ss.insertJSONDocument(FlukeApp42Database.database, FlukeApp42Database.datacollectionId, jsonString, new App42CallBack() {
                @Override
                public void onSuccess(Object o) {
                    Log.d("DoneY", "VAMOS");

                }

                @Override
                public void onException(Exception e) {
                    Log.d("DoneY", "" + e);
                }
            });

        }

    }
}
