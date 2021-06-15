package com.example.cats;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.List;

public class Car {

    private int x, y;
    private int height, width;
    private int carResourceId;
    private List<CarPart> carParts;
    private Integer health, energy, power;
    private Slot slotOne = new Slot();
    private Slot slotTwo = new Slot();

    public Car(int x, int y, int carResourceId, List<CarPart> carParts, Integer health, Integer energy, Integer power, Integer width, Integer height) {
        this.x = x;
        this.y = y;
        this.carResourceId = carResourceId;
        this.carParts = carParts;
        this.health = health;
        this.energy = energy;
        this.power = power;
        this.slotOne.setSlot(new Rect(x + width/4,
                y - height/2 - 30,
                x + width/2,
                y + height/4 - 30));
        this.slotTwo.setSlot(new Rect(x + width/4,
                y + 0,
                x + width/2,
                y + height/2 + 30));
        this.width = width;
        this.height = height;
    }

    public Car(Car car) {
        List<CarPart> carPartsCopy = new ArrayList<>();
        for (CarPart part: car.carParts) {
            carPartsCopy.add(CarPart.copyCarPart(part));
        }
        this.x = car.x;
        this.y = car.y;
        this.carResourceId = car.carResourceId;
        this.carParts = carPartsCopy;
        this.health = car.health;
        this.energy = car.energy;
        this.power = car.power;
        this.width = car.width;
        this.height = car.height;

        this.slotOne = new Slot();

        this.slotOne = new Slot(car.slotOne);
        this.slotTwo = new Slot(car.slotTwo);
    }

    public Slot getSlotOne() {
        return slotOne;
    }

    public void setSlotOne(Slot slotOne) {
        this.slotOne = slotOne;
    }

    public Slot getSlotTwo() {
        return slotTwo;
    }

    public void setSlotTwo(Slot slotTwo) {
        this.slotTwo = slotTwo;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCarResourceId() {
        return carResourceId;
    }

    public void setCarResourceId(int carResourceId) {
        this.carResourceId = carResourceId;
    }

    public List<CarPart> getCarParts() {
        return carParts;
    }

    public void setCarParts(List<CarPart> carParts) {
        this.carParts = carParts;
    }
}
