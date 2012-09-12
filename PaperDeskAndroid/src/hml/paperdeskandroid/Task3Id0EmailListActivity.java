package hml.paperdeskandroid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;

public class Task3Id0EmailListActivity extends Activity {

	static String command = "";
	
	Handler myHandler;
	
	MyReceiver receiver;
	public class MyReceiver extends BroadcastReceiver
	{
		public MyReceiver()
		{
			
		}
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			String command = bundle.getString("command");
			if(command.equals("key#0:NewEmail"))
			{
				Task3Service.bNewEmailComing = true;
				//change the email list picture to show new added email
			}
			else if(command.startsWith("zone#0:hot"))
			{
				Task3Service.iCurrentZone = Task3Service.Zone.Hot;
				
				if(Task3Service.bNewEmailComing)
				{
					//start new email detail activity
					
				}
				else 
				{
					//start email detail activity
					Intent intentEmailDetail = new Intent();
					intentEmailDetail.setClass(Task3Id0EmailListActivity.this, Task3Id0EmailDetailActivity.class);
					
					startActivity(intentEmailDetail);
					Task3Id0EmailListActivity.this.finish();
				}
				
			}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3_id0_email_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task3_id0_email_list, menu);
        return true;
    }
}
