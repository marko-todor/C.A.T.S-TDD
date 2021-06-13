package com.example.cats;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class GameOverFragment extends Fragment {

    private View view;
    private Button back;

    public GameOverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_game_over, container, false);

       back = view.findViewById(R.id.button_go_to_garage);
       back.setBackgroundColor(Color.TRANSPARENT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).db.userDao().incGamesLost(((MainActivity)getActivity()).model.loggedIn);
            }
        }).start();
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MainActivity.buttonMediaPlayer.start();
               MainActivity mainActivity = (MainActivity) getActivity();
               mainActivity.startNewSong(R.raw.garage1);
               NavController navController = NavHostFragment.findNavController(GameOverFragment.this);
               navController.navigate(R.id.action_gameOverFragment_to_garageFragment);
           }
       });

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.startNewSong(R.raw.lose);
        return view;
    }

}
