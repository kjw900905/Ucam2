package com.example.Beans;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.Activity.ChatRoomFragment;
import com.example.Activity.R;

/**
 * Created by kjw90 on 2017-02-11.
 */

public class ClickParticipate {
    private String m_MakeRoomFlag;
    private FragmentActivity m_MyContext;
    private Student mStudent;
    private String m_DetailedInterests;
    private String m_ChattingNumber;

    public ClickParticipate(String makeRoomFlag, FragmentActivity myContext, Student student, String detailedInterests, String chattingNumber){
        m_MakeRoomFlag = makeRoomFlag;
        m_MyContext = myContext;
        mStudent = student;
        m_DetailedInterests = detailedInterests;
        m_ChattingNumber = chattingNumber;
    }

    public void clickParticipate(){
        ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
        FragmentManager fragmentManager = m_MyContext.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_in, chatRoomFragment).addToBackStack(null).commit();

        //m_MakeRoomFlag = "N";

        Bundle bundle = new Bundle(1);
        bundle.putSerializable("myInfo",mStudent);
        bundle.putString("detailedInterests", m_DetailedInterests);
        bundle.putString("chattingNumber", m_ChattingNumber);
        bundle.putString("makeRoomFlag", m_MakeRoomFlag);

        if(m_DetailedInterests == null) {
            bundle.putString("detailedInterestsFlag", "N");
        } else {
            bundle.putString("detailedInterestsFlag", "Y");
        }

        if(m_ChattingNumber == null) {
            bundle.putString("detailedInterestsMemberNumberFlag", "N");
        } else {
            bundle.putString("detailedInterestsMemberNumberFlag", "Y");
        }

        chatRoomFragment.setArguments(bundle);
    }
}
