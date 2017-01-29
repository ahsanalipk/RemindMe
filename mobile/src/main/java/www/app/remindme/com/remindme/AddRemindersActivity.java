package www.app.remindme.com.remindme;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.List;


public class AddRemindersActivity extends AppCompatActivity {
    Button v_btn_add_specific;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminders);

        v_btn_add_specific = (Button) findViewById(R.id.btn_Add_Specific);
        v_btn_add_specific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddRemindersActivity.this, AddReminderStep1Activity.class));
            }
        });
    }

}
