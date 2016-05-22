package com.example.faiz.mylogin.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.Message;
import com.firebase.client.Firebase;

import java.util.ArrayList;

public class AdapterForMessage extends BaseAdapter implements ListAdapter {

    Firebase firebase = new Firebase("https://chatapplicationn.firebaseio.com/");
    private ArrayList<Message> messages;
    private Context context;

    public AdapterForMessage(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (messages.get(position).getU_id().equals(firebase.getAuth().getUid())) {

            view = inflater.inflate(R.layout.message_left_side_layout, null);
        } else {
            view = inflater.inflate(R.layout.message_right_side_layout, null);
        }
        TextView msgView = (TextView) view.findViewById(R.id.messageView);
        TextView timeView = (TextView) view.findViewById(R.id.timeView_messages);
        msgView.setText(messages.get(position).getMsg());
        timeView.setText(messages.get(position).getTime());
        return view;
    }
}
