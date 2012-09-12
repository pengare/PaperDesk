package hml.paperdeskandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class Task2Id1Album2Activity extends Activity {

	int[] photoIds = new int[]
	{
			//Todo: add photo in album2;
	};
	
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
			if(command.startsWith("touch#"))
			{
				//move specific photo
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
        setContentView(R.layout.activity_task2_id1_album2);
        
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i=0; i < photoIds.length; ++i)
        {
        	Map<String, Object> listItem = new HashMap<String, Object>();
        	listItem.put("photo", photoIds[i]);
        	listItems.add(listItem);
        }
        
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
        		listItems,
        		R.layout.photo_grid_cell,
        		new String[]{"photo"},
        		new int[]{R.id.image1});
        GridView grid = (GridView)findViewById(R.id.photoGridTask2Id1);
        grid.setAdapter(simpleAdapter);
        
        
        //receive command
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
        getMenuInflater().inflate(R.menu.activity_task2_id1_album2, menu);
        return true;
    }
}
