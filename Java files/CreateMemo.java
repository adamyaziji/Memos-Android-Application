package com.example.adamyaziji.reminderapp;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by adamyaziji on 07/04/2015.
 */
public class CreateMemo extends Fragment {

    DateFormat dtFormat = DateFormat.getDateTimeInstance();
    Calendar datetime = Calendar.getInstance();
    TextView datetimeLbl;
    Button setMemo, timeBtn, dateBtn;
    Context context;
    EditText memoTitle;
    ListView memoList;
    String getDateTime = null;
    String getTaskName = null;
    String getUrgency = null;
    DataHandlerAdapter dbHandler;
    RadioGroup levelOfUrgency;
    RadioButton r1, r2, r3;
    Boolean load = true;

    DatePickerDialog.OnDateSetListener dp = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            datetime.set(Calendar.YEAR, year);
            datetime.set(Calendar.MONTH, month);
            datetime.set(Calendar.DAY_OF_MONTH, day);
        }
    };

    TimePickerDialog.OnTimeSetListener tp = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {

            datetime.set(Calendar.HOUR, hour);
            datetime.set(Calendar.MINUTE, minute);
        }
    };


    private boolean addMemo() {

        getTaskName = memoTitle.getText().toString();
        getDateTime = dtFormat.format(datetime.getTime());

        long id = dbHandler.insertData(getTaskName, getDateTime, getUrgency);
        if (id < 0) {
            Log.d("DataBase", "Data not inserted");

        } else {
            Log.d("DataBase", "Urgency is ");
        }

        Toast.makeText(getActivity().getApplicationContext(), "Memo Added", Toast.LENGTH_SHORT).show();
        load = true;
        return load;
    }

    public void scheduleAlarm(){
        Intent intentAlarm = new Intent(context,AlarmReciever.class);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, datetime.getTimeInMillis(), PendingIntent.getBroadcast(context,1,intentAlarm,PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public void openDB() {
        DataHandlerAdapter.dataHandler.open();
    }

    public void goToListView() {
        Fragment homeScreen = new MemoList();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, homeScreen);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_memo, container, false);
        context = container.getContext();
        dbHandler = new DataHandlerAdapter(context);

        openDB();

        setMemo = (Button) view.findViewById(R.id.setMemo);
        memoTitle = (EditText) view.findViewById(R.id.setMemoTitle);
        memoList = (ListView) view.findViewById(R.id.memoListView);
        levelOfUrgency = (RadioGroup) view.findViewById(R.id.radioGroup);
        r1 = (RadioButton) view.findViewById(R.id.radio_low);
        r2 = (RadioButton) view.findViewById(R.id.radio_medium);
        r3 = (RadioButton) view.findViewById(R.id.radio_urgent);

        levelOfUrgency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (r1.isChecked()) {
                    getUrgency = "Not Important";
                } else if (r2.isChecked()) {
                    getUrgency = "Mildly Important";
                } else {
                    getUrgency = "Urgent!";
                }
            }
        });


        timeBtn = (Button) view.findViewById(R.id.setTime);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(context, tp, datetime.get(Calendar.HOUR_OF_DAY),
                        datetime.get(Calendar.MINUTE), true).show();
            }
        });

        dateBtn = (Button) view.findViewById(R.id.setDate);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, dp, datetime.get(Calendar.YEAR),
                        datetime.get(Calendar.MONTH), datetime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        setMemo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(getUrgency == "Urgent!"){
                    scheduleAlarm();
                    Toast.makeText(getActivity().getApplicationContext(), "Alarm Added", Toast.LENGTH_SHORT).show();
                }
                addMemo();
                goToListView();
            }
        });

        return view;
    }
}