package hml.paperdeskandroid;

import hml.paperdeskandroid.MapMasterActivity.MyReceiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MapSlaveActivity extends MapActivity {

	boolean bInitialized = false;
	MapView mvSlave;
	MapController mapControllerSlave;
	
	//used to comm with master map device(main eink)
	private static final String id = "2";  //device id
	private static final String MasterDisplayIP = MainActivity.MasterDisplayIP;
	private static final int MasterDisplayPort = MainActivity.MasterDisplayPort;
	
	Handler myHandler;
	
	boolean bSlaveMapInitialized = false;
	
	//geo center of display, set by thread that receives msg from master, and used by UI thread to update mapview
	int slaveMapCenterLong = 0;
	int slaveMapCenterLat = 0;
	int slaveMapZoomLevel = 1;
	
	//this activity has inner class intends broadcast receiver to recive msg from service that comm with pc
	MyReceiver receiver;
	String commandFromPrimary; //receive command from primary display
	
	public class MyReceiver extends BroadcastReceiver
	{
		public MyReceiver()
		{
			
		}
		
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			commandFromPrimary = bundle.getString("command");
			Message notif = new Message();
			notif.what = 0x1000;
			myHandler.sendMessage(notif);
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_map_slave);
        //Handler to receive information from master dispaly and update
        myHandler = new Handler()
        {
        	public void handleMessage(Message msg)
        	{
        		if(msg.what == 0x1000) //location:
        		{
        			String tokens[] = commandFromPrimary.split("\\#");
        			String location = tokens[1];
        			String latlonglevel[] = location.split("\\:");
        			slaveMapCenterLat = Integer.parseInt(latlonglevel[0]);
        			slaveMapCenterLong = Integer.parseInt(latlonglevel[1]);
        			slaveMapZoomLevel = Integer.parseInt(latlonglevel[2]);
        			Log.d("slave map", ""+slaveMapCenterLat+"*"+slaveMapCenterLong+"*"+slaveMapZoomLevel);
        			
        			mvSlave.setVisibility(MapView.VISIBLE);
        			if(!bSlaveMapInitialized)
        			{
        				bSlaveMapInitialized = true;
        				mvSlave.setVisibility(MapView.VISIBLE);
        			}
        			GeoPoint ptCenter = new GeoPoint(slaveMapCenterLat, slaveMapCenterLong);
        			mapControllerSlave.setCenter(ptCenter);
        			mapControllerSlave.setZoom(slaveMapZoomLevel);
        		}
        		else if(msg.what == 0x5678)
        		{
        			mvSlave.setVisibility(MapView.INVISIBLE);
        		}
        	}
        };
        
        mvSlave = (MapView)findViewById(R.id.mvSlave);
        mvSlave.setVisibility(MapView.INVISIBLE);
//        try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        mvSlave.setVisibility(MapView.VISIBLE);
           
        mvSlave.setBuiltInZoomControls(true);
        mvSlave.displayZoomControls(true);
        mapControllerSlave = mvSlave.getController();
        
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
        getMenuInflater().inflate(R.menu.activity_map_slave, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return true;
	}
	
	//New thread to receive information from master display
	private Runnable DataStuff = new Thread()
	{
		public void run()
		{
			try {
				Socket socket = new Socket(MasterDisplayIP, MasterDisplayPort);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				while(true)
				{
					String msg = in.readLine();
					if(msg.equals("show"))
					{
						Message notif = new Message();
						notif.what = 0x1234;
						myHandler.sendMessage(notif);
					}
					else if(msg.equals("hide"))
					{
						Message notif = new Message();
						notif.what = 0x5678;
						myHandler.sendMessage(notif);
					}
					else
					{
						String tokens[] = msg.split("\\#");
						String location = tokens[1];
						String latlonglevel[] = location.split("\\:");
						slaveMapCenterLat = Integer.parseInt(latlonglevel[0]);
						slaveMapCenterLong = Integer.parseInt(latlonglevel[1]);
						slaveMapZoomLevel = Integer.parseInt(latlonglevel[2]);
						Log.d("slave map", ""+slaveMapCenterLat+"*"+slaveMapCenterLong+"*"+slaveMapZoomLevel);
						Message notif = new Message();
						notif.what = 0x1234;
						myHandler.sendMessage(notif);
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};
}
