package com.example.faiz.mylogin.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.User;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText email, pass, id, password, fname, lname, dob, gender, img_Url;
    Firebase firebase;
    Button buttonSignup, buttonSignin;
    RadioButton radioButtonMale, radioButtonFemale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        email = (EditText) findViewById(R.id.edt);
        pass = (EditText) findViewById(R.id.edt1);
        buttonSignup = (Button) findViewById(R.id.but);
        buttonSignin = (Button) findViewById(R.id.but1);


        firebase = new Firebase("https://chatapplicationn.firebaseio.com/");

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

                                firebase.child("User").child(stringObjectMap.get("uid").toString()).setValue(new User(fname.getText().toString(), lname.getText().toString(), id.getText().toString(), password.getText().toString(), dob.getText().toString(), gender.getText().toString(), stringObjectMap.get("uid").toString(), img_Url.getText().toString()));

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

//        firebase.child(firebase.getAuth().getUid()).setValue(new EmailAndPass(fname.getText().toString(),lname.getText().toString(),id.getText().toString(),password.getText().toString()));
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
