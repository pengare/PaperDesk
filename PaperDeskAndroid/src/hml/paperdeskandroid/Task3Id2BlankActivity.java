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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Task3Id2BlankActivity extends Activity {

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

		    if(command.startsWith("tap#2:1") && Task3Service.attachmentStatus == Task3Service.AttachmentStatus.Blank)
			{
		    	Task3Service.attachmentStatus = Task3Service.AttachmentStatus.Attach;
		    	
		    	//set attachment image view visible and set image src
				Message notif = new Message();
				notif.what = 0x2000;
				myHandler.sendMessage(notif);
			}
		    else if(command.startsWith("tap#2:0") && Task3Service.attachmentStatus == Task3Service.AttachmentStatus.Attach)
		    {
		    	Task3Service.attachmentStatus = Task3Service.AttachmentStatus.Move;
		    	
		    	//set attachment image view invisible
				Message notif = new Message();
				notif.what = 0x2000;
				myHandler.sendMessage(notif);
		    }
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task3Id2BlankActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task3Id2BlankActivity.this.finish();
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

        setContentView(R.layout.activity_task3_id2_blank);
        
        if(Task3Service.attachmentStatus == Task3Service.AttachmentStatus.Blank)
        {
        	ImageView attachmentImageView = (ImageView)findViewById(R.id.imageViewTask3Id2BlankThenAttachment);
        	attachmentImageView.setVisibility(View.INVISIBLE);
        }
        
        registerUIHandler();
        registerBroadcastReceiver();
    }

    public void registerBroadcastReceiver()
    {
    	receiver = new MyReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(KeySimulationSlaveService.receiverSlaveAction);
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
        			if(Task3Service.attachmentStatus == Task3Service.AttachmentStatus.Attach)
        			{
        				//set attachment image view visible and set image src
        				ImageView imageViewAttachment = (ImageView)findViewById(R.id.imageViewTask3Id2BlankThenAttachment);
        				imageViewAttachment.setVisibility(View.VISIBLE);
        				imageViewAttachment.setImageResource(Task3Service.attachmentResId);
        			}
        			else if(Task3Service.attachmentStatus == Task3Service.AttachmentStatus.Move)
        			{
        				ImageView imageViewAttachment = (ImageView)findViewById(R.id.imageViewTask3Id2BlankThenAttachment);
        				imageViewAttachment.setVisibility(View.INVISIBLE);
        			}	
        		}
        	}    	
        	
        };
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task3_id2_blank, menu);
        return true;
    }
}
