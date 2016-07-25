package com.example.faiz.mylogin.ui;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.example.faiz.mylogin.util.SharedPref;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/*import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;*/

public class MainActivity extends AppCompatActivity {


    private static final int Browse_image = 1;
    private LoginManager fbLoginMan;
    private CallbackManager callbackManager;
    private Profile profile;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private EditText email, pass, id, password, fname, lname, dob;
    private AutoCompleteTextView gender;
    //, gender;
    private Button buttonSignup, buttonSignin, btn_upload_image, buttonFb;
    private User user = new User();
    //ImageView image;
    private Cloudinary cloudinary;
    private String selectedImagePath;
    private Bitmap bitmap;
    private String url_cloudinary;
    private TextView textView_imageName,per;
    private FirebaseUser firebase_user;
    private File temp_path;
    private final int COMPRESS = 100;
     private static final String[] Gender = new String[] {
            "Male","Female"
    };

    ProgressDialog progressDialog;
    //wait jus see bc
    private boolean fbSignIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbLoginMan = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        final DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
//        final DatabaseReference firebase = database.getReference("https://chatapplicationn.firebaseio.com");


        mAuth = FirebaseAuth.getInstance();
        firebase_user = mAuth.getCurrentUser();
        buttonFb = (Button) findViewById(R.id.button_SignIn_CustomFb);
//        firebase=new Firebase("https://chatapplicationn.firebaseio.com");
//        Log.d("idd",mAuth.getCurrentUser().getUid());

        Map config = new HashMap();
        config.put("cloud_name", "fkcs14");
        config.put("api_key", "527495965545816");
        config.put("api_secret", "RI0k_mpmGjDa0TVkZABkVQwutf0");
        cloudinary = new Cloudinary(config);

        email = (EditText) findViewById(R.id.edt);
        pass = (EditText) findViewById(R.id.edt1);
        buttonSignup = (Button) findViewById(R.id.but);
        buttonSignin = (Button) findViewById(R.id.but1);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    currentUser.getUid();

