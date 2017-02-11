package com.example.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Beans.RoomInfo;
import com.example.Beans.Student;
import com.example.Beans.Variable;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ChatRoomFragment extends Fragment {

    private EditText room_name;

    private ListView listView;
    private ChatRoomArrayAdapter adapter;
    private ArrayList<RoomInfo> list_of_rooms = new ArrayList<RoomInfo>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    Student mStudent;

    private String m_detailedInterests;                //관심분야
    private String m_chattingNumber;
    private String m_makeRoomFlag;
    private String m_detailedInterestsFlag;
    private String m_detailedInterestsMemberNumberFlag;
    private String m_roomName;

    private int roomLimitNumber;
    private int currentRoomNumber;
    private Boolean isEnterRoom;

    private int m_currentMemberNumber;        //현재인원
    private String m_currentMemberNumberString;   //현재인원 스트링으로 변환해줄거


    public ChatRoomFragment() {
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

        m_makeRoomFlag = "N";
        mStudent = (Student) getArguments().getSerializable("myInfo");
        m_detailedInterests = getArguments().getString("detailedInterests");
        m_chattingNumber = getArguments().getString("chattingNumber");
        m_roomName = getArguments().getString("roomName");
        m_makeRoomFlag = getArguments().getString("makeRoomFlag");
        m_detailedInterestsFlag = getArguments().getString("detailedInterestsFlag");
        m_detailedInterestsMemberNumberFlag = getArguments().getString("detailedInterestsMemberNumberFlag");

        //Toast.makeText(getActivity(), m_detailedInterests+m_chattingNumber, Toast.LENGTH_SHORT).show();
        //room_name = (EditText) view.findViewById(R.id.room_name_edittext);
        adapter = new ChatRoomArrayAdapter(getContext(), R.layout.list_item_chat_room, list_of_rooms);
        listView = (ListView) view.findViewById(R.id.listViewConv);
        listView.setAdapter(adapter);

        if (m_makeRoomFlag.equals("Y")) {
            if (Variable.reservationFlag.equals("Y")) {
                Variable.reservationFlag = "N";
                root.child("chats").child(m_roomName).child("title").setValue(m_roomName);
                root.child("chats").child(m_roomName).child("detailedInterests").setValue(m_detailedInterests);
                root.child("chats").child(m_roomName).child("limitMemberNumber").setValue(m_chattingNumber);
                root.child("chats").child(m_roomName).child("currentMemberNumber").setValue("1");
                root.child("chats").child(m_roomName).child("isEnterRoom").setValue("T");

                Variable.reservationRoomName = new JSONObject();
                try {
                    Variable.reservationRoomName.put(Variable.reservationPosition, m_roomName);
                    Variable.reservationPosition = "";
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                //날짜변환
                /*Calendar rightNow = Calendar.getInstance();
                Date date = rightNow.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                String strDate = df.format(date);*/

                //트리 chats에 날짜까지 넣어준다.
                //memeber는 어떤방에 어떤유저가 있는지 알려주기 위해 넣어줌.
                root.child("chats").child(m_roomName).child("time").setValue(Variable.reservationDay + "요일 " + Variable.reservationTime + "시 예약");
                root.child("member").child(m_roomName).child(mStudent.getId()).setValue("T");
                root.child("chats").child(m_roomName).child(mStudent.getId()).setValue("T");

                Variable.reservationDay = "";
                Variable.reservationTime = "";

                Log.e("1", "1");

                //바로 방만들어줌. 바로 방만들어주는거 아니면 모든 방 목록 구경할 수 있음.
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user_id", mStudent.getId());
                intent.putExtra("room_name", (m_roomName));
                startActivity(intent);
            } else {
            /*트리 생성 chats -> 방제목 -> 관심분야
                                        -> 방제목
                                        ->인원  이 형태로 디비에 생성됨*/
                root.child("chats").child(m_roomName).child("title").setValue(m_roomName);
                root.child("chats").child(m_roomName).child("detailedInterests").setValue(m_detailedInterests);
                root.child("chats").child(m_roomName).child("limitMemberNumber").setValue(m_chattingNumber);
                root.child("chats").child(m_roomName).child("currentMemberNumber").setValue("1");
                root.child("chats").child(m_roomName).child("isEnterRoom").setValue("T");

                //날짜변환
                Calendar rightNow = Calendar.getInstance();
                Date date = rightNow.getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                String strDate = df.format(date);

                //트리 chats에 날짜까지 넣어준다.
                //memeber는 어떤방에 어떤유저가 있는지 알려주기 위해 넣어줌.
                root.child("chats").child(m_roomName).child("time").setValue(strDate);
                root.child("member").child(m_roomName).child(mStudent.getId()).setValue("T");
                root.child("chats").child(m_roomName).child(mStudent.getId()).setValue("T");

                Log.e("1", "1");

                //바로 방만들어줌. 바로 방만들어주는거 아니면 모든 방 목록 구경할 수 있음.
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user_id", mStudent.getId());
                intent.putExtra("room_name", (m_roomName));
                startActivity(intent);
            }
        }

        if (m_detailedInterestsFlag.equals("Y") && m_detailedInterestsMemberNumberFlag.equals("N")) {
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    list_of_rooms.clear();

                    //Log.e("sibal", "detailflag");

                    String detailedInterests = "";
                    String memberLimitNumber = "";
                    String time = "";
                    String title = "";

                    //Toast.makeText(getContext(), m_detailedInterests, Toast.LENGTH_SHORT).show();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //child는 현재 root에서 바로 아래 chats, message, users, member까지 온 상태
                        if (child.getKey().equals("chats")) {
                            for (DataSnapshot child2 : child.getChildren()) {
                                //child2는 if문에서 chats로 들어오고 방제목까지 온 상태
                                //list_of_rooms.add(new RoomInfo(child2.getChildren().));
                                for (DataSnapshot child3 : child2.getChildren()) {
                                    //child3는 if문에서 방제목(고유값)으로 들어오고 관심분야, 시간, 인원에 접근 할 수 있는 상태 if문으로 하나하나 값을 넣어주게 만듬.
                                    if (child3.getKey().equals("detailedInterests")) {
                                        detailedInterests = child3.getValue().toString();
                                    }
                                    if (child3.getKey().equals("limitMemberNumber")) {
                                        memberLimitNumber = (child3.getValue().toString());
                                    }
                                    if (child3.getKey().equals("title")) {
                                        title = (child3.getValue().toString());
                                    }
                                    if (child3.getKey().equals("time")) {
                                        time = child3.getValue().toString();
                                    }
                                    if (child3.getKey().equals("currentMemberNumber")) {
                                        m_currentMemberNumber = Integer.valueOf(child3.getValue().toString());
                                        m_currentMemberNumberString = String.valueOf(m_currentMemberNumber);
                                        //currentMemberNumber++;
                                    }
                                }

                                if (m_detailedInterests.equals(child2.child("detailedInterests").getValue().toString())) {
                                    list_of_rooms.add(new RoomInfo(title, detailedInterests, memberLimitNumber, time, m_currentMemberNumberString));
                                }
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (m_detailedInterestsFlag.equals("Y") && m_detailedInterestsMemberNumberFlag.equals("Y")) {
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    list_of_rooms.clear();

                    String detailedInterests = "";
                    String memberLimitNumber = "";
                    String time = "";
                    String title = "";

                    //Toast.makeText(getContext(), m_detailedInterests, Toast.LENGTH_SHORT).show();

                    Log.e("sibal2", "m_detailedInterestsMemberNumberFlag");

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //child는 현재 root에서 바로 아래 chats, message, users, member까지 온 상태
                        if (child.getKey().equals("chats")) {
                            for (DataSnapshot child2 : child.getChildren()) {
                                //child2는 if문에서 chats로 들어오고 방제목까지 온 상태
                                //list_of_rooms.add(new RoomInfo(child2.getChildren().));
                                for (DataSnapshot child3 : child2.getChildren()) {
                                    //child3는 if문에서 방제목(고유값)으로 들어오고 관심분야, 시간, 인원에 접근 할 수 있는 상태 if문으로 하나하나 값을 넣어주게 만듬.
                                    if (child3.getKey().equals("detailedInterests")) {
                                        detailedInterests = child3.getValue().toString();
                                    }
                                    if (child3.getKey().equals("limitMemberNumber")) {
                                        memberLimitNumber = (child3.getValue().toString());
                                    }
                                    if (child3.getKey().equals("title")) {
                                        title = (child3.getValue().toString());
                                    }
                                    if (child3.getKey().equals("time")) {
                                        time = child3.getValue().toString();
                                    }
                                    if (child3.getKey().equals("currentMemberNumber")) {
                                        m_currentMemberNumber = Integer.valueOf(child3.getValue().toString());
                                        m_currentMemberNumberString = String.valueOf(m_currentMemberNumber);
                                        //currentMemberNumber++;
                                    }
                                }

                                if (m_detailedInterests.equals(child2.child("detailedInterests").getValue().toString()) && m_chattingNumber.equals(child2.child("limitMemberNumber").getValue().toString())) {
                                    list_of_rooms.add(new RoomInfo(title, detailedInterests, memberLimitNumber, time, m_currentMemberNumberString));
                                }
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    list_of_rooms.clear();

                    String detailedInterests = "";
                    String memberLimitNumber = "";
                    String time = "";
                    String title = "";

                    //Toast.makeText(getContext(), m_detailedInterests, Toast.LENGTH_SHORT).show();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //child는 현재 root에서 바로 아래 chats, message, users, member까지 온 상태
                        if (child.getKey().equals("chats")) {
                            for (DataSnapshot child2 : child.getChildren()) {
                                //child2는 if문에서 chats로 들어오고 방제목까지 온 상태
                                //list_of_rooms.add(new RoomInfo(child2.getChildren().));
                                for (DataSnapshot child3 : child2.getChildren()) {
                                    //child3는 if문에서 방제목(고유값)으로 들어오고 관심분야, 시간, 인원에 접근 할 수 있는 상태 if문으로 하나하나 값을 넣어주게 만듬.
                                    if (child3.getKey().equals("detailedInterests")) {
                                        detailedInterests = child3.getValue().toString();
                                    }
                                    if (child3.getKey().equals("limitMemberNumber")) {
                                        memberLimitNumber = (child3.getValue().toString());
                                    }
                                    if (child3.getKey().equals("title")) {
                                        title = (child3.getValue().toString());
                                    }
                                    if (child3.getKey().equals("time")) {
                                        time = child3.getValue().toString();
                                    }
                                    if (child3.getKey().equals("currentMemberNumber")) {
                                        m_currentMemberNumber = Integer.valueOf(child3.getValue().toString());
                                        m_currentMemberNumberString = String.valueOf(m_currentMemberNumber);
                                        //currentMemberNumber++;
                                    }
                                }
                                list_of_rooms.add(new RoomInfo(title, detailedInterests, memberLimitNumber, time, m_currentMemberNumberString));
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            RoomInfo r;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                r = list_of_rooms.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("방을 삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        root.child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (Integer.parseInt(dataSnapshot.child(r.getM_roomTitle()).child("currentMemberNumber").getValue().toString()) == 1) {
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

                                    list_of_rooms.remove(position);
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

                                    int currentMemberNumber = Integer.parseInt(dataSnapshot.child(r.getM_roomTitle()).child("currentMemberNumber").getValue().toString());
                                    currentMemberNumber --;
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
                                if (chatsChild.hasChild(mStudent.getId())) {
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

                                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                                        intent.putExtra("user_id", mStudent.getId());
                                        intent.putExtra("room_name", r.getM_roomTitle());
                                        startActivity(intent);

                                        root.child("chats").child(r.getM_roomTitle()).child("currentMemberNumber").setValue(currentMemberNumber);
                                        root.child("chats").child(r.getM_roomTitle()).child(mStudent.getId()).setValue("T");
                                        root.child("member").child(r.getM_roomTitle()).child(mStudent.getId()).setValue("T");
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

    private class ChatRoomArrayAdapter extends ArrayAdapter<RoomInfo> {
        private ArrayList<RoomInfo> items;
        private int textViewResourceId;
        private Context context;

        public ChatRoomArrayAdapter(Context context, int textViewResourceId, ArrayList<RoomInfo> items) {
            super(context, textViewResourceId, items);
            this.context = context;
            this.textViewResourceId = textViewResourceId;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.list_item_chat_room, null);
            }

            RoomInfo r = items.get(position);

            if (r != null) {
                TextView text_room_time = (TextView) v.findViewById(R.id.text_room_time);
                TextView text_room_interests = (TextView) v.findViewById(R.id.text_room_interests);
                TextView text_room_title = (TextView) v.findViewById(R.id.text_room_title);
                TextView text_room_num_people = (TextView) v.findViewById(R.id.text_room_num_people);

                text_room_time.setText(r.getM_roomTime());
                text_room_interests.setText(r.getM_roomInterest());
                text_room_title.setText(r.getM_roomTitle());
                text_room_num_people.setText(r.getM_roomCurrentMemberNumber() + "/" + r.getM_roomLimitMemberNumber());
            }
            return v;
        }
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
        */
}

