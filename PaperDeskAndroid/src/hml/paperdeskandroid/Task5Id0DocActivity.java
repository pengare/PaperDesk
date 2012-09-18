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
import android.widget.ImageView;

public class Task5Id0DocActivity extends Activity {

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
			if(command.startsWith("stack#"))
			{
				Task5Service.bStacked = true;
			}
			else if(command.startsWith("zone#0:warm") && Task5Service.bStacked)
			{
				ImageView img = (ImageView)findViewById(R.id.imageViewTask5Id0DocView);
				img.setImageResource(R.drawable.art_album);
			}
			else if(command.startsWith("zone#0:hot") && Task5Service.bStacked)
			{
				ImageView img = (ImageView)findViewById(R.id.imageViewTask5Id0DocView);
				img.setImageResource(R.drawable.book1_1);
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task5Id0DocActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task5Id0DocActivity.this.finish();
			}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_task5_id0_doc);
        
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
        getMenuInflater().inflate(R.menu.activity_task5_id0_email, menu);
        return true;
    }
}
