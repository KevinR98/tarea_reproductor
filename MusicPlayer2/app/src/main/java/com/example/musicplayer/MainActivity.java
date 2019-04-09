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
            pauseSong();
        } else {
            startSong();
        }
    }

    public void onClickNext(View view){
        nextSong();
    }

    public void onClickPrevious(View view){
        previousSong();
    }

    private void pauseSong(){
        mediaPlayer.pause();
        findViewById(R.id.playButton).setBackgroundResource(android.R.drawable.ic_media_play);
        playing = false;
    }

    private void startSong(){
        mediaPlayer.start();
        findViewById(R.id.playButton).setBackgroundResource(android.R.drawable.ic_media_pause);
        playing = true;
    }

    private boolean nextSong(){
        if(currentSong != songs.size()-1){
            ++currentSong;
            playSong(currentSong);
            return true;
        }
        return false;
    }

    private boolean previousSong(){
        if(currentSong != 0){
            --currentSong;
            playSong(currentSong);
            return true;
        }
        return false;
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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(nextSong()) {startSong();}
                else{
                    findViewById(R.id.playButton).setBackgroundResource(android.R.drawable.ic_media_play);
                }
            }
        });


        findViewById(R.id.playButton).setBackgroundResource(android.R.drawable.ic_media_play);
        playing = false;

        SeekBar musicBar = findViewById(R.id.songProgress);
        musicBar.setMax(mediaPlayer.getDuration());
        musicBar.setProgress(mediaPlayer.getCurrentPosition());

    }

    private void setSongsList(){
        ListView songsList = findViewById(R.id.songsList);
        songs.add(getResources().getResourceEntryName(R.raw.bensound_betterdays));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_onceagain));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_tomorrow));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_dreams));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_newdawn));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_scifi));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_summer));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_thejazzpiano));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_thelounge));
        songs.add(getResources().getResourceEntryName(R.raw.bensound_theelevatorbossanova));


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
                if(fromUser){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
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
                handler.postDelayed(this, 500);
                musicBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        };
        run.run();



    }
}
