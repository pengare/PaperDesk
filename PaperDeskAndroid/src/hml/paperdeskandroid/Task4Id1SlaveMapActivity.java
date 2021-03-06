package hml.paperdeskandroid;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Task4Id1SlaveMapActivity extends MapActivity {

	MapView mvSlave;
	MapController mapControllerSlave;
	
	String command; //receive command from primary display
	
	//Pin
	Bitmap posBitmap;
	GeoPoint[] mapPinGeoPoints;
	int mapPinNum;
		
	Handler myHandler;
	
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
			
		    if(command.startsWith("location"))
			{
		    	String tokens[] = command.split("\\#");
		    	String location = tokens[1];
		    	String latlonglevel[] = location.split("\\:");
		    	Task4Service.slaveMapCenterLat = Integer.parseInt(latlonglevel[0]);
		    	Task4Service.slaveMapCenterLong = Integer.parseInt(latlonglevel[1]);
		    	Task4Service.slaveMapZoomLevel = Integer.parseInt(latlonglevel[2]);
				
		    	Message notif = new Message();
				notif.what = 0x2000;
				myHandler.sendMessage(notif);	    	
		    	
			}
		    else if(command.startsWith("showpin#"))
		    {
		    	showPins();
		    }
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task4Id1SlaveMapActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task4Id1SlaveMapActivity.this.finish();
			}
/*		    else  //bend sensor, xoffsetinc, dec 
		    {
		    	Message notif = new Message();
				notif.what = 0x2001;
				myHandler.sendMessage(notif);	
			}*/

		}
	}
	
	public void showPins()
	{
		posBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pos);
		
		//pin on map
		List<Overlay> ol = mvSlave.getOverlays();
		ol.clear();
		ol.add(new PosOverLay(mapPinGeoPoints, posBitmap)); 
		
		//Get the size of screen
		int screenWidth = MainActivity.screenWidth;
		int screenHeight = MainActivity.screenHeight;
		
		GeoPoint ptGeoCenter = mvSlave.getProjection().fromPixels(screenWidth/2, screenHeight/2);
		mapControllerSlave.animateTo(ptGeoCenter);
		
		
		//call slave to show pin
		MainActivity.clientCommand[1] = "showpin#\n";
		MainActivity.clientCommandChanged[1] = true;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
        setContentView(R.layout.activity_task4_id1_slave_map);
        
        posBitmap = BitmapFactory.decodeResource(getResources(),
    			R.drawable.pos);
        
        mvSlave = (MapView)findViewById(R.id.mapViewTask4Id1Slave);
        //mvSlave.setBuiltInZoomControls(true);
        //mvSlave.displayZoomControls(true);
        mapControllerSlave = mvSlave.getController();
        
        mapPinNum = Task4Service.mapPinNum;
        mapPinGeoPoints = new GeoPoint[mapPinNum];
		for(int i = 0; i < mapPinNum; ++i)
		{
			mapPinGeoPoints[i] = new GeoPoint(Task4Service.pinLat[i], Task4Service.pinLong[i]);
		}
		
        updateMapView();
        
        registerUIHandler();
        registerBroadcastReceiver();
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
        			mapControllerSlave.setZoom(Task4Service.slaveMapZoomLevel);
        			GeoPoint mapPoint = new GeoPoint((int)Task4Service.slaveMapCenterLat, (int)Task4Service.slaveMapCenterLong);
        			mapControllerSlave.setCenter(mapPoint);
        			//mapControllerSlave.animateTo(mapPoint);
        		}
        		else if(msg.what == 0x2001)
        		{
/*        		    if(command.startsWith("key#0:bendsensortopup"))
        		    {
        		    	mapControllerSlave.zoomOut();
        		    }
        		    else if(command.startsWith("key#0:bendsensortopdown"))
        		    {
        		    	mapControllerSlave.zoomIn();
        		    }
        		    else */
/*        			if(command.startsWith("xOffsetInc"))
        		    {
        		    	Task4Service.slaveMapCenterLat += 1E6;
        		    }
        		    else if(command.startsWith("xOffsetDec"))
        		    {
        		    	Task4Service.slaveMapCenterLat -= 1E6;
        		    }*/
        		}
        	}
        };
	}
	
	public void updateMapView()
	{
		mapControllerSlave.setZoom(Task4Service.slaveMapZoomLevel);
		GeoPoint mapPoint = new GeoPoint((int)Task4Service.slaveMapCenterLat, (int)Task4Service.slaveMapCenterLong);
		mapControllerSlave.animateTo(mapPoint);
		
//		//pin on map
//		List<Overlay> ol = mv.getOverlays();
//		ol.clear();
//		ol.add(new PosOverLay(mapPoint, posBitmap)); 
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
        getMenuInflater().inflate(R.menu.activity_task4_id1_slave_map, menu);
        return true;
    }
    
	@Override
	protected boolean isRouteDisplayed()
	{
		return true;
	}
}
