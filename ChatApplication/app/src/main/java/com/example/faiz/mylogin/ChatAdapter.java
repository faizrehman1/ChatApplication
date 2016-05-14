package com.example.faiz.mylogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Kamran ALi on 5/15/2016.
 */
public class ChatAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private ArrayList<ChatMessage> messaage;

    @Override
    public int getCount() {
        return messaage.size();
    }

    @Override
    public Object getItem(int position) {
        return messaage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messaage.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        return null;
    }
}
