package com.example.faiz.mylogin.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kamran ALi on 5/13/2016.
 */
//setting Adapter class for the COntact list View in Fragment
public class ContactListAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private ArrayList<User> userData;
    public static final String UUID_KEY = "data_uudsdfgasdg";
    private Picasso picasso;
    ImageView imgView_dialog;

    public ContactListAdapter(Context context, ArrayList<User> message) {
        this.context = context;
        this.userData = message;
    }

    @Override
    public int getCount() {
        return userData.size();
    }

    @Override
    public Object getItem(int position) {
        return userData.get(position);
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

        TextView nameView = (TextView) view.findViewById(R.id.name_View_ContacList);
        ImageView imgView = (ImageView) view.findViewById(R.id.image_View_ContacList);

        User msg = userData.get(position);
        String imgUrl = msg.getImgUrl();


        String fname = msg.getFname().toString();
        nameView.setText(fname);
        // imgView.setImageDrawable(imgUrl);


        picasso.with(context)
                .load(userData.get(position).getImgUrl())
                .transform(new RoundImage())
                .into(imgView);
        // imgView.setImageResource(R.drawable.ic_menu_camera);

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


                nameView.setText(userData.get(position).getFname());
                emailView.setText(userData.get(position).getEmail());
                dobView.setText(userData.get(position).getDob());
                picasso.with(context)
                        .load(userData.get(position).getImgUrl())
                        .transform(new RoundImage())
                        .into(imgView_dialog);
                textButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, Conversation_Activity.class);
                        i.putExtra(UUID_KEY, userData.get(position));
                        context.startActivity(i);

                    }
                });
//                builder.setCancelable(false);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.create().dismiss();
                    }
                });
                builder.setView(view);


                //     builder.setMessage("LOL");

                builder.create().show();
            }
        });

        return view;
    }

}
