package com.example.apprecordbasic.ui.audio;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecordbasic.R;
import com.example.apprecordbasic.adapter.audioAdapter;

import java.io.File;

public class AudioListActivity extends AppCompatActivity implements audioAdapter.onItemListClick{

    public static RecyclerView recyclerView;
    private audioAdapter audio;
    private File[] files;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_list_activity);

        recyclerView = findViewById(R.id.audio_list_view);

        String path = getExternalFilesDir("/").getAbsolutePath();
        Log.d("PATH IS", path);
        File directory = new File(path);
        files = directory.listFiles();
        for(File file: files){
            Log.d("AUDIO PATH", file.getAbsolutePath());
        }

        Log.d("ALL FILE", files.toString());
        Log.d("NUMBER OF FILE", Integer.toString(files.length));

        audio = new audioAdapter(files,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(audio);
    }

    @Override
    public void onClicklistener(File file, int position) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.media_container, new fragmentAudioPlayer(files, position), null)
                    .commit();

    }

}
