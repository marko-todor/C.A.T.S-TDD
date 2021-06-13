package com.example.cats;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsDialog {


    public void showDialog(Activity activity, final MyViewModel model){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.setting_dialog);

        Switch music = dialog.findViewById(R.id.switch1);
        music.setChecked(model.getMusicOnLiveData().getValue());
        Switch playerMode = dialog.findViewById(R.id.switch2);
        playerMode.setChecked(model.getPlayerModeLiveData().getValue());

        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setMusicOn(isChecked);
            }
        });

        playerMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setPlayerModeOn(isChecked);
            }
        });

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button change_username = dialog.findViewById(R.id.change_usrename);

        change_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsernameChangeDialog alert = new UsernameChangeDialog();
                alert.showDialog(activity, model);
            }
        });

        dialog.show();

    }
}