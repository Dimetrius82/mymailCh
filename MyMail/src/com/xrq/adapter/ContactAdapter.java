package com.xrq.adapter;

import java.util.List;

import com.xrq.mymail.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact_item> {

	private int resource;//item的ID
	public ContactAdapter(Context context ,int textViewResourceId, List<Contact_item> list) {
		super(context, textViewResourceId, list);
		resource=textViewResourceId;
		// TODO 自动生成的构造函数存根
	}
	@Override
	public View getView(int p,View convertView,ViewGroup parent){
		Contact_item item=this.getItem(p);
		View view;
		if(convertView==null){
			view=LayoutInflater.from(getContext()).inflate(resource,null);
		}
		else{
			view=convertView;
		}
		//对每一项都加载
		TextView name=(TextView)view.findViewById(R.id.user_name);
		TextView email=(TextView)view.findViewById(R.id.email);
		ImageView image=(ImageView)view.findViewById(R.id.user_image);
		name.setText(item.getUser());
		email.setText(item.getEmail());
		image.setImageResource(R.drawable.user);
		return view;
		
	}

}
