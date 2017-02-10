package com.example.Beans;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Activity.ChatRoomFragment;
import com.example.Activity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by kjw90 on 2017-02-11.
 */

public class IsMakeRoomTask extends AsyncTask<String, Void, String> {
    private JSONArray getTime;
    private String myJSON;
    private String m_str_User_ID;
    private Activity m_activity;
    private String m_ChattingNumber;
    private String m_DetailedInterests;
    private Student mStudent;
    private boolean isFindFlag;
    private EditText m_edtNumPeople;
    private EditText m_setRoomName;
    private boolean isTitleExist, isRoomExist;
    private String mReservationFlag;
    private String mRoomName;
    private FragmentActivity m_MyContext;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private String m_MakeRoomFlag;

    public IsMakeRoomTask(String str_User_ID, Activity activity, String chattingNumber, String detailedInterests, Student student, EditText editNumPeople, EditText setRoomName, String reservationFlag, String roomName
            ,FragmentActivity myContext, String makeRoomFlag){
        m_str_User_ID = str_User_ID;
        m_activity = activity;
        m_ChattingNumber = chattingNumber;
        m_DetailedInterests = detailedInterests;
        mStudent = student;
        m_edtNumPeople = editNumPeople;
        m_setRoomName = setRoomName;
        mReservationFlag = reservationFlag;
        mRoomName = roomName;
        m_MyContext = myContext;
        m_MakeRoomFlag = makeRoomFlag;
    }

    /*ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }
          */

    protected String doInBackground(String[] params) {
        String temp_ID = m_str_User_ID;

        try {
            String data = "";
            data += URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(temp_ID, "UTF-8");

            URL url = new URL(Variable.m_SERVER_URL + Variable.m_PHP_SELECT_POSITION);
            URLConnection con = url.openConnection();

            con.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        } catch(Exception exception) {
            return new String(exception.getMessage());
        }
    }

    protected  void onPostExecute(String result) {
        myJSON = result;
        get_Time();
    }

    //SelectOneTask selectOneTask = new SelectOneTask();
    //selectOneTask.execute(str_User_ID);

    public void get_Time() {
        try {
            Calendar oCalendar = Calendar.getInstance();
            int nowTime = oCalendar.get(Calendar.HOUR_OF_DAY);
            int dayNum = oCalendar.get(Calendar.DAY_OF_WEEK);
            String day = "";
            switch (dayNum) {
                case 1:
                    day = "일";
                    break;
                case 2:
                    day = "월";
                    break;
                case 3:
                    day = "화";
                    break;
                case 4:
                    day = "수";
                    break;
                case 5:
                    day = "목";
                    break;
                case 6:
                    day = "금";
                    break;
                case 7:
                    day = "토";
                    break;
            }

            JSONObject jsonObj = new JSONObject(myJSON);
            getTime = jsonObj.getJSONArray("result");

            for (int i = 0; i < getTime.length(); i++) {
                JSONObject c = getTime.getJSONObject(i);
                String time = c.getString("time");
                int selectTime = Integer.parseInt(time);
                String selectDay = c.getString("day");

                if (selectTime == nowTime && selectDay.equals(day)) {
                    Toast.makeText(m_activity, "수업시간이라 방을 만들 수 없습니다.", Toast.LENGTH_SHORT).show();
                    isFindFlag = true;
                    break;
                }
            }
            if (!isFindFlag) {
                if (TextUtils.isEmpty(m_edtNumPeople.getText()) || m_setRoomName.getText().toString().length() == 0) {
                    Toast.makeText(m_activity, "위 항목을 전부 채워주십시오.", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isTitleExist) {
                        if (mReservationFlag.equals("Y")) {
                            mReservationFlag = "N";
                            Variable.reservationFlag = "Y";

                            isRoomExist = false;

                            mRoomName = m_setRoomName.getText().toString();

                            root.child("temp").setValue("T");

                            root.child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot chatsChild : dataSnapshot.getChildren()) {
                                        if (chatsChild.getKey().equals(mRoomName)) {
                                            Toast.makeText(m_activity, "방이 존재합니다.", Toast.LENGTH_SHORT).show();
                                            isRoomExist = true;
                                            break;
                                        }
                                    }

                                    if (!isRoomExist) {
                                        ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
                                        FragmentManager fragmentManager = m_MyContext.getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.content_in, chatRoomFragment).addToBackStack(null).commit();

                                        m_MakeRoomFlag = "Y";

                                        Bundle bundle = new Bundle(1);
                                        bundle.putSerializable("myInfo", mStudent);
                                        bundle.putString("detailedInterests", m_DetailedInterests);
                                        bundle.putString("chattingNumber", m_ChattingNumber);
                                        bundle.putString("makeRoomFlag", m_MakeRoomFlag);
                                        bundle.putString("roomName", mRoomName);
                                        bundle.putString("detailedInterestsFlag", "N");
                                        bundle.putString("detailedInterestsMemberNumberFlag", "N");
                                        chatRoomFragment.setArguments(bundle);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            isRoomExist = false;

                            mRoomName = m_setRoomName.getText().toString();

                            root.child("temp").setValue("T");

                            root.child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot chatsChild : dataSnapshot.getChildren()) {
                                        if (chatsChild.getKey().equals(mRoomName)) {
                                            Toast.makeText(m_activity, "방이 존재합니다.", Toast.LENGTH_SHORT).show();
                                            isRoomExist = true;
                                            break;
                                        }
                                    }

                                    if (!isRoomExist) {
                                        ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
                                        FragmentManager fragmentManager = m_MyContext.getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.content_in, chatRoomFragment).addToBackStack(null).commit();

                                        m_MakeRoomFlag = "Y";

                                        Bundle bundle = new Bundle(1);
                                        bundle.putSerializable("myInfo", mStudent);
                                        bundle.putString("detailedInterests", m_DetailedInterests);
                                        bundle.putString("chattingNumber", m_ChattingNumber);
                                        bundle.putString("makeRoomFlag", m_MakeRoomFlag);
                                        bundle.putString("roomName", mRoomName);
                                        bundle.putString("detailedInterestsFlag", "N");
                                        bundle.putString("detailedInterestsMemberNumberFlag", "N");
                                        chatRoomFragment.setArguments(bundle);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    } else {
                        //
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
