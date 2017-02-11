package com.example.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Beans.ClickParticipate;
import com.example.Beans.IsMakeRoomTask;
import com.example.Beans.IsMatchingRoomTask;
import com.example.Beans.RealTimeMatching;
import com.example.Beans.Student;
import com.example.Beans.Variable;
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

public class MatchFragment extends Fragment {
    private EditText edtInterests; // "관심분야" EditText
    private EditText edtDetailInterests; // "세부항목" EditText
    private EditText edtNumPeople; // "인원" EditText
    private EditText setRoomName; // "방제 설정" EditText

    private Button btnInterests; // "관심분야" Button
    private Button btnDetailInterests; // "세부항목" Button
    private Button btnNumPeople; // "인원" Button

    private Button btnMakeRoom; // "방만들기" Button
    private Button btnMatch;
    private Button btnParticipate; // "참여하기" Button;

    private FragmentActivity myContext;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    private int m_selectInterests = 0; // "관심분야" RadioButton value
    private int m_o_selectInterests = -1; // 이전 "관심분야" value
    private boolean[] m_checkDetailInterestsGame = {false, false, false}; // "세부항목"의 "게임" CheckBox value
    private boolean[] m_checkDetailInterestsMeal = {false, false, false, false}; // "세부항목"의 "식사" CheckBox value
    private boolean[] m_checkDetailInterestsExercise = {false, false, false}; // "세부항목"의 "운동" CheckBox value
    private boolean[] m_checkDetailInterestsMajor = {false, false, false}; // "세부항목"의 "전공" CheckBox value
    private int m_selectNumPeople = 0; // "인원" RadioButton value

    private String detailedInterests;                //관심분야
    private String chattingNumber;          //채팅방 인원수
    private String makeRoomFlag;
    private String roomName;
    private String mReservationFlag;

    private Student mStudent;

    private JSONArray getTime;
    private String myJSON;

    private boolean isFindFlag, isTitleExist, isRoomExist;

    //private RealTimeMatching realTimeMatching;
    JSONArray person = null;

