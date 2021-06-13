package com.example.cats;

import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;
import tyrantgit.explosionfield.ExplosionField;

public class FightFragment extends Fragment {

    private View view;
    private MyViewModel model;
    private CustomView customView;
    private TextView timer, watchOut;
    public MovingPartsThread movingPartsThread;
    private TimerThread timerThread;
    private boolean fightOver = false;
    private Car opponent;
    private Car myCar;
    private boolean carHasCannon = false;
    private boolean opponentHasCannon = false;
    public ExplosionField explosionField;

    public MyViewModel getModel() {
        return model;
    }

    public TimerThread getTimerThread() {
        return timerThread;
    }


    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public boolean isCarHasCannon() {
        return carHasCannon;
    }

    public void setCarHasCannon(boolean carHasCannon) {
        this.carHasCannon = carHasCannon;
    }


    public boolean isOpponentHasCannon() {
        return opponentHasCannon;
    }

    public void setOpponentHasCannon(boolean opponentHasCannon) {
        this.opponentHasCannon = opponentHasCannon;
    }

    public FightFragment() {

    }

    public Car getOpponent() {
        return opponent;
    }

    public void setOpponent(Car opponent) {
        this.opponent = opponent;
    }

    public Car getMyCar() {
        return myCar;
    }

    public void setMyCar(Car myCar) {
        this.myCar = myCar;
    }

    public boolean isFightOver() {
        return fightOver;
    }

    public void setFightOver(boolean fightOver) {
        this.fightOver = fightOver;
    }

    public void setNewPosition(Car car, int move) {
        car.setX(car.getX() + move);
        car.getSlotOne().getSlot().left += move;
        car.getSlotOne().getSlot().right += move;
        car.getSlotTwo().getSlot().left += move;
        car.getSlotTwo().getSlot().right += move;
    }

    public void moveCarParts() {
        for (int i = 0; i < myCar.getCarParts().size(); i++) {
            if(myCar.getSlotOne().isCompatible(myCar.getCarParts().get(i).getPartResourceId())) {
                myCar.getCarParts().get(i).setX(myCar.getSlotOne().getSlot().left);
            } else {
                myCar.getCarParts().get(i).setX(myCar.getSlotTwo().getSlot().left);
            }
        }
        for (int i = 0; i < opponent.getCarParts().size(); i++) {
            if(opponent.getSlotOne().isCompatible(opponent.getCarParts().get(i).getPartResourceId())) {
                opponent.getCarParts().get(i).setX(opponent.getSlotOne().getSlot().left);
            } else {
                opponent.getCarParts().get(i).setX(opponent.getSlotTwo().getSlot().left);
            }
        }
    }

