package com.jh.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseJHAdapter<T> extends BaseAdapter{
	private List<T> items = new ArrayList<T>();
	private Context context;
	private LayoutInflater layoutInflater;
	private  ViewGroup parent;
	private int layoutResId = -1;
	public BaseJHAdapter(Context context,List<T> items){
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}
	public BaseJHAdapter(Context context,List<T> items,int layoutResId){
		this(context, items);
		this.layoutResId = layoutResId;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(items==null){
			return 0;
		}
		else {
			return items.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	private class ViewHolder{
		public int position;
		public HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	}
	/*private Object initViewHolder(View convertView){
		return null;
	}*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		View rootView;
		ViewHolder holder;
		if(convertView==null){
			rootView = newView(layoutInflater,position,parent);
			holder = new ViewHolder();
			 rootView.setTag(holder);
		}
		else{
			rootView = convertView;
			holder = (ViewHolder)rootView.getTag();
			holder.viewMap.clear();
		}
		holder.position = position;
		
		bindView(position,rootView);
		return rootView;
	}
	/**
	 * 所有通过子View来获取的，必须通过该方法，同时该方法
	 * @param rootView
	 * @param id
	 * @return
	 */
	public View getItemChildViewById(View rootView,int id){
		ViewHolder viewHolder = (ViewHolder)rootView.getTag();
		
		if(viewHolder!=null&&viewHolder.viewMap.get(id)!=null){
			return viewHolder.viewMap.get(id);
		}
		else{
			View childView = rootView.findViewById(id);
			if(viewHolder!=null){
				viewHolder.viewMap.put(id, childView);
			}
			return childView;
		}
	}
	/**
	 * 初始化View
	 * @param layoutInflater
	 * @param position
	 * @param parent
	 * @return
	 */
	public View newView(LayoutInflater layoutInflater,int position, ViewGroup parent){ 
		if(layoutResId!=-1)
		{
			return layoutInflater.inflate(layoutResId, null);
		}
		return null;
	}
	/**
	 * 将View与数据绑定起来
	 * @param position
	 * @param view
	 * @return
	 */
	public View bindView(int position, View view){
		return null;
	}
	/**
	 * 在初始化View时调用。
	 */
	public void initClickListener(){
		
	}
	/**
	 * 添加元素到头
	 * @param item
	 */
	public void addItemFirst(T item){
		this.items.add(0, item);
		this.notifyDataSetChanged();
	}
	/**
	 * 添加元素
	 * @param position
	 * @param item
	 */
	public void addItem(int position,T item){
		this.items.add(position, item);
		this.notifyDataSetChanged();
	}
	/**
	 * 移除元素
	 * @param item
	 */
	public void removeItem(T item){
		this.items.remove(item);
		this.notifyDataSetChanged();
	}
	/**
	 * 移除元素
	 * @param position
	 */
	public void removeItem(int position){
		this.items.remove(position);
		this.notifyDataSetChanged();
	}
	/**
	 * 重新设置列表
	 * @param items
	 */
	public void resetList(List<T> items){
		this.items.clear(); 
		this.items.addAll(items);
		this.notifyDataSetChanged();
	}
	/**
	 * 获取列表项
	 * @return
	 */
	public List<T> getList(){
		 List<T> items = new ArrayList<T>();
		 items.addAll(items);
		 return items;
	}
	/**
	 * 获取位置对应的子View,一般用于直接更新数据，避免使用adapter.notifyDataSetChanged，可以节省效率。如果当前position对应的View不再界面中，返回null.
	 * @param position
	 * @return
	 */
	public View getViewByPosition(int position){
		if(this.parent!=null){
			for(int index=0;index<this.parent.getChildCount();index++){
				View view = this.parent.getChildAt(index);
				ViewHolder viewHolder = (ViewHolder)view.getTag();
				if(viewHolder==null){
					continue;
				}else{
					if(viewHolder.position == position){
						return view;
					}
				}
			}
		}
		return null;
	}
	/**
	 * 更新第position个条目，运行于主线程
	 * @param position
	 */
	public void invalidateItem(int position){
		View view = getViewByPosition(position);
		if(view!=null)
			view.invalidate();
	}
}
