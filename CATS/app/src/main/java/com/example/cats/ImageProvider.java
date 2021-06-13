package com.example.cats;

import java.util.ArrayList;
import java.util.List;

public class ImageProvider {

    public static List<Image> partImagesRecyclerView = new ArrayList<>();
    public static List<Image> partImagesInfo = new ArrayList<>();

    public static List<Image> getPartImagesRecyclerView() {
        return partImagesRecyclerView;
    }


    public static List<Image> getPartImagesInfo() {
        return partImagesInfo;
    }

    public static final Integer[] carPartResourceId = new Integer[]{
            R.drawable.chainsaw,
            R.drawable.drill,
            R.drawable.saw,
            R.drawable.cannon,
            R.drawable.body
    };

    public static final Integer[] carPartInfoResourceId = new Integer[]{
            R.drawable.file_chainsaw,
            R.drawable.file_drill,
            R.drawable.file_saw,
            R.drawable.file_cannon,
            R.drawable.file_body
    };

    public static final Integer[] carPartMirrorResourceId = new Integer[]{
            R.drawable.chainsaw_mirror,
            R.drawable.drill_mirror,
            R.drawable.saw_mirror,
            R.drawable.cannon_mirror
    };

}
