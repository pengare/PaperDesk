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

public class Task1Id1BlankActivity extends Activity {

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
			
			//start
			if(command.startsWith("tap#1:2")) //0 tap 1 to pick one book
			{
				//Todo: Analyze the coordinate to get the selected book
				//First set it to the first of book;
				Task1Service.selectedBook = 0;
				
				Intent intentBookcover = new Intent();
				intentBookcover.setClass(Task1Id1BlankActivity.this, Task1Id1BookcoverActivity.class);
				startActivity(intentBookcover);
				
				Task1Id1BlankActivity.this.finish();			
			}
			//start to collocate
			else if(command.startsWith("collocate#1:0")) //1 collocate with 0
			{
				Intent intentCollocateSlave = new Intent();
				intentCollocateSlave.setClass(Task1Id1BlankActivity.this, Task1Id1CollocateSlaveActivity.class);
				startActivity(intentCollocateSlave);
				
				Task1Id1BlankActivity.this.finish();			
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_task1_id1_blank);
        
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
        getMenuInflater().inflate(R.menu.activity_task1_id1_blank, menu);
        return true;
    }
}
