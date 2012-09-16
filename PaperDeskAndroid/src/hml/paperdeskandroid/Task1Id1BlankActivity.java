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
			if(command.startsWith("tap#1:0:nonempty")) // tap one chapter to read
			{
				//Todo: Analyze the coordinate to get the selected chapter
				//First set it to the first of book;
				
				Task1Service.selectedBookId1 = Task1Service.selectedBook;
				Task1Service.selectedChapterId1 = 0; //todo
				Task1Service.selectedPageId1 = 0;
				
				Intent intentId1Page = new Intent();
				intentId1Page.setClass(Task1Id1BlankActivity.this, Task1Id1PageActivity.class);
				startActivity(intentId1Page);
				
				Task1Id1BlankActivity.this.finish();			
			}
			//start to collocate
			else if(command.startsWith("collocate#0:1")) //1 collocate with 0   collocate#0:1:bookid:pageid
			{
				String tokens[] = command.split("\\#");
				String Ids[] = tokens[1].split("\\:");
				
				Task1Service.selectedBookId1 = Integer.parseInt(Ids[2]);
				Task1Service.selectedPageId1 = Integer.parseInt(Ids[3]);
						
				Intent intentCollocateSlave = new Intent();
				intentCollocateSlave.setClass(Task1Id1BlankActivity.this, Task1Id1CollocateSlaveActivity.class);
				startActivity(intentCollocateSlave);
				
				Task1Id1BlankActivity.this.finish();			
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task1Id1BlankActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
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
        getMenuInflater().inflate(R.menu.activity_task1_id0_blank, menu);
        return true;
    }
}
