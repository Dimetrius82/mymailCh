package com.xrq.mymail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.xrq.adapter.GetMailAdapter;
import com.xrq.mydb.MyDatabaseHelper;
import com.xrq.util.GetPwAndAcc;
import com.xrq.util.MailItemUtil;
import com.xrq.util.MyGetMail;
import com.xrq.util.MyThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity {

	String name="";
	String mima="";
	private Context mContext;
	private String subject,from,to,date,attach;
	private ProgressDialog pd,pdw;
	private String id;
	private TextView ds,df,dt,dd,da;
	private WebView dc;
	public  Message message[] = null;
	private String fileName="";
	private ImageView file_image;
	private TextView file_name,file_size;
	private LinearLayout al;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
		case 1:
	            Data c=(Data)msg.obj;
	 		    dc.loadDataWithBaseURL(null, c.getC().trim().toString(), "text/html", "utf-8", null);  
	 	        dc.getSettings().setJavaScriptEnabled(true);  
	 	        dc.setWebChromeClient(new WebChromeClient());  
			    //Toast.makeText(mContext, c.getFn(), Toast.LENGTH_SHORT).show();
			    //根据FileName来加载图标
			    if(c.getFn()==null||c.getFn().equalsIgnoreCase("NULL"))
			    	;
			    else {
			    	Toast.makeText(mContext, c.getFn(), Toast.LENGTH_SHORT).show();
			    	file_name.setText(c.getFn());
			    }
			    String fn=file_name.getText().toString();
			    file_image.setImageResource(getImageId(fn));
	            pd.dismiss();
			    Toast.makeText(mContext, "加载完成", Toast.LENGTH_SHORT).show();
				break;
		case 2:
			   Toast.makeText(mContext, "无法链接到服务器，请检查网络连接", Toast.LENGTH_SHORT).show();
			   break;
		case 3:
			   String p=(String)msg.obj;
			   Toast.makeText(mContext,"保存在："+ p, Toast.LENGTH_LONG).show();	
               break;
		case 5:
			String sign=(String)msg.obj;
			pdw.dismiss();
				   Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
			break;
		case 6:
			pdw.dismiss();
				   Toast.makeText(mContext, "标记为已读", Toast.LENGTH_SHORT).show();
			break;
		case 7:	
			pdw.dismiss();
				   Toast.makeText(mContext, "标记为未读", Toast.LENGTH_SHORT).show();
			break;
		case 8:	
			pdw.dismiss();
				   Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
			break;
		case 9:	
			   Toast.makeText(mContext, "点击了下载按钮", Toast.LENGTH_SHORT).show();
		break;
				
			}
		}
	};//handler变量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		this.setContentView(R.layout.mail_detail);
		 mContext=this;
		 //从数据库获取密码等
		 name=(new GetPwAndAcc(mContext)).getAcc();
		 mima=(new GetPwAndAcc(mContext)).getPw();
		 Toast.makeText(mContext, name, Toast.LENGTH_LONG).show();
		 pd=new ProgressDialog(mContext);
         pd.setTitle("加载邮件内容");
         pd.setMessage("正在加载....");
		 pd.setCancelable(true);
		 pd.show();
		 //获得详细的缓存数据
		 Intent msg=this.getIntent();
		 subject=msg.getStringExtra("subject");
		 from=msg.getStringExtra("from");
		 to=msg.getStringExtra("to");
		 date=msg.getStringExtra("date");
		 attach=msg.getStringExtra("attach");
		 id=msg.getStringExtra("id");
		 //设置各个控件的内容然后销毁对话框
		 findView();
		 init();
		 getMailDetail();	
		 //把附件按照附件的名字得到附件应有的图标。然后加载进来等等
		// showAttach();
	}
	
	public void showAttach(String fn){
		
		file_name.setText(fn);
		file_size.setText("4.25M");
		file_image.setImageResource(getImageId(fn));
	}
	
	
	private void init(){
		ds.setText(subject);
		df.setText("发件人："+from);
		dt.setText("收件人："+to);
		dd.setText("发送时间："+date);
		da.setText(attach+":");
		
	}
	private void findView(){
		ds=(TextView)this.findViewById(R.id.d_subject);
		df=(TextView)this.findViewById(R.id.d_from);
		dt=(TextView)this.findViewById(R.id.d_to);
		dd=(TextView)this.findViewById(R.id.d_date);
		da=(TextView)this.findViewById(R.id.d_attach);
		dc=(WebView)this.findViewById(R.id.d_content);
		//dc.setMovementMethod(ScrollingMovementMethod.getInstance()) ; 
		file_name=(TextView)this.findViewById(R.id.file_name);
		file_size=(TextView)this.findViewById(R.id.file_size);
		file_image=(ImageView)this.findViewById(R.id.file_image);
		al=(LinearLayout)this.findViewById(R.id.my_file_item);
		al.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				// TODO 自动生成的方法存根
				switch(v.getId()){
				case R.id.my_file_item:
					AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
					dialog.setTitle("操作窗口");
					dialog.setMessage("确认对附件的操作");
					dialog.setCancelable(true);
					dialog.setPositiveButton("下载", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//下载文件到文件夹
							Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
							  new Thread(){
								  public void run(){
									  //链接到服务器，并且获取数据		  
									  Properties props = System.getProperties();  
								        props=System.getProperties();
								         props.put("mail.store.protocol", "imap");
								         props.put("mail.imap.auth", "true"); //这样才能通过验证
								         props.put("mail.imap.host", "imap.qq.com");	    
								         props.put("mail.imap.port", "993");
								         props.put("mail.imap.ssl.enable", "true");
								         props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
							        	 props.put("mail.imap.socketFactory.fallback", "false");
								         Session session=Session.getInstance(props, null);   
								        Store store;
								        Folder folder;
								        Message message[] = null;
										try {
										
										    store=session.getStore("imap");
										    System.out.println("1111222221 ");
											store.connect(name,mima);
											 System.out.println("111333333 ");
											folder=store.getFolder("INBOX");
											folder.open(Folder.HOLDS_MESSAGES);  
									        message= folder.getMessages();  
									        message = folder.getMessages();
										    //store.isConnected()可用于判断是否
										} catch (NoSuchProviderException e) {
											// TODO 自动生成的 catch 块
											e.printStackTrace();
										} catch (MessagingException e) {
											// TODO 自动生成的 catch 块
											e.printStackTrace();
										}  
								        MyGetMail pmm = null; 
								        MailItemUtil item;
								        String path="";
								        pmm = new MyGetMail((MimeMessage) message[Integer.parseInt(id)]);  
								        try {
										 pmm.saveAttachMent((Part) message[Integer.parseInt(id)]);
										
										} catch (NumberFormatException e) {
											// TODO 自动生成的 catch 块
											e.printStackTrace();
										} catch (Exception e) {
											// TODO 自动生成的 catch 块
											e.printStackTrace();
										}  
								        path=pmm.getPath();
								        android.os.Message m=new android.os.Message();
								        m.obj=path;
								        m.what=3;					
										handler.sendMessage(m);		
								  }
							  }.start();
						}
					});
					//取消
					dialog.setNegativeButton("查看", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String filename=file_name.getText().toString();
							//查看文本,全屏查看网络中的文本
							if(filename.contains("doc")||filename.contains("docx")||filename.contains("txt")||filename.contains("pdf")){
								Intent ii=new Intent(mContext,NetTxtActivity.class);
								ii.putExtra("id",id+"");
								
								startActivity(ii);
							}
							else{
							//查看该图片,全屏查看网络中的图片
							Intent ii=new Intent(mContext,NetImageActivity.class);
							ii.putExtra("id",id+"");
							startActivity(ii);
							}
							
						}
					});
					dialog.show();
				
				}
				return false;
			}
			
		});
	}
	//为该文件下的附件得到一个图标
	public int getImageId(String name){
		if(name.contains("jpg")||name.contains("jpeg")||name.contains("bmp")||name.contains("png")){
			return R.drawable.file_image;
		}
		else if(name.contains("doc")||name.contains("pdf")||name.contains("xls")||
				name.contains("docx")||name.contains("ppt")||name.contains("pptx")
				||name.contains("txt")||name.contains("wps")){
			return R.drawable.file_file;
		}
		else
			return R.drawable.file_default;
	}
	//在子线程中获得该邮件的具体内容
	private void getMailDetail(){

		   Thread t=new Thread(){
			   public void run(){
				 //链接到服务器，并且获取数据		  
				   Properties props = System.getProperties();  
			        props=System.getProperties();
			         props.put("mail.store.protocol", "imap");
			         props.put("mail.imap.auth", "true"); //这样才能通过验证
			         props.put("mail.imap.host", "imap.qq.com");	    
			         props.put("mail.imap.port", "993");
			         props.put("mail.imap.ssl.enable", "true");
			         props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		        	 props.put("mail.imap.socketFactory.fallback", "false");
			         Session session=Session.getInstance(props, null);   
			        Store store;
			        Folder folder;
			        Message message[] = null;
					try {
					
					    store=session.getStore("imap");
					    System.out.println("1111222221 ");
						store.connect(name,mima);
						 System.out.println("111333333 ");
						folder=store.getFolder("INBOX");
						folder.open(Folder.HOLDS_MESSAGES);  
				        message= folder.getMessages();  
				        message = folder.getMessages();
					    //store.isConnected()可用于判断是否
					} catch (NoSuchProviderException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (MessagingException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}  
			        MyGetMail pmm = null;
			        if(message==null){
			        	android.os.Message m=new android.os.Message();				   
					    m.what=1;					
					    handler.sendMessage(m);		
			        }
			        else{
			            pmm = new MyGetMail((MimeMessage) message[Integer.parseInt(id)]);  
			            try {       
					        pmm.getMailContent((Part) message[Integer.parseInt(id)]);
					        String content=pmm.getBodyText();
					        String fileName=pmm.getTrueFileName((Part) message[Integer.parseInt(id)]);
					        android.os.Message m=new android.os.Message();
					        m.obj=new Data(content,fileName);
					        m.what=1;					
						    handler.sendMessage(m);			        
			            } catch (Exception e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}  
			        }//else
			          
			   }
		   };
		   t.start();//别忘了！！！
		
	}
	@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	         MenuInflater in = getMenuInflater();
	         in.inflate(R.menu.detail_menu, menu);
	        return true;
	     }

	     @Override
	     public boolean onOptionsItemSelected(MenuItem item) {
	    	 pdw=new ProgressDialog(mContext);
	         pdw.setTitle("正在执行指令");
	         pdw.setMessage("请稍后....");
			 pdw.setCancelable(true);
			
	         switch (item.getItemId()) {
	         
	         case R.id.yidu:
	        	MyThread t= new MyThread("hasRead",Integer.parseInt(id),handler,mContext);
	        	 pdw.show();
	        	t.start();
	 
	             break;
	        case R.id.weidu:
	        	MyThread t1= new MyThread("hasNotRead",Integer.parseInt(id),handler,mContext);
	        	 pdw.show();
	        	t1.start();

	             break;
	  
	        case R.id.shanchu:
	        	MyThread t2= new MyThread("delete",Integer.parseInt(id),handler,mContext);
	        	 pdw.show();
	        	t2.start();
	             break;
	        case R.id.xieyoujianba:
	        	Intent i=new Intent(mContext,SendMailActivity.class);
	        	i.putExtra("email", from.substring(from.indexOf("<")+1,from.length()-1));
				startActivity(i);
	             break;
	         default:
	             return super.onOptionsItemSelected(item);
	         }
	
	        return true;
	    }
}

class Data{
	
	String c,fn;
	public Data(String c,String f){
		this.c=c;
		fn=f;
	}
public String getC(){
	return c;
}

public String getFn(){
	return fn;
}
	
}