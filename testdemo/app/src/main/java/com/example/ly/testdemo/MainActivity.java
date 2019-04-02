package com.example.ly.testdemo;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
    private final long ONE_DAY = 24 * 60 * 60 * 1000;
    private final long ONE_MIN = 60 * 1000;
    private final long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
    String TAG = "lylog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testAPPTimeLimited();

    }

    private void testAPPTimeLimited() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String installDate = preferences.getString("InstallDate", null);
        if (installDate == null) {
            SharedPreferences.Editor editor = preferences.edit();
            Date now = new Date();
            String dateString = formatter.format(now);
            editor.putString("InstallDate", dateString);
            Log.d(TAG, "dateString: "+dateString);
            editor.commit();
        } else {
            Date before = null;
            try {
                before = (Date) formatter.parse(installDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date now = new Date();
            long diff = now.getTime() - before.getTime();
            long days = diff / ONE_DAY;
            long min = diff / ONE_MIN;
            Log.d(TAG, "now: "+now +"  before"+before+" min ="+min);
            if (min > 1) {
                Log.d(TAG, "testAPPTimeLimited: dilog");
                showTestDialog();
                //Toast.makeText(this,"time over",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        testAPPTimeLimited();
        super.onResume();
    }

    private void showTestDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle("测试apk的使用期限为7天，请按确定结束");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        normalDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

            @Override
            public void onCancel(DialogInterface dialog) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        normalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        //normalDialog.setCancelable(false);
        normalDialog.show();
    }
}
