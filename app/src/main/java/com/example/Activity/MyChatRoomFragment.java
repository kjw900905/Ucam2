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
    private ListView listView;
    private MyChatRoomArrayAdapter adapter;
    private ArrayList<RoomInfo> list_of_rooms = new ArrayList<RoomInfo>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    Student mStudent;

    public MyChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mStudent = (Student)getArguments().getSerializable("myInfo");

        adapter = new MyChatRoomArrayAdapter(getContext(), R.layout.list_item_chat_room, list_of_rooms);
        listView = (ListView) view.findViewById(R.id.listViewConv);
        listView.setAdapter(adapter);

        root.child("temp").setValue("T");

        Toast.makeText(getContext(), "채팅방 확인", Toast.LENGTH_SHORT).show();
        //
        root.child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_of_rooms.clear();

                String currentMemberNumber = "";
                String detailedInterests = "";
                String limitMemberNumber = "";
                String time = "";
                String title = "";

                for(DataSnapshot chatsChild : dataSnapshot.getChildren()) {
                    for(DataSnapshot roomChild : chatsChild.getChildren()) {
                        if(roomChild.hasChild(mStudent.getId())) {
                            if(roomChild.getKey().equals("currentMemberNumber")) {
                                currentMemberNumber = roomChild.getValue().toString();
                            }
                            if(roomChild.getKey().equals("detailedInterests")) {
                                detailedInterests = roomChild.getValue().toString();
                            }
                            if(roomChild.getKey().equals("limitMemberNumber")) {
                                limitMemberNumber = roomChild.getValue().toString();
                            }
                            if(roomChild.getKey().equals("time")) {
                                time = roomChild.getValue().toString();
                            }
                            if(roomChild.getKey().equals("title")) {
                                title = roomChild.getValue().toString();
                            }

                            list_of_rooms.add(new RoomInfo(title, detailedInterests, limitMemberNumber, time, currentMemberNumber));
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            RoomInfo r;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                r = list_of_rooms.get(position);

                // root/chats 이벤트 설정
                root.child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot chatsChild : dataSnapshot.getChildren()) {
                            if (chatsChild.getKey().equals(r.getM_roomTitle())) {
                                if(chatsChild.hasChild(mStudent.getId())) {
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("user_id", mStudent.getId());
                                    intent.putExtra("room_name", r.getM_roomTitle());
                                    startActivity(intent);
                                } else {
                                    int limitMemberNumber = Integer.parseInt(chatsChild.child("limitMemberNumber").getValue().toString());
                                    int currentMemberNumber = Integer.parseInt(chatsChild.child("currentMemberNumber").getValue().toString());

                                    // 현재 인원수가 최대 인원수보다 적으면 채팅방에 입장
                                    if (currentMemberNumber < limitMemberNumber) {
                                        currentMemberNumber++;
                                        r.setM_roomCurrentMemberNumber(String.valueOf(currentMemberNumber));
                                        root.child("chats").child(r.getM_roomTitle()).child("currentMemberNumber").setValue(currentMemberNumber);
                                        root.child("chats").child(r.getM_roomTitle()).child(mStudent.getId()).setValue("T");
                                        root.child("member").child(r.getM_roomTitle()).child(mStudent.getId()).setValue("T");

                                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                                        intent.putExtra("user_id", mStudent.getId());
                                        intent.putExtra("room_name", r.getM_roomTitle());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getContext(), "인원이 다 찼습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private class MyChatRoomArrayAdapter extends ArrayAdapter<RoomInfo> {
        private Context context;
        private int textViewResourceId;
        private ArrayList<RoomInfo> items;

        public MyChatRoomArrayAdapter(Context context, int textViewResourceId, ArrayList<RoomInfo> items) {
            super(context, textViewResourceId, items);
            this.context = context;
            this.textViewResourceId = textViewResourceId;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_chat_room, null);
            }

            RoomInfo r = items.get(position);

            if (r != null) {
                TextView text_room_time = (TextView) view.findViewById(R.id.text_room_time);
                TextView text_room_interests = (TextView) view.findViewById(R.id.text_room_interests);
                TextView text_room_title = (TextView) view.findViewById(R.id.text_room_title);
                TextView text_room_num_people = (TextView) view.findViewById(R.id.text_room_num_people);

                text_room_time.setText(r.getM_roomTime());
                text_room_interests.setText(r.getM_roomInterest());
                text_room_title.setText(r.getM_roomTitle());
                text_room_num_people.setText(r.getM_roomCurrentMemberNumber() + "/" + r.getM_roomLimitMemberNumber());
            }

            return view;
        }
    }
}