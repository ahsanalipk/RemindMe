package www.app.remindme.com.remindme;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class MainServices extends Service{

    private static Timer serviceTimer = new Timer();

    private boolean checkPermission()
    {
        String appName = getCurrentApp();
        if (null == appName){
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            String msgtoSend = "Allow Usage Access First, and then Restart App!";
            Toast.makeText(getApplicationContext(), msgtoSend, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void startTimer()
    {
        serviceTimer.scheduleAtFixedRate( new serviceTimerTask(), 0, 4000);
    }

    private class serviceTimerTask extends TimerTask
    {
        @Override
        public void run() {
            String appName = getCurrentApp();
            Message msgtoSend = Message.obtain();
            msgtoSend.obj = appName;

            timerToastHandler.sendMessage(msgtoSend);//.sendEmptyMessage(0);
        }
    }

    private final Handler timerToastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "Hello " + msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    public String getCurrentApp()
    {
        String currentApp = null;

        if (Build.VERSION.SDK_INT >= 21) {
            UsageStatsManager usManage = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long currTime = System.currentTimeMillis();
            List<UsageStats> runningApps = usManage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currTime - 1000 * 1000, currTime);

            if (runningApps != null && runningApps.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();

                for (UsageStats usageStats : runningApps) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (!mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        }
        else {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();
            currentApp = runningApps.get(0).processName;
        }
        return currentApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (checkPermission()) {
            startTimer();
            Toast.makeText(this, "Service Started...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service Started...", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Stopped Now", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
