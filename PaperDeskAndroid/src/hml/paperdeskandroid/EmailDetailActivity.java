package hml.paperdeskandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class EmailDetailActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_email_detail);
        
        Button btnShow = (Button)findViewById(R.id.buttonShow);
        btnShow.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageView img2 = (ImageView)findViewById(R.id.imageView2);
				if(img2.getVisibility() == View.INVISIBLE)
					img2.setVisibility(View.VISIBLE);
				else
					img2.setVisibility(View.INVISIBLE);
				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_email_detail, menu);
        return true;
    }
}
