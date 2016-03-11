package com.jh.adapter;

import java.util.LinkedList;

import java.util.List;

import com.jinher.commonlib.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
/**
 * 多选Adapter
 * @author jhzhangnan1
 *
 * @param <T>
 */
public class MultiSelectAdapter<T> extends BaseAdapter{

	private List<T> items;
	private List<T> selected;
	private Context context;
	private LayoutInflater inflater;
	public MultiSelectAdapter(List<T> items,Context context)
	{
		this.items = items;
		this.context = context;
		selected = new LinkedList<T>();
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
			//根据数组id得到数组类型 
			TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.listChoiceIndicatorMultiple}); 
			Drawable indicator = ta.getDrawable(0); 
			root.setCheckMarkDrawable(indicator); 
			if(selected.contains(item))
			{
				root.setChecked(true);
			}
			else
			{
				root.setChecked(false);
			}
			ta.recycle() ;
		}
		root.setText(items.get(position).toString());
		return root;
	}
	public void setSelected(int position,boolean isSelect)
	{
		if(isSelect)
		{
			if(!selected.contains(items.get(position)))
				selected.add(items.get(position));
		}
		else
			selected.remove(items.get(position));
	}
	public boolean isSelected(int position)
	{
		return selected.contains(items.get(position));
	}
	public void clearSelect()
	{
		selected.clear();
	}
	public List<T> getSelect()
	{
		List<T> tmpSelect = new LinkedList<T>();
		tmpSelect.addAll(selected);
		return tmpSelect;
	}
	public void setSelectList(List<T> selected)
	{
		this.selected.clear();
		if(selected!=null)
		{
			this.selected.addAll(selected);
		}
	}
}
