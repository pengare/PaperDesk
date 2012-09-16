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

public class Task1Id0BookcoverActivity extends Activity {

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
				Task1Service.iCurrentZone = Task1Service.Zone.Warm;
				
				Intent intentChapterList = new Intent();
				intentChapterList.setClass(Task1Id0BookcoverActivity.this, Task1Id0ChapterActivity.class);
				startActivity(intentChapterList);
				
				Task1Id0BookcoverActivity.this.finish();	
			}
			else if(command.startsWith("zone#1:hot"))
			{
				Task1Service.iCurrentZone = Task1Service.Zone.Hot;
				
				Intent intentPage = new Intent();
				intentPage.setClass(Task1Id0BookcoverActivity.this, Task1Id0PageActivity.class);
				startActivity(intentPage);
				
				Task1Id0BookcoverActivity.this.finish();
			}
/*			else if(command.startsWith("tap#1:2"))
			{
				//Todo: check if tap empty area
				//if yes
				Intent intentBlankList = new Intent();
				//intentBlankList.setClass(Task1Id1BookcoverActivity.this, Task1Id1BlankBeforeCollocateActivity.class);
				intentBlankList.setClass(Task1Id0BookcoverActivity.this, Task1Id1BlankActivity.class);
				startActivity(intentBlankList);
				
				Task1Id0BookcoverActivity.this.finish();
			}*/
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task1Id0BookcoverActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task1Id0BookcoverActivity.this.finish();
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_task1_id0_bookcover);
        
        ImageView imageView = (ImageView)findViewById(R.id.imageViewTask1Bookcover);
        imageView.setImageResource(Task1Service.bookcoverImageList[Task1Service.selectedBook]);
        
        
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
        getMenuInflater().inflate(R.menu.activity_task1_id1_bookcover, menu);
        return true;
    }
}
