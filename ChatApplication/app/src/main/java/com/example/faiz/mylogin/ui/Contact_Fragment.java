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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("uuid",user.getUid());


        //Getting Single value from fire base and setting it to list View
        firebase.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data:dataSnapshot.getChildren()) {
                    User users = data.getValue(User.class);

                    Log.d("idss", users.getU_Id());

                    if (users.getU_Id().equals(user.getUid())) {
                        Log.d("LOL", users.getU_Id());
                    } else {
                        String image = users.getImgUrl();
                        nameList.add(new User(users.getFname(),
                                users.getLname(),
                                users.getEmail(),
                                users.getPassword(),
                                users.getDob(),
                                users.getGender(),
                                users.getU_Id(),
                                image));
                        adapter.notifyDataSetChanged();


                    }
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


