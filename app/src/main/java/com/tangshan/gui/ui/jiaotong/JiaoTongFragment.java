package com.tangshan.gui.ui.jiaotong;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.http.RequestParams;
import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.adapter.CityGridAdapter;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.bean.SearchCity;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.util.OverlayManager;
import com.tangshan.gui.util.Util;
import com.tangshan.gui.view.CMListDialog;

import cz.msebera.android.httpclient.Header;

public class JiaoTongFragment extends BaseFragment implements OnClickListener {

    private BaiduMap mBaidumap;
    private FragmentManager manager;
    private SupportMapFragment map;
    private CMPreference preference;
    private List<OverlayOptions> list;
    private List<MCityInfo> carDetails, zhanDetails;
    private TextView textViewTitle;
    private View mapTipView, llMap, llSearch;
    private TextView mapTitle, mapTitleContent;
    private ListView llList;
    private EditText etSearch;
    private CityGridAdapter adapterList;
    private List<MCityInfo> lvCityInfos;
    private CMListDialog dialog;
    private int indexTab;
    private List<SearchCity> listSearch;
    private int[] ddddd = new int[]{R.id.tvJiaoTong, R.id.tvTianqi,
            R.id.tvTianqiYubao};

    private BitmapDescriptor guanbiShow = BitmapDescriptorFactory
            .fromResource(R.drawable.guanbi11);

    private BitmapDescriptor guanbi = BitmapDescriptorFactory
            .fromResource(R.drawable.guanbis11);

