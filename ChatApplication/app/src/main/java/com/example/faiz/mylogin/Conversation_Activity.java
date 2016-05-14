package com.example.faiz.mylogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Conversation_Activity extends AppCompatActivity {
    private EditText messageField;
    private Button sendButton;

    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatMessagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_);

        messageField = (EditText) findViewById(R.id.editText_Conversation_message);
        sendButton = (Button) findViewById(R.id.button_Conversation_send);

    }
}
