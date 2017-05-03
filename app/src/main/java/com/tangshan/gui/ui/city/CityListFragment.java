package com.tangshan.gui.ui.city;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.adapter.CityListAdapter;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.ui.tianqi.CMTianqi;

public class CityListFragment extends BaseFragment implements OnClickListener,
        OnCmbackListener {

    private CMPreference preference;
    private ImageView ivImageView, ivImageViewDel;
    private ListView lvList;
    private CityListAdapter adapter;
    private List<MCityInfo> list;
    private CMTianqi fatherParent;

    public CityListFragment(CMTianqi fatherParent) {
        // TODO Auto-generated constructor stub
        this.fatherParent = fatherParent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        preference = new CMPreference(context);
        list = new ArrayList<MCityInfo>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewParent = inflater.inflate(R.layout.cm_citylist, null);
        initView();
        return viewParent;
    }

    private void initView() {
        // TODO Auto-generated method stub
        lvList = (ListView) viewParent.findViewById(R.id.lvList);
        list.add(preference.getLocationCityInfo());
        list.addAll(preference.getChooseCityList());
        for (MCityInfo info : list) {
            info.setCategory("0");
            info.setContent("0");
        }
        adapter = new CityListAdapter(CityListFragment.this, list, fatherParent);
        lvList.setAdapter(adapter);

        ivImageView = (ImageView) viewParent.findViewById(R.id.ivImageView);
        ivImageView.setVisibility(View.VISIBLE);
        ivImageViewDel = (ImageView) viewParent
                .findViewById(R.id.ivImageViewDel);
        ivImageViewDel.setVisibility(View.GONE);
        viewParent.findViewById(R.id.btBaocun).setOnClickListener(this);
        viewParent.findViewById(R.id.btTianjia).setOnClickListener(this);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btBack:
                getFragmentManager().popBackStack();
                if (fatherParent != null) {
                    fatherParent.onCMBackItems(null);
                }
                break;
            case R.id.btBaocun:
                if (ivImageView.getVisibility() == View.VISIBLE) {
                    // 修改
                    ivImageView.setVisibility(View.GONE);
                    ivImageViewDel.setVisibility(View.VISIBLE);
                    adapter.updateCauseEdit(true);
                } else {
                    ivImageView.setVisibility(View.VISIBLE);
                    ivImageViewDel.setVisibility(View.GONE);
                    adapter.updateCauseEdit(false);
                }
                break;
            case R.id.btTianjia:
                if (context instanceof MainActivity) {
                    CityListChooseFragment fragment = new CityListChooseFragment(
                            CityListFragment.this);
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCMBackItems(MCityInfo cityInfo) {
        // TODO Auto-generated method stub
        boolean isContain = false;
        for (MCityInfo info : list) {
            if (info.getsNum().equals(cityInfo.getsNum())) {
                isContain = true;
            }
        }
        if (!isContain) {
            list.add(cityInfo);
            adapter.notifyDataSetChanged();
            List<MCityInfo> infos = new ArrayList<MCityInfo>();
            for (int i = 1; i < list.size(); i++) {
                MCityInfo info = list.get(i);
                info.setCategory("0");
                infos.add(info);
            }
            preference.setChooseCityList(infos);
        }
        getFragmentManager().popBackStack();
        fatherParent.onCMBackItems(cityInfo);

    }

    public static CityListFragment getInstance(CMTianqi fatherParent) {
        return new CityListFragment(fatherParent);
    }

}
