package com.example.meri.musicplayer.manager;

import com.example.meri.musicplayer.item.SongUri;

import java.util.ArrayDeque;
import java.util.Deque;

public class SongsManager {

    private Deque<SongUri> songs;

    public SongsManager(){
        songs = new ArrayDeque<>();
    }

    public void addList(SongUri song){
        songs.add(song);
    }

    public void addFirst(SongUri song){
        songs.addFirst(song);
    }

    public void addLast(SongUri song){
        songs.addLast(song);
    }

    public SongUri getFirst(){
        return songs.getFirst();
    }

    public SongUri getLast(){
        return songs.getLast();
    }

    public SongUri pollFirst(){
        return songs.pollFirst();
    }

    public SongUri pollLast(){
        return songs.pollLast();
    }
}
