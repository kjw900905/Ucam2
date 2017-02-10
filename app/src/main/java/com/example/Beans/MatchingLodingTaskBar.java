package com.example.Beans;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

/**
 * Created by kjw90 on 2017-02-11.
 */

public class MatchingLodingTaskBar extends AsyncTask<Void, Void, Void>{
    ProgressDialog dialog;
    private Activity m_activity;
    private RealTimeMatching realTimeMatching;
    private String m_ChattingNumber;
    private String m_DetailedInterests;
    private Student mStudent;

    public MatchingLodingTaskBar(Activity activity, String chattingNumber, String detailedInterests, Student student){
        m_activity = activity;
        realTimeMatching = new RealTimeMatching();
        m_ChattingNumber = chattingNumber;
        m_DetailedInterests = detailedInterests;
        mStudent = student;
    }

    @Override
    protected void onPreExecute() {
        //Log.e("sibal", "sibal");
        super.onPreExecute();
        dialog = new ProgressDialog(m_activity);
        dialog.setMessage("Loading...");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        realTimeMatching.setChattingNumber(m_ChattingNumber);
        realTimeMatching.setDetailedInterests(m_DetailedInterests);
        realTimeMatching.setStudent(mStudent);
        realTimeMatching.setActivity(m_activity);
        realTimeMatching.setProgressDialog(dialog);
        realTimeMatching.insertMatchingId();

        return null;
    }

    protected void onPostExecute(String result) {

        //myJSON = result;
        //check_ID_PW();
    }

    @Override
    protected void onCancelled() {
        //called on ui thread
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
    }
};
