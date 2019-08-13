package com.example.aaaaaaaa;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlarmActivity extends Activity implements View.OnClickListener {

	private TextView switch_in;
	private TextView switch_out;
	private TextView mInTime;
	private TextView mOutTime;
	private RelativeLayout setInTime;
	private RelativeLayout setOutTime;
	private AlarmsSetting alarmsSetting;
	private GridView inGridview;
	private GridView outGridview;
	private WeekGridAdpter inGridAdapter;
	private WeekGridAdpter outGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_alarm_main);
		initView();
	}

	private void initView() {
		alarmsSetting = new AlarmsSetting(this);
		switch_in = (TextView) findViewById(R.id.switch_in);
		switch_in.setOnClickListener(this);
		switch_in.setSelected(alarmsSetting.isInEnble() ? true : false);

		switch_out = (TextView) findViewById(R.id.switch_out);
		switch_out.setOnClickListener(this);
		switch_out.setSelected(alarmsSetting.isOutEnble() ? true : false);

		setInTime = (RelativeLayout) findViewById(R.id.set_in_time);
		setInTime.setOnClickListener(this);
		setOutTime = (RelativeLayout) findViewById(R.id.set_out_time);
		setOutTime.setOnClickListener(this);

		mInTime = (TextView) findViewById(R.id.alarm_in_set_time);
		setTime(alarmsSetting.getInHour(), alarmsSetting.getInMinutes(), AlarmsSetting.ALARM_SETTING_TYPE_IN);
		mOutTime = (TextView) findViewById(R.id.alarm_out_set_time);
		setTime(alarmsSetting.getOutHour(), alarmsSetting.getOutMinutes(), AlarmsSetting.ALARM_SETTING_TYPE_OUT);

		inGridview = (GridView) findViewById(R.id.in_gridview);
		inGridAdapter = new WeekGridAdpter(this, alarmsSetting, AlarmsSetting.ALARM_SETTING_TYPE_IN);
		inGridview.setAdapter(inGridAdapter);
		outGridview = (GridView) findViewById(R.id.out_gridview);
		outGridAdapter = new WeekGridAdpter(this, alarmsSetting, AlarmsSetting.ALARM_SETTING_TYPE_OUT);
		outGridview.setAdapter(outGridAdapter);
	}

	public void setTime(int hour, int minute, int type) {
		String mHour = "" + hour;
		String mMinute = "" + minute;
		if (hour / 10 == 0) mHour = "0" + mHour;
		if (minute / 10 == 0) mMinute = "0" + mMinute;
		if (type == AlarmsSetting.ALARM_SETTING_TYPE_IN) mInTime.setText(mHour + ":" + mMinute);
		else mOutTime.setText(mHour + ":" + mMinute);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_in:
			if (v.isSelected()) {
				alarmsSetting.setInEnble(false);
				v.setSelected(false);
				AlarmOpreation.cancelAlert(AlarmActivity.this, AlarmsSetting.ALARM_SETTING_TYPE_IN);
			} else {
				alarmsSetting.setInEnble(true);
				v.setSelected(true);
				AlarmOpreation.enableAlert(AlarmActivity.this, AlarmsSetting.ALARM_SETTING_TYPE_IN, alarmsSetting);
			}
			break;
		case R.id.switch_out:
			if (v.isSelected()) {
				alarmsSetting.setOutEnble(false);
				v.setSelected(false);
				AlarmOpreation.cancelAlert(AlarmActivity.this, AlarmsSetting.ALARM_SETTING_TYPE_OUT);
			} else {
				alarmsSetting.setOutEnble(true);
				v.setSelected(true);
				AlarmOpreation.enableAlert(AlarmActivity.this, AlarmsSetting.ALARM_SETTING_TYPE_OUT, alarmsSetting);
			}
			break;
		case R.id.set_in_time:
			showTimePickerDialog(AlarmsSetting.ALARM_SETTING_TYPE_IN);
			break;
		case R.id.set_out_time:
			showTimePickerDialog(AlarmsSetting.ALARM_SETTING_TYPE_OUT);
			break;
		}
	}

	public void showTimePickerDialog(final int type) {
		TimePickerFragment timePicker = new TimePickerFragment();
		if (type == AlarmsSetting.ALARM_SETTING_TYPE_IN) {
			timePicker.setTime(alarmsSetting.getInHour(), alarmsSetting.getInMinutes());
		} else {
			timePicker.setTime(alarmsSetting.getOutHour(), alarmsSetting.getOutMinutes());
		}
		timePicker.show(getFragmentManager(), "timePicker");
		timePicker.setOnSelectListener(new TimePickerFragment.OnSelectListener() {
			@Override
			public void getValue(int hourOfDay, int minute) {
				Random r = new Random();
				int ran = r.nextInt(20);
//				if (type == 1) { //上班
//					if (minute>ran) {
//						minute = minute - ran;
//					}else{
//						hourOfDay--;
//						minute = 60-(ran-minute);
//					}
//				}else{ //下班
//					if ((minute + ran) >= 60) {
//						hourOfDay ++;
//						minute = (minute + ran)- 60;
//					}else{
//						minute = minute+ran;
//					}
//				}
				if (type == AlarmsSetting.ALARM_SETTING_TYPE_IN) {
					alarmsSetting.setInHour(hourOfDay);
					alarmsSetting.setInMinutes(minute);
				} else {
					alarmsSetting.setOutHour(hourOfDay);
					alarmsSetting.setOutMinutes(minute);
				}
				setTime(hourOfDay, minute, type);
				AlarmOpreation.cancelAlert(AlarmActivity.this, type);
				AlarmOpreation.enableAlert(AlarmActivity.this, type, alarmsSetting);
			}
		});
	}
}
