package com.example.cats;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cats.database.AppDatabase;
import com.example.cats.database.User;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {
    private AppDatabase database;
    private MutableLiveData<Car> myCar;
    private MutableLiveData<List<CarPart>> availableCarParts;
    private MutableLiveData<List<Car>> opponentCars;
    private MutableLiveData<Boolean> musicOn, playerModeOn;
    private MutableLiveData<List<String>> users;
    private MutableLiveData<LastSave> lastSave;
    public String loggedIn;
    private ImageView slot1, slot2;
    public boolean goBack = false;
    public CarPart bodyPart;

    public void setImageSlots(ImageView slot1, ImageView slot2) {
        this.slot1 = slot1;
        this.slot2 = slot2;
    }

    public MyViewModel() {
        this.myCar = new MutableLiveData<Car>();
        this.availableCarParts = new MutableLiveData<List<CarPart>>();
        this.opponentCars = new MutableLiveData<List<Car>>();
        this.musicOn = new MutableLiveData<Boolean>();
        this.playerModeOn = new MutableLiveData<Boolean>();
        this.users = new MutableLiveData<List<String>>();
        this.users.setValue(new ArrayList<String>());
        this.musicOn.setValue(true);
        this.playerModeOn.setValue(false);
        this.lastSave = new MutableLiveData<LastSave>();

    }

    public void setDatabase(final AppDatabase database) {
        this.database = database;

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> usersList = database.userDao().getAll();
                List<String> usernames = new ArrayList<>();
                for (User user : usersList) {
                    usernames.add(user.username);
                }
                users.postValue(usernames);
            }
        }).start();

    }


    public void setLastSave(LastSave lastSave) {
        this.lastSave.postValue(lastSave);
    }
    public LiveData<LastSave> getLastSave() {
        return lastSave;
    }

    public MutableLiveData<List<String>> getUsersLiveData() {
        return this.users;
    }

    public void setMusicOn(boolean set) {
        this.musicOn.postValue(set);
    }

    public void setPlayerModeOn(boolean set) {
        this.playerModeOn.postValue(set);
    }

    public MutableLiveData<Boolean> getMusicOnLiveData() {
        return this.musicOn;
    }

    public MutableLiveData<Boolean> getPlayerModeLiveData() {
        return this.playerModeOn;
    }

    public LiveData<Car> getMyCar() {
        return myCar;
    }

    public LiveData<List<Car>> getOpponentCars() {
        return opponentCars;
    }

    public LiveData<List<CarPart>> getAvailableCarParts() {
        return availableCarParts;
    }

    public void addCarPart(CarPart part) {
        this.myCar.getValue().getCarParts().add(part);
        this.myCar.postValue(this.myCar.getValue());
    }

    public void addOpponentCars(List<Car> cars) {
        this.opponentCars.postValue(cars);
    }

    public void removeCarPart(CarPart carPart) {
        this.myCar.getValue().getCarParts().remove(carPart);
        this.myCar.postValue(this.myCar.getValue());
    }

    public void setMyCar(Car myCar) {
        this.myCar.postValue(myCar);
    }

    public void setAvailableCarParts(List<CarPart> carParts) {
        this.availableCarParts.postValue(carParts);
    }

    public void addAvailablePart(CarPart part) {
        this.availableCarParts.getValue().add(part);
        this.availableCarParts.postValue(this.availableCarParts.getValue());
    }

    public CarPart removeAvailablePart(int index) {
        CarPart carPart = this.availableCarParts.getValue().remove(index);
        this.availableCarParts.postValue(this.availableCarParts.getValue());
        return carPart;
    }



    public void login(final String username) {
        bodyPart = null;
        loggedIn = username;
        ImageProvider.partImagesRecyclerView.clear();
        ImageProvider.partImagesInfo.clear();
        Slot slotUp = myCar.getValue().getSlotOne();
        slotUp.setAvailable(true);
        slotUp.setPartImage(null);
        Slot slotDown = myCar.getValue().getSlotTwo();
        slotDown.setAvailable(true);
        slotDown.setPartImage(null);
        myCar.getValue().getCarParts().clear();
        myCar.getValue().setHealth(350);
        myCar.getValue().setPower(0);
        myCar.getValue().setEnergy(7);

        User user = database.userDao().getUserWithParts(username);
        List<com.example.cats.database.CarPart> parts = user.availableParts;
        List<CarPart> tmp = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            final CarPart part = new CarPart(0, 0, 0, 0, parts.get(i).partResourceId, parts.get(i).partInfoResourceId, parts.get(i).health, parts.get(i).energy, parts.get(i).power);
            if (!parts.get(i).isPlaced) {
                tmp.add(part);
                Image image = new Image(parts.get(i).partResourceId);
                ImageProvider.partImagesRecyclerView.add(image);
                image = new Image(parts.get(i).partInfoResourceId);
                ImageProvider.partImagesInfo.add(image);
            } else {
                if (myCar.getValue().getSlotOne().isCompatible(part.getPartResourceId())) {
                    Car oldCar = myCar.getValue();
                    oldCar.getSlotOne().setPartImageId(part.getPartResourceId());
                    slot1.post(new Runnable() {
                        @Override
                        public void run() {
                            slot1.setImageResource(part.getPartResourceId());
                            slot1.setVisibility(View.VISIBLE);
                            slot1.requestLayout();
                        }
                    });
                    oldCar.getSlotOne().setPartImage(slot1);
                    oldCar.getSlotOne().setAvailable(false);
                    oldCar.setHealth(oldCar.getHealth() + part.getHealth());
                    oldCar.setEnergy(oldCar.getEnergy() - part.getEnergy());
                    oldCar.setPower(oldCar.getPower() + part.getPower());
                    myCar.postValue(oldCar);

                    addCarPart(part);
                } else if (myCar.getValue().getSlotTwo().isCompatible(part.getPartResourceId())) {
                    Car oldCar = myCar.getValue();
                    oldCar.getSlotTwo().setPartImageId(part.getPartResourceId());
                    slot2.post(new Runnable() {
                        @Override
                        public void run() {
                            slot2.setImageResource(part.getPartResourceId());
                            slot2.setVisibility(View.VISIBLE);
                            slot2.requestLayout();
                        }
                    });
                    oldCar.getSlotTwo().setPartImage(slot2);
                    oldCar.getSlotTwo().setAvailable(false);
                    oldCar.setHealth(oldCar.getHealth() + part.getHealth());
                    oldCar.setEnergy(oldCar.getEnergy() - part.getEnergy());
                    oldCar.setPower(oldCar.getPower() + part.getPower());
                    myCar.postValue(oldCar);
                    addCarPart(part);
                } else if(part.getPartResourceId() == R.drawable.body) {
                    Car oldCar = myCar.getValue();
                    oldCar.setHealth(oldCar.getHealth() + part.getHealth());
                    oldCar.setEnergy(oldCar.getEnergy() + part.getEnergy());
                    oldCar.setPower(oldCar.getPower() + part.getPower());
                    bodyPart = part;
                }
            }
        }

        availableCarParts.postValue(tmp);

    }


    public void register(String username) {
        bodyPart = null;
        List<com.example.cats.database.CarPart> parts = new ArrayList<>();



        com.example.cats.database.CarPart drillerPart = new com.example.cats.database.CarPart();
        drillerPart.health = 0;
        drillerPart.energy = 5;
        drillerPart.power = 22;
        drillerPart.partResourceId = ImageProvider.carPartResourceId[1];
        drillerPart.partInfoResourceId = ImageProvider.carPartInfoResourceId[1];
        drillerPart.isPlaced = false;

        parts.add(drillerPart);

        com.example.cats.database.CarPart sawPart = new com.example.cats.database.CarPart();
        sawPart.health = 0;
        sawPart.energy = 8;
        sawPart.power = 35;
        sawPart.partResourceId = ImageProvider.carPartResourceId[2];
        sawPart.partInfoResourceId = ImageProvider.carPartInfoResourceId[2];
        sawPart.isPlaced = false;

        parts.add(sawPart);



        com.example.cats.database.CarPart bodyPart = new com.example.cats.database.CarPart();
        bodyPart.health = 70;
        bodyPart.energy = 7;
        bodyPart.power = 0;
        bodyPart.partResourceId = ImageProvider.carPartResourceId[4];
        bodyPart.partInfoResourceId = ImageProvider.carPartInfoResourceId[4];
        bodyPart.isPlaced = false;

        parts.add(bodyPart);


        User u = new User();
        u.username = username;
        u.gamesLost = 0;
        u.gamesWon = 0;
        u.newBox = false;
        u.avatar = 1;

        database.userDao().insertUserWithParts(u, parts);

        List<String> oldUsersList = users.getValue();
        oldUsersList.add(username);
        users.postValue(oldUsersList);

    }

    public void setBody(CarPart carPart) {
        bodyPart = carPart;
    }
}

