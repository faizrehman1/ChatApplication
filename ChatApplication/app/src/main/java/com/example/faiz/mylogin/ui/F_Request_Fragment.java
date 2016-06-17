package com.example.faiz.mylogin.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.FriendList_adapter;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.example.faiz.mylogin.util.NodeRef;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class F_Request_Fragment extends Fragment {
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private DatabaseReference firebase;
    private ArrayList<User> nameList;
    private FriendList_adapter adapter;
    private ListView listView;
    private User userCall;


    public F_Request_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f_request_, container, false);
        firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        nameList = new ArrayList<>();
        adapter = new FriendList_adapter(getActivity(), nameList);

        listView = (ListView) view.findViewById(R.id.fRequest_ListView2);

        Toast.makeText(getActivity(), "frequestFragment", Toast.LENGTH_LONG).show();
        AppLogs.loge("FrequestFragment");

        AppLogs.loge("CurrentUser " + mAuth.getCurrentUser().getUid().toString());
        AppLogs.loge("UID unknown " + firebase.child(NodeRef.FRIEND_REQUEST).child(mAuth.getCurrentUser().getUid()).toString());
        firebase.child(NodeRef.FRIEND_REQUEST).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String fname = (String) messageSnapshot.child("fname").getValue();
                    String lname = (String) messageSnapshot.child("lname").getValue();
                    String imgUrl = (String) messageSnapshot.child("imgUrl").getValue();
                    String dob = (String) messageSnapshot.child("dob").getValue();
                    String email = (String) messageSnapshot.child("email").getValue();
                    String gender = (String) messageSnapshot.child("gender").getValue();
                    String u_Id = (String) messageSnapshot.child("u_Id").getValue();
                    AppLogs.loge("Name " + fname + " lname " + lname);
                    AppLogs.loge("imgUrl " + imgUrl + " dob " + dob);
                    AppLogs.loge("gender " + gender + " Uid " + u_Id);
                    AppLogs.loge("Email " + email);
                    nameList.add(new User(fname,
                            lname,
                            email,
                            "",
                            dob,
                            gender,
                            u_Id,
                            imgUrl));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        firebase.child("User").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                userCall = new User(u.getFname(), u.getLname(), u.getEmail(), u.getPassword(), u.getDob(), u.getGender(), u.getU_Id(), u.getImgUrl());
                AppLogs.loge(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final User user = nameList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Click Add to add " + user.getFname() + " in your Friend List ");
                builder.setTitle("Friend Request Confirmation");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebase.child(NodeRef.Friends_Node).child(mAuth.getCurrentUser().getUid()).child(user.getU_Id()).setValue(user);
                        firebase.child(NodeRef.Friends_Node).child(user.getU_Id()).child(mAuth.getCurrentUser().getUid()).setValue(userCall);
                        AppLogs.loge("REF " + firebase.child(NodeRef.FRIEND_REQUEST).child(mAuth.getCurrentUser().getUid()));


                        firebase.child(NodeRef.FRIEND_REQUEST).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i = 0;
                                for (DataSnapshot msg : dataSnapshot.getChildren()) {
                                    if (i == position) {
                                        DatabaseReference ref = msg.getRef();
                                        AppLogs.loge(ref.toString());

                                        ref.removeValue();
                                        nameList.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }
                                    i++;
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                builder.create().show();
            }
        });


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FriendListActivity.class);
                startActivity(i);

            }
        });

        return view;
    }



}
