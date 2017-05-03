package com.tangshan.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.ui.yujing.YujingDetailsFragment;
import com.tangshan.gui.ui.yujing.YujingFragment;

public class ShenghuoQiXiangAdapter extends BaseAdapter {

	private List<MCityInfo> list;
	private LayoutInflater layoutInflater;
	private Context context;
	private BaseFragment fragment;

	public ShenghuoQiXiangAdapter(BaseFragment fragment, List<MCityInfo> list) {
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
			convertView = layoutInflater.inflate(
					R.layout.cm_shenghuoqixiang_item, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tvContent);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MCityInfo cityInfo = getItem(position);
		holder.imageView.setBackgroundResource(Integer.parseInt(cityInfo
				.getCategory()));
		holder.tvTitle.setText(cityInfo.getsName());
		holder.tvContent.setText(cityInfo.getsNum());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (fragment != null && fragment instanceof YujingFragment) {
					if (context instanceof MainActivity) {
						YujingDetailsFragment fragment = new YujingDetailsFragment();
						((MainActivity) context).switchContent(fragment, true);

					}
				}
			}
		});
		return convertView;
	}

	private final class ViewHolder {
		TextView tvContent, tvTitle;
		ImageView imageView;
	}
}
