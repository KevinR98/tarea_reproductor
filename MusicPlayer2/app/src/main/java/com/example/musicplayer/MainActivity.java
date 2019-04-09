package com.example.musicplayer;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {


    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    boolean playing = false;
    int currentSong = 0;
    final ArrayList<String> songs = new ArrayList<String>();



    public void onClickPlay(View view){

        if(playing){
            mediaPlayer.pause();
            view.setBackgroundResource(android.R.drawable.ic_media_play);
        } else {
            mediaPlayer.start();
            view.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
        playing = !playing;
    }

    public void onClickNext(View view){
        Log.i("currentSong", Integer.toString(currentSong));
        if(currentSong != songs.size()-1){
            ++currentSong;
            playSong(currentSong);
        }
        Log.i("currentSong", Integer.toString(currentSong));
    }

    public void onClickPreviouos(View view){
        Log.i("currentSong", Integer.toString(currentSong));
        if(currentSong != 0){
            --currentSong;
            playSong(currentSong);
        }
        Log.i("currentSong", Integer.toString(currentSong));
    }

    private void playSong (int position){
        String songName = songs.get(position);

        TextView nameView = (TextView)findViewById(R.id.songName);
        nameView.setText(songName);

        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }

        int resourceId = this.getResources().getIdentifier(songName, "raw", this.getPackageName());

        mediaPlayer = MediaPlayer.create(this, resourceId);

        findViewById(R.id.playButton).setBackgroundResource(android.R.drawable.ic_media_play);
        playing = false;

        //mediaPlayer.start();
    }

    private void setSongsList(){
        ListView songsList = findViewById(R.id.songsList);
        songs.add(getResources().getResourceEntryName(R.raw.bensound_betterdays));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_onceagain));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_tomorrow));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songs);
        songsList.setAdapter(adapter);
        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentSong = position;
                playSong(currentSong);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSongsList();
        playSong(currentSong);

        //setVolumeBar
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        SeekBar volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setProgress(currentVolume);
        volumeBar.setMax(maxVolume);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //SetProgressBar
        final SeekBar musicBar = findViewById(R.id.songProgress);
        musicBar.setMax(mediaPlayer.getDuration());
        musicBar.setProgress(mediaPlayer.getCurrentPosition());
        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Handler handler = new Handler();
        Runnable run  = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                musicBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        };
        run.run();



    }
}
