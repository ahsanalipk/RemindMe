package www.app.remindme.com.remindme;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static android.app.ActivityManager.*;

/*
 * Created by Ahsan Ali on 29/01/2017.
 */

public class MainServices extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast toast = Toast.makeText(MainServices.this, "Service Started Now", Toast.LENGTH_SHORT);
        toast.show();
        //return super.onStartCommand(intent, flags, startId);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (int i = 0; i < tasks.size(); i++) {
            Log.d("Running task", "Running task: " + tasks.get(i).baseActivity.toShortString() + "\t\t ID: " + tasks.get(i).id);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast toast = Toast.makeText(MainServices.this, "Service Stopped Now", Toast.LENGTH_SHORT);
        toast.show();
        super.onDestroy();
    }

    @Nullable
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