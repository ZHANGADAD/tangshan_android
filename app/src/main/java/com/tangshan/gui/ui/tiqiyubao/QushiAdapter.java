package com.tangshan.gui.ui.tiqiyubao;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tangshan.gui.bean.MCityInfo;

public class QushiAdapter extends FragmentPagerAdapter {

	private QushiyubaoFragment cmTianqi;
	private List<MCityInfo> infos;

	public QushiAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public QushiAdapter(FragmentManager fm, QushiyubaoFragment cmTianqi,
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
		String nuadsd = infos.get(index).getsNum();
		return new TianqiYubaoFragment(cmTianqi, title, nuadsd);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}
}
