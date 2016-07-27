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

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.ui.Conversation_Activity;
import com.example.faiz.mylogin.ui.RoundImage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

//setting Adapter class for the COntact list View in Fragment
public class ContactListAdapter extends BaseAdapter implements ListAdapter {
    public static final String UUID_KEY = "data_uudsdfgasdg";
    private ImageView imgView_dialog;
    private Context context;
    private ArrayList<User> usersList;
    private Picasso picasso;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    public ContactListAdapter(Context context, ArrayList<User> message) {
        this.context = context;
        this.usersList = message;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        //Setting Inflator or new layout for list View in Contacts:
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.contact_viewadapter, null);

        final ImageView imgStatus= (ImageView) view.findViewById(R.id.imagestatus);
        imgStatus.setVisibility(View.INVISIBLE);
        TextView nameView = (TextView) view.findViewById(R.id.name_View_ContacList);
        ImageView imgView = (ImageView) view.findViewById(R.id.image_View_ContacList);

        User msg = usersList.get(position);

        String imgUrl = msg.getImgUrl();


        String fname = msg.getFname().toString();
        nameView.setText(fname);
        // imgView.setImageDrawable(imgUrl);
        Log.d("Contact_adapter","User Name "+fname.toString());

        picasso.with(context)
                .load(usersList.get(position).getImgUrl())
                .transform(new RoundImage())
                .into(imgView);

        Log.d("status",msg.getStatus());

        if(usersList.get(position).getStatus().equals("true")){

            imgStatus.setVisibility(View.VISIBLE);
        }

        // imgView.setImageResource(R.drawable.ic_menu_camera);


        //status Code

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();






        imgView.setOnClickListener(new View.OnClickListener() {
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
                picasso.with(context)
                        .load(usersList.get(position).getImgUrl())
                        .transform(new RoundImage())
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



        return view;
    }

}
