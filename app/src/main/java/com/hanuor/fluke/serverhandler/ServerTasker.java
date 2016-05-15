package com.hanuor.fluke.serverhandler;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.hanuor.fluke.database.FlukeApp42Database;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shantanu Johri on 15-05-2016.
 */
public class ServerTasker extends AsyncTask<Void, Void, Storage> {
    Context c;
    public ServerTasker(Context c) {
        this.c = c;
    }


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




        final ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
        for (int i = 0; i < jsonDocList.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
                String id = jsonObject.getString("id");

                Toast.makeText(c, "Ho gya "+id, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }
    }
}
