package com.xrq.util;

import com.xrq.adapter.Contact_item;
import com.xrq.mydb.MyDatabaseHelper;
import com.xrq.mymail.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GetPwAndAcc {
    public String pw;
    public String acc;
    public Context con;
    public GetPwAndAcc(Context context){
    	con=context;
    	MyDatabaseHelper dbhelper=new MyDatabaseHelper(con,"mail.db",null,1);
		SQLiteDatabase db=dbhelper.getWritableDatabase();
		//得到所有数据
		Cursor cursor =db.query("user_msg", null, null, null,null,null,null);
		if(cursor.moveToFirst()){	
				//遍历
				acc=cursor.getString(cursor.getColumnIndex("account"));
				pw=cursor.getString(cursor.getColumnIndex("password"));	
		}
		cursor.close();
    }
    
    public String getAcc(){
    	return acc;
    }
    public String getPw(){
    	return pw;
    }
}
