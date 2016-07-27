package com.example.faiz.mylogin.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.faiz.mylogin.model.User;

/**
 * Created by Kamran ALi on 5/30/2016.
 */
public class SharedPref {


    private static String NAME = "packageName";
    private static String U_FNAME = "fname";
    private static String U_LNAME = "lanme";
    private static String U_ID = "userid";
    private static String U_EMAIL = "email";
    private static String U_IMG_URL = "iamgurl";
    private static String U_DOB = "u_dob";
    private static String U_GEND = "u_gea";
    private static String U_STATUS = "u_status";

    public static void setCurrentUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(U_FNAME, user.getFname()).apply();
        preferences.edit().putString(U_LNAME, user.getLname()).apply();
        preferences.edit().putString(U_DOB, user.getDob()).apply();
        preferences.edit().putString(U_IMG_URL, user.getImgUrl()).apply();
        preferences.edit().putString(U_ID, user.getU_Id()).apply();
        preferences.edit().putString(U_EMAIL, user.getEmail()).apply();
        preferences.edit().putString(U_GEND, user.getGender()).apply();
        preferences.edit().putString(U_STATUS,user.getStatus()).apply();
    }

    public static User getCurrentUser(Context context) {
        User user = new User();
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        user.setFname(preferences.getString(U_FNAME, ""));
        user.setLname(preferences.getString(U_LNAME, ""));
        user.setEmail(preferences.getString(U_EMAIL, ""));
        user.setU_Id(preferences.getString(U_ID, ""));
        user.setDob(preferences.getString(U_DOB, ""));
        user.setGender(preferences.getString(U_GEND, ""));
        user.setImgUrl(preferences.getString(U_IMG_URL, ""));
        user.setStatus(preferences.getString(U_STATUS, ""));
        return user;
    }


}
