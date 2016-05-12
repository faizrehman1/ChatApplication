package com.example.faiz.mylogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        //Setting Inflator or new layout for list View in Contacts:
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.contact_viewadapter, null);

        TextView nameView = (TextView) view.findViewById(R.id.name_View_ContacList);
        ImageView imgView = (ImageView) view.findViewById(R.id.image_View_ContacList);

        EmailAndPass msg = message.get(position);
        String imgUrl = msg.getImgUrl();

        String fname = msg.getFname().toString();
        nameView.setText(fname);
//        imgView.setImageDrawable(R.drawable.);


        return view;
    }
}
