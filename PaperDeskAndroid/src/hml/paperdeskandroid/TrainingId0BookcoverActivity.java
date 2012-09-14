package hml.paperdeskandroid;

import hml.paperdeskandroid.TrainingService.TrainingStage;
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

public class TrainingId0BookcoverActivity extends Activity {

	//this activity has inner class intends broadcast receiver to recive msg from service that comm with pc
	MyReceiver receiver;
	
	//command
	private String command = "";
	public static boolean bCommandChanged = false;
	
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
			bCommandChanged = true;
			
			if(command.startsWith("zone#0:warm"))
			{
				TrainingService.iCurrentZone = TrainingService.Zone.Warm;
				
				Intent intentChapterList = new Intent();
				intentChapterList.setClass(TrainingId0BookcoverActivity.this, TrainingId0ChapterActivity.class);
				startActivity(intentChapterList);
				
				TrainingId0BookcoverActivity.this.finish();	
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(TrainingId0BookcoverActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				TrainingId0BookcoverActivity.this.finish();
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_training_id0_bookcover);
        
        ImageView imageView = (ImageView)findViewById(R.id.imageViewTrainingBookcover);
        imageView.setImageResource(TrainingService.bookcoverImageList[TrainingService.selectedBook]);
        
        TrainingService.trainingStage = TrainingStage.ViewDocument;
        
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
        getMenuInflater().inflate(R.menu.activity_training_id0_bookcover, menu);
        return true;
    }
}
