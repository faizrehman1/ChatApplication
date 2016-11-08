package com.example.faiz.mylogin.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kamran ALi on 6/14/2016.
 */
public class FriendList_adapter extends BaseAdapter implements ListAdapter {
    private ArrayList<User> user;
    private Context context;
    private LayoutInflater inflater;


    public FriendList_adapter(Context context, ArrayList<User> user) {
        this.context = context;
        this.user = user;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

     //   View view = inflater.inflate(R.layout.frequest_layout, null);

        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.frequest_layout,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.name_View_FriendList);
            viewHolder.imgView = (CircleImageView) convertView.findViewById(R.id.image_View_FriendList);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User msg = user.get(position);
        String imgUrl = msg.getImgUrl();


        String fname = msg.getFname().toString();
        viewHolder.nameView.setText(fname);

        Glide.with(context)
                .load(user.get(position).getImgUrl())
                .into(viewHolder.imgView);
        return convertView;
    }
    private class ViewHolder{
        TextView nameView;
        CircleImageView imgView;
    }
}
