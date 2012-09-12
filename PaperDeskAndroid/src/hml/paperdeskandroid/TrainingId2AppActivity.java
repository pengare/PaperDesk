package hml.paperdeskandroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TrainingId2AppActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_id2_app);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_training_id2_app, menu);
        return true;
    }
}
