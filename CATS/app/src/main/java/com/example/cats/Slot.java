package com.example.cats;

import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Slot {

    private Rect slot;
    private boolean available = true;
    private ImageView partImage;
    private Integer partImageId;
    private List<CarPart> compatibleParts = new ArrayList<>();

    public Integer getPartImageId() {
        return partImageId;
    }

    public void setPartImageId(Integer partImageId) {
        this.partImageId = partImageId;
    }

    public ImageView getPartImage() {
        return partImage;
    }

    public List<CarPart> getCompatibleParts() {
        return compatibleParts;
    }


    public void setCompatibleParts(List<CarPart> compatibleParts) {
        this.compatibleParts = compatibleParts;
    }

    public void setPartImage(ImageView partImage) {
        this.partImage = partImage;
    }


    public Rect getSlot() {
        return slot;
    }

    public void setSlot(Rect slot) {
        this.slot = slot;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public  boolean isCompatible(int pictureResourceId) {
        for(int i = 0; i < compatibleParts.size(); i++) {
            if(compatibleParts.get(i).getPartResourceId() == pictureResourceId) return true;
        }
        return false;
    }

}
