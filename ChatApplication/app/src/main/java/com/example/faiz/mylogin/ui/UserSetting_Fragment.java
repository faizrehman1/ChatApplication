package com.example.faiz.mylogin.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.util.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by faiz on 1/8/2017.
 */

public class UserSetting_Fragment extends android.support.v4.app.Fragment {

    private EditText email,oldPass,newPass,userName;
    private Button backBtn,saveChanges;
    private FirebaseAuth mAuth;
    private FirebaseUser firebase_user;
    private DatabaseReference firebase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_setting_fragment,container,false);
        super.onCreateView(inflater, container, savedInstanceState);

        firebase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        firebase_user = mAuth.getCurrentUser();

       email = (EditText)view.findViewById(R.id.setting_email);
        userName = (EditText)view.findViewById(R.id.setting_firstName);
        oldPass = (EditText)view.findViewById(R.id.setting_oldpass);
        newPass = (EditText)view.findViewById(R.id.setting_newpass);
        backBtn = (Button)view.findViewById(R.id.backsetting_btn);
        saveChanges = (Button)view.findViewById(R.id.save_btn);

        email.setText(firebase_user.getEmail());
        userName.setText(SharedPref.getCurrentUser(getActivity()).getFname());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity().getSupportFragmentManager().findFragmentById(R.id.container) != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().
                            remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.container)).commit();
                }
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Save Changes",Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }

}
