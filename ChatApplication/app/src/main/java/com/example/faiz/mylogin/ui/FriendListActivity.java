package com.example.faiz.mylogin.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class FriendListActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference firebase;
    private ArrayList<User> nameList;
    private FriendList_adapter adapter;
    private ListView listView;
    private String userName;
    private User userCall;
    private String myId;
    private ArrayList<User> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firend_list);
        firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        friendsList = new ArrayList<>();
        myId = mAuth.getCurrentUser().getUid();

        nameList = new ArrayList<>();
        adapter = new FriendList_adapter(FriendListActivity.this, nameList);

        listView = (ListView) findViewById(R.id.fRequest_ListView1);

        firebase.child(NodeRef.Friends_Node).child(myId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    AppLogs.loge(data.getValue().toString());

                    friendsList.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference user = firebase.child("User").child(mAuth.getCurrentUser().getUid()).getRef();
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
                User u = dataSnapshot.getValue(User.class);
                userCall = new User(u.getFname(), u.getLname(), u.getEmail(), u.getPassword(), u.getDob(), u.getGender(), u.getU_Id(), u.getImgUrl());
                AppLogs.loge(dataSnapshot.getValue().toString());
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        membersfromFirebase();


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendListActivity.this);
                final User userFromList = nameList.get(position);
                builder.setMessage("Want to add " + userFromList.getFname().toString());
                builder.setTitle("Adding Process!");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AppLogs.loge("Selected User UID " + userFromList.getU_Id());
                        AppLogs.loge("USER NAME " + nameList.get(position).getFname());
//                        AppLogs.loge("Unknown Name " + mAuth.getCurrentUser().getDisplayName());

                        firebase.child(NodeRef.FRIEND_REQUEST).child(userFromList.getU_Id()).child(mAuth.getCurrentUser().getUid()).setValue(userCall);


                        nameList.remove(position);

                        adapter.notifyDataSetChanged();
                    }
                });


                builder.create().show();
            }
        });

    }
    private boolean userAddKerunYaNahin(String firebaseUserKiId) {
        if (firebaseUserKiId.equals(myId)) {
            AppLogs.loge("Meri id hai ye list me add nahi kia...");
            return false;
        } else {
            for (User u : friendsList) {
                if (u.getU_Id().equals(firebaseUserKiId)) {

                    AppLogs.loge("Friend ki id hai ye add nahi kia...");
                    return false;
                }
            }
        }
        return true;
    }

    private void membersfromFirebase() {
        firebase.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    User users = data.getValue(User.class);
                    AppLogs.loge("User data me hai... : "+users.getFname());

                    if (userAddKerunYaNahin(users.getU_Id())) {
                        nameList.add(users);
                        AppLogs.loge("User list me add ho gaya... : "+users.getFname());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
