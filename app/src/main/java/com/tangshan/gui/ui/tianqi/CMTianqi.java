package com.tangshan.gui.ui.tianqi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
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

import cn.yunzhisheng.tts.online.basic.OnlineTTS;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.adapter.CMPagerAdapter;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.ui.city.OnCmbackListener;
import com.tangshan.gui.util.Util;
import com.tangshan.gui.view.CirclePageIndicator;
import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class CMTianqi extends BaseFragment implements OnClickListener,
        OnCmbackListener {

    private OnlineTTS mTTSPlayer;
    private CMPreference preference;

    public final UMShakeService mShakeController = UMShakeServiceFactory
            .getShakeService(Gloable.SHARRDESCRIPTOR);

    public final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Gloable.SHARRDESCRIPTOR, RequestType.SOCIAL);
    private String shareTitle;
    private TextView textViewTitle;
    private ViewPager viewPager;
    private CirclePageIndicator pageIndicator;
    private List<MCityInfo> infos;
    private CMPagerAdapter imagePagerAdapter;

    public static CMTianqi getInstance() {
        // TODO Auto-generated method stub
        return new CMTianqi();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.getInstance().mContent = this;
        // checkeTitle();
    }

    protected void checkeTitle() {
        // TODO Auto-generated method stub
        String nadad = preference.getLocationCityInfo().getsName();
        textViewTitle.setText(nadad);
        Drawable drawable = getResources().getDrawable(R.drawable.location);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        textViewTitle.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        mTTSPlayer.stop();
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
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        preference = new CMPreference(context);
        initTts();
    }

    private void initTts() {
        // 初始化语音合成对象
        mTTSPlayer = new OnlineTTS(context, Gloable.appKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewParent = inflater.inflate(R.layout.cm_tianqi, null);
        initView();
        return viewParent;
    }

    public void sasasas(String csff) {
        if (Util.isEmpty(csff))
            return;
        mTTSPlayer.play(csff);
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
        infos.add(preference.getLocationCityInfo());
        infos.addAll(preference.getChooseCityList());

        imagePagerAdapter = new CMPagerAdapter(getChildFragmentManager(),
                CMTianqi.this, infos);
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
//                 String defaldaf = preference.getDefautlCityInfo().getsName();
//                 if (locationCity.equals(defaldaf)
//                 && (defaldaf.equals(locationCity))) {
//                 Drawable drawable = getResources().getDrawable(
//                 R.drawable.homda);
//                 drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
//                 drawable.getIntrinsicHeight());
//                 textViewTitle.setCompoundDrawables(null, null, null, null);
//                 } else if (defaldaf.equals(name)) {
//                 Drawable drawable = getResources().getDrawable(
//                 R.drawable.homda);
//                 drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
//                 drawable.getIntrinsicHeight());
//                 textViewTitle.setCompoundDrawables(drawable, null, null,
//                 null);
//                 } else
                if (locationCity.equals(name)) {
                    Drawable drawable = getResources().getDrawable(
                            R.drawable.location);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight());
                    textViewTitle.setCompoundDrawables(drawable, null, null,
                            null);
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

        textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
        viewParent.findViewById(R.id.btShare).setOnClickListener(this);

        viewParent.findViewById(R.id.btShare).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(
                View.VISIBLE);
        checkeTitle();
    }

    protected void configShare(Bitmap bitmap) {

        // 添加新浪SSO授权
        addSinaPlatform(bitmap);

        // 添加QQ、QZone平台
        addQQQZonePlatform(bitmap);

        // 添加微信、微信朋友圈平台
        addWXPlatform(bitmap);

        addDuanXin();
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);

        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.SMS);
    }

    private void addDuanXin() {
        // TODO Auto-generated method stub
        try {
            String aff = preference.getTianqiContent(shareTitle);
            if (!Util.isEmpty(aff)) {
                SmsHandler smsHandler = new SmsHandler();
                smsHandler.addToSocialSDK();
                SmsShareContent sms = new SmsShareContent();

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(shareTitle + "  ");
                JSONObject jsonObject = new JSONObject(aff);
                JSONArray array = jsonObject.getJSONArray("daily_forecast");
                for (int i = 0; i <= 2; i++) {
                    JSONObject object = array.getJSONObject(i);
                    String time = "【"
                            + object.getString("date").substring(8,
                            object.getString("date").length()) + "日】";
                    String day = object.getString("txt_d");
                    String night = object.getString("txt_n");
                    if (!day.endsWith(night)) {
                        day = day + "转" + night;
                    }
                    String tempday = object.getString("min_tmp") + "~"
                            + object.getString("max_tmp") + "℃";
                    stringBuilder.append(time).append(day)
                            .append("," + tempday);
                }

                sms.setShareContent(stringBuilder.toString());
                mController.setShareMedia(sms);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addSinaPlatform(Bitmap bitmap) {
        // TODO Auto-generated method stub
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        SinaShareContent shareContent = new SinaShareContent();
        shareContent.setTitle(shareTitle);
        shareContent.setShareImage(new UMImage(context, bitmap));
        mController.setShareMedia(shareContent);
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform(Bitmap bitmap) {
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx3f9cee272a16a685";
        String appSecret = "108a9408960c98d00a8e41f2bf22ef78";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId,
                appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        // 设置title
        weixinContent.setTargetUrl("http://www.umeng.com/social");
        weixinContent.setTitle(shareTitle);
        // 设置分享图片
        UMImage image = new UMImage(context, bitmap);
        weixinContent.setShareImage(image);
        mController.setShareMedia(weixinContent);

        // 设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        // 设置朋友圈title
        circleMedia.setTargetUrl("http://www.umeng.com/social");
        circleMedia.setTitle(shareTitle);
        circleMedia.setShareImage(image);
        mController.setShareMedia(circleMedia);
    }

    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQQZonePlatform(Bitmap bitmap) {
        String appId = "1105035101";
        String appKey = "xWfsa0qhABhYvHq9";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId,
                appKey);
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
                appId, appKey);
        qZoneSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        // 设置分享title
        qqShareContent.setTitle(shareTitle);
        // 设置分享图片
        qqShareContent.setShareImage(new UMImage(context, bitmap));
        mController.setShareMedia(qqShareContent);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btShare:
                startShare();
                break;
            default:
                break;
        }
    }

    private void startShare() {
        shareTitle = textViewTitle.getText().toString();
        mShakeController.takeScrShot((Activity) context, new UMAppAdapter(
                (Activity) context), new OnScreenshotListener() {

            @Override
            public void onComplete(Bitmap bmp) {
                if (bmp != null) {
                    configShare(bmp);
                    mController.openShare((Activity) context, false);
                }
            }
        });
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(msg.what, true);
        }

    };

    @Override
    public void onCMBackItems(MCityInfo cityInfo) {
        // TODO Auto-generated method stub
        if (infos == null) {
            infos = new ArrayList<MCityInfo>();
        } else {
            infos.clear();
        }
        infos.add(preference.getLocationCityInfo());
        infos.addAll(preference.getChooseCityList());
        int index = 0;
        if (cityInfo != null) {
            for (int i = 0; i < infos.size(); i++) {
                MCityInfo info = infos.get(i);
                if (info.getsName().equals(cityInfo.getsName())) {
                    index = i;
                }
            }
        }
        imagePagerAdapter.notifyDataSetChanged();
        handler.sendEmptyMessage(index);
    }
}
