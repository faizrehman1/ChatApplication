package com.example.faiz.mylogin.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.ContactListAdapter;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.example.faiz.mylogin.util.NodeRef;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat_Fragment extends android.support.v4.app.Fragment {
    public static final String UUID_KEY = "data_uudsdfgasdg";
    private ListView listView;
    private ArrayList<User> nameList;
    private ContactListAdapter adapter;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chatview, null);


        final DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        listView = (ListView) v.findViewById(R.id.chat_ListView);
        nameList = new ArrayList<>();
        adapter = new ContactListAdapter(getActivity(), nameList);


        final FirebaseUser user = mAuth.getCurrentUser();
        Log.e("Chat_uuid", user.getUid());


        firebase.child(NodeRef.Friends_Node).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User users = data.getValue(User.class);
                    AppLogs.loge("USer in CHAT " + dataSnapshot.getValue().toString());
                    nameList.add(new User(users.getFname(),
                            users.getLname(),
                            users.getEmail(),
                            "",
                            users.getDob(),
                            users.getGender(),
                            users.getU_Id(),
                            users.getImgUrl()));
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

//                Log.d("dpname",user.getDisplayName());
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final User user = nameList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Friend !!!");
                builder.setMessage("Want to Delete " + user.getFname() + " from your Contacts ?");
                builder.setNegativeButton("Message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), Conversation_Activity.class);
                        i.putExtra(UUID_KEY, nameList.get(position));
                        startActivity(i);
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppLogs.loge("USername_ChatFragment " + user.getU_Id().toString());
                        DatabaseReference ref = firebase.child(NodeRef.Friends_Node).child(mAuth.getCurrentUser().getUid()).getRef();


                        firebase.child(NodeRef.Friends_Node).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i = 0;
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    if (i == position) {
                                        DatabaseReference ref1 = data.getRef();

                                        AppLogs.loge("UserREF_ChatFragment " + ref1.toString());

                                        ref1.removeValue();
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
                        firebase.child(NodeRef.Friends_Node).child(user.getU_Id()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String uid = mAuth.getCurrentUser().getUid().toString();
                                int i = 0;
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    User user = data.getValue(User.class);
                                    if (user.getU_Id().equals(uid)) {
                                        DatabaseReference refLoginUser = data.getRef();
                                        AppLogs.loge("refLoginUser_chatFragment " + refLoginUser);
                                        refLoginUser.removeValue();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
//                builder.setCancelable(false);

                builder.create().show();

            }
        });

        return v;
    }
}
