package com.example.cats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MovingPartsThread implements Runnable {

    private boolean dealingDamage = false;
    private Random random;


    public MovingPartsThread(MyViewModel model, CustomView customView, FightFragment fightFragment) {
        this.model = model;
        this.customView = customView;
        this.fightFragment = fightFragment;
        this.random = new Random();
    }

    private MyViewModel model;
    private CustomView customView;
    public boolean clash = false;
    private FightFragment fightFragment;


    public void checkIntersect() {

        boolean opponentSlotTwo = customView.getOpponentCar().getSlotTwo().isAvailable();
        boolean meSlotTwo = customView.getMyCar().getSlotTwo().isAvailable();

        Rect rectOpponentTwo = customView.getOpponentCar().getSlotTwo().getSlot();
        Rect rectMeTwo = customView.getMyCar().getSlotTwo().getSlot();

        Car myCar = customView.getMyCar();
        Car opponentCar = customView.getOpponentCar();
        boolean meSaw = false;
        boolean opponentSaw = false;

        if(!meSlotTwo && myCar.getSlotTwo().getPartImageId().equals(R.drawable.saw)) {
            meSaw = true;
        }
        if(!opponentSlotTwo && opponentCar.getSlotTwo().getPartImageId().equals(R.drawable.saw_mirror)) {
            opponentSaw = true;
        }

        boolean intersectsCars = rectMeTwo.intersects(rectOpponentTwo.left + 350, rectOpponentTwo.top,
                rectOpponentTwo.right + 350, rectOpponentTwo.bottom);

        if((meSlotTwo && opponentSlotTwo) || (meSaw && opponentSlotTwo)
                || (opponentSaw && meSlotTwo) ||
                (opponentSaw && meSaw)) {
            if(intersectsCars)clash = true;
        } else if(!meSlotTwo && !opponentSlotTwo && !opponentSaw && !meSaw && rectMeTwo.intersects(rectOpponentTwo.left + 40, rectOpponentTwo.top,
                rectOpponentTwo.right + 40, rectOpponentTwo.bottom)) {
            clash = true;
        } else if(((!meSlotTwo && !opponentSlotTwo && meSaw) || (!meSlotTwo && !opponentSlotTwo && opponentSaw) ||(!meSlotTwo && opponentSlotTwo)
                || (meSlotTwo && !opponentSlotTwo)) && rectMeTwo.intersects(rectOpponentTwo.left + 200, rectOpponentTwo.top,
                rectOpponentTwo.right + 200, rectOpponentTwo.bottom)) {
            clash = true;
        }


        if(clash) {
                Vibrator v = null;
                if(fightFragment.getActivity() != null)v = (Vibrator) fightFragment.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(v != null)v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                if(v != null)v.vibrate(300);
            }
        }
    }

    public boolean isHitBounds() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(fightFragment.getActivity() != null)fightFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return  (fightFragment.getMyCar().getX() < 0 ||
                fightFragment.getOpponent().getX()
                        + fightFragment.getOpponent().getWidth() > displayMetrics.widthPixels);
    }



    void checkRocketsHit() {
        if(customView.getRocketMe() != null) {
            if(customView.getRocketMe().getX() > customView.getOpponentCar().getX() + customView.getOpponentCar().getWidth()/2) {
                customView.setRocketMe(null);
                fightFragment.getTimerThread().carClearToShot = false;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            customView.setExplosion(customView.getOpponentCar());
                            customView.getOpponentCar().setHealth(customView.getOpponentCar().getHealth() - 14);
                            customView.updateHealth();
                            customView.shootRocket = false;
                            Thread.sleep(200);
                            customView.clearExplosion(customView.getOpponentCar());

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        if(customView.getRocketOpponent() != null) {
            if(customView.getRocketOpponent().getX() < customView.getMyCar().getX() + customView.getMyCar().getWidth()/2) {
                customView.setRocketOpponent(null);
                fightFragment.getTimerThread().opponentClearToShot = false;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            customView.setExplosion(customView.getMyCar());
                            customView.getMyCar().setHealth(customView.getMyCar().getHealth() - 14);
                            customView.updateHealth();
                            Thread.sleep(200);
                            customView.clearExplosion(customView.getMyCar());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }



    public void setCarPositions() {
        if(!customView.isDrawExplosionMe()) {
                fightFragment.setNewPosition(customView.getMyCar(), 1);
        } else {
                fightFragment.setNewPosition(customView.getMyCar(), -1);
        }

        if(!customView.isDrawExplosionOpponent()) {
                fightFragment.setNewPosition(customView.getOpponentCar(), -1);
        } else {
                fightFragment.setNewPosition(customView.getOpponentCar(), 1);
        }
    }

    public void moveStronger() {
        if(customView.isDrawExplosionMe()){
            fightFragment.setNewPosition(customView.getMyCar(), -15);
            fightFragment.setNewPosition(customView.getOpponentCar(), -1);
            clash = false;
            return;
        }
        if(customView.isDrawExplosionOpponent()){
            fightFragment.setNewPosition(customView.getOpponentCar(), 15);
            fightFragment.setNewPosition(customView.getMyCar(), 1);
            clash = false;
            return;
        }
        int powerMe = fightFragment.getMyCar().getPower();
        int powerOpponent = fightFragment.getOpponent().getPower();
        int move;

        int rand = random.nextInt(10);
        if (powerMe > powerOpponent) move = 2;
        else move = -2;

        if(rand < 4) move = -move;

        doDamage();

        fightFragment.setNewPosition(fightFragment.getMyCar(), move);
        fightFragment.setNewPosition(fightFragment.getOpponent(), move);
    }

    public void dealDamage(Car car, int damage) {
        car.setHealth(car.getHealth() - damage);
        customView.updateHealth();
    }

    public void doDamage(){

        if(!dealingDamage) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dealingDamage = true;
                    if (customView.getMyCar().getSlotTwo().getPartImageId() != null && customView.getMyCar().getSlotTwo().getPartImageId() == R.drawable.chainsaw) {
                        dealDamage(customView.getOpponentCar(),15);
                        //Log.d("BBB", "ALL: me chainsawed opponent " + customView.getOpponentCar().getHealth());
                    }
                    if (customView.getMyCar().getSlotTwo().getPartImageId() != null && customView.getMyCar().getSlotTwo().getPartImageId() == R.drawable.drill) {
                        dealDamage(customView.getOpponentCar(),22);
                        //Log.d("BBB", "ALL: me drilled opponent " + customView.getOpponentCar().getHealth());
                    }
                    if (customView.getOpponentCar().getSlotTwo().getPartImageId() != null && customView.getOpponentCar().getSlotTwo().getPartImageId() == R.drawable.chainsaw_mirror) {
                        dealDamage(customView.getMyCar(),15);
                        //Log.d("BBB", "ALL: opponent chainsawed me " + customView.getMyCar().getHealth());
                    }
                    if (customView.getOpponentCar().getSlotTwo().getPartImageId() != null && customView.getOpponentCar().getSlotTwo().getPartImageId() == R.drawable.drill_mirror) {
                        dealDamage(customView.getMyCar(),22);
                        //Log.d("BBB", "ALL: opponent drilled me " + customView.getMyCar().getHealth());
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dealingDamage = false;
                }
            }).start();
        }
    }

    public void initRocket(Car car) {
        if(car.getCarResourceId() == R.drawable.car1cc || car.getCarResourceId() == R.drawable.car_with_body_cc) {
            customView.setRocketMe(new CarPart(car.getSlotOne().getSlot().left + (car.getSlotOne().getSlot().right - car.getSlotOne().getSlot().left)/2,
                    car.getSlotOne().getSlot().top + (car.getSlotOne().getSlot().bottom - car.getSlotOne().getSlot().top)/3,
                    50, 50,R.drawable.rocket,
                    R.drawable.file_cannon,
                    0,6,50));
        }  else {
            customView.setRocketOpponent(new CarPart(car.getSlotOne().getSlot().right - (car.getSlotOne().getSlot().right - car.getSlotOne().getSlot().left)/2,
                    car.getSlotOne().getSlot().top + (car.getSlotOne().getSlot().bottom - car.getSlotOne().getSlot().top)/3,
                    50, 50,R.drawable.rocket,
                    R.drawable.file_cannon,
                    0,6,50));
        }
    }

    void initRockets() {

        if(!customView.playerControlling || (customView.playerControlling && customView.shootRocket))
        if (fightFragment.isCarHasCannon() && customView.getRocketMe() == null && fightFragment.getTimerThread().carClearToShot) {
            initRocket(customView.getMyCar());
        }

        if (fightFragment.isOpponentHasCannon() && customView.getRocketOpponent() == null &&  fightFragment.getTimerThread().opponentClearToShot) {
            initRocket(customView.getOpponentCar());
        }
    }

    public void moveRocket() {

        initRockets();

        if(fightFragment.isCarHasCannon() && fightFragment.getTimerThread().carClearToShot) {
            if(customView.getRocketMe() != null)customView.getRocketMe().setX(customView.getRocketMe().getX() + 3);
        }

        if(fightFragment.isOpponentHasCannon() && fightFragment.getTimerThread().opponentClearToShot) {
            customView.getRocketOpponent().setX(customView.getRocketOpponent().getX() - 3);
        }
    }

    public boolean checkGameOver() {
        return customView.getMyCar().getHealth() <= 0 || customView.getOpponentCar().getHealth() <= 0;
    }

    @Override
    public void run() {

        Canvas canvas = null;
        while(true) {
            if(checkGameOver()) break;

            if(!isHitBounds()) {
                if (!clash) {
                    setCarPositions();
                } else {
                   moveStronger();
                }
            }

            fightFragment.moveCarParts();
            moveRocket();

            if(!clash) checkIntersect();
            checkRocketsHit();

            try {
                customView.invalidate();

                if(clash) Thread.sleep(6);
                else Thread.sleep(4);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}