package com.example.cats;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginDialog {

    private ListView listView;

    public void playSong(Activity activity) {
        MainActivity.buttonMediaPlayer.start();
        final MainActivity mainActivity = (MainActivity) activity;
        mainActivity.startNewSong(R.raw.garage1);
        MainActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //custom method to play next song
                mainActivity.playNextSong();
                MainActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mainActivity.playNextSong();
                    }
                });
            }
        });

    }

    public static String isValidPassword(final String password) {
        if(password.length() < 8) return "Password must be at least 8 characters long";
        boolean hasUpperCase = !password.equals(password.toLowerCase());
        if (!hasUpperCase) return "Password must have at least one upper case letter";
        boolean hasLowerCase = !password.equals(password.toUpperCase());
        if (!hasLowerCase) return "Password must have at least one lower case letter";
        if (!password.matches(".*\\d.*")) return "Password must have at least one digit";
        if (!password.matches(".*[\\W]")) return "Password must have at least one special character";
        return  "";
    }

    public void showDialog(final Activity activity, final VideoFragment videoFragment){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.login_dialog);
        final MyViewModel model = ((MainActivity)activity).model;

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText nickname = dialog.findViewById(R.id.nickname);

                final EditText password = dialog.findViewById(R.id.password);
                String passwordStr = "";
                if(password.getText() == null || password.getText().toString().equals("")) {
                    passwordStr = "Password123!";
                } else passwordStr = password.getText().toString();
                final String validPasswordMessage = isValidPassword(passwordStr);
                if (!passwordStr.equals("Password123!") && !validPasswordMessage.equals("")) {
                    final TextView error = dialog.findViewById(R.id.textView_error_pass);
                    error.post(new Runnable() {
                        @Override
                        public void run() {
                            error.setText(validPasswordMessage);
                        }
                    });
                    return;
                }

                dialog.dismiss();
                playSong(activity);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        model.register(nickname.getText().toString(), password.getText().toString());
                        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("loggingIn", true);
                        editor.putString("username", (String) nickname.getText().toString());
                        editor.commit();
                        NavController navController = NavHostFragment.findNavController(videoFragment);
                        navController.navigate(R.id.action_videoFragment_to_garageFragment);
                    }
                }).start();
            }
        });

        listView = dialog.findViewById(R.id.list_view_users);



        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, model.getUsersLiveData().getValue());
        listView.setAdapter(adapter);
        model.getUsersLiveData().observe((MainActivity) activity, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                adapter.notifyDataSetChanged();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                dialog.dismiss();
                playSong(activity);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("loggingIn", true);
                        editor.putString("username", (String) parent.getItemAtPosition(position));
                        editor.commit();
                        NavController navController = NavHostFragment.findNavController(videoFragment);
                        navController.navigate(R.id.action_videoFragment_to_garageFragment);
                    }
                }).start();
            }
        });


        dialog.findViewById(R.id.quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finishAffinity();
                System.exit(0);
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
    }

}