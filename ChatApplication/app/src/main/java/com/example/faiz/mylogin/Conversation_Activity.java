package com.example.faiz.mylogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class Conversation_Activity extends AppCompatActivity {
    private EditText messageField;
    private Button sendButton;
    private Firebase firebase;
    private ListView messagesListView;
    private Button sendBtn;
    private CustomAdapterForMessaging adapter;
    private ArrayList<Messaging> messages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_);
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://chatapplicationn.firebaseio.com/");

        messageField = (EditText) findViewById(R.id.editText_Conversation_message);
        sendButton = (Button) findViewById(R.id.button_Conversation_send);
        messagesListView = (ListView) findViewById(R.id.messagesListView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageField.getText().toString();
/* is line me error hy deakh ly smj nhi arhi me soo rha hn*/
                firebase.child("Conversation").push().push().setValue(messageField);
                messages = new ArrayList<Messaging>();
                adapter = new CustomAdapterForMessaging(messages, Conversation_Activity.this);
                firebase.child("Conversation").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Messaging message = (Messaging) dataSnapshot.getValue();
                            message.getMsg();
                            message.getTime();
                            message.getU_id();
                            messages.add(new Messaging(message.getMsg().toString(), null, message.getTime()));

                            messagesListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

    }
}
