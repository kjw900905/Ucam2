package com.example.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;


public class SetUpFragment extends Fragment {

    private Button set_Color;
    public SetUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final InActivity inActivity = (InActivity)getActivity();

        Bundle extra = getArguments();
        final int position = extra.getInt("position");
        final View view = inflater.inflate(R.layout.fragment_set_up, container, false);
        //View view = inflater.inflate(R.layout.color_test, container, false);




        RadioGroup rg = (RadioGroup)view.findViewById(R.id.select_Color);
        int radioCheck = rg.getCheckedRadioButtonId();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                RadioButton rb = (RadioButton) view.findViewById(checkedID);
                ColorDrawable color = (ColorDrawable)rb.getBackground();
                int selectedColor = color.getColor();
                RelativeLayout mLayout = (RelativeLayout)view.findViewById(R.id.set_up_layout);
                mLayout.setBackgroundColor(selectedColor);
            }
        });

//        RadioButton rb = (RadioButton) view.findViewById(rg.getCheckedRadioButtonId());
//        String id = rb.getText().toString();
 //       Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();

        //TextView testColor = (TextView)view.findViewById(R.id.color_Test);

        //testColor.setBackgroundColor(rb.getDrawingCacheBackgroundColor());


        return view;
    }
    /*
    public void onClick(View v){
        switch (v.getId()){
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
            case R.id.color_Black:
                TextView colorText = (TextView)v.findViewById(R.id.color_Black);
                ColorDrawable color = (ColorDrawable)colorText.getBackground();
                int selectedColor = color.getColor();
                //가져온 색깔 디비에 저장
        }

    }
    */
}
