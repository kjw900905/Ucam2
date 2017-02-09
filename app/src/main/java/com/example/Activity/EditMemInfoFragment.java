package com.example.Activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.WindowDecorActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.Beans.Student;
import com.example.Beans.Variable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class EditMemInfoFragment extends Fragment {

    EditText CurrentPw, NewPw, NewPw_Re;
    boolean isResetAvailable = true;

    public EditMemInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_mem_info, container, false);

        final Student mStudent = (Student)getArguments().getSerializable("myInfo");

        CurrentPw = (EditText)view.findViewById(R.id.input_Current_Pw);
        NewPw = (EditText)view.findViewById(R.id.input_New_Pw);
        NewPw_Re = (EditText)view.findViewById(R.id.re_Input_New_Pw);

        Button btnUnregister = (Button) view.findViewById(R.id.unregister_Button);
        btnUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());     // 여기서 this는 Activity의 this

// 여기서 부터는 알림창의 속성 설정
                builder.setTitle("회원탈퇴")        // 제목 설정
                        .setMessage("정말로 탈퇴하시겠습니까?")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){

                                Delete(mStudent.getId());
                                Toast.makeText(getActivity(), "회원탈퇴 완료\n이전 정보는 열람할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
            }
        });

        Button btnSubmit = (Button) view.findViewById(R.id.submit_Edit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String Name = mStudent.getName();
                String ID = mStudent.getId();
                String StudNum = mStudent.getStudent_Num();
                String UnivName = mStudent.getUniversity();
                String Gender = mStudent.getGender();

                String PW = mStudent.getPW();
                String CurrentPwStr = CurrentPw.getText().toString();
                String NewPwStr = NewPw.getText().toString();
                String NewPwReStr = NewPw_Re.getText().toString();

                isResetAvailable = true;

                if(CurrentPwStr.length() == 0 || NewPwStr.length() == 0 || NewPwReStr.length() == 0) {
                    Toast.makeText(getActivity(), "정보를 모두 입력하여 주십시요.", Toast.LENGTH_SHORT).show();
                    isResetAvailable = false;
                } else if (!(PW.equals(CurrentPwStr))) {
                    Toast.makeText(getActivity(), "현재 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                    isResetAvailable = false;
                } else if (!(NewPwStr.equals(NewPwReStr))) {
                    Toast.makeText(getActivity(), "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    isResetAvailable = false;
                } else if (NewPwStr.equals(CurrentPwStr)) {
                    Toast.makeText(getActivity(), "새 비밀번호를 입력하여 주십시요.\n비밀번호가 변경되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    isResetAvailable = false;
                }

                if (chkString_Edit(Name, ID, NewPwStr, StudNum, UnivName, Gender, isResetAvailable)) {
                    Toast.makeText(getActivity(), "회원정보 수정 완료", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.cancel_Edit);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "수정 취소", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private boolean chkString_Edit(String Name, String ID, String PW, String StudNum, String UnivName, String Gender, boolean isResetAvailable) {
        String[] chk = {Name, ID, PW, StudNum, UnivName, Gender};

        for (int i = 0; i < 6; i++) {
            if (chk[i].length() <= 0) {

                Toast.makeText(getActivity(), "정보를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(!(Gender.equals("남") || Gender.equals("여"))) {
            return false;
        } else if(!isResetAvailable) {
            return false;
        }

        Update(Name, ID, PW, StudNum, UnivName, Gender);

        /*
        *  그 외에 다른 특수한 조건 사항이 있으면 추가할 것 (예: 특정 특수 문자 금지 등)
        *  (참인 것에 대해서 return true 하는 것이 아니라 거짓인 것에 대해서 return false 할 것)
        * */

        return true;
    }

    public void Update(String strName, String strId, String strPw, String strStuNum, String strSchoolName, String gender) {
        class UpdateTask extends AsyncTask<String, Void, String> {
            /*
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }
            */

            protected String doInBackground(String[] params) {
                // 파라미터를 받아오는 부분
                String name = (String) params[0];
                String id = (String) params[1];
                String password = (String) params[2];
                String studentNumber = (String) params[3];
                String schoolName = (String) params[4];
                String gender = (String) params[5];

                try {
                    // 서버에 넘겨주기 위한 데이터를 서버에서 읽을 수 있는 내용으로 인코딩하는 작업
                    String data = "";
                    data += URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("studentNumber", "UTF-8") + "=" + URLEncoder.encode(studentNumber, "UTF-8");
                    data += "&" + URLEncoder.encode("schoolName", "UTF-8") + "=" + URLEncoder.encode(schoolName, "UTF-8");
                    data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");

                    // 서버와 연결을 시도하는 부분
                    URL url = new URL(Variable.m_SERVER_URL + Variable.m_PHP_UPDATE);

                    URLConnection con = url.openConnection();

                    // 서버로 전달하는 데이터가 있는 경우에 outputstream을 이용하는 부분
                    con.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    // 서버 연결 후 데이터를 가져오는 부분이 있을때 사용하는 코드
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    // onPostExecute 부분으로 스트링을 전달함
                    return sb.toString().trim();
                } catch (Exception exception) {
                    return new String(exception.getMessage());
                }
            }

            /*
            }protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            */
        }

        UpdateTask updateTask = new UpdateTask();
        updateTask.execute(strName, strId, strPw, strStuNum, strSchoolName, gender);
    }

    public void Delete(String strId) {
        class DeleteTask extends AsyncTask<String, Void, String> {
            /*ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }
            */

            protected String doInBackground(String[] params) {
                String id = (String) params[0];

                try {
                    String data = "";
                    data += URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

                    URL url = new URL(Variable.m_SERVER_URL + Variable.m_PHP_DELETE);
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

            /*
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            */
        }

        DeleteTask deleteTask = new DeleteTask();
        deleteTask.execute(strId);
    }
}
