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

public class Task2Id1BlankActivity extends Activity {

	//command
	private String command = "";
	public static boolean bCommandChanged = false;
	
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
			if(command.startsWith("tap#1:2")) //0 tap 1 to pick one book
			{
				//Id1 will open album2
				//Get which album to tap, now set id0 to album 1, id1 to album 2
				
				Task2Service.iSelectedAlbum = 1;
				
				Intent intentAlbum2 = new Intent();
				intentAlbum2.setClass(Task2Id1BlankActivity.this, Task2Id1Album2Activity.class);
				startActivity(intentAlbum2);
				
				Task2Id1BlankActivity.this.finish();			
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_task2_id1_blank);
        
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
        getMenuInflater().inflate(R.menu.activity_task2_id1_blank, menu);
        return true;
    }
}
