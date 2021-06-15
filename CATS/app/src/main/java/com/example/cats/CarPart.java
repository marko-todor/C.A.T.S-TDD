package com.example.cats;

import android.graphics.Rect;

class CarPart {

    private Integer x, y, partResourceId, partInfoResourceId;
    private Integer health, energy, power,width, height;
    private Rect rect = new Rect();

    public Integer getPartInfoResourceId() {
        return partInfoResourceId;
    }

    public void setPartInfoResourceId(Integer partInfoResourceId) {
        this.partInfoResourceId = partInfoResourceId;
    }

    public CarPart(Integer x, Integer y, Integer width, Integer height, Integer partResourceId, Integer partInfoResourceId, Integer health, Integer energy, Integer power) {
        this.x = x;
        this.y = y;
        this.partResourceId = partResourceId;
        this.partInfoResourceId = partInfoResourceId;
        this.health = health;
        this.power = power;
        this.energy = energy;
        this.width = width;
        this.height = height;

        rect = new Rect(x - width/2, y - height/2, x + width/2, y + height/2);

    }


    public static CarPart copyCarPart(CarPart part){
        Integer x = part.getX();
        Integer y = part.getY();
        Integer width = part.getWidth();
        Integer height = part.getHeight();
        Integer partResourceId = part.getPartResourceId();
        Integer partInfoResourceId = part.getPartInfoResourceId();
        Integer health = part.getHealth();
        Integer energy = part.getEnergy();
        Integer power = part.getPower();
        return new CarPart(x, y, width, height, partResourceId, partInfoResourceId,
                health, energy, power);
    };

    public CarPart() {

    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getPartResourceId() {
        return partResourceId;
    }

    public void setPartResourceId(Integer partResourceId) {
        this.partResourceId = partResourceId;
    }
}
