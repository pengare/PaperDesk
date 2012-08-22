package hml.paperdeskandroid;

import hml.paperdeskandroid.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final String id = "1";  //device id
	private static final String HostIP = "130.15.5.136";
	
	ImageView imageView;
	
	//Used to simulate keyboard or touch event
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
        imageView = (ImageView)findViewById(R.id.imageView);
        //new Thread(DataStuff).start();
        
        
        Button btnStart = (Button)findViewById(R.id.buttonStartKeyService);
        Button btnEnd = (Button)findViewById(R.id.buttonEndKeyService);
        final Intent intent = new Intent();
        intent.setClass(MainActivity.this, KeySimulationService.class);
        
        btnStart.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Start key service
		        
		        startService(intent);
				
			}
		});
        
        btnEnd.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(intent);
			}
		});
        
    }
    
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent msg)
    {
		
		if (keyCode == KeyEvent.KEYCODE_Q)	
			this.finish();
		else if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
			imageView.setImageResource(R.drawable.pile2);
		else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
			imageView.setImageResource(R.drawable.pile3);
    	
    	return super.onKeyDown(keyCode, msg);
    }
	
	private Runnable DataStuff = new Thread() {
    	public void run() {
    		
    		try
    		{	
    			imageView.clearFocus();
    			Socket socket = new Socket(HostIP, 2222);
    			//Socket socket = new Socket("192.168.0.197", 2222);
    			Log.d(id, "Created Socket");
    			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    					
    			//out.println(id); //tell device id to host
    			
    			while(true)
    			{
    				String msg = in.readLine();
    				if(msg.equals("w"))
    				{
    					simulateKey(KeyEvent.KEYCODE_DPAD_UP);
    				}
    				else if(msg.equals("s"))
    				{
    					simulateKey(KeyEvent.KEYCODE_DPAD_DOWN);
    				}
    				else if(msg.equals("a"))
    				{
    					simulateKey(KeyEvent.KEYCODE_DPAD_LEFT);
    				}
    				else if(msg.equals("d"))
    				{
    					simulateKey(KeyEvent.KEYCODE_DPAD_RIGHT);
    				}
    				else if(msg.equals("e"))
    				{
    					simulateKey(KeyEvent.KEYCODE_DPAD_CENTER);
    				}
    				else if(msg.equals("r"))
    				{
    					simulateKey(KeyEvent.KEYCODE_BACK);
    				}
    					
    				
    				//Below is old change picture code
/*    	            String msg = in.readLine();
    	            String[] tokens = msg.split(" ");
    	            String imageName = "";
    	            if(id.equals("1"))
    	            	imageName = tokens[1];
    	            else if(id.equals("2"))
    	            	imageName = tokens[3];
    	            else
    	            	imageName = tokens[5];
    	            
    	            if(imageName.equals("pile1"))
    	            {
    	            	imageView.post(new Runnable() {
    	            	        public void run() {
    	            	        	imageView.setImageResource(R.drawable.pile1);
    	            	        }
    	            	      });
    	            }
    	            else if(imageName.equals("pile2"))  	
    	            {
    	            	imageView.post(new Runnable() {
	            	        public void run() {
	            	        	imageView.setImageResource(R.drawable.pile2);
	            	        }
	            	      });
    	            }
    	            else if(imageName.equals("pile3"))  	
    	            {
    	            	imageView.post(new Runnable() {
	            	        public void run() {
	            	        	imageView.setImageResource(R.drawable.pile3);
	            	        }
	            	      });
    	            }*/
    			}
    		}
    		catch (UnknownHostException e) 
    		{
    			Log.d(id, "UnknownHost");
    			
    		} catch (IOException e) 
    		{
    			String error = e.toString();
    			String msg = e.getMessage();
    			Log.d(id, "IOException");
    		}
    		catch(Exception e)
    		{
    			String error = e.toString();
    			String msg = e.getMessage();
    			Log.d(id, error);
    		}
    		
    		
    	}
    	};
    	
    public static void simulateKey(final int KeyCode)
    {
    	try {
    		Instrumentation inst = new Instrumentation();
    		inst.sendKeyDownUpSync(KeyCode);
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Exception when sendKeyDownUpSync", e.toString());
		}
    	
    }
	
}
