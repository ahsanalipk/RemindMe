package www.app.remindme.com.remindme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddReminderStep2Activity extends AppCompatActivity {

    Bundle extrasInIntent;
    String[] fromStep1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder_step2);

        extrasInIntent = getIntent().getExtras();
        if (extrasInIntent != null){
            fromStep1 = extrasInIntent.getStringArray("AppsSelected");
        }

    }
}
