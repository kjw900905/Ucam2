package com.example.Beans;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by kjw90 on 2017-02-11.
 */

public class RemoveMyTmpGroupId {
    private Student m_Student;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private String m_DetailedInterests;                //관심분야
    private String m_ChattingNumber;          //채팅방 인원수
    private String m_RoomTitle;

    public RemoveMyTmpGroupId(Student student, String chattingNumber, String detailedInterests){
        m_Student = student;
        m_ChattingNumber = chattingNumber;
        m_DetailedInterests = detailedInterests;
    }

    public void remove(){
        root.child("Tmp").setValue("T");

        m_RoomTitle = m_DetailedInterests + "," + m_ChattingNumber;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query querytmpMatchingGroupId = ref.child("tmpMatchingGroupId");
        Query querytmpConditionEquals = ref.child("tmpConditionEquals").child(m_RoomTitle);

        querytmpConditionEquals.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot chatsSnapshot : dataSnapshot.getChildren()) {
                    if(chatsSnapshot.getKey().equals(m_Student.getId())){
                        chatsSnapshot.getRef().removeValue();
                    }
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
                    if(chatsSnapshot.getKey().equals(m_Student.getId())){
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
