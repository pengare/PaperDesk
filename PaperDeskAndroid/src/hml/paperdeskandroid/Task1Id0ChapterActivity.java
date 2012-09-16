package hml.paperdeskandroid;

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
import android.widget.TextView;

public class Task1Id0ChapterActivity extends ListActivity {

	static String[] ChapterList;
	MyReceiver receiver;
	public class MyReceiver extends BroadcastReceiver
	{
		public MyReceiver()
		{
			
		}
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			String command = bundle.getString("command");
			if(command.startsWith("zone#0:cold"))
			{
				Task1Service.iCurrentZone = Task1Service.Zone.Cold; //cold zone
				
				//start the book activity
				Intent intentBook = new Intent();
				intentBook.setClass(Task1Id0ChapterActivity.this, Task1Id0BookcoverActivity.class);
				
				startActivity(intentBook);
				Task1Id0ChapterActivity.this.finish();
			}
			else if(command.startsWith("zone#0:warm"))
			{
				Task1Service.iCurrentZone = Task1Service.Zone.Warm; //warm zone
			}
			else if(command.startsWith("zone#0:hot"))
			{
				Task1Service.iCurrentZone = Task1Service.Zone.Hot; //hot zone
				//if(BookService.iBookIndex != -1 && BookService.iChapterIndex != -1)
				{
					//start the page activity
					Intent intentPage = new Intent();
					intentPage.setClass(Task1Id0ChapterActivity.this, Task1Id0PageActivity.class);
					
					startActivity(intentPage);
					Task1Id0ChapterActivity.this.finish();
				}
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task1Id0ChapterActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task1Id0ChapterActivity.this.finish();
			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setContentView(R.layout.activity_task1_id1_chapter);
        fillChapterList();
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, ChapterList);
        setListAdapter(adapter);
        
        registerBroadcastReceiver();
    }

    public void fillChapterList()
    {
    	if(Task1Service.selectedBook == 0) 
    	{
    		ChapterList = Task1Service.Book1Chapter;
    	}
    	else if(Task1Service.selectedBook == 1)
    	{
    		ChapterList = Task1Service.Book2Chpater;
    	}
    	else if(Task1Service.selectedBook == 2)
    	{
    		ChapterList = Task1Service.Book3Chpater;
    	}
    	else if(Task1Service.selectedBook == 3)
    	{
    		ChapterList = Task1Service.Book4Chpater;
    	}
    	else if(Task1Service.selectedBook == 4)
    	{
    		ChapterList = Task1Service.Book5Chpater;
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
        getMenuInflater().inflate(R.menu.activity_task1_id1_chapter, menu);
        return true;
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
		}
		
	}
}