    public void MatchFragment() {
        // null
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        Calendar oCalendar = Calendar.getInstance();
        int nowTime = oCalendar.get(Calendar.HOUR_OF_DAY);

        edtInterests = (EditText) view.findViewById(R.id.edtInterests); // "관심분야" EditText
        edtDetailInterests = (EditText) view.findViewById(R.id.edtDetailInterests); // "세부항목" EditText
        edtNumPeople = (EditText) view.findViewById(R.id.edtNumPeople); // "인원" EditText
        setRoomName = (EditText)view.findViewById(R.id.setRoomName);

        makeRoomFlag = "N";
        mReservationFlag = "N";
        isFindFlag = false;
        isTitleExist = false;
        mStudent = (Student)getArguments().getSerializable("myInfo");

        if(Variable.reservationFlag.equals("Y")) {
            Variable.reservationDay = getArguments().getString("reservationDay");
            Variable.reservationTime = getArguments().getString("reservationTime");
            Variable.reservationFlag = "N";
            mReservationFlag = "Y";
        }

        btnInterests = (Button) view.findViewById(R.id.btnInterests); // "관심분야" Button
        btnDetailInterests = (Button) view.findViewById(R.id.btnDetailInterests); // "세부항목" Button
        btnNumPeople = (Button) view.findViewById(R.id.btnNumPeople); // "인원" Button

        btnMakeRoom = (Button) view.findViewById(R.id.btnMakeRoom); // "찾기" Button
        btnMatch = (Button) view.findViewById(R.id.btnMatch); // "매칭" Button
        btnParticipate = (Button) view.findViewById(R.id.btnParticipate); // "참여하기" Button

        // "관심분야" EditText onClick
        edtInterests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickProcessInterests();
            }
        });

        // "관심분야" Button onClick
        btnInterests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickProcessInterests();
            }
        });

        // "세부항목" EditText onClick
        edtDetailInterests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickProcessDetailInterests();
            }
        });

        // "세부항목" Button onClick
        btnDetailInterests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickProcessDetailInterests();
            }
        });

        // "인원" EditText onClick
        edtNumPeople.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickProcessNumPeople();
            }
        });

        // "인원" 버튼 onClick
        btnNumPeople.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickProcessNumPeople();
            }
        });

        // "방만들기" 버튼 onClick
        btnMakeRoom.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                onClickMakeRoom();
            }
        });

        // "매칭" 버튼 onClick
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMatchRoom();
            }
        });

        //"참여하기" 버튼 이겠죠? 저 갈건데요?? 알바 대타까지 구해놨는데요!?
        btnParticipate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                onClickParticipate();
            }
        });

        return view;
    }

    public void onClickProcessInterests() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("관심분야를 선택하세요.");
        //builder.setIcon(R.drawable.ic_menu_gallery);
        builder.setSingleChoiceItems(R.array.Interests, m_selectInterests, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                m_selectInterests = which;
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // [이전에 선택한 것과 다른것을 선택하는 경우(이전 index와 현재 index가 다른 경우)]
                // 1. "관심분야" EditText value 변경, "세부항목" EditText value 초기화, "인원" EditText value 초기화
                // 2. 이전 index와 현재 index를 일치시킨다.

                if (m_o_selectInterests != m_selectInterests) {
                    String[] interests = getResources().getStringArray(R.array.Interests); // app/res/values/strings.xml의 <string-array name="Interests">
                    edtInterests.setText(interests[m_selectInterests]); // "관심분야" EditText value 변경
                    //edtDetailInterests.setText(""); // "세부항목" EditText value 초기화
                    //edtNumPeople.setText(""); // "인원" EditText value 초기화
                    m_o_selectInterests = m_selectInterests; // 이전 index와 현재 index 일치
                    detailedInterests = interests[m_selectInterests]; // 방제목(관심분야 + 세부사항)을 설정해주기 위해 먼저 관심분야를 string값에 넣어줌.
                }
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }

    public void onClickProcessDetailInterests() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (TextUtils.isEmpty(edtInterests.getText())) { // "관심분야"를 선택하지 않았을때 메세지 출력
            builder.setTitle("알림");
            //builder.setIcon(R.drawable.ic_menu_gallery);
            builder.setMessage("관심분야를 먼저 선택해주세요.");
        } else { // "관심분야"를 선택한 경우에는 각 "관심분야"에 맞는 항목 출력
            builder.setTitle("세부항목을 선택하세요.");
            //builder.setIcon(R.drawable.ic_menu_gallery);
            if (m_selectInterests == 0) { // "관심분야"의 "게임" 선택
                builder.setMultiChoiceItems(R.array.DetailInterests_Game, m_checkDetailInterestsGame, new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        m_checkDetailInterestsGame[which] = isChecked;
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String[] game = getResources().getStringArray(R.array.DetailInterests_Game); // app/res/values/strings.xml의 <string-array name="DetailInterests_Game">
                        getCheckedValue(game, m_checkDetailInterestsGame); // 체크 표시가 되어있는 항목의 value를 구한다.
                    }
                });
            } else if (m_selectInterests == 1) { // "관심분야"의 "식사" 선택
                builder.setMultiChoiceItems(R.array.DetailInterests_Meal, m_checkDetailInterestsMeal, new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        m_checkDetailInterestsMeal[which] = isChecked;
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String[] meal = getResources().getStringArray(R.array.DetailInterests_Meal); // app/res/values/strings.xml의 <string-array name="DetailInterests_Meal">
                        getCheckedValue(meal, m_checkDetailInterestsMeal); // 체크 표시가 되어있는 항목의 value를 구한다.
                    }
                });
            } else if (m_selectInterests == 2) { // "관심분야"의 "운동" 선택
                builder.setMultiChoiceItems(R.array.DetailInterests_Exercise, m_checkDetailInterestsExercise, new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        m_checkDetailInterestsExercise[which] = isChecked;
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String[] meal = getResources().getStringArray(R.array.DetailInterests_Exercise); // app/res/values/strings.xml의 <string-array name="DetailInterests_Exercise">
                        getCheckedValue(meal, m_checkDetailInterestsExercise); // 체크 표시가 되어있는 항목의 value를 구한다.
                    }
                });
            } else if (m_selectInterests == 3) { // "관심분야"의 "전공" 선택
                builder.setMultiChoiceItems(R.array.DetailInterests_Major, m_checkDetailInterestsMajor, new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        m_checkDetailInterestsMajor[which] = isChecked;
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String[] meal = getResources().getStringArray(R.array.DetailInterests_Major); // app/res/values/strings.xml의 <string-array name="DetailInterests_Major">
                        getCheckedValue(meal, m_checkDetailInterestsMajor); // 체크 표시가 되어있는 항목의 value를 구한다.
                    }
                });
            }
            builder.setNegativeButton("취소", null);
        }
        builder.show();
    }

    public void onClickMakeRoom(){
        SelectOne(mStudent.getId());
    }

    public void onClickMatchRoom() {
        //TODO: "매칭" 버튼 관련 코드 삽입

        if(edtInterests.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "관심분야를 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else if(edtDetailInterests.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "세부사항을 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else if(edtNumPeople.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "인원을 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            IsMatchingRoomTask isMatchingRoomTask = new IsMatchingRoomTask(mStudent.getId(), getActivity(), chattingNumber, detailedInterests, mStudent, edtInterests, edtDetailInterests, edtNumPeople);
            isMatchingRoomTask.execute();

            edtInterests.setText("");
            edtDetailInterests.setText("");
            edtNumPeople.setText("");
        }
    }

    public void onClickParticipate(){
        ClickParticipate clickParticipate = new ClickParticipate(makeRoomFlag, myContext, mStudent, detailedInterests, chattingNumber);
        clickParticipate.clickParticipate();
    }

    // 체크 표시가 되어 있는 항목의 value를 구하기 위해 사용하는 메소드
    public void getCheckedValue(String[] detailInterests, boolean[] checkDetailInterests) {
        String result = ""; // EditText에 넣어주기 위한 String
        //detailedInterests = "";
        int numCheckedValue = 0; // 체크되어 있는 항목의 개수

        // 체크되어 있는 항목이 몇개인지 확인
        for (int i = 0; i < checkDetailInterests.length; i++) {
            if (checkDetailInterests[i]) { // 체크 표시가 되어있는 항목이 있는 경우
                numCheckedValue++;
            }
        }

        // 체크되어 있는 항목의 개수 (-1)개 만큼 쉼표(,)를 붙여준다.
        for (int i = 0; i < checkDetailInterests.length; i++) {
            if (checkDetailInterests[i]) { // 체크 표시가 되어있는 항목이 있는 경우
                result += detailInterests[i]; // 체크 표시가 되어있는 항목의 value를 result에 추가한다.
                if (numCheckedValue != 1) { // 체크 표시가 되어있는 항목이 1개가 아닌 경우(2개 이상인 경우)
                    result += ", "; // 쉼표를 1개 붙여준다.
                    numCheckedValue--;
                }
            }
        }

        edtDetailInterests.setText(result); // "세부항목" EditText value 변경
        detailedInterests += (" " + result);
        //detailedInterests = "";
        edtNumPeople.setText(""); // "인원" EditText value 초기화
    }

    public void onClickProcessNumPeople() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (TextUtils.isEmpty(edtInterests.getText())) { // "관심분야"를 선택하지 않았을때 메세지 출력
            builder.setTitle("알림");
            //builder.setIcon(R.drawable.ic_menu_gallery);
            builder.setMessage("관심분야를 먼저 선택해주세요.");
        } else if (TextUtils.isEmpty(edtDetailInterests.getText())) { // "세부항목"을 선택하지 않았을때 메세지 출력
            builder.setTitle("알림");
            //builder.setIcon(R.drawable.ic_menu_gallery);
            builder.setMessage("세부항목을 먼저 선택해주세요.");
        } else { // "관심분야"와 "세부항목"을 모두 선택한 경우에만 인원을 선택할 수 있도록 함
            builder.setTitle("인원을 선택하세요.");
            builder.setSingleChoiceItems(R.array.NumPeople, m_selectNumPeople, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    m_selectNumPeople = which;
                }
            });
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String[] numPeople = getResources().getStringArray(R.array.NumPeople); // app/res/values/strings.xml의 <string-array name="NumPeople">
                    edtNumPeople.setText(numPeople[m_selectNumPeople]); // "인원" EditText value 변경
                    chattingNumber = numPeople[m_selectNumPeople];      //인원을 스트링에 넣어줌.
                    chattingNumber = chattingNumber.substring(0,1);
                }
            });
            builder.setNegativeButton("취소", null);
        }
        builder.show();
    }

    public void SelectOne(String str_User_ID) {
        IsMakeRoomTask isMakeRoomTask = new IsMakeRoomTask(str_User_ID, getActivity(), chattingNumber, detailedInterests, mStudent, edtNumPeople, setRoomName, mReservationFlag, roomName, myContext,
                makeRoomFlag);

        isMakeRoomTask.execute();

        /*edtInterests.setText("");
        edtDetailInterests.setText("");
        edtNumPeople.setText("");*/

        //detailedInterests = "";
        //chattingNumber = "";
    }
}