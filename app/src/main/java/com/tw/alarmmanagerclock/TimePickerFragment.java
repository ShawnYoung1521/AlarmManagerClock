package com.tw.alarmmanagerclock;

import java.util.Calendar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
public class TimePickerFragment  extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    OnSelectListener mOnTimeSetListener;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void setTime(int hour,int minute){
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.DAY_OF_YEAR);
            year = calendar.get(Calendar.DAY_OF_MONTH);
            year = calendar.get(Calendar.DATE);
            if(hour == 0) hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(minute == 0) minute = calendar.get(Calendar.MINUTE);
            second =calendar.get(Calendar.SECOND);
            this.hour = hour;
            this.minute = minute;
            this.day = day;
            this.month = month;
            this.year = year;
            this.second = second;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mOnTimeSetListener.getValue( hourOfDay,minute);
        }

        public void setOnSelectListener(OnSelectListener onSelectListener) {
            mOnTimeSetListener = onSelectListener;
        }

        public interface OnSelectListener {
            public void getValue(int hourOfDay, int minute);
        }
}