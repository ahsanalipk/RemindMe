package www.app.remindme.com.remindme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

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
        Toast toast = Toast.makeText(MainServices.this, "Service Started Now", Toast.LENGTH_LONG);
        toast.show();
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast toast = Toast.makeText(MainServices.this, "Service Destroyed Now", Toast.LENGTH_LONG);
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