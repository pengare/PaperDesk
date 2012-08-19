package hml.displaystack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class DisplayStackActivity extends Activity {
	private static final String id = "2";  //device id
	private static final String HostIP = "192.168.84.1";
	
	ImageView imageView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        imageView = (ImageView)findViewById(R.id.imageView);
        new Thread(DataStuff).start();
    }
    
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent msg)
    {
		
		if (keyCode == KeyEvent.KEYCODE_Q)	
			this.finish();
    	
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
    	            }
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
	
	
}