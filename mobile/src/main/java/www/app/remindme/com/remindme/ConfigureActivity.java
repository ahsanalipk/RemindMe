package www.app.remindme.com.remindme;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConfigureActivity extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> arr_loadedRules;
    ListView v_listview_allRules;
    Button v_btn_deleteSelRules;
    int count_userRules;
    int count_toDelRules;
    int count_remainRules;
    int i;
    String[] arr_selRules;

    // ******************************************
    // Save the Newly configured Rule
    protected void saveRulesData()
    {

        try {
            File fileRules = new File(getFilesDir(), "MyRules");
            FileOutputStream fos = new FileOutputStream(fileRules);

            String toWrite;

            for ( i=0; i<arr_selRules.length; i++){
                toWrite = "With " + arr_selRules[i] + ", Remind of ";
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

    protected void loadRulesData()
    {
        try {
            File fileRules = new File(getFilesDir(), "MyRules");
            InputStream fis = new BufferedInputStream(new FileInputStream(fileRules));
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));

            arr_loadedRules = new ArrayList<>();
            String input;
            while ((input = buffer.readLine()) != null)
                arr_loadedRules.add( input);

            buffer.close();
            fis.close();
            printRulesData();
        }
        catch (Exception e) {
            //e.printStackTrace();
            Toast toast = Toast.makeText(ConfigureActivity.this, "No Reminder Defined Yet!", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }

    public void printRulesData()
    {
        v_listview_allRules.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        if (!arr_loadedRules.isEmpty())
        {
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, arr_loadedRules);
            v_listview_allRules.setAdapter(arrayAdapter);
        }
    }

    public void deleteSelRules()
    {

        SparseBooleanArray selectedApps = v_listview_allRules.getCheckedItemPositions();
        count_userRules = v_listview_allRules.getCount();
        count_toDelRules = v_listview_allRules.getCheckedItemCount();

        if (0 == count_toDelRules || selectedApps.size() == 0) {
            Toast toast = Toast.makeText(ConfigureActivity.this, "Please Select a Rule first!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            count_remainRules = count_userRules - count_toDelRules;
            arr_selRules = new String[count_remainRules];
            for ( i=0; i<count_userRules; i++)
            {
                if ( !selectedApps.get(i)){
                    arr_selRules[--count_remainRules] =  v_listview_allRules.getItemAtPosition(i).toString();
                }
            }
            Toast toast = Toast.makeText(ConfigureActivity.this, "Rules Refreshed." ,
                    Toast.LENGTH_LONG);
            toast.show();

            saveRulesData();
            loadRulesData();
            arrayAdapter.notifyDataSetChanged();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        v_listview_allRules = (ListView) findViewById(R.id.listview_allRules);
        v_btn_deleteSelRules =  (Button) findViewById(R.id.btn_deleteSelRules);
        v_btn_deleteSelRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelRules();
            }
        });

        loadRulesData();
    }
}
