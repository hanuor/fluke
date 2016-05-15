package com.hanuor.fluke.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andtinder.view.CardContainer;
import com.hanuor.fluke.R;
import com.hanuor.fluke.apihits.MusicHits;
import com.hanuor.fluke.database.FlukeApp42Database;
import com.hanuor.fluke.serverhandler.ServerTasker;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class SecondScreenFrag extends Fragment {
    CardContainer mcardContainer;
    int checkoir;
    ArrayList<String> mIdstore;
    ArrayList<String> mfinalStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_screen, container, false);

        mcardContainer = (CardContainer) view.findViewById(R.id.layoutview);
        mIdstore = new ArrayList<String>();
        mfinalStore = new ArrayList<String>();
        checkoir = 0;
        MusicHits mah = new MusicHits();
        IntentFilter ifco = mah.searchsong();
        getActivity().registerReceiver(mReceiver, ifco);

        return view;
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
                //Do here what you want to do
            }
            fetchMatchingSuff(track);
            Log.d("Music",artist+":"+album+":     SADASDASDA  "+track+"ablum id"+" "+bundle.get("albumId"));
        }
    };
            private void fetchMatchingSuff(final String track) {

                ServerTasker s = new ServerTasker(getActivity());
                s.execute();
                                FlukeApp42Database.ss.findAllDocuments(FlukeApp42Database.database, FlukeApp42Database.datacollectionId, new App42CallBack() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        // Log.d("Success", "" + o.toString());
                                        Storage storage = (Storage) o;
                                        //This will return JSONObject list, however since Object Id is unique, list will only have one object
                                        final ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
                                        for (int i = 0; i < jsonDocList.size(); i++) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
                                                String id = jsonObject.getString("id");
                                                Log.d("TOTL", "" + id);
                                                String trackJSON = jsonObject.getString("track");
                                                Log.d("Pinging Server","T");
                                                if(track.contentEquals(trackJSON)){
                                                    if(id.contentEquals(FlukeApp42Database.UserID)){
                                                        mIdstore.add(id);
                                                        Log.d("Pingin","YEs"+track+" "+mIdstore.size());
                                                       // updateUIS(mIdstore);
                                                        //fetchMatchingSuff(track);
                                                        //Make an array of ids
                                                    }
                                                    else{
                                                        //pinging the server again
                                                        fetchMatchingSuff(track);
                                                    }
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                       // updateUIS(mIdstore);
                                    }


                                    @Override
                                    public void onException(Exception e) {
                                        Log.d("SuccessNOT",""+e);

                                    }
                                });
                if (mfinalStore!=null){
                    Toast.makeText(getActivity(), " V "+mfinalStore.size()+"   V" + mIdstore.size(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), ""+mfinalStore.size(), Toast.LENGTH_SHORT).show();
                }

  }


    @Override
    public void onPause() {
        super.onPause();
       // onDestroy();
    }

    private void updateUIS(ArrayList<String> mStore) {
        mfinalStore = mStore;
        Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
   // fetchProfiles();

    }


}
