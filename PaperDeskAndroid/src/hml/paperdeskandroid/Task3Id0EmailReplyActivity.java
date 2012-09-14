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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Task3Id0EmailReplyActivity extends Activity {

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

			if(command.equals("key#0:bendsensorleftdown"))
			{
				//Send email replay
				
				Task3Service.bEmailReplySent = true;
				
				//start the list activity(show reply has been sent)
				Intent intentEmailList = new Intent();
				intentEmailList.setClass(Task3Id0EmailReplyActivity.this, Task3Id0EmailListActivity.class);
				
				startActivity(intentEmailList);
				Task3Id0EmailReplyActivity.this.finish();
			}
			else if(command.startsWith("tap#2:1") && Task3Service.attachmentStatus == Task3Service.AttachmentStatus.Blank)
			{
				Task3Service.attachmentStatus = Task3Service.AttachmentStatus.Attach;
			}
			else if(command.startsWith("tap#2:0") && Task3Service.attachmentStatus == Task3Service.AttachmentStatus.Attach)
			{
				Task3Service.attachmentStatus = Task3Service.AttachmentStatus.Move;
				
		    	//set attachment image background
				Message notif = new Message();
				notif.what = 0x2000;
				myHandler.sendMessage(notif);
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task3Id0EmailReplyActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task3Id0EmailReplyActivity.this.finish();
			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
        setContentView(R.layout.activity_task3_id0_email_reply);
        
        registerUIHandler();
        registerBroadcastReceiver();
        
        AutoCompleteTextView emailContent= (AutoCompleteTextView)findViewById(R.id.Task3Id0EmailReplyContent);
        emailContent.requestFocus();
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
				if (msg.what == 0x2000) // change background to the one with attachment
				{

					// set attachment image view visible and set image src
					RelativeLayout bg = (RelativeLayout)findViewById(R.id.Task3Id0EmailReplayBackground);
					bg.setBackgroundResource(R.drawable.email_re2);

				}
			}	
          	
          };
  	}
  	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task3_id0_email_reply, menu);
        return true;
    }
}
