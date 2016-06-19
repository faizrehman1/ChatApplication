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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firend_list);
        firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        nameList = new ArrayList<>();
        adapter = new FriendList_adapter(FriendListActivity.this, nameList);

        listView = (ListView) findViewById(R.id.fRequest_ListView1);

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
                   /*     HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("name", user.getFname());
                        map.put("picUrl", user.getImgUrl());
                        String currentUid = mAuth.getCurrentUser().getUid();
                        FirebaseUser currentUser =
                                mAuth.getCurrentUser();
AppLogs.loge("Current User "+currentUser.getDisplayName());

*/
                        AppLogs.loge("Selected User UID " + userFromList.getU_Id());
                        AppLogs.loge("USER NAME " + nameList.get(position).getFname());
//                        AppLogs.loge("Unknown Name " + mAuth.getCurrentUser().getDisplayName());

                        firebase.child(NodeRef.FRIEND_REQUEST).child(userFromList.getU_Id()).child(mAuth.getCurrentUser().getUid()).setValue(userCall   );
//                        DatabaseReference friendRequest = firebase.child("FriendRequest").child(currentUid).getRef();
//                        friendRequest.removeValue();
                        nameList.remove(position);

                        adapter.notifyDataSetChanged();
                    }
                });


                builder.create().show();
            }
        });

    }


    private void membersfromFirebase() {
        firebase.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User users = data.getValue(User.class);
                    if ((users.getU_Id()).equals(mAuth.getCurrentUser().getUid())) {

                    } else {
                        userName = users.getFname();
                        String imgUrl = users.getImgUrl();
                        nameList.add(new User(users.getFname(),
                                users.getLname(),
                                users.getEmail(),
                                users.getPassword(),
                                users.getDob(),
                                users.getGender(),
                                users.getU_Id(),
                                imgUrl));
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
