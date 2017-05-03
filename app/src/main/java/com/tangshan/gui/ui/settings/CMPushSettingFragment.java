package com.tangshan.gui.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

public class CMPushSettingFragment extends BaseFragment implements
        OnClickListener {

    private CMPreference preference;
    private TextView tvTime;
    private ToggleButton tianqiToggle, qixiangToggle, pushToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        preference = new CMPreference(context);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        MainActivity.getInstance().mContent = this;
        String startTime = preference.getPushTimeStart();
        String ada = startTime.substring(0, 2);
        String adadd = startTime.substring(2, 4);
        tvTime.setText(ada + ":" + adadd + "~"
                + preference.getPushTimeEnd().substring(0, 2) + ":"
                + preference.getPushTimeEnd().subSequence(2, 4));
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_settings_push, null);
        initView();

        return viewParent;
    }

    private void configTitle() {
        // TODO Auto-generated method stub
        TextView textView = (TextView) viewParent
                .findViewById(R.id.tvHeaderTitle);
        textView.setText("推送设置");
        textView.setCompoundDrawables(null, null, null, null);

        ((ImageView) viewParent.findViewById(R.id.ivHeaderImageView))
                .setBackgroundResource(R.drawable.back);
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
        viewParent.findViewById(R.id.btShare).setVisibility(View.GONE);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
    }

    private void initView() {
        configTitle();
        viewParent.findViewById(R.id.llTime).setOnClickListener(this);
        tvTime = (TextView) viewParent.findViewById(R.id.tvTime);
        tianqiToggle = (ToggleButton) viewParent
                .findViewById(R.id.tianqiToggle);
        tianqiToggle.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                // TODO Auto-generated method stub
                if (on) {
                    preference.setTianqi(1);
                } else {
                    preference.setTianqi(0);
                }
            }
        });

        if (preference.getTianqi() == 1) {
            tianqiToggle.setToggleOn();
        } else {
            tianqiToggle.setToggleOff();
        }

        qixiangToggle = (ToggleButton) viewParent
                .findViewById(R.id.qixiangToggle);
        qixiangToggle.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                // TODO Auto-generated method stub
                if (on) {
                    preference.setQixiang(1);
                } else {
                    preference.setQixiang(0);
                }
            }
        });
        if (preference.getQixiang() == 1) {
            qixiangToggle.setToggleOn();
        } else {
            qixiangToggle.setToggleOff();
        }
        pushToggle = (ToggleButton) viewParent.findViewById(R.id.pushToggle);
        pushToggle.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                // TODO Auto-generated method stub
                if (on) {
                    preference.setPush(1);
                } else {
                    preference.setPush(0);
                }
            }
        });
        if (preference.getPush() == 1) {
            pushToggle.setToggleOn();
        } else {
            pushToggle.setToggleOff();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btBack:
                getFragmentManager().popBackStack();
                break;
            case R.id.llTime:
                if (context instanceof MainActivity) {
                    CMTimeFragment fragment = new CMTimeFragment();
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            default:
                break;
        }
    }

}
