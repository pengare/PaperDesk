package hml.paperdeskandroid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class KeySimulationSlaveService extends Service {
	
	private static final String MasterDisplayIP = MainActivity.MasterDisplayIP;
	private static final int MasterDisplayPort = MainActivity.MasterDisplayPort;
	
	//this service can broadcst command from primary display to different slave activity
	public static final String receiverSlaveAction = "hml.paperdeskandroid.action.slave.command";
	
    public KeySimulationSlaveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		//thread get command from primary display
		new Thread(CommandFromPrimaryStuff).start();
	}
    
	
	private Runnable CommandFromPrimaryStuff = new Thread()
	{
		@Override
		public void run()
		{
			try
			{
				//connect to primary display
				
				Socket socket = new Socket(MasterDisplayIP, MasterDisplayPort);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				while(true)
				{
					String msg = in.readLine();
					
					//map command
//					if(msg.startsWith("location"))
//					{
//						broadcastCommand(msg);
//					}
//					else 
					
					broadcastCommand(msg);
					
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.v("slave error", e.toString());
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
    	intent.setAction(receiverSlaveAction);
    	intent.putExtra("command", command);
    	sendBroadcast(intent); 	
    }
    
}
