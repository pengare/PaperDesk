package hml.paperdeskandroid;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class TrainingId0PageActivity extends Activity {

	int[] pages;
	Map chapterToPageMap = new HashMap();
	
	static String command = "";
	
	Handler myHandler;
	
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
			if(command.equals("zone#0:cold"))
			{
				TrainingService.iCurrentZone = TrainingService.Zone.Cold; //cold zone
				
				//start the book activity
				Intent intentBook = new Intent();
				intentBook.setClass(TrainingId0PageActivity.this, TrainingId0BookcoverActivity.class);
				
				startActivity(intentBook);
				TrainingId0PageActivity.this.finish();
			}
			else if(command.equals("zone#0:warm"))
			{
				TrainingService.iCurrentZone = TrainingService.Zone.Warm;
				
				//start the chapter activity
				Intent intentChapterIntent = new Intent();
				intentChapterIntent.setClass(TrainingId0PageActivity.this, TrainingId0ChapterActivity.class);
				
				startActivity(intentChapterIntent);
				TrainingId0PageActivity.this.finish();
			}
			else if(command.equals("zone#0:hot"))
			{
				TrainingService.iCurrentZone = TrainingService.Zone.Hot;
			}
			else if(command.equals("key#0:bendsensortopdown"))
			{
				if(TrainingService.selectedPage < pages.length)
				{
					TrainingId0PageActivity.command = command;
					
					++TrainingService.selectedPage;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
			else if(command.equals("key#0:bendsensortopup"))
			{
				if(TrainingService.selectedPage > 0)
				{
					TrainingId0PageActivity.command = command;
					
					--TrainingService.selectedPage;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
			else if(command.startsWith("tap") && command.endsWith("reset"))
			{
				Intent intentBlankBetweenDocAndPhoto = new Intent();
				intentBlankBetweenDocAndPhoto.setClass(TrainingId0PageActivity.this, TrainingId0BlankBetweenDocAndPhotoActivity.class);
				startActivity(intentBlankBetweenDocAndPhoto);
				
				TrainingId0PageActivity.this.finish();
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(TrainingId0PageActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				TrainingId0PageActivity.this.finish();
			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_training_id0_page);
        
        
        fillPage();
        ImageView pageView = (ImageView)findViewById(R.id.imageViewPage);
        pageView.setImageResource(pages[TrainingService.selectedPage]);
        
        
        registerUIHandler();
        registerBroadcastReceiver();
    }

    public void registerBroadcastReceiver()
    {
    	receiver = new MyReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(KeySimulationService.receiverAction);
    	this.registerReceiver(receiver, filter);
    }
    
    public void fillPage()
    {
    	//TrainingService.selectedPage = 0;
    	
    	//Todo: select books
    	if(TrainingService.selectedBook == 0 )//android
    	{
    		pages = TrainingService.AndroidPage;
    		chapterToPageMap = TrainingService.AndroidChapterMap;
    	}
    }
    
	//This handle the received command
	public void registerUIHandler()
	{
        //Handler to receive information from master dispaly and update
        myHandler = new Handler()
        {
        	@Override
			public void handleMessage(Message msg)
        	{
        		if(msg.what == 0x2000) //bend sensor
        		{
        			if(command.endsWith("bendsensortopdown"))
        			{
        				ImageView imageViewPage = (ImageView)findViewById(R.id.imageViewPage);
        				imageViewPage.setImageResource(pages[TrainingService.selectedPage]);
        			}
        			else if(command.endsWith("bendsensortopup"))
        			{
        				ImageView imageViewPage = (ImageView)findViewById(R.id.imageViewPage);
        				imageViewPage.setImageResource(pages[TrainingService.selectedPage]);
        			}
        			
        		}
        	}
        	
        	
        };
	}
          	
          	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_training_id0_page, menu);
        return true;
    }
}
