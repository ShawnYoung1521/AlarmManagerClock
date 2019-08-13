
/*
 *  autor：OrandNot
 *  email：orandnot@qq.com
 *  time: 2016 - 1 - 13
 *
 */

package com.example.aaaaaaaa;


import android.content.Context;

public class AlarmsSetting {

    public static int ALARM_SETTING_TYPE_IN = 1;

    public static int ALARM_SETTING_TYPE_OUT = 2;

    public static final String ALARM_SETTING_SHAKE = "alarm_setting_shake";
    public static final String ALARM_SETTING_VOICE = "alarm_setting_voice";

    public static final String ALARM_SETTING_IN_ENBLE = "alarm_setting_in_enble";
    public static final String ALARM_SETTING_IN_HOUR = "alarm_setting_in_hour";
    public static final String ALARM_SETTING_IN_MINUTES = "alarm_setting_in_minutes";
    public static final String ALARM_SETTING_IN_DAYSOFWEEK = "alarm_setting_in_daysofweek";

    public static final String ALARM_SETTING_OUT_ENBLE = "alarm_setting_out_enble";
    public static final String ALARM_SETTING_OUT_HOUR = "alarm_setting_out_hour";
    public static final String ALARM_SETTING_OUT_MINUTES = "alarm_setting_out_minutes";
    public static final String ALARM_SETTING_OUT_DAYSOFWEEK = "alarm_settoutg_out_daysofweek";

    public static final String ALARM_SETTING_NEXT_ALARM="alarm_next_alarm";

    public static final String ALARM_ALERT_ACTION = "com.kidcare.alarm_alert";

    private SharedPreferenceUtil spUtil;

    public AlarmsSetting(Context context) {
        spUtil = SharedPreferenceUtil.getInstance(context);
    }
    public boolean isShake() {
        return spUtil.getBoolean(ALARM_SETTING_SHAKE , true);
    }

    public void setShake(boolean isShake) {
        spUtil.putBoolean(ALARM_SETTING_SHAKE, isShake);
    }

    public boolean isVoice() {
        return spUtil.getBoolean(ALARM_SETTING_VOICE , true);
    }

    public void setVoice(boolean isShake) {
        spUtil.putBoolean(ALARM_SETTING_VOICE, isShake);
    }
    //入园
    public boolean isInEnble() {
        return spUtil.getBoolean(ALARM_SETTING_IN_ENBLE , false);
    }

    public void setInEnble(boolean isShake) {
        spUtil.putBoolean(ALARM_SETTING_IN_ENBLE, isShake);
    }

    public int getInHour() {
        return spUtil.getInt(ALARM_SETTING_IN_HOUR);
    }

    public void setInHour(int hour) {
        spUtil.putInt(ALARM_SETTING_IN_HOUR, hour);
    }

    public int getInMinutes() {
        return spUtil.getInt(ALARM_SETTING_IN_MINUTES);
    }

    public void setInMinutes(int minutes) {
        spUtil.putInt(ALARM_SETTING_IN_MINUTES, minutes);
    }

    public int getInDays() {
        return spUtil.getInt(ALARM_SETTING_IN_DAYSOFWEEK);
    }

    public void setInDays(int days) {
        spUtil.putInt(ALARM_SETTING_IN_DAYSOFWEEK, days);
    }

    //离园
    public boolean isOutEnble() {
        return spUtil.getBoolean(ALARM_SETTING_OUT_ENBLE, false);
    }

    public void setOutEnble(boolean isShake) {
        spUtil.putBoolean(ALARM_SETTING_OUT_ENBLE, isShake);
    }

    public int getOutHour() {
        return spUtil.getInt(ALARM_SETTING_OUT_HOUR);
    }

    public void setOutHour(int hour) {
        spUtil.putInt(ALARM_SETTING_OUT_HOUR, hour);
    }

    public int getOutMinutes() {
        return spUtil.getInt(ALARM_SETTING_OUT_MINUTES);
    }

    public void setOutMinutes(int minutes) {
        spUtil.putInt(ALARM_SETTING_OUT_MINUTES, minutes);
    }

    public int getOutDays() {
        return spUtil.getInt(ALARM_SETTING_OUT_DAYSOFWEEK);
    }

    public void setOutDays(int days) {
        spUtil.putInt(ALARM_SETTING_OUT_DAYSOFWEEK, days);
    }


    public long getNextAlarm() {
        return spUtil.getLong(ALARM_SETTING_NEXT_ALARM);
    }

    public void setNextAlarm(long timestamp) {spUtil.putLong(ALARM_SETTING_NEXT_ALARM, timestamp);}
}
