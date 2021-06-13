package com.example.cats;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class WelcomeFragment extends Fragment {

    private MyViewModel model;

    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        view.findViewById(R.id.imagePlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(WelcomeFragment.this).navigate(R.id.action_welcomeFragment_to_videoFragment);
            }
        });


        view.findViewById(R.id.quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finishAffinity();
                System.exit(0);
            }
        });



        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.startNewSong(R.raw.menu);


        return view;
    }

    @Override
    public void onPause() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.pausePlayer();
        super.onPause();
    }




}
