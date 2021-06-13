package com.example.cats;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class GameWonFragment extends Fragment {
     private View view;
     private Button back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game_won, container, false);
        back = view.findViewById(R.id.button_go_to_garage);
        back.setBackgroundColor(Color.TRANSPARENT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).db.userDao().incGamesWon(((MainActivity)getActivity()).model.loggedIn);
            }
        }).start();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.buttonMediaPlayer.start();
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.startNewSong(R.raw.garage1);
                NavController navController = NavHostFragment.findNavController(GameWonFragment.this);
                navController.navigate(R.id.action_gameWonFragment_to_garageFragment);
            }
        });
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.startNewSong(R.raw.win);
        return view;
    }
}
