package com.example.faiz.mylogin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.AdapterForMessage;
import com.example.faiz.mylogin.adaptor.ContactListAdapter;
import com.example.faiz.mylogin.model.Message;
import com.example.faiz.mylogin.model.TempRefObj;
import com.example.faiz.mylogin.model.User;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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

public class Conversation_Activity extends AppCompatActivity {
    private EditText messageField;
    private Button sendButton;
    private DatabaseReference firebase;
    private ListView messagesListView;
    private Button sendBtn;
    private AdapterForMessage adapter;
    private ArrayList<Message> messages;
    private User friendData;
    private FirebaseAuth mAuth
            ;
    private TempRefObj conversationData;
    private String conversationPushRef;
    private boolean isConversationOld = false;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_);
        firebase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getInstance().getCurrentUser();
        try {
//            mAuth = firebase.getAuth();
            friendData = getIntent().getParcelableExtra(ContactListAdapter.UUID_KEY);
        } catch (Exception ex) {
            Log.e("Error getting I values", "Error Msg:" + ex.getMessage());
        }
        messageField = (EditText) findViewById(R.id.editText_Conversation_message);
        sendButton = (Button) findViewById(R.id.button_Conversation_send);
        messagesListView = (ListView) findViewById(R.id.messagesListView);

        messages = new ArrayList<Message>();
        adapter = new AdapterForMessage(messages, Conversation_Activity.this, friendData);
        messagesListView.setAdapter(adapter);


        checkConversationNewOROLD();

    }

    private void checkConversationNewOROLD() {
        firebase.child("user_conv").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        TempRefObj data = d.getValue(TempRefObj.class);
                        if (data.getUserId().equals(friendData.getU_Id())) {
                            Log.d(data.getUserId().toString(), friendData.getU_Id().toString() + " " + user.getUid());
                            conversationData = data;
                            isConversationOld = true;
                            conversationPushRef = data.getConversationId();
                        }
                    }

                } catch (Exception ec) {
                    ec.printStackTrace();
                } finally {
                    getConvoDataOrCreateNew();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    private void getConvoDataOrCreateNew() {
        if (isConversationOld) {
            getConversationData();
        } else {
            createNewConversation();
        }
    }

    private void createNewConversation() {
        DatabaseReference pushRef = firebase.child("conversation").push();
        conversationPushRef = pushRef.getKey();
        final TempRefObj tempRefObj = new TempRefObj(user.getUid(), conversationPushRef);
        firebase.child("user_conv").child(friendData.getU_Id()).push().setValue(tempRefObj, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase f) {
                if (firebaseError == null) {
                    tempRefObj.setUserId(friendData.getU_Id());
                    firebase.child("user_conv").child(user.getUid()).push().setValue(tempRefObj, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError == null) {
                                getConversationData();
                            }
                        }
                    });
                }
            }
        });

    }

    private void getConversationData() {
        firebase.child("conversation").child(conversationPushRef).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Message message = d.getValue(Message.class);
                    messages.add(message);
                    adapter.notifyDataSetChanged();
                    Log.d("User Message Data", "Msg:" + message.getMsg() + "\nUUID:" + message.getU_id() + "\nTime:" + message.getTime());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error Getting Data", "No Message Data Available");
            }

        });
        setButtonClick();
    }

    private void setButtonClick() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());

                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

                final String var = dateFormat.format(date);
                if (messageField.getText().length() > 1) {
                    Message m = new Message();
                    m.setMsg(messageField.getText().toString());
                    m.setTime(var);
                    m.setU_id(user.getUid());
                    firebase.child("conversation").child(conversationPushRef).push().setValue(m);
                    Log.d("Message Send Button", "Clicked");
                    messageField.setText("");
                }
            }
        });
    }
}

