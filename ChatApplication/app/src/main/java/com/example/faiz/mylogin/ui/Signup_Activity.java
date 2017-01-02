package com.example.faiz.mylogin.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by faizrehman on 11/17/16.
 */

public class Signup_Activity extends AppCompatActivity {

    private static final int Browse_image = 1;

    private EditText pass, id, password, fname, lname, dob,confirmPass;
    private static final String[] Gender = new String[]{
            "Male", "Female"
    };
    private AutoCompleteTextView gender;
     private CircleImageView btn_upload_image;
    private Bitmap bitmap;
    private String selectedImagePath;
    private Uri selectedImage;
    private Button signup;
    private StorageReference rootStorageRef, folderRef, imageRef;
    private String url_ProfileImage="";
    private File temp_path;
    private final int COMPRESS = 100;
    private FirebaseAuth mAuth;
    private FirebaseUser firebase_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_view);

        rootStorageRef = FirebaseStorage.getInstance().getReference();
        folderRef = rootStorageRef.child("profileImages");
        mAuth = FirebaseAuth.getInstance();
        firebase_user = mAuth.getCurrentUser();

        final DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

        id = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);
        signup = (Button)findViewById(R.id.buttonSignUp);
        confirmPass = (EditText)findViewById(R.id.editText_confirmPassword);
        fname = (EditText) findViewById(R.id.editText_fname);
        lname = (EditText) findViewById(R.id.editText_lastName);
        dob = (EditText) findViewById(R.id.editText_dob);

        gender = (AutoCompleteTextView) findViewById(R.id.editGender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Signup_Activity.this, android.R.layout.simple_dropdown_item_1line, Gender);
        gender.setAdapter(adapter);

        btn_upload_image = (CircleImageView) findViewById(R.id.profilepic);

        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Browse_image);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                String confrim_passwordd = confirmPass.getText().toString();
                //Checking the length of pasword while registering new USER;
                if (pass.length() <= 6) {
                    main(pass);
                }else if(!pass.matches(confrim_passwordd)){
                    Toast.makeText(Signup_Activity.this,"Something Wrong in Password",Toast.LENGTH_SHORT).show();
                    confirmPass.setError("Pass not Confirm");
                }else if(( fname.getText().toString().equals("")
                        || lname.getText().toString().equals("")
                        || id.getText().toString().equals("")
                        || pass.equals("")
                        || gender.getText().toString().equals("")
                        || dob.getText().toString().equals("")
                        || confrim_passwordd.equals("")) ){
                    Toast.makeText(Signup_Activity.this,"Fields Should not be left Empty",Toast.LENGTH_SHORT).show();

                }
              else if(id.getText().length()==0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(id.getText().toString()).matches() ){
                    id.setError("Enter Valid Email Address");
                }
              else if(fname.getText().length()== 0 || !fname.getText().toString().matches("[a-zA-Z ]+")){
                    fname.setError("Invalid Name");
                }
              else if(lname.getText().length() == 0 || !lname.getText().toString().matches("[a-zA-Z ]+")){
                    lname.setError("Invalid Name");
                }
                else {
                    if(url_ProfileImage.equals("")){
                        url_ProfileImage="https://www.soundstream.tv/assets/default_profile-e08597880fc222202f22984a4f1966a29b108e856a3fb935072bfbbc302a4b73.png";
                    }
                    try {
                        mAuth.createUserWithEmailAndPassword((id.getText().toString()), (password.getText().toString())).addOnCompleteListener(Signup_Activity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                      ProgressDialog  progressDialog = ProgressDialog.show(Signup_Activity.this, "Signing Up", "Connecting...", true, false);

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
                                                url_ProfileImage
                                                , "true"));

                                        Toast.makeText(Signup_Activity.this, "Successfull", Toast.LENGTH_SHORT).show();
                                        AppLogs.logd("createUserWithEmail:onComplete: " + task.isSuccessful());
                                        Intent intent = new Intent(Signup_Activity.this,MainActivity.class);
                                        startActivity(intent);
                                        progressDialog.dismiss();
//                                                } else
                                        if (!task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Signup_Activity.this, " " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
            }
            });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
                selectedImage = data.getData();

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setData(selectedImage);
                intent.putExtra("crop", true);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 96);
                intent.putExtra("outputY", 96);
                intent.putExtra("noFaceDetection", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 2);
            } else if (Build.VERSION.SDK_INT < 19) {
                selectedImage = data.getData();
                // System.out.println("selectedImage "+selectedImage);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                selectedImagePath = cursor.getString(columnIndex);
                cursor.close();
                System.out.println("smallImagePath" + selectedImagePath);
                Log.d("Tag", selectedImagePath);
            } else {
                try {
                    InputStream imInputStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap bitmap = BitmapFactory.decodeStream(imInputStream);
                    selectedImagePath = saveGalaryImageOnLitkat(bitmap);

                    Log.d("Tag", selectedImagePath);
                    startUpload(selectedImagePath);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // finishAndSetResult(RESULT_OK, picturePath, false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void startUpload(String path) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(path, o);
            new android.support.v7.app.AlertDialog.Builder(Signup_Activity.this)
                    .setTitle("Upload Picture")
                    .setMessage("Are you sure you want to upload picture?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int which) {
                            //     image.setImageBitmap(bitmap);
                            Log.d("File PATH IS ", selectedImagePath + "");
                            try {
                                File fileRef = new File(selectedImagePath);
                                Date date = new Date(System.currentTimeMillis());
                                String filenew = fileRef.getName();
                                Log.d("fileNewName", filenew);
                                int dot = filenew.lastIndexOf('.');
                                String base = (dot == -1) ? filenew : filenew.substring(0, dot);
                                String extension = (dot == -1) ? "" : filenew.substring(dot + 1);
                                Log.d("extensionsss", extension);
                                final ProgressDialog mProgressDialog = ProgressDialog.show(Signup_Activity.this, "Sending Image", "loading...", true, false);
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                UploadTask uploadTask;
                                Uri file = Uri.fromFile(new File(selectedImagePath));
                                imageRef = folderRef.child(base + "" + String.valueOf(date) + "." + extension);
                                uploadTask = imageRef.putFile(file);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        Toast.makeText(Signup_Activity.this, "UPLOAD FAILD", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                        url_ProfileImage = taskSnapshot.getDownloadUrl().toString();
                                        Log.e("Image ka URL", "" + url_ProfileImage);
                                        mProgressDialog.dismiss();
                                        //  messageEditText.setText("");
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
                cacheDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
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
    private void main(String pass) {

        Toast.makeText(Signup_Activity.this, pass + "\nYou Password is no longer Stronger \nPlease signup Again with \natleast 7 Character of Pasword.\nThanks ", Toast.LENGTH_SHORT).show();

    }

}
