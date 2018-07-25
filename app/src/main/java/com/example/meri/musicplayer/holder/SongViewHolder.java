package com.example.meri.musicplayer.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.meri.musicplayer.R;

public class SongViewHolder extends RecyclerView.ViewHolder{

    private TextView mSongName;
    private TextView mSongArtist;

    public SongViewHolder(View itemView) {
        super(itemView);

        mSongName = itemView.findViewById(R.id.song_title);
        mSongArtist = itemView.findViewById(R.id.song_artist);
    }

    public void bind(String name, String artist){
        mSongName.setText(name);
        mSongArtist.setText(artist);
    }
}