package com.hanuor.fluke.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hanuor.fluke.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shantanu Johri on 17-05-2016.
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    ArrayList<String> fbNames;
    ArrayList<String> fb;

    Context c;
    public ResultAdapter(Context c,ArrayList<String> fbNames,ArrayList<String> fb){
        this.fbNames = fbNames;
        this.fb = fb;
        this.c = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.single_item_second_frag, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CircleImageView cIM =  holder.cIM;
        TextView tv = holder.tv;
        Toast.makeText(c, ""+fbNames.get(position), Toast.LENGTH_SHORT).show();
        tv.setText(fb.get(position));
        Picasso.with(c).load(fbNames.get(position)).into(cIM);



    }

    @Override
    public int getItemCount() {
        return fbNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView cIM;
        TextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.textView);
            cIM = (CircleImageView) itemView.findViewById(R.id.userImage);



        }
    }

}
