package com.example.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Beans.RoomInfo;
import com.example.Beans.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyChatRoomFragment extends Fragment {

    private EditText room_name;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    Student mStudent;

    private String m_detailedInterests;                //관심분야
    private String m_chattingNumber;
    private String m_makeRoomFlag;

    public MyChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mStudent = (Student)getArguments().getSerializable("myInfo");

        //Toast.makeText(getActivity(), m_detailedInterests+m_chattingNumber, Toast.LENGTH_SHORT).show();

        //room_name = (EditText) view.findViewById(R.id.room_name_edittext);
        listView = (ListView) view.findViewById(R.id.listViewConv);

        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_of_rooms);

        listView.setAdapter(arrayAdapter);

        //request_user_name();

        /*add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Map<String, Object> map = new HashMap<String, Object>();
                //map.put(room_name.getText().toString(), "");
                //root.updateChildren(map);
                root.child("chats").child(room_name.getText().toString()).child("title").setValue(" ");
                root.child("chats").child(room_name.getText().toString()).child("memberNumber").setValue(" ");
            }
        });*/

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()) {
                    set.add(((DataSnapshot)i.next()).getValue().toString());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);*/

                list_of_rooms.clear();

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    if(child.getKey().equals("chats")) {
                        for (DataSnapshot child2 : child.getChildren()) {
                            if(child.getKey().equals("users")){

                            }
                            list_of_rooms.add(child2.getKey());
                        }
                    }
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* TODO: 일단 채팅은 보류
                Intent intent = new Intent(getActivity(), Chat_Room.class);
                intent.putExtra("room_name", ((TextView)view).getText().toString());
                intent.putExtra("user_name", name);
                startActivity(intent);
                */
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user_id", mStudent.getId());
                intent.putExtra("room_name", ((TextView)view).getText().toString());
                root.child("users").child(mStudent.getId()).child("roomName").setValue(m_detailedInterests);
                startActivity(intent);
            }
        });

        return view;
    }

    /*
    private void request_user_name() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter name:");

        final EditText input_field = new EditText(getActivity());

        builder.setView(input_field);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = input_field.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                //request_user_name();
            }
        });

        builder.show();
    }
    */

}