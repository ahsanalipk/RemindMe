package www.app.remindme.com.remindme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    Button v_btn_configure;
    Button v_btn_addreminders;

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
                startActivity(new Intent(MainActivity.this, AddRemindersActivity.class));
            }
        });
    }

}
