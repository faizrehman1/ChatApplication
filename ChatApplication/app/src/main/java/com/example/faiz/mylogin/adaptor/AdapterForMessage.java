package com.example.faiz.mylogin.adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.Message;
import com.example.faiz.mylogin.model.TempRefObj;
import com.example.faiz.mylogin.model.User;

import com.firebase.client.DataSnapshot;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.realtime.util.StringListReader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterForMessage extends BaseAdapter implements ListAdapter {


    private ArrayList<Message> messages;
    private Context context;
    User uid;
    Picasso picasso;
    String U_ID;
    RoundImage mRoundImage;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference firebase;



    public AdapterForMessage(ArrayList<Message> messages, Context context,User uid) {
        this.messages = messages;
        this.context = context;
        this.uid = uid;
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
        firebase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getInstance().getCurrentUser();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view;


        if (messages.get(position).getU_id().equals(user.getUid())) {

            U_ID = user.getUid();
            view = inflater.inflate(R.layout.message_left_side_layout, null);

        } else {

            U_ID = uid.getU_Id();
            view = inflater.inflate(R.layout.message_right_side_layout, null);
            Log.d("TAGG",uid.getU_Id());

        }

        TextView msgView = (TextView) view.findViewById(R.id.messageView);
        TextView timeView = (TextView) view.findViewById(R.id.timeView_messages);
        final ImageView img = (ImageView)view.findViewById(R.id.imageView_messages);
        msgView.setText(messages.get(position).getMsg());
        timeView.setText(messages.get(position).getTime());
//for image in conversation Activity
       try {

          firebase.child("User").child(U_ID).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
              @Override
              public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                      User user = dataSnapshot.getValue(User.class);
//                 user.getImgUrl()
//                Log.d("imaaa",user.getImgUrl());

                 if(user.getImgUrl().equals("")){
                    img.setImageResource(R.drawable.ic_menu_camera);

                 }else{
                  picasso.with(context).load(user.getImgUrl()).transform(new RoundImage()).into(img);

              }
              }
              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
               //    Log.d("image",dataSnapshot.child("imgUrl").getValue().toString());
               //    picasso.with(context).load(dataSnapshot.child("imgUrl").getValue().toString()).transform(new RoundImage()).into(img);





       }catch(Exception ex){
           ex.printStackTrace();
       }
        return view;
    }

}
