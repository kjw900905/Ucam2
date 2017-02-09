package com.example.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.Beans.Variable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, id, pw, studNum, univName, School_Name;
    private Button Submit_Button;
    private RadioGroup rg;
    private static final int LAUNCHED_ACTIVITY = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            String name = data.getStringExtra("name");
            School_Name.setText(name);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        rg = (RadioGroup)findViewById(R.id.rdgroup);
        School_Name = (EditText) findViewById(R.id.input_School_Name);
        School_Name.setFocusable(false);
        School_Name.setClickable(false);

        Submit_Button = (Button)findViewById(R.id.submit);
        Submit_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                name = (EditText) findViewById(R.id.input_Name);
                id = (EditText) findViewById(R.id.input_Id);
                pw = (EditText) findViewById(R.id.input_Pw);
                studNum = (EditText) findViewById(R.id.input_Stu_Num);
                univName = (EditText) findViewById(R.id.input_School_Name);

                int radioCheck = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)findViewById(radioCheck);

                String Name = name.getText().toString();
                String ID = id.getText().toString();
                String PW = pw.getText().toString();
                String StudNum = studNum.getText().toString();
                String UnivName = univName.getText().toString();


                if (chkString(Name, ID, PW, StudNum, UnivName)) {
                    Toast.makeText(getApplicationContext(), "회원가입 완료 ", Toast.LENGTH_LONG).show();

                    //이부분에 DB구문을 써주면 됨.
                    //Insert(Name, ID, PW, StudNum, UnivName); 이 형식대로 하면 됨.
                    EditText edtName = (EditText) findViewById(R.id.input_Name);
                    EditText edtId = (EditText) findViewById(R.id.input_Id);
                    EditText edtPw = (EditText) findViewById(R.id.input_Pw);
                    EditText edtStuNum = (EditText) findViewById(R.id.input_Stu_Num);
                    EditText edtSchoolName = (EditText) findViewById(R.id.input_School_Name);

                    String strName = edtName.getText().toString();
                    String strId = edtId.getText().toString();
                    String strPw = edtPw.getText().toString();
                    String strStuNum = edtStuNum.getText().toString();
                    String strSchoolName = edtSchoolName.getText().toString();
                    String gender = rb.getText().toString();

                    Insert(strName, strId, strPw, strStuNum, strSchoolName, gender);

                    finish();
                }
            }
        });

        // Toast.makeText(getApplicationContext(), name2, Toast.LENGTH_SHORT).show();
    }


    public void Insert(String strName, String strId ,String strPw, String strStuNum, String strSchoolName, String gender) {
        class InsertTask extends AsyncTask<String, Void, String> {
            /*ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }
            */

            protected String doInBackground(String[] params) {
                String name = (String) params[0];
                String id = (String) params[1];
                String password = (String) params[2];
                String studentNumber = (String) params[3];
                String schoolName = (String) params[4];
                String gender = (String)params[5];

                try {
                    String data = "";
                    data += URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("studentNumber", "UTF-8") + "=" + URLEncoder.encode(studentNumber, "UTF-8");
                    data += "&" + URLEncoder.encode("schoolName", "UTF-8") + "=" + URLEncoder.encode(schoolName, "UTF-8");
                    data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");

                    URL url = new URL(Variable.m_SERVER_URL + Variable.m_PHP_INSERT_PERSONAL_INFORMATION);
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

        InsertTask insertTask = new InsertTask();
        insertTask.execute(strName, strId, strPw, strStuNum, strSchoolName, gender);
    }

    public void search_Button(View v) {

        //Toast.makeText(getApplicationContext(), Sex, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SearchSchoolDialog.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, LAUNCHED_ACTIVITY);

        //Toast.makeText(getApplicationContext(), "검색중...", Toast.LENGTH_SHORT).show();

        /* TODO: 검색 화면으로 넘어가기
        *  1) DB와 연동하여 검색
        *  2) 검색을 위한 액티비티 따로 형성
        * */
    }

    private boolean chkString(String Name, String ID, String PW, String StudNum, String UnivName) {
        int[] chk = {Name.length(), ID.length(), PW.length(), StudNum.length(), UnivName.length()};

        for (int i = 0; i < chk.length; i++) {
            if (chk[i] <= 0) {
                Toast.makeText(getApplicationContext(), "정보를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        /*
        *  그 외에 다른 특수한 조건 사항이 있으면 추가할 것 (예: 특정 특수 문자 금지 등)
        *  (참인 것에 대해서 return true 하는 것이 아니라 거짓인 것에 대해서 return false 할 것)
        * */

        return true;
    }

    public void cancel_Button(View v) {
        Toast.makeText(getApplicationContext(), "가입 취소", Toast.LENGTH_SHORT).show();
        finish();
    }
}