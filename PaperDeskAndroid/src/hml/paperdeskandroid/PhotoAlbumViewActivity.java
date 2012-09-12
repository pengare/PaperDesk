package hml.paperdeskandroid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class PhotoAlbumViewActivity extends Activity {

	//this activity has inner class intends broadcast receiver to recive msg from service that comm with pc
	MyReceiver receiver;
	
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
        
        setContentView(R.layout.activity_photo_album_view);
        
        MainActivity.activeActivity = this;
        		
        ImageButton btnAnimal = (ImageButton)findViewById(R.id.imageAlbumButton1);
        btnAnimal.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PhotoAlbumViewActivity.this, PhotoViewerActivity.class);
				PhotoService.iAlbumIndex = 0;
				
				startActivity(intent);
				PhotoAlbumViewActivity.this.finish();		
			}
		});
        
        ImageButton btnArt = (ImageButton)findViewById(R.id.imageAlbumButton2);
        btnArt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PhotoAlbumViewActivity.this, PhotoViewerActivity.class);
				PhotoService.iPhotoIndex = 1;
				
				startActivity(intent);
				PhotoAlbumViewActivity.this.finish();
				
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo_album_view, menu);
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
								} 
								else if (zone.equals("warm")) 
								{
									PhotoService.iCurrentZone = 1; // warm zone
									if (PhotoService.iAlbumIndex != -1) {
										// start the album activity
										Intent intentPhotoList = new Intent();
										intentPhotoList.setClass(
												PhotoAlbumViewActivity.this,
												PhotoViewerActivity.class);
										startActivity(intentPhotoList);
										
										PhotoAlbumViewActivity.this.finish();
									}
								}
								else if(zone.equals("hot"))
								{
									PhotoService.iCurrentZone = 0; //hot zone
									if(PhotoService.iAlbumIndex != -1 && PhotoService.iPhotoIndex != -1)
									{
										//start the photo detail activity
										Intent intentPhotoDetail = new Intent();
										intentPhotoDetail.setClass(PhotoAlbumViewActivity.this, PhotoDetailViewActivity.class);
										startActivity(intentPhotoDetail);
										
										PhotoAlbumViewActivity.this.finish();
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
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};
}
