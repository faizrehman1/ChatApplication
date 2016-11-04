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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.Tab_Adapter;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.example.faiz.mylogin.util.NodeRef;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Navigation_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Picasso picasso;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;

    private Chat_Fragment chatFragment;
    private Contact_Fragment contactFragment;
    private Group_Fragment groupFragment;
    private F_Request_Fragment f_requestfragment;
    private DatabaseReference firebase;
    private FirebaseAuth auth;
    private static int Browse_image = 1;
    private String selectedImagePath;
    private Bitmap bitmap;
    private String url_cloudinary;
    private File temp_path;
    private final int COMPRESS = 100;
    private ProgressDialog progressDialog;
    private Cloudinary cloudinary;
    private CircleImageView img;
    private TextView user_name, user_email;
    private String status = "true";
    private FirebaseUser users;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_);
        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Map config = new HashMap();
        config.put("cloud_name", "fkcs14");
        config.put("api_key", "527495965545816");
        config.put("api_secret", "RI0k_mpmGjDa0TVkZABkVQwutf0");
        cloudinary = new Cloudinary(config);



        firebase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();


        users = auth.getCurrentUser();


        Tab_ViewPager();


        LayoutInflater inflater = (LayoutInflater) Navigation_Activity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.nav_header_navigation_, null);


        img = (CircleImageView) view.findViewById(R.id.imageView_NavBar);
        user_name = (TextView) view.findViewById(R.id.user_name_NavBar);
        user_email = (TextView) view.findViewById(R.id.textView_email_NavBar);


        testing(status);

        firebase.child("User").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User use = dataSnapshot.getValue(User.class);
                Log.d("dataaName", dataSnapshot.getValue().toString());
                Log.d("name", use.getFname());

                user_name.setText(use.getFname() + " " + use.getLname());
                user_email.setText(use.getEmail());
                picasso.with(Navigation_Activity.this).load(use.getImgUrl())
                        //  .resize(width,height)
                        .transform(new RoundImage())
                        .into(img);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebase.child("User").child(auth.getCurrentUser().getUid()).child("status").setValue("true");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.addHeaderView(view);
        navigationView.setNavigationItemSelectedListener(this);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Navigation_Activity.this);
                builder.setTitle("Change Image");
                builder.setMessage("You want to Change your Picture");
                builder.setNegativeButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, Browse_image);
                    }
                });
                builder.setPositiveButton("Back", null);
                builder.create().show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_send) {

            status = "false";
            testing(status);

            auth.getInstance().signOut();
            Intent intent = new Intent(Navigation_Activity.this, MainActivity.class);
            startActivity(intent);

            finish();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Tab_ViewPager() {

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("FRequest"));

        chatFragment = new Chat_Fragment();
        contactFragment = new Contact_Fragment();
        groupFragment = new Group_Fragment();
        f_requestfragment = new F_Request_Fragment();


        fragments = new ArrayList<>();
        fragments.add(chatFragment);
        fragments.add(groupFragment);
        fragments.add(contactFragment);
        fragments.add(f_requestfragment);


        final Tab_Adapter adapter = new Tab_Adapter(getSupportFragmentManager(), fragments);
        //is line se tablayout k neche jo shade araaha hai woh change hoga pageviewer k mutabik
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(0);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        try {
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                 //   adapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(tab.getPosition());


                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


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

                //   image.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                //   encodeImage();
            } else {
                try {
                    InputStream imInputStream = getContentResolver().openInputStream(selectedImage);
                 //   Bitmap bitmap = data.getParcelableExtra("data");
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

        super.onActivityResult(requestCode, resultCode, data);
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
                            img.setImageBitmap(bitmap);
                            Log.d("File PATH IS ", selectedImagePath + "");
                            AsyncTask<String, Void, HashMap<String, Object>> upload = new AsyncTask<String, Void, HashMap<String, Object>>() {
                                @Override
                                protected HashMap<String, Object> doInBackground(String... params) {
                                    File file = new File(selectedImagePath);
                                    HashMap<String, Object> responseFromServer = null;
                                    try {
                                        responseFromServer = (HashMap<String, Object>) cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

                                    } catch (IOException e) {
                                        Toast.makeText(Navigation_Activity.this, "Cannot Upload Image Please Try Again", Toast.LENGTH_LONG).show();
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
                                    firebase.child("User").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // int i=0;
                                            //          for(DataSnapshot data:dataSnapshot.getChildren()){
                                            //   User user = data.getValue(User.class);

                                            DatabaseReference ref = dataSnapshot.getRef();
                                            AppLogs.logd(ref.toString());
                                            ref.child("imgUrl").setValue(url_cloudinary);
                                            Toast.makeText(Navigation_Activity.this, "You Have Successfully Change Picture..", Toast.LENGTH_LONG).show();

                                            //        }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    firebase.child(NodeRef.Friends_Node).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.d("tag", String.valueOf(dataSnapshot.hasChild(users.getUid())));
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                User user = data.getValue(User.class);
                                                if (data.hasChild(users.getUid())) {
                                                    DatabaseReference ref = data.getRef();
                                                    String keys = ref.getKey();
                                                    Log.d("tag1", ref.getKey());
                                                    firebase.child("Friends").child(keys).child(users.getUid()).child("imgUrl").setValue(url_cloudinary);


                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }

                                @Override
                                protected void onPreExecute() {
//                                    progressDialog = ProgressDialog.show(MainActivity.this, "Upload ", "Image Uploading...");
//                                    progressDialog.show();
                                    progressDialog = new ProgressDialog(Navigation_Activity.this);
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

    public void testing(final String status) {

        Log.d("tagg", users.getUid());

        firebase.child(NodeRef.Friends_Node).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("tag", String.valueOf(dataSnapshot.hasChild(users.getUid())));
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                  //  User user = data.getValue(User.class);
                    if (data.hasChild(users.getUid())) {
                        DatabaseReference ref = data.getRef();
                        String keys = ref.getKey();
                        Log.d("tag1", ref.getKey());
                        firebase.child("Friends").child(keys).child(users.getUid()).child("status").setValue(status);


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        firebase.child("User").child(auth.getCurrentUser().getUid()).child("status").setValue(status);

    }
}
