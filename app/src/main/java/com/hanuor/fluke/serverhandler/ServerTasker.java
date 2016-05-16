package com.hanuor.fluke.serverhandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.hanuor.fluke.apihits.MusicHits;
import com.hanuor.fluke.database.FlukeApp42Database;
import com.hanuor.fluke.fragments.FirstScreenFrag;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Shantanu Johri on 15-05-2016.
 */
public class ServerTasker extends AsyncTask<Void, Void, Storage> {
    Context c;
    String playing_now = null;

    public ServerTasker(Context c) {
        this.c = c;
    }

    @Override
    protected void onPreExecute() {
        MusicHits mah = new MusicHits();
        IntentFilter ifco = mah.searchsong();
        c.registerReceiver(mReceiver, ifco);

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
            //fetchMatchingSuff(track);
            playing_now =  track;
            Log.d("Music",artist+":"+album+":     SADASDASDA  "+track+"ablum id"+" "+bundle.get("albumId"));
        }
    };


    @Override
    protected Storage doInBackground(Void... voids) {
        try {
            String dbName = FlukeApp42Database.database;
            String dataCollection = FlukeApp42Database.datacollectionId;
            Storage sto = FlukeApp42Database.ss.findAllDocuments(dbName,dataCollection);
            return sto;
        } catch (App42Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    @Override
    protected void onPostExecute(Storage storage) {
        ArrayList<String> idoo = new ArrayList<>();



        final ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
        for (int i = 0; i < jsonDocList.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
                String id = jsonObject.getString("id");
                Log.d("TOTL", "" + id);
                String trackJSON = jsonObject.getString("track");
                Log.d("Pinging Server", "T");
                FirstScreenFrag ms = new FirstScreenFrag();
                //String track = ms.song_track;
                Log.d("Pingin",""+playing_now);
                if (playing_now != null) {
                    if (playing_now.contentEquals(trackJSON)) {
                        if (id.contentEquals(FlukeApp42Database.UserID)) {
                            idoo.add(id);
                            Log.d("Pingin", "YEs" + playing_now + " " + idoo.size());
                             } else {
                            //pinging the server again
                            //fetchMatchingSuff(track);
                            ServerTasker sa = new ServerTasker(c);
                            sa.execute();

                        }
                    }
                }

                   }catch(Exception e){

                }

        }


    }
}
