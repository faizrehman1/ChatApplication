package com.example.faiz.mylogin.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.GroupMessage;
import com.example.faiz.mylogin.model.Message;
import com.example.faiz.mylogin.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupMessageAdapter extends BaseAdapter {

    private ArrayList<Message> arrayList;
    private Context context;
  //  private ArrayList<Message> messages;

    User uid;
    Picasso picasso;
    String U_ID;
    RoundImage mRoundImage;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference firebase;


    public GroupMessageAdapter(ArrayList<Message> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        firebase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getInstance().getCurrentUser();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;


       if(arrayList.get(position).getU_id().equals(user.getUid())){
           view = inflater.inflate(R.layout.message_left_side_layout,null);
       }else{
           view= inflater.inflate(R.layout.message_right_side_layout,null);

       }

        TextView msgView = (TextView) view.findViewById(R.id.messageView);
        TextView timeView = (TextView) view.findViewById(R.id.timeView_messages);
        final ImageView img = (ImageView)view.findViewById(R.id.imageView_messages);
        msgView.setText(arrayList.get(position).getMsg());
        timeView.setText(arrayList.get(position).getTime());



        return view;
    }
}
