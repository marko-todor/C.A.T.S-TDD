package com.example.cats;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.cats.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    private View view;

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video, container, false);


        MediaController mc= new MediaController(getActivity());

        final VideoView videoView = view.findViewById(R.id.videoView);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.trailer;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(null);
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LoginDialog alert = new LoginDialog();
                alert.showDialog(getActivity(), VideoFragment.this);
            }
        });





        return view;
    }

}