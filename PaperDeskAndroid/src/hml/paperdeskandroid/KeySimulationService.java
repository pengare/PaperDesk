package hml.paperdeskandroid;

import android.R.integer;
import android.app.Instrumentation;
import android.app.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class KeySimulationService extends Service {
    
	private static final String id = "1";  //device id
	//private static final String HostIP = "130.15.5.136";
	private static final String HostIP = MainActivity.HostIP;
	private static final int HostIPPort = MainActivity.HostIPPort;
	
	private static final int HostKeyboardIPPort = MainActivity.HostKeyboardIPPort;
	private static final int HostTouchSensorPort = MainActivity.HostTouchSensorPort;
	
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
    	//thread get command from pc, port is 7777
    	new Thread(DataStuff).start();
    	
    	//thread get command from pc keyboard, port is 7778
    	new Thread(KeyboardStuff).start();
    	
    	//thread get command from pc keyboard, port is 7778
    	new Thread(TouchSensorStuff).start();
    	
    	//thread send processed command to secondary display
    	new Thread(CommandToClientStuff).start();
    	
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();	
    }
    
    //Thread used to receive notif from PC
  	private Runnable TouchSensorStuff = new Thread() {
      	@Override
  		public void run() {
      		
      		try
      		{	
      			//Connect to PC
      			Socket socket = new Socket(HostIP, HostTouchSensorPort);
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
      					broadcastCommand("return");
      				}
      				else if(msg.equals("left"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_LEFT);
      				}
      				else if(msg.equals("right"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_RIGHT);
      				}
      				else if(msg.equals("up"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_UP);
      				}
      				else if(msg.equals("down"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_DOWN);
      				}
      				else if(msg.equals("enter"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_CENTER);
      				}
      				else if(msg.equals("esc"))
      				{
      					if(MainActivity.activeActivity != null)
      					{
      						Intent intentMainApp = new Intent();
      						intentMainApp.setClass(MainActivity.activeActivity, MainAppActivity.class);
      						intentMainApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      						startActivity(intentMainApp);
      						
      						MainActivity.activeActivity.finish();			
      					}
      				}
      				else if(msg.equals("i"))
      				{
      					simulateKey(KeyEvent.KEYCODE_I);
      				}
      				//msg is a command or status notif
      				else if(msg.equals("show"))
      				{
      					broadcastCommand(msg);
      					
//      					//notify other devices
//      					command = "show";
//      					bCommandChanged = true;
      				}
      				else if(msg.equals("hide"))
      				{
      					broadcastCommand(msg);
      					
//      					//notify other devices
//      					command = "hide";
//      					bCommandChanged = true;
      				}
      				else if(msg.startsWith("map"))
      				{
      					broadcastCommand(msg);
//      					command = msg;
//      					bCommandChanged = true;
      				}
      				else if(msg.equals("hot"))
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.equals("warm"))
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.equals("cold"))
      				{
      					broadcastCommand(msg);
      				}
      				
      				//new architecture
      				//simulate key
      				else if(msg.startsWith("key#keycode"))
      				{
      					String tokens[] = msg.split("\\#");
      					String wrap[] = tokens[1].split("\\:");
      					String keycode = wrap[1];
      					
      					if(keycode.equals("Q"))
      					{
      						simulateKey(KeyEvent.KEYCODE_Q);
      					}
      					else if(keycode.equals("E"))
      					{
      						simulateKey(KeyEvent.KEYCODE_E);
      					}
      					else if(keycode.equals("LEFT"))
      					{
      						simulateKey(KeyEvent.KEYCODE_DPAD_LEFT);
      					}
      					else if(keycode.equals("ENTER"))
      					{
      						simulateKey(KeyEvent.KEYCODE_ENTER);
      					}
      				}
  /*    				else if(!MainActivity.bDeviceSet || !MainActivity.bTaskSet)
      				{
      						if( msg.startsWith("key#0:bendsensortopup"))
      						{
      							simulateKey(KeyEvent.KEYCODE_DPAD_UP);
      						}
      						else if(msg.startsWith("key#0:bendsensortopdown"))
      						{
      							simulateKey(KeyEvent.KEYCODE_DPAD_DOWN);
      						}
      						else if(msg.startsWith("key#0:bendsensorleftdown"))
      						{
      							simulateKey(KeyEvent.KEYCODE_DPAD_CENTER);
      						}
      						else {
      	    					broadcastCommand(msg);
      	        				MainActivity.clientCommandChanged[1] = true;
      							MainActivity.clientCommand[1] = msg+"\n";
      	  						MainActivity.clientCommandChanged[2] = true;
      							MainActivity.clientCommand[2] = msg + "\n";
  							}
      				}*/
      				else if(msg.startsWith("collocate#0:1") && MainActivity.taskType == MainActivity.TaskType.Task1Document)
      				{
      					Task1Service.bCollocate = true;
      					
      					if(Task1Service.selectedPageId1 == Task1Service.Book1Page.length - 1)
      						msg += ":"+Task1Service.selectedBook+":"+Task1Service.selectedPage;
      					else 
      						msg += ":"+Task1Service.selectedBook+":"+(Task1Service.selectedPage+1);
      					
      					broadcastCommand(msg);
          				MainActivity.clientCommandChanged[1] = true;
  						MainActivity.clientCommand[1] = msg+"\n";
      					
      				}
      				else if( msg.startsWith("key#1:bendsensortopdown")  && Task1Service.bCollocate == true) //collocate next page
      				{
      					
      					if(Task1Service.selectedPageId1 < Task1Service.Book1Page.length - 1)
      					{
      						String slaveMsg = "doc#";
      						slaveMsg = slaveMsg + (Task1Service.selectedPageId1 + 1);
      						
      						broadcastCommand(msg);
              				MainActivity.clientCommandChanged[1] = true;
      						MainActivity.clientCommand[1] = slaveMsg+"\n";
      					}
      						
      					
      				}
      				else if( msg.startsWith("key#1:bendsensortopup") && Task1Service.bCollocate == true )
      				{
      					if(Task1Service.selectedPageId1 > 1)
      					{
      						String slaveMsg = "doc#";
      						slaveMsg = slaveMsg + (Task1Service.selectedPageId1 - 2);
      						
      						broadcastCommand(msg);
              				MainActivity.clientCommandChanged[1] = true;
      						MainActivity.clientCommand[1] = slaveMsg+"\n";
      					}
      				}
    				else if(msg.startsWith("collocate#0:1"))
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.endsWith("bendsensortopup") && Task4Service.bCollocate == true) //map zoom, dont directly send to slave
      				{
      					broadcastCommand(msg);  //braodcast to main map, and map will calculate new slave position and send it using keyservice
      				}
      				else if(msg.endsWith("bendsensortopdown") && Task4Service.bCollocate == true)  //map zoom, dont directly send to slave
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.endsWith("Inc") || msg.endsWith("Dec"))
      				{
      					broadcastCommand(msg);
      				}
      				else //collocate, key, zone, taskChooser
      				{
          				//String[] tokens = msg.split("\\#");
              			
//          				if(tokens[0].equals("zone"))
//          				{
//          					if(tokens[1].startsWith("0"))
//          						broadcastCommand(msg);
//          					else if(tokens[1].startsWith("1"))
//          					{
//          						MainActivity.clientCommandChanged[1] = true;
//          						MainActivity.clientCommand[1] = msg;
//          					}
//          					else if(tokens[2].startsWith("2"))
//          					{
//          						MainActivity.clientCommandChanged[2] = true;
//          						MainActivity.clientCommand[2] = msg;
//          					}
//          				}
      					broadcastCommand(msg);
          				MainActivity.clientCommandChanged[1] = true;
  						MainActivity.clientCommand[1] = msg+"\n";
    						MainActivity.clientCommandChanged[2] = true;
  						MainActivity.clientCommand[2] = msg + "\n";
  						
  						
      				}
      				sleep(50);
      			}
      		}
      		catch(Exception e)
      		{
      			String error = e.toString();
      			String msg = e.getMessage();
      			Log.d(id, error);
      		}
      		
      		
      	}
      	};
    
      	
  //Thread used to receive notif from PC
  	private Runnable KeyboardStuff = new Thread() {
      	@Override
  		public void run() {
      		
      		try
      		{	
      			//Connect to PC
      			Socket socket = new Socket(HostIP, HostKeyboardIPPort);
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
      					broadcastCommand("return");
      				}
      				else if(msg.equals("left"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_LEFT);
      				}
      				else if(msg.equals("right"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_RIGHT);
      				}
      				else if(msg.equals("up"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_UP);
      				}
      				else if(msg.equals("down"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_DOWN);
      				}
      				else if(msg.equals("enter"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_CENTER);
      				}
      				else if(msg.equals("esc"))
      				{
      					if(MainActivity.activeActivity != null)
      					{
      						Intent intentMainApp = new Intent();
      						intentMainApp.setClass(MainActivity.activeActivity, MainAppActivity.class);
      						intentMainApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      						startActivity(intentMainApp);
      						
      						MainActivity.activeActivity.finish();			
      					}
      				}
      				else if(msg.equals("i"))
      				{
      					simulateKey(KeyEvent.KEYCODE_I);
      				}
      				//msg is a command or status notif
      				else if(msg.equals("show"))
      				{
      					broadcastCommand(msg);
      					
//      					//notify other devices
//      					command = "show";
//      					bCommandChanged = true;
      				}
      				else if(msg.equals("hide"))
      				{
      					broadcastCommand(msg);
      					
//      					//notify other devices
//      					command = "hide";
//      					bCommandChanged = true;
      				}
      				else if(msg.startsWith("map"))
      				{
      					broadcastCommand(msg);
//      					command = msg;
//      					bCommandChanged = true;
      				}
      				else if(msg.equals("hot"))
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.equals("warm"))
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.equals("cold"))
      				{
      					broadcastCommand(msg);
      				}
      				
      				//new architecture
      				//simulate key
      				else if(msg.startsWith("key#keycode"))
      				{
      					String tokens[] = msg.split("\\#");
      					String wrap[] = tokens[1].split("\\:");
      					String keycode = wrap[1];
      					
      					if(keycode.equals("Q"))
      					{
      						simulateKey(KeyEvent.KEYCODE_Q);
      					}
      					else if(keycode.equals("E"))
      					{
      						simulateKey(KeyEvent.KEYCODE_E);
      					}
      					else if(keycode.equals("LEFT"))
      					{
      						simulateKey(KeyEvent.KEYCODE_DPAD_LEFT);
      					}
      					else if(keycode.equals("ENTER"))
      					{
      						simulateKey(KeyEvent.KEYCODE_ENTER);
      					}
      				}
  /*    				else if(!MainActivity.bDeviceSet || !MainActivity.bTaskSet)
      				{
      						if( msg.startsWith("key#0:bendsensortopup"))
      						{
      							simulateKey(KeyEvent.KEYCODE_DPAD_UP);
      						}
      						else if(msg.startsWith("key#0:bendsensortopdown"))
      						{
      							simulateKey(KeyEvent.KEYCODE_DPAD_DOWN);
      						}
      						else if(msg.startsWith("key#0:bendsensorleftdown"))
      						{
      							simulateKey(KeyEvent.KEYCODE_DPAD_CENTER);
      						}
      						else {
      	    					broadcastCommand(msg);
      	        				MainActivity.clientCommandChanged[1] = true;
      							MainActivity.clientCommand[1] = msg+"\n";
      	  						MainActivity.clientCommandChanged[2] = true;
      							MainActivity.clientCommand[2] = msg + "\n";
  							}
      				}*/
      				else if(msg.startsWith("collocate#0:1") && MainActivity.taskType == MainActivity.TaskType.Task1Document)
      				{
      					Task1Service.bCollocate = true;
      					
      					if(Task1Service.selectedPage == Task1Service.Book1Page.length - 1)
      						msg += ":"+Task1Service.selectedBook+":"+Task1Service.selectedPage;
      					else 
      						msg += ":"+Task1Service.selectedBook+":"+(Task1Service.selectedPage+1);
      					
      					broadcastCommand(msg);
          				MainActivity.clientCommandChanged[1] = true;
  						MainActivity.clientCommand[1] = msg+"\n";
      					
      				}
      				else if( msg.startsWith("key#1:bendsensortopdown")  && Task1Service.bCollocate == true) //collocate next page
      				{
      					
      					if(Task1Service.selectedPage < Task1Service.Book1Page.length - 3)
      					{
      						String slaveMsg = "doc#";
      						slaveMsg = slaveMsg + (Task1Service.selectedPage + 3);
      						
      						broadcastCommand(msg);
              				MainActivity.clientCommandChanged[1] = true;
      						MainActivity.clientCommand[1] = slaveMsg+"\n";
      					}
      						
      					
      				}
      				else if( msg.startsWith("key#1:bendsensortopup") && Task1Service.bCollocate == true )
      				{
      					if(Task1Service.selectedPage > 1)
      					{
      						String slaveMsg = "doc#";
      						slaveMsg = slaveMsg + (Task1Service.selectedPage - 1);
      						
      						broadcastCommand(msg);
              				MainActivity.clientCommandChanged[1] = true;
      						MainActivity.clientCommand[1] = slaveMsg+"\n";
      					}
      				}
    				else if(msg.startsWith("collocate#0:1"))
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.endsWith("bendsensortopup") && Task4Service.bCollocate == true) //map zoom, dont directly send to slave
      				{
      					broadcastCommand(msg);  //braodcast to main map, and map will calculate new slave position and send it using keyservice
      				}
      				else if(msg.endsWith("bendsensortopdown") && Task4Service.bCollocate == true)  //map zoom, dont directly send to slave
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.endsWith("Inc") || msg.endsWith("Dec"))
      				{
      					broadcastCommand(msg);
      				}
      				else //collocate, key, zone, taskChooser
      				{
          				//String[] tokens = msg.split("\\#");
              			
//          				if(tokens[0].equals("zone"))
//          				{
//          					if(tokens[1].startsWith("0"))
//          						broadcastCommand(msg);
//          					else if(tokens[1].startsWith("1"))
//          					{
//          						MainActivity.clientCommandChanged[1] = true;
//          						MainActivity.clientCommand[1] = msg;
//          					}
//          					else if(tokens[2].startsWith("2"))
//          					{
//          						MainActivity.clientCommandChanged[2] = true;
//          						MainActivity.clientCommand[2] = msg;
//          					}
//          				}
      					broadcastCommand(msg);
          				MainActivity.clientCommandChanged[1] = true;
  						MainActivity.clientCommand[1] = msg+"\n";
    						MainActivity.clientCommandChanged[2] = true;
  						MainActivity.clientCommand[2] = msg + "\n";
  						
  						
      				}
      				sleep(50);
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
    
    //Thread used to receive notif from PC
	private Runnable DataStuff = new Thread() {
      	@Override
  		public void run() {
      		
      		try
      		{	
      			//Connect to PC
      			Socket socket = new Socket(HostIP, HostIPPort);
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
      					broadcastCommand("return");
      				}
      				else if(msg.equals("left"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_LEFT);
      				}
      				else if(msg.equals("right"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_RIGHT);
      				}
      				else if(msg.equals("up"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_UP);
      				}
      				else if(msg.equals("down"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_DOWN);
      				}
      				else if(msg.equals("enter"))
      				{
      					simulateKey(KeyEvent.KEYCODE_DPAD_CENTER);
      				}
      				else if(msg.equals("esc"))
      				{
      					if(MainActivity.activeActivity != null)
      					{
      						Intent intentMainApp = new Intent();
      						intentMainApp.setClass(MainActivity.activeActivity, MainAppActivity.class);
      						intentMainApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      						startActivity(intentMainApp);
      						
      						MainActivity.activeActivity.finish();			
      					}
      				}
      				else if(msg.equals("i"))
      				{
      					simulateKey(KeyEvent.KEYCODE_I);
      				}
      				//msg is a command or status notif
      				else if(msg.equals("show"))
      				{
      					broadcastCommand(msg);
      					
//      					//notify other devices
//      					command = "show";
//      					bCommandChanged = true;
      				}
      				else if(msg.equals("hide"))
      				{
      					broadcastCommand(msg);
      					
//      					//notify other devices
//      					command = "hide";
//      					bCommandChanged = true;
      				}
      				else if(msg.startsWith("map"))
      				{
      					broadcastCommand(msg);
//      					command = msg;
//      					bCommandChanged = true;
      				}
      				else if(msg.equals("hot"))
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.equals("warm"))
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.equals("cold"))
      				{
      					broadcastCommand(msg);
      				}
      				
      				//new architecture
      				//simulate key
      				else if(msg.startsWith("key#keycode"))
      				{
      					String tokens[] = msg.split("\\#");
      					String wrap[] = tokens[1].split("\\:");
      					String keycode = wrap[1];
      					
      					if(keycode.equals("Q"))
      					{
      						simulateKey(KeyEvent.KEYCODE_Q);
      					}
      					else if(keycode.equals("E"))
      					{
      						simulateKey(KeyEvent.KEYCODE_E);
      					}
      					else if(keycode.equals("LEFT"))
      					{
      						simulateKey(KeyEvent.KEYCODE_DPAD_LEFT);
      					}
      					else if(keycode.equals("ENTER"))
      					{
      						simulateKey(KeyEvent.KEYCODE_ENTER);
      					}
      				}
  /*    				else if(!MainActivity.bDeviceSet || !MainActivity.bTaskSet)
      				{
      						if( msg.startsWith("key#0:bendsensortopup"))
      						{
      							simulateKey(KeyEvent.KEYCODE_DPAD_UP);
      						}
      						else if(msg.startsWith("key#0:bendsensortopdown"))
      						{
      							simulateKey(KeyEvent.KEYCODE_DPAD_DOWN);
      						}
      						else if(msg.startsWith("key#0:bendsensorleftdown"))
      						{
      							simulateKey(KeyEvent.KEYCODE_DPAD_CENTER);
      						}
      						else {
      	    					broadcastCommand(msg);
      	        				MainActivity.clientCommandChanged[1] = true;
      							MainActivity.clientCommand[1] = msg+"\n";
      	  						MainActivity.clientCommandChanged[2] = true;
      							MainActivity.clientCommand[2] = msg + "\n";
  							}
      				}*/
      				else if(msg.startsWith("collocate#0:1") && MainActivity.taskType == MainActivity.TaskType.Task1Document)
      				{
      					Task1Service.bCollocate = true;
      					
      					if(Task1Service.selectedPageId1 == Task1Service.Book1Page.length - 1)
      						msg += ":"+Task1Service.selectedBook+":"+Task1Service.selectedPage;
      					else 
      						msg += ":"+Task1Service.selectedBook+":"+(Task1Service.selectedPage+1);
      					
      					broadcastCommand(msg);
          				MainActivity.clientCommandChanged[1] = true;
  						MainActivity.clientCommand[1] = msg+"\n";
      					
      				}
      				else if( msg.startsWith("key#1:bendsensortopdown")  && Task1Service.bCollocate == true) //collocate next page
      				{
      					
      					if(Task1Service.selectedPageId1 < Task1Service.Book1Page.length - 1)
      					{
      						String slaveMsg = "doc#";
      						slaveMsg = slaveMsg + (Task1Service.selectedPageId1 + 1);
      						
      						broadcastCommand(msg);
              				MainActivity.clientCommandChanged[1] = true;
      						MainActivity.clientCommand[1] = slaveMsg+"\n";
      					}
      						
      					
      				}
      				else if( msg.startsWith("key#1:bendsensortopup") && Task1Service.bCollocate == true )
      				{
      					if(Task1Service.selectedPageId1 > 1)
      					{
      						String slaveMsg = "doc#";
      						slaveMsg = slaveMsg + (Task1Service.selectedPageId1 - 2);
      						
      						broadcastCommand(msg);
              				MainActivity.clientCommandChanged[1] = true;
      						MainActivity.clientCommand[1] = slaveMsg+"\n";
      					}
      				}
    				else if(msg.startsWith("collocate#0:1"))
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.endsWith("bendsensortopup") && Task4Service.bCollocate == true) //map zoom, dont directly send to slave
      				{
      					broadcastCommand(msg);  //braodcast to main map, and map will calculate new slave position and send it using keyservice
      				}
      				else if(msg.endsWith("bendsensortopdown") && Task4Service.bCollocate == true)  //map zoom, dont directly send to slave
      				{
      					broadcastCommand(msg);
      				}
      				else if(msg.endsWith("Inc") || msg.endsWith("Dec"))
      				{
      					broadcastCommand(msg);
      				}
      				else //collocate, key, zone, taskChooser
      				{
          				//String[] tokens = msg.split("\\#");
              			
//          				if(tokens[0].equals("zone"))
//          				{
//          					if(tokens[1].startsWith("0"))
//          						broadcastCommand(msg);
//          					else if(tokens[1].startsWith("1"))
//          					{
//          						MainActivity.clientCommandChanged[1] = true;
//          						MainActivity.clientCommand[1] = msg;
//          					}
//          					else if(tokens[2].startsWith("2"))
//          					{
//          						MainActivity.clientCommandChanged[2] = true;
//          						MainActivity.clientCommand[2] = msg;
//          					}
//          				}
      					broadcastCommand(msg);
          				MainActivity.clientCommandChanged[1] = true;
  						MainActivity.clientCommand[1] = msg+"\n";
    						MainActivity.clientCommandChanged[2] = true;
  						MainActivity.clientCommand[2] = msg + "\n";
  						
  						
      				}
      				sleep(50);
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
        
        
    	//Thread used to broadcast command from every activity to slave display
    	private Runnable CommandToClientStuff = new Thread() {
        	@Override
			public void run() {
        		
        		try
        		{	
        			int iClientCounter = 1;
        			ServerSocket serverSocket = new ServerSocket(MainActivity.MasterDisplayPort);
        			while(iClientCounter < MainActivity.clientNum )
        			{
        				MainActivity.clientSocket[iClientCounter] = serverSocket.accept();
        				MainActivity.os[iClientCounter] = MainActivity.clientSocket[iClientCounter].getOutputStream();
        				
        				iClientCounter++;
        			}
        			
        			while(true)
        			{
        				for(int i = 1; i < MainActivity.clientNum; ++i)
        				{
        					if(MainActivity.clientCommandChanged[i] == true)
        					{
        						MainActivity.os[i].write((MainActivity.clientCommand[i]).getBytes("utf-8"));
        						MainActivity.clientCommand[i] = "";
        						MainActivity.clientCommandChanged[i] = false;
        					}
        				}
        				sleep(20);
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
