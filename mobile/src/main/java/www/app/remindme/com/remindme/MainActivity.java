package www.app.remindme.com.remindme;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    Button v_btn_configure;
    Button v_btn_addreminders;
    Button v_btn_settings;
    ToggleButton v_btn_enableServices;

    public void startMainService()
    {
        Intent intent = new Intent(this, MainServices.class);
        startService(intent);
    }

    public void stopMainService()
    {
        Intent intent = new Intent(this, MainServices.class);
        stopService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v_btn_configure  = (Button) findViewById(R.id.btn_configure);
        v_btn_configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConfigureActivity.class));
            }
        });

        v_btn_addreminders  = (Button) findViewById(R.id.btn_addreminder);
        v_btn_addreminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddReminderStep1Activity.class)); // Skipped AddRemindersActivity Screen. Future Usage
            }
        });

        v_btn_settings  = (Button) findViewById(R.id.btn_settings);
        v_btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Will be available in later releases \n :)", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences spRules = getSharedPreferences("MyConfig", MODE_PRIVATE);

        v_btn_enableServices  = (ToggleButton) findViewById(R.id.btn_enableServices);
        v_btn_enableServices.setChecked(spRules.getBoolean("ServiceEnabled", false));
        if (v_btn_enableServices.isChecked())
            startMainService();

        v_btn_enableServices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    startMainService();
                else
                    stopMainService();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopMainService();
    }
}
