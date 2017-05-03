package com.tangshan.gui.ui.tiqiyubao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;
import com.tangshan.gui.util.Util;
import com.tangshan.gui.view.chart.LineView;

import cz.msebera.android.httpclient.Header;

public class TianqiYubaoFragment extends BaseFragment implements
        OnClickListener {

    // private CMPreference preference;
    private TextView textView;
    private int[] dayTextView = new int[]{R.id.tvDay1, R.id.tvDay2,
            R.id.tvDay3, R.id.tvDay4, R.id.tvDay5, R.id.tvDay6};

    private int[] iconView = new int[]{R.id.ivIcon1, R.id.ivIcon2,
            R.id.ivIcon3, R.id.ivIcon4, R.id.ivIcon5, R.id.ivIcon6};

    private int[] iconViewDown = new int[]{R.id.ivIcon11, R.id.ivIcon12,
            R.id.ivIcon13, R.id.ivIcon14, R.id.ivIcon15, R.id.ivIcon16};

    private int[] tempTextView = new int[]{R.id.tvDay11, R.id.tvDay12,
            R.id.tvDay13, R.id.tvDay14, R.id.tvDay15, R.id.tvDay16};

    private int[] tempTextViewFeng = new int[]{R.id.tvDay111, R.id.tvDay112,
            R.id.tvDay113, R.id.tvDay114, R.id.tvDay115, R.id.tvDay116};

    private int[] tempTextViewFengTop = new int[]{R.id.tvDayTop111,
            R.id.tvDayTop112, R.id.tvDayTop113, R.id.tvDayTop114,
            R.id.tvDayTop115, R.id.tvDayTop116};

    private LineView lineViewTop, lineViewBottom;
    private CMPreference preference;
    private String titleText, cityNum;
    private QushiyubaoFragment fatherParent;

    public TianqiYubaoFragment(QushiyubaoFragment fatherParent,
                               String titleText, String cityNum) {
        // TODO Auto-generated constructor stub
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

        viewParent = inflater.inflate(R.layout.cm_tianqiyubao, null);
        initView();

        return viewParent;
    }

    private void configTitle() {
        // TODO Auto-generated method stub
        String name = titleText;
        textView = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
        String locationCity = preference.getLocationCityInfo().getsName();
        String defaldaf = preference.getDefautlCityInfo().getsName();
        textView.setText(name);
        if (defaldaf.equals(name)) {
            Drawable drawable = getResources().getDrawable(R.drawable.homda);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            textView.setCompoundDrawables(null, null, null, null);
        } else if (locationCity.equals(name)) {
            Drawable drawable = getResources().getDrawable(R.drawable.location);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        } else {
            textView.setCompoundDrawables(null, null, null, null);
        }

        ((ImageView) viewParent.findViewById(R.id.ivHeaderImageView))
                .setBackgroundResource(R.drawable.back);
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
        viewParent.findViewById(R.id.btShare).setVisibility(View.GONE);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);

        lineViewTop = (LineView) viewParent.findViewById(R.id.llTop);
        lineViewTop.setDrawDotLine(true);
        lineViewTop.setShowPopup(LineView.SHOW_POPUPS_All);

        lineViewBottom = (LineView) viewParent.findViewById(R.id.llBottom);
        lineViewBottom.setDrawDotLine(true);
        lineViewBottom.setShowPopup(LineView.SHOW_POPUPS_All);

    }

    private void initView() {
        configTitle();

        getData();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // checkeTitle();
    }

    // private void checkeTitle() {
    // // TODO Auto-generated method stub
    // String title = textView.getText().toString();
    // String nadad = preference.getDefautlCityInfo().getsName();
    // if (!title.equals(nadad)) {
    // textView.setText(nadad);
    // getData();
    // }
    // }

    private void getData() {
        showDialog(context);
        RequestParams params = new RequestParams();
        params.put("stationNum", cityNum);
        CMHttpClient.getInstance().get(Gloable.TIANQIUTL, params,
                new CMHttpResponseHandler(true) {
                    @Override
                    public void onSuccess(int code, Header[] header, byte[] data) {
                        // TODO Auto-generated method stub
                        super.onSuccess(code, header, data);
                        dismissDialog();
                        String consafdg = new String(data);
                        if (!Util.isEmpty(consafdg)) {
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
            List<String> minNumbers = new ArrayList<String>();
            List<String> maxNumbers = new ArrayList<String>();

            JSONObject jsonObject = new JSONObject(consafdg);
            MCityInfo cityInfo = new MCityInfo();
            cityInfo.setsNum(jsonObject.getJSONObject("basic").getString(
                    "stationNum"));
            String cith = jsonObject.getJSONObject("basic").getString(
                    "stationName");
            cityInfo.setsName(cith);
            setTextWithContent(0, jsonObject.getJSONObject("yestoday"));
            minNumbers.add(jsonObject.getJSONObject("yestoday").getString(
                    "min_tmp"));
            maxNumbers.add(jsonObject.getJSONObject("yestoday").getString(
                    "max_tmp"));
            for (int i = 1; i < 6; i++) {
                JSONObject object = jsonObject.getJSONArray("daily_forecast")
                        .getJSONObject(i - 1);
                setTextWithContent(i, object);
                minNumbers.add(object.getString("min_tmp"));
                maxNumbers.add(object.getString("max_tmp"));
            }
            ArrayList<String> test = new ArrayList<String>();
            for (int i = 0; i < 6; i++) {
                test.add(String.valueOf(i + 1));
            }
            lineViewTop.setBottomTextList(test);
            lineViewBottom.setBottomTextList(test);

            ArrayList<Integer> dataList = new ArrayList<Integer>();
            for (int i = 0; i < 6; i++) {
                dataList.add(i);
            }
            ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
            dataLists.add(getLineNumers(minNumbers));
            lineViewBottom.setShowDataList(minNumbers, 0);
            lineViewBottom.setLineColor(Color.parseColor("#56c4fb"));
            lineViewBottom.setDataList(dataLists);

            ArrayList<ArrayList<Integer>> dataLists1 = new ArrayList<ArrayList<Integer>>();
            dataLists1.add(getLineNumers(maxNumbers));
            lineViewTop.setShowDataList(maxNumbers, 0);
            lineViewTop.setLineColor(Color.parseColor("#faff09"));
            lineViewTop.setDataList(dataLists1);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setTextWithContent(int index, JSONObject jsonObject) {
        try {
            TextView textView = (TextView) viewParent
                    .findViewById(dayTextView[index]);
            String day = jsonObject.getString("date");
            String week = Util.getWeekByDayString(day);
            String txt_d = jsonObject.getString("txt_d");
            String txt_n = jsonObject.getString("txt_n");
            SpannableString spannableString = new SpannableString(week + "\n"
                    + txt_d);
            spannableString.setSpan(new AbsoluteSizeSpan(12, true),
                    week.length(), week.length() + txt_d.length() + 1,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);

            ImageView IMAGEAe = (ImageView) viewParent
                    .findViewById(iconView[index]);
            Util.setQingYinImage(IMAGEAe, txt_d, true);

            TextView tempTAsdwTop = (TextView) viewParent
                    .findViewById(tempTextViewFengTop[index]);
            String fengTop = jsonObject.getString("wind_b_dir_txt");
            String feeeTop = jsonObject.getString("wind_b_sc_txt");
            String adsafsTop = fengTop + "\n" + feeeTop;
            SpannableString sfsf1Top = new SpannableString(adsafsTop);
            sfsf1Top.setSpan(new AbsoluteSizeSpan(12, true), fengTop.length(),
                    adsafsTop.length(),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            tempTAsdwTop.setText(sfsf1Top);

            ImageView IMAGEA11e = (ImageView) viewParent
                    .findViewById(iconViewDown[index]);
            Util.setQingYinImage(IMAGEA11e, txt_n, false);

            TextView tempTextVieadAsdw = (TextView) viewParent
                    .findViewById(tempTextView[index]);
            String adasf = jsonObject.getString("min_tmp") + "/"
                    + jsonObject.getString("max_tmp");
            Util.setTempreTure(adasf, tempTextVieadAsdw, txt_n);

            TextView tempTAsdw = (TextView) viewParent
                    .findViewById(tempTextViewFeng[index]);
            String feng = jsonObject.getString("wind_e_dir_txt");
            String feee = jsonObject.getString("wind_e_sc_txt");
            String adsafs = feng + "\n" + feee;
            SpannableString sfsf1 = new SpannableString(adsafs);
            sfsf1.setSpan(new AbsoluteSizeSpan(12, true), feng.length(),
                    adsafs.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            tempTAsdw.setText(sfsf1);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
