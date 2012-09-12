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

public class TrainingId0ChapterActivity extends ListActivity {

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
				TrainingService.iCurrentZone = TrainingService.Zone.Cold; //cold zone
				
				//start the book activity
				Intent intentBook = new Intent();
				intentBook.setClass(TrainingId0ChapterActivity.this, TrainingId0BookcoverActivity.class);
				
				startActivity(intentBook);
				TrainingId0ChapterActivity.this.finish();
			}
			else if(command.startsWith("zone#0:warm"))
			{
				TrainingService.iCurrentZone = TrainingService.Zone.Warm; //warm zone
			}
			else if(command.startsWith("zone#0:hot"))
			{
				TrainingService.iCurrentZone = TrainingService.Zone.Hot; //hot zone
				//if(BookService.iBookIndex != -1 && BookService.iChapterIndex != -1)
				{
					//start the page activity
					Intent intentPage = new Intent();
					intentPage.setClass(TrainingId0ChapterActivity.this, TrainingId0PageActivity.class);
					
					startActivity(intentPage);
					TrainingId0ChapterActivity.this.finish();
				}
			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_training_id0_chapter);
        
        fillChapterList();
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, ChapterList);
        setListAdapter(adapter);
        
        registerBroadcastReceiver();
    }

    public void fillChapterList()
    {
    	if(TrainingService.selectedBook == 0) //android
    	{
    		ChapterList = TrainingService.AndroidChapter;
    	}
    	else if(TrainingService.selectedBook == 1) //python
    	{
    		ChapterList = TrainingService.PythonChapter;
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
        getMenuInflater().inflate(R.menu.activity_training_id0_chapter, menu);
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
