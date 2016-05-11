package com.example.faiz.mylogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class Signin extends AppCompatActivity implements AdapterForToDoItems.Checked {

  //  private ArrayList<EmailAndPass> list = new ArrayList<>();
    private ArrayList<ToDo_Objects> todolist = new ArrayList<>();
    AdapterForToDoItems adapter = new AdapterForToDoItems(todolist,Signin.this);
    Firebase firebase;
    Button buttonAdd, buttonLogoutt;
    EditText todoTitle, todoDis;
    private String ref;
    private CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Firebase.setAndroidContext(this);


        ListView listView = (ListView) findViewById(R.id.listView1);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonLogoutt = (Button) findViewById(R.id.buttonLogout);


        listView.setAdapter(adapter);

        firebase = new Firebase("https://myloginf.firebaseio.com/");


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(Signin.this);
                View vv = layoutInflater.inflate(R.layout.myviewtodo, null);

                todoTitle = (EditText) vv.findViewById(R.id.edtTodoTitle);
                todoDis = (EditText) vv.findViewById(R.id.edtTodoDiscrip);
                check = (CheckBox)vv.findViewById(R.id.dialogboxcheck);
                ref = firebase.getAuth().getUid();


                AlertDialog.Builder builder = new AlertDialog.Builder(Signin.this);
                builder.setTitle("Add");
                builder.setMessage("Add ToDo in List");
                builder.setView(vv);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebase.child("UserData").child(ref).push().setValue(new ToDo_Objects(todoTitle.getText().toString(), todoDis.getText().toString(),check.isChecked()));
                        Toast.makeText(Signin.this, "Item add in ToDo", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Retrun", null);

                builder.show();
            }
        });


//
        firebase.child("UserData").child(firebase.getAuth().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                   ToDo_Objects post = dataSnapshot.getValue(ToDo_Objects.class);
                   todolist.add(new ToDo_Objects(post.getTitle().toString(), post.getDiscription().toString(), post.isCheck()));
                   adapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        buttonLogoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this, MainActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Signin.this);
                builder.setTitle("Remove");
                builder.setMessage("Remove this ToDo ?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       firebase.child(firebase.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               int i = 0;
                               for (DataSnapshot data : dataSnapshot.getChildren()) {
                                   if (i == position) {
                                       Firebase ref=data.getRef();
                                       ref.removeValue();
                                       Toast.makeText(Signin.this, "item Deleted", Toast.LENGTH_SHORT).show();
                                        todolist.remove(position);
                                       adapter.notifyDataSetChanged();

                                   }
                                   i++;

                               }
                           }

                           @Override
                           public void onCancelled(FirebaseError firebaseError) {

                           }
                       });
                    }
                });
                builder.setNegativeButton("Return", null);
                builder.show();
            }
        });


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

    @Override
    public void Check(final int pos, final boolean tick) {
        firebase.child(firebase.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Firebase ref = data.getRef();
                    if(i==pos){
                        ref.child("check").setValue(!tick);
                    }
                    i++;
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
