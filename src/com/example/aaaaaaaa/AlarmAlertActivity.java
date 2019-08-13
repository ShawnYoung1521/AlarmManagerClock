

package com.example.aaaaaaaa;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class AlarmAlertActivity extends Activity {
    private AlarmsSetting alarmsSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmsSetting = new AlarmsSetting(getApplicationContext());
        Log.e(">>>>>>>>", "oncreate");
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        wakeUpAndUnlock();
        int type = getIntent().getIntExtra("type", 0);
        if (type == AlarmsSetting.ALARM_SETTING_TYPE_IN) {
            finish();
            openDing("com.alibaba.android.rimet",getApplicationContext());
        } else if (type == AlarmsSetting.ALARM_SETTING_TYPE_OUT) {
            finish();
            openDing("com.alibaba.android.rimet",getApplicationContext());
        } else {
            finish();
        }
        notificationVibrator();
        notificationRing();
        AlarmOpreation.cancelAlert(getApplicationContext(),  AlarmsSetting.ALARM_SETTING_TYPE_IN);
        AlarmOpreation.enableAlert(getApplicationContext(),  AlarmsSetting.ALARM_SETTING_TYPE_IN, new AlarmsSetting(getApplicationContext()));
        AlarmOpreation.cancelAlert(getApplicationContext(),  AlarmsSetting.ALARM_SETTING_TYPE_OUT);
        AlarmOpreation.enableAlert(getApplicationContext(),  AlarmsSetting.ALARM_SETTING_TYPE_OUT, new AlarmsSetting(getApplicationContext()));
        mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent= new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
				Log.i("md", "Home键触发");
			}
		}, 15000);
    }
    
    
    private Handler mHandler = new Handler();
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
    public void wakeUpAndUnlock() {
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    private MediaPlayer mediaPlayer = new MediaPlayer();
    public Vibrator vibrator;

    /**
     * 振动通知
     */
    private void notificationVibrator() {

        if (vibrator == null) {
            vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        }
        vibrator.vibrate(new long[]{500, 50, 50, 1000, 50}, -1);
    }

    /**
     * 铃声通知
     */
    private void notificationRing() {

        if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
        if (mediaPlayer.isPlaying()) return;

        try {
            // 这里是调用系统自带的铃声
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepare();
        } catch (Exception e) {

        }
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

//	@Override
//	public void onAttachedToWindow() {
//		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//		super.onAttachedToWindow();
//	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            // Volume keys and camera keys dismiss the alarm
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_FOCUS:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
//
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		// Do this on key down to handle a few of the system keys.
//		switch (event.getKeyCode()) {
//			// Volume keys and camera keys dismiss the alarm
//			case KeyEvent.KEYCODE_VOLUME_UP:
//			case KeyEvent.KEYCODE_BACK:
//			case KeyEvent.KEYCODE_HOME:
//			case KeyEvent.KEYCODE_MENU:
//			case KeyEvent.KEYCODE_VOLUME_DOWN:
//			case KeyEvent.KEYCODE_CAMERA:
//			case KeyEvent.KEYCODE_FOCUS:
//				return true;
//			default:
//				break;
//		}
//		return super.dispatchKeyEvent(event);
//	}
}
