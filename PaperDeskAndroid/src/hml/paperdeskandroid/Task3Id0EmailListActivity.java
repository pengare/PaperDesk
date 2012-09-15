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

public class Task3Id0EmailListActivity extends Activity {

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
			if(command.equals("key#0:NewEmail"))
			{
				Task3Service.bNewEmailComing = true;
				Task3Id0EmailListActivity.command = command;
				
				//change the email list picture to show new added email
				
				Message notif = new Message();
				notif.what = 0x2000;
				myHandler.sendMessage(notif);
				
			}
			else if(command.startsWith("zone#0:hot"))
			{
				Task3Service.iCurrentZone = Task3Service.Zone.Hot;
				
/*				if(Task3Service.bNewEmailComing)
				{
					//start new email detail activity
					Intent intentNewEmailDetail = new Intent();
					intentNewEmailDetail.setClass(Task3Id0EmailListActivity.this, Task3Id0NewEmailDetailActivity.class);
					
					startActivity(intentNewEmailDetail);
					Task3Id0EmailListActivity.this.finish();
				}
				else */
				{
					//start email detail activity
					Intent intentEmailDetail = new Intent();
					intentEmailDetail.setClass(Task3Id0EmailListActivity.this, Task3Id0EmailDetailActivity.class);
					
					startActivity(intentEmailDetail);
					Task3Id0EmailListActivity.this.finish();
				}
				
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task3Id0EmailListActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task3Id0EmailListActivity.this.finish();
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_task3_id0_email_list);
        
        if(Task3Service.bEmailReplySent)
        {
			ImageView imageViewPage = (ImageView)findViewById(R.id.imageViewTask3Id0EmailList);
			//Todo: replace it with real new mail list
			imageViewPage.setImageResource(R.drawable.inbox3);
        }
        	
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
    
	public void registerUIHandler()
	{
        //Handler to receive information from master dispaly and update
        myHandler = new Handler()
        {
        	@Override
			public void handleMessage(Message msg)
        	{
        		if(msg.what == 0x2000) //new email comes
        		{
        			if(command.startsWith("key#0:NewEmail"))
        			{
        				ImageView imageViewPage = (ImageView)findViewById(R.id.imageViewTask3Id0EmailList);
        				//Todo: replace it with real new mail list
        				imageViewPage.setImageResource(R.drawable.inbox2);
        			}
        			
        		}
        	}    	
        	
        };
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task3_id0_email_list, menu);
        return true;
    }
}
