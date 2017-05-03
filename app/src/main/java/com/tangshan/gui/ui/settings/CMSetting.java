package com.tangshan.gui.ui.settings;

import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.umeng.fb.FeedbackAgent;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

public class CMSetting extends BaseFragment implements OnClickListener {

    private CMPreference preference;
    private ToggleButton gpsToggle;

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
        super.onResume();
        MainActivity.getInstance().mContent = this;
        MainActivity.getInstance().pushInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_settings, null);
        initView();

        return viewParent;
    }

    private void initView() {
        configTitle();
        viewParent.findViewById(R.id.llPush).setOnClickListener(this);
        viewParent.findViewById(R.id.llBuy).setOnClickListener(this);
        viewParent.findViewById(R.id.llClear).setOnClickListener(this);
        viewParent.findViewById(R.id.llCheck).setOnClickListener(this);
        viewParent.findViewById(R.id.llFeedBack).setOnClickListener(this);
        gpsToggle = (ToggleButton) viewParent.findViewById(R.id.gpsToggle);
        if (preference.getGps() == 1) {
            gpsToggle.setToggleOn();
        } else {
            gpsToggle.setToggleOff();
        }
        gpsToggle.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                // TODO Auto-generated method stub
                if (on) {
                    preference.setGps(1);
                } else {
                    preference.setGps(0);
                }
            }
        });
    }

    private void configTitle() {
        // TODO Auto-generated method stub
        TextView textView = (TextView) viewParent
                .findViewById(R.id.tvHeaderTitle);
        textView.setText("设置");
        textView.setCompoundDrawables(null, null, null, null);
        ((ImageView) viewParent.findViewById(R.id.ivHeaderImageView))
                .setBackgroundResource(R.drawable.daohangicon);
        viewParent.findViewById(R.id.btBack).setVisibility(View.GONE);
        viewParent.findViewById(R.id.btShare).setVisibility(View.GONE);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
    }

    public static CMSetting getInstance() {
        // TODO Auto-generated method stub
        return new CMSetting();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissDialog();
            showToast(context, "缓存清除完毕!");
        }

    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.llPush:
                if (context instanceof MainActivity) {
                    CMPushSettingFragment fragment = new CMPushSettingFragment();
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            case R.id.llBuy:
                if (context instanceof MainActivity) {
                    CMBuySettingFragment fragment = new CMBuySettingFragment();
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            case R.id.llClear:
                showDialog(context);
                Random random = new Random();
                handler.sendEmptyMessageDelayed(0, random.nextInt(8000));
                break;
            case R.id.llCheck:
                showBuySuccess();
                break;
            case R.id.llFeedBack:
                FeedbackAgent agent = new FeedbackAgent(context);
                agent.startFeedbackActivity();
                break;
            default:
                break;
        }
    }

    public void showBuySuccess() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setMessage("当前版本为最新版本!").setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });
        alertDialog.show();

    }

}
