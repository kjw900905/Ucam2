package com.example.Beans;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by kjw90 on 2017-02-11.
 */

public class MatchingLodingTaskBar extends AsyncTask<Void, Void, Void>{
    ProgressDialog dialog;
    private Activity m_activity;
    private RealTimeMatching realTimeMatching;
    private String m_ChattingNumber;
    private String m_DetailedInterests;
    private Student mStudent;
    private EditText m_EdtInterests; // "관심분야" EditText
    private EditText m_EdtDetailInterests; // "세부항목" EditText
    private EditText m_EdtNumPeople; // "인원" EditText

    public MatchingLodingTaskBar(Activity activity, String chattingNumber, String detailedInterests, Student student, EditText edtInterests, EditText edtDetailInterests, EditText edtNumPeople){
        m_activity = activity;
        realTimeMatching = new RealTimeMatching();
        m_ChattingNumber = chattingNumber;
        m_DetailedInterests = detailedInterests;
        mStudent = student;
        m_EdtInterests = edtInterests;
        m_EdtDetailInterests = edtDetailInterests;
        m_EdtNumPeople = edtNumPeople;

    }

    @Override
    protected void onPreExecute() {
        //Log.e("sibal", "sibal");
        super.onPreExecute();
        dialog = new ProgressDialog(m_activity);
        dialog.setMessage("매칭중입니다...");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(m_activity, "취소됨", Toast.LENGTH_SHORT).show();
                RemoveMyTmpGroupId removeMyTmpGroupId = new RemoveMyTmpGroupId(mStudent, m_ChattingNumber, m_DetailedInterests);
                removeMyTmpGroupId.remove();
                realTimeMatching.setDetailedInterests("");
                realTimeMatching.setChattingNumber("0");
                m_EdtInterests.setText("");
                m_EdtDetailInterests.setText("");
                m_EdtNumPeople.setText("");

            }
        });
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        realTimeMatching.setChattingNumber(m_ChattingNumber);
        realTimeMatching.setDetailedInterests(m_DetailedInterests);
        realTimeMatching.setStudent(mStudent);
        realTimeMatching.setActivity(m_activity);
        realTimeMatching.setProgressDialog(dialog);
        realTimeMatching.insertMatchingId();

        return null;
    }

    protected void onPostExecute(String result) {

        //Toast.makeText(m_activity, "취소됨", Toast.LENGTH_SHORT).show();
        //dialog.dismiss();
        //myJSON = result;
        //check_ID_PW();
    }

    @Override
    protected void onCancelled() {
        //called on ui thread

    }
};
