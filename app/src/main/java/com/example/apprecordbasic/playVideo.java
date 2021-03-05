package com.example.apprecordbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import static com.example.apprecordbasic.recordVideo.videoModelArrayList;

public class playVideo extends AppCompatActivity {

    VideoView videoView;
    ImageView prev, next, pause;
    SeekBar seekBar;
    TextView current, total;
    LinearLayout showProcess;
    Handler mHandler, handler;
    int video_index = 0;
    double current_pos, total_duration;
    boolean isVisible = true;
    RelativeLayout relativeLayout;
    private MediaPlayer mediaPlayer = null;
    public static int PERMISSION_READ = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        setVideo();

    }

    private void setVideo() {
        videoView = findViewById(R.id.video_view);
        prev = (ImageView) findViewById(R.id.prev);
        next = (ImageView) findViewById(R.id.next);
        pause = (ImageView) findViewById(R.id.pause);
        seekBar = (SeekBar) findViewById(R.id.seekbar_video);
        current = (TextView) findViewById(R.id.current);
        total = (TextView) findViewById(R.id.total);
        showProcess = (LinearLayout) findViewById(R.id.show_process);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);

        video_index = getIntent().getIntExtra("pos",0);
        mHandler = new Handler();
        handler = new Handler();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video_index ++;
                if(video_index < (videoModelArrayList.size())){
                    playThisVideo(video_index);
                }
                else {
                    video_index = 0;
                    playThisVideo(video_index);
                }
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setvideoProgress();
            }
        });

        playThisVideo(video_index);
        prevVideo();
        nextVideo();
        setPause();
        hidelayout();

    }

    private void hidelayout() {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showProcess.setVisibility(View.GONE);
                isVisible = false;
            }
        };

        handler.postDelayed(runnable, 5000);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(runnable);
                if(isVisible){
                    showProcess.setVisibility(View.GONE);
                    isVisible = false;
                }
                else {
                    showProcess.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(runnable,5000);
                    isVisible = true;
                }
            }
        });

    }

    private void setPause() {

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()){
                    videoView.pause();
                    pause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                }
                else {
                    videoView.start();
                    pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });

    }

    private void nextVideo() {

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(video_index < (videoModelArrayList.size() - 1)){
                    video_index++;
                }
                else {
                    video_index = 0;
                }
                playThisVideo(video_index);
            }
        });

    }

    private void prevVideo() {

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(video_index > 0){
                    video_index -- ;
                }
                else {
                    video_index = videoModelArrayList.size() - 1;
                }
                playThisVideo(video_index);
            }
        });

    }

    private void setvideoProgress() {
        //get the video duration

        current_pos = videoView.getCurrentPosition();
        total_duration = videoView.getDuration();

        //display duration
        total.setText(timeConversion((long) total_duration));
        current.setText(timeConversion((long) current_pos));
        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    current_pos = videoView.getCurrentPosition();
                    current.setText(timeConversion((long) current_pos));
                    seekBar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                }
                catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        };

        handler.postDelayed(runnable, 1000);

        //Seekbar change listner
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                videoView.seekTo((int) current_pos);
            }
        });

    }

    public void playThisVideo(int pos){

        try{
            videoView.setVideoURI(videoModelArrayList.get(pos).getVideoUri());
            videoView.start();
            pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            video_index = pos;
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    // Time Conversion
    public String timeConversion(long value) {
        String songTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            songTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            songTime = String.format("%02d:%02d", mns, scs);
        }
        return songTime;
    }

}
















