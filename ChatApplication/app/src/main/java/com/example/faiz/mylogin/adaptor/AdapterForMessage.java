package com.example.faiz.mylogin.adaptor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.Message;
import com.example.faiz.mylogin.model.TempRefObj;
import com.example.faiz.mylogin.model.User;

import com.firebase.client.DataSnapshot;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.realtime.util.StringListReader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterForMessage extends BaseAdapter implements ListAdapter {


    private ArrayList<Message> messages;
    private Context context;
    User uid;
    Picasso picasso;
    String U_ID;
    RoundImage mRoundImage;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference firebase;
    String fileExtenstion;
    ProgressDialog mProgressDialogforFileAndPic;


    public AdapterForMessage(ArrayList<Message> messages, Context context, User uid) {
        this.messages = messages;
        this.context = context;
        this.uid = uid;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        firebase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getInstance().getCurrentUser();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view;
        boolean flagURL;

        if (messages.get(position).getU_id().equals(user.getUid())) {

            U_ID = user.getUid();
            view = inflater.inflate(R.layout.message_left_side_layout, null);

        } else {

            U_ID = uid.getU_Id();
            view = inflater.inflate(R.layout.message_right_side_layout, null);
            Log.d("TAGG", uid.getU_Id());

        }

        final TextView msgView = (TextView) view.findViewById(R.id.messageView);
        TextView timeView = (TextView) view.findViewById(R.id.timeView_messages);
        final CircleImageView img = (CircleImageView) view.findViewById(R.id.imageView_messages);
        ImageView sendImagePic = (ImageView) view.findViewById(R.id.imagepic);
      //  ProgressBar prog = (ProgressBar)view.findViewById(R.id.progressBarpic);
        msgView.setText(messages.get(position).getMsg());
        timeView.setText(messages.get(position).getTime());
//for image in conversation Activity
        final String getMsg = messages.get(position).getMsg();
        String messageObject = getMsg;
        String url = messageObject;
        fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d("fileExtension", fileExtenstion);
        final String name = URLUtil.guessFileName(url, null, fileExtenstion);
        if (messages.get(position).getMsg().contains("https://")) {
            msgView.setVisibility(View.GONE);
            if (getMsg.contains(".jpg") || getMsg.contains(".png") || getMsg.contains(".jpeg") || getMsg.contains(".gif")) {
                flagURL = true;
            } else {
                flagURL = false;
            }

            getImageFromUrl(getMsg, sendImagePic, flagURL, name);

        }

        sendImagePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Drawable dr = ((ImageView) imageView).getDrawable();

                //        String tag = (String) imageView.getTag();
                final String fileURl = getMsg;
                Log.d("FileURL", fileURl);
                String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(fileURl);
                Log.d("FILEEXTENSION", fileExtenstion);
                int dot = fileURl.lastIndexOf('.');
                String base = (dot == -1) ? fileURl : fileURl.substring(0, dot);
                String extension = (dot == -1) ? "" : fileURl.substring(dot + 1);
                String finalextension = extension.substring(0, 3);
                Log.d("agar", extension);
                Log.d("NewExtension", finalextension);
                final String name = URLUtil.guessFileName(fileURl, null, fileExtenstion);
                Log.d("NameFile", "" + name);

                if (finalextension.equals("jpg") || finalextension.equals("png") || getMsg.contains(".jpeg") || getMsg.contains(".gif")) {
//                    imgArrayList = new ArrayList<>();
                    //  Bitmap bmp = ((GlideBitmapDrawable) dr.getCurrent()).getBitmap();

//                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Employee");
//                    if (!file.exists()){
//                        file.mkdir();
//                    }
                    final String outPutDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "ChatApp";
                    Log.d("ImageADdress", outPutDir);
//                        final String fileDes = outPutDir.getAbsolutePath();
//                        if (!outPutDir.mkdirs()) {
//                            AppLog.d("LOG", "Directory not created");
//                        }
                    try {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("IMAGE");
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view1 = inflater.inflate(R.layout.attach_alert_view, null);
                        ImageView alertImageView = (ImageView) view1.findViewById(R.id.imageView_Alert);
                        Picasso.with(context).load(getMsg).into(alertImageView);
                        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mProgressDialogforFileAndPic = ProgressDialog.show(context, "Downloading", "loading...", true, false);
                                mProgressDialogforFileAndPic.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                startDownLoad(context, fileURl, outPutDir, name, mProgressDialogforFileAndPic);

                            }
                        });
                        builder.setNeutralButton("Cancel", null);
                        builder.setView(view1);
                        builder.create().show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mProgressDialogforFileAndPic = ProgressDialog.show(context, "Downloading", "loading...", true, false);
                    mProgressDialogforFileAndPic.setProgressStyle(ProgressDialog.STYLE_SPINNER);//        imgArrayList = new ArrayList<>();
                    String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    //   AppLog.d("PATH", PATH);
                    startDownLoad(context, fileURl, PATH, name, mProgressDialogforFileAndPic);

                }
                //   mProgressDialogforFileAndPic.dismiss();
            }
        });






        try {
           firebase.child("User").child(U_ID).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
              @Override
              public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                  User user = dataSnapshot.getValue(User.class);
                 if(user.getImgUrl().equals("")){
                    img.setImageResource(R.drawable.ic_menu_camera);
                 }else{
                  picasso.with(context).load(user.getImgUrl()).transform(new RoundImage()).into(img);
              }
              }
              @Override
              public void onCancelled(DatabaseError databaseError) {
              }
          });





       }catch(Exception ex){
           ex.printStackTrace();
       }
        return view;
    }

    private void getImageFromUrl(String getMsg, ImageView sendImagePic, boolean flagURL, String name) {
        sendImagePic.setVisibility(View.VISIBLE);
        if (flagURL) {
            Picasso.with(context).load(getMsg).into(sendImagePic);
           // imageName.setText(name);
        } else {
          //  AppLog.d("DocName", name);

            Picasso.with(context).load(R.drawable.docs_icon).into(sendImagePic);
          //  imageName.setText(name);
        }
    }

    public void startDownLoad(Context context, String sourceUrl, String destinationPath, String filename, ProgressDialog progressDialog) {
        new DownLoadFileThread(context, sourceUrl, destinationPath, filename, progressDialog).start();
    }

    private class DownLoadFileThread extends Thread {
        ProgressDialog progressDialog = null;
        Context context = null;
        String sourceUrl = null;
        String destinationPath = null;
        String fileName = null;


        /*
         * Make sure sourceUrl ends with a file to download and not a folder!.
         *  Same with destinationPath.
         *  Ex:
         *
         *  sourceUrl="http://www.tempurl.com/image1.jpg"
         *  destinationPath="data/data/<package-name>/files/image1.jpg"
         */
        public DownLoadFileThread(Context context, String sourceUrl, String destinationPath, String fileName, ProgressDialog progressDialog) {
            this.context = context;
            this.sourceUrl = sourceUrl;
            this.destinationPath = destinationPath;
            this.fileName = fileName;
            this.progressDialog = progressDialog;
        }

        @Override
        public void run() {
            downLoadFileFromServer();
        }


        public boolean downLoadFileFromServer() {
            File outputFile = null;
            Log.v("DEBUG", "sourceUrl: " + sourceUrl);
            Log.v("DEBUG", "destinationPath: " + destinationPath);
            InputStream urlInputStream = null;
            URLConnection urlConnection;
            File file = new File(destinationPath);
            if (!file.exists()){
                file.mkdir();
            }
            try {

                //Form a new URL
                URL finalUrl = new URL(sourceUrl);

                urlConnection = finalUrl.openConnection();

                //Get the size of the (file) inputstream from server..
                int contentLength = urlConnection.getContentLength();


                Log.d("1URL", "Streaming from " + sourceUrl + "....");
                DataInputStream stream = new DataInputStream(finalUrl.openStream());

                Log.d("2FILE", "Buffering the received stream(size=" + contentLength + ") ...");
                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();
                Log.d("3FILE", "Buffered successfully(Buffer.length=" + buffer.length + ")....");

                if (buffer.length > 0) {
                    try {
                        Log.d("4FILE", "Creating the new file..");
                        outputFile = new File(file, fileName);
//                        if (!outputFile.exists()){
//                            outputFile.mkdir();
//                        }
                        FileOutputStream fos = new FileOutputStream(outputFile);

                        Log.d("5FILE", "Writing from buffer to the new file..");
                        fos.write(buffer);
                        fos.flush();

                        fos.close();
                        Log.d("6.1FILE", "Created the new file & returning 'true'..");

                        return true;
                    } catch (Exception e) {
                        Log.e("7ERROR", "Could not create new file(Path=" + destinationPath + ") ! & returning 'false'.......");
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    //Could not download the file...
                    Log.e("8ERROR", "Buffer size is zero ! & returning 'false'.......");
                    return false;
                }
            } catch (Exception e) {
                Log.e("9ERROR", "Failed to open urlConnection/Stream the connection(From catch block) & returning 'false'..");
                System.out.println("Exception: " + e);
                e.printStackTrace();
                return false;
            } finally {
                try {
                    Log.d("10URL", "Closing urlInputStream... ");

                    if (urlInputStream != null) urlInputStream.close();
                } catch (Exception e) {
                    Log.e("11ERROR", "Failed to close urlInputStream(From finally block)..");
                }
                MediaScannerConnection.scanFile(context, new String[]{outputFile.getAbsolutePath()},
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ChatApp")));
                            }
                        });
                progressDialog.dismiss();

            }
        }
    }
}
