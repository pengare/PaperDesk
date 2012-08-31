package hml.paperdeskandroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class DocBookViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_book_view);
        
        ImageButton btnAndroid = (ImageButton)findViewById(R.id.imageButton1);
        btnAndroid.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setClass(DocBookViewActivity.this, DocChapterViewActivity.class);
				startActivity(intent);
				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_doc_book_view, menu);
        return true;
    }
}
