package com.xrq.mymail;


import java.io.File;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.MailSSLSocketFactory;
import com.xrq.Async.MyAsyncTest;
import com.xrq.util.GetPwAndAcc;
import com.xrq.util.MailAndPw;
import com.xrq.util.MySendMail;
import com.xrq.util.MySendMailFor163;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Files;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SendMailActivity extends Activity {

	private EditText shoujianren;
	private EditText fajianren;
	private EditText zhuti;
	private EditText neirong;
	private TextView attach_text;
	private Button fasong;
	private Button fujian;
	private Context mContext=this;
	private MailAndPw all;
	private String email;
	private TableLayout tl;
	private String name="",mima="";
	private ProgressDialog pd;
	public List<String> list=null;
	final static int HAS_SENT=1;
	final static int HAS_NOT_SENT=0;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
		case HAS_SENT:
			pd.dismiss();
	        Toast.makeText(mContext, "邮件成功发送了！", Toast.LENGTH_LONG).show();
				break;
		case HAS_NOT_SENT:
			pd.dismiss();
			Toast.makeText(mContext, "服务器或网络异常，邮件发送失败！", Toast.LENGTH_LONG).show();
			   break;
			}
		}
	};//handler变量
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		this.setContentView(R.layout.send_mail);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.send_title);
		mContext=this;
		//设置快捷信息，比如收件人或者发件人
		Intent i=this.getIntent();
		email=i.getStringExtra("email");
		findViewAndInit();
		//添加动画
		showAnim();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
	        Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
	        File file = new File(uri.toString().substring(5));
	        //把选择的文件加到List对象里面去
	        list.add(file.getAbsolutePath());
	        flushTheAttachPath();
	        Toast.makeText(mContext, "你选择了文件:"+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
	    }
	}
	
	public void flushTheAttachPath(){
		if(list!=null&&list.size()!=0){
			StringBuilder sb=new StringBuilder("");
			for(int i=0;i<list.size();i++){
				sb=sb.append(list.get(i)+"\n");
			}
			attach_text.setText(sb);
		}
		
	}
	public void showAnim(){
		Animation anim=AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		tl.startAnimation(anim);//设置动画效果。
	}
	public void findViewAndInit(){
		 tl=(TableLayout)this.findViewById(R.id.send_layout);
		 shoujianren =(EditText)this.findViewById(R.id.shoujianren);
		 fajianren =(EditText)this.findViewById(R.id.fajianren);
		 zhuti=(EditText)this.findViewById(R.id.zhuti);
		 neirong =(EditText)this.findViewById(R.id.neirong);
		 attach_text =(TextView)this.findViewById(R.id.attach_text);
		 fujian =(Button)this.findViewById(R.id.fujian);
		 fujian.setOnClickListener(btnListener);
		 fasong =(Button)this.findViewById(R.id.send);
		 fasong.setOnClickListener(btnListener);
		 //初始化密码和快捷数据
	  	 name=(new GetPwAndAcc(mContext)).getAcc();
		 mima=(new GetPwAndAcc(mContext)).getPw();
		 shoujianren.setText(email);
		 fajianren.setText(name);
		 list=new ArrayList<String>();
	}
	private OnClickListener btnListener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.fujian:
				//打开附件
				
	                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
	                intent.addCategory(Intent.CATEGORY_OPENABLE);
	                startActivityForResult(intent,1);
				 break;
			
			case R.id.send:
				//发送邮件
				String sjr=shoujianren.getText().toString();
				String fjr=fajianren.getText().toString();
				String zt=zhuti.getText().toString();
				String nr=neirong.getText().toString();
				boolean OK=sjr.contains(".")&&sjr.contains("@")&&sjr.contains("com");
				if(OK){//
					//发送邮件
				    pd=new ProgressDialog(mContext);
					pd.setTitle("发送邮件");
					pd.setMessage("正在发送...");
					pd.setCancelable(true);
					pd.show();
					//组装邮件内容并且开启线程去发送邮件
					//(new MySendMailFor163(zt,fjr,sjr,nr,list,handler)).start();
					(new MySendMail(zt,fjr,sjr,nr,mContext,list,handler)).start();
				  
				}
				else{
					Toast.makeText(SendMailActivity.this, "请输入正确的内容", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
		
	};
	
	class PopupAuthenticator extends Authenticator {
		public static final String mailuser = "1251931015";
		public static final String password = "cqhwthrdqytnjdif";

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(mailuser, password);
		}
	}
}
