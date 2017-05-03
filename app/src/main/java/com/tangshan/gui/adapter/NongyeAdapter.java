package com.tangshan.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.ui.nongye.NongyeDetailsFragment;

public class NongyeAdapter extends BaseAdapter {

	private List<MCityInfo> list;
	private LayoutInflater layoutInflater;
	private Context context;

	public NongyeAdapter(Context context, List<MCityInfo> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;
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
			convertView = layoutInflater.inflate(R.layout.cm_nongye_item, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tvContent);
			holder.imageView = (TextView) convertView
					.findViewById(R.id.imageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MCityInfo cityInfo = getItem(position);
		holder.imageView.setText(cityInfo.getsNum());
		holder.tvTitle.setText(cityInfo.getsName());
		holder.tvContent.setText(cityInfo.getContent());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (context instanceof MainActivity) {
					NongyeDetailsFragment fragment = new NongyeDetailsFragment();
					((MainActivity) context).switchContent(fragment, true);
				}
			}
		});
		return convertView;
	}

	private final class ViewHolder {
		TextView tvContent, tvTitle, imageView;
	}
}
