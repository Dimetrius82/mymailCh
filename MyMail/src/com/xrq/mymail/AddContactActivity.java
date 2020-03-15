package com.xrq.mymail;

import com.xrq.mydb.MyDatabaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddContactActivity extends Activity {

	Context mContext;
	EditText name;
	EditText email;
	Button add;
	TableLayout contact_layout;
	TextView tv;
	private MyDatabaseHelper dbhelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		setContentView(R.layout.add_contact);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		findView();
		init();
	
	}
	
	private void findView(){
		mContext=this;
		name=(EditText)this.findViewById(R.id.contact_name);
		email=(EditText)this.findViewById(R.id.contact_email);
		contact_layout=(TableLayout)this.findViewById(R.id.contact_layout);
		tv=(TextView)this.findViewById(R.id.title);
		add=(Button)this.findViewById(R.id.addNewContact);
		add.setOnClickListener(l);
	}
	private void init(){//设置动画效果
		Animation anim=AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		contact_layout.startAnimation(anim);//设置动画效果。
		//设置标题栏
		tv.setText("添加联系人");
	}
	private OnClickListener l=new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.addNewContact:
				//先判断，然后再添加到数据表里面去
				String username=name.getText().toString().trim();
				String useremail=email.getText().toString().trim();
				//合法就加到数据库的contact数据表里面去
				if(useremail.contains("@")&&useremail.contains(".")&&useremail.contains("com")){
					dbhelper=new MyDatabaseHelper(mContext,"mail.db",null,1);
					SQLiteDatabase db=dbhelper.getWritableDatabase();
					//组装联系人数据
					ContentValues values=new ContentValues();
					values.put("name", username);
					values.put("email", useremail);
					db.insert("contact", null, values);
					values.clear();
					//清空，并且回到主界面
					name.setText("");
					email.setText("");
					Intent i=new Intent(mContext,MainActivity.class);
					startActivity(i);
					Toast.makeText(mContext, "成功加入联系人数据库",Toast.LENGTH_SHORT).show();
					
				}
				else{
					Toast.makeText(mContext, "输入的email地址不合法",Toast.LENGTH_SHORT).show();
				}
				break;
			}
			
		}
		
	};
	
}
