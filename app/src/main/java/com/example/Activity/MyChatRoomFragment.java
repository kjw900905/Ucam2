package com.example.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.Query;
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
        View view = inflater.inflate(R.layout.fragment_my_chat, container, false);

        mStudent = (Student) getArguments().getSerializable("myInfo");

        adapter = new MyChatRoomArrayAdapter(getContext(), R.layout.list_item_chat_room, list_of_rooms);
        listView = (ListView) view.findViewById(R.id.listViewMyChat);
        listView.setAdapter(adapter);

        root.child("temp").setValue("T");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_of_rooms.clear();

                String currentMemberNumber = "";
                String detailedInterests = "";
                String limitMemberNumber = "";
                String time = "";
                String title = "";

                for (DataSnapshot roomChild : dataSnapshot.child("chats").getChildren()) {
                    if (roomChild.hasChild(mStudent.getId())) {
                        currentMemberNumber = roomChild.child("currentMemberNumber").getValue().toString();
                        detailedInterests = roomChild.child("detailedInterests").getValue().toString();
                        limitMemberNumber = roomChild.child("limitMemberNumber").getValue().toString();
                        time = roomChild.child("time").getValue().toString();
                        title = roomChild.child("title").getValue().toString();

                        list_of_rooms.add(new RoomInfo(title, detailedInterests, limitMemberNumber, time, currentMemberNumber));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (list_of_rooms.isEmpty()) {
                    Toast.makeText(getContext(), "현재 속해 있는 채팅방이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
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

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user_id", mStudent.getId());
                intent.putExtra("room_name", r.getM_roomTitle());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            RoomInfo r;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                r = list_of_rooms.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("방을 삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (Integer.parseInt(dataSnapshot.child("chats").child(r.getM_roomTitle()).child("currentMemberNumber").getValue().toString()) == 1) {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    Query deleteChatsQuery = ref.child("chats").child(r.getM_roomTitle());
                                    Query deleteMemberQuery = ref.child("member").child(r.getM_roomTitle());
                                    Query deleteMessageQuery = ref.child("message").child(r.getM_roomTitle());

                                    deleteChatsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().removeValue();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    deleteMemberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().removeValue();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    deleteMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().removeValue();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    //Toast.makeText(getContext(), position, Toast.LENGTH_SHORT).show();

                                    list_of_rooms.remove(r);
                                } else {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    Query deleteChatsQuery = ref.child("chats").child(r.getM_roomTitle()).child(mStudent.getId());
                                    Query deleteMemberQuery = ref.child("member").child(r.getM_roomTitle()).child(mStudent.getId());

                                    deleteChatsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().removeValue();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    deleteMemberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().removeValue();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    int currentMemberNumber = Integer.parseInt(dataSnapshot.child("chats").child(r.getM_roomTitle()).child("currentMemberNumber").getValue().toString());
                                    currentMemberNumber--;
                                    root.child("chats").child(r.getM_roomTitle()).child("currentMemberNumber").setValue(String.valueOf(currentMemberNumber));
                                    r.setM_roomCurrentMemberNumber(String.valueOf(currentMemberNumber));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();

                return true;
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