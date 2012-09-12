package hml.paperdeskandroid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class PhotoViewerSlaveActivity extends Activity {

	//used to comm with master map device(main eink)
	private static final String id = "2";  //device id
	private static final String MasterDisplayIP = MainActivity.MasterDisplayIP;
	
	Handler myHandler;
	
	String strPickedPhotoIndex = "";
	
	int[] photoIds = new int[]
	{
		0, 0, 0,
		0, 0, 0,
		0, 0, 0,
		0, 0, 0
	};
	
	int iInsertPhotoIndex = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_photo_viewer_slave);
        
        //Handler to receive information from master display and update photo
        myHandler = new Handler()
        {
        	@Override
			public void handleMessage(Message msg)
        	{
        		if(msg.what == 0x1234)
        		{
        			String resName = "photo_"+strPickedPhotoIndex;
        			int resId = getResourceIdFromString(resName);
        			
        			photoIds[iInsertPhotoIndex] = resId;
        			++iInsertPhotoIndex;
        			if(iInsertPhotoIndex == photoIds.length)
        				iInsertPhotoIndex = 0;
        				
        			refreshPhotoGrid();
        		}
        	}
        };
        
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i=0; i < photoIds.length; ++i)
        {
        	Map<String, Object> listItem = new HashMap<String, Object>();
        	listItem.put("photo", photoIds[i]);
        	listItems.add(listItem);
        }
        
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
        		listItems,
        		R.layout.photo_grid_cell_slave,
        		new String[]{"photo"},
        		new int[]{R.id.image1});
        GridView grid = (GridView)findViewById(R.id.photoFavoriteGrid);
        grid.setAdapter(simpleAdapter);
        
        //Start to receive msg from master device
        new Thread(DataStuff).start();
    }

    public int getResourceIdFromString(String resourceName)
    {
    	int resourceId = getResources().getIdentifier(resourceName, "drawable", "hml.paperdeskandroid");
    	return resourceId;
    }
    
    public void refreshPhotoGrid()
    {
    	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i=0; i < photoIds.length; ++i)
        {
        	Map<String, Object> listItem = new HashMap<String, Object>();
        	listItem.put("photo", photoIds[i]);
        	listItems.add(listItem);
        }
        
		SimpleAdapter simpleAdapter = new SimpleAdapter(this,
        		listItems,
        		R.layout.photo_grid_cell_slave,
        		new String[]{"photo"},
        		new int[]{R.id.image1});
        GridView grid = (GridView)findViewById(R.id.photoFavoriteGrid);
        grid.setAdapter(simpleAdapter);	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo_viewer_slave, menu);
        return true;
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_U)
		{
			
			photoIds[1] = R.drawable.photo_2;
			
	        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	        for(int i=0; i < photoIds.length; ++i)
	        {
	        	Map<String, Object> listItem = new HashMap<String, Object>();
	        	listItem.put("photo", photoIds[i]);
	        	listItems.add(listItem);
	        }
	        
			SimpleAdapter simpleAdapter = new SimpleAdapter(this,
	        		listItems,
	        		R.layout.photo_grid_cell_slave,
	        		new String[]{"photo"},
	        		new int[]{R.id.image1});
	        GridView grid = (GridView)findViewById(R.id.photoFavoriteGrid);
	        grid.setAdapter(simpleAdapter);
			
		}
		else if(keyCode == KeyEvent.KEYCODE_I)
		{
			photoIds[1] = 0;
			
	        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	        for(int i=0; i < photoIds.length; ++i)
	        {
	        	Map<String, Object> listItem = new HashMap<String, Object>();
	        	listItem.put("photo", photoIds[i]);
	        	listItems.add(listItem);
	        }
			SimpleAdapter simpleAdapter = new SimpleAdapter(this,
	        		listItems,
	        		R.layout.photo_grid_cell_slave,
	        		new String[]{"photo"},
	        		new int[]{R.id.image1});
	        GridView grid = (GridView)findViewById(R.id.photoFavoriteGrid);
	        grid.setAdapter(simpleAdapter);
						
		}
		
		return super.onKeyDown(keyCode, event);
	}
    
    
    //New thread to receive information from master display
    private Runnable DataStuff = new Thread()
    {
    	@Override
		public void run()
    	{
    		try {
				Socket socket = new Socket(MasterDisplayIP, 3333);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				while(true)
				{
					String msg = in.readLine();
					if(msg.startsWith("photo:"))
					{
						Message notif = new Message();
						notif.what = 0x1234;
						String strTempIndex = msg.substring(6);
						int iTempIndex = Integer.parseInt(strTempIndex);
						++iTempIndex;
						strPickedPhotoIndex = ""+iTempIndex;
						myHandler.sendMessage(notif);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    };
}
