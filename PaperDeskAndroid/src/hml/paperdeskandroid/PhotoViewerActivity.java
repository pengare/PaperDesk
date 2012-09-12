package hml.paperdeskandroid;

import hml.paperdeskandroid.MapMasterActivity.MyReceiver;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.color;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class PhotoViewerActivity extends Activity {
	
	int[] photoIds = new int[]
	{
		R.drawable.photo_1, R.drawable.photo_2, R.drawable.photo_3,
		R.drawable.photo_4, R.drawable.photo_5, R.drawable.photo_6,
		R.drawable.photo_7,R.drawable.photo_8,R.drawable.photo_9,
		R.drawable.photo_10, R.drawable.photo_11, R.drawable.photo_12
	};
	
	boolean bInDetailView = false;
	
	int iSelPhotoIndex = 0;
	
	
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
			
			
/*			if(command.equals("cold"))
			{
				
			}
			else if(command.equals("warm"))
			{
				
			}
			else if(command.equals("hot"))
			{
				if(PhotoService.iAlbumIndex != -1 && PhotoService.iPhotoIndex != -1)
				{
					setContentView(R.layout.activity_photo_detail);
					ImageView photoDetail = (ImageView)findViewById(R.id.photoDetail);
					photoDetail.setImageResource(photoIds[iSelPhotoIndex]);
				}
			}
			bCommandChanged = true;*/
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_photo_viewer);
        
        MainActivity.activeActivity = this;
        
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
        GridView grid = (GridView)findViewById(R.id.photoGrid);
        grid.setAdapter(simpleAdapter);
        
        //registerBroadcastReceiver();
        
		grid.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, 
				int position , long id)
			{
				iSelPhotoIndex = position % photoIds.length;
				//bInDetailView = true;
				PhotoService.iPhotoIndex = iSelPhotoIndex;
				
				//view.setBackgroundColor(getResources().getColor(Color.GRAY));
			}
			public void onNothingSelected(AdapterView<?> parent){}			
		});

		grid.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent
				, View view, int position, long id)
			{
				iSelPhotoIndex = position % photoIds.length;
				//bInDetailView = true;
				PhotoService.iPhotoIndex = iSelPhotoIndex;
				
				Intent intentDetailView = new Intent();
				intentDetailView.setClass(PhotoViewerActivity.this, PhotoDetailViewActivity.class);
				startActivity(intentDetailView);
				
				PhotoViewerActivity.this.finish();
						
//				setContentView(R.layout.activity_photo_detail);
//				ImageView photoDetail = (ImageView)findViewById(R.id.photoDetail);
//				photoDetail.setImageResource(photoIds[iSelPhotoIndex]);
//				//setupListenerForImageView();
//				new Thread(NotifStuff).start();
			}
		});
		
        //receive command
        registerBroadcastReceiver();
        //process command
        new Thread(NotifStuff).start();
        
    }

    public void registerBroadcastReceiver()
    {
    	receiver = new MyReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(KeySimulationService.receiverAction);
    	this.registerReceiver(receiver, filter);
    }
//    public void setupListenerForImageView()
//    {
//    	ImageView photoDetail = (ImageView)findViewById(R.id.photoDetail);
//    	
//    	photoDetail.setOnKeyListener(new OnKeyListener() {
//			
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				// TODO Auto-generated method stub
//				if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
//				{
//					ImageView photoDetail = (ImageView)findViewById(R.id.photoDetail);
//					photoDetail.setImageResource(photoIds[iSelPhotoIndex]);
//					--iSelPhotoIndex;
//					if(iSelPhotoIndex == -1)
//						iSelPhotoIndex = photoIds.length-1;
//				}
//				else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
//				{
//					ImageView photoDetail = (ImageView)findViewById(R.id.photoDetail);
//					photoDetail.setImageResource(photoIds[iSelPhotoIndex]);
//					++iSelPhotoIndex;
//					if(iSelPhotoIndex == photoIds.length)
//						iSelPhotoIndex = 0;					
//				}
//				return false;
//			}
//		});
//    }
    

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo_viewer, menu);
        return true;
    }
    
    
	
	// Thread used to broadcast PC's notif to other devices
	private Runnable NotifStuff = new Thread() {
		@Override
		public void run() 
		{
			try 
			{
				while (true) 
				{
					if (bCommandChanged) // there are new command, apply it
					{
						bCommandChanged = false;

						if (command.startsWith("zone")) 
						{
							Log.d("master photo", "receive msg:" + command);
							String token[] = command.split("\\#");
							String[] deviceIdAndZone = token[1].split("\\:");
							int deviceId = Integer.parseInt(deviceIdAndZone[0]);
							String zone = deviceIdAndZone[1];

							if (deviceId == 0) // self
							{
								if (zone.equals("cold")) {
									PhotoService.iCurrentZone = 2; // cold zone
									
									Intent intentAlbum = new Intent();
									intentAlbum.setClass(PhotoViewerActivity.this, PhotoAlbumViewActivity.class);
									startActivity(intentAlbum);
									
									PhotoViewerActivity.this.finish();
								} 
								else if (zone.equals("warm")) 
								{
									PhotoService.iCurrentZone = 1; // warm zone
								}
								else if(zone.equals("hot"))
								{
									PhotoService.iCurrentZone = 0; //hot zone
									if(PhotoService.iAlbumIndex != -1 && PhotoService.iPhotoIndex != -1)
									{
										//start the photo detail activity
										Intent intentPhotoDetail = new Intent();
										intentPhotoDetail.setClass(PhotoViewerActivity.this, PhotoDetailViewActivity.class);
										startActivity(intentPhotoDetail);
										
										PhotoViewerActivity.this.finish();
									}
								}
							} else {
								MainActivity.clientCommand[deviceId] = "zone#"
										+ zone + "\n";
								MainActivity.clientCommandChanged[deviceId] = true;
							}
						} 
						else if (command.startsWith("key")) 
						{
/*							String tokens[] = command.split("\\#");
							String deviceAndKey[] = tokens[1].split("\\:");
							int deviceId = Integer.parseInt(deviceAndKey[0]);
							String keyCode = deviceAndKey[1];

							if (deviceId == 0) // self
							{
								key = keyCode;

								Message notif = new Message();
								notif.what = 0x2000;
								myHandler.sendMessage(notif);

							} 
							else 
							{
								MainActivity.clientCommand[deviceId] = "key#"
										+ keyCode + "\n";
								MainActivity.clientCommandChanged[deviceId] = true;
							}*/

						}
						
					}
					sleep(200);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};
    
/*    //Thread used to broadcast pick photo to slave devices
    private Runnable NotifStuff = new Thread()
    {
    	public void run()
    	{
    		try {
				ServerSocket serverSocket = new ServerSocket(3333);
				while(true)
				{
					Socket socket = serverSocket.accept();
					OutputStream os = socket.getOutputStream();
					while(true)
					{
						if(bCommandChanged)  //there are new command, apply it
						{
							bCommandChanged = false;
							
							if(command.startsWith("photo:"))
							{
								os.write(command.getBytes("utf-8"));
								command = "";
							}
							
							
						}
						
						sleep(200);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    };*/
    
    
    
}
