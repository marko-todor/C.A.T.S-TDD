package com.example.cats;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cats.database.AppDatabase;
import com.example.cats.database.User;

import java.util.List;

import static android.view.DragEvent.ACTION_DRAG_ENDED;

public class AvailablePartsAdapter extends RecyclerView.Adapter<AvailablePartsAdapter.MyViewHolder> {



    private static final String DEBUG_TAG = "Debug";
    private List<Image> partImagesRecyclerView; //za recycler view
    private List<Image> partImagesInfo; //za info kada se klikne
    private ModifyCarFragment fragment;
    private int partIndex = -1;
    private MyViewModel model;
    private AppDatabase appDatabase;
    private Rect carRect = new Rect(600, 700, 1500, 1000);

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public AvailablePartsAdapter(List<Image> partImages, List<Image> partImagesInfo, ModifyCarFragment fragment, AppDatabase appDatabase) {
        this.partImagesRecyclerView = partImages;
        this.partImagesInfo = partImagesInfo;
        this.fragment = fragment;
        this.model = new ViewModelProvider(fragment.getActivity()).get(MyViewModel.class);
        this.appDatabase = appDatabase;

//        model.setImageSlots((ImageView) fragment.getActivity().findViewById(R.id.image_slot1),
//                (ImageView) fragment.getActivity().findViewById(R.id.image_slot2));

    }

