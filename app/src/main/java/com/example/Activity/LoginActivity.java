package com.example.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Beans.Student;
import com.example.Beans.Variable;
import com.example.kjw90.ucam.FindIdPwActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.example.Activity.R.id.finding_Button;
import static com.example.Activity.R.id.sign_In_Button;
import static com.example.Activity.R.id.sign_Up_Button;

public class LoginActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false; //두 번 뒤로가기 시 종료하는 지에 대한 여부를 판단하는 불 변수

    public Button log_In_Button;

    private TextView join_Button;
    private TextView find_id_pw_Button;

    private String TAG_RESULTS = "result";
    private String php_ID = "id";
    private String myJSON;

    private EditText username;
    private EditText password;

    JSONArray person = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username_Input);
        password = (EditText)findViewById(R.id.password_Input);

        log_In_Button = (Button)findViewById(sign_In_Button);
        log_In_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), Environment.getExternalStorageState(), Toast.LENGTH_SHORT).show();
                //Limited Access to clarify whether the information is corresponding with DB

                if(chk_ID_PW(username, password)) {

                    EditText edt_Username_Input = (EditText) findViewById(R.id.username_Input);
                    EditText edt_Password_Input = (EditText) findViewById(R.id.password_Input);
                    String str_Username_Input = edt_Username_Input.getText().toString();
                    String str_Password_Input = edt_Password_Input.getText().toString();

                    //Intent intent = new Intent(getApplicationContext(), InActivity.class);
                    //startActivity(intent);
                    SelectOne(str_Username_Input, str_Password_Input);
                }
            }
        });

        join_Button = (TextView)findViewById(sign_Up_Button);
        join_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        find_id_pw_Button = (TextView)findViewById(finding_Button);
        find_id_pw_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindIdPwActivity.class);
                startActivity(intent);
            }
        });
    }

    public void SelectOne(String str_Username_Input, String str_Password_Input) {
        class SelectOneTask extends AsyncTask<String, Void, String> {
            /*ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }
            */

            protected String doInBackground(String[] params) {
                String temp_ID = (String) params[0];
                String temp_PW = (String) params[1];

                try {
                    String data = "";
                    data += URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(temp_ID, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(temp_PW, "UTF-8");

                    URL url = new URL(Variable.m_SERVER_URL + Variable.m_PHP_CHECK_ID_PW);
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
                check_ID_PW();
            }
        }

        SelectOneTask selectOneTask = new SelectOneTask();
        selectOneTask.execute(str_Username_Input, str_Password_Input);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "한번 더 뒤로가기를 누르시면 종료합니다", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void check_ID_PW(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            person = jsonObj.getJSONArray("result");

            if(person.optString(0, "false").equals("false")) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setMessage("아이디, 비밀번호를 확인하십시오.");
                alert.show();
            } else {
                JSONObject c = person.getJSONObject(0);

                String name = c.getString("name");
                String id = c.getString("id");
                String password = c.getString("password");
                String studentNumber = c.getString("studentNumber");
                String schoolName = c.getString("schoolName");
                String gender = c.getString("gender");

                Intent intent = new Intent(getApplicationContext(), InActivity.class);
                intent.putExtra("myInfo", new Student(name, id, password, studentNumber, schoolName, gender));
                finish();
                startActivity(intent);
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean chk_ID_PW(EditText id, EditText pw) {
        String ID = id.getText().toString();
        String PW = pw.getText().toString();

        if(ID.length() == 0 || PW.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}