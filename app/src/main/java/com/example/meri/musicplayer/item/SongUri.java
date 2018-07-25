package com.example.meri.musicplayer.item;

import android.net.Uri;

public class SongUri {

    private long id;
    private Uri uri;
    private String title;

    public SongUri(long id, Uri uri, String title) {
        this.id = id;
        this.uri = uri;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public Uri getUri() {
        return uri;
    }

    public String getTitle(){
        return title;
    }
}