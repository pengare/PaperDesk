package hml.paperdeskandroid;

import hml.paperdeskandroid.DocChapterViewActivity.MyReceiver;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EmailFolderViewActivity extends ListActivity {
	
	MyReceiver receiver;
	public class MyReceiver extends BroadcastReceiver
	{
		public MyReceiver()
		{
			
		}
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
//			Bundle bundle = intent.getExtras();
//			String command = bundle.getString("command");
//			if(command.equals("cold"))
//			{
//				BookService.iCurrentZone = 2; //cold zone
//				//start the book activity
//				Intent intentBook = new Intent();
//				intentBook.setClass(DocChapterViewActivity.this, DocBookViewActivity.class);
//				
//				startActivity(intentBook);
//				DocChapterViewActivity.this.finish();
//			}
//			else if(command.equals("warm"))
//			{
//				BookService.iCurrentZone = 1; //warm zone
//			}
//			else if(command.equals("hot"))
//			{
//				BookService.iCurrentZone = 0; //hot zone
//				if(BookService.iBookIndex != -1 && BookService.iChapterIndex != -1)
//				{
//					//start the page activity
//					Intent intentPage = new Intent();
//					intentPage.setClass(DocChapterViewActivity.this, DocPageViewActivity.class);
//					//BookService.iBookIndex = 1;
//					
//					startActivity(intentPage);
//					DocChapterViewActivity.this.finish();
//				}
			//}

		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
/*        //set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        */
        this.setTitle("Folders");
        
        MainActivity.activeActivity = this;
        
        //setContentView(R.layout.activity_email_folder_view);
        
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, EmailService.emailFolderList);
        setListAdapter(adapter);
        
        //registerBroadcastReceiver();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_email_folder_view, menu);
        return true;
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//String item = ChapterList[position].toString();
		if(position >= 0)
		{
			Intent intent = new Intent();
			intent.setClass(EmailFolderViewActivity.this, EmailListViewActivity.class);
			
			EmailService.iFolderIndex = position;
			
			startActivity(intent);
			EmailFolderViewActivity.this.finish();
		}
		
		//Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
		//super.onListItemClick(l, v, position, id);
	}
    
	public class MySimpleArrayAdapter extends ArrayAdapter<String>
	{
		private final Context context;
		private final String[] values;
		
		public MySimpleArrayAdapter(Context context, String[] values)
		{
			super(context, R.layout.doc_chapter_row_layout, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.doc_chapter_row_layout, parent, false);
			TextView textView = (TextView)rowView.findViewById(R.id.chapterLabel);
			ImageView imageView = (ImageView)rowView.findViewById(R.id.chapterIcon);
			
			imageView.setImageResource(R.drawable.chapter1);
			textView.setText(values[position]);
			
			return rowView;
			//return super.getView(position, convertView, parent);
		}
		
		
	}
}
