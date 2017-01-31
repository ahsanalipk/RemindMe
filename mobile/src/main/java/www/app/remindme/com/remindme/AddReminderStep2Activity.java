package www.app.remindme.com.remindme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddReminderStep2Activity extends AppCompatActivity {

    Context cxt = AddReminderStep2Activity.this;
    ListView v_listview_allTargets;
    Button v_btn_add_next_s2;
    Bundle extrasInIntent;
    String[] arr_fromStep1;
    String[] arr_forStep3;
    String[] arr_allTargets = {"Call", "Text", "Mail", "Note to"};

    int i, count_selApps;
    String textEntered;

    public void fillTargets()
    {
        v_listview_allTargets = (ListView) findViewById(R.id.listview_allTargets);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, arr_allTargets);

        v_listview_allTargets.setAdapter(arrayAdapter);
    }

    // Selected Apps
    public void selectedTarget()
    {
        v_btn_add_next_s2 = (Button) findViewById(R.id.btn_add_next_s2);
        v_btn_add_next_s2.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int selectedToDo = v_listview_allTargets.getCheckedItemPosition();

                if (0 == v_listview_allTargets.getCheckedItemCount()) {
                    Toast.makeText(cxt, "Please Select a ToDo first!", Toast.LENGTH_SHORT).show();
                }
                else{
                    arr_forStep3 = new String[count_selApps];
                    for (i=0; i<count_selApps; i++)
                    {
                        arr_forStep3[i] = arr_fromStep1[i] + ";" + arr_allTargets[selectedToDo];
                    }
                    //Toast.makeText(cxt, "Apps Selected." , Toast.LENGTH_SHORT).show();

                    showPopup();
                    //Intent intent = new Intent(getApplicationContext(), AddReminderStep2Activity.class);
                    //intent.putExtra("AppsSelected", arr_selApps);
                    //startActivity(intent);
                    //saveRulesData();
                }
            }
        });
    }

    protected void showPopup()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddReminderStep2Activity.this);
        final EditText et = new EditText(AddReminderStep2Activity.this);

        alertDialogBuilder.setView(et);

        // set dialog message
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        textEntered = et.getText().toString();

                        for (i=0; i<count_selApps; i++)
                        {
                            arr_forStep3[i] = arr_forStep3[i] + ";" + textEntered;
                        }
                        Toast.makeText(cxt, "New Reminder Defined!" , Toast.LENGTH_LONG).show();
                        saveRulesData();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.create().show();
    }

    // ******************************************
    // Save the Newly configured Rule
    protected void saveRulesData()
    {
        try {
            File fileRules = new File(getFilesDir(), "MyRules");
            FileOutputStream fos = new FileOutputStream(fileRules, true);
            String toWrite;

            for ( i=0; i<arr_forStep3.length; i++){
                toWrite = arr_forStep3[i];
                fos.write(toWrite.getBytes() );
                fos.write('\n');
                fos.flush();
            }
            fos.close();
            finish();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder_step2);

        extrasInIntent = getIntent().getExtras();
        if (extrasInIntent != null){
            arr_fromStep1 = extrasInIntent.getStringArray("AppsSelected");
            count_selApps = arr_fromStep1.length;

        }
        fillTargets();
        selectedTarget();

    }
}
