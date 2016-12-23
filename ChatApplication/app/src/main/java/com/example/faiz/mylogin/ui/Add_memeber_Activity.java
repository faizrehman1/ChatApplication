package com.example.faiz.mylogin.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.AddGroupmemberAdapter;
import com.example.faiz.mylogin.adaptor.FriendList_adapter;
import com.example.faiz.mylogin.model.Group_Detail;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.example.faiz.mylogin.util.NodeRef;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Add_memeber_Activity extends AppCompatActivity {

    private ListView listView;
    private AddGroupmemberAdapter adapter;
    private ArrayList<User> arrayList;
    private FirebaseAuth mAuth;
    private DatabaseReference firebase;
    private User userCall;
    private String userName;
    private String grpname,grpAdminName,groupUrl;
    private String key;
    private User users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_view);
        firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        grpname = getIntent().getStringExtra("groupName");
        grpAdminName = getIntent().getStringExtra("groupAdminName");
        groupUrl = getIntent().getStringExtra("groupUrl");
        AppLogs.logd("grpName " + grpname);


        arrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.memberList);

        adapter = new AddGroupmemberAdapter(this, arrayList);

        final DatabaseReference user = firebase.child("MyGroup").getRef();
        userName = user.getKey();
        AppLogs.logd("group: " + userName);

           main();
//        membersfromFirebase();

//




    }

    public void addfriend(){
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_memeber_Activity.this);
                builder.setTitle("You want to Add this Friend.?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebase.child("MyGroup").child(arrayList.get(position).getU_Id()).child(grpname).setValue(new Group_Detail(grpname, grpAdminName, groupUrl));
                        arrayList.remove(position);
                        adapter.notifyDataSetChanged();

                    }
                });
                builder.setPositiveButton("Back",null);
                builder.create().show();

            }
        });
    }

    public void main(){


        AppLogs.logd("Aloo "+key);
try {
    firebase.child(NodeRef.FRIENDS).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (final DataSnapshot data : dataSnapshot.getChildren()) {

                AppLogs.logd("LOLOA "+data.getKey());
                key = data.getKey();
                firebase.child("MyGroup").child(data.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        AppLogs.logd("as"+dataSnapshot.getValue());
                        if(dataSnapshot.hasChild(grpname)){

                        }else {
                             users = data.getValue(User.class);
                       //     AppLogs.loge("USer in COntact " + dataSnapshot.getValue().toString());
                            arrayList.add(new User(users.getFname(),
                                    users.getLname(),
                                    users.getEmail(),
                                    "",
                                    users.getDob(),
                                    users.getGender(),
                                    users.getU_Id(),
                                    users.getImgUrl(),users.getStatus()));
                            adapter.notifyDataSetChanged();
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


}catch (Exception ex){
    ex.printStackTrace();
}

    addfriend();
    }
}





