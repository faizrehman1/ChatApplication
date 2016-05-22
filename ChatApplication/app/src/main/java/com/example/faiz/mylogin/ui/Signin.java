package com.example.faiz.mylogin.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.faiz.mylogin.adaptor.AdapterForMessage;
import com.example.faiz.mylogin.R;
import com.example.faiz.mylogin.model.Message;
import com.firebase.client.Firebase;

import java.util.ArrayList;

public class Signin extends AppCompatActivity {

    //  private ArrayList<EmailAndPass> list = new ArrayList<>();
    private ArrayList<Message> todolist = new ArrayList<>();
    AdapterForMessage adapter = new AdapterForMessage(todolist, Signin.this);
    Firebase firebase;
    Button buttonAdd, buttonLogoutt;
    EditText userMsg, userU_id;
    private String ref;
    private CheckBox check;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Firebase.setAndroidContext(this);


//        ListView listView = (ListView) findViewById(R.id.listView1);
//        buttonAdd = (Button) findViewById(R.id.buttonAdd);
//        buttonLogoutt = (Button) findViewById(R.id.buttonLogout);

//        Date date = new Date(System.currentTimeMillis());
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
//
//        final String var = dateFormat.format(date);


       // listView.setAdapter(adapter);

        firebase = new Firebase("https://chatapplicationn.firebaseio.com/");






//        buttonAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LayoutInflater layoutInflater = LayoutInflater.from(Signin.this);
//                View vv = layoutInflater.inflate(R.layout.myviewtodo, null);
//
//                userMsg = (EditText) vv.findViewById(R.id.edtTodoTitle);
//                userU_id = (EditText) vv.findViewById(R.id.edtTodoDiscrip);
//                // check = (CheckBox)vv.findViewById(R.id.dialogboxcheck);
//                ref = firebase.getAuth().getUid();
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(Signin.this);
//                builder.setTitle("Add");
//                builder.setMessage("Add ToDo in List");
//                builder.setView(vv);
//                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Firebase push = firebase.push();
//                        key = push.getKey();
//                        Log.d("LOL", key);
//                        firebase.child("Conversation").child(key).push().setValue(new Message(userMsg.getText().toString(), ref, var));
//                        Toast.makeText(Signin.this, "Item add in ToDo", Toast.LENGTH_SHORT).show();
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//                builder.setNegativeButton("Retrun", null);
//
//                builder.show();
//            }
//        });


//
//        firebase.child("Conversation").child(key).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                Message post = dataSnapshot.getValue(Message.class);
//                todolist.add(new Message(post.getMsg().toString(), post.getU_id().toString(), post.getTime()));
//                adapter.notifyDataSetChanged();
//
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });


        buttonLogoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(Signin.this);
//                builder.setTitle("Remove");
//                builder.setMessage("Remove this ToDo ?");
//                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        firebase.child(firebase.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                int i = 0;
//                                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                    if (i == position) {
//                                        Firebase ref = data.getRef();
//                                        ref.removeValue();
//                                        Toast.makeText(Signin.this, "item Deleted", Toast.LENGTH_SHORT).show();
//                                        todolist.remove(position);
//                                        adapter.notifyDataSetChanged();
//
//                                    }
//                                    i++;
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(FirebaseError firebaseError) {
//
//                            }
//                        });
//                    }
//                });
//                builder.setNegativeButton("Return", null);
//                builder.show();
//            }
//        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
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
