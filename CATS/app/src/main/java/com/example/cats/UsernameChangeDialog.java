package com.example.cats;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.cats.database.User;

import java.util.List;

public class UsernameChangeDialog {


    public void showDialog(final Activity activity, final MyViewModel model){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.fragment_username_change_dialog);

        final TextView old_username = dialog.findViewById(R.id.textView_old_username);
        old_username.post(new Runnable() {
            @Override
            public void run() {
                old_username.setText(model.loggedIn);
            }
        });

        Button save_changes = dialog.findViewById(R.id.button_save_new_username);

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView new_username = dialog.findViewById(R.id.new_username_edit);
                final String newUsername = new_username.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        User user = ((MainActivity) activity).db.userDao().findByName(newUsername);
                        if (user == null) {
                            ((MainActivity) activity).db.userDao().updateUsername(model.loggedIn, newUsername);
                            List<String> users = model.getUsersLiveData().getValue();
                            for (int i = 0; i < users.size(); i++) {
                                if (users.get(i).equals(model.loggedIn)) {
                                    users.set(i, newUsername);
                                }
                            }
                            model.getUsersLiveData().postValue(users);
                            model.loggedIn = newUsername;
                            dialog.dismiss();
                        } else {
                            final TextView error = dialog.findViewById(R.id.textView_username_error);
                            error.post(new Runnable() {
                                @Override
                                public void run() {
                                    error.setText("Username " + newUsername + " is already taken");
                                }
                            });

                        }
                    }
                }).start();

            }
        });

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}