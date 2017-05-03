package com.tangshan.gui.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.ui.wangri.WangriShiFragment;
import com.tangshan.gui.ui.wangri.WangtiPagerFragment;

public class CMWangriAdapter extends FragmentPagerAdapter {

	private WangtiPagerFragment cmTianqi;
	private List<MCityInfo> infos;
	private boolean isFuture;

	public CMWangriAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public CMWangriAdapter(FragmentManager fm, WangtiPagerFragment cmTianqi,
			List<MCityInfo> infos, boolean isFuture) {
		// TODO Auto-generated constructor stub
		super(fm);
		this.cmTianqi = cmTianqi;
		this.infos = infos;
		this.isFuture = isFuture;
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		String title = infos.get(index).getsName();
		String nuadsd = infos.get(index).getsNum();
		return new WangriShiFragment(cmTianqi, title, nuadsd,isFuture);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}
}
