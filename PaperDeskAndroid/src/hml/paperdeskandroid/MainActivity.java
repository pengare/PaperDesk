package hml.paperdeskandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.R.integer;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	public static boolean bDeviceSet = false;
	public static boolean bTaskSet = false;
	
	//all information saved here 
	public static String id = "0";  //device id, used to mark master and slave display. initialize during press role button
	public static final String HostIP = "130.15.5.136"; //macbook air
	//public static final String HostIP = "130.15.5.144"; //lab mac pro
	//public static final String HostIP = "130.15.5.156";
	//public static final String HostIP = "192.168.0.105";
	public static final int HostIPPort = 7777;
	
	public static final int HostKeyboardIPPort = 7778;
	
	public static final int AndroidServerListenPort = 8888;
	
	public static Activity activeActivity;
	
	public static final int clientNum = 2; //include primary display
	
	//public static final String MasterDisplayIP = "130.15.5.171"; //phone
	//public static final String MasterDisplayIP = "130.15.5.169";
	
	public static final String MasterDisplayIP = "130.15.5.178"; //72
	public static final int MasterDisplayPort = 3333; //for the other two displays to connect
	
	public static final int clientPort[] = {0, 4444, 5555};  //
	public static Socket clientSocket[] = new Socket[3];
	public static OutputStream os[] = new OutputStream[3];
	
	
	public static String[] clientStatus = {"mainApp", "blank", "blank"};  //blank, mainApp, map, doc, email, photo
	ImageView imageView;
	
	
	//System info
	public static int screenWidth, screenHeight;
	
	public static boolean[] clientCommandChanged = {false, false, false}; //0 should always be false, it is primary
	public static String[] clientCommand = {"", "", ""};  // 0 should always be empty, because it is primary
	
	public static MapMasterActivity mapActivity;
	
	enum TaskType
	{
		Preliminary,
		Training,
		Task1Document,
		Task2Photo,
		Task3Email,
		Task4Map
	}
	
	
	public static  TaskType taskType = TaskType.Preliminary;
	
	//Used to simulate keyboard or touch event
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set Full Screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_main);
        
        initializeSystemInfo(); //get and save system info like screen solution
        
        
        Button btnStartClient1 = (Button)findViewById(R.id.btnStartClient0);
        btnStartClient1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.id = "0";  //master display
				

				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(MainActivity.this,
						TaskChooserActivity.class);
				startActivity(intentTaskChooser);

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, KeySimulationService.class);
				startService(intent);
				
			}
		});
        
        Button btnStartClient2 = (Button)findViewById(R.id.btnStartClient1);
        btnStartClient2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.id = "1"; //slave
				
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(MainActivity.this,
						TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Intent intent = new Intent();
		        intent.setClass(MainActivity.this, KeySimulationSlaveService.class);
				startService(intent);
			}
		});
        
        Button btnStartClient3 = (Button)findViewById(R.id.btnStartClient2);
        btnStartClient3.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.id = "2"; //slave
				
				Intent intentTaskChooser = new Intent();
				intentTaskChooser.setClass(MainActivity.this,
						TaskChooserActivity.class);
				startActivity(intentTaskChooser);
				
				Intent intent = new Intent();
		        intent.setClass(MainActivity.this, KeySimulationSlaveService.class);
				startService(intent);
			}
		});
        
//        
//        Button btnStart = (Button)findViewById(R.id.buttonStartKeyService);
//        Button btnEnd = (Button)findViewById(R.id.buttonEndKeyService);
//        final Intent intent = new Intent();
//        intent.setClass(MainActivity.this, KeySimulationService.class);
//        
//        btnStart.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//Start key service
//		        
//		        startService(intent);
//				
//			}
//		});
//        
//        btnEnd.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				stopService(intent);
//			}
//		});
//        
//        Button btnDoc = (Button)findViewById(R.id.btnDoc);
//        btnDoc.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intentDoc = new Intent();
//				intentDoc.setClass(MainActivity.this, DocBookViewActivity.class);
//				startActivity(intentDoc);
//				
//			}
//		});
//        
//        Button btnMap = (Button)findViewById(R.id.btnMap);
//        btnMap.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intentMap = new Intent();
//				intentMap.setClass(MainActivity.this, MapMasterActivity.class);
//				startActivity(intentMap);
//				
//			}
//		});
//        
//        Button btnMapSlave = (Button)findViewById(R.id.btnMapSlave);
//        btnMapSlave.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intentMapSlave = new Intent();
//				intentMapSlave.setClass(MainActivity.this, MapSlaveActivity.class);
//				startActivity(intentMapSlave);
//				
//			}
//		});
//        
//        Button btnPhoto = (Button)findViewById(R.id.btnPhotoViewer);
//        btnPhoto.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intentPhotoViewer = new Intent();
//				intentPhotoViewer.setClass(MainActivity.this, PhotoViewerActivity.class);
//				startActivity(intentPhotoViewer);
//				
//			}
//		});
//        
//        Button btnPhotoSlave = (Button)findViewById(R.id.btnPhotoViewerSlave);
//        btnPhotoSlave.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intentPhotoViewerSlave = new Intent();
//				intentPhotoViewerSlave.setClass(MainActivity.this, PhotoViewerSlaveActivity.class);
//				startActivity(intentPhotoViewerSlave);	
//			}
//		});
//        
//        Button btnPhotoEmail = (Button)findViewById(R.id.btnPhotoViewerEmail);
//        btnPhotoEmail.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intentPhotoViewerEmail = new Intent();
//				intentPhotoViewerEmail.setClass(MainActivity.this, PhotoViewerEmailActivity.class);
//				startActivity(intentPhotoViewerEmail);	
//				
//			}
//		});
        
    }
    
    
    public void initializeSystemInfo()
    {
    	//Get the size of screen
		DisplayMetrics displaysMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
		screenWidth = displaysMetrics.widthPixels;
		screenHeight = displaysMetrics.heightPixels;
		
    }
    
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent msg)
    {
		
		if (keyCode == KeyEvent.KEYCODE_Q)	
			this.finish();
    	
    	return super.onKeyDown(keyCode, msg);
    }
	
	
}
