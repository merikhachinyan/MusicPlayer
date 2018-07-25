package com.example.meri.musicplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.meri.musicplayer.R;
import com.example.meri.musicplayer.fragments.PlayMusicFragment;
import com.example.meri.musicplayer.fragments.SongsListFragment;

public class MainActivity extends AppCompatActivity {

    private SongsListFragment mSongsListFragment;
    private PlayMusicFragment mPlayMusicFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSongsListFragment = new SongsListFragment();
        mPlayMusicFragment = new PlayMusicFragment();

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.add(R.id.frame, mSongsListFragment, "Songs");
        transaction.add(R.id.play_frame, mPlayMusicFragment, "Play");
        transaction.commit();
    }
}