                    if (fbSignIn) {
                        /**
                         * Face Book Auth
                         * */
                        user.setU_Id(currentUser.getUid());
                        user.setPassword("");
                        user.setImgUrl(currentUser.getPhotoUrl().toString());
                        AppLogs.logd("Auth State User ID:" + currentUser.getUid());
                        AppLogs.logd("Auth State User Email:" + currentUser.getEmail());
                        AppLogs.logd("Auth State User PhotoUrl:" + currentUser.getPhotoUrl());
                        AppLogs.logd("Auth State User Name:" + currentUser.getDisplayName());

                        firebase.child("User").child(currentUser.getUid()).setValue(user, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                AppLogs.logd("User Logged In For FB:" + user.getEmail());
                                SharedPref.setCurrentUser(MainActivity.this, user);
                                openNavigationActivity();
                            }
                        });

                    }
                    else  {
                        try {
                            firebase.child("User").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
//                                AppLogs.logd("User Logged In For My Auth:" + user.getEmail());

                                    SharedPref.setCurrentUser(MainActivity.this, user);
                                    openNavigationActivity();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    AppLogs.loge("Error Logged In MYAUTH");

                                }
                            });

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }

                } else {
                    AppLogs.loge("Auth Listener: User Not Signed In");
                }

            }
        };


        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
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

                    gender = (AutoCompleteTextView) vv.findViewById(R.id.editGender);

                    //  AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.actv_country);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, Gender);
                    gender.setAdapter(adapter);
                    btn_upload_image = (Button) vv.findViewById(R.id.BtnuploadImage);
                    textView_imageName = (TextView) vv.findViewById(R.id.image_Name);
                    // forImageUpload();



                    btn_upload_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Browse_image);
                        }
                    });
                    builder.setView(vv);




                    builder.setPositiveButton("SIGN-UP", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String pass = password.getText().toString();
                                if(pass.length() <= 6){
                                    main(pass);
                                }

                                else{


                                try {
                                    mAuth.createUserWithEmailAndPassword((id.getText().toString()), (password.getText().toString())).addOnCompleteListener(MainActivity.this,
                                            new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    String uid = mAuth.getCurrentUser().getUid();


//                                                if (task.isSuccessful()) {
                                                    firebase.child("User").child(uid).setValue(new
                                                            User(fname.getText().toString(),
                                                            lname.getText().toString(),
                                                            id.getText().toString(),
                                                            password.getText().toString(),
                                                            dob.getText().toString(),
                                                            gender.getText().toString(),
                                                            uid,
                                                            url_cloudinary));

                                                    Toast.makeText(MainActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                    AppLogs.logd("createUserWithEmail:onComplete:" + task.isSuccessful());
//                                                } else
                                                    if (!task.isSuccessful()) {

                                                        Toast.makeText(MainActivity.this, " " + task.getException(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } catch (Exception ex) {

                                    ex.printStackTrace();
                                }
                                }
                            }


                        });
                        builder.setNegativeButton("Cancel", null);

                    //    builder.setCancelable(false);

                    builder.create().show();



                    }catch(Exception ex){
                        ex.printStackTrace();
                    }


            }
        });

        buttonFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbSignIn = true;
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

                                    String first_namefire = object.getString("first_name");
                                    Log.e("FirstName", "" + first_namefire);
                                    user.setFname(first_namefire);
                                    String last_namefire = object.getString("last_name");
                                    Log.e("LastName", "" + last_namefire);
                                    user.setLname(last_namefire);
                                    String emailfire = object.getString("email");
                                    Log.e("Email", "" + emailfire);
                                    user.setEmail(emailfire);
                                    String birthdayfire = object.getString("birthday");
                                    Log.e("birthday", "" + birthdayfire);
                                    user.setDob(birthdayfire);
                                    String genderfire = object.getString("gender");
                                    Log.e("gender :", "" + genderfire);
                                    user.setGender(genderfire);
                                    String dpfire = Profile.getCurrentProfile().getProfilePictureUri(400, 400).toString();
                                    Log.e("Graph Dp:", "" + dpfire);
                                    user.setImgUrl(dpfire);


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


                        handleFacebookAccessToken(loginResult.getAccessToken());


                    }

                    @Override
                    public void onCancel() {
                        fbSignIn = false;
                        Log.d("TAG", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        fbSignIn = false;
                        Toast.makeText(MainActivity.this,""+error, Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "facebook:onError " + error);
                    }
                });

            }
        });


        buttonSignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fbSignIn = false;

                String emails = email.getText().toString();
                String passo = pass.getText().toString();


                if (emails.length() == 0) {
                    email.setError("This is Required Field");
                } else if (passo.length() == 0 && passo.length() <= 5) {
                    pass.setError("This is Required Field");
                }


                try {
                    mAuth.signInWithEmailAndPassword(emails, passo).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                AppLogs.logd("signInWithEmail:onComplete:" + task.isSuccessful());
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                openNavigationActivity();
                            } else if (!task.isSuccessful()) {
                                AppLogs.logw("signInWithEmail" + task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void main(String pass) {

        Toast.makeText(MainActivity.this,pass+"\n You Password is no longer Stronger",Toast.LENGTH_SHORT).show();

    }


    private void openNavigationActivity() {
        Intent intent = new Intent(this, Navigation_Activity.class);
        startActivity(intent);
        finish();
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        Log.d("TAG", "handleFacebookAccessToken: " + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "signInWithCredential", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Successfully Logged In.", Toast.LENGTH_LONG).show();

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == Browse_image) {
//                if (resultCode == RESULT_OK) {
//                    //   setDefaultLayout();
//                    Uri selectedImageUri = data.getData();
//
//
//                 //   Log.d("Uploading file from URI: %s", selectedImageUri.getPath());
//
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                    Cursor cursor = getContentResolver().query(
//                            selectedImageUri, filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String filePath = cursor.getString(columnIndex);
//                    cursor.close();
//                    Log.d("Upload file is:", filePath);
//                    selectedImagePath = filePath;
//                    textView_imageName.setText("Uploaded");
//                    startUpload(filePath);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
         try {
             if (Build.VERSION.SDK_INT < 19) {
                 Uri selectedImage = data.getData();
                 // System.out.println("selectedImage "+selectedImage);
                 String[] filePathColumn = {MediaStore.Images.Media.DATA};
                 Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                 cursor.moveToFirst();
                 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                 selectedImagePath = cursor.getString(columnIndex);
                 cursor.close();
                 System.out.println("smallImagePath" + selectedImagePath);
                 Log.d("Tag", selectedImagePath);

                 //   image.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                 //   encodeImage();
             } else {
                 try {
                     InputStream imInputStream = getContentResolver().openInputStream(data.getData());
                     Bitmap bitmap = BitmapFactory.decodeStream(imInputStream);
                     selectedImagePath = saveGalaryImageOnLitkat(bitmap);
                     //     image.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                     //  encodeImage();
//                     final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
//                     dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                     dialog.setMessage("Uploading Image...");
//                     dialog.setCancelable(true);
//                     dialog.setMax(100);
//                     dialog.setProgress(0);
//                     dialog.show();
//
//                     Thread t = new Thread(new Runnable() {
//                         public void run() {
//                             while (dialog.getProgress() < dialog.getMax()) {
//                                 dialog.incrementProgressBy(1);
//                                 try {
//                                     Thread.sleep(50);
//                                 } catch (Exception e) {/* no-op */}
//                             }
//                             dialog.dismiss();
//                         }
//                     });
//                     t.start();




                     Log.d("Tag", selectedImagePath);
                     startUpload(selectedImagePath);

                 } catch (FileNotFoundException e) {
                     e.printStackTrace();
                 }
                 // finishAndSetResult(RESULT_OK, picturePath, false);
             }
         }catch (Exception ex){
             ex.printStackTrace();
         }
    }



    public void startUpload(String path) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(path, o);
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("Upload Picture")
                    .setMessage("Are you sure you want to upload picture?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int which) {
                            //     image.setImageBitmap(bitmap);
                            Log.d("File PATH IS ", selectedImagePath + "");
                            AsyncTask<String, Void, HashMap<String, Object>> upload = new AsyncTask<String, Void, HashMap<String, Object>>() {
                                @Override
                                protected HashMap<String, Object> doInBackground(String... params) {
                                    File file = new File(selectedImagePath);
                                    HashMap<String, Object> responseFromServer = null;
                                    try {
                                        responseFromServer = (HashMap<String, Object>) cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

                                    } catch (IOException e) {
                                        Toast.makeText(MainActivity.this, "Cannot Upload Image Please Try Again", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }

                                    return responseFromServer;
                                }

                                @Override
                                protected void onPostExecute(final HashMap<String, Object> stringObjectHashMap) {

                                    url_cloudinary = (String) stringObjectHashMap.get("url");
                                    Log.d("LAG", url_cloudinary);
                                    progressDialog.dismiss();
                                    textView_imageName.setText("Uploaded");

                                }
                                @Override
                                protected void onPreExecute() {
//                                    progressDialog = ProgressDialog.show(MainActivity.this, "Upload ", "Image Uploading...");
//                                    progressDialog.show();
                                     progressDialog = new ProgressDialog(MainActivity.this);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    progressDialog.setMessage("Uploading Image...");
                                    progressDialog.setCancelable(false);
                                    progressDialog.setMax(100);
                                    progressDialog.setProgress(0);
                                    progressDialog.show();

                                    Thread t = new Thread(new Runnable() {
                                        public void run() {
                                            while (progressDialog.getProgress() < progressDialog.getMax()) {
                                                progressDialog.incrementProgressBy(1);
                                                try {
                                                    Thread.sleep(250);
                                                } catch (Exception e) {/* no-op */}
                                            }
                                             // dialog.dismiss();
                                        }
                                    });
                                    t.start();
//                                    AppLogs.logd("Hello");
                                }
                            };
                            upload.execute(selectedImagePath);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    }).show();
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    private String saveGalaryImageOnLitkat(Bitmap bitmap) {
        try {
            File cacheDir;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                cacheDir = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name));
            else
                cacheDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!cacheDir.exists())
                cacheDir.mkdirs();
            String filename = System.currentTimeMillis() + ".jpg";
            File file = new File(cacheDir, filename);
            temp_path = file.getAbsoluteFile();

            // if(!file.exists())
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS, out);
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
