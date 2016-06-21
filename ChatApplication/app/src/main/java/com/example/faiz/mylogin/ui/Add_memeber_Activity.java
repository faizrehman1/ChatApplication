package com.example.faiz.mylogin.ui;

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
        AppLogs.logd("grpName " + grpname);

        grpAdminName = getIntent().getStringExtra("groupAdminName");
        groupUrl = getIntent().getStringExtra("groupUrl");

        arrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.memberList);

        adapter = new AddGroupmemberAdapter(this, arrayList);

        final DatabaseReference user = firebase.child("MyGroup").getRef();
        userName = user.getKey();
        AppLogs.logd("group: " + userName);


//        membersfromFirebase();


        firebase.child("MyGroup").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    AppLogs.logd("Tag " + dataSnapshot.getKey());
                    AppLogs.logd("Tag1 " + data.getKey());


                    if (data.hasChild(grpname)) {

                    } else {
                        key = data.getKey();
                        AppLogs.logd("Ley "+key);
                        main();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firebase.child("MyGroup").child(users.getU_Id()).setValue(new Group_Detail(grpname,grpAdminName,groupUrl));
            }
        });

    }
    public void main(){

        AppLogs.logd("Aloo"+key);
        try {
            firebase.child("User").child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

               //     AppLogs.logd(dataSnapshot);

//                    if (dataSnapshot.hasChild(grpname)){
//
//                   }else {
                     //   for (DataSnapshot data : dataSnapshot.getChildren()) {
                            users = dataSnapshot.getValue(User.class);
                            AppLogs.loge("USer in COntact " + dataSnapshot.getValue().toString());
                            arrayList.add(new User(users.getFname(),
                                    users.getLname(),
                                    users.getEmail(),
                                    "",
                                    users.getDob(),
                                    users.getGender(),
                                    users.getU_Id(),
                                    users.getImgUrl()));
                            adapter.notifyDataSetChanged();
                    //    }

//                    }
//'
 //                   }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception rx){
            rx.printStackTrace();
        }
    }
}





