package com.xt.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AlarmOpreation {

	public static void cancelAlert(Context context, int type) {
		AlarmManager mAlarmManager = (AlarmManager)
				context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(AlarmsSetting.ALARM_ALERT_ACTION);
		intent.putExtra("type", type);
		intent.setClass(context, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, type, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mAlarmManager.cancel(pi);
	}

	public static void enableAlert(Context context, int type, AlarmsSetting alarmsSetting) {
		if(type==AlarmsSetting.ALARM_SETTING_TYPE_IN && !alarmsSetting.isInEnble()){
			return ;
		}else if(type==AlarmsSetting.ALARM_SETTING_TYPE_OUT && !alarmsSetting.isOutEnble()){
			return;
		}
		int rantime = alarmsSetting.getDynamic();
		AlarmManager mAlarmManager = (AlarmManager)
				context.getSystemService(Context.ALARM_SERVICE);
		int hours = 0,minute=0,dayOfweek=0;
		if(type==AlarmsSetting.ALARM_SETTING_TYPE_IN){
			hours = alarmsSetting.getInHour();
			minute=alarmsSetting.getInMinutes();
			dayOfweek = alarmsSetting.getInDays();
		}else if(type==AlarmsSetting.ALARM_SETTING_TYPE_OUT){
			hours = alarmsSetting.getOutHour();
			minute=alarmsSetting.getOutMinutes();
			dayOfweek=alarmsSetting.getOutDays();
		}
		int rans = 0;
		int ran = 0;
		Random r = new Random();
		switch (rantime) {
		case 0:
			rans=0;
			ran =0;
			break;
		case 1:
			rans=5;
			ran = r.nextInt(rans);
			break;
		case 2:
			rans=15;
			ran = r.nextInt(rans);
			break;
		case 3:
			rans=25;
			ran = r.nextInt(rans);
			break;
		case 4:
			rans=35;
			ran = r.nextInt(rans);
			break;
		}
		if (type == 1) { //上班
			if (minute>ran) {
				minute = minute - ran;
			}else{
				hours--;
				minute = 60-(ran-minute);
			}
		}else{ //下班
			if ((minute + ran) >= 60) {
				hours ++;
				minute = (minute + ran)- 60;
			}else{
				minute = minute+ran;
			}
		}
		Log.i("md", "随机范围为："+rans+"  ,生产的随机数为:"+ran+"  ,修改之后的闹钟时间为："+hours+"  "+minute+"  类型为："+type);
		Calendar mCalendar = cacluteNextAlarm(hours, minute, dayOfweek);
		//        Log.e("<<<<<<<<<<<<<<<<<", "alarmsSetting" + alarmsSetting.getInHour() + "-" + alarmsSetting.getInMinutes());
		//        Log.e("<<<<<<<<<<<<<<<<<", " mCalendar" + mCalendar.get(Calendar.DAY_OF_WEEK));
		if (mCalendar.getTimeInMillis() < System.currentTimeMillis()) {
			Log.e("!!!!!!!!!!!","setAlarm FAIL:设置时间不能小于当前系统时间，本次"+mCalendar.getTimeInMillis()+"闹钟无效");
			return;
		}
		Intent intent = new Intent(AlarmsSetting.ALARM_ALERT_ACTION);
		intent.putExtra("type", type);
		intent.setClass(context, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, type, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
		alarmsSetting.setNextAlarm(mCalendar.getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日——HH时mm分ss秒SSS毫秒");
		Log.e("###########设置闹钟时间#######", "alarmsSetting.getNextAlarm()" + formatter.format(new Date(alarmsSetting.getNextAlarm())));
	}

	public static Calendar cacluteNextAlarm(int hour, int minute, int dayOfweek){
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		mCalendar.set(Calendar.HOUR_OF_DAY,hour);
		mCalendar.set(Calendar.MINUTE, minute);
		int differDays = getNextAlarmDifferDays(dayOfweek,mCalendar.get(Calendar.DAY_OF_WEEK), mCalendar.getTimeInMillis());
		int nextYear = getNextAlarmYear(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.DAY_OF_YEAR), mCalendar.getActualMaximum(Calendar.DAY_OF_YEAR), differDays);
		int nextMonth = getNextAlarmMonth(mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.getActualMaximum(Calendar.DATE), differDays);
		int nextDay = getNextAlarmDay(mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.getActualMaximum(Calendar.DATE), differDays);
		mCalendar.set(Calendar.YEAR,nextYear);
		mCalendar.set(Calendar.MONTH, nextMonth % 12);//月份从0开始
		mCalendar.set(Calendar.DAY_OF_MONTH, nextDay);
		mCalendar.set(Calendar.SECOND, 0);
		mCalendar.set(Calendar.MILLISECOND, 0);
		return mCalendar;
	}


	//获取下次闹钟相差的天数
	private static int getNextAlarmDifferDays(int data, int currentDayOfWeek,long timeInMills){
		int nextDayOfWeek =  getNextDayOfWeek(data, currentDayOfWeek,timeInMills);
		return currentDayOfWeek<=nextDayOfWeek?(nextDayOfWeek-currentDayOfWeek):(7 - currentDayOfWeek + nextDayOfWeek);
	}


	//考虑年进位的情况
	private static int getNextAlarmYear(int year,int dayOfYears, int actualMaximum, int differDays) {
		int temp = actualMaximum-dayOfYears-differDays;
		return temp >= 0?year:year+1;
	}

	//考虑月进位的情况
	private static int getNextAlarmMonth(int month,int dayOfMonth,int actualMaximum, int differDays) {
		int temp = actualMaximum-dayOfMonth-differDays;
		return temp >= 0?month:month+1;
	}

	//获取下次闹钟的day
	private static int getNextAlarmDay(int thisDayOfMonth, int actualMaximum, int differDays) {
		int temp = actualMaximum - thisDayOfMonth-differDays;
		if (temp<0){
			return thisDayOfMonth + differDays - actualMaximum;
		}
		return thisDayOfMonth + differDays;
	}

	//获取下次显示是星期几
	private static int getNextDayOfWeek(int data, int cWeek,long timeInMillis) {
		int tempBack = data >> cWeek - 1;
		int tempFront = data ;

		if(tempBack%2==1){
			if(System.currentTimeMillis()<timeInMillis)  return cWeek;
		}
		tempBack = tempBack>>1;
			int m=1,n=0;
			while (tempBack != 0) {
				if (tempBack % 2 == 1 ) return cWeek + m;
				tempBack = tempBack / 2;
				m++;
			}
			while(n<cWeek){
				if (tempFront % 2 == 1)  return n+1;
				tempFront =tempFront/2;
				n++;
			}
			return 0;
	}
}
