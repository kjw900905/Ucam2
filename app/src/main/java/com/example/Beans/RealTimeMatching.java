package com.example.Beans;

/**
 * Created by kjw90 on 2017-02-10.
 */

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.Activity.ChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RealTimeMatching {
    private String m_DetailedInterests;                //관심분야
    private String m_ChattingNumber;          //채팅방 인원수
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference tmpConditionEquals = FirebaseDatabase.getInstance().getReference().child("tmpConditionEquals");
    private Student m_Student;
    private String m_otherPersonIdDetailedTnterests;
    private int m_otherPersonIdChattingNumber;
    private String m_otherPersonId;
    private String m_roomTitle;
    private int roomPeopleNumber;
    private HashMap<String, String> idList;
    private ArrayList list;
    private Activity m_activity;

    public RealTimeMatching() {
        idList = new HashMap<String, String>();
    }

    public void setDetailedInterests(String detailedInterests) {
        m_DetailedInterests = detailedInterests;
    }

    public void setStudent(Student student) {
        m_Student = student;
    }

    public void setChattingNumber(String chattingNumber) {
        m_ChattingNumber = chattingNumber;
    }

    public String getDetailedInterests() {
        return m_DetailedInterests;
    }

    public String getChattingNumber() {
        return m_ChattingNumber;
    }

    public Student getStudent() {
        return m_Student;
    }

    public void setActivity(Activity activity){
        m_activity = activity;
    }

    public Activity getActivity(){
        return m_activity;
    }

    public void insertMatchingId() {
        root.child("tmpMatchingGroupId").child(m_Student.getId()).child("detailedInterests").setValue(m_DetailedInterests);
        root.child("tmpMatchingGroupId").child(m_Student.getId()).child("chattingNumber").setValue(m_ChattingNumber);

        root.child("tmpMatchingGroupId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //root.child("temp").setValue("T");
                for (DataSnapshot tmpMatchingGroupIdChild : dataSnapshot.getChildren()) {
                    if (!tmpMatchingGroupIdChild.getKey().equals(m_Student.getId())) {
                        m_otherPersonId = tmpMatchingGroupIdChild.getKey();

                        for (DataSnapshot idChild : tmpMatchingGroupIdChild.getChildren()) {
                            if (idChild.getKey().equals("detailedInterests")) {
                                m_otherPersonIdDetailedTnterests = idChild.getValue().toString();
                            }
                            if (idChild.getKey().equals("chattingNumber")) {
                                m_otherPersonIdChattingNumber = Integer.parseInt(idChild.getValue().toString());
                            }

                            if (m_otherPersonIdDetailedTnterests == null) {
                                Log.e("기달려", "기달");
                            } else {
                                if (m_otherPersonIdDetailedTnterests.equals(m_DetailedInterests) && m_otherPersonIdChattingNumber == Integer.parseInt(m_ChattingNumber)) {
                                    m_roomTitle = m_DetailedInterests + "," + m_ChattingNumber;
                                    root.child("tmpConditionEquals").child(m_roomTitle).child(m_Student.getId()).setValue("T");
                                    root.child("tmpConditionEquals").child(m_roomTitle).child(m_otherPersonId).setValue("T");

                                    root.child("tmpConditionEquals").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot tmpConditionEqualsChild : dataSnapshot.getChildren()) {
                                                if (tmpConditionEqualsChild.getKey().equals(m_roomTitle)) {
                                                    roomPeopleNumber = (int)tmpConditionEqualsChild.getChildrenCount();

                                                    for(DataSnapshot idChild : tmpConditionEqualsChild.getChildren()){
                                                        idList.put(idChild.getKey(), idChild.getKey());
                                                    }

                                                    if(roomPeopleNumber == m_otherPersonIdChattingNumber){
                                                        list = new ArrayList<>(idList.keySet());
                                                        //root.child("chats").child(m_roomTitle).

                                                        root.child("chats").child(m_roomTitle + " " + list.get(0)).child("detailedInterests").setValue(m_DetailedInterests);
                                                        for(int i=0; i<list.size(); i++){
                                                            root.child("chats").child(m_roomTitle + " " + list.get(0)).child(list.get(i).toString()).setValue("T");
                                                            root.child("member").child(m_roomTitle + " " + list.get(0)).child(list.get(i).toString()).setValue("T");
                                                        }

                                                        Calendar rightNow = Calendar.getInstance();
                                                        Date date = rightNow.getTime();
                                                        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                                                        String strDate = df.format(date);

                                                        root.child("chats").child(m_roomTitle + " " + list.get(0)).child("isEnterRoom").setValue("F");
                                                        root.child("chats").child(m_roomTitle + " " + list.get(0)).child("currentMemberNumber").setValue(list.size());
                                                        root.child("chats").child(m_roomTitle + " " + list.get(0)).child("limitMemberNumber").setValue(list.size());
                                                        root.child("chats").child(m_roomTitle + " " + list.get(0)).child("title").setValue(m_roomTitle + " " + list.get(0));
                                                        root.child("chats").child(m_roomTitle + " " + list.get(0)).child("time").setValue(strDate);

                                                        Intent intent = new Intent(m_activity, ChatActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("user_id", m_Student.getId());
                                                        intent.putExtra("room_name", m_roomTitle + " " + list.get(0));
                                                        m_activity.startActivity(intent);

                                                        m_otherPersonIdDetailedTnterests = "";
                                                        m_otherPersonIdChattingNumber = 0;
                                                        m_otherPersonId = "";
                                                        m_roomTitle = "";
                                                         roomPeopleNumber = 0;
                                                        idList.clear();
                                                        list.clear();

                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                        Query querytmpConditionEquals = ref.child("tmpConditionEquals").child(m_roomTitle);
                                                        Query querytmpMatchingGroupId = ref.child("tmpMatchingGroupId");

                                                        querytmpConditionEquals.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for(DataSnapshot chatsSnapshot : dataSnapshot.getChildren()) {
                                                                    chatsSnapshot.getRef().removeValue();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                        querytmpMatchingGroupId.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for(DataSnapshot chatsSnapshot : dataSnapshot.getChildren()) {
                                                                    if(chatsSnapshot.getKey().equals(idList.get(chatsSnapshot.getKey()))){
                                                                        chatsSnapshot.getRef().removeValue();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
