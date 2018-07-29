package com.example.meri.musicplayer.fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.meri.musicplayer.R;
import com.example.meri.musicplayer.item.SongUri;
import com.example.meri.musicplayer.manager.SongsManager;
import com.example.meri.musicplayer.model.PlayViewModel;
import com.example.meri.musicplayer.service.MusicPlayerService;

import java.util.concurrent.TimeUnit;

public class PlayMusicFragment extends Fragment{

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (MusicPlayerService.MusicBinder) iBinder;
            binder.setNextMusicPlayingListener(mMusicPlayingComplete);
            mPlayPause.setImageResource(R.drawable.pause);

            isBound = true;
            if(isContinue){
                binder.continuePlaying(findSong(mId).getUri(), currProgress);
                isContinue = false;
            } else {
                if (isPlayNext){
                    binder.playMusic(mManager.getFirst().getUri());
                    isPlayNext = false;
                } else {
                    binder.playMusic(findSong(mId).getUri());
                }
            }
            mTitle.setText(findSong(mId).getTitle());

            final Handler handler = new Handler();

            updateSeekBar(handler);
            updateTime(handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            binder = null;
            isBound = false;
        }
    };

    MusicPlayerService.OnMusicPlayingComplete mMusicPlayingComplete =
            new MusicPlayerService.OnMusicPlayingComplete() {
                @Override
                public void playNextSong() {
                    mManager.addLast(mManager.pollFirst());
                    mId = mManager.getFirst().getId();
                    isPlayNext = true;
                    unbindService();
                    bindService();
                }
            };

    private void updateSeekBar(final Handler handler){
        mProgress.setMax(binder.getDuration());

        new Thread(new Runnable() {
            @Override
            public void run() {
                mProgress.setProgress(binder.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        }).start();
    }

    private void updateTime(final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int position = binder.getCurrentPosition();
                @SuppressLint("DefaultLocale") final String time = String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(position),
                        TimeUnit.MILLISECONDS.toSeconds(position) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(position)));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCurrentTime.setText(time);
                    }
                });
                handler.postDelayed(this, 1000);
            }
        }).start();
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                int time = binder.getCurrentPosition();
//                @SuppressLint("DefaultLocale") String timeText = String.format("%02d : %02d",
//                        TimeUnit.MILLISECONDS.toMinutes(time),
//                        TimeUnit.MILLISECONDS.toSeconds(time) -
//                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
//
//                mCurrentTime.setText(timeText);
//                handler.postDelayed(this, 1000);
//            }
//        });
    }

    private MusicPlayerService.MusicBinder binder;
    private SongsManager mManager;
    private boolean isBound;
    long mId;

    public PlayMusicFragment() {
        mManager = new SongsManager();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setListeners();
        getSongs();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    private ImageView mPlayPause;
    private ImageView mPrevious;
    private ImageView mNext;
    private SeekBar mProgress;
    private TextView mTitle;
    private TextView mCurrentTime;

    private boolean isContinue;
    private int currProgress;
    private int seekBarProgress;
    private boolean isPlayNext;

    private void init(View view){
        mPlayPause = view.findViewById(R.id.image_play_music_fragment_play_pause_song);
        mPrevious = view.findViewById(R.id.image_play_music_fragment_previous_song);
        mNext = view.findViewById(R.id.image_play_music_fragment_next_song);
        mProgress = view.findViewById(R.id.seekbar_play_music_fragment_music_progress);
        mTitle = view.findViewById(R.id.text_play_music_fragment_music_title);
        mCurrentTime = view.findViewById(R.id.text_play_music_fragment_music_current_time);

        PlayViewModel model = ViewModelProviders.of(getActivity()).get(PlayViewModel.class);
        model.getMusicId().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long aLong) {
                mId = aLong;

                if(isBound){
                    unbindService();
                }
                isContinue = false;
                bindService();
            }
        });
    }

    private void setListeners(){
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBound){
                    mPlayPause.setImageResource(R.drawable.play);

                    currProgress = binder.getCurrentPosition();
                    isContinue = true;
                    unbindService();
                } else {
                    if(!isContinue){
                        mId = mManager.getFirst().getId();
                        isBound = true;
                    }
                    bindService();
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlayNext = true;
                mManager.addLast(mManager.pollFirst());
                mId = mManager.getFirst().getId();
                unbindService();
                bindService();
            }

        });

        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlayNext = true;
                mManager.addFirst(mManager.pollLast());
                mId = mManager.getFirst().getId();
                unbindService();
                bindService();
            }
        });

        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(b){
                    seekBarProgress = progress;
                    binder.setProgress(seekBarProgress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void bindService(){
        Intent intent = new Intent(getActivity(), MusicPlayerService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(){
        getActivity().unbindService(mServiceConnection);
        isBound = false;
    }

    private void getSongs(){
        long id;
        Uri uri;
        String title;

        ContentResolver resolver = getContext().getContentResolver();

        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null){
            while (cursor.moveToNext()){
                id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                uri = ContentUris
                        .withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                id);
                mManager.addList(new SongUri(id, uri, title));
            }
        }
        cursor.close();
    }

    private SongUri findSong(long id){
        SongUri song = mManager.getFirst();
        while (song.getId() != id){
            mManager.addList(mManager.pollFirst());
            song = mManager.getFirst();
        }
        return song;
    }
}