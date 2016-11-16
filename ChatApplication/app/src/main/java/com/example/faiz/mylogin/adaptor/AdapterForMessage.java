package com.example.faiz.mylogin.adaptor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.Message;
import com.example.faiz.mylogin.model.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterForMessage extends BaseAdapter implements ListAdapter {


    private ArrayList<Message> arraylistforMessages;
    private Context context;
    private User uid;
    private String U_ID;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private DatabaseReference firebase;
    private String fileExtenstion;
    private ProgressDialog mProgressDialogforFileAndPic;
    private ImageView sendImagePic;
    private TextView msgView, timeView;
    private CircleImageView circleImageView;


    public AdapterForMessage(ArrayList<Message> arraylistforMessages, Context context, User uid) {
        this.arraylistforMessages = arraylistforMessages;
        this.context = context;
        this.uid = uid;
    }

    @Override
    public int getCount() {
        return arraylistforMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylistforMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        firebase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getInstance().getCurrentUser();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;


        if (arraylistforMessages.get(position).getU_id().equals(user.getUid())) {
            view = inflater.inflate(R.layout.message_left_side_layout, null);
        } else {
            view = inflater.inflate(R.layout.message_right_side_layout, null);

        }

        sendImagePic = (ImageView) view.findViewById(R.id.imagepic);
        msgView = (TextView) view.findViewById(R.id.messageView);
        timeView = (TextView) view.findViewById(R.id.timeView_messages);
        circleImageView = (CircleImageView) view.findViewById(R.id.imageView_messages);

        U_ID = arraylistforMessages.get(position).getU_id();

            firebase.child("User").child(U_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        // ImageView
        //  ProgressBar prog = (ProgressBar)view.findViewById(R.id.progressBarpic);
        msgView.setText(arraylistforMessages.get(position).getMsg());
        timeView.setText(arraylistforMessages.get(position).getTime());
//for image in conversation Activity
        final String getMsg = arraylistforMessages.get(position).getMsg();
        String messageObject = getMsg;
        String url = messageObject;
        fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d("fileExtension", fileExtenstion);
        final String name = URLUtil.guessFileName(url, null, fileExtenstion);
        final String fileURl = getMsg;
        Log.d("FileURL", fileURl);
        String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(fileURl);
        Log.d("FILEEXTENSION", fileExtenstion);
        int dot = fileURl.lastIndexOf('.');
        String base = (dot == -1) ? fileURl : fileURl.substring(0, dot);
        String extension = (dot == -1) ? "" : fileURl.substring(dot + 1);
        if (arraylistforMessages.get(position).getMsg().contains("https://")) {
            msgView.setVisibility(View.GONE);
            sendImagePic.setVisibility(View.VISIBLE);
            if (getMsg.contains(".jpg") || getMsg.contains(".png") || getMsg.contains(".jpeg") || getMsg.contains(".gif")) {
                Glide.with(context).load(arraylistforMessages.get(position).getMsg()).into(sendImagePic);
            } else {
                Glide.with(context).load(R.drawable.docs_icon).into(sendImagePic);
            }

        }

        sendImagePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Drawable dr = ((ImageView) sendImagePic).getDrawable();

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
//
                    final String Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ChatApp";

                    try {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("IMAGE");
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view1 = inflater.inflate(R.layout.attach_alert_view, null);
                        ImageView alertImageView = (ImageView) view1.findViewById(R.id.imageView_Alert);
                        Glide.with(context).load(getMsg).into(alertImageView);
                        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mProgressDialogforFileAndPic = ProgressDialog.show(context, "Downloading", "loading...", true, false);
                                mProgressDialogforFileAndPic.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                startDownLoad(context, fileURl, Path, name, mProgressDialogforFileAndPic);

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
                    final String Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/ChatApp";
                    //   AppLog.d("PATH", PATH);
                    startDownLoad(context, fileURl, Path, name, mProgressDialogforFileAndPic);

                }
                //   mProgressDialogforFileAndPic.dismiss();
            }
        });


        return view;
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
            if (!file.exists()) {
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

                progressDialog.dismiss();

            }
        }
    }


}
