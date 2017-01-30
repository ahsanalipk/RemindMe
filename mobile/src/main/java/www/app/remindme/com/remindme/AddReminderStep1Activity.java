package www.app.remindme.com.remindme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddReminderStep1Activity extends AppCompatActivity {
    ListView v_listview_installed_apps;
    String[] arr_allApps;
    String[] arr_allAppsOrdered;
    String[] arr_selApps;
    Button v_btn_add_next;
    int count_userApps = 0;
    int count_selApps = 0;
    int i;

    // ******************************************
    // Get List of Installed Apps on the System
    protected void installedApps()
    {
        List<PackageInfo> packageList = getPackageManager().getInstalledPackages(0);

        arr_allApps = new String[packageList.size()];
        for (i=0; i < packageList.size(); i++)
        {
            PackageInfo packageInfo = packageList.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                arr_allApps[count_userApps++] = appName;
            }
        }
        arr_allAppsOrdered = Arrays.copyOfRange( arr_allApps, 0, count_userApps);
        Arrays.sort(arr_allAppsOrdered, String.CASE_INSENSITIVE_ORDER);

        v_listview_installed_apps = (ListView) findViewById(R.id.listview_allApps);
        v_listview_installed_apps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, arr_allAppsOrdered);

        v_listview_installed_apps.setAdapter(arrayAdapter);
    }

    // ******************************************
    // Get List of Selected Apps by tht User
    protected void selectedApps()
    {
        v_btn_add_next = (Button) findViewById(R.id.btn_add_next);
        v_btn_add_next.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                SparseBooleanArray selectedApps = v_listview_installed_apps.getCheckedItemPositions();
                count_selApps = v_listview_installed_apps.getCheckedItemCount();

                if (0 == count_selApps) {
                    Toast toast = Toast.makeText(AddReminderStep1Activity.this, "Please Select an App first!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    arr_selApps = new String[count_selApps];
                    for (i=0; i<count_userApps; i++)
                    {
                        if ( selectedApps.get(i)){
                            arr_selApps[--count_selApps] =  v_listview_installed_apps.getItemAtPosition(i).toString();
                        }
                    }
                    Toast toast = Toast.makeText(AddReminderStep1Activity.this, "Apps Selected." ,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    saveRulesData();
                }
                //v_listview_installed_apps.getSelectedItem();
            }
        });
    }

    // ******************************************
    // Save the Newly configured Rule
    protected void saveRulesData()
    {
        try {
            File fileRules = new File(getFilesDir(), "MyRules");
            FileOutputStream fos = new FileOutputStream(fileRules, true);
            String toWrite;

            for ( i=0; i<arr_selApps.length; i++){
                toWrite = "With " + arr_selApps[i] + ", Remind of ";
                fos.write(toWrite.getBytes() );
                fos.write('\n');
                fos.flush();
            }
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder_step1);

        installedApps();
        selectedApps();

    }
}
