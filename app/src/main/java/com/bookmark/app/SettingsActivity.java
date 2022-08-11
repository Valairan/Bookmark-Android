package com.bookmark.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;

public class SettingsActivity extends AppCompatActivity {


    public TimePicker timePicker_DropDown;
    public Button delete_Button;

    int hour = 0;
    int minute = 0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bindAllViews();
        getSharedPrefs();
        timePicker_DropDown.setHour(hour);
        timePicker_DropDown.setMinute(minute);

        timePicker_DropDown.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {

                hour = timePicker.getHour();
                minute = timePicker.getMinute();
                // Storing data into SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("ReadingGoalPrefs",MODE_PRIVATE);

                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit.putInt("hour", hour);
                myEdit.putInt("minute", minute);

                myEdit.commit();

            }
        });
    }

    public void getSharedPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences("ReadingGoalPrefs",MODE_PRIVATE);
        hour = sharedPreferences.getInt("hour", 0);
        minute = sharedPreferences.getInt("minute", 0);

    }

    public void bindAllViews(){
        timePicker_DropDown = findViewById(R.id.timePicker_DropDown);
        delete_Button = findViewById(R.id.delete_Button);
    }


}