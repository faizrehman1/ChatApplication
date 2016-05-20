package com.example.faiz.mylogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Conversation_Activity extends AppCompatActivity {
    private EditText messageField;
    private Button sendButton;
    private Firebase firebase;
    private ListView messagesListView;
    private Button sendBtn;
    private AdapterForMessage adapter;
    private ArrayList<Message> messages;
    private String friendKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_);
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://chatapplicationn.firebaseio.com/");
        try {


            friendKey = getIntent().getStringExtra(ContactListAdapter.UUID_KEY);
        } catch (Exception ex) {
            Log.e("Error getting I values", "Error Msg:" + ex.getMessage());
        }
        messageField = (EditText) findViewById(R.id.editText_Conversation_message);
        sendButton = (Button) findViewById(R.id.button_Conversation_send);
        messagesListView = (ListView) findViewById(R.id.messagesListView);

        messages = new ArrayList<Message>();
        adapter = new AdapterForMessage(messages, Conversation_Activity.this);
        messagesListView.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());

                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

                final String var = dateFormat.format(date);

                Message msg1 = new Message(messageField.getText().toString(), firebase.getAuth().getUid(), var);


                //sending Object to Fire base at Conversation Node
                Firebase pushRef = firebase.child("Conversation").push();
                Log.d("pushRef", "Reference " + pushRef);
                String key = pushRef.getKey();
                Log.d("ref1", "Reference " + key);

                pushRef.push().setValue(msg1);

                messageField.setText("");
            }
        });
        firebase.child("User_Conversation").child(firebase.getAuth().getUid()).push().child(friendKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //  Message message = (Message) dataSnapshot.getChildren();
                if (dataSnapshot.getValue() == null) {
                    Firebase ref = firebase.child("Conversation").push();

                    String key = ref.getKey();
                    Log.d("ref2", "Reference " + key);

//                    firebase.child("User_Conversation").child(firebase.getAuth().getUid()).push().child(friendKey).setValue(key);

                } else {

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}
