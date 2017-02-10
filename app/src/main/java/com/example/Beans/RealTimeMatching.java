package com.example.Beans;

/**
 * Created by kjw90 on 2017-02-10.
 */
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public RealTimeMatching(){

    }

    public void setDetailedInterests(String detailedInterests){
        m_DetailedInterests = detailedInterests;
    }

    public void setStudent(Student student){
        m_Student = student;
    }

    public void setChattingNumber(String chattingNumber){
        m_ChattingNumber = chattingNumber;
    }

    public String getDetailedInterests(){
        return m_DetailedInterests;
    }

    public String getChattingNumber(){
        return m_ChattingNumber;
    }

    public Student getStudent(){
        return m_Student;
    }

    public void insertMatchingId(){
        root.child("tmpMatchingGroupId").child(m_Student.getId()).child("detailedInterests").setValue(m_DetailedInterests);
        root.child("tmpMatchingGroupId").child(m_Student.getId()).child("chattingNumber").setValue(m_ChattingNumber);

        root.child("tmpMatchingGroupId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //root.child("temp").setValue("T");
                for(DataSnapshot tmpMatchingGroupIdChild : dataSnapshot.getChildren()){
                    if(!tmpMatchingGroupIdChild.getKey().equals(m_Student.getId())){
                        m_otherPersonId = tmpMatchingGroupIdChild.getKey();

                        for(DataSnapshot idChild : tmpMatchingGroupIdChild.getChildren()){
                            if(idChild.getKey().equals("detailedInterests")){
                                m_otherPersonIdDetailedTnterests = idChild.getValue().toString();
                            }
                            if(idChild.getKey().equals("chattingNumber")){
                                m_otherPersonIdChattingNumber = Integer.parseInt(idChild.getValue().toString());
                            }

                            if(m_otherPersonIdDetailedTnterests.equals(m_DetailedInterests) && m_otherPersonIdChattingNumber == Integer.parseInt(m_ChattingNumber)){
                                m_roomTitle = m_DetailedInterests + "," + m_ChattingNumber;
                                root.child("tmpConditionEquals").child(m_roomTitle).child(m_Student.getId()).setValue("T");
                                root.child("tmpConditionEquals").child(m_roomTitle).child(m_otherPersonId).setValue("T");

                                root.child("tmpConditionEquals").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot tmpConditionEqualsChild : dataSnapshot.getChildren()){
                                            if(tmpConditionEqualsChild.getKey().equals(m_roomTitle)){
                                                for(DataSnapshot roomTitleChild : tmpConditionEqualsChild.getChildren()){
                                                    roomPeopleNumber = (int)roomTitleChild.getChildrenCount();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Log.e("ee", ""+roomPeopleNumber);
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
