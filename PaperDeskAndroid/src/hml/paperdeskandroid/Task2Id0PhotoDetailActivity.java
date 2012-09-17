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
import android.widget.ImageView;

public class Task2Id0PhotoDetailActivity extends Activity {

	int[] photos;
	static String command = "";
	
	ImageView photoView;
	
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
				Task2Service.iCurrentZone = Task2Service.Zone.Warm;
				
				//start the album activity
				Intent intentAlbum = new Intent();
				intentAlbum.setClass(Task2Id0PhotoDetailActivity.this, Task2Id0Album1Activity.class);
				
				startActivity(intentAlbum);
				Task2Id0PhotoDetailActivity.this.finish();
			}
			else if(command.equals("zone#0:hot"))
			{
				Task2Service.iCurrentZone = Task2Service.Zone.Hot;
			}
			else if(command.equals("key#0:bendsensortopdown"))
			{
				if(Task2Service.selectedPhoto < photos.length - 1)
				{
					Task2Id0PhotoDetailActivity.command = command;
					
					++Task2Service.selectedPhoto;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
			else if(command.equals("key#0:bendsensortopup"))
			{
				if(Task2Service.selectedPhoto > 0)
				{
					Task2Id0PhotoDetailActivity.command = command;
					
					--Task2Service.selectedPhoto;
					
					Message notif = new Message();
					notif.what = 0x2000;
					myHandler.sendMessage(notif);
				}
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task2Id0PhotoDetailActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task2Id0PhotoDetailActivity.this.finish();
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2_id0_photo_detail);
        
        fillPhoto();
        photoView = (ImageView)findViewById(R.id.imageViewTask2Id0PhotoDetail);
        photoView.setImageResource(photos[Task2Service.selectedPhoto]);
        
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
        				
        				photoView.setImageResource(photos[Task2Service.selectedPhoto]);
        			}
        			else if(command.endsWith("bendsensortopup"))
        			{
        				photoView.setImageResource(photos[Task2Service.selectedPhoto]);
        			}
        			
        		}
        	}    		
        };
	}
	
    public void fillPhoto()
    {
    	if(Task2Service.iSelectedAlbum == 0)
    	{
    		photos = Task2Service.album1;
    	}
    	else {
    		photos = Task2Service.album2;
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task2_id0_photo_detail, menu);
        return true;
    }
}
