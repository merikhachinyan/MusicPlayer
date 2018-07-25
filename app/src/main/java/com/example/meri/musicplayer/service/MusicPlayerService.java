package com.example.meri.musicplayer.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

public class MusicPlayerService extends IntentService{

    private MusicBinder mMusicBinder;
    private MediaPlayer mMediaPlayer;
    private OnMusicPlayingComplete mMusicPlayingComplete;

    public MusicPlayerService() {
        super("MusicPlayer");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
        }
        if (mMusicBinder == null){
            mMusicBinder = new MusicBinder();
        }
        setListener();

        return mMusicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mMusicBinder.pause();
        return true;
    }

    private void setListener(){
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mMusicPlayingComplete.playNextSong();
            }
        });
    }

    private void play(Uri uri){
        if(mMediaPlayer != null){
            try {
                mMediaPlayer.setDataSource(this, uri);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void pauseMusic(){
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
    }

    private void continueMusic(Uri uri, int progress){
        try {
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.prepare();
            mMediaPlayer.seekTo(progress);
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MusicBinder extends Binder {
        public void playMusic(Uri uri){
            play(uri);
        }

        public void pause(){
            pauseMusic();
        }

        public int getCurrentPosition(){
            return mMediaPlayer.getCurrentPosition();
        }

        public int getDuration(){
            return mMediaPlayer.getDuration();
        }

        public void setProgress(int progress){
            mMediaPlayer.seekTo(progress);
        }

        public void continuePlaying(Uri uri, int progress){
            continueMusic(uri, progress);
        }

        public void setNextMusicPlayingListener(OnMusicPlayingComplete musicPlayingComplete){
            mMusicPlayingComplete = musicPlayingComplete;
        }
    }

    public interface OnMusicPlayingComplete{
        void playNextSong();
    }
}
