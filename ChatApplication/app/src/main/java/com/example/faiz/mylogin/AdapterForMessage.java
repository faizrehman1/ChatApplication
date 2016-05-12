package com.example.faiz.mylogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterForMessage extends BaseAdapter {

    private ArrayList<Message> arrayList;
    private Context context;



    public AdapterForMessage(ArrayList<Message> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;

    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Message getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chatview, null);

        TextView msg = (TextView) view.findViewById(R.id.textViewtitle);
        TextView time = (TextView) view.findViewById(R.id.textViewDiscrip);
      //  TextView text1 = (TextView) view.findViewById(R.id.textViewDiscrip);
      //  final CheckBox checkbox = (CheckBox) view.findViewById(R.id.listCheckbox);




     //   checkbox.setChecked(arrayList.get(position).isCheck());
        msg.setText(arrayList.get(position).getMsg());
        time.setText(arrayList.get(position).getTime());


        return view;
    }



}
