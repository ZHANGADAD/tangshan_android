package com.tangshan.gui;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.tangshan.gui.bean.AppReleaseInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.common.CommonWebFragment;
import com.tangshan.gui.ui.fuwu.CMFuwu;
import com.tangshan.gui.ui.settings.CMBuySettingFragment;
import com.tangshan.gui.ui.settings.CMSetting;
import com.tangshan.gui.ui.shiyuanhui.CMShiyuanhui;
import com.tangshan.gui.ui.tianqi.CMTianqi;
import com.tangshan.gui.util.ShowDialogUtil;
import com.tangshan.gui.util.Util;
import com.tangshan.gui.view.CMTableView;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {

    public Fragment mContent;

    private long tempTime = 0l;

    private static MainActivity activity;

    private CMTableView tableView;

    private TelephonyManager manager;

    private CMPreference preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MApplication.getInstance().initLocation();
        activity = this;
        manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        preference = new CMPreference(this);
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        if (mContent == null) {
            mContent = new CMTianqi();
        }
        setContentView(R.layout.main_activity);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.content_frame, mContent).commit();

        doVersionCheck();

        initUmeng();

        pushInfo();

        tableView = (CMTableView) findViewById(R.id.llTab);

    }


    private void doVersionCheck() {
        RequestParams params = new RequestParams();
        params.put("version", BuildConfig.VERSION_CODE);
        CMHttpClient.getInstance().get(Gloable.UPDATEURL, params,
                new CMHttpResponseHandler(false) {
                    @Override
                    public void onSuccess(int code, Header[] header, byte[] data) {
                        super.onSuccess(code, header, data);
                        try {
                            String consafdg = new String(data);
                            if (!Util.isEmpty(consafdg)) {
                                JSONObject jsonObject = new JSONObject(consafdg);
                                AppReleaseInfo info = new AppReleaseInfo();
                                info.setAppVersion(jsonObject.getString("versionName"));
                                info.setReleaseNote(jsonObject.getString("content"));
                                info.setDownloadUrl(jsonObject.getString("url"));
                                ShowDialogUtil.showV4UpdateDialog(MainActivity.this, info);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void pushInfo() {
        RequestParams params = new RequestParams();
        params.put("telno", getPhone());
        params.put("device_token", MApplication.getInstance().mPushAgent.getRegistrationId());
        params.put("latitude", preference.getLatitude());
        params.put("longitude", preference.getLongtitude());
        params.put("platform", "android");
        params.put("push", preference.getPush());
        params.put("qxzhyjpush", preference.getQixiang());
        params.put("tqybpush", preference.getTianqi());
        params.put("pushtimebegin", preference.getPushTimeStart());
        params.put("pushtimeend", preference.getPushTimeEnd());
        params.put("city", preference.getLocationCityInfo().getsName());
        CMHttpClient.getInstance().get(Gloable.INFOURL, params,
                new CMHttpResponseHandler(false));
    }

    private String getPhone() {
        // TODO Auto-generated method stub
        return manager.getLine1Number();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        MApplication.getInstance().stopLocation();
        super.onDestroy();

    }

    private void initUmeng() {
        MobclickAgent.setDebugMode(BuildConfig.LogDebug);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
        AnalyticsConfig.enableEncrypt(true);
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            getSupportFragmentManager().putFragment(outState, "mContent",
                    mContent);
        } catch (Exception e) {
            mContent = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            if (resultCode == Activity.RESULT_OK) {
                if (mContent instanceof CMBuySettingFragment) {
                    ((CMBuySettingFragment) mContent).showBuySuccess();
                }
            }
        } else {
            /** 使用SSO授权必须添加如下代码 */
            UMSsoHandler ssoHandler = null;
            if (mContent instanceof CMShiyuanhui) {
                ssoHandler = ((CMShiyuanhui) mContent).mController.getConfig()
                        .getSsoHandler(requestCode);
            } else if (mContent instanceof CMTianqi) {
                ssoHandler = ((CMTianqi) mContent).mController.getConfig()
                        .getSsoHandler(requestCode);
            }

            if (ssoHandler != null) {
                ssoHandler.authorizeCallBack(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (isMainFragment()) {
            if ((System.currentTimeMillis() - tempTime) > 2000) {
                Toast.makeText(this, getString(R.string.exit_info),
                        Toast.LENGTH_SHORT).show();
                tempTime = System.currentTimeMillis();

            } else {
                finish();
            }
        } else {
            if (mContent instanceof CommonWebFragment) {
                ((CommonWebFragment) mContent).goback();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }

    }

    private boolean isMainFragment() {
        // TODO Auto-generated method stub
        if (mContent instanceof CMShiyuanhui || mContent instanceof CMTianqi
                || mContent instanceof CMFuwu || mContent instanceof CMSetting) {

            return true;
        }
        return false;
    }

    public static MainActivity getInstance() {
        return activity;
    }

    public void hideTableView(boolean isShow, int index) {
        if (isShow) {
            tableView.setVisibility(View.GONE);
        } else {
            tableView.setVisibility(View.VISIBLE);
            tableView.changeTab(index);
        }
    }

    public void switchContent(Fragment fragment, boolean add2BackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        if (add2BackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
        mContent = fragment;
    }
}
