package com.tangshan.gui.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.ui.tianqi.CMTianqi;
import com.tangshan.gui.ui.tianqi.CMTiqiContentFragment;

public class CMPagerAdapter extends FragmentPagerAdapter {

	private CMTianqi cmTianqi;
	private List<MCityInfo> infos;

	public CMPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public CMPagerAdapter(FragmentManager fm, CMTianqi cmTianqi,
			List<MCityInfo> infos) {
		// TODO Auto-generated constructor stub
		super(fm);
		this.cmTianqi = cmTianqi;
		this.infos = infos;
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		String title = infos.get(index).getsName();
		String nuadsd=infos.get(index).getsNum();
		return new CMTiqiContentFragment(cmTianqi, title,nuadsd);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}
}