    public void setMyCar() {
        Car oldCar = model.getMyCar().getValue();
        myCar = new Car(300, 650, oldCar.getCarResourceId(),null
                ,350,oldCar.getEnergy(),0,480,480); //OVDE PROMENI

        if(model.bodyPart != null) {
            myCar.setCarResourceId(R.drawable.car_with_body_cc);
        } else {
            myCar.setCarResourceId(R.drawable.car1cc);
        }

        Slot slotUpMy = new Slot();
        Slot slotDownMy = new Slot();

        slotUpMy.setAvailable(oldCar.getSlotOne().isAvailable());
        slotDownMy.setAvailable(oldCar.getSlotTwo().isAvailable());

        slotUpMy.setSlot(new Rect(610, 760, 810,960));
        slotDownMy.setSlot(new Rect(760, 830, 960, 1030));

        for(int i=0; i< oldCar.getSlotOne().getCompatibleParts().size(); i++) {
            CarPart part = oldCar.getSlotOne().getCompatibleParts().get(i);
            CarPart newPart = new CarPart(part.getX(), part.getY(), part.getWidth(), part.getHeight(), part.getPartResourceId(), part.getPartInfoResourceId(),part.getHealth(), part.getEnergy(), part.getPower());
            slotUpMy.getCompatibleParts().add(newPart);
        }

        for(int i=0; i< oldCar.getSlotTwo().getCompatibleParts().size(); i++) {
            CarPart part = oldCar.getSlotTwo().getCompatibleParts().get(i);
            CarPart newPart = new CarPart(part.getX(), part.getY(), part.getWidth(), part.getHeight(),part.getPartResourceId(), part.getPartInfoResourceId(), part.getHealth(), part.getEnergy(), part.getPower());
            slotDownMy.getCompatibleParts().add(newPart);
        }


        ArrayList<CarPart> carParts = new ArrayList<>();
        for (int i = 0; i < oldCar.getCarParts().size(); i++) {

            CarPart oldCarPart = oldCar.getCarParts().get(i);
            CarPart newCarPart = null;

            if(oldCar.getSlotOne().isCompatible(oldCarPart.getPartResourceId())) {
                newCarPart = new CarPart(slotUpMy.getSlot().left
                        , slotUpMy.getSlot().top
                        , slotUpMy.getSlot().right - slotUpMy.getSlot().left
                        , slotUpMy.getSlot().bottom - slotUpMy.getSlot().top,
                        oldCarPart.getPartResourceId(), oldCarPart.getPartInfoResourceId()
                        , oldCarPart.getHealth(), oldCarPart.getEnergy(), oldCarPart.getPower());
                carParts.add(newCarPart);
                if(newCarPart.getPartResourceId().equals(R.drawable.cannon)){
                    carHasCannon = true;
                }
                slotUpMy.setPartImageId(oldCarPart.getPartResourceId());
            } else if (oldCar.getSlotTwo().isCompatible(oldCarPart.getPartResourceId())) {
                newCarPart = new CarPart(slotDownMy.getSlot().left
                        , slotDownMy.getSlot().top
                        , slotDownMy.getSlot().right - slotDownMy.getSlot().left
                        , slotDownMy.getSlot().bottom - slotDownMy.getSlot().top,
                        oldCarPart.getPartResourceId(), oldCarPart.getPartInfoResourceId()
                        , oldCarPart.getHealth(), oldCarPart.getEnergy(), oldCarPart.getPower());
                carParts.add(newCarPart);
                slotDownMy.setPartImageId(oldCarPart.getPartResourceId());
            }

            if(newCarPart != null) {
                myCar.setPower(myCar.getPower() + newCarPart.getPower());
                myCar.setHealth(myCar.getHealth() + newCarPart.getHealth());
                myCar.setEnergy(myCar.getEnergy() + newCarPart.getEnergy());
            }
        }
        myCar.setCarParts(carParts);
        myCar.setSlotOne(slotUpMy);
        myCar.setSlotTwo(slotDownMy);

        customView.setMyCar(myCar);

    }

    public void setOpponentCar() {
        List<Car> opponents = model.getOpponentCars().getValue();
        Random rand = new Random();

        opponent = model.getOpponentCars().getValue().get(rand.nextInt(model.getOpponentCars().getValue().size()));


        Slot slotUpOpponent = opponent.getSlotOne();
        Slot slotDownOpponent = opponent.getSlotTwo();

        Slot newSlotUp = new Slot();
        Slot newSlotDown = new Slot();

        newSlotUp.setAvailable(false); //promeni da ne bude uvek
        newSlotDown.setAvailable(false); //promeni da ne bude uvek

        newSlotUp.setCompatibleParts(slotUpOpponent.getCompatibleParts());
        newSlotDown.setCompatibleParts(slotDownOpponent.getCompatibleParts());

        newSlotUp.setSlot(new Rect(1590, 780, 1790,980));
        newSlotDown.setSlot(new Rect(1498, 840, 1698, 1040));


        CarPart newCarPart;

        int indexUp = rand.nextInt(opponent.getSlotOne().getCompatibleParts().size());
        int indexDown = rand.nextInt(opponent.getSlotTwo().getCompatibleParts().size());


        ArrayList<CarPart> carPartsOpponent = new ArrayList<>();

        newCarPart = new CarPart(newSlotUp.getSlot().left
                , newSlotUp.getSlot().top
                , newSlotUp.getSlot().right - newSlotUp.getSlot().left
                , newSlotUp.getSlot().bottom - newSlotUp.getSlot().top,
                opponent.getSlotOne().getCompatibleParts().get(indexUp).getPartResourceId(),
                opponent.getSlotOne().getCompatibleParts().get(indexUp).getPartInfoResourceId()
                , opponent.getSlotOne().getCompatibleParts().get(indexUp).getHealth()
                , opponent.getSlotOne().getCompatibleParts().get(indexUp).getEnergy()
                , opponent.getSlotOne().getCompatibleParts().get(indexUp).getPower());

        carPartsOpponent.add(newCarPart);
        newSlotUp.setPartImageId(newCarPart.getPartResourceId());

        int health = opponent.getHealth() + newCarPart.getHealth();
        int power = opponent.getPower() + newCarPart.getPower();

        newCarPart = new CarPart(newSlotDown.getSlot().left
                , newSlotDown.getSlot().top
                , newSlotDown.getSlot().right - newSlotDown.getSlot().left
                , newSlotDown.getSlot().bottom - newSlotDown.getSlot().top,
                opponent.getSlotTwo().getCompatibleParts().get(indexDown).getPartResourceId(),
                opponent.getSlotTwo().getCompatibleParts().get(indexDown).getPartInfoResourceId()
                , opponent.getSlotTwo().getCompatibleParts().get(indexDown).getHealth()
                , opponent.getSlotTwo().getCompatibleParts().get(indexDown).getEnergy()
                , opponent.getSlotTwo().getCompatibleParts().get(indexDown).getPower());

        carPartsOpponent.add(newCarPart);
        newSlotDown.setPartImageId(newCarPart.getPartResourceId());


        health += newCarPart.getHealth();
        power += newCarPart.getPower();


        opponent = new Car(opponent.getX(), opponent.getY(), opponent.getCarResourceId(),opponent.getCarParts(),health, 0,power,opponent.getWidth(),opponent.getHeight());
        opponent.setCarParts(carPartsOpponent);
        opponent.setSlotOne(newSlotUp);
        opponent.setSlotTwo(newSlotDown);

        if(opponent.getSlotOne().getCompatibleParts().get(indexUp).getPartResourceId().equals(R.drawable.cannon_mirror)){
            opponentHasCannon = true;
        }
        customView.setOpponentCar(opponent);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fight, container, false);


