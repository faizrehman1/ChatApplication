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
import android.os.Environment;
import android.provider.MediaStore;
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

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.ContactListAdapter;
import com.example.faiz.mylogin.model.Group_Detail;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.example.faiz.mylogin.util.NodeRef;
import com.example.faiz.mylogin.util.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Create_Group extends AppCompatActivity {

    private static final int Browse_image = 1;
    private String selectedImagePath;
    private Bitmap bitmap;
    private String url_cloudinary;
    private Cloudinary cloudinary;
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
    boolean imageFlag =true;
    private ProgressDialog progressDialog;


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

        Map config = new HashMap();
        config.put("cloud_name", "fkcs14");
        config.put("api_key", "527495965545816");
        config.put("api_secret", "RI0k_mpmGjDa0TVkZABkVQwutf0");
        cloudinary = new Cloudinary(config);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Browse_image);
            }
        });


        adapter = new ContactListAdapter(this, arrayList);

//        firebase.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    User users = data.getValue(User.class);
//
//                    Log.d("idss", users.getU_Id());
//
//                    if (users.getU_Id().equals(firebase_user.getUid())) {
//                        Log.d("LOL", users.getU_Id());
//                    } else {
//                        String image = users.getImgUrl();
//                        arrayList.add(new User(users.getFname(),
//                                users.getLname(),
//                                users.getEmail(),
//                                users.getPassword(),
//                                users.getDob(),
//                                users.getGender(),
//                                users.getU_Id(),
//                                image));
//                        adapter.notifyDataSetChanged();
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                AppLogs.loge("database error" + databaseError.getMessage());
//            }
//        });

        firebase.child(NodeRef.Friends_Node).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            users.getImgUrl()));
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
                        Toast.makeText(Create_Group.this, "Please upload picutre And then Add friends",Toast.LENGTH_SHORT).show();

                    }
                   else if (grp.length() == 0) {
                        grpName.setError("This field is Required");
                    }  else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Create_Group.this);
                        builder.setTitle("Add Contact in Group");
                        builder.setMessage("You want to Add this Contact ?");
                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                firebase.child("MyGroup").child(arrayList.get(position).getU_Id()).child(grp).setValue(new Group_Detail(grp, SharedPref.getCurrentUser(Create_Group.this).getFname(), url_cloudinary));
                                Toast.makeText(Create_Group.this, "Friend Add Successfuly", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setPositiveButton("Back", null);
                        builder.create().show();
                    }
                }catch (Exception ex){
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
                    final ProgressDialog dialog = new ProgressDialog(Create_Group.this);
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setMessage("Uploading Image...");
                    dialog.setCancelable(true);
                    dialog.setMax(100);
                    dialog.setProgress(0);
                    dialog.show();

                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            while (dialog.getProgress() < dialog.getMax()) {
                                dialog.incrementProgressBy(1);
                                try {
                                    Thread.sleep(50);
                                } catch (Exception e) {/* no-op */}
                            }
                            dialog.dismiss();
                        }
                    });
                    t.start();

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
                        public void onClick(DialogInterface dialog, int which) {
                            imageView.setImageBitmap(bitmap);
                            Log.d("File PATH IS ", selectedImagePath + "");
                            AsyncTask<String, Void, HashMap<String, Object>> upload = new AsyncTask<String, Void, HashMap<String, Object>>() {
                                @Override
                                protected HashMap<String, Object> doInBackground(String... params) {
                                    File file = new File(selectedImagePath);
                                    HashMap<String, Object> responseFromServer = null;
                                    try {
                                        responseFromServer = (HashMap<String, Object>) cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                                    } catch (IOException e) {
                                        Toast.makeText(Create_Group.this, "Cannot Upload Image Please Try Again", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }

                                    return responseFromServer;
                                }

                                @Override
                                protected void onPostExecute(final HashMap<String, Object> stringObjectHashMap) {

                                    url_cloudinary = (String) stringObjectHashMap.get("url");
                                    Log.d("LAG", url_cloudinary);
                                    progressDialog.dismiss();
                                  //  textView_imageName.setText("Uploaded");

                                }
                                @Override
                                protected void onPreExecute() {
//                                    progressDialog = ProgressDialog.show(MainActivity.this, "Upload ", "Image Uploading...");
//                                    progressDialog.show();
                                    progressDialog = new ProgressDialog(Create_Group.this);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    progressDialog.setMessage("Uploading Image...");
                                    //   progressDialog.setCancelable(true);
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

                    firebase.child("MyGroup").child(firebase_user.getUid()).child(grp).setValue(new Group_Detail(grp, SharedPref.getCurrentUser(Create_Group.this).getFname(), url_cloudinary));
                    firebase.child("Groupinfo").child(grp).setValue(new Group_Detail(grp, SharedPref.getCurrentUser(Create_Group.this).getFname(), url_cloudinary));
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