package com.example.faiz.mylogin.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.ContactListAdapter;
import com.example.faiz.mylogin.model.Group_Detail;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.example.faiz.mylogin.util.NodeRef;
import com.example.faiz.mylogin.util.SharedPref;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class Create_Group extends AppCompatActivity {

    private static final int Browse_image = 1;
    private String selectedImagePath;
    private Bitmap bitmap;
    private String url_image;
    private FirebaseAuth mAuth;
    private FirebaseUser firebase_user;
    private ArrayList<User> arrayList;
    private ContactListAdapter adapter;
    private ArrayList<Group_Detail> groupdetail;
    private EditText grpName;
    private ImageView imageView;
    private Button button;
    private ListView list;
    private String grp;
    private User user = new User();
    private DatabaseReference firebase;
    private File temp_path;
    private final int COMPRESS = 100;
    boolean imageFlag = true;
    private ProgressDialog progressDialog;
    private StorageReference rootStorageRef, folderRef,imageRef;

    private  Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);


        grpName = (EditText) findViewById(R.id.groupName);
        imageView = (ImageView) findViewById(R.id.grpPic);
        button = (Button) findViewById(R.id.upload_image);
        list = (ListView) findViewById(R.id.contact_group);

        groupdetail = new ArrayList<>();
        arrayList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        firebase = FirebaseDatabase.getInstance().getReference();

        firebase_user = mAuth.getCurrentUser();

        rootStorageRef = FirebaseStorage.getInstance().getReference();
        folderRef = rootStorageRef.child("groupImages");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Browse_image);
            }
        });


        adapter = new ContactListAdapter(this, arrayList);


        firebase.child(NodeRef.FRIENDS_REFERENCE).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User users = data.getValue(User.class);
                    AppLogs.loge("USer in COntact " + dataSnapshot.getValue().toString());
                    arrayList.add(new User(users.getFname(),
                            users.getLname(),
                            users.getEmail(),
                            "",
                            users.getDob(),
                            users.getGender(),
                            users.getU_Id(),
                            users.getImgUrl(), users.getStatus()));
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try {
                    grp = grpName.getText().toString();
                    if (imageFlag) {
                        Toast.makeText(Create_Group.this, "Please upload picutre And then Add friends", Toast.LENGTH_SHORT).show();

                    } else if (grp.length() == 0) {
                        grpName.setError("This field is Required");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Create_Group.this);
                        builder.setTitle("Add Contact in Group");
                        builder.setMessage("You want to Add this Contact ?");
                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                firebase.child("MyGroup").child(arrayList.get(position).getU_Id()).child(grp).setValue(new Group_Detail(grp, SharedPref.getCurrentUser(Create_Group.this).getFname(), url_image));
                                Toast.makeText(Create_Group.this, "Friend Add Successfuly", Toast.LENGTH_SHORT).show();
                                arrayList.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        builder.setPositiveButton("Back", null);
                        builder.create().show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

//        firebase.child("GroupInfo").child(grp).setValue(new Group_Detail(grp,user.getFname(),url_cloudinary));

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
            }
           else if (Build.VERSION.SDK_INT < 19) {
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

                //   image.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                //   encodeImage();
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
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("Upload Picture")
                    .setMessage("Are you sure you want to upload picture?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

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
                                final ProgressDialog mProgressDialog = ProgressDialog.show(Create_Group.this, "Sending Image", "loading...", true, false);
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                UploadTask uploadTask;
                                Uri file = Uri.fromFile(new File(selectedImagePath));
                                imageRef = folderRef.child(base + "" + String.valueOf(date) + "." + extension);
                                uploadTask = imageRef.putFile(file);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        Toast.makeText(Create_Group.this, "UPLOAD FAILD", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                        url_image = taskSnapshot.getDownloadUrl().toString();
                                        Log.e("Image ka URL", "" + url_image);


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
        imageView.setImageBitmap(bitmap);
        imageFlag = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        try {
            if (id == R.id.action_done) {

                if (grp.length() == 0) {
                    grpName.setError("This field is Required");
                } else if (imageFlag) {
                    Toast.makeText(Create_Group.this, "Please upload picutre And then Add friends", Toast.LENGTH_LONG).show();
                } else {

                    firebase.child("MyGroup").child(firebase_user.getUid()).child(grp).setValue(new Group_Detail(grp, SharedPref.getCurrentUser(Create_Group.this).getFname(), url_image));
                    firebase.child("Groupinfo").child(grp).setValue(new Group_Detail(grp, SharedPref.getCurrentUser(Create_Group.this).getFname(), url_image));
                    Toast.makeText(this, "Group Created...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Create_Group.this, Navigation_Activity.class);
                    startActivity(intent);
                    finish();
                    //   getSupportFragmentManager().beginTransaction().add(R.id.main,new Group_Fragment()).addToBackStack(null).commit();
                    return true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_signin, menu);

        return true;
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