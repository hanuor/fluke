package com.hanuor.fluke.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.hanuor.fluke.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shantanu Johri on 20-05-2016.
 */
public class ThirdScreenFrag extends Fragment {

    CircleImageView cimUser;
    TextView userName;
    TextView userEmail;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_frag, container, false);
            cimUser = (CircleImageView) view.findViewById(R.id.userImage);
            userName = (TextView) view.findViewById(R.id.userName);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String mFullname = object.getString("name");
                    String memail = object.getString("email");
                    String userid = object.optString("id");
                    String rem = "https://graph.facebook.com/" + userid+ "/picture?type=large";
                    Picasso.with(getActivity()).load(rem).into(cimUser);
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



        return view;
    }
}