    public void placePart(boolean slotOne) {


        CarPart part = model.getAvailableCarParts().getValue().get(partIndex);
        if(model.getMyCar().getValue().getEnergy() - part.getEnergy() >= 0) {
            ImageView imageView = null;
            imageView = fragment.getActivity().findViewById(R.id.image_slot1);
            Car car = model.getMyCar().getValue();
            if (slotOne) {
                car.getSlotOne().setAvailable(false);
                car.getSlotOne().setPartImage(imageView);
                car.getSlotOne().setPartImageId(partImagesRecyclerView.get(partIndex).getPictureResourceId());
            } else {
                imageView = fragment.getActivity().findViewById(R.id.image_slot2);
                car.getSlotTwo().setAvailable(false);
                car.getSlotTwo().setPartImage(imageView);
                car.getSlotTwo().setPartImageId(partImagesRecyclerView.get(partIndex).getPictureResourceId());
            }
            //CarPart part = model.getAvailableCarParts().getValue().get(partIndex);
            car.setHealth(car.getHealth() + part.getHealth());
            car.setEnergy(car.getEnergy() - part.getEnergy());
            car.setPower(car.getPower() + part.getPower());
            model.setMyCar(car);

            imageView.setImageResource(partImagesRecyclerView.get(partIndex).getPictureResourceId());
            imageView.setVisibility(View.VISIBLE);
            imageView.requestLayout();


            partImagesRecyclerView.remove(partIndex);
            partImagesInfo.remove(partIndex);

            CarPart carPart = model.removeAvailablePart(partIndex);

            model.addCarPart(carPart);

            partIndex = -1;
            notifyDataSetChanged();
        } else {
            Context context = fragment.getActivity().getApplicationContext();
            CharSequence text = "Not enough energy!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void placeBody() {

        ImageView imageView = null;
        imageView = fragment.getActivity().findViewById(R.id.image_car);
        Car car = model.getMyCar().getValue();
        CarPart part = model.getAvailableCarParts().getValue().get(partIndex);
        car.setHealth(car.getHealth() + part.getHealth());
        car.setEnergy(car.getEnergy() + part.getEnergy());
        car.setPower(car.getPower() + part.getPower());
        model.setMyCar(car);

        imageView.setImageResource(R.drawable.car_with_body);
        imageView.setVisibility(View.VISIBLE);
        imageView.requestLayout();


        partImagesRecyclerView.remove(partIndex);
        partImagesInfo.remove(partIndex);

        CarPart carPart = model.removeAvailablePart(partIndex);

        model.setBody(carPart);

        partIndex = -1;
        notifyDataSetChanged();
    }

    @Override
    public AvailablePartsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {

        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_part_view, parent, false);

        final MyViewHolder vh = new MyViewHolder(v);

        v.setLongClickable(true);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = v.findViewById(R.id.image_view_part);
                fragment.setInfo(partImagesInfo.get((Integer) imageView.getTag()).getPictureResourceId());
            }
        });

        v.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                switch (event.getAction()) {
                    case ACTION_DRAG_ENDED:
                        if(partIndex != -1) {

                            Rect rectPart = new Rect(
                                    ((int) event.getX() - 100),
                                    ((int) event.getY() - 100),
                                    ((int) event.getX() + 100),
                                    ((int) event.getY() + 100));

                            final Car car = model.getMyCar().getValue();
                            if (car.getSlotOne().getSlot().intersects(rectPart.left, rectPart.top, rectPart.right, rectPart.bottom)) {
                                if (car.getSlotOne().isAvailable()
                                        && car.getSlotOne().isCompatible(partImagesRecyclerView.get(partIndex).getPictureResourceId())) {
                                    placePart(true);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            User u = appDatabase.userDao().findByName(model.loggedIn);
                                            appDatabase.partDao().updateIsPlaced(true, car.getSlotOne().getPartImageId(), u.uid);
                                        }
                                    }).start();
                                }
                                else if (car.getSlotTwo().isAvailable()
                                        && car.getSlotTwo().isCompatible(partImagesRecyclerView.get(partIndex).getPictureResourceId())) {
                                    placePart(false);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            User u = appDatabase.userDao().findByName(model.loggedIn);
                                            appDatabase.partDao().updateIsPlaced(true, car.getSlotTwo().getPartImageId(), u.uid);
                                        }
                                    }).start();
                                }
                        }
                            else if (partImagesRecyclerView.get(partIndex).getPictureResourceId() == R.drawable.body && rectPart.intersects(carRect.left,
                                    carRect.top, carRect.right, carRect.bottom)) {
                                placeBody();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        User u = appDatabase.userDao().findByName(model.loggedIn);
                                        appDatabase.userDao().updateBodyUpgraded(model.loggedIn, true);
                                        appDatabase.partDao().updateIsPlaced(true, model.bodyPart.getPartResourceId(), u.uid);

                                    }
                                }).start();
                            }
                        }
                }
                return true;
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ImageView imageView = v.findViewById(R.id.image_view_part);
                ClipData.Item item = new ClipData.Item(imageView.getTag().toString());

                ClipData dragData = new ClipData(
                        imageView.getTag().toString(),
                        new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN },
                        item);

                partIndex = (int) imageView.getTag();

                View.DragShadowBuilder myShadow = new MyDragShadowBuilder(imageView, (Integer) imageView.getTag());

                v.startDrag(dragData,
                        myShadow,
                        null,
                        0
                );
                return v.callOnClick();
            }
        });



        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ImageView imageView = holder.view.findViewById(R.id.image_view_part);
        imageView.setImageResource(partImagesRecyclerView.get(position).getPictureResourceId());
        imageView.setTag(position); // to save a tag for info
    }

    @Override
    public int getItemCount() {
        return partImagesRecyclerView.size();
    }


    private class MyDragShadowBuilder extends View.DragShadowBuilder {

        private ImageView shadow;
        public MyDragShadowBuilder(View v, Integer position) {

            super(v);

            shadow = new ImageView(v.getContext());
            shadow.setImageResource(partImagesRecyclerView.get(position).getPictureResourceId());
            ConstraintLayout constraintLayout = fragment.getActivity().findViewById(R.id.modifyCarLayout);
        }

        @Override
        public void onProvideShadowMetrics (Point size, Point touch) {

            int width, height;

            width = getView().getWidth();

            height = getView().getHeight();

            size.set(width, height);

            touch.set(width / 2, height / 2);

            shadow.setX(touch.x);
            shadow.setY(touch.y);
            shadow.requestLayout();
        }

    }
}


