package com.example.faiz.mylogin;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Moosa moosa.bh@gmail.com on 5/23/2016 23 May.
 * Everything is possible in programming.
 */
public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        /**
         * Remove this line from all activities
         * */
    }

}
