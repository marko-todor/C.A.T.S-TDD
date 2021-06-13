package com.example.cats;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cats.database.AppDatabase;
import com.example.cats.database.CarPart;
import com.example.cats.database.User;

import java.util.List;
import java.util.Random;


public class NewPartFragment extends Fragment {

    private ImageView background;
    private MyViewModel model;
    private AppDatabase db;
    private ImageView ok;

    public NewPartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_part, container, false);


        ok = view.findViewById(R.id.button_ok_new_weapon);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(NewPartFragment.this).navigate(R.id.action_newPartFragment_to_garageFragment);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                background = view.findViewById(R.id.imageView_newPart);

                db = ((MainActivity)getActivity()).db;

                model = ((MainActivity)getActivity()).model;

                final User user = db.userDao().getUserWithParts(model.loggedIn);
                List<CarPart> partList = user.availableParts;

                Random random = new Random();

                boolean newPartFound;
                do {
                    newPartFound = true;
                    int newPartId = random.nextInt(ImageProvider.carPartResourceId.length);

                    final Integer newPartImage = ImageProvider.carPartResourceId[newPartId];

                    for (int i = 0; i < partList.size(); i++) {
                        if (newPartImage.equals(partList.get(i).partResourceId)) {
                            newPartFound = false;
                        }
                    }






                    if (newPartFound == true) {
                        //DODAJ NOVI DEO
                        background.post(new Runnable() {
                            @Override
                            public void run() {
                                switch(newPartImage) {
                                    case R.drawable.chainsaw: {
                                        CarPart chainsaw = new CarPart();
                                        chainsaw.isPlaced = false;
                                        chainsaw.partResourceId = ImageProvider.carPartResourceId[0];
                                        chainsaw.partInfoResourceId = ImageProvider.carPartInfoResourceId[0];
                                        chainsaw.power = 15;
                                        chainsaw.health = 0;
                                        chainsaw.energy = 4;
                                        chainsaw.userId = user.uid;

                                        addToDb(chainsaw);
                                        background.setImageResource(R.drawable.new_weapon_chainsaw);
                                        break;
                                    }
                                    case R.drawable.drill: {
                                        CarPart driller = new CarPart();
                                        driller.isPlaced = false;
                                        driller.partResourceId = ImageProvider.carPartResourceId[1];
                                        driller.partInfoResourceId = ImageProvider.carPartInfoResourceId[1];
                                        driller.power = 22;
                                        driller.health = 0;
                                        driller.energy = 5;
                                        driller.userId = user.uid;
                                        addToDb(driller);
                                        background.setImageResource(R.drawable.new_weapon_driller);
                                        break;
                                    }
                                    case R.drawable.saw: {
                                        CarPart saw = new CarPart();
                                        saw.isPlaced = false;
                                        saw.partResourceId = ImageProvider.carPartResourceId[2];
                                        saw.partInfoResourceId = ImageProvider.carPartInfoResourceId[2];
                                        saw.power = 35;
                                        saw.health = 0;
                                        saw.energy = 8;
                                        saw.userId = user.uid;
                                        addToDb(saw);
                                        background.setImageResource(R.drawable.new_weapon_saw);
                                        break;
                                    }
                                    case R.drawable.cannon: {
                                        CarPart cannon = new CarPart();
                                        cannon.isPlaced = false;
                                        cannon.partResourceId = ImageProvider.carPartResourceId[3];
                                        cannon.partInfoResourceId = ImageProvider.carPartInfoResourceId[3];
                                        cannon.power = 14;
                                        cannon.health = 0;
                                        cannon.energy = 6;
                                        cannon.userId = user.uid;
                                        addToDb(cannon);
                                        background.setImageResource(R.drawable.new_weapon_rocket);
                                        break;
                                    }
                                }
                            }
                        });

                    }
                } while (!newPartFound);
            }
        }).start();




        return view;
    }

    private void addToDb(final CarPart part) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.partDao().insert(part);
            }
        }).start();
    }

}
