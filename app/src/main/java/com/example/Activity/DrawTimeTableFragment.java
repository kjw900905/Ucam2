package com.example.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class DrawTimeTableFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        DrawTimeTable drawTimeTable = new DrawTimeTable(view.getContext());
        WindowManager.LayoutParams wmLayoutParams = getActivity().getWindow().getAttributes();
        LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(wmLayoutParams.width, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 550, getResources().getDisplayMetrics()));
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.TimeTableLayout);
        linearLayout.addView(drawTimeTable, 0, llLayoutParams);

        return view;
    }
}
