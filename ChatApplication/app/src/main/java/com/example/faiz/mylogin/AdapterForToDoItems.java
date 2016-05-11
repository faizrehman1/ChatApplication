package com.example.faiz.mylogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class AdapterForToDoItems extends BaseAdapter {

    private ArrayList<ToDo_Objects> arrayList;
    private Context context;
    private Signin signin;


    public AdapterForToDoItems(ArrayList<ToDo_Objects> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        signin=(Signin)context;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public ToDo_Objects getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dataview, null);

        TextView text = (TextView) view.findViewById(R.id.textViewtitle);
        TextView text1 = (TextView) view.findViewById(R.id.textViewDiscrip);
        final CheckBox checkbox = (CheckBox) view.findViewById(R.id.listCheckbox);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Checked tick = signin;
                tick.Check(position,checkbox.isChecked());
            }
        });


        checkbox.setChecked(arrayList.get(position).isCheck());
        text.setText(arrayList.get(position).getTitle());
        text1.setText(arrayList.get(position).getDiscription());


        return view;
    }

    interface Checked{
        void Check(int pos,boolean tick);
    }

}
