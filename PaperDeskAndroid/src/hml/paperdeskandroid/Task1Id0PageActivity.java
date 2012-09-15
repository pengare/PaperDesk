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

public class Task1Id0PageActivity extends Activity {

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
			if(command.startsWith("tap#0:1"))
			{
				//Todo: Analyze the coordinate to get the selected chapter
				//First set it to the first of book;
				
				Task1Service.selectedBookId0 = Task1Service.selectedBook;
				Task1Service.selectedChapterId0 = 0; //todo
				Task1Service.selectedPageId0 = 0;
				
				
				Message notif = new Message();
				notif.what = 0x2000;
				myHandler.sendMessage(notif);
			}
			else if(command.equals("key#0:bendsensortopdown"))
			{
				if(Task1Service.bCollocate == false)
				{
					if(Task1Service.selectedPageId0 < pages.length)
					{
						Task1Id0PageActivity.command = command;
						
						++Task1Service.selectedPageId0;
						
						Message notif = new Message();
						notif.what = 0x2000;
						myHandler.sendMessage(notif);
					}
				}
				else 
				{
					if(Task1Service.selectedPageId0 < pages.length - 1)
					{
						Task1Id0PageActivity.command = command;
						
						Task1Service.selectedPageId0 += 2;
						
						Message notif = new Message();
						notif.what = 0x2000;
						myHandler.sendMessage(notif);
						
//        				MainActivity.clientCommandChanged[1] = true;
//						MainActivity.clientCommand[1] = "doc#"+(Task1Service.selectedPageId0 - 1) + "\n";
					}

				}
			}
			else if(command.equals("key#0:bendsensortopup"))
			{
				if(Task1Service.bCollocate == false)
				{
					if(Task1Service.selectedPageId0 > 0)
					{
						Task1Id0PageActivity.command = command;
						
						--Task1Service.selectedPageId0;
						
						Message notif = new Message();
						notif.what = 0x2000;
						myHandler.sendMessage(notif);
					}
				}
				else 
				{
					if(Task1Service.selectedPageId0 > 2)
					{
						Task1Id0PageActivity.command = command;
						
						Task1Service.selectedPageId0 -= 2;
						
						Message notif = new Message();
						notif.what = 0x2000;
						myHandler.sendMessage(notif);
						
//        				MainActivity.clientCommandChanged[1] = true;
//						MainActivity.clientCommand[1] = "doc#" + (Task1Service.selectedPageId0 - 1) + "\n";
					}
				}
			}
			else if(command.equals("collocate#1:0"))
			{
				Task1Service.bCollocate = true;
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task1Id0PageActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task1Id0PageActivity.this.finish();
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
        
        setContentView(R.layout.activity_task1_id0_page);
        
        imageViewPage = (ImageView)findViewById(R.id.imageViewTask1PageId0);
        
        fillPage();
        
        imageViewPage.setImageResource(pages[Task1Service.selectedPageId0]);
        
        
        registerUIHandler();
        registerBroadcastReceiver();
        new Thread(refreshStuff).start();
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
    	//Task1Service.selectedPage = 0;
    	
    	//Todo: select books
    	if(Task1Service.selectedBookId0 == 0 )
    	{
    		pages = Task1Service.Book1Page;
    		chapterToPageMap = Task1Service.ChapterMap;
    	}
    	else if(Task1Service.selectedBookId0 == 1 )
    	{
    		pages = Task1Service.Book2Page;
    		chapterToPageMap = Task1Service.ChapterMap;
    	}
    	else if(Task1Service.selectedBookId0 == 2 )
    	{
    		pages = Task1Service.Book3Page;
    		chapterToPageMap = Task1Service.ChapterMap;
    	}
    	else if(Task1Service.selectedBookId0 == 3 )
    	{
    		pages = Task1Service.Book4Page;
    		chapterToPageMap = Task1Service.ChapterMap;
    	}
    	else if(Task1Service.selectedBookId0 == 4 )
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
        				imageViewPage.setImageResource(R.drawable.black_blank);
        				
        				needRefresh = true;
        			}
        			else if(command.endsWith("bendsensortopup"))
        			{
        				imageViewPage.setImageResource(R.drawable.black_blank);
        				
        				needRefresh = true;
        			}
        			
        		}
        		else if(msg.what == 0x3000)
        		{
        			
        			imageViewPage.setImageResource(pages[Task1Service.selectedPageId0]);
        		}
        	}
        	
        	
        };
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task1_id0_page, menu);
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
