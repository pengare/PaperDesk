package hml.paperdeskandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainAppActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_main_app);
        
//        if( MainActivity.id != "0")
//        	Log.v("Peng error", "The slave device 1 or 2 enter Main App Mode");
//        else
//        {
        	int id = Integer.parseInt(MainActivity.id);
            MainActivity.clientStatus[id] = "mainApp";
//        }
        
        Button btnDocApp = (Button)findViewById(R.id.btnDoc);
        btnDocApp.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intentDoc = new Intent();
				intentDoc.setClass(MainAppActivity.this, DocBookViewActivity.class);
				startActivity(intentDoc);
				
				MainAppActivity.this.finish();
				
			}
		});
        
        Button btnPhoto = (Button)findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (MainActivity.id == "0") 
				{
					Intent intentPhoto = new Intent();
					intentPhoto.setClass(MainAppActivity.this,
							PhotoAlbumViewActivity.class);
					startActivity(intentPhoto);
				}
				else if(MainActivity.id == "1" || MainActivity.id == "2")
				{
					Intent intentPhotoSlave = new Intent();
					intentPhotoSlave.setClass(MainAppActivity.this, PhotoViewerActivity.class);
					startActivity(intentPhotoSlave);
				}
				
				MainAppActivity.this.finish();		
			}
		});
        
        Button btnMap = (Button)findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (MainActivity.id == "0") 
				{
					Intent intentMap = new Intent();
					intentMap.setClass(MainAppActivity.this,
							MapMasterActivity.class);
					startActivity(intentMap);
				}
				else if(MainActivity.id == "1" || MainActivity.id == "2")
				{
					Intent intentMapSlave = new Intent();
					intentMapSlave.setClass(MainAppActivity.this, MapSlaveActivity.class);
					startActivity(intentMapSlave);
				}
				
				MainAppActivity.this.finish();		
			}
		});
        
        Button btnEmail = (Button)findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentEmail = new Intent();
				intentEmail.setClass(MainAppActivity.this, EmailFolderViewActivity.class);
				startActivity(intentEmail);
				
				MainAppActivity.this.finish();
				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_app, menu);
        return true;
    }
}
