package com.tangshan.gui;

import android.app.Application;
import android.graphics.Bitmap.Config;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.util.CMLog;
import com.tangshan.gui.util.Util;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.List;

public class MApplication extends Application {

    private static MApplication instance;
    public PushAgent mPushAgent;
    public LocationService locationService;
    private CMPreference preference;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preference = new CMPreference(this);
        initUmeng();
        initImageLoader();

    }

    public void initLocation() {
        SDKInitializer.initialize(getApplicationContext());
        locationService = new LocationService(getApplicationContext());
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.registerListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // Receive Location
                CMLog.i("TAG", "====d=d=d=d==d========" + location.getLocTypeDescription());
                String city = location.getCity();
                if (Util.isEmpty(city))
                    return;
                locationService.stop();
                preference.setLatitude(location.getLatitude() + "");
                preference.setLongtitude(location.getLongitude() + "");
                if (location.getProvince().contains("河北")) {
                    String adf = location.getDistrict();
                    if (adf.contains("区")) {
                        int index = city.indexOf("市");
                        city = city.substring(0, index);
                        saveDefaultCity(city);
                    } else {
                        saveHeibei(adf);
                    }
                } else {
                    if (!Util.isEmpty(city)) {
                        if (city.contains("北京") || city.contains("重庆")
                                || city.contains("天津") || city.contains("重庆")
                                || city.contains("上海")) {
                            int index = city.indexOf("市");
                            city = city.substring(0, index);
                        } else if (city.contains("市")) {
                            int index = city.indexOf("市");
                            city = city.substring(0, index);
                        }
                        saveDefaultCity(city);
                    }
                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        locationService.start();
    }

    protected void saveDefaultCity(final String city) {
        List<MCityInfo> cityInfos = Util.getCityList();
        for (MCityInfo mCityInfo : cityInfos) {
            if (mCityInfo.getsName().contains(city)) {
                preference.setLocationCityInfo(mCityInfo);
            }
        }
    }

    protected void saveHeibei(String city) {
        List<MCityInfo> cityInfos = Util.getHbeiCityList();
        for (MCityInfo mCityInfo : cityInfos) {
            String asddddd = mCityInfo.getsName() + "县";
            if (asddddd.contains(city)) {
                preference.setLocationCityInfo(mCityInfo);
            }
        }
    }

    public void stopLocation() {
        if (locationService != null) {
            locationService.stop();
        }
    }

    public void startLocation() {
        if (locationService == null) {
            initLocation();
        } else {
            locationService.start();
        }
    }

    private void initUmeng() {

        FeedbackPush.getInstance(this).init(false);
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    private void initImageLoader() {
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .bitmapConfig(Config.RGB_565).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                MApplication.this)
                .diskCache(new UnlimitedDiskCache(Gloable.FILE_IMAGE_PATH))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(8 * 1024 * 1024)
                .defaultDisplayImageOptions(defaultDisplayImageOptions).build();
        ImageLoader.getInstance().init(configuration);
    }

    public static MApplication getInstance() {
        return instance;
    }
}
