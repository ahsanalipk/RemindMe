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
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class MainServices extends Service{

    private static Timer serviceTimer = new Timer();
    private Context serviceContext;
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
            timerToastHandler.sendMessage( msgtoSend);//.sendEmptyMessage(0);
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

        }
        else {
            //startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();

            currentApp = runningApps.get(0).processName;
        }
        return currentApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceContext = this;
        startTimer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //String appName = getCurrentApp();

        Toast.makeText(this, "Service Started...", Toast.LENGTH_SHORT).show();
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





/**
 * Created by Ahsan Ali on 29/01/2017.


 class CheckTopActivity extends Thread{
 ActivityManager am = null;
 Context context = null;

 public CheckTopActivity(Context con){
 context = con;
 am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
 }

 public void run(){
 Looper.prepare();

 while(true){
 // Return a list of the tasks that are currently running,
 // with the most recent being first and older ones after in order.
 // Taken 1 inside getRunningTasks method means want to take only
 // top activity from stack and forgot the olders.
 List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);

 String currentTopActivityName = taskInfo.get(0).topActivity.getClassName();

 if (currentTopActivityName.equals("PACKAGE_NAME.ACTIVITY_NAME")) {
 // show your activity here on top of PACKAGE_NAME.ACTIVITY_NAME
 Log.e(currentTopActivityName);
 }
 }
 Looper.loop();
 }
 }
 */