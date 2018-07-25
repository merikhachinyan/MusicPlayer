package com.example.meri.musicplayer.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meri.musicplayer.R;
import com.example.meri.musicplayer.holder.SongViewHolder;
import com.example.meri.musicplayer.item.Song;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongViewHolder>{

    private List<Song> mSongs;

    private OnAdapterItemClickListener mListener;

    public SongsAdapter() {
        mSongs = new ArrayList<>();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongViewHolder holder, int position) {
        final Song song = mSongs.get(position);

        holder.bind(song.getName(), song.getArtist());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickSong(mSongs.get(holder.getAdapterPosition()).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public void getSongs(Context context){
        long id;
        String title, artist;
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null){
            while (cursor.moveToNext()){
                id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                mSongs.add(new Song(id, title, artist));
            }
        }
        cursor.close();
    }

    public void setAdapterItemClickListener(OnAdapterItemClickListener listener){
        mListener = listener;
    }

    public interface OnAdapterItemClickListener{
        void onClickSong(long id);
    }
}
