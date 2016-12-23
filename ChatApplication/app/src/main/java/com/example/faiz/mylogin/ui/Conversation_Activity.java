package com.example.faiz.mylogin.ui;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.adaptor.AdapterForMessage;
import com.example.faiz.mylogin.adaptor.ContactListAdapter;
import com.example.faiz.mylogin.model.Message;
import com.example.faiz.mylogin.model.TempRefObj;
import com.example.faiz.mylogin.model.User;
import com.example.faiz.mylogin.util.AppLogs;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Conversation_Activity extends AppCompatActivity {
    private EditText messageField;
    private Button sendButton;
    private DatabaseReference firebase;
    private ListView messagesListView;
    private AdapterForMessage adapter;
    private ArrayList<Message> messages;
    private User friendData;
    private FirebaseAuth mAuth;
    private TempRefObj conversationData;
    private String conversationPushRef;
    private boolean isConversationOld = false;
    private FirebaseUser user;
    private boolean chooseFlag;
    private static final int SELECTED_PICTURE = 1;
    private static final int SELECT_DOC_DIALOG = 1;
    private String imgPath;
    private StorageReference rootStorageRef, imageRef, folderRef, fileStorageRef;
    private ProgressDialog mProgressDialog;
    private long fileLenght;
    private Date date;
    private SimpleDateFormat dateFormat;
    private String var;
    private RelativeLayout relative;
    private boolean flag = true;
    private  View view;
    Animator animator =
            null;
    private FloatingActionButton contact,location,gallery,audio,document,camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_);
        firebase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getInstance().getCurrentUser();
        date = new Date(System.currentTimeMillis());
        dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        var = dateFormat.format(date);
        view = (View)findViewById(R.id.cover);
        relative = (RelativeLayout)findViewById(R.id.attach_container);

        contact = (FloatingActionButton)relative.findViewById(R.id.contact_btn);
        location = (FloatingActionButton)relative.findViewById(R.id.locat_btn);
        gallery = (FloatingActionButton)relative.findViewById(R.id.gallery_btn);
        audio = (FloatingActionButton)relative.findViewById(R.id.aud_btn);
        document = (FloatingActionButton)relative.findViewById(R.id.doc_btn);
        camera = (FloatingActionButton)relative.findViewById(R.id.cam_btn);

        Log.d("TAG",user.getUid());
        try {
//            mAuth = firebase.getAuth();
            friendData = getIntent().getParcelableExtra(ContactListAdapter.UUID_KEY);
            Log.d("err",friendData.getU_Id());
        } catch (Exception ex) {
            Log.e("Error getting I values", "Error Msg:" + ex.getMessage());
        }
        messageField = (EditText) findViewById(R.id.editText_Conversation_message);
        sendButton = (Button) findViewById(R.id.button_Conversation_send);
        messagesListView = (ListView) findViewById(R.id.messagesListView);
        messages = new ArrayList<Message>();
        adapter = new AdapterForMessage(messages, Conversation_Activity.this, friendData);
        messagesListView.setAdapter(adapter);
        rootStorageRef = FirebaseStorage.getInstance().getReference();
        folderRef = rootStorageRef.child("chat");

        checkConversationNewOROLD();


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                flag = true;
            }
        });


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Conversation_Activity.this,"Contact",Toast.LENGTH_SHORT).show();
                relative.setVisibility(View.GONE);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           Toast.makeText(Conversation_Activity.this,"Location",Toast.LENGTH_SHORT).show();
                relative.setVisibility(View.GONE);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Conversation_Activity.this,"Gallery",Toast.LENGTH_SHORT).show();
                flag = true;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);
                relative.setVisibility(View.GONE);

            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Conversation_Activity.this,"Audio",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                flag = false;
                //  intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(intent, SELECT_DOC_DIALOG);
                relative.setVisibility(View.GONE);
            }
        });
        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Conversation_Activity.this,"Document",Toast.LENGTH_SHORT).show();
                relative.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (chooseFlag) {
                if (resultCode == Conversation_Activity.this.RESULT_OK && requestCode == SELECTED_PICTURE) {
                    Uri uri = data.getData();
                    String[] imgHolder = {MediaStore.Images.Media.DATA};
                    Cursor cursor = Conversation_Activity.this.getContentResolver().query(uri, imgHolder, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(imgHolder[0]);
                    imgPath = cursor.getString(columnIndex);
                    cursor.close();

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Conversation_Activity.this);
                    builder.setTitle("Want to Send Image or not ?");
                    LayoutInflater inflater = LayoutInflater.from(Conversation_Activity.this);
                    View view1 = inflater.inflate(R.layout.attach_alert_view, null);
                    ImageView alertImageView = (ImageView) view1.findViewById(R.id.imageView_Alert);

                        Glide.with(Conversation_Activity.this).load(uri).into(alertImageView);

                    builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            uploadImage(imgPath);
                        }
                    });
                    builder.setNeutralButton("Cancel", null);
                    builder.setView(view1);
                    builder.create().show();
                } else {
                    Toast.makeText(Conversation_Activity.this, "Nothing Selected !", Toast.LENGTH_LONG).show();
                }
            } else if (!chooseFlag) {
                if (resultCode == Conversation_Activity.this.RESULT_OK && requestCode == SELECT_DOC_DIALOG) {
                    final Uri uri = data.getData();
                    String filePath = null;
                    if (uri.getScheme().equals("content")) {
                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        try {
                            if (cursor != null && cursor.moveToFirst()) {
                                filePath = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                    if (filePath == null) {
                        filePath = uri.getPath();
                        int cut = filePath.lastIndexOf('/');
                        if (cut != -1) {
                            filePath = filePath.substring(cut + 1);
                        }
                    }
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Conversation_Activity.this);
                    builder.setTitle("Want to Send File or not ?");
                    final String finalFilePath = filePath;
                    builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            uploadDocOrFile(finalFilePath, uri);
                        }
                    });
                    builder.setNeutralButton("Cancel", null);
                    builder.create().show();
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void uploadDocOrFile(String filePath, Uri uri) {
        Date date = new Date(System.currentTimeMillis());
            File fileRef = new File(filePath);
            String filenew = fileRef.getName();
            Log.d("fileNewName", filenew);
            int dot = filenew.lastIndexOf('.');
            String base = (dot == -1) ? filenew : filenew.substring(0, dot);
            String extension = (dot == -1) ? "" : filenew.substring(dot + 1);
            final ProgressDialog uploadPDialoge = new ProgressDialog(Conversation_Activity.this);
            uploadPDialoge.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            uploadPDialoge.setTitle("Uploading");
            uploadPDialoge.setMessage("File Uploading Please wait !");
            uploadPDialoge.setIndeterminate(false);
            uploadPDialoge.setCancelable(false);
            uploadPDialoge.setMax(100);
            uploadPDialoge.show();
            fileLenght = fileRef.length();
            fileLenght = fileLenght / 1024;
            System.out.println("File Path : " + fileRef.getPath() + ", File size : " + fileLenght + " KB");
            Log.d("uridata", filePath);
            Log.d("uridataLastSegment", uri.getLastPathSegment());
            final long FIVE_MEGABYTE = 1024 * 1024 * 20;
            fileLenght = fileLenght * 1024;
            UploadTask uploadTask;

            if (fileLenght <= FIVE_MEGABYTE) {
                Uri file = Uri.fromFile(new File(filePath));
                Log.d("fileKaLastSeg", file.getLastPathSegment() + date);
                fileStorageRef = folderRef.child(base + "" + String.valueOf(date) + "." + extension);
                uploadTask = fileStorageRef.putFile(uri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        Log.d("DownloadURL", downloadUrl.toString());

                        Message m = new Message();
                        m.setMsg(downloadUrl);
                        m.setTime(var);
                        m.setU_id(user.getUid());
                        firebase.child("conversation").child(conversationPushRef).push().setValue(m);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = 0;
                        progress += (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        System.out.println("Upload is " + progress + "% done");
                        uploadPDialoge.setProgress((int) progress);
                        if (progress == 100) {
                            uploadPDialoge.dismiss();
                        }
                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("Upload is paused");
                    }
                });
            } else {
                Toast.makeText(Conversation_Activity.this, "File size is too large !", Toast.LENGTH_LONG).show();
            }
    }

    private void uploadImage(String imgPath) {
        try {
            File fileRef = new File(imgPath);
            Date date = new Date(System.currentTimeMillis());
            String filenew = fileRef.getName();
            Log.d("fileNewName", filenew);
            int dot = filenew.lastIndexOf('.');
            String base = (dot == -1) ? filenew : filenew.substring(0, dot);
            String extension = (dot == -1) ? "" : filenew.substring(dot + 1);
            Log.d("extensionsss", extension);
            mProgressDialog = ProgressDialog.show(Conversation_Activity.this, "Sending Image", "loading...", true, false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            UploadTask uploadTask;
            Uri file = Uri.fromFile(new File(imgPath));
            imageRef = folderRef.child(base + "" + String.valueOf(date) + "." + extension);
            uploadTask = imageRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(Conversation_Activity.this, "UPLOAD FAILD", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                    Log.e("Image ka URL", "" + downloadUrl);
                    Message m = new Message();
                    m.setMsg(downloadUrl);
                    m.setTime(var);
                    m.setU_id(user.getUid());
                    firebase.child("conversation").child(conversationPushRef).push().setValue(m);
                    mProgressDialog.dismiss();
                    //  messageEditText.setText("");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void checkConversationNewOROLD() {
        firebase.child("user_conv").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        TempRefObj data = d.getValue(TempRefObj.class);
                        if (data.getUserId().equals(friendData.getU_Id())) {
                            Log.d(data.getUserId().toString(), friendData.getU_Id().toString() + " " + user.getUid());
                            conversationData = data;
                            isConversationOld = true;
                            conversationPushRef = data.getConversationId();
                        }
                    }

                } catch (Exception ec) {
                    ec.printStackTrace();
                } finally {
                    getConvoDataOrCreateNew();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getConvoDataOrCreateNew() {
        if (isConversationOld) {
            getConversationData();
        } else {
            createNewConversation();
        }
    }

    private void createNewConversation() {
        DatabaseReference pushRef = firebase.child("conversation").push();
        conversationPushRef = pushRef.getKey();
        final TempRefObj tempRefObj = new TempRefObj(user.getUid(), conversationPushRef);
        Log.d("fid",friendData.getU_Id());
//        firebase.child("user_conv").child(friendData.getU_Id()).push().setValue(tempRefObj, new Firebase.CompletionListener() {
//            @Override
//            public void onComplete(FirebaseError firebaseError, Firebase f) {
//                if (firebaseError == null) {
//                    tempRefObj.setUserId(friendData.getU_Id());
//                    firebase.child("user_conv").child(user.getUid()).push().setValue(tempRefObj, new Firebase.CompletionListener() {
//                        @Override
//                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                            if (firebaseError == null) {
//                                getConversationData();
//                            }
//                        }
//                    });
//                }
//            }
  //      });

        firebase.child("user_conv").child(friendData.getU_Id()).push().setValue(tempRefObj, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    tempRefObj.setUserId(friendData.getU_Id());
                    firebase.child("user_conv").child(user.getUid()).push().setValue(tempRefObj, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                getConversationData();
                            }
                        }
                    });
                }
            }
        });
    }

    private void getConversationData() {
        firebase.child("conversation").child(conversationPushRef).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Message message = d.getValue(Message.class);
                    messages.add(message);
                    adapter.notifyDataSetChanged();
                    Log.d("User Message Data", "Msg:" + message.getMsg() + "\nUUID:" + message.getU_id() + "\nTime:" + message.getTime());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error Getting Data", "No Message Data Available");
            }

        });
        setButtonClick();
    }

    private void setButtonClick() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (messageField.getText().length() > 1) {
                    Message m = new Message();
                    m.setMsg(messageField.getText().toString());
                    m.setTime(var);
                    m.setU_id(user.getUid());
                    firebase.child("conversation").child(conversationPushRef).push().setValue(m);
                    Log.d("Message Send Button", "Clicked");
                    messageField.setText("");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attach_menu, menu);
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
        if(id == R.id.action_attach && flag){

            showMenu();

        }else {
            hideMenu();
        }


        return super.onOptionsItemSelected(item);
    }
    public void showMenu(){
        int cx = (relative.getLeft() + relative.getRight());
        int cy = relative.getTop();
        int radius = Math.max(relative.getWidth(), relative.getHeight());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(relative, cx, cy, 0, radius);
        }
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(400);
        relative.setVisibility(View.VISIBLE);

        view.setVisibility(View.VISIBLE);
        animator.start();

        flag = false;
    }

    public void hideMenu(){
        int cx = (relative.getLeft() + relative.getRight());
        int cy = relative.getTop();
        int radius = relative.getWidth();
        // int radius = Math.max(relative.getWidth(), relative.getHeight());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(relative, cx, cy,radius, 0);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                relative.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                flag = true;

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

    }
}

