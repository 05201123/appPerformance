package com.jh.memory.leak.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jh.memory.R;
/**
 * 
 * @author 099
 *
 */
public class LeakAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> mList;
	public LeakAdapter(Context context,List<String> list){
		this.mContext=context;
		mInflater = LayoutInflater.from(context);
		mList=new ArrayList<String>();
		mList.addAll(list);
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=mInflater.inflate(R.layout.leak_list_item, null);
//		ImageView imageview=(ImageView) view.findViewById(R.id.user_icon_iv);
		TextView textview=(TextView) view.findViewById(R.id.user_name_tv);
		if(!TextUtils.isEmpty(mList.get(position))){
			textview.setText(mList.get(position));
		}
		return view;
	}

}
