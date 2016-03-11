package com.jh.adapter;

import java.util.LinkedList;
import java.util.List;

import com.jinher.commonlib.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
/**
 * 单选adapter
 * @author jhzhangnan1
 *
 */
public class SingleSelectAdapter extends BaseAdapter{

	private List<String> items;
	private String selected;
	private Context context;
	private LayoutInflater inflater;
	public SingleSelectAdapter(List<String> items,Context context)
	{
		this.items = items;
		this.context = context;
		selected = "";
		inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CheckedTextView root;
		if(convertView!=null)
		{
			root = (CheckedTextView)convertView;
		}
		else
		{
			root = (CheckedTextView)inflater.inflate(R.layout.multi_select_item, null);
		}
		Object item = items.get(position);
		if (position==items.size()-1) {
			root.setCheckMarkDrawable(0);
		}else {
			TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.listChoiceIndicatorSingle});
			root.setCheckMarkDrawable(typedArray.getDrawable(0));
			if(selected.equals(item))
			{
				root.setChecked(true);
			}
			else
			{
				root.setChecked(false);
			}
		}
		root.setText(items.get(position).toString());
		return root;
	}
	public void setSelected(int position,boolean isSelect)
	{
		if(isSelect)
		{
			if(selected!=items.get(position))
				selected = items.get(position);
		}
	}
	public void setListItems(List<String> list){
		items = list;
	}
	public void clearSelect()
	{
		selected = "";
	}
	public String getSelect()
	{
		return selected;
	}
}
