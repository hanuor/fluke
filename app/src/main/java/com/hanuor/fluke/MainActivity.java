package com.hanuor.fluke;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.android.callback.KinveyUserDeleteCallback;
import com.kinvey.java.User;
import com.kinvey.java.core.DownloaderProgressListener;
import com.kinvey.java.core.MediaHttpDownloader;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.user.UserService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    LoginButton fblogin;
    TextView tv;
    CallbackManager mcallbackManager;
    ProfileTracker mProfileT;
    ImageView iv;
    Client mKinveyClient;
    Button remove;
    FileInputStream fin;
    InputStream is;
    Client getKinveyService;
    ImageView ivd;
    String userid;
    Button au;

    //User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App42API.initialize(MainActivity.this,"31fe046a4bba23fbeb15c63ab0dc976ba035b7bda880a79246fc002a2efd5843","13b404947b35ce97ba546d6539914dcf262ea967ec20fecdfc72e2532c5cfe4a");
        mKinveyClient = new Client.Builder("kid_Z13FKXn9ax", "eb1eafee5c4d463ca65032faccb9f5de"
                , this.getApplicationContext()).build();
        printKeyHash();
        final UserService us = App42API.buildUserService();

        mKinveyClient.ping(new KinveyPingCallback() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Log.d("Success",""+aBoolean);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });



        FacebookSdk.sdkInitialize(this);
        mcallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        getKinveyService = this.getKinveyService();


        fblogin = (LoginButton) findViewById(R.id.login_button);
        remove = (Button) findViewById(R.id.remove);
        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
        ivd = (ImageView) findViewById(R.id.ivd);
        au = (Button) findViewById(R.id.au);
         fblogin.setReadPermissions(Arrays.asList(
                 "public_profile", "email", "user_birthday", "user_friends"));
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKinveyService.user().delete(true, new KinveyUserDeleteCallback() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "YOu are deletetd", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        });
        au.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                us.createUser("ABC", "res", "abc@gmai.com", new App42CallBack() {
                    @Override
                    public void onSuccess(Object o) {

                        Toast.makeText(MainActivity.this,"User inserted",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onException(Exception e) {
                        Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        fblogin.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("facebookresult",""+loginResult);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                try {

                                    getKinveyService.user().create(userid, "ds", new KinveyUserCallback() {
                                        @Override
                                        public void onSuccess(User user) {

                                            try {
                                                getKinveyService.user().put("E_mail",""+object.getString("email"));
                                                getKinveyService.user().put("Fullname",""+object.getString("name"));

                                                getKinveyService.user().update(new KinveyUserCallback() {
                                                    @Override
                                                    public void onSuccess(User user) {
                                                        Toast.makeText(MainActivity.this, "User Saved into the database success!", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable throwable) {
                                                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Throwable throwable) {
                                            Toast.makeText(MainActivity.this, ""+throwable, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                      tv.setText("Hi, " + object.getString("name")+" , "+object.optString("id"));
                                    final String uid = object.optString("id");
                                    userid = object.optString("id");
                                    Picasso.with(MainActivity.this)
                                            .load("https://graph.facebook.com/" + uid+ "/picture?type=large")
                                            .into(iv);
                                    Thread thread = new Thread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            try {
                                                Log.v("tue","huhi");
                                               // fin = new FileInputStream(new File("https://graph.facebook.com/" + uid + "/picture?type=large"));
                                                Log.v("tue2",""+fin);
                                                is = new URL("https://graph.facebook.com/" + uid + "/picture?type=large").openStream();
                                                Log.v("tue1",""+is);
                                                final FileMetaData fmd = new FileMetaData("vamos");
                                                Log.v("Turn",""+fmd+"here");
                                                fmd.setPublic(true);

                                                fmd.setFileName(""+userid+"pic");
                                                getKinveyService.file().upload(fmd, is, new UploaderProgressListener() {
                                                    @Override
                                                    public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {

                                                    }

                                                    @Override
                                                    public void onSuccess(FileMetaData fileMetaData) {
                                                        Toast.makeText(MainActivity.this, "SAved the profile pic"+fileMetaData, Toast.LENGTH_SHORT).show();
                                                       Log.v("URL","sdsd");
                                                        String urloffile =
                                                                fileMetaData.getDownloadURL();
                                                        Log.v("URLmm",""+urloffile);
                                                        Toast.makeText(MainActivity.this, "URL "+" "+urloffile, Toast.LENGTH_SHORT).show();
                                                        try {
                                                            final FileOutputStream os = new FileOutputStream(new File(getFilesDir(),"vamosi"));
                                                            FileMetaData jsd = new FileMetaData("vamos");
                                                            getKinveyService.file().download(jsd, os, new DownloaderProgressListener() {
                                                                @Override
                                                                public void progressChanged(MediaHttpDownloader mediaHttpDownloader) throws IOException {

                                                                }

                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(MainActivity.this, "Image Downloaded", Toast.LENGTH_SHORT).show();
                                                                    Log.v("Os",""+os);
                                                                }

                                                                @Override
                                                                public void onFailure(Throwable throwable) {
                                                                    Log.v("throwable",""+throwable);
                                                                    Toast.makeText(MainActivity.this, "Error "+throwable, Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        } catch (FileNotFoundException e) {
                                                            e.printStackTrace();
                                                        }


                                                    }

                                                    @Override
                                                    public void onFailure(Throwable throwable) {
                                                        Log.v("Error",""+throwable);
                                                        Toast.makeText(MainActivity.this, "Error"+throwable, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //Your code goes here
                                            }
                                            catch (Exception e)
                                            {

                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                        thread.start();

                                } catch(JSONException ex) {
                                    ex.printStackTrace();

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("blae",""+error);

            }
        });
        mProfileT = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                tv.setText(""+currentProfile.getName());
            }
        };
        mProfileT.startTracking();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    mcallbackManager.onActivityResult(requestCode, resultCode,data);
    }

    @Override
    protected void onResume() {
        super.onResume();

    Profile mp = Profile.getCurrentProfile();
        if(mp!=null){
            Log.d("blaq",""+mp.getName());
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
    public Client getKinveyService(){
        return mKinveyClient;
    }

}
