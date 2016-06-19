package com.example.faiz.mylogin.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kamran ALi on 6/14/2016.
 */
public class FriendList_adapter extends BaseAdapter implements ListAdapter {
    private ArrayList<User> user;
    private Context context;
    private Picasso picasso;


    public FriendList_adapter(Context context, ArrayList<User> user) {
        this.context = context;
        this.user = user;
    }

    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public Object getItem(int position) {
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frequest_layout, null);

        TextView nameView = (TextView) view.findViewById(R.id.name_View_FriendList);
        ImageView imgView = (ImageView) view.findViewById(R.id.image_View_FriendList);

        User msg = user.get(position);
        String imgUrl = msg.getImgUrl();


        String fname = msg.getFname().toString();
        nameView.setText(fname);

        picasso.with(context)
                .load(user.get(position).getImgUrl())
                .transform(new com.example.faiz.mylogin.ui.RoundImage())
                .into(imgView);
        return view;
    }
}
