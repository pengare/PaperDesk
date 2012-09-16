package hml.paperdeskandroid;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomButtonsController.OnZoomListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class Task4Id0MapActivity extends MapActivity {

	MapView mv;
	MapController mapController;
	EditText editTextSearch;
	String strSearchWord = "";
	
	int collocateSide = 1;//side -1 means at slave at left, 1 means right
	//command
	private String command = "hide";
	public static boolean bCommandChanged = false;
	
	//The property of slave display
	int slaveMapCenterLong = 0;
	int slaveMapCenterLat = 0;
	int slaveMapZoomLevel = 1;
	
	
	//Pin
	Bitmap posBitmap;
	
	Handler myHandler;
	
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
			if(command.startsWith("collocate"))
			{
				Task4Service.bCollocate = true;
				
				String tokens[] = command.split("\\#");
				String adjacent = tokens[1];
				String[] deviceId = adjacent.split("\\:");
				if(deviceId[0].equals("0"))   //0-1, 1 is at the right of 0
				{
					collocateSide = 1; //on the right
					Task4Service.xOffset = 0;
					Task4Service.yOffset = 0;
					
					//Calculate the map center lat/long of other device and inform to other device
					CalculateMapOffset(collocateSide);
					
					MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
					MainActivity.clientCommandChanged[1] = true;
				}
				else if(deviceId[1].equals("0")) //1-0, 1 is at the left of 0
				{
					collocateSide = -1; //on the left
					Task4Service.xOffset = 0;
					Task4Service.yOffset = 0;
					
					//Calculate the map center lat/long of other device and inform to other device
					CalculateMapOffset(collocateSide);
					
					MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
					MainActivity.clientCommandChanged[1] = true;
				}
			}
			else if(command.endsWith("bendsensortopup") )
			{
				mapController.zoomOut();
				
       		    if(Task4Service.bCollocate)
				{
					//Calculate the map center lat/long of other device and inform to other device
					CalculateMapOffset(collocateSide);
					
					MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
					MainActivity.clientCommandChanged[1] = true;
				}
/*				Message notif = new Message();
				notif.what = 0x2000;   //bend sensor
				myHandler.sendMessage(notif);*/
			}
			else if(command.endsWith("bendsensortopdown"))
			{
				mapController.zoomIn();
				
       		    if(Task4Service.bCollocate)
				{
					//Calculate the map center lat/long of other device and inform to other device
					CalculateMapOffset(collocateSide);
					
					MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
					MainActivity.clientCommandChanged[1] = true;
				}
			}
			else if(command.startsWith("xOffsetInc"))
			{
				Task4Service.xOffset += 1E5;
				
    					//Calculate the map center lat/long of other device and inform to other device
				CalculateMapOffset(collocateSide);
				
				MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
				MainActivity.clientCommandChanged[1] = true;
			}
			else if(command.startsWith("xOffsetDec"))
			{
				Task4Service.xOffset -= 1E5;
				
    					//Calculate the map center lat/long of other device and inform to other device
				CalculateMapOffset(collocateSide);
				
				MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
				MainActivity.clientCommandChanged[1] = true;
			}
			else if(command.startsWith("yOffsetInc"))
			{
				Task4Service.yOffset += 2E5;
				
    					//Calculate the map center lat/long of other device and inform to other device
				CalculateMapOffset(collocateSide);
				
				MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
				MainActivity.clientCommandChanged[1] = true;
			}
			else if(command.startsWith("yOffsetDec"))
			{
				Task4Service.yOffset -= 2E5;
				
    					//Calculate the map center lat/long of other device and inform to other device
				CalculateMapOffset(collocateSide);
				
				MainActivity.clientCommand[1] = "location#"+slaveMapCenterLat+":"+slaveMapCenterLong+":"+slaveMapZoomLevel+"\n";
				MainActivity.clientCommandChanged[1] = true;
			}
			else if(command.startsWith("bendsensorleftdown"))  //bend left down to type, bend again to confirm search
			{
				if(!Task4Service.bInSearch) //user want to search
				{
					Task4Service.bInSearch = true;
					
					editTextSearch.setVisibility(View.VISIBLE);
					editTextSearch.requestFocus();
					
				}
				else //user want to search
				{
					Toast.makeText(Task4Id0MapActivity.this, "finding location", Toast.LENGTH_SHORT).show();
					
					//reset search bar
					Task4Service.bInSearch = false;
					
					editTextSearch.setVisibility(View.INVISIBLE);
					strSearchWord = "";
				}
			}
			else if(command.startsWith("bendsensorleftup"))  //bend left up to hide input box
			{
				if(Task4Service.bInSearch)
				{
					Task4Service.bInSearch = false;
					
					editTextSearch.setVisibility(View.INVISIBLE);
					strSearchWord = "";
				}
			}
			else if(command.startsWith("taskChooser"))
			{
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(Task4Id0MapActivity.this, TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Task4Id0MapActivity.this.finish();
			}
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
		
		slaveMapCenterLong =  Task4Service.yOffset + ptGeoCenter.getLongitudeE6() + side * (ptGeoCenter.getLongitudeE6() - ptGeoEdge.getLongitudeE6()) * 2;
		slaveMapCenterLat = Task4Service.xOffset +  ptGeoCenter.getLatitudeE6();
		slaveMapZoomLevel = mv.getZoomLevel();
		
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_task4_id0_map);
        
        posBitmap = BitmapFactory.decodeResource(getResources(),
    			R.drawable.pos);
        
        
        mv = (MapView)findViewById(R.id.mapViewTask4Id0);
/*        mv.setBuiltInZoomControls(true);
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
		});*/
        
       // mv.displayZoomControls(true);
        
        mapController = mv.getController();
        updateMapView();
        
        
        editTextSearch = (EditText)findViewById(R.id.editTextTask4Id0SearchMap);
        editTextSearch.setVisibility(View.INVISIBLE);
        
        registerBroadcastReceiver();
    }

	
	public void updateMapView()
	{
		double lon = -76.5 * 1E6;
		double lat = 44.25 * 1E6;
		mapController.setZoom(14);
		GeoPoint mapPoint = new GeoPoint((int)lat, (int)lon);
		mapController.animateTo(mapPoint);
		
//		//pin on map
//		List<Overlay> ol = mv.getOverlays();
//		ol.clear();
//		ol.add(new PosOverLay(mapPoint, posBitmap)); 
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
        getMenuInflater().inflate(R.menu.activity_task4_id0_map, menu);
        return true;
    }
    
	@Override
	protected boolean isRouteDisplayed()
	{
		return true;
	}
}
