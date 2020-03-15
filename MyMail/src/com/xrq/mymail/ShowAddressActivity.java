package com.xrq.mymail;

import java.util.ArrayList;
import java.util.List;

import com.xrq.adapter.ContactAdapter;
import com.xrq.adapter.Contact_item;
import com.xrq.mydb.MyDatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAddressActivity extends Activity {
    private Context mContext;
    private ListView listView;
    private TextView title;
    private List<Contact_item> list=new ArrayList<Contact_item>();
    private MyDatabaseHelper dbhelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		setContentView(R.layout.show_address);	
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		mContext=this;
		findView();
		init();
		showData();
		//添加动画
		showAnim();
	}
	public void showAnim(){
		Animation anim=AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		listView.startAnimation(anim);//设置动画效果。
	}
	private void findView(){
		listView=(ListView)this.findViewById(R.id.address_list_view);
		listView.setOnItemLongClickListener(longclick);
		listView.setOnItemClickListener(click);
		title=(TextView)this.findViewById(R.id.title);
		title.setText("联系人列表");
	}
	
	private OnItemClickListener click =new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, final int position, long id) {
			// TODO 自动生成的方法存根
			Contact_item item =list.get(position);
			Intent i=new Intent(mContext,SendMailActivity.class);
			i.putExtra("email", item.getEmail());
			startActivity(i);		
		}

	
		
	};
	
	
	private OnItemLongClickListener longclick =new OnItemLongClickListener(){

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View arg1, final int position, long id) {
			final Contact_item item =list.get(position);
			//删除数据
			AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
			dialog.setTitle("确认窗口");
			dialog.setMessage("确认删除该联系人？");
			dialog.setCancelable(true);
			dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
					dbhelper=new MyDatabaseHelper(mContext,"mail.db",null,1);
					SQLiteDatabase db=dbhelper.getWritableDatabase();
					db.delete("contact", "name=?", new String[]{item.getUser()});
					//
				    list.remove(position);
				    showData();
				}
			});
			//取消
dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
					
				}
			});
			dialog.show();
			
			return false;
		}//listenr
		
	};
	
	private void init(){//从数据表contact里面拿出数据
		dbhelper=new MyDatabaseHelper(mContext,"mail.db",null,1);
		SQLiteDatabase db=dbhelper.getWritableDatabase();
		//得到所有contact数据
		Cursor cursor =db.query("contact", null, null, null,null,null,null);
		Contact_item item;
		if(cursor.moveToFirst()){
			do{
				//遍历所有数据
				String name=cursor.getString(cursor.getColumnIndex("name"));
				String email=cursor.getString(cursor.getColumnIndex("email"));
				item=new Contact_item(name,email,R.drawable.user);
				list.add(item);
			}
			while(cursor.moveToNext());
		}
		cursor.close();
	}
	private void showData(){
		ContactAdapter adapter=new ContactAdapter(mContext,R.layout.contact_item,list);
		listView=(ListView)this.findViewById(R.id.address_list_view);
		listView.setAdapter(adapter);
	}
}
