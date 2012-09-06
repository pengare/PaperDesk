package hml.paperdeskandroid;

import hml.paperdeskandroid.DocBookViewActivity.MyReceiver;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class DocChapterViewActivity extends ListActivity {

	static String[] ChapterList = new String[]
	{
		"Chapter 1: Introduction",
		"Chapter 2: Design",
		"Chapter 3: API Reference",
		"Chapter 4: Conclusion"
	};
	
	MyReceiver receiver;
	public class MyReceiver extends BroadcastReceiver
	{
		public MyReceiver()
		{
			
		}
		
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			String command = bundle.getString("command");
			if(command.equals("cold"))
			{
				BookService.iCurrentZone = 2; //cold zone
				//start the book activity
				Intent intentBook = new Intent();
				intentBook.setClass(DocChapterViewActivity.this, DocBookViewActivity.class);
				
				startActivity(intentBook);
				DocChapterViewActivity.this.finish();
			}
			else if(command.equals("warm"))
			{
				BookService.iCurrentZone = 1; //warm zone
			}
			else if(command.equals("hot"))
			{
				BookService.iCurrentZone = 0; //hot zone
				if(BookService.iBookIndex != -1 && BookService.iChapterIndex != -1)
				{
					//start the page activity
					Intent intentPage = new Intent();
					intentPage.setClass(DocChapterViewActivity.this, DocPageViewActivity.class);
					//BookService.iBookIndex = 1;
					
					startActivity(intentPage);
					DocChapterViewActivity.this.finish();
				}
			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_doc_chapter_view);
        
        fillChapterList();
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, ChapterList);
        setListAdapter(adapter);
        //setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_doc_chapter_view, AndroidChapters));
        
        registerBroadcastReceiver();
        //ListView listView = getListView();
        //listView.setTextFilterEnabled(true);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
    }

    public void fillChapterList()
    {
    	if(BookService.iBookIndex == 0) //android
    	{
    		ChapterList = BookService.AndroidChapter;
    	}
    	else if(BookService.iBookIndex == 1) //python
    	{
    		ChapterList = BookService.PythonChapter;
    	}
    }
    
    public void registerBroadcastReceiver()
    {
    	receiver = new MyReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(KeySimulationService.receiverAction);
    	this.registerReceiver(receiver, filter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_doc_chapter_view, menu);
        return true;
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		String item = ChapterList[position].toString();
		if(position >= 0)
		{
			Intent intent = new Intent();
			intent.setClass(DocChapterViewActivity.this, DocPageViewActivity.class);
			
			BookService.iChapterIndex = position;
			
			startActivity(intent);
			DocChapterViewActivity.this.finish();
		}
		
		Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
		//super.onListItemClick(l, v, position, id);
	}
    
    
	
	
	
	public class MySimpleArrayAdapter extends ArrayAdapter<String>
	{
		private final Context context;
		private final String[] values;
		
		public MySimpleArrayAdapter(Context context, String[] values)
		{
			super(context, R.layout.doc_chapter_row_layout, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.doc_chapter_row_layout, parent, false);
			TextView textView = (TextView)rowView.findViewById(R.id.chapterLabel);
			ImageView imageView = (ImageView)rowView.findViewById(R.id.chapterIcon);
			
			imageView.setImageResource(R.drawable.chapter1);
			textView.setText(values[position]);
			
			return rowView;
			//return super.getView(position, convertView, parent);
		}
		
		
	}
}


