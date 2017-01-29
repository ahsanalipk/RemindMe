package www.app.remindme.com.remindme;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class ConfigureActivity extends AppCompatActivity {

    String arr_loadedRulesStr;
    ArrayList<String> arr_loadedRules = new ArrayList<String>();
    protected void loadRulesData()
    {
        SharedPreferences spRules = getSharedPreferences("MyRules", MODE_PRIVATE);

        arr_loadedRules.add( spRules.getString("Using", arr_loadedRulesStr));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

    }
}
