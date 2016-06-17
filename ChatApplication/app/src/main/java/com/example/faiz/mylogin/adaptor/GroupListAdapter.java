package com.example.faiz.mylogin.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.Group_Detail;

import java.util.ArrayList;

public class GroupListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<Group_Detail> groupData;
    private Context context;

    public GroupListAdapter(ArrayList<Group_Detail> groupData, Context context) {
        this.groupData = groupData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return groupData.size();
    }

    @Override
    public Group_Detail getItem(int position) {
        return groupData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.group_viewadapter,null);

        TextView nameView = (TextView) view.findViewById(R.id.name_View_groupList);
        ImageView imgView = (ImageView) view.findViewById(R.id.image_View_GroupList);

        Group_Detail msg = groupData.get(position);


        nameView.setText(msg.getGroupName());


        return view;
    }
}
