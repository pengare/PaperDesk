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

public class Task3Id1EmailDetailActivity extends Activity {

	int[] emails;
	
	//command
	private String command = "";
	public static boolean bCommandChanged = false;
	
	Handler myHandler;
	
	//this activity has inner class intends broadcast receiver to recive msg from service that comm with pc
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
			command = bundle.getString("command");
			if(command.startsWith("tap#1:0")) //0 tap 1 to pick one book
			{
				//get selected email index
				//Task3Service.selectedEmailId1 = 0;
				
				Message notif = new Message();
				notif.what = 0x2000;
				myHandler.sendMessage(notif);			
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task3Id1EmailDetailActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task3Id1EmailDetailActivity.this.finish();
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
     
        
        setContentView(R.layout.activity_task3_id1_email_detail);
        
        fillEmail();
        ImageView pageView = (ImageView)findViewById(R.id.imageViewTask3Id1EmailDetail);
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

    	emails = Task3Service.emails;
    
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
        		if(msg.what == 0x2000) //tap on the email list
        		{
        				ImageView imageViewPage = (ImageView)findViewById(R.id.imageViewTask3Id1EmailDetail);
        				imageViewPage.setImageResource(emails[Task3Service.selectedEmailId1]);	
        			
        		}
        	}    	
        	
        };
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task3_id1_email_detail, menu);
        return true;
    }
}
