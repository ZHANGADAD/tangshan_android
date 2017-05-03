package com.tangshan.gui.ui.city;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.tangshan.gui.R;
import com.tangshan.gui.adapter.CityGridAdapter;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.bean.SearchCity;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.util.Util;

public class CityListChooseFragment extends BaseFragment implements
		OnClickListener {

	private View llHotCity, llSearch;
	private ListView llList;
	private GridView gvGridView;
	private List<MCityInfo> gridCityInfos, lvCityInfos;
	private EditText etSearch;
	private CityGridAdapter adapterGrid;
	private CityGridAdapter adapterList;
	private Button btMore;
	private InputMethodManager manager;
	private CityListFragment cityListFragment;
	private List<SearchCity> listSearch;

	public CityListChooseFragment(CityListFragment cityListFragment) {
		// TODO Auto-generated constructor stub
		this.cityListFragment = cityListFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
		manager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		listSearch = Util.getSearchCityList();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (viewParent == null) {
			viewParent = inflater.inflate(R.layout.cm_citylist_all, null);
			initView();
		} else {
			((ViewGroup) viewParent.getParent()).removeView(viewParent);
		}
		return viewParent;
	}

	private void initView() {
		// TODO Auto-generated method stub
		llHotCity = viewParent.findViewById(R.id.llHotCity);
		llSearch = viewParent.findViewById(R.id.llSearch);
		initHot();
		initSearch();

	}

	private void initSearch() {
		// TODO Auto-generated method stub
		viewParent.findViewById(R.id.btCancel).setOnClickListener(this);
		llList = (ListView) viewParent.findViewById(R.id.llList);
		etSearch = (EditText) viewParent.findViewById(R.id.etSearch);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String string = s.toString();
				lvCityInfos.clear();
				for (SearchCity city : listSearch) {
					if (!Util.isEmpty(string)) {
						String made = city.getName();
						if (made.contains(string)) {
							List<MCityInfo> cityInfos = city.getInfos();
							for (MCityInfo cityInfo : cityInfos) {
								cityInfo.setCategory("1");
								lvCityInfos.add(cityInfo);
							}
						}
					}
				}
				// List<MCityInfo> infos =
				// for (MCityInfo info : infos) {
				// if (!Util.isEmpty(string)) {
				// String made = info.getsName();
				// if (made.contains(string)) {
				// info.setCategory("1");
				// lvCityInfos.add(info);
				// }
				// }
				// }
				adapterList.notifyDataSetChanged();
			}
		});

		setListView();
	}

	private void initHot() {
		// TODO Auto-generated method stub
		gvGridView = (GridView) viewParent.findViewById(R.id.gvGridView);
		viewParent.findViewById(R.id.btBack).setOnClickListener(this);
		btMore = (Button) viewParent.findViewById(R.id.btMore);
		btMore.setOnClickListener(this);
		viewParent.findViewById(R.id.btSearch).setOnClickListener(this);
		setGridView();
	}

	private void setListView() {
		lvCityInfos = new ArrayList<MCityInfo>();
		adapterList = new CityGridAdapter(CityListChooseFragment.this,
				cityListFragment, lvCityInfos, 1);
		llList.setAdapter(adapterList);
	}

	private void setGridView() {
		gridCityInfos = new ArrayList<MCityInfo>();
		gridCityInfos.addAll(Util.getHotCityList());
		for (MCityInfo cityInfo : gridCityInfos) {
			cityInfo.setCategory("1");
		}
		adapterGrid = new CityGridAdapter(CityListChooseFragment.this,
				cityListFragment, gridCityInfos, 0);
		gvGridView.setAdapter(adapterGrid);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btBack:
		case R.id.btCancel:
			getFragmentManager().popBackStack();
			break;
		case R.id.btSearch:
			etSearch.setFocusable(true);
			etSearch.requestFocus();
			manager.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
			llHotCity.setVisibility(View.GONE);
			llSearch.setVisibility(View.VISIBLE);
			break;
		case R.id.btMore:
			btMore.setVisibility(View.GONE);
			List<MCityInfo> cityInfos = Util.getMoreCityInfos();
			for (MCityInfo cityInfo : cityInfos) {
				cityInfo.setCategory("1");
			}
			gridCityInfos.addAll(cityInfos);
			adapterGrid.notifyDataSetChanged();
			break;
		}
	}

}
