package com.hanuor.fluke.init;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.shephertz.app42.paas.sdk.android.App42API;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Shantanu Johri on 23-04-2016.
 */
public class FlukeInit extends Application {

    public static final String TAG = FlukeInit.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private static FlukeInit mInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        App42API.initialize(this, "31fe046a4bba23fbeb15c63ab0dc976ba035b7bda880a79246fc002a2efd5843", "13b404947b35ce97ba546d6539914dcf262ea967ec20fecdfc72e2532c5cfe4a");
        FacebookSdk.sdkInitialize(this);
        mInstance = this;
 }

    public static synchronized FlukeInit getInstance() {
        return mInstance;
    }

    public RequestQueue getReqQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public void cancelPendingReq(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.hanuor.fluke", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
}
