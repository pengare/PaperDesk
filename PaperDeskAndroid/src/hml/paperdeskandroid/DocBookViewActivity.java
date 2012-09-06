package hml.paperdeskandroid;

import hml.paperdeskandroid.MapMasterActivity.MyReceiver;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class DocBookViewActivity extends Activity {

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
			}
			else if(command.equals("warm"))
			{
				BookService.iCurrentZone = 1; //warm zone
				if(BookService.iBookIndex != -1)
				{
					//start the chapter activity
					Intent intentChapter = new Intent();
					intentChapter.setClass(DocBookViewActivity.this, DocChapterViewActivity.class);
					//BookService.iBookIndex = 1;
					
					startActivity(intentChapter);
					DocBookViewActivity.this.finish();
				}
			}
			else if(command.equals("hot"))
			{
				BookService.iCurrentZone = 0; //hot zone
				if(BookService.iBookIndex != -1 && BookService.iChapterIndex != -1)
				{
					//start the page activity
					Intent intentPage = new Intent();
					intentPage.setClass(DocBookViewActivity.this, DocPageViewActivity.class);
					//BookService.iBookIndex = 1;
					
					startActivity(intentPage);
					DocBookViewActivity.this.finish();
				}
			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_book_view);
        
        registerBroadcastReceiver();
        
        ImageButton btnAndroid = (ImageButton)findViewById(R.id.imageButton1);
        btnAndroid.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setClass(DocBookViewActivity.this, DocChapterViewActivity.class);
				BookService.iBookIndex = 0;
				
				startActivity(intent);
				DocBookViewActivity.this.finish();
				
			}
		});
        
        ImageButton btnPython = (ImageButton)findViewById(R.id.imageButton2);
        btnPython.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setClass(DocBookViewActivity.this, DocChapterViewActivity.class);
				BookService.iBookIndex = 1;
				
				startActivity(intent);
				DocBookViewActivity.this.finish();
			}
		});
        
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
        getMenuInflater().inflate(R.menu.activity_doc_book_view, menu);
        return true;
    }
}
