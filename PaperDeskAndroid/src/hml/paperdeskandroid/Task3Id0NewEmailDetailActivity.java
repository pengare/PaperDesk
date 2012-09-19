package hml.paperdeskandroid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Task3Id0NewEmailDetailActivity extends Activity {

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
				//start the chapter activity
				Intent intentEmailList = new Intent();
				intentEmailList.setClass(Task3Id0NewEmailDetailActivity.this, Task3Id0EmailReplyActivity.class);
				
				startActivity(intentEmailList);
				Task3Id0NewEmailDetailActivity.this.finish();
			}
			else if(command.startsWith("zone#0:warm") && Task3Service.bEmailReplySent == true)
			{
				//start the list activity(show reply has been sent)
				Intent intentEmailList = new Intent();
				intentEmailList.setClass(Task3Id0NewEmailDetailActivity.this, Task3Id0EmailListActivity.class);
				
				startActivity(intentEmailList);
				Task3Id0NewEmailDetailActivity.this.finish();
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task3Id0NewEmailDetailActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task3Id0NewEmailDetailActivity.this.finish();
			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_task3_id0_new_email_detail);
        
        registerBroadcastReceiver();
    }

    public void registerBroadcastReceiver()
    {
    	receiver = new MyReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(KeySimulationService.receiverAction);
    	this.registerReceiver(receiver, filter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task3_id0_new_email_detail, menu);
        return true;
    }
}
