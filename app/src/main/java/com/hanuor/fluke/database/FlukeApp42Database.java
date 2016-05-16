package com.hanuor.fluke.database;

import com.facebook.AccessToken;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;

/**
 * Created by Shantanu Johri on 14-05-2016.
 */
public class FlukeApp42Database {
    public static final String database = "flukedata";
    public static final String jsontrack = "{\"track\":\"";
    public static final String userImage = ",\"user_fb_image\":";
    public static final String jsonid = "\",\"id\":";
    public static final String jsonartisit = ",\"artist\":\"";
    public static final String jsonend = "\"}";
    public static final String datacollectionId = "1281537931875901";
    public static final StorageService ss = App42API.buildStorageService();
    public static final String UserID = AccessToken.getCurrentAccessToken().getUserId();

}
