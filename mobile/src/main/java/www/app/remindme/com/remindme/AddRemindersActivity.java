package www.app.remindme.com.remindme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


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
