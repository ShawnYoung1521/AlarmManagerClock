package com.example.aaaaaaaa;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/1/13.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    OnSelectListener mOnTimeSetListener;

    private int hour;
    private int minute;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void setTime(int hour,int minute){
            Calendar calendar = Calendar.getInstance();
            if(hour == 0) hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(minute == 0) minute = calendar.get(Calendar.MINUTE);
            this.hour = hour;
            this.minute = minute;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //处理设置的时间，这里我们作为示例，在日志中输出我们选择的时间
//            Log.d("onTimeSet", "hourOfDay: " + hourOfDay + "Minute: " + minute);
            mOnTimeSetListener.getValue( hourOfDay,minute);
        }

        public void setOnSelectListener(OnSelectListener onSelectListener) {
            mOnTimeSetListener = onSelectListener;
        }

        public interface OnSelectListener {
            public void getValue(int hourOfDay, int minute);
        }
}
