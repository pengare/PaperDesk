package hml.paperdeskandroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TempActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_temp, menu);
        return true;
    }
}
