package hml.paperdeskandroid;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DocChapterViewActivity extends ListActivity {

	static final String[] AndroidChapters = new String[]
	{
		"Chapter 1: Introduction",
		"Chapter 2: Design",
		"Chapter 3: API Reference",
		"Chapter 4: Conclusion"
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_doc_chapter_view);
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_doc_chapter_view, AndroidChapters));
        
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_doc_chapter_view, menu);
        return true;
    }
}
