package com.example.cats;

import android.os.Bundle;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ModifyCarFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView energy, power, health;
    View view;
    private static GestureDetectorCompat mDetector;
    private MyViewModel model;

    public ModifyCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_modify_car, container, false);

        power = view.findViewById(R.id.textView_power);
        health = view.findViewById(R.id.textView_health);
        energy = view.findViewById(R.id.textView_energy);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                ModifyCarFragment.mDetector.onTouchEvent(event);
                return true;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_parts);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AvailablePartsAdapter(ImageProvider.getPartImagesRecyclerView(),
                ImageProvider.getPartImagesInfo(),this, ((MainActivity)getActivity()).db);

        recyclerView.setAdapter(mAdapter);

        model = new ViewModelProvider(getActivity()).get(MyViewModel.class);

        model.goBack = true;

        model.getMyCar().observe(getActivity(), new Observer<Car>() {
            @Override
            public void onChanged(Car car) {
                power.setText(car.getPower().toString());
                energy.setText(car.getEnergy().toString());
                health.setText(car.getHealth().toString());
            }
        });

        mDetector = new GestureDetectorCompat(getActivity(), new MyGestureListener(mAdapter,model,this));

        Car car = model.getMyCar().getValue();

        ImageView myCarIV = view.findViewById(R.id.image_car);
        if(model.bodyPart != null && model.bodyPart.getPartResourceId() == R.drawable.body) {
            car.setCarResourceId(R.drawable.car_with_body);
        } else {
            car.setCarResourceId(R.drawable.car1);
        }
        myCarIV.setImageResource(car.getCarResourceId());

        if(car.getSlotOne().getPartImage() != null) {
            ImageView imageView = view.findViewById(R.id.image_slot1);
            imageView.setImageResource(car.getSlotOne().getPartImageId());
            imageView.setVisibility(View.VISIBLE);
            Slot oldSlot = car.getSlotOne();
            oldSlot.setPartImage(imageView);
            car.setSlotOne(car.getSlotOne());
        }

        if(car.getSlotTwo().getPartImage() != null) {
            ImageView imageView = view.findViewById(R.id.image_slot2);
            imageView.setImageResource(car.getSlotTwo().getPartImageId());
            imageView.setVisibility(View.VISIBLE);
            Slot oldSlot = car.getSlotTwo();
            oldSlot.setPartImage(imageView);
            car.setSlotTwo(car.getSlotTwo());
        }

        return view;
    }

    public void setInfo(int id) {

        ImageView imageView = view.findViewById(R.id.image_info);
        imageView.setImageResource(id);
    }

    @Override
    public void onPause() {
        model.goBack = false;
        super.onPause();
    }
}
