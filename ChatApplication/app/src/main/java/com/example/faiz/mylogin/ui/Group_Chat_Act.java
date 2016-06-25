package com.example.faiz.mylogin.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.GroupMessageAdapter;
import com.example.faiz.mylogin.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Group_Chat_Act extends AppCompatActivity {

    private String getName,grpAdminName,grpImgUrl;
    private EditText edtmessage;
    private Button btnsend;
    private ListView listView;
    private FirebaseUser user;
    private DatabaseReference firebase;
    private FirebaseAuth mAuth;
    private GroupMessageAdapter adapter;
    private ArrayList<Message> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__chat_);
        firebase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getInstance().getCurrentUser();
        arrayList = new ArrayList<>();
        getName = getIntent().getStringExtra("value");
        grpAdminName = getIntent().getStringExtra("adminvalue");
      //  intent.putExtra("imagevalue",arraylist.get(position).getImgUrl());
        grpImgUrl = getIntent().getStringExtra("imagevalue");

        edtmessage = (EditText)findViewById(R.id.group_Conversation_message);
        btnsend = (Button)findViewById(R.id.groupButton_Conversation_send);
        listView = (ListView)findViewById(R.id.GroupmessagesListView);


        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

        final String var = dateFormat.format(date);
        DatabaseReference ref = firebase.child(getName).child("Conversation");
        String reff = ref.getKey();
        Log.d("TaGG",reff);

        try {
            firebase.child("GroupData").child(getName).child("Conversation").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    adapter = new GroupMessageAdapter(arrayList,Group_Chat_Act.this);
                    listView.setAdapter(adapter);
                     arrayList.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Message message = d.getValue(Message.class);
                        arrayList.add(new Message(message.getMsg(),message.getU_id(),message.getTime()));
                        adapter.notifyDataSetChanged();
                        Log.d("User Message Data", "Msg:" + message.getMsg() + "\nUUID:" + message.getU_id() + "\nTime:" + message.getTime());
                    }


                    // Log.d("data", msg.getMsg());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }

       // firebase.child("Group")

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.setMsg(edtmessage.getText().toString());
                msg.setTime(var);
                msg.setU_id(user.getUid());
                firebase.child("GroupData").child(getName).child("Conversation").push().setValue(msg);
                edtmessage.setText("");

            }
        });









    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_createGroup) {
            Intent intent = new Intent(this,Create_Group.class);
            startActivity(intent);
            finish();
            return true;
        }
      else  if (id == R.id.action_addmember) {

            Intent intent = new Intent(Group_Chat_Act.this,Add_memeber_Activity.class);
            intent.putExtra("groupName",getName);
            intent.putExtra("groupAdminName",grpAdminName);
            intent.putExtra("groupUrl",grpImgUrl);
            startActivity(intent);


            return true;
        }
        else  if (id == R.id.action_leaveGroup) {

        //  Group_Fragment group_fragment = new Group_Fragment();
        //    group_fragment.leavegroup();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);

        return true;

    }
}
