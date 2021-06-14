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

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cats.database.User;

import java.util.List;

public class DeleteDialog {

    public void showDialog(final Activity activity, final MyViewModel model, final GarageFragment garageFragment, final Dialog settingsDialog){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.fragment_delete_dialog);



        Button delete_yes = dialog.findViewById(R.id.btn_yes);
        delete_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = ((MainActivity) activity).db.userDao().findByName(model.loggedIn);
                        ((MainActivity) activity).db.userDao().delete(user);
                        model.getUsersLiveData().getValue().remove(model.loggedIn);
                        model.loggedIn = null;
                        NavController navController = NavHostFragment.findNavController(garageFragment);
                        dialog.dismiss();
                        settingsDialog.dismiss();
                        navController.navigate(R.id.action_garageFragment_to_welcomeFragment);

                    }
                }).start();
            }
        });

        Button delete_no = (Button) dialog.findViewById(R.id.btn_no);
        delete_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}