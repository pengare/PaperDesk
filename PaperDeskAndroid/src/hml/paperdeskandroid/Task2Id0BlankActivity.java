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

public class Task2Id0BlankActivity extends Activity {

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
			if(command.startsWith("tap#0:2")) //0 tap 1 to pick one book
			{
				//Id0 will open album1
				//Get which album to tap, now set id0 to album 1, id1 to album 2
				
				Task2Service.iSelectedAlbum = 0;
				
				Intent intentAlbum1 = new Intent();
				intentAlbum1.setClass(Task2Id0BlankActivity.this, Task2Id0Album1Activity.class);
				startActivity(intentAlbum1);
				
				Task2Id0BlankActivity.this.finish();			
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
        setContentView(R.layout.activity_task2_id0_blank);
        
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
        getMenuInflater().inflate(R.menu.activity_task2_id0_blank, menu);
        return true;
    }
}