        model = new ViewModelProvider(getActivity()).get(MyViewModel.class);
        customView = view.findViewById(R.id.customView);
        customView.setFightFragment(this);

        timer = view.findViewById(R.id.textView_timer);
        watchOut = view.findViewById(R.id.textView_watch_out);
        setMyCar();

        setOpponentCar();


        customView.resizeImageParts();


        movingPartsThread = new MovingPartsThread(model, customView, this);


        timerThread = new TimerThread(timer,watchOut,this);
        new Thread(movingPartsThread).start();
        new Thread(timerThread).start();

        explosionField = ExplosionField.attach2Window(this.getActivity());

        model.goBack = false;

        return view;
    }

    @Override
    public void onStop() {
        movingPartsThread.clash = false;
        fightOver = true;
        super.onStop();
    }

    @Override
    public void onDestroy() {

        movingPartsThread.clash = false;
        fightOver = true;

        super.onDestroy();
    }

    public void navigateGameOver() {
        NavController navController = NavHostFragment.findNavController(FightFragment.this);
        if(navController.getCurrentDestination().getId() == R.id.fightFragment)
        navController.navigate(R.id.action_fightFragment_to_gameOverFragment);
    }
    public void navigateGameWon() {
        NavController navController = NavHostFragment.findNavController(FightFragment.this);
        if(navController.getCurrentDestination().getId() == R.id.fightFragment)
            navController.navigate(R.id.action_fightFragment_to_gameWonFragment);
    }

    public static class TimerThread implements Runnable {

        private TextView timer, watchOut;
        private Integer timeRemaining = 7;
        private FightFragment fightFragment;
        public boolean carClearToShot, opponentClearToShot;
        public TimerThread(TextView timer, TextView watchOut, FightFragment fightFragment) {
            this.timer = timer;
            this.watchOut = watchOut;
            this.fightFragment = fightFragment;
        }



        @Override
        public void run() {
            while(true) {
                if(timeRemaining % 2 == 0) {
                    carClearToShot = true;
                    opponentClearToShot = true;
                }
                if(timeRemaining > 0) {
                    timer.post(new Runnable() {
                        @Override
                        public void run() {
                            timer.setText(timeRemaining.toString());
                        }
                    });
                } else{
                    timeRemaining = 1000;
                    fightFragment.setFightOver(true);
                    timer.post(new Runnable() {
                        @Override
                        public void run() {
                            timer.setVisibility(View.INVISIBLE);
                        }
                    });
                    watchOut.post(new Runnable() {
                        @Override
                        public void run() {
                            watchOut.setVisibility(View.VISIBLE);
                        }
                    });
                }
                timeRemaining--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

    }

}
