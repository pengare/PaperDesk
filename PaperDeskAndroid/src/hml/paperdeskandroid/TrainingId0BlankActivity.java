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

public class TrainingId0BlankActivity extends Activity {

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
			if(command.startsWith("tap#0:1")) //0 tap 1 to pick one book
			{
				//Todo: Analyze the coordinate to get the selected book
				//First set it to the first of book;
				TrainingService.selectedBook = 0;
				
				Intent intentBookcover = new Intent();
				intentBookcover.setClass(TrainingId0BlankActivity.this, TrainingId0BookcoverActivity.class);
				startActivity(intentBookcover);
				
				TrainingId0BlankActivity.this.finish();			
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_training_id0_blank);
        
        TrainingService.trainingStage = TrainingService.TrainingStage.Blank;
        
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
        getMenuInflater().inflate(R.menu.activity_training_id0_blank, menu);
        return true;
    }
}
