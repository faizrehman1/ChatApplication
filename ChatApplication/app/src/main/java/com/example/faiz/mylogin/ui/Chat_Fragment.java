package com.example.faiz.mylogin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.ChatListAdapter;
import com.example.faiz.mylogin.model.TempRefObj;
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
    private ChatListAdapter adapter;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chatview, null);


        final DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        listView = (ListView) v.findViewById(R.id.chat_ListView);
        nameList = new ArrayList<>();
        adapter = new ChatListAdapter(getActivity(), nameList);


        final FirebaseUser user = mAuth.getCurrentUser();
        Log.e("Chat_uuid", user.getUid());
        try {

            firebase.child("user_conv").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    AppLogs.logd("Tag : " + dataSnapshot);
                    for (final DataSnapshot data : dataSnapshot.getChildren()) {
                        TempRefObj tempRefObj = data.getValue(TempRefObj.class);
                        AppLogs.logd("TT " + tempRefObj.getUserId());
                        firebase.child(NodeRef.FRIENDS_REFERENCE).child(mAuth.getCurrentUser().getUid()).child(tempRefObj.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                AppLogs.logd("dataa " + dataSnapshot1);
                                if (dataSnapshot.getValue() != null) {
                                    try {
                                        User users = dataSnapshot1.getValue(User.class);
                                        nameList.add(new User(users.getFname(),
                                                users.getLname(),
                                                users.getEmail(),
                                                "",
                                                users.getDob(),
                                                users.getGender(),
                                                users.getU_Id(),
                                                users.getImgUrl(), users.getStatus()));
                                        adapter.notifyDataSetChanged();


                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            listView.setAdapter(adapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final User user = nameList.get(position);
                Intent i = new Intent(getActivity(), Conversation_Activity.class);
                i.putExtra(UUID_KEY, nameList.get(position));
                startActivity(i);
            }
        });


        return v;
    }
}
