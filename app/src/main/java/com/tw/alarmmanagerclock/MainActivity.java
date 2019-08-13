package com.tw.alarmmanagerclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tw.alarmmanagerclock.view.SelectRemindCyclePopup;
import com.tw.alarmmanagerclock.view.SelectRemindWayPopup;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView date_tv;
    private RelativeLayout repeat_rl, ring_rl;
    private TextView tv_repeat_value, tv_ring_value;
    private LinearLayout allLayout;
    private Button set_btn;
    private String time;
    private int cycle;
    private int ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allLayout = (LinearLayout) findViewById(R.id.all_layout);
        set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(this);
        date_tv = (TextView) findViewById(R.id.date_tv);
        repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        repeat_rl.setOnClickListener(this);
        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);
        tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(0);
            }
        });
    }

    public void showTimePickerDialog(final int type){
        TimePickerFragment  timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(),"timePicker" );
        timePicker.setOnSelectListener(new TimePickerFragment.OnSelectListener() {
            @Override
            public void getValue(int hourOfDay, int minute) {
                DateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                Date date = null;
                try {
                    date = format1.parse(hourOfDay+":"+minute+":30");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                time = getTime(date);
                date_tv.setText(time);
            }
        });
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repeat_rl:
                selectRemindCycle();
                break;
            case R.id.ring_rl:
                selectRingWay();
                break;
            case R.id.set_btn:
                setClock();
                openDing("com.alibaba.android.rimet",getApplicationContext());
                break;
            default:
                break;
        }
    }

        public static void openDing(String packageName, Context context) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo pi = null;
            try {
                pi = packageManager.getPackageInfo("com.alibaba.android.rimet", 0);
            } catch (PackageManager.NameNotFoundException e) {
            }
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pi.packageName);
            List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
            ResolveInfo resolveInfo = apps.iterator().next();
            if (resolveInfo != null ) {
                String className = resolveInfo.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }

    }

    private void setClock() {
        if (time != null && time.length() > 0) {
            String[] times = time.split(":");
            if (cycle == 0) {//是每天的闹钟
                AlarmManagerUtil.setAlarm(this, 0, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), 0, 0, "闹钟响了", ring);
            } if(cycle == -1){//是只响一次的闹钟
                AlarmManagerUtil.setAlarm(this, 1, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), 0, 0, "闹钟响了", ring);
            }else {//多选，周几的闹钟
                String weeksStr = parseRepeat(cycle, 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    AlarmManagerUtil.setAlarm(this, 2, Integer.parseInt(times[0]), Integer
                            .parseInt(times[1]), i, Integer.parseInt(weeks[i]), "闹钟响了", ring);
                }
            }
            Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_LONG).show();
        }

    }


    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        tv_repeat_value.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tv_repeat_value.setText("每天");
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tv_repeat_value.setText("只响一次");
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        tv_ring_value.setText("震动");
                        ring = 0;
                        break;
                    // 铃声
                    case 1:
                        tv_ring_value.setText("铃声");
                        ring = 1;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "周二";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "周三";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "周四";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "周五";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "周六";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "周日";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }

}
