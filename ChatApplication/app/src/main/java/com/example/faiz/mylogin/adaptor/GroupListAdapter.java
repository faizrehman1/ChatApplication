package com.example.faiz.mylogin.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.Group_Detail;
import com.example.faiz.mylogin.ui.Conversation_Activity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<Group_Detail> groupData;
    private Context context;
    private Picasso picasso;
    ImageView imgView_dialog;


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
    public View getView(final int position, final View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.group_viewadapter,null);

        TextView nameView = (TextView) view.findViewById(R.id.name_View_groupList);
        ImageView imgView = (ImageView) view.findViewById(R.id.image_View_GroupList);

        final Group_Detail msg = groupData.get(position);


        nameView.setText(msg.getGroupName());

        picasso.with(context)
                .load(msg.getImgUrl())
                .transform(new RoundImage())
                .into(imgView);


        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater1 = LayoutInflater.from(context);
                final View view = inflater1.inflate(R.layout.groupdetail_view, null);

                TextView nameView = (TextView) view.findViewById(R.id.groupNamealert);
                TextView adminView = (TextView) view.findViewById(R.id.groupAdminName);

                imgView_dialog = (ImageView) view.findViewById(R.id.groupImage);


                nameView.setText("Group Name: "+groupData.get(position).getGroupName());
                adminView.setText("Admin Name: "+groupData.get(position).getAdminName());

                picasso.with(context)
                        .load(groupData.get(position).getImgUrl())
                        .transform(new com.example.faiz.mylogin.ui.RoundImage())
                        .into(imgView_dialog);

                builder.setNegativeButton("Add Friends",null);
                builder.setPositiveButton("Delete Group",null);



                builder.setView(view);


                //     builder.setMessage("LOL");

                builder.create().show();
            }
        });





        return view;
    }
}
