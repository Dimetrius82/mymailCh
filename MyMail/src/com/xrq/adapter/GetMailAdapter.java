package com.xrq.adapter;

import java.util.List;

import com.xrq.mymail.R;
import com.xrq.util.MailItemUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GetMailAdapter extends ArrayAdapter<MailItemUtil> {

	int resourceid;
	public GetMailAdapter(Context context,  int textViewResourceId, List<MailItemUtil> list) {
		super(context, textViewResourceId, list);
		// TODO 自动生成的构造函数存根
		resourceid=textViewResourceId;
	}
	@Override
	public View getView(int p,View convertView,ViewGroup parent){
		MailItemUtil item=this.getItem(p);
		View view;
		if(convertView==null){
			view=LayoutInflater.from(getContext()).inflate(resourceid,null);
		}
		else{
			view=convertView;
		}
		//对每一项都加载
		ImageView image=(ImageView)view.findViewById(R.id.from_image);
		TextView from=(TextView)view.findViewById(R.id.from_text);
		TextView subject=(TextView)view.findViewById(R.id.subject);
		TextView attach=(TextView)view.findViewById(R.id.attach);
		TextView date=(TextView)view.findViewById(R.id.date);
		image.setImageResource(item.getImageId());
		from.setText(item.getFrom());
		subject.setText(item.getSub());
	    attach.setText(item.getDate());
		date.setText(item.getDate());
		return view;	
	}
}
