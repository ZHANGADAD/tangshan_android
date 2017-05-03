package com.tangshan.gui.ui.fuwu;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.adapter.FuwuAdapter;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.ui.BaseFragment;

public class CMFuwu extends BaseFragment {

    private GridView gvGridView;

    private List<MCityInfo> list = null;

    public static CMFuwu getInstance() {
        // TODO Auto-generated method stub
        return new CMFuwu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.getInstance().mContent = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_fuwu, null);
        initView();

        return viewParent;
    }

    private void configTitle() {
        // TODO Auto-generated method stub
        TextView textView = ((TextView) viewParent
                .findViewById(R.id.tvHeaderTitle));
        Drawable drawable = getResources().getDrawable(R.drawable.location);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        // String adsf = preference.getDefautlCityInfo().getsName();
        // String locationCity = preference.getLocationCityInfo().getsName();
        // if (adsf.equals("唐山")) {
        // if (!Util.isEmpty(locationCity)) {
        // adsf = locationCity;
        // MCityInfo cityInfo = preference.getLocationCityInfo();
        // preference.setDefautlCityInfo(cityInfo);
        // }
        // }
        // textView.setText(adsf);
        // if (locationCity.equals(textView.getText().toString())) {
        // textView.setCompoundDrawables(drawable, null, null, null);
        // } else {
        //
        // }
        textView.setCompoundDrawables(null, null, null, null);

        ((ImageView) viewParent.findViewById(R.id.ivHeaderImageView))
                .setBackgroundResource(R.drawable.daohangicon);
        viewParent.findViewById(R.id.btBack).setVisibility(View.GONE);
        viewParent.findViewById(R.id.btShare).setVisibility(View.GONE);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);

    }

    private void initView() {

        configTitle();

        gvGridView = (GridView) viewParent.findViewById(R.id.gvGridView);
        list = new ArrayList<MCityInfo>();
        MCityInfo info = new MCityInfo();
        info.setsName("逐日预报");
        info.setsNum(R.drawable.tianqiyubao + "");
        list.add(info);
        info = null;
        MCityInfo info1 = new MCityInfo();
        info1.setsName("逐小时预报");
        info1.setsNum(R.drawable.zhuxiaoshi + "");
        list.add(info1);
        info1 = null;

        MCityInfo info11 = new MCityInfo();
        info11.setsName("天气实况");
        info11.setsNum(R.drawable.shikuang + "");
        list.add(info11);
        info11 = null;

        MCityInfo info2 = new MCityInfo();
        info2.setsName("气象预警");
        info2.setsNum(R.drawable.qixiang + "");
        list.add(info2);
        info2 = null;

        MCityInfo info3 = new MCityInfo();
        info3.setsName("生活气象");
        info3.setsNum(R.drawable.shenghuoqixiang + "");
        list.add(info3);
        info3 = null;
        MCityInfo info4 = new MCityInfo();
        info4.setsName("交通气象");
        info4.setsNum(R.drawable.jiaotongqixiang + "");
        list.add(info4);
        info4 = null;
        // MCityInfo info5 = new MCityInfo();
        // info5.setsName("农业气象");
        // info5.setsNum(R.drawable.nongyeqixiang + "");
        // list.add(info5);
        // info5 = null;
        MCityInfo info6 = new MCityInfo();
        info6.setsName("旅游气象");
        info6.setsNum(R.drawable.lvyouqixiang + "");
        list.add(info6);
        info6 = null;
//        MCityInfo info7 = new MCityInfo();
//        info7.setsName("世园会气象");
//        info7.setsNum(R.drawable.shiyuanhuiqixaing + "");
//        list.add(info7);
//        info7 = null;
        MCityInfo info8 = new MCityInfo();
        info8.setsName("气象科普");
        info8.setsNum(R.drawable.qixiangkepu + "");
        list.add(info8);
        info8 = null;
        FuwuAdapter adapter = new FuwuAdapter(context, list);
        gvGridView.setAdapter(adapter);
    }

}
