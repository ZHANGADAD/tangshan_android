package com.tangshan.gui.ui.tianqi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.loopj.android.http.RequestParams;
import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.ui.city.CityListFragment;
import com.tangshan.gui.ui.tiqiyubao.QushiyubaoFragment;
import com.tangshan.gui.ui.wangri.WangtiPagerFragment;
import com.tangshan.gui.ui.yujing.YujingFragment;
import com.tangshan.gui.util.Util;
import com.tangshan.gui.view.CMTianqiDetailsDialog;

import cz.msebera.android.httpclient.Header;

public class CMTiqiContentFragment extends BaseFragment implements
        OnClickListener {

    private View viewAll;

    private TextView tvCondition, tvCity, tvWeek, tvDay, tvQiya, tvFeng,
            tvTempture, tvQingyin;
    private int[] dayTextView = new int[]{R.id.tvDay1, R.id.tvDay2,
            R.id.tvDay3, R.id.tvDay4, R.id.tvDay5, R.id.tvDay6};
    private int[] iconView = new int[]{R.id.ivIcon1, R.id.ivIcon2,
            R.id.ivIcon3, R.id.ivIcon4, R.id.ivIcon5, R.id.ivIcon6};
    private int[] tempTextView = new int[]{R.id.tvTemp1, R.id.tvTemp2,
            R.id.tvTemp3, R.id.tvTemp4, R.id.tvTemp5, R.id.tvTemp6};

    private int[] dayTextViewN = new int[]{R.id.tvDay1N, R.id.tvDay2N,
            R.id.tvDay3N, R.id.tvDay4N, R.id.tvDay5N, R.id.tvDay6N};
    private int[] iconViewN = new int[]{R.id.ivIcon1N, R.id.ivIcon2N,
            R.id.ivIcon3N, R.id.ivIcon4N, R.id.ivIcon5N, R.id.ivIcon6N};

    private CMTianqiDetailsDialog detailsDialog = null;
    private CMPreference preference;
    private boolean isTanhao = false;
    private String jsond, condition, titleText, cityNum;
    private CMTianqi fatherParent;

    private View viewBg;

    public CMTiqiContentFragment(CMTianqi fatherParent, String titleText,
                                 String cityNum) {
        this.fatherParent = fatherParent;
        this.cityNum = cityNum;
        this.titleText = titleText;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        preference = new CMPreference(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewAll = inflater.inflate(R.layout.tianqicontebt, null);
        initView();

        return viewAll;
    }

    private void getData() {
        RequestParams params = new RequestParams();
        params.put("stationNum", cityNum);
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
                                    condition = jsonObject
                                            .getString("AQI_class");
                                    if (condition.length() > 3) {
                                        tvCondition.setTextSize(
                                                TypedValue.COMPLEX_UNIT_SP, 10);
                                    }
                                    tvCondition.setText(condition);
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

        String aff = preference.getTianqiContent(titleText);
        if (!Util.isEmpty(aff)) {
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
                            preference.setTianqiContent(consafdg, titleText);
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
            jsond = consafdg;
            JSONObject jsonObject = new JSONObject(consafdg);
            String cith = jsonObject.getJSONObject("basic").getString(
                    "stationName");
            JSONArray daily_forecast = jsonObject
                    .getJSONArray("daily_forecast");
            tvCity.setText(cith);
            tvWeek.setText(Util.getCurrentWeek());
            tvDay.setText(Util.getCurrentDay());
            tvTempture
                    .setText(jsonObject.getJSONObject("now").getString("tmp"));
            String adasf = daily_forecast.getJSONObject(0).getString("txt_d");
            tvQingyin.setText(adasf);

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

            changeBgView(adasf);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void changeBgView(String adasf) {
        if (!Util.isEmpty(adasf)) {
            if (adasf.contains("阴")) {
                viewBg.setBackgroundResource(R.drawable.yintianbg);
            } else if (adasf.contains("多云")) {
                viewBg.setBackgroundResource(R.drawable.duoyunbg);
            } else if (adasf.contains("雨")) {
                viewBg.setBackgroundResource(R.drawable.yubg);
            } else if (adasf.contains("雪")) {
                viewBg.setBackgroundResource(R.drawable.xuebg);
            } else if (adasf.contains("雷")) {
                viewBg.setBackgroundResource(R.drawable.leibg);
            } else if (adasf.contains("雾霾")) {
                viewBg.setBackgroundResource(R.drawable.wumaibg);
            } else if (adasf.contains("雾")) {
                viewBg.setBackgroundResource(R.drawable.wu);
            } else if (adasf.contains("晴")) {
                viewBg.setBackgroundResource(R.drawable.qing);
            }
        }
    }

    private void setTextWithContent(int index, JSONObject jsonObject) {
        // TODO Auto-generated method stub
        try {
            TextView textView = (TextView) viewAll
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

            TextView textViewS = (TextView) viewAll
                    .findViewById(dayTextViewN[index]);
            SpannableString spannableString2 = new SpannableString(txt_n);
            spannableString2.setSpan(new AbsoluteSizeSpan(12, true), 0,
                    txt_n.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewS.setText(spannableString2);

            ImageView IMAGEAe = (ImageView) viewAll
                    .findViewById(iconView[index]);
            Util.setQingYinImage(IMAGEAe, afasf, true);

            ImageView IMAGEAdddde = (ImageView) viewAll
                    .findViewById(iconViewN[index]);
            Util.setQingYinImage(IMAGEAdddde, txt_n, false);

            TextView tempTextVieadAsdw = (TextView) viewAll
                    .findViewById(tempTextView[index]);
            String adasf = jsonObject.getString("min_tmp") + "/"
                    + jsonObject.getString("max_tmp");
            Util.setTempreTure(adasf, tempTextVieadAsdw);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
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
                        info.setsName(titleText);
                        info.setsNum(cityNum);
                        args.putSerializable("city", info);
                        fragment.setArguments(args);
                        ((MainActivity) context).switchContent(fragment, true);
                    }
                }
                break;
            case R.id.llDay:
            case R.id.llIcon:
            case R.id.llTemp:
                if (context instanceof MainActivity) {
                    QushiyubaoFragment fragment = new QushiyubaoFragment();
                    // Bundle args = new Bundle();
                    // MCityInfo info = new MCityInfo();
                    // info.setsName(titleText);
                    // info.setsNum(cityNum);
                    // args.putSerializable("city", info);
                    // fragment.setArguments(args);
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            case R.id.llllKonqi:
                if (context instanceof MainActivity) {
                    KongqiFragment fragment = new KongqiFragment();
                    Bundle args = new Bundle();
                    MCityInfo info = new MCityInfo();
                    info.setsName(titleText);
                    info.setsNum(cityNum);
                    args.putSerializable("city", info);
                    fragment.setArguments(args);
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            default:
                break;
        }
    }

    private void startHome() {
        // TODO Auto-generated method stub
        if (context instanceof MainActivity) {
            CityListFragment fragment = CityListFragment
                    .getInstance(fatherParent);
            ((MainActivity) context).switchContent(fragment, true);
        }
    }

    private void startZhexian() {
        // TODO Auto-generated method stub
        // if (context instanceof MainActivity) {
        // if (context instanceof MainActivity) {
        // WangriShiFragment fragment = new WangriShiFragment(true);
        // Bundle args = new Bundle();
        // MCityInfo info = new MCityInfo();
        // info.setsName(titleText);
        // info.setsNum(cityNum);
        // args.putSerializable("city", info);
        // fragment.setArguments(args);
        // ((MainActivity) context).switchContent(fragment, true);
        // }
        // }
        if (context instanceof MainActivity) {
            WangtiPagerFragment fragment = new WangtiPagerFragment(false);
            ((MainActivity) context).switchContent(fragment, true);
        }
    }

    private void startYuyin() {
        // TODO Auto-generated method stub
        try {
            StringBuilder content = new StringBuilder();
            content.append("气温:");
            JSONObject jsonObject = new JSONObject(jsond);
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
            fatherParent.sasasas(content.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void showContentDetais() {
        if (detailsDialog != null) {
            detailsDialog = null;
        }
        detailsDialog = new CMTianqiDetailsDialog(context, jsond, condition);
        detailsDialog.show();
    }

    private void initView() {
        viewBg = viewAll.findViewById(R.id.lll);

        viewAll.findViewById(R.id.llllKonqi).setOnClickListener(this);

        viewAll.findViewById(R.id.ivHome)
                .setBackgroundResource(R.drawable.home);

        tvCondition = (TextView) viewAll.findViewById(R.id.tvCondition);
        tvCity = (TextView) viewAll.findViewById(R.id.tvCity);
        tvDay = (TextView) viewAll.findViewById(R.id.tvDay);
        tvWeek = (TextView) viewAll.findViewById(R.id.tvWeek);
        tvCity.setOnClickListener(this);
        tvDay.setOnClickListener(this);
        tvWeek.setOnClickListener(this);

        tvQiya = (TextView) viewAll.findViewById(R.id.tvQiya);
        tvFeng = (TextView) viewAll.findViewById(R.id.tvFeng);
        tvTempture = (TextView) viewAll.findViewById(R.id.tvTempture);
        tvQingyin = (TextView) viewAll.findViewById(R.id.tvQingYin);

        viewAll.findViewById(R.id.tvNext).setOnClickListener(this);
        viewAll.findViewById(R.id.ivHome).setOnClickListener(this);
        viewAll.findViewById(R.id.ivZhexian).setOnClickListener(this);
        viewAll.findViewById(R.id.ivYuyin).setOnClickListener(this);

        viewAll.findViewById(R.id.llDay).setOnClickListener(this);
        viewAll.findViewById(R.id.llIcon).setOnClickListener(this);
        viewAll.findViewById(R.id.llTemp).setOnClickListener(this);

        getData();
    }

}
