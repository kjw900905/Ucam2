package com.example.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

import static com.example.Activity.R.id.listView;

public class SearchSchoolDialog extends Activity {
    // php 주소 구성 : "http://(서버 주소)/(php 파일명 + 확장자명)"
    // php 주소 형식 : "http://xxx.xxx.xxx.xxx/xxxxx.php"
    // php 주소 예시 : "http://221.148.86.18/SelectAll.php"

    private String myJSON;
    private String temp_School_Name;
    private String school_Name;
    private String TAG_RESULTS = "result";
    private String php_school_Name = "universityName";

    private JSONArray peoples = null;

    private ArrayList<HashMap<String, String>> personList;

    private ListView list;

    EditText edtId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_school_dialog);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 90%
        int height = (int) (display.getHeight() * 0.7);//Display 사이즈의 70%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
        this.setFinishOnTouchOutside(false);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClickSelectOne(View v) {
        list = (ListView) findViewById(listView);
        personList = new ArrayList<HashMap<String, String>>();
        edtId = (EditText) findViewById(R.id.edtId);
        String strId = edtId.getText().toString();
        //Toast.makeText(getApplicationContext(), strId, Toast.LENGTH_SHORT).show();
        if (strId.equals("") || strId == null)  return;
        SelectOne(strId);
    }

    public void SelectOne(String strId) {
        class SelectOneTask extends AsyncTask<String, Void, String> {
            /*ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }
            */

            protected String doInBackground(String[] params) {
                String temp_University_Name = (String) params[0];

                try {
                    String data = "";
                    data += URLEncoder.encode(php_school_Name, "UTF-8") + "=" + URLEncoder.encode(temp_University_Name, "UTF-8");

                    URL url = new URL(Variable.m_SERVER_URL + Variable.m_PHP_SELECTONE_SEARCH_SCHOOL);
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
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                showList();
            }
        }

        SelectOneTask selectOneTask = new SelectOneTask();
        selectOneTask.execute(strId);
    }

    public void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i<peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(php_school_Name);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(php_school_Name, id);
                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    SearchSchoolDialog.this, personList, R.layout.list_item,
                    new String[]{php_school_Name},
                    new int[]{R.id.id}
            );

            list.setAdapter(adapter);
            list.setOnItemClickListener(itemClickListenerOfSchoolList);
            //personList.clear();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    AdapterView.OnItemClickListener itemClickListenerOfSchoolList = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id){

            temp_School_Name = adapterView.getAdapter().getItem(pos).toString();
            school_Name =  temp_School_Name.substring(16, temp_School_Name.length()-1);
            edtId.setText(school_Name);
            Toast.makeText(getApplicationContext(), school_Name+"이 선택되었습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    public void setUniversity(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        intent.putExtra("name", school_Name);
        this.setResult(RESULT_OK, intent);
        finish();
    }
}