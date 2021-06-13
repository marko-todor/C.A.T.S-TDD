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