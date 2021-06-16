package com.example.cats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cats.database.Box;
import com.example.cats.database.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GarageFragment extends Fragment {

    private View view;
    private MyViewModel model;
    private ImageView startFight;
    private List<ImageView> boxes = new ArrayList<>();
    private List<Integer> slotTimes = new ArrayList<>();
    private List<TextView> slotTimesTV = new ArrayList<>();
    private List<Boolean> slotsAvailable = new ArrayList<>();
    private List<ImageView> boxesImages = new ArrayList<>();
    private List<ImageView> time_tags = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
    private Boolean loadingBoxes = new Boolean(true);
    private Activity mActivity;
    private LastSave lastSave;
    private List<ImageView> avatarExtraPictures = new ArrayList<>();
    private TextView chooseCharacter;
    private List<ImageView> avatars = new ArrayList<>();
    private boolean choosingCharacter = false;

    public GarageFragment() {

    }



    public void loadUserBoxes(final String username) {
        synchronized (loadingBoxes) {
            User user = ((MainActivity) mActivity).db.userDao().findByName(username);
            if(user != null) {
                List<Box> boxesDB = ((MainActivity) mActivity).db.userDao().getBoxList(user.uid);
                for (final Box box : boxesDB) {
                    if (convertDate(box.timeToOpen) > 0) {
                        slotTimes.set(box.slotNumber, convertDate(box.timeToOpen));
                        slotsAvailable.set(box.slotNumber, false);

                        time_tags.get(box.slotNumber).post(new Runnable() {
                            @Override
                            public void run() {
                                time_tags.get(box.slotNumber).setVisibility(View.VISIBLE);
                            }
                        });

                        slotTimesTV.get(box.slotNumber).post(new Runnable() {
                            @Override
                            public void run() {
                                slotTimesTV.get(box.slotNumber).setVisibility(View.VISIBLE);
                                slotTimesTV.get(box.slotNumber).setText(Integer.toString(convertDate(box.timeToOpen)));
                            }
                        });
                        boxes.get(box.slotNumber).setImageResource(R.drawable.box);
                    } else {
                        boxesImages.get(box.slotNumber).post(new Runnable() {
                            @Override
                            public void run() {
                                boxesImages.get(box.slotNumber).setEnabled(true);
                            }
                        });

                        slotTimes.set(box.slotNumber, 0);
                        slotsAvailable.set(box.slotNumber, false);

                        time_tags.get(box.slotNumber).post(new Runnable() {
                            @Override
                            public void run() {
                                time_tags.get(box.slotNumber).setVisibility(View.INVISIBLE);
                            }
                        });

                        slotTimesTV.get(box.slotNumber).post(new Runnable() {
                            @Override
                            public void run() {
                                slotTimesTV.get(box.slotNumber).setVisibility(View.INVISIBLE);
                            }
                        });
                        boxesImages.get(box.slotNumber).post(new Runnable() {
                            @Override
                            public void run() {
                                boxesImages.get(box.slotNumber).setImageResource(R.drawable.box);
                            }
                        });
                    }
                }
        }
    }

    }

    public void initBoxes() {

        slotsAvailable.clear();

        slotsAvailable.add(true);
        slotsAvailable.add(true);
        slotsAvailable.add(true);
        slotsAvailable.add(true);


        boxes.clear();

        boxes.add((ImageView) view.findViewById(R.id.box1));
        boxes.add((ImageView) view.findViewById(R.id.box2));
        boxes.add((ImageView) view.findViewById(R.id.box3));
        boxes.add((ImageView) view.findViewById(R.id.box4));

        boxesImages.clear();

        boxesImages.add(boxes.get(0));
        boxesImages.add(boxes.get(1));
        boxesImages.add(boxes.get(2));
        boxesImages.add(boxes.get(3));

        slotTimes.clear();

        slotTimes.add(0);
        slotTimes.add(0);
        slotTimes.add(0);
        slotTimes.add(0);

        slotTimesTV.clear();

        slotTimesTV.add((TextView) view.findViewById(R.id.textView_box1_time));
        slotTimesTV.add((TextView) view.findViewById(R.id.textView_box2_time));
        slotTimesTV.add((TextView) view.findViewById(R.id.textView_box3_time));
        slotTimesTV.add((TextView) view.findViewById(R.id.textView_box4_time));

        time_tags.clear();

        time_tags.add((ImageView) view.findViewById(R.id.time_tag1));
        time_tags.add((ImageView) view.findViewById(R.id.time_tag_2));
        time_tags.add((ImageView) view.findViewById(R.id.time_tag3));
        time_tags.add((ImageView) view.findViewById(R.id.time_tag4));


        for (int i = 0; i < 4; i++) {
            boxesImages.get(i).setEnabled(false);
            final int finalI = i;
            boxesImages.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBox((ImageView) v, finalI);
                }
            });
        }

    }

    public void openBox(final ImageView box, final int i) {
        box.post(new Runnable() {
            @Override
            public void run() {
                box.setEnabled(false);
                box.setImageResource(R.drawable.square);
            }
        });

        slotTimes.set(i, -1);
        slotsAvailable.set(i, true);

        time_tags.get(i).post(new Runnable() {
            @Override
            public void run() {
                time_tags.get(i).setVisibility(View.INVISIBLE);
            }
        });

        slotTimesTV.get(i).post(new Runnable() {
            @Override
            public void run() {
                slotTimesTV.get(i).setVisibility(View.INVISIBLE);
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("New part unlocked!");
        alertDialog.setMessage("Congrats! You have unlocked a new part.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CHECK IT OUT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        NavHostFragment.findNavController(GarageFragment.this).navigate(R.id.action_garageFragment_to_newPartFragment);
                    }
                });
        alertDialog.show();
        new

                Thread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) getActivity()).db.userDao().deleteUserBox(model.loggedIn, i);
            }
        }).

                start();
    }




    @Override
    public void onResume() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                boolean defaultValue = sharedPref.getBoolean("loggingIn", false);
                if(defaultValue) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("loggingIn", false);
                    String username = sharedPref.getString("username", "");
                    if(getActivity() != null)
                        model.setImageSlots((ImageView)getActivity().findViewById(R.id.image_slot1),
                            (ImageView)getActivity().findViewById(R.id.image_slot2));
                    model.login(username);
                }
                initBoxes();
                loadUserBoxes(model.loggedIn);
                final User u = ((MainActivity) getActivity()).db.userDao().findByName(model.loggedIn);
                final ImageView fightImageView = view.findViewById(R.id.imageView_fight);
                if (u.gamesWon % 3 == 0) {
                    if(u.gamesWon < 7) {
                        if (u.gamesWon != 0 && !u.newBox) {
                            ((MainActivity) getActivity()).db.userDao().updateGeneratedNewBox(u.username, true);
                            final Box box = new Box();
                            box.userId = u.uid;

                            for (int i = 0; i < slotsAvailable.size(); i++) {
                                if (slotsAvailable.get(i)) {
                                    box.slotNumber = i;
                                    break;
                                }
                            }
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.MINUTE, 1);
                            box.timeToOpen = sdf.format(c.getTime());
                            ((MainActivity) getActivity()).db.userDao().insertBox(box);
                        }
                    }
                    if (u.gamesWon == 6) {
//                        Context context = getActivity().getApplicationContext();
//                        CharSequence text = "You unlocked all the parts! Congrats!";
//                        int duration = Toast.LENGTH_SHORT;
//                        Toast toast = Toast.makeText(context, text, duration);
//                        toast.show();
                    }
                    fightImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            fightImageView.setImageResource(R.drawable.fight0);
                        }
                    });
                }
                if (u.gamesWon % 3 == 1) {
                    ((MainActivity) getActivity()).db.userDao().updateGeneratedNewBox(u.username, false);
                    fightImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            fightImageView.setImageResource(R.drawable.fight1);
                        }
                    });
                }
                if (u.gamesWon % 3 == 2) {
                    fightImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            fightImageView.setImageResource(R.drawable.fight2);
                        }
                    });
                }
                loadUserBoxes(model.loggedIn);
                refreshCar();
            }
        }).start();


        super.onResume();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_garage, container, false);
        view.findViewById(R.id.imageMyCar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = NavHostFragment.findNavController(GarageFragment.this);
                navController.navigate(R.id.action_garageFragment_to_modifyCarFragment);
            }
        });


        model = new ViewModelProvider(getActivity()).get(MyViewModel.class);

        lastSave = model.getLastSave().getValue();

        ImageView restart = view.findViewById(R.id.restart);

        if(lastSave == null || !lastSave.hasHistory()) restart.setVisibility(View.INVISIBLE);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.buttonMediaPlayer.start();
                Random random = new Random();
                int song = random.nextInt(3);
                MainActivity mainActivity = (MainActivity) getActivity();
                switch (song) {
                    case 1: {
                        mainActivity.startNewSong(R.raw.fight1);
                        break;
                    }
                    case 2: {
                        mainActivity.startNewSong(R.raw.fight2);
                        break;
                    }
                    case 3: {
                        mainActivity.startNewSong(R.raw.fight3);
                        break;
                    }
                }


                NavController navController = NavHostFragment.findNavController(GarageFragment.this);
                navController.navigate(R.id.action_garageFragment_to_fightFragment);
            }
        });

        refreshCar();

        startFight = view.findViewById(R.id.imageView_fight);

        startFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSave != null) lastSave.reset();
                MainActivity.buttonMediaPlayer.start();
                Random random = new Random();
                int song = random.nextInt(3);
                MainActivity mainActivity = (MainActivity) getActivity();
                switch (song) {
                    case 1: {
                        mainActivity.startNewSong(R.raw.fight1);
                        break;
                    }
                    case 2: {
                        mainActivity.startNewSong(R.raw.fight2);
                        break;
                    }
                    case 3: {
                        mainActivity.startNewSong(R.raw.fight3);
                        break;
                    }
                }


                NavController navController = NavHostFragment.findNavController(GarageFragment.this);
                navController.navigate(R.id.action_garageFragment_to_fightFragment);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        Thread.sleep(10000);
                        if((MainActivity)getActivity() != null) loadUserBoxes(model.loggedIn);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        ImageView settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDialog alert = new SettingsDialog();
                alert.showDialog(getActivity(), model, GarageFragment.this);
            }
        });



        ImageView exit = view.findViewById(R.id.logout);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(GarageFragment.this);
                navController.navigate(R.id.action_garageFragment_to_welcomeFragment);
            }
        });

        ImageView stats = view.findViewById(R.id.stats);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsDialog alert = new StatsDialog();
                alert.showDialog(getActivity(), model);
            }
        });

        avatarExtraPictures.add((ImageView) view.findViewById(R.id.imageView_grayout));
        avatarExtraPictures.add((ImageView) view.findViewById(R.id.imageView_cloud1));
        avatarExtraPictures.add((ImageView) view.findViewById(R.id.imageView_cloud2));
        avatarExtraPictures.add((ImageView) view.findViewById(R.id.imageView_cloud3));
        avatarExtraPictures.add((ImageView) view.findViewById(R.id.imageView_cloud4));
        avatars.add((ImageView) view.findViewById(R.id.cat_my_gif1));
        avatars.add((ImageView) view.findViewById(R.id.cat_my_gif2));
        avatars.add((ImageView) view.findViewById(R.id.cat_my_gif3));
        avatars.add((ImageView) view.findViewById(R.id.cat_my_gif4));
        for(ImageView avatar : avatars) {
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeVisibilityAvatars(View.INVISIBLE);
                    choosingCharacter = false;
                }
            });
        }
        chooseCharacter = view.findViewById(R.id.textView_choose_character);
        ImageView change_avatar = view.findViewById(R.id.imageView_change_avatar);
        change_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingCharacter = true;
                if(choosingCharacter) {
                    changeVisibilityAvatars(View.VISIBLE);
                }
            }
        });

        return view;
    }

    public void changeVisibilityAvatars(final int visibility) {
        for (final ImageView extra : avatarExtraPictures) {
            extra.post(new Runnable() {
                @Override
                public void run() {
                    extra.setVisibility(visibility);
                }
            });
        }
        for (final ImageView avatars : avatars) {
            avatars.post(new Runnable() {
                @Override
                public void run() {
                    avatars.setVisibility(visibility);
                }
            });
        }
        chooseCharacter.post(new Runnable() {
            @Override
            public void run() {
                chooseCharacter.setVisibility(visibility);
            }
        });
    }

    public int convertDate(String date) {

        int minutes = 0;
        Calendar c = Calendar.getInstance();
        String getCurrentDateTime = sdf.format(c.getTime());

        try {
            Date dateToOpen = sdf.parse(date);
            Date currentDate = sdf.parse(getCurrentDateTime);


            minutes = (int) ((dateToOpen.getTime() - currentDate.getTime()) / 60000);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return minutes;
    }

    public void updateBox(int i) {

        final int index = i;
        if (!slotsAvailable.get(i)) {
            slotTimes.set(i, slotTimes.get(i) - 1);
            slotTimesTV.get(i).post(new Runnable() {
                @Override
                public void run() {
                    Integer newTime = slotTimes.get(index);
                    slotTimesTV.get(index).setText(newTime.toString());
                }
            });
            if (slotTimes.get(i) == 0) {
                boxesImages.get(i).post(new Runnable() {
                    @Override
                    public void run() {
                        boxesImages.get(index).setEnabled(false);
                        slotTimes.set(index, -1);
                        slotsAvailable.set(index, false);
                        time_tags.get(index).setVisibility(View.INVISIBLE);
                        slotTimesTV.get(index).setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    public void refreshCar() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = ((MainActivity) getActivity()).db.userDao().findByName(model.loggedIn);
                if (user != null && user.bodyUpgraded != null) {
                    if (model.bodyPart != null) {
                        final ImageView imageView = view.findViewById(R.id.imageMyCar);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(R.drawable.car_with_body);
                            }
                        });
                    } else {
                        final ImageView imageView = view.findViewById(R.id.imageMyCar);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(R.drawable.car1);
                            }
                        });
                    }
                }
            }
        }).start();


        final Car car = model.getMyCar().getValue();
        final ImageView imageView1 = view.findViewById(R.id.image_slot1);
        imageView1.post(new Runnable() {
            @Override
            public void run() {
                if (car.getSlotOne().getPartImage() != null) {
                    imageView1.setImageResource(car.getSlotOne().getPartImageId());
                } else {
                    imageView1.setVisibility(View.GONE);
                }
            }
        });

        final ImageView imageView2 = view.findViewById(R.id.image_slot2);
        imageView2.post(new Runnable() {
            @Override
            public void run() {
                if (car.getSlotTwo().getPartImage() != null) {
                    imageView2.setImageResource(car.getSlotTwo().getPartImageId());
                } else {
                    imageView2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity =(Activity) context;
        }
    }



}
