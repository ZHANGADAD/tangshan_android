package com.tangshan.gui.ui.tiqiyubao;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.util.Util;
import com.tangshan.gui.view.CirclePageIndicator;

public class QushiyubaoFragment extends BaseFragment {

    private TextView textViewTitle;
    private ViewPager viewPager;
    private CirclePageIndicator pageIndicator;
    private List<MCityInfo> infos;
    private QushiAdapter imagePagerAdapter;
    private CMPreference preference;
    private boolean isShiyuanhui = false;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(msg.what, true);
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("city")) {
            isShiyuanhui = true;
        } else {
            isShiyuanhui = false;
        }
        preference = new CMPreference(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_tianqi, null);
        initView();

        return viewParent;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        if (!isShiyuanhui) {
            String chooseCity = textViewTitle.getText().toString();
            List<MCityInfo> cityInfos = new ArrayList<MCityInfo>();
            cityInfos.add(preference.getLocationCityInfo());
            cityInfos.addAll(preference.getChooseCityList());
            int index = -1;
            for (int i = 0; i < cityInfos.size(); i++) {
                MCityInfo info = cityInfos.get(i);
                if (info.getsName().equals(chooseCity)) {
                    index = i;
                }
            }
            if (index != -1) {
                preference.setDefautlCityInfo(cityInfos.get(index));
            }
        }
        super.onPause();
    }

    private void initView() {
        // TODO Auto-generated method stub
        configTitle();
        viewPager = (ViewPager) viewParent.findViewById(R.id.viewPager);
        pageIndicator = (CirclePageIndicator) viewParent
                .findViewById(R.id.pageIndicator);
        initWithData();
    }

    private void initWithData() {
        infos = new ArrayList<MCityInfo>();
        if (isShiyuanhui) {
            MCityInfo info = new MCityInfo();
            info.setsName("世园会");
            info.setsNum("50000");
            infos.add(info);
        } else {
            infos.add(preference.getLocationCityInfo());
            infos.addAll(preference.getChooseCityList());
        }

        imagePagerAdapter = new QushiAdapter(getChildFragmentManager(),
                QushiyubaoFragment.this, infos);
        viewPager.setAdapter(imagePagerAdapter);
        pageIndicator.setCentered(false);
        pageIndicator.setInLoop(false);
        pageIndicator.setViewPager(viewPager);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                // TODO Auto-generated method stub
                String name = infos.get(index).getsName();
                String locationCity = preference.getLocationCityInfo()
                        .getsName();
                // String defaldaf = preference.getDefautlCityInfo().getsName();
                // if (locationCity.equals(defaldaf)
                // && (defaldaf.equals(locationCity))) {
                // Drawable drawable = getResources().getDrawable(
                // R.drawable.homda);
                // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                // drawable.getIntrinsicHeight());
                // textViewTitle.setCompoundDrawables(null, null, null, null);
                // } else if (defaldaf.equals(name)) {
                // Drawable drawable = getResources().getDrawable(
                // R.drawable.homda);
                // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                // drawable.getIntrinsicHeight());
                // textViewTitle.setCompoundDrawables(drawable, null, null,
                // null);
                // } else
                if (locationCity.equals(name)) {
                    if (isShiyuanhui) {
                        textViewTitle.setCompoundDrawables(null, null, null,
                                null);
                    } else {
                        Drawable drawable = getResources().getDrawable(
                                R.drawable.location);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                                drawable.getIntrinsicHeight());
                        textViewTitle.setCompoundDrawables(drawable, null,
                                null, null);
                    }

                } else {
                    textViewTitle.setCompoundDrawables(null, null, null, null);
                }

                textViewTitle.setText(name);
                pageIndicator.onPageSelected(index);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        String adf = preference.getDefautlCityInfo().getsName();
        if (!Util.isEmpty(adf)) {
            List<MCityInfo> cityInfos = new ArrayList<MCityInfo>();
            cityInfos.add(preference.getLocationCityInfo());
            cityInfos.addAll(preference.getChooseCityList());
            int index = 0;
            for (int i = 0; i < cityInfos.size(); i++) {
                MCityInfo info = cityInfos.get(i);
                if (info.getsName().equals(adf)) {
                    index = i;
                }
            }
            handler.sendEmptyMessage(index);
        }

    }

    private void configTitle() {

        ImageView imageView = (ImageView) viewParent
                .findViewById(R.id.ivHeaderImageView);
        imageView.setBackgroundResource(R.drawable.daohangicon);

        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
        textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        getFragmentManager().popBackStack();
                    }
                });
        checkeTitle();
    }

    protected void checkeTitle() {
        // TODO Auto-generated method stub
        if (isShiyuanhui) {
            textViewTitle.setText("世园会");
            textViewTitle.setCompoundDrawables(null, null, null, null);
        } else {
            String nadad = preference.getLocationCityInfo().getsName();
            textViewTitle.setText(nadad);
            Drawable drawable = getResources().getDrawable(R.drawable.location);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            textViewTitle.setCompoundDrawables(drawable, null, null, null);
        }

    }
}
