package hml.paperdeskandroid;

import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Task4Id1BlankActivity extends Activity {

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

		    if(command.startsWith("location"))
			{
		    	String tokens[] = command.split("\\#");
		    	String location = tokens[1];
		    	String latlonglevel[] = location.split("\\:");
		    	Task4Service.slaveMapCenterLat = Integer.parseInt(latlonglevel[0]);
		    	Task4Service.slaveMapCenterLong = Integer.parseInt(latlonglevel[1]);
		    	Task4Service.slaveMapZoomLevel = Integer.parseInt(latlonglevel[2]);
		    	
				Intent intentEmailList = new Intent();
				intentEmailList.setClass(Task4Id1BlankActivity.this, Task4Id1SlaveMapActivity.class);
				startActivity(intentEmailList);
				
				Task4Id1BlankActivity.this.finish();
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_task4_id1_blank);
        
        registerBroadcastReceiver();
    }

    public void registerBroadcastReceiver()
    {
    	receiver = new MyReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(KeySimulationSlaveService.receiverSlaveAction);
    	this.registerReceiver(receiver, filter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task4_id1_blank, menu);
        return true;
    }
}
