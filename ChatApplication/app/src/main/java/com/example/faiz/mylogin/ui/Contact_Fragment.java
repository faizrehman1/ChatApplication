package com.example.faiz.mylogin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.ContactListAdapter;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Contact_Fragment extends android.support.v4.app.Fragment {
    private ListView listView;
    private ArrayList<User> nameList;
    private ContactListAdapter adapter;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contactview, null);
        super.onCreateView(inflater, container, savedInstanceState);


        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        listView = (ListView) view.findViewById(R.id.contact_ListView);
        nameList = new ArrayList<>();
        adapter = new ContactListAdapter(getActivity(), nameList);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("id", mAuth.getCurrentUser().getUid());

        //Getting Single value from fire base and setting it to list View
        firebase.child("User").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Log.d("user:", user.getFname());
                Log.d("idss", user.getU_Id());

                if (user.getU_Id().equals(mAuth.getCurrentUser().getUid().toString())) {
                    Log.d("LOL", user.getU_Id());
                } else {
                    String image = user.getImgUrl();
                    nameList.add(new User(user.getFname(),
                            user.getLname(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getDob(),
                            user.getGender(),
                            user.getU_Id(),
                            image));
                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AppLogs.loge("database error" + databaseError.getMessage());
            }

        });

        listView.setAdapter(adapter);
        return view;

    }
}


