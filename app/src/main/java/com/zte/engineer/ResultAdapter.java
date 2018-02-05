package com.zte.engineer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResultAdapter extends BaseAdapter {
	private static class ViewHolder {
		TextView name;
		TextView state;
                View layout;
	}

	private LayoutInflater inflater;
	private Context mContext;
	private List<String> resultlist = new ArrayList<String>();
	private List<Integer> codelist = new ArrayList<Integer>();

	public ResultAdapter(Context context, List<String> list,List<Integer> code) {
		this.inflater = LayoutInflater.from(context);
		mContext = context;
		resultlist = list;
		codelist = code;
	}

	@Override
	public int getCount() {
		return resultlist.size();
	}

	@Override
	public Object getItem(int position) {
		return resultlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		ViewHolder holder;
		if (convertView == null
				|| (holder = (ViewHolder) convertView.getTag()) == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.result_list_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.layout = convertView.findViewById(R.id.items);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		} 		
		holder.name.setText(resultlist.get(position));
                holder.name.setTextColor(Color.BLUE);
		if (codelist.get(position) == 20) {
			holder.layout.setBackgroundColor(Color.RED);
			holder.state.setTextColor(Color.BLUE);
			holder.state.setText("FAIL");
		}else {
			holder.layout.setBackgroundColor(Color.GREEN);
			holder.state.setTextColor(Color.BLUE);
			holder.state.setText("PASS");
		}
		return convertView;
	}

}
