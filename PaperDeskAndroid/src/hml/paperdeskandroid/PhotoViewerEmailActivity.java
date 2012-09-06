package hml.paperdeskandroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PhotoViewerEmailActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer_email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo_viewer_email, menu);
        return true;
    }
}
