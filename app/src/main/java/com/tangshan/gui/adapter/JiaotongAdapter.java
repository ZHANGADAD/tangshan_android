package com.tangshan.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;

public class JiaotongAdapter extends BaseAdapter {

	private List<MCityInfo> list;
	private LayoutInflater layoutInflater;

	public JiaotongAdapter(Context context, List<MCityInfo> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public MCityInfo getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.cm_jiaotong_item,
					null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tvContent);
			holder.imageView = (TextView) convertView.findViewById(R.id.tvDay);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MCityInfo cityInfo = getItem(position);
		holder.imageView.setText(cityInfo.getsNum());
		holder.tvTitle.setText(cityInfo.getsName());
		holder.tvContent.setText(cityInfo.getContent());
		return convertView;
	}

	private final class ViewHolder {
		TextView tvContent, tvTitle, imageView;
	}
}
