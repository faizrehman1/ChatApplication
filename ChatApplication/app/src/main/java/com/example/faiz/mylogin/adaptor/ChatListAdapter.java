package com.example.faiz.mylogin.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.ui.Conversation_Activity;
import com.example.faiz.mylogin.util.AppLogs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//setting Adapter class for the COntact list View in Fragment
public class ChatListAdapter extends BaseAdapter implements ListAdapter {
    public static final String UUID_KEY = "data_uudsdfgasdg";
    private ImageView imgView_dialog;
    private Context context;
    private ArrayList<User> usersList;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private LayoutInflater inflater;

    public ChatListAdapter(Context context, ArrayList<User> message) {
        this.context = context;
        this.usersList = message;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Object getItem(int position) {
        return usersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Setting Inflator or new layout for list View in Contacts:

//
//        View view = inflater.inflate(R.layout.contact_viewadapter, null);

        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.contact_viewadapter,parent,false);
            viewHolder = new ViewHolder();
           viewHolder.imgStatus= (ImageView) convertView.findViewById(R.id.imagestatus);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.name_View_ContacList);
            viewHolder.imgView = (CircleImageView) convertView.findViewById(R.id.image_View_ContacList);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        User msg = usersList.get(position);
        AppLogs.logd("Tag value "+usersList.get(position).getFname());
        viewHolder.nameView.setText(msg.getFname());
        viewHolder.imgStatus.setVisibility(View.GONE);
        // imgView.setImageDrawable(imgUrl);
        Log.d("Contact_adapter","User Name "+msg.getFname());

        Glide.with(context)
                .load(usersList.get(position).getImgUrl())
                .into(viewHolder.imgView);

        Log.d("status",msg.getStatus());

        //status Code


        viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater1 = LayoutInflater.from(context);
                final View view = inflater1.inflate(R.layout.contact_alertview_userprofile, null);

                TextView nameView = (TextView) view.findViewById(R.id.textViewName_alert_Name);
                TextView emailView = (TextView) view.findViewById(R.id.textView_alert_Email);
                TextView dobView = (TextView) view.findViewById(R.id.textView_alert_Dob);
                imgView_dialog = (ImageView) view.findViewById(R.id.imageView_userprofile);
                Button textButton = (Button) view.findViewById(R.id.button_Message_alertBox);
                Button cancelButton = (Button) view.findViewById(R.id.button_Cancel_alertBox);


                nameView.setText(usersList.get(position).getFname());
                emailView.setText(usersList.get(position).getEmail());
                dobView.setText(usersList.get(position).getDob());
                Glide.with(context)
                        .load(usersList.get(position).getImgUrl())
                        .into(imgView_dialog);
                textButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, Conversation_Activity.class);
                        i.putExtra(UUID_KEY, usersList.get(position));
                        context.startActivity(i);

                    }
                });
//                builder.setCancelable(false);
                cancelButton.setOnClickListener(null);
                // View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        builder.create().dismiss();
//                    }
//                });
                builder.setView(view);


                //     builder.setMessage("LOL");

                builder.create().show();
            }
        });



        return convertView;
    }

   private class ViewHolder{
       ImageView imgStatus;
       TextView nameView;
       CircleImageView imgView;
    }

}
