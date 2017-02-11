package com.example.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.example.Beans.ChatMessage;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    private String user_id, room_name;

    private Button btnclose;
    //static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ;
    private ArrayList<String> memberNameList;
    private ArrayAdapter adapter2;

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    RelativeLayout activity_chat;
    FloatingActionButton fab;
    Set<String> set;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.in, menu);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        memberNameList = new ArrayList<String>();

        btnclose = (Button)findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SlidingDrawer drawer = (SlidingDrawer)findViewById(R.id.slide);
                drawer.animateClose();
            }
        });

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("user_id");
        room_name = intent.getExtras().getString("room_name");

        activity_chat = (RelativeLayout)findViewById(R.id.activity_chat);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText)findViewById(R.id.input);

                mData.child("message").child(room_name).push().setValue(new ChatMessage(input.getText().toString(), user_id));
                input.setText("");

            }
        });

        adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, memberNameList);
        ListView listview = (ListView) findViewById(R.id.listMember);
        listview.setAdapter(adapter2);

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getKey().equals("member")){
                        for(DataSnapshot child2 : child.getChildren()){
                            if(child2.getKey().equals(room_name)){
                                for(DataSnapshot child3 : child2.getChildren()){
                                    //set = new HashSet<String>();

                                    //set.add(child3.getKey());
                                    if(!child3.hasChild(user_id)) {
                                        memberNameList.add(child3.getKey());
                                    }
                                    //Toast.makeText(getApplicationContext(), child3.getKey(), Toast.LENGTH_SHORT).show();

                                    /*if((child3.getKey().equals(room_name))){

                                    }*/
                                }
                            }
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(), memberNameList.toString(), Toast.LENGTH_SHORT).show();
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Toast.makeText(getApplicationContext(), "ss", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "zz", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), memberNameList.toString(), Toast.LENGTH_SHORT).show();

        //Snackbar.make(activity_chat, "Welcome " + user_id, Snackbar.LENGTH_SHORT).show();
        displayChatMessage();

    }

    private void displayChatMessage() {

        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.list_item_chat, mData.child("message").child(room_name)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText, messageUser, messageTime;
                messageText = (TextView)v.findViewById(R.id.message_text);
                messageUser = (TextView)v.findViewById(R.id.message_user);
                messageTime = (TextView)v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };
        listOfMessage.setAdapter(adapter);
    }
}
