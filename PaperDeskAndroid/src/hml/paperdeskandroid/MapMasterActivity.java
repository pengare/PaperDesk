package hml.paperdeskandroid;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

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
	private String command = "hide";
	private boolean bCommandChanged = false;
	
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
			bCommandChanged = true;
			//if(command.startsWith("map"))
			{
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
        //initToPCConnection();
        registerBroadcastReceiver();
        new Thread(NotifStuff).start();
        
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
    	public void run()
    	{
    		try {
    			ServerSocket serverSocket = new ServerSocket(2222);
    			while(true)
    			{
    				Socket socket = serverSocket.accept();
    				OutputStream os = socket.getOutputStream();
    				while(true)
    				{
    					if(bCommandChanged) //there are new command, apply it
    					{
    						bCommandChanged = false;
    						
    						if(command.equals("show"))
    							os.write("show\n".getBytes("utf-8"));
    						else
    							os.write("hide\n".getBytes("utf-8"));
    					}
    				}
    			}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    };
}
