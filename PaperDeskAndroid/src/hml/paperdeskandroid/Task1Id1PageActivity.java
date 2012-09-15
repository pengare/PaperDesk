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

public class Task1Id1PageActivity extends Activity {
	int[] pages;
	Map chapterToPageMap = new HashMap();
	
	public static ImageView imageViewPage;
	
	static String command = "";
	static boolean needRefresh = false;
	
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
			if(command.equals("zone#1:cold"))
			{
				Task1Service.iCurrentZone = Task1Service.Zone.Cold; //cold zone
				
				//start the book activity
				Intent intentBook = new Intent();
				intentBook.setClass(Task1Id1PageActivity.this, Task1Id1BookcoverActivity.class);
				
				startActivity(intentBook);
				Task1Id1PageActivity.this.finish();
			}
			else if(command.equals("zone#1:warm"))
			{
				Task1Service.iCurrentZone = Task1Service.Zone.Warm;
				
				//start the chapter activity
				Intent intentChapterIntent = new Intent();
				intentChapterIntent.setClass(Task1Id1PageActivity.this, Task1Id1ChapterActivity.class);
				
				startActivity(intentChapterIntent);
				Task1Id1PageActivity.this.finish();
			}
			else if(command.equals("zone#1:hot"))
			{
				Task1Service.iCurrentZone = Task1Service.Zone.Hot;
			}
			else if(command.equals("key#1:bendsensortopdown"))
			{
				if(Task1Service.selectedPage < pages.length - 1)
				{
					Task1Id1PageActivity.command = command;
					
					++Task1Service.selectedPage;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
			else if(command.equals("key#1:bendsensortopup"))
			{
				if(Task1Service.selectedPage > 0)
				{
					Task1Id1PageActivity.command = command;
					
					--Task1Service.selectedPage;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
			else if(command.equals("key#1:bendsensorleftup")) //prev chapter
			{
				if(Task1Service.selectedPage > 0 && Task1Service.selectedPage <= 3)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 0;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
				else if(Task1Service.selectedPage > 3 && Task1Service.selectedPage <= 6)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 3;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
				else if(Task1Service.selectedPage > 6 && Task1Service.selectedPage <= 9)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 6;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
				else if(Task1Service.selectedPage > 9 && Task1Service.selectedPage <= 12)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 9;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
				else if(Task1Service.selectedPage > 12 && Task1Service.selectedPage <= 14)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 12;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
			else if(command.equals("key#1:bendsensorleftdown")) //next chapter
			{
				if(Task1Service.selectedPage >= 0 && Task1Service.selectedPage < 3)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 3;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
				else if(Task1Service.selectedPage >= 3 && Task1Service.selectedPage < 6)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 6;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
				else if(Task1Service.selectedPage >= 6 && Task1Service.selectedPage < 9)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 9;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
				else if(Task1Service.selectedPage >= 9 && Task1Service.selectedPage < 12)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 12;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
				else if(Task1Service.selectedPage >= 12 && Task1Service.selectedPage < 14)
				{
					Task1Id1PageActivity.command = command;
					
					Task1Service.selectedPage = 14;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task1Id1PageActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task1Id1PageActivity.this.finish();
			}
//			else if(command.startsWith("tap") && command.endsWith("reset"))
//			{
//				Intent intentBlankBetweenDocAndPhoto = new Intent();
//				intentBlankBetweenDocAndPhoto.setClass(Task1Id1PageActivity.this, TrainingId0BlankBetweenDocAndPhotoActivity.class);
//				startActivity(intentBlankBetweenDocAndPhoto);
//				
//				Task1Id1PageActivity.this.finish();
//			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_task1_id1_page);
        
        imageViewPage = (ImageView)findViewById(R.id.imageViewTask1Page);
        		
        fillPage();
        
        imageViewPage.setImageResource(pages[Task1Service.selectedPage]);
        
        
        registerUIHandler();
        registerBroadcastReceiver();
        new Thread(refreshStuff).start();
    }

    public void registerBroadcastReceiver()
    {
    	receiver = new MyReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(KeySimulationSlaveService.receiverSlaveAction);
    	this.registerReceiver(receiver, filter);
    }
    
    public void fillPage()
    {
    	//Task1Service.selectedPage = 0;
    	
    	//Todo: select books
    	if(Task1Service.selectedBook == 0 )
    	{
    		pages = Task1Service.Book1Page;
    		chapterToPageMap = Task1Service.ChapterMap;
    	}
    	else if(Task1Service.selectedBook == 1 )
    	{
    		pages = Task1Service.Book2Page;
    		chapterToPageMap = Task1Service.ChapterMap;
    	}
    	else if(Task1Service.selectedBook == 2 )
    	{
    		pages = Task1Service.Book3Page;
    		chapterToPageMap = Task1Service.ChapterMap;
    	}
    	else if(Task1Service.selectedBook == 3 )
    	{
    		pages = Task1Service.Book4Page;
    		chapterToPageMap = Task1Service.ChapterMap;
    	}
    	else if(Task1Service.selectedBook == 4 )
    	{
    		pages = Task1Service.Book5Page;
    		chapterToPageMap = Task1Service.ChapterMap;
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
        				
        				Task1Id1PageActivity.imageViewPage.setImageResource(R.drawable.black_blank);
        				
        				needRefresh = true;
        				
        				
        			}
        			else if(command.endsWith("bendsensortopup"))
        			{
        				
        				imageViewPage.setImageResource(R.drawable.black_blank);
        				
        				needRefresh = true;
        			}
        			else if(command.endsWith("bendsensorleftup")) //prev chapter
        			{
        				
        				imageViewPage.setImageResource(R.drawable.black_blank);
        				
        				needRefresh = true;
        			}
        			else if(command.endsWith("bendsensorleftdown")) //next chapter
        			{
        				
        				imageViewPage.setImageResource(R.drawable.black_blank);
        				
        				needRefresh = true;
        			}
        			
        		}
        		else if(msg.what == 0x3000)
        		{
        			
        			imageViewPage.setImageResource(pages[Task1Service.selectedPage]);
        		}
        	}
        	
        	
        };
	}
     
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task1_id1_page, menu);
        return true;
    }
    
    private Runnable refreshStuff = new Thread()
    {
    	public void run()
    	{
    		try {
				
    			while(true)
    			{
    				if(needRefresh)
    				{
    					needRefresh = false;
    					
    					Message notif = new Message();
    					notif.what = 0x3000;
    					myHandler.sendMessage(notif);
    					
    					sleep(15);
    					
    				}
    			}
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    };
}
