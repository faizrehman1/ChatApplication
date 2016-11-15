package com.example.faiz.mylogin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Moosa moosa.bh@gmail.com on 5/23/2016 23 May.
 * Everything is possible in programming.
 */
public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
      //  Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //when anyone install this app it shows in analytics at Fb.developer page
        AppEventsLogger.activateApp(this);
        /**
         * Remove this line from all activities
         * */
     //   printHashKaey();

    }

//    private void printHashKaey() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.example.faiz.mylogin",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//    }

}
