package com.example.apprecordbasic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class recordVideo extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton btnBack;
    public static ArrayList<videoModel> videoModelArrayList;
    public static final int PERMISSION_READ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        recyclerView = findViewById(R.id.recycle_list_video);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(recordVideo.this, MainActivity.class);
                startActivity(intent);
            }
        });
        
        videoList();
    }

    private void videoList() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        videoModelArrayList = new ArrayList<>();
        getVideos();

    }

    private void getVideos() {

        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri , null , null, null, null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                videoModel videoModel = new videoModel();
                videoModel.setVideoTitle(title);
                videoModel.setVideoUri(Uri.parse(data));
                videoModel.setVideoDuration(timeConvertion(Long.parseLong(duration)));
                videoModelArrayList.add(videoModel);
            }
            while (cursor.moveToNext());
        }

        videoAdapter videoAdapter = new videoAdapter(this, videoModelArrayList);
        recyclerView.setAdapter(videoAdapter);

        videoAdapter.setOnItemClickListener(new videoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                Intent intent = new Intent(getApplicationContext(), playVideo.class);
                intent.putExtra("pos", pos);
                startActivity(intent);
            }
        });

        /*

        videoAdapter.setOnItemLongClickListener(new videoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int pos, View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

                alertDialog.setMessage("Do you want to delete this file ?");
                alertDialog.setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                videoModelArrayList.remove(pos);
                                Toast.makeText(getApplicationContext(), "Delete file successful",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = alertDialog.create();
                dialog.setTitle("DELETE");
                dialog.show();

            }
        });

         */
    }

    private String timeConvertion(long parseLong) {

        String videoTime;
        int dur = (int) parseLong;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if(hrs > 0 ){
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        }
        else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }


}