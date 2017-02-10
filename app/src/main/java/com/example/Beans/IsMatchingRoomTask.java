package com.example.Beans;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

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

public class IsMatchingRoomTask extends AsyncTask<String, Void, String> {

    private JSONArray getTime;
    private String myJSON;
    private String m_str_User_ID;
    private Activity m_activity;
    private String m_ChattingNumber;
    private String m_DetailedInterests;
    private Student mStudent;
    private boolean isFindFlag;
    private EditText m_EdtInterests; // "관심분야" EditText
    private EditText m_EdtDetailInterests; // "세부항목" EditText
    private EditText m_EdtNumPeople; // "인원" EditText

    public IsMatchingRoomTask(String str_User_ID, Activity activity, String chattingNumber, String detailedInterests, Student student, EditText edtInterests, EditText edtDetailInterests, EditText edtNumPeople){
        m_str_User_ID = str_User_ID;
        m_activity = activity;
        m_ChattingNumber = chattingNumber;
        m_DetailedInterests = detailedInterests;
        mStudent = student;
        m_EdtInterests = edtInterests;
        m_EdtDetailInterests = edtDetailInterests;
        m_EdtNumPeople = edtNumPeople;
    }

    /*ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }
          */

    protected String doInBackground(String[] params) {
        String temp_ID = m_str_User_ID;
        Log.e("log222", temp_ID);

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
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString().trim();
        } catch (Exception exception) {
            return new String(exception.getMessage());
        }
    }

    protected void onPostExecute(String result) {
        myJSON = result;
        get_Time();
    }

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
                    Toast.makeText(m_activity, "수업시간이라 매칭을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    isFindFlag = true;
                    break;
                }
            }

            if(!isFindFlag){
                MatchingLodingTaskBar matchingLodingTaskBar = new MatchingLodingTaskBar(m_activity, m_ChattingNumber, m_DetailedInterests, mStudent, m_EdtInterests, m_EdtDetailInterests, m_EdtNumPeople);
                matchingLodingTaskBar.execute();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
