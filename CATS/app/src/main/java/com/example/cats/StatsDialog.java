package com.example.cats;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cats.database.User;

public class StatsDialog {

    public void showDialog(final Activity activity, final MyViewModel model){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.stats_dialog);


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final TextView wins = dialog.findViewById(R.id.wins);
        final TextView losses = dialog.findViewById(R.id.losses);
        final TextView userStat = dialog.findViewById(R.id.user);

        new Thread(new Runnable() {
            @Override
            public void run() {

                final User user = ((MainActivity)activity).db.userDao().findByName(model.loggedIn);
                wins.post(new Runnable() {
                    @Override
                    public void run() {
                        wins.setText(Integer.toString(user.gamesWon));
                    }
                });
                losses.post(new Runnable() {
                    @Override
                    public void run() {
                        losses.setText(Integer.toString(user.gamesLost));
                    }
                });
                userStat.post(new Runnable() {
                    @Override
                    public void run() {
                        userStat.setText(model.loggedIn.toUpperCase() + "'S STATS");
                    }
                });
            }
        }).start();


        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
        dialog.show();

    }
}
