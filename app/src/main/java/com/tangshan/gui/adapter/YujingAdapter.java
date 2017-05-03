package com.tangshan.gui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.ui.yujing.YujingDetailsFragment;
import com.tangshan.gui.ui.yujing.YujingFragment;

public class YujingAdapter extends BaseAdapter {

	private List<MCityInfo> list;
	private LayoutInflater layoutInflater;
	private Context context;
	private YujingFragment fragment;

	public YujingAdapter(YujingFragment fragment, List<MCityInfo> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.fragment = fragment;
		this.context = fragment.getActivity();
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
			convertView = layoutInflater.inflate(R.layout.cm_yujing_item, null);
			holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tvContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MCityInfo cityInfo = getItem(position);
		holder.tvDay.setText(cityInfo.getsNum());
		holder.tvContent.setText(cityInfo.getsName());
		Drawable drawable = fragment.getYujingDrawable(cityInfo);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		holder.tvContent.setCompoundDrawables(drawable, null, null, null);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (context instanceof MainActivity) {
					YujingDetailsFragment fragment = new YujingDetailsFragment();
					Bundle bundle = new Bundle();
					bundle.putSerializable("id", cityInfo);
					fragment.setArguments(bundle);
					((MainActivity) context).switchContent(fragment, true);

				}
			}
		});
		return convertView;
	}

	private final class ViewHolder {
		TextView tvContent, tvDay;
	}
}
