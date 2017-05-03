package com.tangshan.gui.ui.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.util.Util;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class CommonWebFragment extends BaseFragment implements OnClickListener {

    private int index;

    public CommonWebFragment(int index) {
        // TODO Auto-generated constructor stub
        this.index = index;
    }

    private TextView textViewTitle;
    private String url, title;
    private Context context;
    private WebView wvWebView;

    public final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Gloable.SHARRDESCRIPTOR);
    private CMPreference preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        preference = new CMPreference(context);
        if (index == 0) {
            // 旅游气象
            url = "http://wx.hbweather.com.cn/jy_module/tsapp/lyqx.php";
        } else if (index == 1) {
            // 世园会气象
            url = "http://wx.hbweather.com.cn/jy_module/tsapp/syh.php";
        } else if (index == 2) {
            // 气象科普
            url = "http://wx.hbweather.com.cn/index.php?g=Wap&m=Index&a=index&token=mqkxpl1436865077&diymenu=1";
        } else if (index == 3) {
            // n农业
            url = "http://wx.hbweather.com.cn/jy_module/tsapp/nyqx.php";
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.getInstance().mContent = this;
        checkeTitle();
    }

    private void checkeTitle() {
        if (index == 0) {
            // 旅游气象
            textViewTitle.setText("旅游气象");
        } else if (index == 1) {
            // 世园会气象
            textViewTitle.setText("世园会气象");
        } else if (index == 2) {
            // 气象科普
            textViewTitle.setText("气象科普");
        }

        textViewTitle.setCompoundDrawables(null, null, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewParent = inflater.inflate(R.layout.cm_pushshare_view, null);
        initView();

        return viewParent;
    }

    private void initView() {
        configTitle();

        wvWebView = (WebView) viewParent.findViewById(R.id.wvWebView);
        WebSettings settings = wvWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        String appCachePath = context.getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        wvWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        wvWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title111) {
                // TODO Auto-generated method stub
                super.onReceivedTitle(view, title111);
                if (!Util.isEmpty(title111)) {
                    title = title111;
                    configPlatforms();
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }
        });
        wvWebView.loadUrl(url);

    }

    public void goback() {
        if (wvWebView.canGoBack()) {
            wvWebView.goBack();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void configTitle() {
        // TODO Auto-generated method stub
        textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
        Drawable drawable = getResources().getDrawable(R.drawable.homda);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        textViewTitle.setCompoundDrawables(drawable, null, null, null);
        textViewTitle.setText(preference.getDefautlCityInfo().getsName());
        ((ImageView) viewParent.findViewById(R.id.ivHeaderImageView))
                .setBackgroundResource(R.drawable.back);
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
        viewParent.findViewById(R.id.btShare).setOnClickListener(this);
        viewParent.findViewById(R.id.btShare).setVisibility(View.GONE);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
    }

    private String getCommonUrl() {
        return url;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btShare:
                mController.openShare(getActivity(), false);
                break;
            case R.id.btBack:
                goback();
                break;
        }
    }

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        title = textViewTitle.getText().toString();
        // 添加新浪SSO授权
        addSinaPlatform();

        // 添加QQ、QZone平台
        addQQQZonePlatform();

        // 添加微信、微信朋友圈平台
        addWXPlatform();
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT);
    }

    private void addSinaPlatform() {
        // TODO Auto-generated method stub
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        SinaShareContent shareContent = new SinaShareContent();
        shareContent.setTitle(title);
        shareContent.setShareContent(title + getCommonUrl());
        mController.setShareMedia(shareContent);
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
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
        weixinContent.setTitle(title);
        // 设置分享内容跳转URL
        weixinContent.setTargetUrl(getCommonUrl());
        // 设置分享图片
        UMImage image = new UMImage(context, R.mipmap.ic_launcher);
        weixinContent.setShareImage(image);
        mController.setShareMedia(weixinContent);

        // 设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        // 设置朋友圈title
        circleMedia.setTitle(title);
        circleMedia.setShareImage(image);
        circleMedia.setTargetUrl(getCommonUrl());
        mController.setShareMedia(circleMedia);
    }

    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQQZonePlatform() {
        String appId = "1105035101";
        String appKey = "xWfsa0qhABhYvHq9";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId,
                appKey);
        qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
                appId, appKey);
        qZoneSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        // 设置分享title
        qqShareContent.setTitle(title);
        // 设置分享图片
        qqShareContent.setShareImage(new UMImage(context,
                R.mipmap.ic_launcher));
        // 设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl(getCommonUrl());
        mController.setShareMedia(qqShareContent);

        QZoneShareContent qzone = new QZoneShareContent();
        // 设置点击消息的跳转URL
        qzone.setTargetUrl(getCommonUrl());
        // 设置分享内容的标题
        qzone.setTitle(title);
        // 设置分享图片
        UMImage image = new UMImage(context, R.mipmap.ic_launcher);
        qzone.setShareImage(image);
        mController.setShareMedia(qzone);

    }

}
