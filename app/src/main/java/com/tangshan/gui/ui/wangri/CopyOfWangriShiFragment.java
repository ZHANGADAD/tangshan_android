package com.tangshan.gui.ui.wangri;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.util.Util;
import com.tangshan.gui.view.chart.LineView;
import com.tangshan.gui.view.chart.MyUtils;
import com.tangshan.gui.view.chart.ScrollListenerHorizontalScrollView;
import com.tangshan.gui.view.chart.ScrollListenerHorizontalScrollView.OnScrollListener;
import com.tangshan.gui.view.chart.ViewHelper;

import cz.msebera.android.httpclient.Header;

public class CopyOfWangriShiFragment extends BaseFragment implements OnClickListener {

	// private CMPreference preference;
	private TextView textViewTitle;
	private ScrollListenerHorizontalScrollView topScrollView, bottomScrollView;
	private LineView llTop, llBottom;
	private List<MCityInfo> cityInfos, cityInfos1;
	private View remenView, remenView1, toutiaoView, toutiaoView1, zhuantiView;
	private TextView llTopGrad1, llTopGrad11, llTopGrad2, llTopGrad21,
			llTopGrad3, llTopGrad31;
	private View llScrollTop, llScrollBottom;
	private TextView tvTips, tvTipsBotttom;
	int sideLineLength;
	private MCityInfo cityInfo;
	private CMPreference preference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
		preference = new CMPreference(context);
		sideLineLength = MyUtils.dip2px(context, 45) / 3 * 2;
		cityInfo = (MCityInfo) getArguments().getSerializable("city");
	}

	@Override
	public void onResume() {
		MainActivity.getInstance().mContent = this;
		super.onResume();
		// checkeTitle();
	}

	// private void checkeTitle() {
	// // TODO Auto-generated method stub
	// String title = textViewTitle.getText().toString();
	// String nadad = preference.getDefautlCityInfo().getsName();
	// if (!title.equals(nadad)) {
	// textViewTitle.setText(nadad);
	// getData();
	// }
	// }

	private void getData() {
		showDialog(context);
		RequestParams params = new RequestParams();
		params.put("stationNum", cityInfo.getsNum());
		CMHttpClient.getInstance().get(Gloable.GUOQU24URL, params,
				new CMHttpResponseHandler(false) {
					@Override
					public void onSuccess(int code, Header[] header, byte[] data) {
						// TODO Auto-generated method stub
						super.onSuccess(code, header, data);
						String consafdg = new String(data);
						if (!Util.isEmpty(consafdg)) {
							initWithNextData(consafdg);
						}
					}

					@Override
					public void onFailure(int code, Header[] header,
							byte[] data, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(code, header, data, throwable);
					}

				});

		CMHttpClient.getInstance().get(Gloable.TIANQIUTL, params,
				new CMHttpResponseHandler(true) {
					@Override
					public void onSuccess(int code, Header[] header, byte[] data) {
						// TODO Auto-generated method stub
						super.onSuccess(code, header, data);
						dismissDialog();
						String consafdg = new String(data);
						if (!Util.isEmpty(consafdg)) {
							initWithPreData(consafdg);
						}
					}

					@Override
					public void onFailure(int code, Header[] header,
							byte[] data, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(code, header, data, throwable);
						dismissDialog();
					}

				});
	}

	protected void initWithNextData(String consafdg) {
		try {
			if (Util.isEmpty(consafdg))
				return;
			if (cityInfos == null) {
				cityInfos = new ArrayList<MCityInfo>();
			} else {
				cityInfos.clear();
			}
			JSONObject jsonObject = new JSONObject(consafdg);
			JSONArray jsonArray = jsonObject.getJSONArray("previous24h");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				MCityInfo cityInfo = new MCityInfo();
				cityInfo.setsNum(object.getString("tmp"));
				cityInfo.setsName(object.getString("wind_sc"));
				String rimeee = object.getString("time");
				String hourddd = rimeee.substring(rimeee.length() - 2,
						rimeee.length());
				cityInfo.setCategory(hourddd + ":" + "00");
				cityInfo.setContent(object.getString("pcpn"));
				cityInfo.setFeng(object.getString("wind_dir_txt"));
//				cityInfo.setTianqi(object.getString("code"));
				cityInfos.add(cityInfo);
				cityInfo = null;
			}
			llTop.setBottomTextList(getIndexArray(3));
			ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
			ArrayList<String> minNumbers = getIndexArray(0);
			List<String> quJianList = getQujian(minNumbers);
			initQujian(quJianList, 0);
			ArrayList<Integer> arrayList = getLineNumers(minNumbers);
			dataLists.add(arrayList);
			llTop.setShowDataList(minNumbers, 0);
			llTop.setShowDataListData(cityInfos);
			llTop.setLineColor(Color.parseColor("#ffffff"));
			llTop.setDataList(dataLists);
			topScrollView.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					topScrollView.smoothScrollTo(llTop.getMeasuredWidth()
							- MyUtils.dip2px(context, 15 * 10),
							topScrollView.getTop());
				}
			});
			tvTips.setText(cityInfos.get(0).getsNum());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void initQujian(List<String> quJianList, int index) {
		if (index == 0) {
			int max = Float.valueOf(quJianList.get(1)).intValue() + 1;
			Float min = Float.valueOf(quJianList.get(0));
			BigDecimal bigDecimal = (new BigDecimal(max)
					.subtract(new BigDecimal(min))).divide(new BigDecimal(2))
					.add(new BigDecimal(min))
					.setScale(1, BigDecimal.ROUND_HALF_UP);
			Util.setTempreTure(new BigDecimal(max).setScale(1) + "", llTopGrad1);
			Util.setTempreTure(bigDecimal + "", llTopGrad2);
			Util.setTempreTure(min + "", llTopGrad3);
		} else if (index == 1) {
			int minLee = Integer.parseInt(quJianList.get(0));
			int maxLee = Integer.parseInt(quJianList.get(1)) + 1;
			if (Integer.parseInt(quJianList.get(1)) == minLee) {
				maxLee = Integer.parseInt(quJianList.get(1)) + 2;
			}
			int middle = 0;
			if ((maxLee - minLee) % 2 != 0) {
				middle = (int) (maxLee - minLee) / 2 + 1 + minLee;
			} else {
				middle = (maxLee - minLee) / 2 + minLee;
			}
			llTopGrad1.setText(maxLee + "级");
			llTopGrad2.setText(middle + "级");
			llTopGrad3.setText(minLee + "级");
		} else {
			int maxLee = Integer.parseInt(quJianList.get(1)) + 2;
			int minLee = Integer.parseInt(quJianList.get(0));
			int middle = 0;
			if ((maxLee - minLee) % 2 != 0) {
				middle = (int) (maxLee - minLee) / 2 + 1 + minLee;
			} else {
				middle = (maxLee - minLee) / 2 + minLee;
			}
			llTopGrad1.setText(maxLee + "mm");
			llTopGrad2.setText(middle + "mm");
			llTopGrad3.setText(minLee + "mm");
		}

	}

	protected void initQujian1(List<String> quJianList, int index) {
		if (index == 0) {
			int max = Float.valueOf(quJianList.get(1)).intValue() + 1;
			Float min = Float.valueOf(quJianList.get(0));
			BigDecimal bigDecimal = (new BigDecimal(max)
					.subtract(new BigDecimal(min))).divide(new BigDecimal(2))
					.add(new BigDecimal(min))
					.setScale(1, BigDecimal.ROUND_HALF_UP);
			Util.setTempreTure(new BigDecimal(max).setScale(1) + "",
					llTopGrad11);
			Util.setTempreTure(bigDecimal + "", llTopGrad21);
			Util.setTempreTure(min + "", llTopGrad31);
		} else if (index == 1) {
			int minLee = Integer.parseInt(quJianList.get(0));
			int maxLee = Integer.parseInt(quJianList.get(1)) + 1;
			if (Integer.parseInt(quJianList.get(1)) == minLee) {
				maxLee = Integer.parseInt(quJianList.get(1)) + 2;
			}
			int middle = 0;
			if ((maxLee - minLee) % 2 != 0) {
				middle = (int) (maxLee - minLee) / 2 + 1 + minLee;
			} else {
				middle = (maxLee - minLee) / 2 + minLee;
			}
			llTopGrad11.setText(maxLee + "级");
			llTopGrad21.setText(middle + "级");
			llTopGrad31.setText(minLee + "级");
		}

	}

	private ArrayList<String> getIndexArray(int i) {
		// TODO Auto-generated method stub
		ArrayList<String> arrayList = new ArrayList<String>();
		for (MCityInfo cityInfo : cityInfos) {
			if (i == 0) {
				// 气温
				arrayList.add(cityInfo.getsNum());
			} else if (i == 1) {
				// 风力
				arrayList.add(cityInfo.getsName());
			} else if (i == 2) {
				// 降水
				arrayList.add(cityInfo.getContent());
			} else {
				// 时间
				arrayList.add(cityInfo.getCategory());
			}
		}
		return arrayList;
	}

	private ArrayList<String> getIndexArray1(int i) {
		// TODO Auto-generated method stub
		ArrayList<String> arrayList = new ArrayList<String>();
		for (MCityInfo cityInfo : cityInfos1) {
			if (i == 0) {
				// 气温
				arrayList.add(cityInfo.getsNum());
			} else if (i == 1) {
				// 风力
				arrayList.add(cityInfo.getsName());
			} else if (i == 2) {
				// 降水
			} else {
				// 时间
				arrayList.add(cityInfo.getCategory());
			}
		}
		return arrayList;
	}

	protected void initWithPreData(String consafdg) {
		// TODO Auto-generated method stub
		try {
			if (Util.isEmpty(consafdg))
				return;
			if (cityInfos1 == null) {
				cityInfos1 = new ArrayList<MCityInfo>();
			} else {
				cityInfos1.clear();
			}
			JSONObject jsonObject = new JSONObject(consafdg);
			JSONArray jsonArray = jsonObject.getJSONArray("hourly_forecast");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				MCityInfo cityInfo = new MCityInfo();
				cityInfo.setsNum(object.getString("tmp"));
				cityInfo.setsName(object.getString("wind_sc_code"));
				String rimeee = object.getString("time");
				String hourddd = rimeee.substring(rimeee.length() - 4,
						rimeee.length());
				String hour = hourddd.substring(0, 2);
				String tim = hourddd.substring(2, 4);
				cityInfo.setCategory(hour + ":" + tim);
				cityInfo.setFeng(object.getString("wind_dir_txt"));
				cityInfo.setTianqi(object.getString("code"));
				cityInfos1.add(cityInfo);
				cityInfo = null;
			}
			llBottom.setBottomTextList(getIndexArray1(3));
			ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
			ArrayList<String> minNumbers = getIndexArray1(0);
			List<String> quJianList = getQujian(minNumbers);
			initQujian1(quJianList, 0);
			ArrayList<Integer> arrayList = getLineNumers(minNumbers);
			dataLists.add(arrayList);
			llBottom.setShowDataList(minNumbers, 0);
			llBottom.setShowDataListData(cityInfos1);
			llBottom.setLineColor(Color.parseColor("#ffffff"));
			llBottom.setDataList(dataLists);
			tvTipsBotttom.setText(cityInfos1.get(0).getsNum());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (viewParent == null) {
			viewParent = inflater.inflate(R.layout.cm_wangri, null);
			initView();
		} else {
			((ViewGroup) viewParent.getParent()).removeView(viewParent);
		}
		return viewParent;
	}

	private void configTitle() {
		// TODO Auto-generated method stub
		textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
		String name = cityInfo.getsName();
		String locationCity = preference.getLocationCityInfo().getsName();
		String defaldaf = preference.getDefautlCityInfo().getsName();
		textViewTitle.setText(name);
		if (defaldaf.equals(name)) {
			Drawable drawable = getResources().getDrawable(R.drawable.homda);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			textViewTitle.setCompoundDrawables(null, null, null, null);
		} else if (locationCity.equals(name)) {
			Drawable drawable = getResources().getDrawable(R.drawable.location);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			textViewTitle.setCompoundDrawables(drawable, null, null, null);
		} else {
			textViewTitle.setCompoundDrawables(null, null, null, null);
		}
		((ImageView) viewParent.findViewById(R.id.ivHeaderImageView))
				.setBackgroundResource(R.drawable.back);
		viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
		viewParent.findViewById(R.id.btShare).setVisibility(View.VISIBLE);
		viewParent.findViewById(R.id.ivShareImageView).setVisibility(
				View.VISIBLE);
		viewParent.findViewById(R.id.btBack).setOnClickListener(this);
	}

	private void initView() {
		configTitle();
		initTopView();
		initBottomView();
		getData();
	}

	private void initBottomView() {
		// TODO Auto-generated method stub
		bottomScrollView = (ScrollListenerHorizontalScrollView) viewParent
				.findViewById(R.id.llBottomScrollView);
		llBottom = (LineView) viewParent.findViewById(R.id.llBottom);

		llTopGrad11 = (TextView) viewParent.findViewById(R.id.lineBottom)
				.findViewById(R.id.llTopGrad1);
		llTopGrad21 = (TextView) viewParent.findViewById(R.id.lineBottom)
				.findViewById(R.id.llTopGrad2);
		llTopGrad31 = (TextView) viewParent.findViewById(R.id.lineBottom)
				.findViewById(R.id.llTopGrad3);

		remenView1 = viewParent.findViewById(R.id.remenView1);
		toutiaoView1 = viewParent.findViewById(R.id.toutiaoView1);

		viewParent.findViewById(R.id.btRemenzixun1).setOnClickListener(this);
		viewParent.findViewById(R.id.btYongchetoutiao1)
				.setOnClickListener(this);

		bottomScrollView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollChanged(
					ScrollListenerHorizontalScrollView scrollView, int x,
					int offSet, int range, int extent) {
				computeScroll(1, offSet, range, extent);
			}

			@Override
			public void onEndScroll(
					ScrollListenerHorizontalScrollView scrollView) {
			}
		});

		llScrollBottom = viewParent.findViewById(R.id.llScrollBottom);
		tvTipsBotttom = (TextView) llScrollBottom
				.findViewById(R.id.tvTipsBotttom);

		changeTab1(0);
	}

	private void initTopView() {
		topScrollView = (ScrollListenerHorizontalScrollView) viewParent
				.findViewById(R.id.llTopScrollView);
		topScrollView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollChanged(
					ScrollListenerHorizontalScrollView scrollView, int x,
					int offSet, int range, int extent) {
				computeScroll(0, offSet, range, extent);
			}

			@Override
			public void onEndScroll(
					ScrollListenerHorizontalScrollView scrollView) {
			}
		});

		llTop = (LineView) viewParent.findViewById(R.id.llTop);

		llTopGrad1 = (TextView) viewParent.findViewById(R.id.lineTop)
				.findViewById(R.id.llTopGrad1);
		llTopGrad2 = (TextView) viewParent.findViewById(R.id.lineTop)
				.findViewById(R.id.llTopGrad2);
		llTopGrad3 = (TextView) viewParent.findViewById(R.id.lineTop)
				.findViewById(R.id.llTopGrad3);

		remenView = viewParent.findViewById(R.id.remenView);
		toutiaoView = viewParent.findViewById(R.id.toutiaoView);
		zhuantiView = viewParent.findViewById(R.id.zhuantiView);

		viewParent.findViewById(R.id.btRemenzixun).setOnClickListener(this);
		viewParent.findViewById(R.id.btYongchetoutiao).setOnClickListener(this);
		viewParent.findViewById(R.id.btChemizhuanti).setOnClickListener(this);

		llScrollTop = viewParent.findViewById(R.id.llScrollTop);
		tvTips = (TextView) llScrollTop.findViewById(R.id.tvTips);

		changeTab(0);
	}

	protected void computeScroll(int x, int offSet, int range, int extent) {

		if (x == 0) {
			int allWidth = llTop.getMeasuredWidth()
					- MyUtils.dip2px(context, 15 * 10);
			float dasdfa = new BigDecimal(offSet)
					.multiply(new BigDecimal(MyUtils.getWidth(context)))
					.divide(new BigDecimal(allWidth), 1,
							BigDecimal.ROUND_HALF_UP).floatValue();

			int backgroundGridWidth = llScrollTop.getMeasuredWidth() - 5;
			int index = offSet / backgroundGridWidth;
			if (index >= 23) {
				index = 23;
			}
			if (remenView.getVisibility() == View.VISIBLE) {
				tvTips.setText(cityInfos.get(index).getsNum());
			} else if (toutiaoView.getVisibility() == View.VISIBLE) {
				tvTips.setText(cityInfos.get(index).getsName());
			} else if (zhuantiView.getVisibility() == View.VISIBLE) {
				tvTips.setText(cityInfos.get(index).getContent());
			}
			ViewHelper.setTranslationX(llScrollTop,
					dasdfa + MyUtils.dip2px(context, 12));

		} else {

			int allWidth = llBottom.getMeasuredWidth()
					- MyUtils.dip2px(context, 15 * 10);
			float dasdfa = new BigDecimal(offSet)
					.multiply(new BigDecimal(MyUtils.getWidth(context)))
					.divide(new BigDecimal(allWidth), 1,
							BigDecimal.ROUND_HALF_UP).floatValue();
			int backgroundGridWidth = llScrollBottom.getMeasuredWidth() - 5;
			int index = offSet / backgroundGridWidth;
			if (index >= 23) {
				index = 23;
			}

			if (remenView1.getVisibility() == View.VISIBLE) {
				tvTipsBotttom.setText(cityInfos1.get(index).getsNum());
			} else if (toutiaoView1.getVisibility() == View.VISIBLE) {
				tvTipsBotttom.setText(cityInfos1.get(index).getsName());
			}
			ViewHelper.setTranslationX(llScrollBottom,
					dasdfa + MyUtils.dip2px(context, 12));

		}

	}

	private void changeTab(int category) {
		// TODO Auto-generated method stub
		if (category == 0) {
			remenView.setVisibility(View.VISIBLE);
			toutiaoView.setVisibility(View.INVISIBLE);
			zhuantiView.setVisibility(View.INVISIBLE);
		} else if (category == 1) {
			toutiaoView.setVisibility(View.VISIBLE);
			remenView.setVisibility(View.INVISIBLE);
			zhuantiView.setVisibility(View.INVISIBLE);
		} else {
			zhuantiView.setVisibility(View.VISIBLE);
			remenView.setVisibility(View.INVISIBLE);
			toutiaoView.setVisibility(View.INVISIBLE);
		}
	}

	protected void reloadDataTop(int category) {
		// TODO Auto-generated method stub
		llTop.setBottomTextList(getIndexArray(3));
		ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
		ArrayList<String> minNumbers = getIndexArray(category);
		List<String> quJianList = getQujian(minNumbers);
		initQujian(quJianList, category);
		ArrayList<Integer> arrayList = getLineNumers(minNumbers);
		dataLists.add(arrayList);
		llTop.setShowDataList(minNumbers, category);
		llTop.setLineColor(Color.parseColor("#ffffff"));
		llTop.setDataList(dataLists);
	}

	protected void reloadDataBottom(int category) {
		// TODO Auto-generated method stub
		llBottom.setBottomTextList(getIndexArray1(3));
		ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
		ArrayList<String> minNumbers = getIndexArray1(category);
		List<String> quJianList = getQujian(minNumbers);
		initQujian1(quJianList, category);
		ArrayList<Integer> arrayList = getLineNumers(minNumbers);
		dataLists.add(arrayList);
		llBottom.setShowDataList(minNumbers, category);
		llBottom.setLineColor(Color.parseColor("#ffffff"));
		llBottom.setDataList(dataLists);
	}

	private void changeTab1(int category) {
		// TODO Auto-generated method stub
		if (category == 0) {
			remenView1.setVisibility(View.VISIBLE);
			toutiaoView1.setVisibility(View.INVISIBLE);
		} else if (category == 1) {
			toutiaoView1.setVisibility(View.VISIBLE);
			remenView1.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btBack:
			getFragmentManager().popBackStack();
			break;
		case R.id.btShare:
			gotoShare();
			break;
		case R.id.btRemenzixun:
			changeTab(0);
			reloadDataTop(0);
			break;
		case R.id.btYongchetoutiao:
			changeTab(1);
			reloadDataTop(1);
			break;
		case R.id.btChemizhuanti:
			changeTab(2);
			reloadDataTop(2);
			break;
		case R.id.btRemenzixun1:
			changeTab1(0);
			reloadDataBottom(0);
			break;
		case R.id.btYongchetoutiao1:
			changeTab1(1);
			reloadDataBottom(1);
			break;
		default:
			break;
		}
	}

	private void gotoShare() {
		// TODO Auto-generated method stub

	}
}
