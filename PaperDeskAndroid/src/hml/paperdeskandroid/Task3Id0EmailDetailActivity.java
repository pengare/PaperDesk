package hml.paperdeskandroid;

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

public class Task3Id0EmailDetailActivity extends Activity {

	int[] emails;
	
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

		    if(command.equals("zone#0:warm"))
			{
				Task3Service.iCurrentZone = Task3Service.Zone.Warm;
				
				//start the chapter activity
				Intent intentEmailList = new Intent();
				intentEmailList.setClass(Task3Id0EmailDetailActivity.this, Task3Id0EmailListActivity.class);
				
				startActivity(intentEmailList);
				Task3Id0EmailDetailActivity.this.finish();
			}
			else if(command.equals("zone#1:hot"))
			{
				Task3Service.iCurrentZone = Task3Service.Zone.Hot;
			}
			else if(command.equals("key#0:bendsensortopdown"))
			{
				if(Task3Service.selectedEmail < emails.length)
				{
					Task3Id0EmailDetailActivity.command = command;
					
					++Task3Service.selectedEmail;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
			else if(command.equals("key#0:bendsensortopup"))
			{
				if(Task3Service.selectedEmail > 0)
				{
					Task3Id0EmailDetailActivity.command = command;
					
					--Task3Service.selectedEmail;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
		}
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_task3_id0_email_detail);
        
        fillEmail();
        ImageView pageView = (ImageView)findViewById(R.id.imageViewTask3Id0EmailDetail);
        pageView.setImageResource(emails[Task3Service.selectedEmail]);
        
        
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
    
    public void fillEmail()
    {
    	if(Task3Service.selectedEmail == 0 )//android
    	{
    		emails = Task3Service.emails;
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
        				ImageView imageViewPage = (ImageView)findViewById(R.id.imageViewTask3Id0EmailDetail);
        				imageViewPage.setImageResource(emails[Task3Service.selectedEmail]);
        			}
        			else if(command.endsWith("bendsensortopup"))
        			{
        				ImageView imageViewPage = (ImageView)findViewById(R.id.imageViewTask3Id0EmailDetail);
        				imageViewPage.setImageResource(emails[Task3Service.selectedEmail]);
        			}
        			
        		}
        	}    	
        	
        };
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task3_id0_email_detail, menu);
        return true;
    }
}
