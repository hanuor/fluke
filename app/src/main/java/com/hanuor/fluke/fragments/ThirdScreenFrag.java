package com.hanuor.fluke.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.hanuor.fluke.R;
import com.hanuor.fluke.database.IdDatabase;
import com.hanuor.fluke.launcher.FragHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shantanu Johri on 20-05-2016.
 */
public class ThirdScreenFrag extends AppCompatActivity {

    CircleImageView cimUser;
    TextView userName;
    TextView userEmail;
    FloatingActionButton fabSearch;
    Button logout;
    int tempColor = 0;
    IdDatabase idDatabase;
    int colors[] = {Color.parseColor("#E91E63"),Color.parseColor("#B71C1C"),Color.parseColor("#0D47A1"),Color.parseColor("#006064")};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_frag);
        Log.d("CreateOn","VVVVV");
        idDatabase = new IdDatabase(this);
        logout = (Button) findViewById(R.id.logOut);
        cimUser = (CircleImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.userEmail);
        fabSearch = (FloatingActionButton) findViewById(R.id.backtohome);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.d("UIDSSA",idDatabase.query());
        //Change this
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String mFullname = object.getString("name");
                    String memail = object.getString("email");
                    String userid = object.optString("id");
                    String rem = "https://graph.facebook.com/" + userid+ "/picture?type=large";
                    Log.v("Auth",""+rem);
                    Picasso.with(ThirdScreenFrag.this).load(rem).into(cimUser);
                    userName.setText(mFullname);
                    userEmail.setText(memail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();


        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sar = new Intent(ThirdScreenFrag.this, FragHandler.class);
                sar.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(sar);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.ma keText(ThirdScreenFrag.this, "Click", Toast.LENGTH_SHORT).show();
                Log.d("UIDLL",""+idDatabase.query());
            idDatabase.clear();
                Log.d("UIDLD",""+idDatabase.query());

                animate();


            }
        });
    }

    private void animate() {

                    ValueAnimator colorAnimati = ValueAnimator.ofObject(new ArgbEvaluator(), colors[tempColor], Color.parseColor("#880E4F"));
                    colorAnimati.setDuration(2500); // milliseconds
                    colorAnimati.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            logout.setTextColor((int) valueAnimator.getAnimatedValue());


                        }

                    });

                    colorAnimati.start();

        LoginManager.getInstance().logOut();
        finish();
    }

}

