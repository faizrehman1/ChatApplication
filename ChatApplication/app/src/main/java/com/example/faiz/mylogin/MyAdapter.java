package com.example.faiz.mylogin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private ArrayList<EmailAndPass> emailandpass;
    private Context context;

    public MyAdapter(ArrayList<EmailAndPass> emailandpass, Context context) {
        this.emailandpass = emailandpass;
        this.context = context;
    }

    @Override
    public int getCount() {
        return emailandpass.size();
    }

    @Override
    public EmailAndPass getItem(int position) {
        return emailandpass.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View vieww = layoutInflater.inflate(R.layout.dataview, null);

        TextView text = (TextView)vieww.findViewById(R.id.textViewtitle);
        TextView text1 = (TextView)vieww.findViewById(R.id.textViewDiscrip);
     //   TextView text2 = (TextView)vieww.findViewById(R.id.textViewfname);
    //    TextView text3 = (TextView)vieww.findViewById(R.id.textViewlname);

        text.setText(emailandpass.get(position).getEmail());
        text1.setText(emailandpass.get(position).getPassword());
       // text2.setText(emailandpass.get(position).getFname());
      //  text3.setText(emailandpass.get(position).getLname());




        return vieww;
    }
}
