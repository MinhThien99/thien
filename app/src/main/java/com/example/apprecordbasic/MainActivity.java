package com.example.apprecordbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton listbtn , bntstop;
    private ImageButton bntrecord;
    private MediaRecorder mediaRecorder = null;
    private String recordFile;
    private Chronometer timer;
    private TextView filenameRecord;
    private int mRecordPromptCount = 0;
    private boolean mPauseRecording = true;

    private boolean isRecording = false;
    private boolean mStartRecording = true;
    long timeWhenPaused = 0;

    private static final int PEMISSION_CODE = 1000 ;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bntstop = findViewById(R.id.btnstop);
        filenameRecord = findViewById(R.id.record_filename);
        timer = findViewById(R.id.timerrecord);
        bntrecord = findViewById(R.id.recordbtn);
        listbtn = findViewById(R.id.record_list_btn);
        bntstop.setEnabled(false);


        bntrecord.setOnClickListener(this);
        listbtn.setOnClickListener(this);
        bntstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseRecord();
                mPauseRecording = !mPauseRecording;

            }
        });

    }

    private void onPauseRecord() {

        if(mPauseRecording){
            bntstop.setImageDrawable(getResources().getDrawable(R.drawable.playicon));
            bntrecord.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Recorder Pause...", Toast.LENGTH_LONG).show();

            timeWhenPaused = timer.getBase() - SystemClock.elapsedRealtime();
            timer.stop();

        }

        else {
            bntstop.setImageDrawable(getResources().getDrawable(R.drawable.pauseicon));
            bntrecord.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Recorder Recording...", Toast.LENGTH_LONG).show();

            timer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
            timer.start();

        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.record_list_btn:
                if (isRecording){
                    AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                    alBuilder.setPositiveButton("OKEY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stopRecording();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                bntrecord.setImageDrawable(getResources().getDrawable(R.drawable.micicon,null));
                            }
                            Intent intent = new Intent(MainActivity.this, AudioListActivity.class);
                            startActivity(intent);
                            isRecording = false;
                        }
                    });
                    alBuilder.setNegativeButton("CANCLE" , null);
                    alBuilder.setTitle("Audio still record");
                    alBuilder.setMessage("Are you sure you want to stop recording ?");
                    alBuilder.create().show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, AudioListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.recordbtn:
                if(isRecording){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        stopRecording();
                        bntrecord.setImageDrawable(getResources().getDrawable(R.drawable.micicon,null));
                    }
                    isRecording = false;
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startRecording();
                        bntrecord.setImageDrawable(getResources().getDrawable(R.drawable.stopicon, null));
                        isRecording = true;
                    }
                }
                break;
        }

    }

    private void startRecording() {
        bntstop.setEnabled(true);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        String recordPath = this.getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss" , Locale.CANADA);
        Date now = new Date();

        recordFile = "Recording_" + formatter.format(now) + ".mp3";

        filenameRecord.setText("Recording Filename:\n"+recordFile);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(recordPath +"/"+ recordFile);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private void stopRecording() {

        Toast.makeText(getApplicationContext(), "Recorder Stopping...", Toast.LENGTH_LONG).show();

        bntstop.setEnabled(false);

        timer.stop();
        filenameRecord.setText("Recording Stop , Saved File: \n" + recordFile);

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

    }
}
