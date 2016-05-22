package com.example.faiz.mylogin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

<<<<<<< HEAD:ChatApplication/app/src/main/java/com/example/faiz/mylogin/Contact_Fragment.java
=======
import com.example.faiz.mylogin.adaptor.ContactListAdapter;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.User;
>>>>>>> 73e0e74441c604ca7ed215cc93193ddbae737cb5:ChatApplication/app/src/main/java/com/example/faiz/mylogin/ui/Contact_Fragment.java
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class Contact_Fragment extends android.support.v4.app.Fragment {
    private static final String UUID_KEY = "UUID";
    private ListView listView;
    private ArrayList<User> nameList;
    private ContactListAdapter adapter;
    private Firebase firebase;
    String name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contactview, null);
        super.onCreateView(inflater, container, savedInstanceState);
        Firebase.setAndroidContext(getActivity().getApplicationContext());

        firebase = new Firebase("https://chatapplicationn.firebaseio.com/");
        listView = (ListView) view.findViewById(R.id.contact_ListView);

        nameList = new ArrayList<>();
        adapter = new ContactListAdapter(getActivity(), nameList);

        //Getting Single value from fire base and setting it to list View
        firebase.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
<<<<<<< HEAD:ChatApplication/app/src/main/java/com/example/faiz/mylogin/Contact_Fragment.java
                    EmailAndPass msg = data.getValue(EmailAndPass.class);
                    // Log.d("msg:",msg.getU_Id() +" User Id:"+ firebase.getAuth().getUid());
                    if (msg.getU_Id().equals(firebase.getAuth().getUid())) {
                        //  Log.d("LOL", msg.getU_Id());

                    } else {
                        //  Log.d("LOL2","Hahaha");
                        String image = msg.getImgUrl();
                        nameList.add(new EmailAndPass(msg.getFname(), msg.getLname(), msg.getEmail(), msg.getPassword(), msg.getDob(), msg.getGender(), msg.getU_Id(), image));
                        adapter.notifyDataSetChanged();
                    }
=======
                  User msg = data.getValue(User.class);
                   // Log.d("msg:",msg.getU_Id() +" User Id:"+ firebase.getAuth().getUid());
                       if(msg.getU_Id().equals(firebase.getAuth().getUid())) {
                         //  Log.d("LOL", msg.getU_Id());

                       }else {
                         //  Log.d("LOL2","Hahaha");
                           String image = msg.getImgUrl();
                           nameList.add(new User(msg.getFname(), msg.getLname(), msg.getEmail(), msg.getPassword(), msg.getDob(), msg.getGender(), msg.getU_Id(), image));
                           adapter.notifyDataSetChanged();
                       }
                }
                  //  Log.d("LOL", msg.getU_Id() + " " + firebase.getAuth().getUid());
>>>>>>> 73e0e74441c604ca7ed215cc93193ddbae737cb5:ChatApplication/app/src/main/java/com/example/faiz/mylogin/ui/Contact_Fragment.java
                }
                //  Log.d("LOL", msg.getU_Id() + " " + firebase.getAuth().getUid());
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        listView.setAdapter(adapter);
        return view;

    }
}
