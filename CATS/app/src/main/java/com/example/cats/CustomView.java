package com.example.cats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomView extends View {

    private int maxHealthMyCar;
    private int maxHealthOpponentCar;
    private Car myCar;
    private Car opponentCar;
    private List<Bitmap> resizedPartsMy = new ArrayList<>();
    private List<Bitmap> resizedPartsOpponent = new ArrayList<>();
    private List<Bitmap> resizedHealthMy = new ArrayList<>();
    private List<Bitmap> resizedHealthOpponent = new ArrayList<>();
    private Bitmap resizedCarMy;
    private Bitmap resizedCarOpponent;
    private Matrix positionPartMe = new Matrix();
    private Matrix positionPartOpponent = new Matrix();
    private float rotateClockwise = 1;
    private float rotateAnticlockwise = -1;
    private Bitmap rocketBitmapMe;
    private Bitmap rocketBitmapOpponent;
    private CarPart rocketMe = null, rocketOpponent = null;
    private Bitmap explosionBitmapMe;
    private Bitmap explosionBitmapOpponent;
    private boolean drawExplosionMe = false;
    private boolean drawExplosionOpponent = false;
    private boolean scratchedMe = false;
    private boolean scratchedOpponent = false;
    private Bitmap scratchMe;
    private Bitmap scratchOpponent;
    private FightFragment fightFragment;
    private Bitmap wall_left;
    private Bitmap wall_right;
    private Rect wall_leftRect, wall_rightRect;
    private boolean gameOverRedirect = false;
    private GestureDetectorCompat mDetector;
    public boolean playerControlling = false, bladeSpinning = false, shootRocket = false;


    public FightFragment getFightFragment() {
        return fightFragment;
    }

    public void setFightFragment(FightFragment fightFragment) {
        this.fightFragment = fightFragment;
    }

    public boolean isDrawExplosionMe() {
        return drawExplosionMe;
    }

    public boolean isDrawExplosionOpponent() {
        return drawExplosionOpponent;
    }

    public void setExplosion(Car car) {
        if(car.getCarResourceId() == myCar.getCarResourceId()) {
            drawExplosionMe = true;
        } else {
            drawExplosionOpponent = true;
        }
    }

    public void clearExplosion(Car car) {
        if(car.getCarResourceId() == myCar.getCarResourceId()) {
            drawExplosionMe = false;
        } else {
            drawExplosionOpponent = false;
        }
    }

    public void resizeImageParts() {

        mDetector = new GestureDetectorCompat(fightFragment.getActivity(), new PlayerMotionGestureListener(this));
        setLongClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        Bitmap bmpCar = BitmapFactory.decodeResource(getResources(), myCar.getCarResourceId());
        resizedCarMy = Bitmap.createScaledBitmap(bmpCar, myCar.getWidth()
                , myCar.getHeight(), false);

        bmpCar = BitmapFactory.decodeResource(getResources(), opponentCar.getCarResourceId());
        resizedCarOpponent = Bitmap.createScaledBitmap(bmpCar, opponentCar.getWidth()
                , opponentCar.getHeight(), false);

        for (int i = 0; i < myCar.getCarParts().size(); i++) {
            CarPart carPart = myCar.getCarParts().get(i);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), carPart.getPartResourceId());
            Bitmap resizedBmp = Bitmap.createScaledBitmap(bmp, carPart.getWidth()
                    , carPart.getHeight(), false);
            resizedPartsMy.add(resizedBmp);
        }

        for (int i = 0; i < opponentCar.getCarParts().size(); i++) {
            CarPart carPart = opponentCar.getCarParts().get(i);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), carPart.getPartResourceId());
            Bitmap resizedBmp = Bitmap.createScaledBitmap(bmp, carPart.getWidth()
                    , carPart.getHeight(), false);
            resizedPartsOpponent.add(resizedBmp);
        }
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
        rocketBitmapMe = Bitmap.createScaledBitmap(bmp, 50
                , 50, false);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.rocket); //TREBA MIRROR
        rocketBitmapOpponent = Bitmap.createScaledBitmap(bmp, 50
                , 50, false);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
        explosionBitmapMe = Bitmap.createScaledBitmap(bmp, 100
                , 100, false);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
        explosionBitmapOpponent = Bitmap.createScaledBitmap(bmp, 100
                , 100, false);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.scratch);
        scratchMe = Bitmap.createScaledBitmap(bmp, 100
                , 100, false);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.scratch_mirror);
        scratchOpponent = Bitmap.createScaledBitmap(bmp, 100
                , 100, false);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        wall_left = Bitmap.createScaledBitmap(bmp, 1024
                , 800, false);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        wall_right = Bitmap.createScaledBitmap(bmp, 1024
                , 800, false);

        resizeHealth();


        wall_leftRect = new Rect(-1024,0,0,800);
        wall_rightRect = new Rect(2350,0,3374, 800);

        fightFragment.getModel().getPlayerModeLiveData().observe(fightFragment.getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                playerControlling = aBoolean;
            }
        });
    }

    public void resizeHealth() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar0);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar10);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar20);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar30);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar40);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar50);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar60);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar70);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar80);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar90);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar100);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthMy.add(bmp);


        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar0_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar10_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar20_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar30_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar40_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar50_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar60_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar70_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar80_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar90_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.health_bar100_mirror);
        bmp = Bitmap.createScaledBitmap(bmp, 1020, 542, false);
        resizedHealthOpponent.add(bmp);
    }

    public Bitmap getRocketBitmapMe() {
        return rocketBitmapMe;
    }

    public void setRocketBitmapMe(Bitmap rocketBitmapMe) {
        this.rocketBitmapMe = rocketBitmapMe;
    }

    public Bitmap getRocketBitmapOpponent() {
        return rocketBitmapOpponent;
    }

    public void setRocketBitmapOpponent(Bitmap rocketBitmapOpponent) {
        this.rocketBitmapOpponent = rocketBitmapOpponent;
    }

    public CarPart getRocketMe() {
        return rocketMe;
    }

    public void setRocketMe(CarPart rocketMe) {
        this.rocketMe = rocketMe;
    }

    public CarPart getRocketOpponent() {
        return rocketOpponent;
    }

    public void setRocketOpponent(CarPart rocketOpponent) {
        this.rocketOpponent = rocketOpponent;
    }

    public Car getOpponentCar() {
        return opponentCar;
    }

    public void setOpponentCar(Car opponentCar) {
        this.opponentCar = opponentCar;
        this.maxHealthOpponentCar = opponentCar.getHealth();
    }

    public Car getMyCar() {
        return myCar;
    }

    public void setMyCar(Car car) {
        this.myCar = car;
        this.maxHealthMyCar = car.getHealth();
    }

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void rotate(Bitmap partBitmap, CarPart carPart, float rotation) {
        Matrix matrix = new Matrix();

        if(rotateClockwise > 100 && 0 < rotateClockwise % 351 && rotateClockwise % 351 < 30 &&
                (!playerControlling || (playerControlling && bladeSpinning))) {
            if(!scratchedMe) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CustomView.this.scratchedMe = true;
                        opponentCar.setHealth(opponentCar.getHealth() - 35);
                        updateHealth();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CustomView.this.scratchedMe = false;
                    }
                }).start();
            }
        }

        if(rotateAnticlockwise < -100 && 0 < -rotateAnticlockwise % 351 && -rotateAnticlockwise % 351 < 30) {
            if(!scratchedOpponent) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CustomView.this.scratchedOpponent = true;
                        myCar.setHealth(myCar.getHealth() - 35);
                        updateHealth();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CustomView.this.scratchedOpponent = false;
                    }
                }).start();
            }
        }

            if (rotation > 0) {

                    matrix.postRotate(rotateClockwise, 5, partBitmap.getHeight() / 2 + 14);
                    matrix.postTranslate(carPart.getX(), carPart.getY());
                    positionPartMe.set(matrix);
                if(!playerControlling || (playerControlling && bladeSpinning)) {
                    rotateClockwise += 2;
                }
            } else {
                matrix.postRotate(rotateAnticlockwise, partBitmap.getWidth() - 5, partBitmap.getHeight() / 2 + 14);
                matrix.postTranslate(carPart.getX(), carPart.getY());
                positionPartOpponent.set(matrix);
                rotateAnticlockwise -= 2;
            }


        if(playerControlling && bladeSpinning && rotateClockwise % 360 >= 0 && rotateClockwise % 360 < 2) {
            bladeSpinning = false;
            //updateHealth();
        }

    }

    public boolean checkGameOver() {
        return myCar.getHealth() <= 0 || opponentCar.getHealth() <= 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {

            Paint paint = new Paint();
            paint.setAlpha(240);
            canvas.drawBitmap(resizedCarMy, myCar.getX(), myCar.getY(), paint);

            canvas.drawBitmap(resizedCarOpponent, opponentCar.getX(), opponentCar.getY(), paint);

            for (int i = 0; i < myCar.getCarParts().size(); i++) {
                CarPart carPart = myCar.getCarParts().get(i);
                if (carPart.getPartResourceId().equals(R.drawable.saw)) {
                    rotate(resizedPartsMy.get(i), carPart, rotateClockwise);
                    canvas.drawBitmap(resizedPartsMy.get(i), positionPartMe, null);
                } else canvas.drawBitmap(resizedPartsMy.get(i), carPart.getX(),
                        carPart.getY(), null);
            }
//            Paint paint1 = new Paint();
//            paint1.setColor(Color.BLACK);
//            canvas.drawRect(opponentCar.getSlotOne().getSlot(),paint);
//            canvas.drawRect(opponentCar.getSlotTwo().getSlot(),paint);
            for (int i = 0; i < opponentCar.getCarParts().size(); i++) {
                CarPart carPart = opponentCar.getCarParts().get(i);
                if (carPart.getPartResourceId().equals(R.drawable.saw_mirror)) {
                    rotate(resizedPartsOpponent.get(i), carPart, rotateAnticlockwise);
                    canvas.drawBitmap(resizedPartsOpponent.get(i), positionPartOpponent, null);
                } else canvas.drawBitmap(resizedPartsOpponent.get(i), carPart.getX(),
                        carPart.getY(), null);
            }

            if (rocketMe != null) {
                canvas.drawBitmap(rocketBitmapMe, rocketMe.getX(),
                        rocketMe.getY(), null);
            }
            if (rocketOpponent != null) {
                canvas.drawBitmap(rocketBitmapOpponent, rocketOpponent.getX(),
                        rocketOpponent.getY(), null);
            }

            if (drawExplosionMe) {
                canvas.drawBitmap(explosionBitmapMe, myCar.getX() + myCar.getWidth() / 2,
                        myCar.getY() + myCar.getHeight() / 2 - 80, null);
            }

            if (drawExplosionOpponent) {
                canvas.drawBitmap(explosionBitmapOpponent, opponentCar.getX() + opponentCar.getWidth() / 2,
                        opponentCar.getY() + opponentCar.getHeight() / 2 - 120, null);
            }

            if (scratchedMe) {
                canvas.drawBitmap(scratchMe, myCar.getX() + myCar.getWidth() + 80,
                        myCar.getY() + myCar.getHeight() / 2 - 30, null);

            }

            if (scratchedOpponent) {
                canvas.drawBitmap(scratchOpponent, opponentCar.getX() - 200,
                        opponentCar.getY() + opponentCar.getHeight() / 2 - 60, null);
            }
            if(fightFragment.isFightOver()) {
                wall_leftRect.left++;
                wall_leftRect.right++;
                canvas.drawBitmap(wall_left, wall_leftRect.left,220,null);
                wall_rightRect.left--;
                wall_rightRect.right--;
                canvas.drawBitmap(wall_right, wall_rightRect.left,220,null);

                if(wall_leftRect.contains(myCar.getX(), myCar.getY())) {
                    myCar.setHealth(0);
                    updateHealth();
                }
                if(wall_rightRect.contains(opponentCar.getX() + opponentCar.getWidth(), opponentCar.getY())) {
                    opponentCar.setHealth(0);
                    updateHealth();
                }
            }

            int playerHealthBarIndex = (int)((float)myCar.getHealth()/maxHealthMyCar *10);
            int opponentHealthBarIndex = (int)((float)opponentCar.getHealth()/maxHealthOpponentCar *10);

            if(playerHealthBarIndex < 0) playerHealthBarIndex = 0;
            if(opponentHealthBarIndex < 0) opponentHealthBarIndex = 0;

            canvas.drawBitmap(resizedHealthMy.get(playerHealthBarIndex), 50,-120,null);
            canvas.drawBitmap(resizedHealthOpponent.get(opponentHealthBarIndex), 1270,-120,null);
            super.onDraw(canvas);
    }

    public void updateHealth() {
        float meHealthPercentage = ((float) myCar.getHealth()) / maxHealthMyCar;
        float opponentHealthPercentage = ((float) opponentCar.getHealth()) / maxHealthOpponentCar;

        if (fightFragment != null && fightFragment.getActivity() != null) {

            if(meHealthPercentage <= 0.05 && !gameOverRedirect) {
                gameOverRedirect = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fightFragment.setFightOver(true);
                        post(new Runnable() {
                            @Override
                            public void run() {
                                fightFragment.explosionField.explode(CustomView.this);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        fightFragment.navigateGameOver();
                    }
                }).start();
            }

            else if(opponentHealthPercentage <= 0.05 && !gameOverRedirect) {
                gameOverRedirect = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fightFragment.setFightOver(true);
                        post(new Runnable() {
                            @Override
                            public void run() {
                                fightFragment.explosionField.explode(CustomView.this);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        fightFragment.navigateGameWon();
                    }
                }).start();
            }
        }
    }

}
