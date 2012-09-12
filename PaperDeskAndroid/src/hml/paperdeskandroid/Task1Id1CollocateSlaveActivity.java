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

public class Task1Id1CollocateSlaveActivity extends Activity {

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
			if(command.startsWith("doc#"))  //primary ask slave to show doc#page number
			{
				Task1Id1CollocateSlaveActivity.command = command;
				
				Message notif = new Message();
				notif.what = 0x2000;
				myHandler.sendMessage(notif);	
			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_task1_id1_collocate_slave);
        
        fillPage();
        ImageView pageView = (ImageView)findViewById(R.id.imageViewTask1Page);
        pageView.setImageResource(pages[Task1Service.selectedPage]);
        
        
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
    	//Task1Service.selectedPage = 0;
    	
    	//Todo: select books
    	if(Task1Service.selectedBook == 0 )//android
    	{
    		pages = Task1Service.AndroidPage;
    		chapterToPageMap = Task1Service.AndroidChapterMap;
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
        			if(command.startsWith("doc#"))
        			{
        				//update slave book page
        				ImageView imageViewPage = (ImageView)findViewById(R.id.imageViewTask1PageCollocateSlave);
        				imageViewPage.setImageResource(pages[Task1Service.selectedPage]);
        			}
        			
        		}
        	}
        		
        };
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task1_id1_collocate_slave, menu);
        return true;
    }
}
