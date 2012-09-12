package hml.paperdeskandroid;

import hml.paperdeskandroid.PhotoAlbumViewActivity.MyReceiver;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PhotoDetailViewActivity extends Activity {

	int[] photoIds = new int[]
	{
		R.drawable.photo_1, R.drawable.photo_2, R.drawable.photo_3,
		R.drawable.photo_4, R.drawable.photo_5, R.drawable.photo_6,
		R.drawable.photo_7,R.drawable.photo_8,R.drawable.photo_9,
		R.drawable.photo_10, R.drawable.photo_11, R.drawable.photo_12
	};
	
	//this activity has inner class intends broadcast receiver to recive msg from service that comm with pc
	MyReceiver receiver;
	
	//int iSelPhotoIndex = 0;
	
	//command
	private String command = "hide";
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
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_photo_detail_view);
        
        MainActivity.activeActivity = this;
        
		ImageView photoDetail = (ImageView)findViewById(R.id.photoDetail);
		photoDetail.setImageResource(photoIds[PhotoService.iPhotoIndex]);
		
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo_detail_view, menu);
        return true;
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{
			ImageView photoDetail = (ImageView)findViewById(R.id.photoDetail);
			
			--PhotoService.iPhotoIndex;
			if(PhotoService.iPhotoIndex == -1)
				PhotoService.iPhotoIndex = photoIds.length-1;
			photoDetail.setImageResource(photoIds[PhotoService.iPhotoIndex]);
			
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
		{
			ImageView photoDetail = (ImageView)findViewById(R.id.photoDetail);
			
			++PhotoService.iPhotoIndex;
			if(PhotoService.iPhotoIndex == photoIds.length)
				PhotoService.iPhotoIndex = 0;		
			photoDetail.setImageResource(photoIds[PhotoService.iPhotoIndex]);
						
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
		{
			//record in variables to send to slave
			
			//command = "photo:"+iSelPhotoIndex+"\n";
			//bCommandChanged = true;
		}
		
		return super.onKeyDown(keyCode, event);
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
									intentAlbum.setClass(PhotoDetailViewActivity.this, PhotoAlbumViewActivity.class);
									startActivity(intentAlbum);
									
									PhotoDetailViewActivity.this.finish();
								} 
								else if (zone.equals("warm")) 
								{
									PhotoService.iCurrentZone = 1; // warm zone
									if (PhotoService.iAlbumIndex != -1) {
										// start the album activity
										Intent intentPhotoList = new Intent();
										intentPhotoList.setClass(
												PhotoDetailViewActivity.this,
												PhotoViewerActivity.class);
										startActivity(intentPhotoList);
										
										PhotoDetailViewActivity.this.finish();
									}
								}
								else if(zone.equals("hot"))
								{
									PhotoService.iCurrentZone = 0; //hot zone
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
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};
}
