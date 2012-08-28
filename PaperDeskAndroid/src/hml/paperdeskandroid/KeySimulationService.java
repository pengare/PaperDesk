package hml.paperdeskandroid;

import android.app.Instrumentation;
import android.app.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class KeySimulationService extends Service {
    
	private static final String id = "1";  //device id
	private static final String HostIP = "130.15.5.136";
	
	//this service can broadcast specific activty with action
	public static final String receiverAction = "hml.paperdeskandroid.action.command";
	
	
	public KeySimulationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate()
    {
    	super.onCreate();
    	new Thread(DataStuff).start();
    	
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();	
    }
    
    
    //Thread used to receive notif from PC
	private Runnable DataStuff = new Thread() {
    	public void run() {
    		
    		try
    		{	
    			//Connect to PC
    			Socket socket = new Socket(HostIP, 2222);
    			//Socket socket = new Socket("192.168.0.197", 2222);
    			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    					
    			//out.println(id); //tell device id to host
    			
    			while(true)
    			{
    				String msg = in.readLine();
    				//msg is a navigation key
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
    				//msg is a command or status notif
    				else if(msg.equals("show"))
    				{
    					broadcastCommand(msg);
    					
//    					//notify other devices
//    					command = "show";
//    					bCommandChanged = true;
    				}
    				else if(msg.equals("hide"))
    				{
    					broadcastCommand(msg);
    					
//    					//notify other devices
//    					command = "hide";
//    					bCommandChanged = true;
    				}
    				else if(msg.startsWith("map"))
    				{
    					broadcastCommand(msg);
//    					command = msg;
//    					bCommandChanged = true;
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
    	
    	
        public void simulateKey(final int KeyCode)
        {
        	try {
        		Instrumentation inst = new Instrumentation();
        		inst.sendKeyDownUpSync(KeyCode);
    			
    		} catch (Exception e) {
    			// TODO: handle exception
    			Log.e("Exception when sendKeyDownUpSync", e.toString());
    			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
    		}
        	
        }
        
        public void broadcastCommand(String command)
        {     	
        	Intent intent = new Intent();
        	intent.setAction(receiverAction);
        	intent.putExtra("command", command);
        	sendBroadcast(intent); 	
        }
}
