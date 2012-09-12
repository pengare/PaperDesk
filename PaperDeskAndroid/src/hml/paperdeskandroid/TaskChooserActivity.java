package hml.paperdeskandroid;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class TaskChooserActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_task_chooser);
        
        Button btnPreliminary = (Button)findViewById(R.id.btnPreliminary);
        btnPreliminary.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.taskType = MainActivity.TaskType.Preliminary;
				
				if(MainActivity.id == "0")
				{
					Intent intent = new Intent();
					intent.setClass(TaskChooserActivity.this, PreliminaryId0Activity.class);
					startActivity(intent);
				}
				else if(MainActivity.id == "1")
				{
					Intent intent = new Intent();
					intent.setClass(TaskChooserActivity.this, PreliminaryId1Activity.class);
					startActivity(intent);
				}
				else if (MainActivity.id == "2") {
					Intent intent = new Intent();
					intent.setClass(TaskChooserActivity.this, PreliminaryId2Activity.class);
					startActivity(intent);
				}
				
				TaskChooserActivity.this.finish();
			}
		});
        
        Button btnTrainingButton = (Button)findViewById(R.id.btnTraining);
        btnTrainingButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				MainActivity.taskType = MainActivity.TaskType.Training;
				
				if(MainActivity.id == "0") //primary
				{
					Intent intent = new Intent();
					intent.setClass(TaskChooserActivity.this, TrainingId0BlankActivity.class);
					startActivity(intent);
				}
				else if(MainActivity.id == "1")
				{
					Intent intent = new Intent();
					intent.setClass(TaskChooserActivity.this, TrainingId1BookViewActivity.class);
					startActivity(intent);
				}
				else if(MainActivity.id == "2")
				{
					Intent intent = new Intent();
					intent.setClass(TaskChooserActivity.this, TrainingId2AppActivity.class);
					startActivity(intent);
				}
				
				TaskChooserActivity.this.finish();
			}
		});
        
		Button btnTask1 = (Button) findViewById(R.id.btnTask1);
		btnTask1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				MainActivity.taskType = MainActivity.TaskType.Task1Document;
				if(MainActivity.id == "0")
				{
					
				}
				else if(MainActivity.id == "1")
				{
					Intent intent = new Intent();
					intent.setClass(TaskChooserActivity.this, Task1Id1BlankActivity.class);
					startActivity(intent);
				}
				else if(MainActivity.id == "2")
				{
					Intent intent = new Intent();
					intent.setClass(TaskChooserActivity.this, Task1Id2BookActivity.class);
					startActivity(intent);
				}
				

				TaskChooserActivity.this.finish();

			}
		});
		
		Button btnTask2 = (Button)findViewById(R.id.btnTask2);
		btnTask2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.taskType = MainActivity.TaskType.Task2Photo;
				Intent intent = new Intent();
				intent.setClass(TaskChooserActivity.this, MainActivity.class);
				startActivity(intent);
				
				TaskChooserActivity.this.finish();
			}
		});
		
		Button btnTask3 = (Button)findViewById(R.id.btnTask3);
		btnTask3.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.taskType = MainActivity.TaskType.Task3Email;
				Intent intent = new Intent();
				intent.setClass(TaskChooserActivity.this, MainActivity.class);
				startActivity(intent);
				
				TaskChooserActivity.this.finish();
				
			}
		});
		
		
		Button btnTask4 = (Button)findViewById(R.id.btnTask4);
		btnTask4.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.taskType = MainActivity.TaskType.Task4Map;
				Intent intent = new Intent();
				intent.setClass(TaskChooserActivity.this, MainActivity.class);
				startActivity(intent);
				
				TaskChooserActivity.this.finish();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task_chooser, menu);
        return true;
    }
}
