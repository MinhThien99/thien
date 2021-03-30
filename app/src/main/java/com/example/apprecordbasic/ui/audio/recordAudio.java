package com.example.apprecordbasic.ui.audio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.apprecordbasic.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class recordAudio extends AppCompatActivity implements View.OnClickListener {

    private ImageButton listbtn, bntstop, video_list;
    private ImageButton bntrecord;
    private MediaRecorder mediaRecorder = null;
    private String recordFile;
    private Chronometer timer;
    private TextView filenameRecord;
    private int mRecordPromptCount = 0;
    private boolean mPauseRecording = true;
    private ImageButton btnvideorecord;

    private boolean isRecording = false;
    private boolean mStartRecording = true;
    long timeWhenPaused = 0;

    private final String recordPermission = Manifest.permission.RECORD_AUDIO;

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
            //filenameRecord.setText(getString(R.string.resume_record).toUpperCase());

            Toast.makeText(getApplicationContext(), "Recorder Pause...", Toast.LENGTH_LONG).show();

            timeWhenPaused = timer.getBase() - SystemClock.elapsedRealtime();
            timer.stop();

        }

        else {
            bntstop.setImageDrawable(getResources().getDrawable(R.drawable.pauseicon));
            bntrecord.setEnabled(true);
            //filenameRecord.setText(getString(R.string.record_in_progress).toUpperCase());

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
                            Intent intent = new Intent(recordAudio.this, AudioListActivity.class);
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
                    Intent intent = new Intent(recordAudio.this, AudioListActivity.class);
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
                /*
            case R.id.record_video:
                Intent camera_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
               // File video_file = getFilepath();
                //Uri Video_uri = Uri.fromFile(video_file);
                if (camera_intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(camera_intent, VIDEO_REQUEST_CODE);
                }
                break;
            case R.id.video_list:
                Intent intent = new Intent(MainActivity.this, recordVideo.class);
                startActivity(intent);
                break;

                 */
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RESULT_OK && resultCode == 1) {
                Uri uriVideo = data.getData();
                VideoView videoView = new VideoView(this);
                videoView.setVideoURI(uriVideo);

            }

    }

/*
    private File getFilepath() {

        File forder = new File("sdcard/video_app");
        if(!forder.exists()){
            forder.mkdir();
        }
        File video_File = new File(forder, "sample_video.mp4");
        return video_File;
    }
 */

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

        bntstop.setEnabled(false);

        timer.stop();
        filenameRecord.setText("Recording Stop, Saved File: \n" + recordFile);

        mediaRecorder.stop();
//        mediaRecorder.release();
        mediaRecorder = null;

    }
}
