package com.tangshan.gui.ui.shiyuanhui;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunzhisheng.common.USCError;
import cn.yunzhisheng.tts.online.basic.OnlineTTS;
import cn.yunzhisheng.tts.online.basic.TTSPlayerListener;
import cz.msebera.android.httpclient.Header;

import com.loopj.android.http.RequestParams;
import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.ui.common.CommonWebFragment;
import com.tangshan.gui.ui.tianqi.KongqiFragment;
import com.tangshan.gui.ui.tiqiyubao.QushiyubaoFragment;
import com.tangshan.gui.ui.wangri.WangtiPagerFragment;
import com.tangshan.gui.ui.yujing.YujingFragment;
import com.tangshan.gui.util.Util;
import com.tangshan.gui.view.CMKandianDialog;
import com.tangshan.gui.view.CMTianqiDetailsDialog;
import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class CMShiyuanhui extends BaseFragment implements OnClickListener {

    private TextView tvCondition, tvCity, tvWeek, tvDay, tvQiya, tvFeng,
            tvTempture, tvQingyin;
    private OnlineTTS mTTSPlayer;

    private int[] dayTextView = new int[]{R.id.tvDay1, R.id.tvDay2,
            R.id.tvDay3, R.id.tvDay4, R.id.tvDay5, R.id.tvDay6};
    private int[] iconView = new int[]{R.id.ivIcon1, R.id.ivIcon2,
            R.id.ivIcon3, R.id.ivIcon4, R.id.ivIcon5, R.id.ivIcon6};

    private int[] dayTextViewN = new int[]{R.id.tvDay1N, R.id.tvDay2N,
            R.id.tvDay3N, R.id.tvDay4N, R.id.tvDay5N, R.id.tvDay6N};
    private int[] iconViewN = new int[]{R.id.ivIcon1N, R.id.ivIcon2N,
            R.id.ivIcon3N, R.id.ivIcon4N, R.id.ivIcon5N, R.id.ivIcon6N};

    private int[] tempTextView = new int[]{R.id.tvTemp1, R.id.tvTemp2,
            R.id.tvTemp3, R.id.tvTemp4, R.id.tvTemp5, R.id.tvTemp6};

    private CMTianqiDetailsDialog detailsDialog = null;

    private boolean isTanhao = false, isPlay = false;

    private String afasfsf, dasfafs;

    public final UMShakeService mShakeController = UMShakeServiceFactory
            .getShakeService(Gloable.SHARRDESCRIPTOR);

    public final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Gloable.SHARRDESCRIPTOR, RequestType.SOCIAL);
    private String shareTitle;

    private TextView textViewTitle;
    private CMKandianDialog kandianDialog;
    private CMPreference preference;

    public static CMShiyuanhui getInstance() {
        // TODO Auto-generated method stub
        return new CMShiyuanhui();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.getInstance().mContent = this;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mTTSPlayer.stop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        preference = new CMPreference(context);
        // List<MCityInfo> afasf = preference.getChooseCityList();
        // boolean isContain = false;
        // for (MCityInfo info : afasf) {
        // if (info.getsNum().equals("54534")) {
        // isContain = true;
        // }
        // }
        //
        // if (!isContain) {
        // MCityInfo object = new MCityInfo();
        // object.setsName("唐山");
        // object.setsNum("54534");
        // afasf.add(object);
        // preference.setChooseCityList(afasf);
        // }
        initTts();
    }

    private void initTts() {
        // TODO Auto-generated method stub
        // 初始化语音合成对象
        mTTSPlayer = OnlineTTS.getInstance(context, Gloable.appKey);
        mTTSPlayer.setTTSListener(new TTSPlayerListener() {

            @Override
            public void onPlayEnd() {
                // TODO Auto-generated method stub
                isPlay = false;
            }

            @Override
            public void onPlayBegin() {
                // TODO Auto-generated method stub
                isPlay = true;
            }

            @Override
            public void onEnd(USCError arg0) {
                // TODO Auto-generated method stub
                isPlay = false;
            }

            @Override
            public void onBuffer() {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.shiyuanhui, null);
        initView();
        return viewParent;
    }

    private void getData() {
        RequestParams params = new RequestParams();
        params.put("stationNum", "50000");
        CMHttpClient.getInstance().get(Gloable.KONGQIZHILIANG, params,
                new CMHttpResponseHandler(false) {
                    @Override
                    public void onSuccess(int code, Header[] header, byte[] data) {
                        // TODO Auto-generated method stub
                        super.onSuccess(code, header, data);
                        try {
                            String consafdg = new String(data);
                            if (!Util.isEmpty(consafdg)) {
                                JSONObject jsonObject = new JSONObject(consafdg);
                                if (jsonObject.has("AQI_class")) {
                                    dasfafs = jsonObject.getString("AQI_class");
                                    if (dasfafs.length() > 3) {
                                        tvCondition.setTextSize(
                                                TypedValue.COMPLEX_UNIT_SP, 10);
                                    }
                                    tvCondition.setText(dasfafs);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int code, Header[] header,
                                          byte[] data, Throwable throwable) {
                        // TODO Auto-generated method stub
                        super.onFailure(code, header, data, throwable);
                    }

                });

        CMHttpClient.getInstance().get(Gloable.YUJINGURL, params,
                new CMHttpResponseHandler(false) {
                    @Override
                    public void onSuccess(int code, Header[] header, byte[] data) {
                        // TODO Auto-generated method stub
                        super.onSuccess(code, header, data);
                        try {
                            String consafdg = new String(data);
                            if (!Util.isEmpty(consafdg)) {
                                JSONObject jsonObject = new JSONObject(consafdg);
                                if (jsonObject.has("alarm")) {
                                    JSONArray array = jsonObject
                                            .getJSONArray("alarm");
                                    if (array.length() > 0) {
                                        isTanhao = true;
                                        Drawable drawable = context
                                                .getResources().getDrawable(
                                                        R.drawable.tanhao);
                                        drawable.setBounds(0, 0,
                                                drawable.getIntrinsicWidth(),
                                                drawable.getIntrinsicHeight());
                                        tvCity.setCompoundDrawables(null, null,
                                                drawable, null);
                                    }
                                }

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int code, Header[] header,
                                          byte[] data, Throwable throwable) {
                        // TODO Auto-generated method stub
                        super.onFailure(code, header, data, throwable);
                    }

                });

        String aff = preference.getShiyuanhui();
        if (!Util.isEmpty(aff) && !aff.contains("\"stationName\":null")) {
            initWithData(aff);
            return;
        }
        showDialog(context);
        CMHttpClient.getInstance().get(Gloable.TIANQIUTL, params,
                new CMHttpResponseHandler(true) {
                    @Override
                    public void onSuccess(int code, Header[] header, byte[] data) {
                        // TODO Auto-generated method stub
                        super.onSuccess(code, header, data);
                        dismissDialog();
                        String consafdg = new String(data);
                        if (!Util.isEmpty(consafdg)) {
                            preference.setShiyuanhui(consafdg);
                            initWithData(consafdg);
                        }
                    }

                    @Override
                    public void onFailure(int code, Header[] header,
                                          byte[] data, Throwable throwable) {
                        // TODO Auto-generated method stub
                        super.onFailure(code, header, data, throwable);
                        dismissDialog();
                    }

                });

    }

    protected void initWithData(String consafdg) {
        try {
            afasfsf = consafdg;
            JSONObject jsonObject = new JSONObject(consafdg);
            MCityInfo cityInfo = new MCityInfo();
            cityInfo.setsNum(jsonObject.getJSONObject("basic").getString(
                    "stationNum"));
            String cith = jsonObject.getJSONObject("basic").getString(
                    "stationName");
            cityInfo.setsName(cith);
            JSONArray daily_forecast = jsonObject
                    .getJSONArray("daily_forecast");
            tvCity.setText(cith);
            tvWeek.setText(Util.getCurrentWeek());
            tvDay.setText(Util.getCurrentDay());
            tvTempture
                    .setText(jsonObject.getJSONObject("now").getString("tmp"));
            tvQingyin.setText(daily_forecast.getJSONObject(0)
                    .getString("txt_d"));
            tvFeng.setText(jsonObject.getJSONObject("now").getString(
                    "wind_dir_txt")
                    + jsonObject.getJSONObject("now").getString("wind_sc")
                    + "级 湿度"
                    + jsonObject.getJSONObject("now").getString("hum")
                    + "%");

            tvQiya.setText("气压:"
                    + jsonObject.getJSONObject("now").getString("pres") + "hpa");

            setTextWithContent(0, jsonObject.getJSONObject("yestoday"));

            for (int i = 1; i < 6; i++) {
                setTextWithContent(i, jsonObject.getJSONArray("daily_forecast")
                        .getJSONObject(i - 1));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setTextWithContent(int index, JSONObject jsonObject) {
        // TODO Auto-generated method stub
        try {
            TextView textView = (TextView) viewParent
                    .findViewById(dayTextView[index]);
            String day = jsonObject.getString("date");
            String week = Util.getWeekByDayString(day);
            String txt_d = jsonObject.getString("txt_d");
            String txt_n = jsonObject.getString("txt_n");
            String afasf = txt_d;
            SpannableString spannableString = new SpannableString(week + "\n"
                    + afasf);
            spannableString.setSpan(new AbsoluteSizeSpan(12, true),
                    week.length(), week.length() + afasf.length() + 1,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);

            TextView textViewS = (TextView) viewParent
                    .findViewById(dayTextViewN[index]);
            SpannableString spannableString2 = new SpannableString(txt_n);
            spannableString2.setSpan(new AbsoluteSizeSpan(12, true), 0,
                    txt_n.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewS.setText(spannableString2);

            ImageView IMAGEAe = (ImageView) viewParent
                    .findViewById(iconView[index]);
            Util.setQingYinImage(IMAGEAe, txt_d, true);

            ImageView IMAGEAdddde = (ImageView) viewParent
                    .findViewById(iconViewN[index]);
            Util.setQingYinImage(IMAGEAdddde, txt_n, false);

            TextView tempTextVieadAsdw = (TextView) viewParent
                    .findViewById(tempTextView[index]);
            String adasf = jsonObject.getString("min_tmp") + "/"
                    + jsonObject.getString("max_tmp");
            Util.setTempreTure(adasf, tempTextVieadAsdw);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void configTitle() {

        ImageView imageView = (ImageView) viewParent
                .findViewById(R.id.ivHeaderImageView);
        imageView.setBackgroundResource(R.drawable.daohangicon);

        textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
        textViewTitle.setCompoundDrawables(null, null, null, null);
        textViewTitle.setText("唐山");
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
        viewParent.findViewById(R.id.btShare).setOnClickListener(this);
        viewParent.findViewById(R.id.btShare).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(
                View.VISIBLE);
    }

    private void initView() {
        // TODO Auto-generated method stub
        configTitle();
        viewParent.findViewById(R.id.lll).setBackgroundResource(
                R.drawable.shiyuanhui);

        viewParent.findViewById(R.id.dasffff).setOnClickListener(this);

        viewParent.findViewById(R.id.ivHome).setBackgroundResource(
                R.drawable.shiyuanhuicon);

        tvCondition = (TextView) viewParent.findViewById(R.id.tvCondition);
        tvCity = (TextView) viewParent.findViewById(R.id.tvCity);
        tvDay = (TextView) viewParent.findViewById(R.id.tvDay);
        tvWeek = (TextView) viewParent.findViewById(R.id.tvWeek);

        viewParent.findViewById(R.id.tvCity).setOnClickListener(this);
        viewParent.findViewById(R.id.tvDay).setOnClickListener(this);
        viewParent.findViewById(R.id.tvWeek).setOnClickListener(this);

        tvQiya = (TextView) viewParent.findViewById(R.id.tvQiya);
        tvFeng = (TextView) viewParent.findViewById(R.id.tvFeng);
        tvTempture = (TextView) viewParent.findViewById(R.id.tvTempture);
        tvQingyin = (TextView) viewParent.findViewById(R.id.tvQingYin);

        viewParent.findViewById(R.id.llDay).setOnClickListener(this);
        viewParent.findViewById(R.id.llIcon).setOnClickListener(this);
        viewParent.findViewById(R.id.llTemp).setOnClickListener(this);

        viewParent.findViewById(R.id.tvNext).setOnClickListener(this);
        viewParent.findViewById(R.id.ivHome).setOnClickListener(this);
        viewParent.findViewById(R.id.ivZhexian).setOnClickListener(this);
        viewParent.findViewById(R.id.ivYuyin).setOnClickListener(this);
        View view = viewParent.findViewById(R.id.tvKandian);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(this);

        getData();
    }

    protected void configShare(Bitmap bitmap) {
        shareTitle = "唐山气象";
        // 添加新浪SSO授权
        addSinaPlatform(bitmap);

        // 添加QQ、QZone平台
        addQQQZonePlatform(bitmap);

        // 添加微信、微信朋友圈平台
        addWXPlatform(bitmap);

        addDuanXin();

        mController.getConfig().removePlatform(SHARE_MEDIA.QZONE);

        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
                SHARE_MEDIA.TENCENT, SHARE_MEDIA.SMS);
    }

    private void addDuanXin() {
        // TODO Auto-generated method stub
        try {
            SmsHandler smsHandler = new SmsHandler();
            smsHandler.addToSocialSDK();
            SmsShareContent sms = new SmsShareContent();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("唐山 ");
            JSONObject jsonObject = new JSONObject(afasfsf);
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
                stringBuilder.append(time).append(day).append("," + tempday);
            }
            sms.setShareContent(stringBuilder.toString());
            mController.setShareMedia(sms);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addSinaPlatform(Bitmap bitmap) {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.setShareMedia(new UMImage(context, bitmap));
        mController.setShareContent(shareTitle);
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

        // // 添加QZone平台
        // QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
        // appId, appKey);
        // qZoneSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setTitle(shareTitle);
        // 设置分享图片
        qqShareContent.setShareImage(new UMImage(context, bitmap));
        mController.setShareMedia(qqShareContent);

        // QZoneShareContent qzone = new QZoneShareContent();
        // // 设置点击消息的跳转URL
        // qzone.setTitle(shareTitle);
        // qzone.setShareContent(shareTitle);
        // // 设置分享图片
        // UMImage image = new UMImage(context, bitmap);
        // qzone.setShareImage(image);
        // mController.setShareMedia(qzone);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btShare:
                startShare();
                break;
            case R.id.tvNext:
                showContentDetais();
                break;
            case R.id.ivYuyin:
                startYuyin();
                break;
            case R.id.ivZhexian:
                startZhexian();
                break;
            case R.id.ivHome:
                startHome();
                break;
            case R.id.tvCity:
            case R.id.tvDay:
            case R.id.tvWeek:
                // 预警
                if (isTanhao) {
                    if (context instanceof MainActivity) {
                        YujingFragment fragment = new YujingFragment();
                        Bundle args = new Bundle();
                        MCityInfo info = new MCityInfo();
                        info.setsName("唐山");
                        info.setsNum("50000");
                        args.putSerializable("city", info);
                        fragment.setArguments(args);
                        ((MainActivity) context).switchContent(fragment, true);
                    }

                }
                break;
            case R.id.tvKandian:
                showKandian();
                break;
            case R.id.llIcon:
            case R.id.llTemp:
            case R.id.llDay:
                if (context instanceof MainActivity) {
                    QushiyubaoFragment fragment = new QushiyubaoFragment();
                    Bundle args = new Bundle();
                    MCityInfo info = new MCityInfo();
                    info.setsName("世园会");
                    info.setsNum("50000");
                    args.putSerializable("city", info);
                    fragment.setArguments(args);
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            case R.id.dasffff:
                if (context instanceof MainActivity) {
                    KongqiFragment fragment = new KongqiFragment();
                    Bundle args = new Bundle();
                    MCityInfo info = new MCityInfo();
                    info.setsName("唐山");
                    info.setsNum("50000");
                    args.putSerializable("city", info);
                    fragment.setArguments(args);
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            default:
                break;
        }
    }

    protected void showKandian() {
        if (kandianDialog != null)
            kandianDialog = null;
        kandianDialog = new CMKandianDialog(context, afasfsf);
        kandianDialog.show();

    }

    private void startHome() {
        // TODO Auto-generated method stub
        if (context instanceof MainActivity) {
            CommonWebFragment fragment = new CommonWebFragment(1);
            ((MainActivity) context).switchContent(fragment, true);
        }
    }

    private void startZhexian() {
        if (context instanceof MainActivity) {
            WangtiPagerFragment fragment = new WangtiPagerFragment(false);
            Bundle args = new Bundle();
            MCityInfo info = new MCityInfo();
            info.setsName("唐山");
            info.setsNum("50000");
            args.putSerializable("city", info);
            fragment.setArguments(args);
            ((MainActivity) context).switchContent(fragment, true);
        }
    }

    private void startYuyin() {
        // TODO Auto-generated method stub
        try {
            if (isPlay) {
                isPlay = false;
                mTTSPlayer.stop();
                return;
            }

            StringBuilder content = new StringBuilder();
            content.append("气温:");
            JSONObject jsonObject = new JSONObject(afasfsf);
            String adaf = jsonObject.getJSONObject("now").getString("tmp");
            if (Float.parseFloat(adaf) < 0) {
                content.append("零下").append(adaf.substring(1, adaf.length()));
            } else {
                content.append(adaf);
            }
            content.append("度:");
            JSONArray daily_forecast = jsonObject
                    .getJSONArray("daily_forecast");
            String dfdff = daily_forecast.getJSONObject(0).getString("txt_n");
            String adfsff = daily_forecast.getJSONObject(0).getString("txt_d");
            if (!dfdff.equals(adfsff)) {
                adfsff = (adfsff + "转" + dfdff);
            }
            content.append(adfsff);
            content.append(":"
                    + jsonObject.getJSONObject("now").getString("wind_dir_txt")
                    + jsonObject.getJSONObject("now").getString("wind_sc")
                    + "级:湿度:百分之"
                    + jsonObject.getJSONObject("now").getString("hum"));
            content.append(":").append(
                    "气压:" + jsonObject.getJSONObject("now").getString("pres")
                            + "百帕");
            mTTSPlayer.play(content.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void startShare() {
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

    protected void showContentDetais() {
        if (detailsDialog != null) {
            detailsDialog = null;
        }
        detailsDialog = new CMTianqiDetailsDialog(context, afasfsf, dasfafs, 1);
        detailsDialog.show();
    }
}
