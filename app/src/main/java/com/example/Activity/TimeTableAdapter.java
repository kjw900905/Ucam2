package com.example.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.Beans.TimeTableDetail;
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

public class TimeTableAdapter extends BaseAdapter {
    private String user_ID;
    private JSONArray getPosition;
    private String myJSON;
    private Context context;
    private int rootViewHeight;
    private int dayViewHeight;
    private int selectedPosition = -1;
    private ArrayList<TimeTableDetail> arrayTimeTableDetail = new ArrayList<TimeTableDetail>();
    private TimeTableDetail timeTableDetail;

    public TimeTableAdapter(Context context, int rootViewHeight, int dayViewHeight, String user_ID) {
        this.context = context;
        this.rootViewHeight = rootViewHeight;
        this.dayViewHeight = dayViewHeight;

        this.user_ID = user_ID;

        //notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        Toast.makeText(convertView.getContext(), ""+position, Toast.LENGTH_SHORT).show();

        View gridView;


        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate(R.layout.timetable_layout, null);
/*
            if(position==2){
                //gridView.setBackgroundColor(Color.BLUE);
                //Toast.makeText(gridView.getContext(), "sds"+ position, Toast.LENGTH_SHORT).show();
                //TextView textView = (TextView)gridView.findViewById(R.id.grid_TextView);
                //textView.setBackgroundColor(Color.BLUE);
            }
*/
            for(int i=0; i<arrayTimeTableDetail.size(); i++){
                if(arrayTimeTableDetail.get(i).getfield().equals("Y")){
                    Log.d(arrayTimeTableDetail.get(i).getposition(), "실행");
                    Log.w(arrayTimeTableDetail.get(i).getposition(), "실행");
                    Log.i(arrayTimeTableDetail.get(i).getposition(), "실행");
                    Log.e(arrayTimeTableDetail.get(i).getposition(), "실행");
                    //Toast.makeText(gridView.getContext(), arrayTimeTableDetail.get(i).getposition(), Toast.LENGTH_SHORT).show();
                    TextView textView = (TextView)gridView.findViewById(R.id.grid_TextView);
                    textView.setBackgroundColor(Color.LTGRAY);
                }
            }


            int cellHeight = (rootViewHeight - dayViewHeight) / 12 - dpToPx(1);
            gridView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cellHeight));
            //set value into textview
            TextView textView = (TextView) gridView.findViewById(R.id.grid_TextView);

        } else {
            gridView = (View) convertView;
        }
        TextView textView = (TextView)gridView.findViewById(R.id.grid_TextView);
        textView.setText("");

        SelectOne(user_ID, gridView, position);
        SelectReservation(user_ID, gridView, position);

        //positionArrayList = new ArrayList<String>();
        //String test = positionArrayList.get(0);
        //Toast.makeText(gridView.getContext(), positionArrayList.get(0), Toast.LENGTH_SHORT).show();
        /*
        for(int i = 0 ; i < positionArrayList.size() ; i++ ){
            if(position == Integer.parseInt(positionArrayList.get(i))){
                TextView textView = (TextView)gridView.findViewById(R.id.grid_TextView);
                textView.setBackgroundColor(Color.BLUE);
            }
        }
        */

        /*
        for(int i=0; i<arrayTimeTableDetail.size(); i++){
            if(arrayTimeTableDetail.get(i).getfield().equals("Y")){
                Toast.makeText(gridView.getContext(), arrayTimeTableDetail.get(i).getposition(), Toast.LENGTH_SHORT).show();
                TextView textView = (TextView)gridView.findViewById(R.id.grid_TextView);
                textView.setBackgroundColor(Color.BLUE);
            }
        }
        */

/*
        if(position==2){
            //gridView.setBackgroundColor(Color.BLUE);
            //Toast.makeText(gridView.getContext(), "sds"+ position, Toast.LENGTH_SHORT).show();
            TextView textView = (TextView)gridView.findViewById(R.id.grid_TextView);
            textView.setBackgroundColor(Color.BLUE);
        }
        */

        return gridView;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getCount() {
        return 60;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setSelectedPosition(int position) {

        selectedPosition = position;
    }

    public void setArrayTimeTableDetail(ArrayList<TimeTableDetail> arrayTimeTableDetail){
        this.arrayTimeTableDetail = arrayTimeTableDetail;
    }

    public void SelectOne(String str_User_ID, final View gridView, final int viewPosition) {
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
                getPosition(gridView, viewPosition);
            }
        }

        SelectOneTask selectOneTask = new SelectOneTask();
        selectOneTask.execute(str_User_ID);
    }

    public void getPosition(View gridView, int viewPosition) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            getPosition = jsonObj.getJSONArray("result");
            for (int i = 0; i < getPosition.length(); i++) {
                    JSONObject c = getPosition.getJSONObject(i);
                String position = c.getString("position");
                int selectPosition = Integer.parseInt(position);
                if(viewPosition == selectPosition) {
                    TextView textView = (TextView) gridView.findViewById(R.id.grid_TextView);
                    textView.setBackgroundColor(Color.LTGRAY);
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void SelectReservation(String str_User_ID, final View gridView, final int viewPosition) {
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

                    URL url = new URL(Variable.m_SERVER_URL + Variable.m_PHP_SELECT_RESERVATION_POSITION);
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
                getReservationPosition(gridView, viewPosition);
            }
        }

        SelectOneTask selectOneTask = new SelectOneTask();
        selectOneTask.execute(str_User_ID);
    }

    public void getReservationPosition(View gridView, int viewPosition) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            getPosition = jsonObj.getJSONArray("result");
            for (int i = 0; i < getPosition.length(); i++) {
                JSONObject c = getPosition.getJSONObject(i);
                String position = c.getString("position");
                int selectPosition = Integer.parseInt(position);
                if(viewPosition == selectPosition) {
                    TextView textView = (TextView) gridView.findViewById(R.id.grid_TextView);
                    textView.setBackgroundColor(Color.BLUE);
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}