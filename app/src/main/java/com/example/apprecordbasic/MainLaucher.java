package com.example.apprecordbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainLaucher extends AppCompatActivity implements View.OnClickListener {

    ImageButton imgRecordVideo, imgRecordAudio, ListVideo;
    private final int VIDEO_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_laucher);

        imgRecordVideo = findViewById(R.id.bnt_main_mic_recordlaucher);
        imgRecordAudio = findViewById(R.id.bnt_record_Videolaucher);
        ListVideo = findViewById(R.id.bnt_list_VideoLaucher);

        imgRecordAudio.setOnClickListener(this);
        imgRecordVideo.setOnClickListener(this);
        ListVideo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnt_main_mic_recordlaucher:
                Intent camera_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                // File video_file = getFilepath();
                //Uri Video_uri = Uri.fromFile(video_file);
                if (camera_intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(camera_intent, VIDEO_REQUEST_CODE);
                }
                break;
            case R.id.bnt_record_Videolaucher:
                Intent intent1 = new Intent(MainLaucher.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.bnt_list_VideoLaucher:
                Intent intent2 = new Intent(MainLaucher.this, recordVideo.class);
                startActivity(intent2);
                break;
        }
    }
}