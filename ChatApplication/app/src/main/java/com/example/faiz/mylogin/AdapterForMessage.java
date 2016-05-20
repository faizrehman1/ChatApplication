package com.example.faiz.mylogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterForMessage extends BaseAdapter implements ListAdapter {

    private ArrayList<Message> messages;
    private Context context;
    Firebase firebase = new Firebase("https://chatapplicationn.firebaseio.com/");

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

        View view1 = inflater.inflate(R.layout.message_left_side_layout, null);
        View view2 = inflater.inflate(R.layout.message_right_side_layout, null);

        TextView msgViewLeft = (TextView) view1.findViewById(R.id.message_view_leftSide);
        TextView timeViewLeft = (TextView) view1.findViewById(R.id.timeView_leftSide_messages);

        TextView msgViewRight = (TextView) view1.findViewById(R.id.messageView_rightSide);
        TextView timeViewRight = (TextView) view1.findViewById(R.id.timeView_RightSide_messages);


        if (messages.get(position).getU_id().equals(firebase.getAuth().getUid())) {

            msgViewRight.setText(messages.get(position).getMsg());
            timeViewRight.setText(messages.get(position).getTime());
            return view2;
        } else {
            msgViewLeft.setText(messages.get(position).getMsg());
            timeViewLeft.setText(messages.get(position).getTime());
            return view1;
        }

    }
}
