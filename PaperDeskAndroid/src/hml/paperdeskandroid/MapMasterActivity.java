package hml.paperdeskandroid;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ResourceBundle.Control;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MapMasterActivity extends MapActivity {

	MapView mv;
	MapController mapController;
	
	Bitmap posBitmap;
	
	//this activity has inner class intends broadcast receiver to recive msg from service that comm with pc
	MyReceiver receiver;
	
	
	//command
	public String command = "hide";
	public boolean bCommandChanged = false;
	
	
	//The property of slave display
	int slaveMapCenterLong = 0;
	int slaveMapCenterLat = 0;
	int slaveMapZoomLevel = 1;
	
	public class MyReceiver extends BroadcastReceiver
	{
		public MyReceiver()
		{
			
		}
		
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			command = bundle.getString("command");
			//if(command.startsWith("map"))
			{
				//notify other devices
				bCommandChanged = true;
				
				String token[] = command.split(",");
				Toast toast = Toast.makeText(MapMasterActivity.this, "command recevied", Toast.LENGTH_LONG);
				toast.show();
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_map_master);
        
        posBitmap = BitmapFactory.decodeResource(getResources(),
    			R.drawable.pos);
        
        mv = (MapView)findViewById(R.id.mv);
        mv.setBuiltInZoomControls(true);
        mv.displayZoomControls(true);
        mapController = mv.getController();
        updateMapView();
        try {
        	new Thread(NotifStuff).start();
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("end", "end");
		}
        Log.d("end2", "end2");
        //initToPCConnection();
        registerBroadcastReceiver();
        Log.d("start", "start");
        
        
        
        
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
		ol.add(new PosOverLay(mapPoint, posBitmap, this)); 
	}
	
    //Thread used to broadcast PC's notif to other devices
    private Runnable NotifStuff = new Thread()
    {
    	public void run()
    	{
    		Log.d("run", "run");
    		try {
    			ServerSocket serverSocket = new ServerSocket(4444);
    			while(true)
    			{
//    				Toast.makeText(MapMasterActivity.this, "Ready to receive", Toast.LENGTH_SHORT).show();
//    				Log.d("slave", "slave");
    				Socket socket = serverSocket.accept();
    				Log.d("connected", "connected");
    				
    				OutputStream os = socket.getOutputStream();
    				PrintWriter out = new PrintWriter(os, true);
    				//Toast.makeText(MapMasterActivity.this, "Connect to slave display", Toast.LENGTH_LONG).show();
    				while(true)
    				{
    					
    					if(bCommandChanged) //there are new command, apply it
    					{
    						Log.d("start to send msg", "start to send msg");
    						bCommandChanged = false;
    						
    						//parse the msg
    						//os.write("hello\n".getBytes("utf-8"));
    						out.println("hello");
    						Log.d("send msg successful", "send msg successful");
//    						if(command.startsWith("map"))
//    						{
//    							String token[] = command.split(",");
//    							String adjacent = token[1];
//    				    		String[] deviceId = adjacent.split("-");
//    				    		if(deviceId[0].equals("1"))  //1-2, 2 is at the right of 1
//    				    		{
//    				    			//Calculate the map center lat/long of other device and inform to other device
//        							CalculateMapOffset(1); //means slave at right
//    				    		}
//    				    		else if(deviceId[1].equals("1")) //2-1, 2 is at the left of 1
//    				    		{
//    				    			//Calculate the map center lat/long of other device and inform to other device
//        							CalculateMapOffset(-1); //means slave at left
//    				    		}
//    							//String adjacent[] = token[1].split("-");
//    				    		
//    				    		//send it to device
//    				    		Toast.makeText(MapMasterActivity.this, "-"+slaveMapCenterLat+"-"+slaveMapCenterLong+"-"+slaveMapZoomLevel, Toast.LENGTH_LONG).show();
//    				    		os.write((""+slaveMapCenterLat+"-"+slaveMapCenterLong+"-"+slaveMapZoomLevel+"\n").getBytes("utf-8"));
//    						}
    					}
    				}
    			}
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("slavemapactivity", e.toString());
			}
    	}
    	
    	//Now we only support two devices, one master(id 1), one slave(id 2) 
    	public void CalculateMapOffset(int side) //side -1 means at slave at left, 1 means right
    	{
    		//Get the size of screen
    		DisplayMetrics displaysMetrics = new DisplayMetrics();
    		getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
    		int screenWidth = displaysMetrics.widthPixels;
    		int screenHeight = displaysMetrics.heightPixels;
    		
    		GeoPoint ptGeoCenter = mv.getProjection().fromPixels(screenWidth/2, screenHeight/2);
    		GeoPoint ptGeoEdge = mv.getProjection().fromPixels(0, screenHeight/2);
    		
    		slaveMapCenterLong = ptGeoCenter.getLongitudeE6();
    		slaveMapCenterLat = ptGeoCenter.getLatitudeE6() + side * ptGeoEdge.getLatitudeE6() * 2;
    		slaveMapZoomLevel = mv.getZoomLevel();
    		
    	}
    };
}
