package com.example.cats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.cats.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static MediaPlayer mediaPlayer;
    public static MediaPlayer buttonMediaPlayer;
    public MyViewModel model;
    public static int currentlyPlaying;
    private boolean musicOn = true;
    public  AppDatabase db;

    @Override
    public void onBackPressed() {

        //ModifyCarFragment fragment =  (ModifyCarFragment) getSupportFragmentManager().findFragmentById(R.id.modifyCarFragment);

        if(model.goBack)
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Car car = new Car(displayMetrics.widthPixels/2,displayMetrics.heightPixels*4/5,R.drawable.car1, new ArrayList<CarPart>(),
                350, 7, 0, 700, 300);

        model = new ViewModelProvider(this).get(MyViewModel.class);


        CarPart chainsaw = new CarPart(0, 0,100,100, ImageProvider.carPartResourceId[0], ImageProvider.carPartInfoResourceId[0],0,4,15);
        CarPart driller = new CarPart(0, 0,100,100, ImageProvider.carPartResourceId[1], ImageProvider.carPartInfoResourceId[1],0,5,22);
        CarPart saw = new CarPart(0, 0,100,100, ImageProvider.carPartResourceId[2], ImageProvider.carPartInfoResourceId[2],0,8,35);
        CarPart cannon = new CarPart(0, 0,100,100, ImageProvider.carPartResourceId[3], ImageProvider.carPartInfoResourceId[3],0,6,14);


        car.getSlotTwo().getCompatibleParts().add(chainsaw);
        car.getSlotTwo().getCompatibleParts().add(saw);
        car.getSlotTwo().getCompatibleParts().add(driller);
        car.getSlotOne().getCompatibleParts().add(cannon);

        model.setMyCar(car);


        int width = 450;
        int height = 450;
        int y = displayMetrics.heightPixels*3/5 + 60;
        int x = displayMetrics.widthPixels/7*5;

        Car opponent1 = new Car(x,y,R.drawable.opponent_1, new ArrayList<CarPart>(),
                330, 20, -20, width, height);

        Car opponent2 = new Car(x,y,R.drawable.opponent_2, new ArrayList<CarPart>(),
                342, 15, -20, width, height);

        Car opponent3 = new Car(x,y,R.drawable.opponent_3, new ArrayList<CarPart>(),
                355, 22, -20, width, height);

        CarPart chainsawMirror = new CarPart(0, 0,100,100, ImageProvider.carPartMirrorResourceId[0], ImageProvider.carPartInfoResourceId[0],0,4,15);
        CarPart drillerMirror = new CarPart(0, 0,100,100, ImageProvider.carPartMirrorResourceId[1], ImageProvider.carPartInfoResourceId[1],0,5,22);
        CarPart sawMirror = new CarPart(0, 0,100,100, ImageProvider.carPartMirrorResourceId[2], ImageProvider.carPartInfoResourceId[2],0,8,35);
        CarPart cannonMirror = new CarPart(0, 0,100,100, ImageProvider.carPartMirrorResourceId[3], ImageProvider.carPartInfoResourceId[3],0,6,14);

        opponent1.getSlotTwo().getCompatibleParts().add(chainsawMirror);
        opponent1.getSlotTwo().getCompatibleParts().add(sawMirror);
        opponent1.getSlotTwo().getCompatibleParts().add(drillerMirror);
        opponent1.getSlotOne().getCompatibleParts().add(cannonMirror);

        opponent2.getSlotTwo().getCompatibleParts().add(chainsawMirror);
        opponent2.getSlotTwo().getCompatibleParts().add(sawMirror);
        opponent2.getSlotTwo().getCompatibleParts().add(drillerMirror);
        opponent2.getSlotOne().getCompatibleParts().add(cannonMirror);

        opponent3.getSlotTwo().getCompatibleParts().add(chainsawMirror);
        opponent3.getSlotTwo().getCompatibleParts().add(sawMirror);
        opponent3.getSlotTwo().getCompatibleParts().add(drillerMirror);
        opponent3.getSlotOne().getCompatibleParts().add(cannonMirror);

        List<Car> opponents = new ArrayList<>();
        opponents.add(opponent1);
        opponents.add(opponent2);
        opponents.add(opponent3);
        model.addOpponentCars(opponents);

        buttonMediaPlayer = MediaPlayer.create(this, R.raw.button);

        model.getMusicOnLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                musicOn = aBoolean;
                if(musicOn) {
                    if(MainActivity.mediaPlayer != null)MainActivity.mediaPlayer.start();
                } else {
                    if(MainActivity.mediaPlayer != null)MainActivity.mediaPlayer.pause();
                }
            }
        });

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "CATS").fallbackToDestructiveMigration().build();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                db.clearAllTables();
//            }
//        }).start();



        model.setDatabase(db);

        setContentView(R.layout.activity_main);

    }

    public void playNextSong() {
        if (currentlyPlaying == R.raw.garage1){
            startNewSong(R.raw.garage2);
        }
        else if (currentlyPlaying == R.raw.garage2) {
            startNewSong(R.raw.garage1);
        }
    }

       public void startNewSong(int song) {
           if(musicOn) {
               if (MainActivity.mediaPlayer != null && MainActivity.mediaPlayer.isPlaying())
                   MainActivity.mediaPlayer.stop();
               mediaPlayer = MediaPlayer.create(this, song);
               currentlyPlaying = song;
               mediaPlayer.seekTo(0);
               mediaPlayer.setVolume(0.5f, 0.5f);
               mediaPlayer.start();
           }
       }


    @Override
    protected void onStop() {
        if(MainActivity.mediaPlayer != null && MainActivity.mediaPlayer.isPlaying())MainActivity.mediaPlayer.stop();
        super.onStop();
    }

    @Override
    protected void onPause() {
        if(MainActivity.mediaPlayer != null && MainActivity.mediaPlayer.isPlaying())MainActivity.mediaPlayer.stop();
        super.onPause();
    }

    public void pausePlayer() {
        if(MainActivity.mediaPlayer != null) {
            MainActivity.mediaPlayer.stop();
        }
    }
}
