package com.example.meri.musicplayer.item;

public class Song {

    private long id;
    private String name;
    private String artist;

    public Song(long id, String name, String artist) {
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }
}

