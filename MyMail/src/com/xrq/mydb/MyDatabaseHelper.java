package com.xrq.mydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

	public static String create_contact="create table contact ("
	                                    +"name text primary key, "
			                            +"email text)";
	public static String user_msg="create table user_msg ("
            +"id text primary key, "
            +"account text, "
            +"password text)";
	
	private Context mContext;
	public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO 自动生成的构造函数存根
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自动生成的方法存根
		db.execSQL(create_contact);
		db.execSQL(user_msg);
		//组装
		ContentValues values=new ContentValues();
		values.put("id", "1");
		values.put("account", "13927594696");
		values.put("password", "1995328mmmm");
		db.insert("user_msg", null, values);
		values.clear();
		Toast.makeText(mContext, "成功创建数据表", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自动生成的方法存根
		
	}

}