    private BitmapDescriptor norrmm = BitmapDescriptorFactory
            .fromResource(R.drawable.adsaffs11);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        MapStatus ms = new MapStatus.Builder().overlook(0).zoom(14).build();
        BaiduMapOptions bo = new BaiduMapOptions().mapStatus(ms)
                .compassEnabled(false).zoomControlsEnabled(false);
        map = SupportMapFragment.newInstance(bo);
        manager = getChildFragmentManager();
        preference = new CMPreference(context);
        listSearch = Util.getSearchCityList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_jiaotong, null);
        initView();

        return viewParent;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.getInstance().mContent = this;
        if (llMap.getVisibility() == View.VISIBLE) {
            mBaidumap = map.getBaiduMap();
            mBaidumap.setMyLocationConfigeration(new MyLocationConfiguration(
                    LocationMode.NORMAL, true, null));
            mBaidumap.setMyLocationEnabled(true);
        }
        checkeTitle();
    }

    private void checkeTitle() {
        // TODO Auto-generated method stub
        textViewTitle.setText("交通气象");
        // String title = textViewTitle.getText().toString();
        // String nadad = preference.getDefautlCityInfo().getsName();
        // if (!title.equals(nadad)) {
        // textViewTitle.setText(nadad);
        // if (llMap.getVisibility() == View.VISIBLE)
        // getData(null);
        // }
    }

    private void configTitle() {
        textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
        Drawable drawable = getResources().getDrawable(R.drawable.homda);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        textViewTitle.setCompoundDrawables(null, null, null, null);
        textViewTitle.setText("交通气象");
        (viewParent.findViewById(R.id.ivHeaderImageView))
                .setBackgroundResource(R.drawable.back);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
    }

    private void initView() {
        configTitle();
        llMap = viewParent.findViewById(R.id.llMap);
        llMap.setVisibility(View.VISIBLE);
        llSearch = viewParent.findViewById(R.id.llSearch);
        llSearch.setVisibility(View.GONE);
        initMapView();
        initChoose();
        // initSearch();
    }

    private void initChoose() {
        // TODO Auto-generated method stub
        viewParent.findViewById(R.id.tvJiaoTong).setOnClickListener(this);
        viewParent.findViewById(R.id.tvTianqi).setOnClickListener(this);
        viewParent.findViewById(R.id.tvTianqiYubao).setOnClickListener(this);
        changeTab(0);
    }

    private void initSearch() {
        // TODO Auto-generated method stub
        viewParent.findViewById(R.id.btCancel).setOnClickListener(this);
        llList = (ListView) viewParent.findViewById(R.id.llList);
        etSearch = (EditText) viewParent.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                lvCityInfos.clear();
                for (SearchCity city : listSearch) {
                    if (!Util.isEmpty(string)) {
                        String made = city.getName();
                        if (made.contains(string)) {
                            List<MCityInfo> cityInfos = city.getInfos();
                            for (MCityInfo cityInfo : cityInfos) {
                                cityInfo.setCategory("1");
                                lvCityInfos.add(cityInfo);
                            }
                        }
                    }
                }
                adapterList.notifyDataSetChanged();
            }
        });
        setListView();

    }

    private void setListView() {
        lvCityInfos = new ArrayList<MCityInfo>();
        adapterList = new CityGridAdapter(JiaoTongFragment.this, null,
                lvCityInfos, 1);
        llList.setAdapter(adapterList);
    }

    private void initMapView() {
        // TODO Auto-generated method stub
        manager.beginTransaction().replace(R.id.map, map, "map_fragment")
                .commit();

        mapTipView = viewParent.findViewById(R.id.llShow);
        mapTitle = (TextView) viewParent.findViewById(R.id.tvTitle);
        mapTitleContent = (TextView) viewParent
                .findViewById(R.id.tvTitleContent);
        // viewParent.findViewById(R.id.btMapSearch).setOnClickListener(this);
        viewParent.findViewById(R.id.btLukuang).setOnClickListener(this);
        viewParent.findViewById(R.id.btXingche).setOnClickListener(this);
        mapTipView.setOnClickListener(this);

        getData(null);
    }

    private void getData(final String name) {
        // TODO Auto-generated method stub
        showDialog(context);
        RequestParams params = new RequestParams();
        if (!Util.isEmpty(name)) {
            params.put("expwyName", name);
        }
        if (carDetails == null) {
            carDetails = new ArrayList<MCityInfo>();
        } else {
            carDetails.clear();
        }

        if (zhanDetails == null) {
            zhanDetails = new ArrayList<MCityInfo>();
        } else {
            zhanDetails.clear();
        }

        CMHttpClient.getInstance().get(Gloable.ADafaFSurl, params,
                new CMHttpResponseHandler(false) {
                    @Override
                    public void onSuccess(int code, Header[] header, byte[] data) {
                        // TODO Auto-generated method stub
                        super.onSuccess(code, header, data);
                        try {
                            JSONArray jsonArray = new JSONArray(
                                    new String(data));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MCityInfo cityInfo = new MCityInfo();
                                JSONObject object = jsonArray.getJSONObject(i);
                                cityInfo.setCategory(object.getString("lat")
                                        + "," + object.getString("lng"));
                                cityInfo.setsName(object
                                        .getString("tollStationName"));
                                cityInfo.setContent(object
                                        .getString("offDirection")
                                        + "\n"
                                        + object.getString("desc"));
                                zhanDetails.add(cityInfo);
                                cityInfo = null;
                            }
                            drawableMap();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        CMHttpClient.getInstance().get(Gloable.JIAOTONGURL, params,
                new CMHttpResponseHandler(true) {
                    @Override
                    public void onSuccess(int code, Header[] header, byte[] data) {
                        // TODO Auto-generated method stub
                        super.onSuccess(code, header, data);
                        dismissDialog();
                        configData(new String(data), name);
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

    protected void drawableMap() {
        // TODO Auto-generated method stub
        try {
            if (mBaidumap != null)
                mBaidumap.clear();
            initOverlay();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void configData(String string, String name) {
        // TODO Auto-generated method stub
        try {
            if (Util.isEmpty(name)) {
                JSONArray jsonArray111 = new JSONArray(string);
                for (int j = 0; j < jsonArray111.length(); j++) {
                    JSONObject jsonObject = jsonArray111.getJSONObject(j);
                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MCityInfo cityInfo = new MCityInfo();
                        JSONObject object = jsonArray.getJSONObject(i);

                        JSONObject objectBasic = object.getJSONObject("basic");
                        cityInfo.setCategory(objectBasic.getString("lat") + ","
                                + objectBasic.getString("lng"));
                        cityInfo.setsName(objectBasic.getString("stationName"));
                        JSONObject object2 = object.getJSONObject("now");
                        cityInfo.setContent("温度:" + object2.getString("tmp")
                                + "℃\n湿度:" + object2.getString("hum")
                                + "%\n小时降水量:" + object2.getString("pcpn")
                                + "mm\n能见度:" + object2.getString("vis") + "米\n风向:"
                                + object2.getString("wind_dir_txt") + "\n风速:"
                                + object2.getString("wind_sc") + "级");
                        JSONObject adadaForcast = object
                                .getJSONObject("forecast");
                        String dayII = adadaForcast.getString("txt_d");
                        String nightII = adadaForcast.getString("txt_n");
                        if (!dayII.equals(nightII)) {
                            dayII = (dayII + "转" + nightII);
                        }
                        String sfsf = adadaForcast.getString("wind_b_txt");
                        String adfaff = adadaForcast.getString("wind_e_txt");
                        if (!sfsf.equals(adfaff)) {
                            sfsf = (sfsf + "转" + adfaff);
                        }
                        cityInfo.setTianqiYubao(dayII + ","
                                + adadaForcast.getString("min_tmp") + "~"
                                + adadaForcast.getString("max_tmp") + "℃,"
                                + sfsf);
                        carDetails.add(cityInfo);
                        cityInfo = null;
                    }
                }
            } else {
                JSONObject jsonObject = new JSONObject(string);
                if (jsonObject.has("info")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MCityInfo cityInfo = new MCityInfo();
                        JSONObject object = jsonArray.getJSONObject(i);
                        JSONObject objectBasic = object.getJSONObject("basic");
                        cityInfo.setCategory(objectBasic.getString("lat") + ","
                                + objectBasic.getString("lng"));
                        cityInfo.setsName(objectBasic.getString("stationName"));
                        JSONObject object2 = object.getJSONObject("now");
                        cityInfo.setContent("温度:" + object2.getString("tmp")
                                + "℃\n湿度:" + object2.getString("hum")
                                + "%\n小时降水量:" + object2.getString("pcpn")
                                + "\n能见度:" + object2.getString("vis")
                                + "米\n风向:" + object2.getString("wind_dir_txt")
                                + "\n风速:" + object2.getString("wind_sc") + "级");
                        JSONObject adadaForcast = object
                                .getJSONObject("forecast");
                        String dayII = adadaForcast.getString("txt_d");
                        String nightII = adadaForcast.getString("txt_n");
                        if (!dayII.equals(nightII)) {
                            dayII = (dayII + "转" + nightII);
                        }
                        String sfsf = adadaForcast.getString("wind_b_txt");
                        String adfaff = adadaForcast.getString("wind_e_txt");
                        if (!sfsf.equals(adfaff)) {
                            sfsf = (sfsf + "转" + adfaff);
                        }
                        cityInfo.setTianqiYubao(dayII + ","
                                + adadaForcast.getString("min_tmp") + "~"
                                + adadaForcast.getString("max_tmp") + "℃,"
                                + sfsf);
                        carDetails.add(cityInfo);
                        cityInfo = null;
                    }

                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void initOverlay() {
        // TODO Auto-generated method stub
        OverlayManager manager = new OverlayManager(mBaidumap) {

            @Override
            public boolean onPolylineClick(Polyline arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }

            @Override
            public List<OverlayOptions> getOverlayOptions() {
                if (list == null) {
                    list = new ArrayList<OverlayOptions>();
                } else {
                    list.clear();
                }
                List<MCityInfo> drawInfos = null;
                if (indexTab == 0) {
                    drawInfos = zhanDetails;
                } else {
                    drawInfos = carDetails;
                }

                for (int i = 0; i < drawInfos.size(); i++) {
                    MCityInfo details = drawInfos.get(i);
                    String csds = details.getsName();
                    if (!Util.isEmpty(csds)) {
                        String[] llocation = details.getCategory().split(",");
                        if (llocation.length == 2) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("index", i);
                            LatLng llA = new LatLng(
                                    Double.parseDouble(llocation[0]),
                                    Double.parseDouble(llocation[1]));
                            MarkerOptions ooA = new MarkerOptions()
                                    .position(llA).extraInfo(bundle)
                                    .icon(getImageResouce(details, false))
                                    .title(details.getsName()).draggable(false);
                            list.add(ooA);
                        }
                    }

                }
                return list;
            }

        };

        mBaidumap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                // TODO Auto-generated method stub
                if (list == null)
                    return;
                if (mapStatus.zoom >= Gloable.MapZoomLevel) {
                    for (int i = 0; i < list.size(); i++) {
                        MCityInfo cityInfo = null;
                        if (indexTab == 0) {
                            cityInfo = zhanDetails.get(i);
                        } else {
                            cityInfo = carDetails.get(i);
                        }
                        MarkerOptions markerOptions = (MarkerOptions) list
                                .get(i);
                        String acofsfsf = cityInfo.getContent();
                        if (acofsfsf.contains("关闭")) {
                            markerOptions.icon(guanbiShow);
                        }
                    }
                }
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                // TODO Auto-generated method stub

            }
        });

        mBaidumap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                mapTipView.setVisibility(View.VISIBLE);
                int index = marker.getExtraInfo().getInt("index");
                MCityInfo details = null;
                if (indexTab == 0) {
                    details = zhanDetails.get(index);
                } else {
                    details = carDetails.get(index);
                }
                mapTitle.setText(details.getsName());
                if (indexTab == 2) {
                    mapTitleContent.setText(details.getTianqiYubao());
                } else {
                    mapTitleContent.setText(details.getContent());
                }
                // showMapTipView(details);
                return false;
            }
        });

        MyLocationData locData = new MyLocationData.Builder().direction(100)
                .latitude(Double.parseDouble(preference.getLatitude()))
                .longitude(Double.parseDouble(preference.getLongtitude()))
                .build();
        mBaidumap.setMyLocationData(locData);
        manager.addToMap();
        // manager.zoomToSpan();
        MapStatus status = new MapStatus.Builder()
                .target(new LatLng(locData.latitude, locData.longitude))
                .zoom(12).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(status);
        mBaidumap.animateMapStatus(mapStatusUpdate);
    }

    private int idddd = 0;

    private void showMapTipView(MCityInfo details) {
        // TODO Auto-generated method stub
        if (mapTipView.getVisibility() == View.VISIBLE) {
            Log.i("CM TAG", indexTab + "====GONE=====");
            mapTipView.setVisibility(View.GONE);
        } else {
            Log.i("CM TAG", indexTab + "====VISIBLE=====");
            mapTipView.setVisibility(View.VISIBLE);
            mapTitle.setText(details.getsName());
            if (indexTab == 2) {
                mapTitleContent.setText(details.getTianqiYubao());
            } else {
                mapTitleContent.setText(details.getContent());
            }
        }
    }

    protected BitmapDescriptor getImageResouce(MCityInfo details, boolean isZoom) {
        String acofsfsf = details.getContent();
        if (isZoom) {
            if (Util.isEmpty(acofsfsf)) {
                return norrmm;
            } else {
                if (acofsfsf.contains("关闭")) {
                    return guanbiShow;
                } else {
                    return norrmm;
                }
            }
        } else {
            if (acofsfsf.contains("关闭")) {
                return guanbi;
            } else {
                return norrmm;
            }
        }

    }

    public void refreshDataByChooseData(MCityInfo cityInfo) {
        llMap.setVisibility(View.VISIBLE);
        llSearch.setVisibility(View.GONE);
        getData(cityInfo.getsName());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btMapSearch:
                llMap.setVisibility(View.GONE);
                llSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.btCancel:
                llMap.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.GONE);
                break;
            case R.id.btBack:
                getFragmentManager().popBackStack();
                break;
            case R.id.llShow:
                mapTipView.setVisibility(View.GONE);
                break;
            case R.id.btLukuang:
                if (dialog != null) {
                    dialog = null;
                }
                dialog = new CMListDialog(context, 0, textViewTitle.getText()
                        .toString());
                dialog.show();
                break;
            case R.id.btXingche:
                // 行车
                if (dialog != null) {
                    dialog = null;
                }
                dialog = new CMListDialog(context, 1, textViewTitle.getText()
                        .toString());
                dialog.show();
                break;
            case R.id.tvTianqi:
                changeTab(1);
                break;
            case R.id.tvTianqiYubao:
                changeTab(2);
                break;
            case R.id.tvJiaoTong:
                changeTab(0);
                break;
            default:
                break;
        }
    }

    private void changeTab(int index) {
        // TODO Auto-generated method stub
        idddd = 0;
        indexTab = index;
        for (int i = 0; i < ddddd.length; i++) {
            if (i == index) {
                TextView textView = (TextView) viewParent
                        .findViewById(ddddd[i]);
                textView.setBackgroundResource(R.drawable.fffffffff);
                textView.setTextColor(Color.parseColor("#ffffff"));
            } else {
                TextView textView = (TextView) viewParent
                        .findViewById(ddddd[i]);
                textView.setBackgroundResource(R.drawable.dddddddd);
                textView.setTextColor(Color.parseColor("#333333"));
            }
        }
        drawableMap();
    }
}
