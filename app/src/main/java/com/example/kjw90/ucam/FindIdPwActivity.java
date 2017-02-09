package com.example.kjw90.ucam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.Activity.R;

public class FindIdPwActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_pw);

        Button cancel_Button = (Button)findViewById(R.id.finding_cancel_Button);
        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Set Up Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
