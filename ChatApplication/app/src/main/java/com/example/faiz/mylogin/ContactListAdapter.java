package com.example.faiz.mylogin;

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

import java.util.ArrayList;

/**
 * Created by Kamran ALi on 5/13/2016.
 */
//setting Adapter class for the COntact list View in Fragment
public class ContactListAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private ArrayList<EmailAndPass> message;

    public ContactListAdapter(Context context, ArrayList<EmailAndPass> message) {
        this.context = context;
        this.message = message;
    }

    @Override
    public int getCount() {
        return message.size();
    }

    @Override
    public Object getItem(int position) {
        return message.get(position);
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

        EmailAndPass msg = message.get(position);
        String imgUrl = msg.getImgUrl();


        String fname = msg.getFname().toString();
        nameView.setText(fname);
        // imgView.setImageDrawable(imgUrl);

        imgView.setImageResource(R.drawable.ic_menu_camera);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater1 = LayoutInflater.from(context);
                final View view = inflater1.inflate(R.layout.contact_alertview_userprofile, null);

                TextView nameView = (TextView) view.findViewById(R.id.textViewName_alert_Name);
                TextView emailView = (TextView) view.findViewById(R.id.textView_alert_Email);
                TextView dobView = (TextView) view.findViewById(R.id.textView_alert_Dob);
                ImageView imgView = (ImageView) view.findViewById(R.id.imageView_userprofile);
                Button textButton = (Button) view.findViewById(R.id.button_Text_alertBox);
                Button cancelButton = (Button) view.findViewById(R.id.button_Cancel_alertBox);


                nameView.setText(message.get(position).getFname());
                emailView.setText(message.get(position).getEmail());
                dobView.setText(message.get(position).getDob());
                imgView.setImageResource(R.drawable.ic_menu_camera);
                textButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, Conversation_Activity.class);
                        context.startActivity(i);

                    }
                });
//                builder.setCancelable(false);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


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
