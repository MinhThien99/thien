package com.example.apprecordbasic.ui.audio;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecordbasic.R;
import com.example.apprecordbasic.adapter.audioAdapter;

import java.io.File;

public class fragmentAudioPlayer extends Fragment implements View.OnClickListener  {

    private ConstraintLayout playsheet;
    RecyclerView recyclerView;
    private audioAdapter audio;
    private File[] files;
    private File file_play;
    private int file_cur_pos;

    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    private ImageButton playbtn ,nextbtn, previuosbtn;
    private Button btn_close_media;
    private TextView playerfilename;
    private SeekBar playSeekbar;
    private Handler seekbarHandler;
    private Runnable updateseekbar;

    public fragmentAudioPlayer(File[] files, int file_cur_pos){
        this.files = files;
        this.file_cur_pos = file_cur_pos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playsheet = view.findViewById(R.id.playersheet);
        recyclerView = view.findViewById(R.id.audio_list_view);

        playbtn = view.findViewById(R.id.player_play_btn);
        playSeekbar = view.findViewById(R.id.player_seekbar);
        playerfilename = view.findViewById(R.id.player_filename);
        nextbtn = view.findViewById(R.id.nextbtn);
        previuosbtn = view.findViewById(R.id.previourbtn);
        btn_close_media = view.findViewById(R.id.btn_close_media);

        String[] file_path = (files[file_cur_pos]).getAbsolutePath().split("/");
        playerfilename.setText(file_path[file_path.length-1]);
        Log.d("FILE NAME", file_path[file_path.length-1]);
        btn_close_media.setOnClickListener(this);

        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        files = directory.listFiles();

        playaudio(files[file_cur_pos]);

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    pauseAudio();
                }
                else {
                   // if(file_play != null) {
                        resumeAudio();
                   // }
                }
            }
        });

        playSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int process = seekBar.getProgress();
                mediaPlayer.seekTo(process);
                resumeAudio();
            }
        });

        nextbtn.setOnClickListener(this);
        previuosbtn.setOnClickListener(this);

    }



    private void pauseAudio(){
        mediaPlayer.pause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playbtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_media_play, null));
            isPlaying = false;
            seekbarHandler.removeCallbacks(updateseekbar);
        }

    }

    private void resumeAudio(){
        mediaPlayer.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playbtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_media_pause, null));
            isPlaying = true;
            updareRunnable();
            seekbarHandler.postDelayed(updateseekbar, 0);
        }
    }

    private void stopAudio() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playbtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_media_play, null));
            isPlaying = false;
            mediaPlayer.stop();
            try {
                Thread.sleep(2000);
                mediaPlayer.reset();
                playaudio(files[file_cur_pos]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            seekbarHandler.removeCallbacks(updateseekbar);
        }
    }

    private void playaudio(File filetoplay) {

        mediaPlayer = new MediaPlayer();
//        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(filetoplay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playbtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_media_pause, null));
//            playerfilename.setText(filetoplay.getName());
            isPlaying = true;
        }


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
            }
        });

        playSeekbar.setMax(mediaPlayer.getDuration());
        seekbarHandler = new Handler();
        updareRunnable();
        seekbarHandler.postDelayed(updateseekbar,0);
    }

    private void updareRunnable() {

        updateseekbar = new Runnable() {
            @Override
            public void run() {
                playSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isPlaying) {
            stopAudio();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close_media){
            if(isPlaying){
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.reset();
                seekbarHandler.removeCallbacks(updateseekbar);
            }
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
//            boolean success = renameFile(files[file_cur_pos], "thien");
//            Log.d("RENAME FILE SUCESS", Boolean.toString(success));
        }
        else if(id == R.id.nextbtn){

            if(isPlaying) {
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.reset();
                seekbarHandler.removeCallbacks(updateseekbar);
                if (file_cur_pos < files.length - 1) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.media_container, new fragmentAudioPlayer(files, ++file_cur_pos), null)
                            .commit();
                } else {
                    file_cur_pos = 0;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.media_container, new fragmentAudioPlayer(files, file_cur_pos), null)
                            .commit();
                }
            }
            else {
                if (file_cur_pos < files.length - 1) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.media_container, new fragmentAudioPlayer(files, ++file_cur_pos), null)
                            .commit();
                } else {
                    file_cur_pos = 0;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.media_container, new fragmentAudioPlayer(files, file_cur_pos), null)
                            .commit();
                }
            }
        }
        else if(id == R.id.previourbtn){
            if(isPlaying) {
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.reset();
                seekbarHandler.removeCallbacks(updateseekbar);
                if (file_cur_pos > 0) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.media_container, new fragmentAudioPlayer(files, --file_cur_pos), null)
                            .commit();
                } else {
                    file_cur_pos = files.length - 1;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.media_container, new fragmentAudioPlayer(files, file_cur_pos), null)
                            .commit();
                }
            }
            else {
                if (file_cur_pos > 0) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.media_container, new fragmentAudioPlayer(files, --file_cur_pos), null)
                            .commit();
                } else {
                    file_cur_pos = files.length - 1;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.media_container, new fragmentAudioPlayer(files, file_cur_pos), null)
                            .commit();
                }
            }

        }
    }

    public static boolean renameFile(File file, String new_name){
        String[] arr_old_name = file.getAbsolutePath().split("/");
        StringBuilder new_file_name = new StringBuilder();
        for (int i = 0; i < arr_old_name.length - 1; i ++){
            new_file_name.append("/").append(arr_old_name[i]);
        }
        new_file_name.append("/").append(new_name).append(".mp3");
        File old_file = new File(file.getAbsolutePath());
        File new_file = new File(new_file_name.toString());
        Log.d("OLD FILE", old_file.getAbsolutePath());
        Log.d("NEW FILE", new_file.getAbsolutePath());
        return old_file.renameTo(new_file);
    }

}
