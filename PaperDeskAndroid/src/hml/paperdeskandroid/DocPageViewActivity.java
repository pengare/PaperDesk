package hml.paperdeskandroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

public class DocPageViewActivity extends Activity {

	public WebView webView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_page_view);
        
        webView = (WebView)findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        
        webView.loadUrl("http://developer.android.com/training/basics/firstapp/creating-project.html");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_doc_page_view, menu);
        return true;
    }
}
