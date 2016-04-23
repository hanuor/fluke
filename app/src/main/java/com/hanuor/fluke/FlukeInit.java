package com.hanuor.fluke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.shephertz.app42.paas.sdk.android.App42API;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Shantanu Johri on 23-04-2016.
 */
public class FlukeInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        App42API.initialize(this, String.valueOf(R.string.app42apikey), String.valueOf(R.string.app42secret));
        FacebookSdk.sdkInitialize(this);
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
