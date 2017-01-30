package www.app.remindme.com.remindme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    Button v_btn_configure;
    Button v_btn_addreminders;
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
        startMainService();
        setContentView(R.layout.activity_main);

        v_btn_configure  = (Button) findViewById(R.id.btn_configure);
        v_btn_configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConfigureActivity.class));
                //stopMainService();
            }
        });

        v_btn_addreminders  = (Button) findViewById(R.id.btn_addreminder);
        v_btn_addreminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddRemindersActivity.class));
                //stopMainService();
            }
        });

        v_btn_enableServices  = (ToggleButton) findViewById(R.id.btn_enableServices);
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
}
