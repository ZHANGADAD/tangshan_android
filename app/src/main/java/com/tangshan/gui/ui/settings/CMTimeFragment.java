package com.tangshan.gui.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;

public class CMTimeFragment extends BaseFragment implements OnClickListener {

    private CMPreference preference;
    private TimePicker leftTimer, rightTimer;

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
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        String hourStartSS = "0";
        String minStartSS = "0";
        int hourStart = leftTimer.getCurrentHour();
        if (hourStart < 10) {
            hourStartSS = "0" + hourStart;
        } else {
            hourStartSS = hourStart + "";
        }
        int minStart = leftTimer.getCurrentMinute();
        if (minStart < 10) {
            minStartSS = "0" + minStart;
        } else {
            minStartSS = minStart + "";
        }
        preference.setPushTimeStart(hourStartSS + minStartSS);

        String hourEndSS = "0";
        String minEndSS = "0";
        int hourEnd = rightTimer.getCurrentHour();
        if (hourEnd < 10) {
            hourEndSS = "0" + hourEnd;
        } else {
            hourEndSS = hourEnd + "";
        }
        int minEnd = rightTimer.getCurrentMinute();
        if (minEnd < 10) {
            minEndSS = "0" + minEnd;
        } else {
            minEndSS = minEnd + "";
        }
        preference.setPushTimeEnd(hourEndSS + minEndSS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_settings_push_time, null);
        initView();

        return viewParent;
    }

    private void configTitle() {
        // TODO Auto-generated method stub
        TextView textView = (TextView) viewParent
                .findViewById(R.id.tvHeaderTitle);
        textView.setText("时间设置");
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
        leftTimer = (TimePicker) viewParent.findViewById(R.id.leftTimer);
        leftTimer.setIs24HourView(true);
        leftTimer.setCurrentHour(7);
        leftTimer.setCurrentMinute(0);
        rightTimer = (TimePicker) viewParent.findViewById(R.id.rightTimer);
        rightTimer.setIs24HourView(true);
        rightTimer.setCurrentHour(22);
        rightTimer.setCurrentMinute(0);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btBack:
                getFragmentManager().popBackStack();
                break;
            default:
                break;
        }
    }

}
