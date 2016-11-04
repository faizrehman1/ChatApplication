package com.example.faiz.mylogin.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.GroupListAdapter;
import com.example.faiz.mylogin.model.Group_Detail;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.example.faiz.mylogin.util.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group_Fragment extends android.support.v4.app.Fragment {

    private String selectedImagePath;
    private Bitmap bitmap;
    private String url_cloudinary;
    private Cloudinary cloudinary;
    private FirebaseAuth mAuth;
    private FirebaseUser firebase_user;
    private GroupListAdapter adapter;
    private ArrayList<Group_Detail> arraylist;
    private User users;
    private ArrayList<User> userlist;
    private ListView list;
    private  DatabaseReference firebase;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.groupchatview,null);
        firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        arraylist = new ArrayList<>();
        firebase_user = mAuth.getCurrentUser();



         list = (ListView)view.findViewById(R.id.group_list);
        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.add_group);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Create Group");
                builder.setMessage("You Want to Create Group .. ?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(),Create_Group.class);
                        startActivity(intent);

                    }
                });
                builder.setPositiveButton("Back",null);
                builder.create().show();

           //     Create_Group_Fragment create_group_fragment = new Create_Group_Fragment();
                //    getActivity().getSupportFragmentManager().beginTransaction().hide(new Group_Fragment()).add(R.id.main,create_group_fragment).addToBackStack(null).commit();
            }
        });

        adapter = new GroupListAdapter(arraylist,getActivity());
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),Group_Chat_Act.class);
                intent.putExtra("value",arraylist.get(position).getGroupName());
                intent.putExtra("adminvalue",arraylist.get(position).getAdminName());
                intent.putExtra("imagevalue",arraylist.get(position).getImgUrl());
                startActivity(intent);

            }
        });

        leavegroup();

        try {
//            firebase.child("MyGroup").child(firebase_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                     for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Group_Detail group_detail = data.getValue(Group_Detail.class);
//                    arraylist.add(new Group_Detail(group_detail.getGroupName(), group_detail.getAdminName()));
//                    Log.d("data:", group_detail.getAdminName() + " " + group_detail.getGroupName());
//                      }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

            firebase.child("MyGroup").child(firebase_user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Group_Detail group_detail = data.getValue(Group_Detail.class);
                        arraylist.add(new Group_Detail(group_detail.getGroupName(), group_detail.getAdminName(),group_detail.getImgUrl()));
                        Log.d("data:", group_detail.getAdminName() + " " + group_detail.getGroupName());
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }



        return view;
    }

    public void leavegroup(){

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                 users = userlist.get(position);

                //  final User user = userlist.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Leave Group");
                builder.setPositiveButton("Back",null);
                builder.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if(SharedPref.getCurrentUser(getActivity()).getFname().equals(arraylist.get(position).getAdminName())) {
                                firebase.child("Groupinfo").child(arraylist.get(position).getGroupName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int i = 0;
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            if (i == position) {
                                                DatabaseReference ref = data.getRef();
                                                ref.removeValue();
//                                        arraylist.remove(position);
                                                adapter.notifyDataSetChanged();
                                            }

                                        }


                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                firebase.child("MyGroup").child(firebase_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int i = 0;
                                        AppLogs.logd("idsss"+dataSnapshot);
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                                            if (i == position) {
                                                DatabaseReference ref = data.getRef();
                                                ref.removeValue();
                                                arraylist.remove(position);
                                                adapter.notifyDataSetChanged();
                                            }
                                            //    i++;
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                            }else {
                                firebase.child("MyGroup").child(firebase_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int i = 0;
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                                            if (i == position) {
                                                DatabaseReference ref = data.getRef();
                                                ref.removeValue();
                                                arraylist.remove(position);
                                                adapter.notifyDataSetChanged();
                                            }
                                            //    i++;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
                builder.create().show();



                return true;
            }
        });

    }

    private class ViewHolder{

    }


     }

