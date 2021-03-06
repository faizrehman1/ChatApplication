package com.example.faiz.mylogin.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.Group_Detail;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.ui.Add_memeber_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<Group_Detail> groupData;
    private Context context;

    AddGroupmemberAdapter adapter;
    private ArrayList<User> user;
    private FirebaseAuth mAuth;



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

        final DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.group_viewadapter,null);

        TextView nameView = (TextView) view.findViewById(R.id.name_View_groupList);
        CircleImageView imgView = (CircleImageView) view.findViewById(R.id.image_View_GroupList);

        final Group_Detail msg = groupData.get(position);


        nameView.setText(msg.getGroupName());

        Glide.with(context)
                .load(msg.getImgUrl())
                .into(imgView);


        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater1 = LayoutInflater.from(context);
                final View view = inflater1.inflate(R.layout.groupdetail_view, null);


                TextView nameView = (TextView) view.findViewById(R.id.groupNamealert);
                TextView adminView = (TextView) view.findViewById(R.id.groupAdminName);

              CircleImageView  imgView_dialog = (CircleImageView) view.findViewById(R.id.groupImage);


                nameView.setText("Group Name: " + groupData.get(position).getGroupName());
                adminView.setText("Admin Name: " + groupData.get(position).getAdminName());

                Glide.with(context)
                        .load(groupData.get(position).getImgUrl())
                        .into(imgView_dialog);

                builder.setNegativeButton("Add Friends", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, Add_memeber_Activity.class);
                                intent.putExtra("groupName",groupData.get(position).getGroupName());
                                intent.putExtra("groupAdminName",groupData.get(position).getAdminName());
                                intent.putExtra("groupUrl",groupData.get(position).getImgUrl());
                                context.startActivity(intent);
                            }
                        });
                        builder.setPositiveButton("Delete Group", null);
                        builder.setView(view);
                        builder.create().show();
                    }
                });
                return view;
            }

        }


