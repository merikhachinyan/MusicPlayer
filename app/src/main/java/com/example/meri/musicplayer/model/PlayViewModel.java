package com.example.meri.musicplayer.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class PlayViewModel extends ViewModel{

    private final MutableLiveData<Long> mId = new MutableLiveData<>();

    public void setId(Long id) {
        mId.setValue(id);
    }

    public MutableLiveData<Long> getMusicId(){
        return mId;
    }
}
