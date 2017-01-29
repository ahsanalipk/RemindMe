package www.app.remindme.com.remindme;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.List;

public class AddReminderStep1Activity extends AppCompatActivity {
    Button v_btn_add_step1_next;
    PopupWindow v_pwindow_installed_apps;
    LayoutInflater v_linflator_installed_apps;
    RelativeLayout v_relative_layout;
    RelativeLayout v_pwindow_installed_apps_layout;
    ListView v_listview_installed_apps;

    String[] arr_allApps;

    protected void installedApps()
    {
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);

        arr_allApps = new String[packList.size()];
        int count_userApps = 0;
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("Application " + Integer.toString(i), appName);
                arr_allApps[count_userApps++] = appName;
            }
        }

        v_listview_installed_apps = (ListView) findViewById(R.id.listview_allApps);
        v_listview_installed_apps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Arrays.copyOfRange( arr_allApps, 0, count_userApps));

        v_listview_installed_apps.setAdapter(arrayAdapter);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder_step1);

        //popupWindow();
        installedApps();
    }
}
