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
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Contact_Fragment extends android.support.v4.app.Fragment {
    private static final String UUID_KEY = "UUID";
    private ListView listView;
    private ArrayList<User> nameList;
    private ContactListAdapter adapter;
    private Firebase firebase;
    String name;
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;
    private AuthData meData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contactview, null);
        super.onCreateView(inflater, container, savedInstanceState);


        firebase = new Firebase("https://chatapplicationn.firebaseio.com/");

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference firebase = database.getReference("");
        listView = (ListView) view.findViewById(R.id.contact_ListView);
        meData = firebase.getAuth();
        nameList = new ArrayList<>();
        adapter = new ContactListAdapter(getActivity(), nameList);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       Log.d("id",meData.getUid());

        //Getting Single value from fire base and setting it to list View
        firebase.child("User").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    User msg = data.getValue(User.class);
                    Log.d("msg:", msg.getFname());
                    Log.d("idss",msg.getU_Id());

                         //   + " User Id:" + firebase.getAuth().getUid());
//                    if (msg.getU_Id().equals(firebase.getAuth().getUid().toString())) {
//                        //  Log.d("LOL", msg.getU_Id()); yahan error hai na ?
//                    } else {
//                        //  Log.d("LOL2","Hahaha");
//                        String image = msg.getImgUrl();
//                        nameList.add(new User(msg.getFname(),
//                                msg.getLname(),
//                                msg.getEmail(),
//                                msg.getPassword(),
//                                msg.getDob(),
//                                msg.getGender(),
//                                msg.getU_Id(),
//                                image));
//                        adapter.notifyDataSetChanged();
//                    }
                }
            }

            //  Log.d("LOL", msg.getU_Id() + " " + firebase.getAuth().getUid());


            //  Log.d("LOL", msg.getU_Id() + " " + firebase.getAuth().getUid());


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        listView.setAdapter(adapter);
        return view;

    }

}

