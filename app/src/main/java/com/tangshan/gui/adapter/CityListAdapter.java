package com.tangshan.gui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.city.CityListFragment;
import com.tangshan.gui.ui.tianqi.CMTianqi;

public class CityListAdapter extends BaseAdapter {

	private Context context;
	private List<MCityInfo> list;
	private LayoutInflater layoutInflater;
	private AlertDialog alertDialog;
	private CMPreference preference;
	private CityListFragment fragment;
	private CMTianqi fatherParent;

	public CityListAdapter(CityListFragment fragment, List<MCityInfo> list,
			CMTianqi fatherParent) {
		this.fragment = fragment;
		this.context = fragment.getActivity();
		preference = new CMPreference(context);
		this.list = list;
		this.fatherParent = fatherParent;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.cm_city_item, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView);
			holder.textView = (TextView) convertView
					.findViewById(R.id.textView);
			holder.llHome = convertView.findViewById(R.id.llHome);
			holder.imageViewD = (ImageView) convertView
					.findViewById(R.id.imageViewDefaa);
			holder.llll = convertView.findViewById(R.id.llll);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MCityInfo mCityInfo = getItem(position);
		final int isDel = Integer.parseInt(mCityInfo.getCategory());
		holder.textView.setText(mCityInfo.getsName());
		if (position == 0) {
			convertView.setBackgroundColor(Color.parseColor("#eceeef"));
			holder.imageView.setBackgroundResource(R.drawable.locationzafs);
			holder.imageViewD.setVisibility(View.GONE);
		} else {
			if (isDel == 1) {
				holder.imageView.setBackgroundResource(R.drawable.delee);
				holder.imageView.setVisibility(View.VISIBLE);
			} else {
				holder.imageView.setVisibility(View.INVISIBLE);
				holder.imageView.setBackgroundResource(0);
			}
			if (mCityInfo.getsName().equals(
					preference.getDefautlCityInfo().getsName())) {
				holder.imageViewD.setVisibility(View.GONE);
			} else {
				holder.imageViewD.setVisibility(View.GONE);
			}
		}
		// holder.llHome.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (position != 0) {
		// if (isDel == 1) {
		// showHome(position);
		// } else {
		// fragment.getFragmentManager().popBackStack();
		// }
		// } else {
		// fragment.getFragmentManager().popBackStack();
		// }
		// }
		// });

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (position != 0) {
					if (isDel == 1) {
						showDel(position);
					} else {
						fragment.getFragmentManager().popBackStack();
						fatherParent.onCMBackItems(mCityInfo);
					}
				} else {
					fragment.getFragmentManager().popBackStack();
					fatherParent.onCMBackItems(mCityInfo);
				}
			}
		});

		return convertView;
	}

	protected void showHome(final int position) {
		// TODO Auto-generated method stub
		if (alertDialog != null) {
			alertDialog = null;
		}
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage("设为默认城市吗?");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						preference.setDefautlCityInfo(list.get(position));
						notifyDataSetChanged();
						fragment.getFragmentManager().popBackStack();
					}
				});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		alertDialog.show();
	}

	protected void showDel(final int position) {
		// TODO Auto-generated method stub
		if (alertDialog != null) {
			alertDialog = null;
		}
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage("确定要删除么?");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						list.remove(position);
						List<MCityInfo> infos = new ArrayList<MCityInfo>();
						for (int i = 1; i < list.size(); i++) {
							MCityInfo info = list.get(i);
							info.setCategory("0");
							infos.add(info);
						}
						preference.setChooseCityList(infos);
						notifyDataSetChanged();
					}
				});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		alertDialog.show();
	}

	private final class ViewHolder {
		ImageView imageView, imageViewD;
		TextView textView;
		View llHome, llll;

	}

	public void updateCauseEdit(boolean isEdit) {
		// TODO Auto-generated method stub
		for (MCityInfo cityInfo : list) {
			if (isEdit) {
				cityInfo.setCategory("1");
			} else {
				cityInfo.setCategory("0");
			}
		}
		notifyDataSetChanged();
	}
}
