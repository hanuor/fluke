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
import com.hanuor.fluke.interfaces.ServerInterface;
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
    public ServerInterface serverInterface = null;

    public ServerTasker(Context c,ServerInterface serverInterface) {
        this.c = c;
        this.serverInterface = serverInterface;
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
            String dbName = FlukeApp42Database.database;
            String dataCollection = FlukeApp42Database.datacollectionId;
            Storage sto = FlukeApp42Database.ss.findAllDocuments(dbName,dataCollection);
            return sto;


    }

    @Override
    protected void onPostExecute(Storage storage) {
        ArrayList<String> idoo = new ArrayList<>();



        final ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
        for (int i = 0; i < jsonDocList.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
                String name = jsonObject.getString("fbName").replace(" ","%20");

                String track = jsonObject.getString("track").replaceAll(" ","%20");
                String mail = jsonObject.getString("ebemail");
                String artist = jsonObject.getString("artist").replace(" ","%20");
                String artistIm = jsonObject.getString("artistImage");
                String albumIm = jsonObject.getString("albumImage");
                String userPic = jsonObject.getString("fbUserpic");
               /* if (playing_now != null) {
                    if (playing_now.contentEquals(track)) {
                        if (name.contentEquals("Shantanu Johri")) {
               */             StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(name+" "+userPic+ " "+mail+" "+track+" "+artist+" "+artistIm+" "+ albumIm);
                            // stringBuilder.append(name+FlukeApp42Database.separator+userPic+FlukeApp42Database.separator+mail+FlukeApp42Database.separator+track+FlukeApp42Database.separator+artist+FlukeApp42Database.separator+artistIm+FlukeApp42Database.separator+albumIm);
                            idoo.add(stringBuilder.toString());
                            Log.d("Pingin", "YEs" + playing_now + " " + idoo.size());
                           //  } else {
                            //pinging the server again
                            //fetchMatchingSuff(track);
                            Log.d("REEXE","REXEXEXEX");
                           // ServerTasker sa = new ServerTasker(c,serverInterface);
                            //sa.execute();
/*

                        }
                    }
                }
*/

                   }catch(Exception e){

                }

        }
        if (idoo!=null) {
            serverInterface.fetchalldetails(idoo);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

}
