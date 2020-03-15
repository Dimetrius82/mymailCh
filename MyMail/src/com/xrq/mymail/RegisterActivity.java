package com.xrq.mymail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class RegisterActivity extends Activity {

	private WebView webview;
	private ProgressBar pb;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		this.setContentView(R.layout.register);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
	    this.findView();
	    
	}
	private void findView(){
		pb=(ProgressBar)this.findViewById(R.id.pb);
		if(pb.getVisibility()==View.GONE){
	    pb.setVisibility(View.VISIBLE);
		}
		pd=new ProgressDialog(this);
		pd.setTitle("加载网页。。");
		pd.setCancelable(true);
		pd.setProgressStyle(TRIM_MEMORY_RUNNING_CRITICAL);
		pd.setMessage("正在加载....");
		pd.show();
		webview=(WebView)this.findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,String url){
				view.loadUrl(url);
				return true;
			}
		});
		webview.loadUrl("http://mail.163.com/");
		//加载完毕
		  pb.setVisibility(View.GONE);
		  pd.dismiss();
		  
	}
	
}
