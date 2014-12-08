package com.BARcode.utilities;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.BARcode.mycarpooling.R;

public class CarpoolAdapter extends BaseExpandableListAdapter{

	private Context ctx;
	private HashMap<String, List<String>> carpoolHistory;
	private List<String> carpools;
	
	public CarpoolAdapter(Context ctx, HashMap<String, List<String>> carpoolHistory, List<String> carpools){
		this.ctx = ctx;
		this.carpoolHistory = carpoolHistory;
		this.carpools = carpools;
	}
	
	@Override
	public int getGroupCount() {
		return carpools.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return carpoolHistory.get(carpools.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return carpools.get(groupPosition);
	}

	@Override
	public Object getChild(int parent, int child) {
		return carpoolHistory.get(carpools.get(parent)).get(child);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int parent, int child) {
		return child;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int parent, boolean isExpanded,
			View convertView, ViewGroup parentView) {
		String group_title = (String) getGroup(parent);
		if(convertView == null){
			LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.parent_expandable_layout, parentView, false);
		}
		TextView parent_textview = (TextView) convertView.findViewById(R.id.parent_text);
		parent_textview.setTypeface(null, Typeface.BOLD);
		parent_textview.setText(group_title);
		return convertView;
	}

	@Override
	public View getChildView(int parent, int child,
			boolean lastChild, View convertView, ViewGroup parentView) {
		
		String child_title = (String)getChild(parent, child);
		if(convertView == null){
			LayoutInflater inflator = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.child_expandable_layout, parentView, false);
		}
		TextView child_textview = (TextView)convertView.findViewById(R.id.child_txt);
		
		child_textview.setText(child_title);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
