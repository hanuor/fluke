package com.hanuor.fluke.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.hanuor.fluke.R;
import com.hanuor.fluke.database.FlukeApp42Database;
import com.hanuor.fluke.interfaces.ServerInterface;
import com.hanuor.fluke.serverhandler.ServerTasker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondScreenFrag extends Fragment{
    CardContainer mcardContainer;
    int checkoir;
    ArrayList<String> mIdstore;
    ArrayList<String> mfinalStore;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_screen, container, false);

        mcardContainer = (CardContainer) view.findViewById(R.id.layoutview);
        mcardContainer.setOrientation(Orientations.Orientation.Disordered);




        ServerTasker s = (ServerTasker) new ServerTasker(getActivity(), new ServerInterface() {
            @Override
            public void fetchalldetails(ArrayList<String> fetchAll) {

                List<String> fetchDetails;
                Toast.makeText(getActivity(), "Yipeee", Toast.LENGTH_SHORT).show();
                //Do your task here
                for(int i=0;i<fetchAll.size();i++) {

                    fetchDetails = Arrays.asList(fetchAll.get(i).split(FlukeApp42Database.separator));
                    Log.d("STRINGER",""+fetchDetails.get(0));
                   /* String fbName = fetchDetails.get(6);
                    String fbPic = fetchDetails.get(5);
                    String track = fetchDetails.get(0);
                    String email = fetchDetails.get(1);
                    String artist = fetchDetails.get(2);
                   */ final CardModel model = new CardModel();
                    //model.setTitle(fbName);
                    /*Picasso.with(getActivity()).load(fbPic).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Drawable mD = new BitmapDrawable(getActivity().getResources(),bitmap);
                            model.setCardImageDrawable(mD);

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
*/
                }




            }
        }).execute();



        checkoir = 0;

        return view;
    }



    @Override
    public void onPause() {
        super.onPause();
       // onDestroy();
    }


}
