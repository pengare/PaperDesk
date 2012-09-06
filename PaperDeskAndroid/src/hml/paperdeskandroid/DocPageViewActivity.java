package hml.paperdeskandroid;

import hml.paperdeskandroid.DocBookViewActivity.MyReceiver;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.webkit.WebView;

public class DocPageViewActivity extends Activity {

	
	public WebView webView;
	public String link;
	
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
				intentBook.setClass(DocPageViewActivity.this, DocBookViewActivity.class);
				
				startActivity(intentBook);
				DocPageViewActivity.this.finish();
			}
			else if(command.equals("warm"))
			{
				BookService.iCurrentZone = 1; //warm zone
				if(BookService.iBookIndex != -1)
				{
					//start the chapter activity
					Intent intentChapter = new Intent();
					intentChapter.setClass(DocPageViewActivity.this, DocChapterViewActivity.class);
					//BookService.iBookIndex = 1;
					
					startActivity(intentChapter);
					DocPageViewActivity.this.finish();
				}
			}
			else if(command.equals("hot"))
			{
				BookService.iCurrentZone = 0; //hot zone
			}

		}
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_page_view);
        
        registerBroadcastReceiver();
        
        webView = (WebView)findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        
        findPageLink();
        webView.loadUrl(link);

    }

    public void registerBroadcastReceiver()
    {
    	receiver = new MyReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(KeySimulationService.receiverAction);
    	this.registerReceiver(receiver, filter);
    }
    
    public void findPageLink()
    {
    	if(BookService.iBookIndex == 0) //android
    	{
    		link = BookService.AndroidLink[BookService.iChapterIndex];
    	}
    	else if(BookService.iBookIndex == 1) //python
    	{
    		link = BookService.PythonLink[BookService.iChapterIndex];
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_doc_page_view, menu);
        return true;
    }
}
