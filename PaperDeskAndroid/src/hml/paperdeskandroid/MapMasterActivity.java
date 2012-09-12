package hml.paperdeskandroid;

import java.util.List;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ZoomButtonsController.OnZoomListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapMasterActivity extends MapActivity {

	MapView mv;
	MapController mapController;
	
	Bitmap posBitmap;
	
	//this activity has inner class intends broadcast receiver to recive msg from service that comm with pc
	MyReceiver receiver;
	
	
	//command
	private String command = "hide";
	public static boolean bCommandChanged = false;
	
	//The property of slave display
	int slaveMapCenterLong = 0;
	int slaveMapCenterLat = 0;
	int slaveMapZoomLevel = 1;
	
	public String key; //used to save pressed key sent by host pc
	public String collocateCommand = ""; //used to save collocate status

	Handler myHandler;
	
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
		}
	}
	
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MainActivity.mapActivity = this;
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_map_master);
        
        registerUIHandler();
        
        posBitmap = BitmapFactory.decodeResource(getResources(),
    			R.drawable.pos);
        
        mv = (MapView)findViewById(R.id.mv);
        mv.setBuiltInZoomControls(true);
        mv.getZoomButtonsController().setOnZoomListener(new OnZoomListener() {
			
			public void onZoom(boolean zoomIn) {
				// TODO Auto-generated method stub
				if(zoomIn)
					mapController.zoomIn();
				else
					mapController.zoomOut();
				
				bCommandChanged = true;
				
			}
			
			public void onVisibilityChanged(boolean visible) {
				// TODO Auto-generated method stub
				
			}
		});
        mv.displayZoomControls(true);
        mapController = mv.getController();
        updateMapView();
        //initToPCConnection();
        registerBroadcastReceiver();
        new Thread(NotifStuff).start();
        
    }

	
	//This handle the received command
	public void registerUIHandler()
	{
        //Handler to receive information from master dispaly and update
        myHandler = new Handler()
        {
        	@Override
			public void handleMessage(Message msg)
        	{
        		if(msg.what == 0x2000) //location:
        		{
        			
        			if(!key.equals(""))
        			{
        				if(key.equals("bendsensortopup"))
        				{
        					mapController.zoomOut();
        					
        					
        					if(!collocateCommand.equals(""))
        					{
        						command = collocateCommand;
        						bCommandChanged = true;
        					}
        					
        				}
        				else if(key.equals("bendsensortopdown"))
        				{
        					mapController.zoomIn();
        					
        					
        					if(!collocateCommand.equals(""))
        					{
        						command = collocateCommand;
        						bCommandChanged = true;
        					}

        				}
        				else if (key.equals("bendsensorbottomup"))
        				{
        					simulateOnePan(-1);
        					
        					if(!collocateCommand.equals(""))
        					{
        						command = collocateCommand;
        						bCommandChanged = true;
        					}
        				}
        				else if (key.equals("bendsensorbottomdown"))
        				{
        					simulateOnePan(1);

        					if(!collocateCommand.equals(""))
        					{
        						command = collocateCommand;
        						bCommandChanged = true;
        					}
        				}
        				
        				//at last reset key code
        				key = "";
        			}
        		}
        	}
        	
        	
        	public void simulateOnePan(int side)  //-1 left, 1 right
        	{
        		//Get the size of screen
        		int screenWidth = MainActivity.screenWidth;
        		int screenHeight = MainActivity.screenHeight;
        		
        		GeoPoint ptGeoCenter = mv.getProjection().fromPixels(screenWidth/2, screenHeight/2);
        		GeoPoint ptGeoEdge = mv.getProjection().fromPixels(0, screenHeight/2);
        		
        		int slaveMapCenterLong = ptGeoCenter.getLongitudeE6() + side * (ptGeoCenter.getLongitudeE6() - ptGeoEdge.getLongitudeE6());
        		int slaveMapCenterLat = ptGeoCenter.getLatitudeE6();
        		
        		GeoPoint mapPoint = new GeoPoint(slaveMapCenterLat, slaveMapCenterLong);
        		
        		
        		//TODO decide wihch is better on eink, animateto is smooth, setcenter is quick
        		mapController.animateTo(mapPoint);
        		//mapController.setCenter(mapPoint);
        		
        	}
        };
	}
	
	public void simulateTouchEvent()
	{
		try {
    		Instrumentation inst = new Instrumentation();
    		inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_LEFT);
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Exception when sendKeyDownUpSync", e.toString());
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
	}
