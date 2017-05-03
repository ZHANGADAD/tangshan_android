package com.tangshan.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.ui.city.CityListFragment;
import com.tangshan.gui.ui.jiaotong.JiaoTongFragment;
import com.tangshan.gui.util.Util;

public class CityGridAdapter extends BaseAdapter {

	private Context context;
	private int type;
	private List<MCityInfo> gridCityInfos;
	private LayoutInflater layoutInflater;
	private BaseFragment listChooseFragment;
	private CityListFragment listFragment;

	public CityGridAdapter(BaseFragment listChooseFragment,
			CityListFragment listFragment, List<MCityInfo> gridCityInfos,
			int type) {
		this.listChooseFragment = listChooseFragment;
		if (listFragment == null) {
			this.listFragment = null;
		} else {
			this.listFragment = listFragment;
		}
		this.context = listChooseFragment.getActivity();
		this.type = type;
		this.gridCityInfos = gridCityInfos;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gridCityInfos.size();
	}

	@Override
	public MCityInfo getItem(int position) {
		// TODO Auto-generated method stub
		return gridCityInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.cm_grid_item, null);
			holder.textView = (TextView) convertView
					.findViewById(R.id.llllText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setVisibility(View.GONE);
		MCityInfo cityInfo = getItem(position);
		String acafs = cityInfo.getCategory();
		if (Util.isEmpty(acafs)) {
			convertView.setVisibility(View.GONE);
		} else {
			if (Integer.parseInt(acafs) == 1) {
				convertView.setVisibility(View.VISIBLE);
			} else {
				convertView.setVisibility(View.GONE);
			}

		}
		if (type == 1) {
			holder.textView.setGravity(Gravity.LEFT);
		}
		holder.textView.setText(cityInfo.getsName());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MCityInfo cityInfo = getItem(position);
				if (type == 0) {
					// GridView

				} else {
					// ListView
				}
				if (listChooseFragment instanceof JiaoTongFragment) {
					((JiaoTongFragment) listChooseFragment)
							.refreshDataByChooseData(cityInfo);
				} else {
					listChooseFragment.getFragmentManager().popBackStack();
					if (listFragment != null) {
						listFragment.onCMBackItems(cityInfo);
					}
				}
			}
		});
		return convertView;
	}

	private final class ViewHolder {
		TextView textView;

	}
}
