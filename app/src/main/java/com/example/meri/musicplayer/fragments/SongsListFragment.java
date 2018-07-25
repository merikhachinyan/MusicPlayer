package com.example.meri.musicplayer.fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meri.musicplayer.R;
import com.example.meri.musicplayer.adapter.SongsAdapter;
import com.example.meri.musicplayer.model.PlayViewModel;


public class SongsListFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_READ_AUDIO = 100;

    SongsAdapter.OnAdapterItemClickListener mListener =
            new SongsAdapter.OnAdapterItemClickListener() {
                @Override
                public void onClickSong(long id) {
                    PlayViewModel model = ViewModelProviders.of(getActivity())
                            .get(PlayViewModel.class);
                    model.setId(id);
                }
            };

    private RecyclerView mRecyclerView;
    private SongsAdapter mAdapter;

    public SongsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        showAudio();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if(requestCode == PERMISSIONS_REQUEST_READ_AUDIO){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showAudio();
            } else {
                Toast.makeText(getContext(), "This app need permission",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init(View view){
        mRecyclerView =
                view.findViewById(R.id.item_songs_list_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        mAdapter = new SongsAdapter();
        mAdapter.setAdapterItemClickListener(mListener);
    }

    private void showAudio(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_AUDIO);
        } else {
            mAdapter.getSongs(getContext());
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
