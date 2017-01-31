package www.app.remindme.com.remindme;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.provider.Telephony;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class MainServices extends Service{

    private static Timer serviceTimer = new Timer();
    private static boolean serviceTimerRunning = false;
    String prevFGApp;
    ArrayList<String> myAllRules;
    String[][] myDecodedRules;

    private boolean checkPermission()
    {
        String appName = getCurrentApp();
        if (null == appName){
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            String msgtoSend = "Allow Usage Access First, and then Re-enable App!";
            Toast.makeText(getApplicationContext(), msgtoSend, Toast.LENGTH_LONG).show();
            return false;
        }

        myAllRules = new ArrayList<>();
        myAllRules = getMyRules();
        if (myAllRules.isEmpty()){
            String msgtoSend = "No Reminders Defined Yet.";//\n No Service to Start!";
            Toast.makeText(getApplicationContext(), msgtoSend, Toast.LENGTH_LONG).show();
            return false;
        }

        int countRules = myAllRules.size();
        myDecodedRules = new String[countRules][1];
        myDecodedRules = decodeMyRules(myAllRules, countRules);
        return true;
    }

    private void startTimer()
    {
        serviceTimer.scheduleAtFixedRate( new serviceTimerTask(), 0, 4000);
        serviceTimerRunning =true;
    }

    private void stopTimer()
    {
        if (serviceTimerRunning){
            serviceTimer.cancel();
            serviceTimer.purge();
            serviceTimerRunning = false;
        }
    }

    private class serviceTimerTask extends TimerTask
    {
        @Override
        public void run() {
            String appName = getCurrentApp();
            Message msgtoSend = Message.obtain();
            msgtoSend.obj = appName;

            if (!appName.equals(prevFGApp)) {
                // App not changed, so no need for multiple notifications.
                prevFGApp = appName;
                for (int i = 0; i < myDecodedRules.length; i++)
                    if (appName.equals(myDecodedRules[i][1]))
                        pushNotification(myDecodedRules[i], i);

                timerToastHandler.sendMessage(msgtoSend);//.sendEmptyMessage(0);
            }
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


    public ArrayList<String> getMyRules()
    {
        ArrayList<String> arr_loadedRules = new ArrayList<>();
        try {
            File fileRules = new File(getFilesDir(), "MyRules");
            InputStream fis = new BufferedInputStream(new FileInputStream(fileRules));
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));

            String input;
            while ((input = buffer.readLine()) != null)
                arr_loadedRules.add( input);

            buffer.close();
            fis.close();
        }
        catch (Exception e) {
            //e.printStackTrace();
            Toast toast = Toast.makeText(this, "No Reminder Defined Yet!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return arr_loadedRules;
    }


    public String[][] decodeMyRules( ArrayList<String> input, int countR)
    {
        String[][] output;
        String[] input_arr = input.toArray( new String[countR]);

        output = new String[countR][4];
        for(int i=0; i<countR; i++ )
            output[i] = input_arr[i].split(";");

        return output;
    }


    public void pushNotification(String[] ruleFound, int ruleNmbr)
    {
        String appToLaunch;
        if( ruleFound[3].equals( "Call" ))
            appToLaunch = Telephony.Sms.getDefaultSmsPackage(MainServices.this);
        if( ruleFound[3].equals( "Text" ))
            appToLaunch = Telephony.Sms.getDefaultSmsPackage(MainServices.this);
        if( ruleFound[3].equals( "Mail" ))
            appToLaunch = Telephony.Sms.getDefaultSmsPackage(MainServices.this);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        /*
        Intent notificationIntent = new Intent(android.content.Intent.ACTION_VIEW);
        notificationIntent.addCategory(Intent.CATEGORY_DEFAULT);
        notificationIntent.setType("vnd.android-dir/mms-sms");
        */

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        builder.setContentIntent(contentIntent);
        //builder.setSmallIcon(R.drawable.cast_ic_notification_0);
        builder.setContentText("Using "+ ruleFound[0] +
        "? Why not " + ruleFound[2] + " " +ruleFound [3] + " instead.");

        builder.setContentTitle("Reminding you of a " + ruleFound[2]);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setVibrate( new long [0]);

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        nm.notify(ruleNmbr,notification);


/*

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(0, notification);
*/
    }

    public void setServiceConfig(boolean serviceState)
    {
        SharedPreferences spRules = getSharedPreferences("MyConfig", MODE_PRIVATE);
        SharedPreferences.Editor spRulesEdit = spRules.edit();
        spRulesEdit.putBoolean("ServiceEnabled", serviceState);
        spRulesEdit.apply();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (checkPermission()) {
            startTimer();
            setServiceConfig(true);
            Toast.makeText(this, "Service Started...", Toast.LENGTH_SHORT).show();
        }
        else
            setServiceConfig(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service Started...", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
        setServiceConfig(false);
        Toast.makeText(this, "Service Stopped Now", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
