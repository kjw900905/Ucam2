package com.example.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

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

public class InActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected Student myInfo;

    String myJSON;
    JSONArray person = null;

    boolean doubleBackToExitPressedOnce = false; //두 번 뒤로가기 시 종료하는 지에 대한 여부를 판단하는 불 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        myInfo = (Student) intent.getSerializableExtra("myInfo");

        //SelectOne(myInfo.getId());
        ///positionArrayList = new ArrayList<String>();
        //Toast.makeText(getApplicationContext(), positionArrayList.get(0), Toast.LENGTH_SHORT).show();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        SelectOne(myInfo.getId(), toolbar);
/*
        NotDbMainFragment notdbMainFragment = new NotDbMainFragment();
        FragmentManager manager= getSupportFragmentManager();
        manager.beginTransaction().add(R.id.content_in, notdbMainFragment).addToBackStack(null).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/
    }

    public void set_Schedule_Button(View v) {
        /* TODO: 스케줄 설정 액티비티로 전환
        *  신규 .java 파일과 연동
        * */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Skips one activity to go back twice
            FragmentManager fm = getSupportFragmentManager();
            int backStackNum = fm.getBackStackEntryCount();
            if (backStackNum == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

                // 여기서 부터는 알림창의 속성 설정
                builder.setTitle("종료"); // 제목 설정
                builder.setMessage("UCam을 종료하시겠습니까?"); // 메세지 설정
                builder.setCancelable(false); // 뒤로 버튼 클릭시 취소 가능 설정
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (backStackNum > 1) {
                for (int i = backStackNum; i > 2; i--) {
                    fm.popBackStack();
                }
                super.onBackPressed();
            }
        }
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.in, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_time_table) {
            TimeTableFragment timeTableFragment = new TimeTableFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_in, timeTableFragment).addToBackStack(null).commit();

            Bundle bundle = new Bundle(1);
            bundle.putSerializable("myInfo", myInfo);
            timeTableFragment.setArguments(bundle);
            
        } else if (id == R.id.nav_edit_mem_info) {
            EditMemInfoFragment editMemInfoFragment = new EditMemInfoFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_in, editMemInfoFragment).addToBackStack(null).commit();

            Bundle bundle = new Bundle(1);
            bundle.putSerializable("myInfo", myInfo);
            editMemInfoFragment.setArguments(bundle);
        } else if (id == R.id.nav_matching) {
            MatchFragment matchFragment = new MatchFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_in, matchFragment).addToBackStack(null).commit();

            Bundle bundle = new Bundle(1);
            bundle.putSerializable("myInfo", myInfo);
            matchFragment.setArguments(bundle);
        } else if (id == R.id.nav_chat) {
            MyChatRoomFragment myChatRoomFragment = new MyChatRoomFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_in, myChatRoomFragment).commit();

            Bundle bundle = new Bundle(1);
            bundle.putSerializable("myInfo", myInfo);
            myChatRoomFragment.setArguments(bundle);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SelectOne(final String str_ID, final Toolbar toolbar) {
        class SelectOneTask extends AsyncTask<String, Void, String> {
            /*ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }
            */

            protected String doInBackground(String[] params) {
                String temp_ID = (String) params[0];
                try {
                    String data = "";
                    data += URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(temp_ID, "UTF-8");

                    URL url = new URL(Variable.m_SERVER_URL + Variable.m_PHP_CHECK_TIME_TABLE);
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
                check_ID_PW(toolbar);
            }
        }

        SelectOneTask selectOneTask = new SelectOneTask();
        selectOneTask.execute(str_ID);
    }

    public void check_ID_PW(Toolbar toolbar) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            person = jsonObj.getJSONArray("result");
            if (person.isNull(0)) {
                NotDbMainFragment notdbMainFragment = new NotDbMainFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().add(R.id.content_in, notdbMainFragment).addToBackStack(null).commit();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

            } else {

                TimeTableFragment timeTableFragment = new TimeTableFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_in, timeTableFragment).addToBackStack(null).commit();

                Bundle bundle = new Bundle(1);
                bundle.putSerializable("myInfo", myInfo);
                timeTableFragment.setArguments(bundle);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}