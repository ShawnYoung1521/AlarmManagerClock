/*
 *  autor：OrandNot
 *  email：orandnot@qq.com
 *  time: 2016 - 1 - 14
 *
 */

package com.example.aaaaaaaa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class AlarmReceiver  extends BroadcastReceiver {
    private AlarmsSetting alarmsSetting;
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmsSetting = new AlarmsSetting(context);
        int type = intent.getIntExtra("type",0);
        Log.e("#######################", "getRecevier_ACtion" + intent.getAction());

        //如果已经设置闹钟w不可用，先拦截
        if(type==AlarmsSetting.ALARM_SETTING_TYPE_IN && !alarmsSetting.isInEnble()){
            return ;
        }else if(type==AlarmsSetting.ALARM_SETTING_TYPE_OUT && !alarmsSetting.isOutEnble()){
            return;
        }

        if(intent.getAction().equals(AlarmsSetting.ALARM_ALERT_ACTION) && type !=0) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日——HH时mm分ss秒SSS毫秒");
            Log.e("###########此次闹钟#######", "alarmsSetting.getNextAlarm()" + formatter.format(new Date(alarmsSetting.getNextAlarm())));
            Log.e("###########当前系统时间###", "System.currentTimeMillis()" + formatter.format(new Date(System.currentTimeMillis())));
            if (alarmsSetting.getNextAlarm() + 1000 * 30 < System.currentTimeMillis()){//解决闹钟广播比设置时间闹钟快的问题
                Log.e("###########无效闹钟#######", "不执行");
                return;
            }
            Log.e("###########准备弹出提示框###", " ");
            intent.setClass(context, AlarmAlertActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            AlarmOpreation.cancelAlert(context, type);
            AlarmOpreation.enableAlert(context, type, new AlarmsSetting(context));
        }else{
            AlarmOpreation.cancelAlert(context,  AlarmsSetting.ALARM_SETTING_TYPE_IN);
            AlarmOpreation.enableAlert(context,  AlarmsSetting.ALARM_SETTING_TYPE_IN, new AlarmsSetting(context));
            AlarmOpreation.cancelAlert(context,  AlarmsSetting.ALARM_SETTING_TYPE_OUT);
            AlarmOpreation.enableAlert(context,  AlarmsSetting.ALARM_SETTING_TYPE_OUT, new AlarmsSetting(context));
        }

    }
}
