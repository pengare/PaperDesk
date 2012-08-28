package hml.paperdeskandroid;

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
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MapSlaveActivity extends MapActivity {

	//boolean bMapInitialized = false;
	MapView mvSlave;
	MapController mapControllerSlave;
	
	//used to comm with master map device(main eink)
	private static final String id = "2";  //device id
	private static final String MasterDisplayIP = "130.15.5.171";//"130.15.5.169";
	
	Handler myHandler;
	
	boolean bSlaveMapInitialized = false;
	
	//geo center of display, set by thread that receives msg from master, and used by UI thread to update mapview
	int slaveMapCenterLong = 0;
	int slaveMapCenterLat = 0;
	int slaveMapZoomLevel = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d("slave start", "try to connect");
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_map_slave);
        //Handler to receive information from master dispaly and update
        myHandler = new Handler()
        {
        	public void handleMessage(Message msg)
        	{
        		if(msg.what == 0x1234)
        		{
        			if(!bSlaveMapInitialized)
        			{
        				bSlaveMapInitialized = true;
        				mvSlave.setVisibility(MapView.VISIBLE);
        				
        			}
        			//mvSlave.setVisibility(MapView.VISIBLE);
        			GeoPoint ptCenter = new GeoPoint(slaveMapCenterLat, slaveMapCenterLong);
        			mapControllerSlave.setCenter(ptCenter);
        			mapControllerSlave.setZoom(slaveMapZoomLevel);
        			Toast.makeText(MapSlaveActivity.this, ("lat:" + slaveMapCenterLat + " long:" + slaveMapCenterLong + " zoomLevel:" + slaveMapZoomLevel), Toast.LENGTH_SHORT).show();
        			//mvSlave.invalidate();
        		}
        		
        	}
        };
        
        
        mvSlave = (MapView)findViewById(R.id.mvSlave);
        //mvSlave.setVisibility(MapView.INVISIBLE);
//        try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        mvSlave.setVisibility(MapView.VISIBLE);
           
      //Start to receive msg from master device
        new Thread(SlaveDataStuff).start();
        
        mvSlave.setBuiltInZoomControls(true);
        mvSlave.displayZoomControls(true);
        mapControllerSlave = mvSlave.getController();
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
	private Runnable SlaveDataStuff = new Thread()
	{
		public void run()
		{
			Log.d("slave run method", "slave run method");
			try {
				Log.d("try to connect", "try to connect");
				Socket socket = new Socket(MasterDisplayIP, 4444);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				Log.d("ready to receive msg", "ready to receive msg");
				while(true)
				{
					String msg = in.readLine();
					Log.d("receive msg successlyy", "receive msg sucessfuy");
					Toast.makeText(MapSlaveActivity.this, "receiveMsg", Toast.LENGTH_SHORT).show();
//					String msg = in.readLine();
//					String tokens[] = msg.split("-");
//					slaveMapCenterLat = Integer.parseInt(tokens[0]);
//					slaveMapCenterLong = Integer.parseInt(tokens[1]);
//					slaveMapZoomLevel = Integer.parseInt(tokens[2]);
//					//inform UI thread to update map
//					Message notif = new Message();
//					notif.what = 0x1234;
//					myHandler.sendMessage(notif);

				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("errrrrrorrrrr", e.toString());
			}
		}
	};
}
