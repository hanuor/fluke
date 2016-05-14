package com.hanuor.fluke.launcher;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hanuor.fluke.R;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.social.Social;
import com.shephertz.app42.paas.sdk.android.social.SocialService;
import com.shephertz.app42.paas.sdk.android.upload.UploadFileType;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;
import com.shephertz.app42.paas.sdk.android.user.UserService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    LoginButton fblogin;
    TextView tv;
    CallbackManager mcallbackManager;
    ProfileTracker mProfileT;
    ImageView iv;
    Button remove;
    FileInputStream fin;
    InputStream is;
    ImageView ivd;
    String userid;
    String fPath;
    String aura;
    UploadService upservice;
    UserService us;
    Button b2;

    SocialService msocialserice;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    //User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);

        us = App42API.buildUserService();
        msocialserice = App42API.buildSocialService();
        upservice = App42API.buildUploadService();
        mcallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        Profile mcheck = Profile.getCurrentProfile();
        if(mcheck!=null){
            Toast.makeText(MainActivity.this, "You can now move to the new screen", Toast.LENGTH_SHORT).show();
        }
        fblogin = (LoginButton) findViewById(R.id.login_button);
        remove = (Button) findViewById(R.id.remove);
        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
        ivd = (ImageView) findViewById(R.id.ivd);
        b2 = (Button) findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ac = new Intent();
                ac.setClass(MainActivity.this,FragHandler.class);
                startActivity(ac);

            }
        });
         fblogin.setReadPermissions(Arrays.asList(
                 "public_profile", "email", "user_birthday", "user_friends"));

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ac = new Intent();
                ac.setClass(MainActivity.this,FragHandler.class);
                startActivity(ac);
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
                                    AccessToken tokel = AccessToken.getCurrentAccessToken();

                                    if(tokel!=null){
                                        String userID = object.getString("id");
                                        msocialserice.linkUserFacebookAccount(userID, tokel.getToken(), new App42CallBack() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Social social = (Social) o;
                                                Log.d("APP42and",""+social.getUserName());


                                                Log.d("APP42",""+social.getFacebookProfile().picture);
                                             }

                                            @Override
                                            public void onException(Exception e) {

                                            }
                                        });
                                    }
                                    tv.setText("Hi, " + object.getString("name")+" , "+object.optString("id"));
                                    String mFullname = object.getString("name");
                                    String userID = object.getString("id");
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday");

                                    if(email!=null) {
                                        us.createUser(userID, mFullname, email, new App42CallBack() {
                                            @Override
                                            public void onSuccess(Object o) {
                                            }

                                            @Override
                                            public void onException(Exception e) {
                                            }
                                        });
                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                final String uid = object.optString("id");
                                userid = object.optString("id");
                                String rem = "https://graph.facebook.com/" + uid+ "/picture?type=large";
                               /* Picasso.with(MainActivity.this)
                                        .load("https://graph.facebook.com/" + uid+ "/picture?type=large")
                                        .into(iv);
*/
                                uploadProfilePicture(userid,rem);
                               /* Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            userid = object.optString("id");
                                            Log.v("Id",""+userid);

                                            InputStream ism =  new URL("https://graph.facebook.com/" + userid + "/picture?type=large").openStream();
                                           // is = new URL("https://graph.facebook.com/" + uid + "/picture?type=large").openStream();
                                            upservice.uploadFileForUser("abc1.jpg","ABC", "https://graph.facebook.com/" + userid + "/picture?type=large", UploadFileType.IMAGE, "balsh", new App42CallBack() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Log.v("Response",""+o.toString());
                                                  //  Toast.makeText(MainActivity.this, "Boom!", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onException(Exception e) {

                                                    Log.v("Response",""+e);
                                                   // Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                thread.start();
*/

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

/*


                upservice.getFileByUser("xyz.jpg", "XYZ", new App42CallBack() {
                    @Override
                    public void onSuccess(Object o) {
                        Upload upo = (Upload)o;
                        ArrayList<Upload.File> fileList = upo.getFileList();
                        String urlo = null;
                        for(int i = 0; i < fileList.size();i++ ) {


//                    Toast.makeText(MainActivity.this, "URL "+fileList.get(i).getUrl(), Toast.LENGTH_SHORT).show();
                            urlo = fileList.get(i).getUrl();

                            //imageup(urlo);
                            Log.d("LOLL", "" + fileList.get(i).getUrl());
                            new com.hanuor.fluke.launcher.DownloadImageTask(ivd).execute(fileList.get(i).getUrl());
*/
/*
                            new com.hanuor.fluke.imageutils.D
                            new DownloadImageTask((ImageView) findViewById(R.id.ivd))
                                    .execute(fileList.get(i).getUrl());

                            *//*
//tv.setText(""+urlo);
                        }

                    }

                    @Override
                    public void onException(Exception e) {
//                Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                        Log.d("LOL",""+e);
                    }
                });

*/



    }


    private void uploadProfilePicture(String userid,String url) {
        Picasso.with(MainActivity.this).load(url).into(target);


    }

    private Target target = new Target() {
        String uid = userid;
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new GetFilePath(bitmap).execute();
         }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

            Log.d("Runtastic","GO2");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

            Log.d("Runtastic","GO3");
        }
    };


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



    private class GetFilePath extends AsyncTask<Void, Void, String> {
        ImageView bmImage;
        String address;
        Bitmap bits;

     public GetFilePath(Bitmap bits){
         this.bits = bits;
     }

        @Override
        protected void onPostExecute(String s) {
            Log.v("check",""+s+""+userid);
            //Every time a user logs in ..replace the pic with a new one
            upservice.removeFileByName(userid+".jpg", new App42CallBack() {
                public void onSuccess(Object response)
                {
                    App42Response app42response = (App42Response)response;
                    System.out.println("response is " + app42response) ;
                }
                public void onException(Exception ex)
                {
                    System.out.println("Exception Message"+ex.getMessage());
                }
            });


            upservice.uploadFile(userid + ".jpg", s, UploadFileType.IMAGE, "Image for this user" + userid, new App42CallBack() {
                @Override
                public void onSuccess(Object o) {
                    Log.d("Up","loaded");
                }

                @Override
                public void onException(Exception e) {
                    Log.v("Up"," "+e);
                    e.printStackTrace();

                }
            });


        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("Runtastic", "GO");



            File fm = new File(
                    Environment.getExternalStorageDirectory().getPath()
                            + "/fluke");
            fm.mkdirs();
            File file = new File(fm, userid+".jpg");
            Log.d("Runtastic", "GOa" +
                    file.getPath());
            fPath = file.getAbsolutePath();
            try {
                file.createNewFile();

                Log.d("Runtastic", "GOb");
                FileOutputStream ostream = new FileOutputStream(file);
                Log.d("Runtastic", "GOc");
               // Bitmap bmp = BitmapFactory.decodeFile(fPath);
                bits.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.close();
                return fPath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fPath;
        }
    }
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
