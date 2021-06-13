package com.example.cats;

import android.content.Context;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cats.database.AppDatabase;
import com.example.cats.database.User;


public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String DEBUG_TAG = "Gestures";
    private RecyclerView.Adapter mAdapter;
    private MyViewModel model;
    private ModifyCarFragment modifyCarFragment;
    private Rect carRect = new Rect(600, 700, 1200, 1000);

    public MyGestureListener(RecyclerView.Adapter mAdapter, MyViewModel model, ModifyCarFragment modifyCarFragment) {
        this.mAdapter = mAdapter;
        this.model = model;
        this.modifyCarFragment = modifyCarFragment;
    }

    public int intersectionWithFullSlot(int x, int y) {
        Car car = model.getMyCar().getValue();
        if(car.getSlotOne().getSlot().intersects(x - 30, y - 30, x + 30, y + 30)
                    && (!car.getSlotOne().isAvailable())) return 1;

        if (car.getSlotTwo().getSlot().intersects(x - 30, y - 30, x + 30, y + 30)
                && (!car.getSlotTwo().isAvailable())) return 2;

        return -1;
    }

    public void removePart(final Slot slot, final CarPart partOnCar, final int partId) {

        Car car = model.getMyCar().getValue();


        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = ((MainActivity)modifyCarFragment.getActivity()).db;
                User u = db.userDao().findByName(model.loggedIn);
                db.partDao().updateIsPlaced(false, partId , u.uid);
            }
        }).start();

        if(slot != null) {
            ImageView imageView = slot.getPartImage();
            imageView.setVisibility(View.INVISIBLE);
            slot.setPartImage(null);
            slot.setPartImageId(-1);
            slot.setAvailable(true);
        }


        if(slot != null) {
            car.setEnergy(car.getEnergy() + partOnCar.getEnergy());
        } else {
            car.setEnergy(car.getEnergy() - partOnCar.getEnergy());
        }
        car.setPower(car.getPower() - partOnCar.getPower());
        car.setHealth(car.getHealth() - partOnCar.getHealth());
        model.addAvailablePart(partOnCar);
        model.removeCarPart(partOnCar);

        ImageProvider.getPartImagesRecyclerView().clear();
        ImageProvider.getPartImagesInfo().clear();

        for (int j = 0; j < model.getAvailableCarParts().getValue().size(); j++) {
            Image image = new Image(model.getAvailableCarParts().getValue().get(j).getPartResourceId());
            ImageProvider.partImagesRecyclerView.add(image);

            Image imageInfo = new Image(model.getAvailableCarParts().getValue().get(j).getPartInfoResourceId());
            ImageProvider.partImagesInfo.add(imageInfo);
        }



        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        for(int i = 0; i < model.getMyCar().getValue().getCarParts().size(); i++) {
                int outcome = intersectionWithFullSlot((int)event.getX(), (int)event.getY());
                Car car = model.getMyCar().getValue();
                CarPart carPart = model.getMyCar().getValue().getCarParts().get(i);
                if(outcome == 1 && model.getMyCar().getValue().getCarParts().get(i).getPartResourceId()
                        .equals(car.getSlotOne().getPartImageId())) {
                    removePart(car.getSlotOne(), carPart, model.getMyCar().getValue().getCarParts().get(i).getPartResourceId());
                    car.getSlotOne().setAvailable(true);
                } else if (outcome == 2 && model.getMyCar().getValue().getCarParts().get(i).getPartResourceId()
                        .equals(car.getSlotTwo().getPartImageId())) {
                    removePart(car.getSlotTwo(), carPart, model.getMyCar().getValue().getCarParts().get(i).getPartResourceId());
                    car.getSlotTwo().setAvailable(true);
                }
        }
        if(checkCarClicked((int)event.getX(), (int)event.getY())) {
            if(model.bodyPart != null && model.bodyPart.getPartResourceId() == R.drawable.body) {
                Car car = model.getMyCar().getValue();
                if(car.getEnergy() - model.bodyPart.getEnergy() > 0) {

                    removePart(null, model.bodyPart, model.bodyPart.getPartResourceId());
                    modifyCarFragment.getActivity().findViewById(R.id.image_car).post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imageView = modifyCarFragment.getActivity().findViewById(R.id.image_car);
                            imageView.setImageResource(R.drawable.car1);
                        }
                    });

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase appDatabase = ((MainActivity) modifyCarFragment.getActivity()).db;
                            ((MainActivity) modifyCarFragment.getActivity()).db.userDao().updateBodyUpgraded(model.loggedIn, false);
                            User u = appDatabase.userDao().findByName(model.loggedIn);
                            appDatabase.partDao().updateIsPlaced(false, model.bodyPart.getPartResourceId(), u.uid);
                            model.bodyPart = null;
                        }
                    }).start();
                } else {
                    Context context = modifyCarFragment.getActivity().getApplicationContext();
                    CharSequence text = "You need to remove the weapon first(LOW ENERGY)!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        }
        return super.onDoubleTap(event);
    }

    private boolean checkCarClicked(int x, int y) {
        return carRect.intersects(x - 30, y - 30, x + 30, y + 30);
    }
}