/*    public void initToPCConnection()
    {
    	;//MainActivity.HostIP
    }*/
    
    public void registerBroadcastReceiver()
    {
    	receiver = new MyReceiver();
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(KeySimulationService.receiverAction);
    	this.registerReceiver(receiver, filter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map_master, menu);
        return true;
    }
    
	@Override
	protected boolean isRouteDisplayed()
	{
		return true;
	}
	
	
	public void updateMapView()
	{
		double lon = -76.5 * 1E6;
		double lat = 44.25 * 1E6;
		mapController.setZoom(14);
		GeoPoint mapPoint = new GeoPoint((int)lat, (int)lon);
		mapController.animateTo(mapPoint);
		
		//pin on map
		List<Overlay> ol = mv.getOverlays();
		ol.clear();
		ol.add(new PosOverLay(mapPoint, posBitmap)); 
	}

	
    //Thread used to broadcast PC's notif to other devices
    private Runnable NotifStuff = new Thread()
    {
    	@Override
		public void run()
    	{
    		try {
    			//ServerSocket serverSocket = new ServerSocket(2222);
    			while(true)
    			{
    				//Socket socket = serverSocket.accept();
    				//OutputStream os = socket.getOutputStream();
    				while(true)
    				{
    					if(bCommandChanged) //there are new command, apply it
    					{
    						bCommandChanged = false;
    						
    						if(command.equals("show"))
    						{
    							//os.write("show\n".getBytes("utf-8"));
    							//Toast toast = Toast.makeText(MapMasterActivity.this, "command recevied", Toast.LENGTH_LONG);
    							//toast.show();
    						}
    						else if(command.equals("hide"))
    						{
    							//os.write("hide\n".getBytes("utf-8"));
    							//Toast toast = Toast.makeText(MapMasterActivity.this, "command recevied", Toast.LENGTH_LONG);
    							//toast.show();
    						}
    						else if(command.startsWith("collocate"))
    						{
    							collocateCommand = command;
    							
    							Log.d("master map", "receive msg:" + command);
    							//os.write("show\n".getBytes("utf-8"));
    							String token[] = command.split("\\#");
    							String adjacent = token[1];
    							String[] deviceId = adjacent.split("\\:");
    							if(deviceId[0].equals("0")) //0-1, 1 is at the right of 0
    							{
    								//Calculate the map center lat/long of other device and inform to other device
    								//Toast.makeText(MapMasterActivity.this, "right", Toast.LENGTH_SHORT).show();
    								Log.d("right", "right");
    								CalculateMapOffset(1);
    								
    								if(deviceId[1].equals("1"))
    								{
    									MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
    									MainActivity.clientCommandChanged[1] = true;
    								}
    								else if(deviceId[1].equals("2"))
    								{
    									MainActivity.clientCommand[2] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
    									MainActivity.clientCommandChanged[2] = true;
    								}
    								
    							}
    							else if(deviceId[1].equals("0")) //1-0, 1 is at the left of 0
    							{
    								//Calculate the map center lat/long of other device and inform to other device
    								//Toast.makeText(MapMasterActivity.this, "right", Toast.LENGTH_SHORT).show();
    								Log.d("left", "left");
    								CalculateMapOffset(-1);
    								
    								if(deviceId[0].equals("1"))
    								{
    									MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
    									MainActivity.clientCommandChanged[1] = true;
    								}
    								else if(deviceId[0].equals("2"))
    								{
    									MainActivity.clientCommand[2] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
    									MainActivity.clientCommandChanged[2] = true;
    								}
    							}
    							else
    							{
    								Log.d("master map", "third chance");
    							}
    							
    							
    							//send it to slave device
    							Log.d("location#", ""+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel);
    							//os.write(("location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n").getBytes("utf-8"));
    						}
    						else if(command.startsWith("key"))
    						{
    							String tokens[] = command.split("\\#");
    							String deviceAndKey[] = tokens[1].split("\\:");
    							int deviceId = Integer.parseInt(deviceAndKey[0]);
    							String keyCode = deviceAndKey[1];
    							
    							if(deviceId == 0)  //self
    							{
    								key = keyCode;
    								
    								Message notif = new Message();
    								notif.what = 0x2000;
    								myHandler.sendMessage(notif);
    								
    							}
    							else
    							{
    								MainActivity.clientCommand[deviceId] = "key#" + keyCode + "\n";
    								MainActivity.clientCommandChanged[deviceId] = true;
    							}
    							
    						}
    					}
    				}
    			}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    	
    	
     	//Now we only support two devices, one master(id 1), one slave(id 2) 
    	public void CalculateMapOffset(int side) //side -1 means at slave at left, 1 means right
    	{
    		//Get the size of screen
    		int screenWidth = MainActivity.screenWidth;
    		int screenHeight = MainActivity.screenHeight;
    		
    		GeoPoint ptGeoCenter = mv.getProjection().fromPixels(screenWidth/2, screenHeight/2);
    		GeoPoint ptGeoEdge = mv.getProjection().fromPixels(0, screenHeight/2);
    		
    		slaveMapCenterLong = ptGeoCenter.getLongitudeE6() + side * (ptGeoCenter.getLongitudeE6() - ptGeoEdge.getLongitudeE6()) * 2;
    		slaveMapCenterLat = ptGeoCenter.getLatitudeE6();
    		slaveMapZoomLevel = mv.getZoomLevel();
    		
    	}

    };
}
