package com.example.adamyaziji.reminderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by adamyaziji on 19/04/2015.
 */
public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Set!!", Toast.LENGTH_LONG).show();
    }
}
