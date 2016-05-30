package com.example.faiz.mylogin.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText pass;
    EditText id;
    EditText password;
    EditText fname;
    EditText lname;
    EditText dob;
    EditText gender;
    EditText img_Url;
    Firebase firebase;
    LoginManager fbLoginMan;
    Button buttonSignup, buttonSignin, buttonFb;
    String first_namefire, last_namefire, emailfire, birthdayfire, uuidfire, dpfire, genderfire;
    private CallbackManager callbackManager;
    private Profile profile;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbLoginMan = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        firebase = new Firebase("https://chatapplicationn.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();

        buttonFb = (Button) findViewById(R.id.button_SignIn_CustomFb);

        email = (EditText) findViewById(R.id.edt);
        pass = (EditText) findViewById(R.id.edt1);
        buttonSignup = (Button) findViewById(R.id.but);
        buttonSignin = (Button) findViewById(R.id.but1);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {

                    Log.d("TAG", "onAuthStateChanged:signed_in:" + currentUser.getUid());


                } else {
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }

            }
        };
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add");
                builder.setMessage("Add New Email or Password");
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View vv = inflater.inflate(R.layout.signup_view, null);
                id = (EditText) vv.findViewById(R.id.edtviewEmail);

                password = (EditText) vv.findViewById(R.id.edtviewPassword);
                fname = (EditText) vv.findViewById(R.id.edtviewFirstName);
                lname = (EditText) vv.findViewById(R.id.edtviewLastName);
                dob = (EditText) vv.findViewById(R.id.editTextDob);
                gender = (EditText) vv.findViewById(R.id.editGender);
                img_Url = (EditText) vv.findViewById(R.id.imag_URL);


                builder.setView(vv);
                builder.setPositiveButton("SIGN-UP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebase.createUser(id.getText().toString(), password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                            @Override
                            public void onSuccess(Map<String, Object> stringObjectMap) {

                                firebase.child("User").child(stringObjectMap.get("uid").toString()).setValue(new
                                        User(fname.getText().toString(),
                                        lname.getText().toString(),
                                        id.getText().toString(),
                                        password.getText().toString(),
                                        dob.getText().toString(),
                                        gender.getText().toString(),
                                        stringObjectMap.get("uid").toString(),
                                        img_Url.getText().toString()));

//                            Log.d("Data After Signup",""+stringObjectMap.get("uid"));
                                Toast.makeText(MainActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                switch (firebaseError.getCode()) {
                                    case FirebaseError.EMAIL_TAKEN:
                                        Toast.makeText(MainActivity.this, "Email Alreaddy Exists", Toast.LENGTH_SHORT).show();
                                        break;
                                    case FirebaseError.NETWORK_ERROR:
                                        Toast.makeText(MainActivity.this, "Network Issue", Toast.LENGTH_SHORT).show();
                                        break;

                                }
                            }
                        });
                    }

                });
                builder.setNegativeButton("Cancel", null);
                builder.show();

            }
        });

        buttonFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbLoginMan.logInWithReadPermissions(MainActivity.this, Arrays.asList("email",
                        "user_birthday", "public_profile"));

                fbLoginMan.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(MainActivity.this, "LoginSuccessfully Via Facebook", Toast.LENGTH_SHORT).show();

                        AccessToken accessToken = loginResult.getAccessToken();

                        Log.d("accessToken", " " + accessToken);

                        profile = Profile.getCurrentProfile();

                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.e("Graph Resp Json:", "" + object.toString());
                                    Log.e("Graph Resp Raw:", "" + response.getRawResponse());
                                    String token = AccessToken.getCurrentAccessToken().getToken();
                                    Log.e("Graph Token:", "" + token);

                                    first_namefire = object.getString("first_name");
                                    Log.e("FirstName", "" + first_namefire);

                                    last_namefire = object.getString("last_name");
                                    Log.e("LastName", "" + last_namefire);

                                    emailfire = object.getString("email");
                                    Log.e("Email", "" + emailfire);

                                    birthdayfire = object.getString("birthday");
                                    Log.e("birthday", "" + birthdayfire);

                                    genderfire = object.getString("gender");
                                    Log.e("gender :", "" + genderfire);

                                    uuidfire = object.getString("id");
                                    Log.e("id:", "" + uuidfire);

                                    dpfire = Profile.getCurrentProfile().getProfilePictureUri(400, 400).toString();
                                    Log.e("Graph Dp:", "" + dpfire);


                                    firebase.child("User").child(uuidfire).setValue(new
                                            User(first_namefire,
                                            last_namefire,
                                            emailfire,
                                            "",
                                            birthdayfire,
                                            genderfire,
                                            uuidfire,
                                            dpfire
                                    ));

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                        });
                        Bundle parameters = new Bundle();
                        String request_params = "id,name,gender,email,birthday";
                        String old_req_params = "id, first_name, last_name, email,gender, birthday, location";
                        parameters.putString("fields", old_req_params);
                        request.setParameters(parameters);
                        request.executeAsync();

                        /////////////////FB user data save and Sign in END/////////////////////
                        // user ka jo b data hai wo yaha per get ker k save kerwa k rakh do then jab user firevase se login ho jaye to
                        //tab wo sab data waha se auth.get id k ref per store kwerwa do

                        //yeh b karlia h

                       /* firebase.child("User").child(uuidfire).setValue(new
                                User(first_namefire,
                                last_namefire,
                                emailfire,
                                "",
                                birthdayfire,
                                genderfire,
                                uuidfire,
                                dpfire
                        ));*/

                        handleFacebookAccessToken(loginResult.getAccessToken());

                        if (profile != null) {
                            Intent intent = new Intent(MainActivity.this, Navigation_Activity.class);
                            startActivity(intent);
                            Log.e("Emailactive", "" + emailfire);

                        } else {

                        }

                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG", "facebook:onError " + error);
                    }
                });

            }
        });


        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebase.authWithPassword(email.getText().toString(), pass.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {

                        Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Navigation_Activity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {

                        switch (firebaseError.getCode()) {
                            case FirebaseError.INVALID_EMAIL:
                                Toast.makeText(MainActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                                break;
                            case FirebaseError.INVALID_PASSWORD:
                                Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                                break;
                            case FirebaseError.NETWORK_ERROR:
                                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                break;
                            case FirebaseError.AUTHENTICATION_PROVIDER_DISABLED:
                                Toast.makeText(MainActivity.this, "Authentication Provider Disabled", Toast.LENGTH_SHORT).show();
                                break;
                            case FirebaseError.USER_DOES_NOT_EXIST:
                                Toast.makeText(MainActivity.this, "User Does Not Exist", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }


                });
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        Log.d("TAG", "handleFacebookAccessToken: " + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());



                if (!task.isSuccessful()) {
                    Log.w("TAG", "signInWithCredential", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
