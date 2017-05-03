package com.tangshan.gui.ui.yujing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.adapter.YujingAdapter;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;

import cz.msebera.android.httpclient.Header;

public class YujingFragment extends BaseFragment {
    private ListView lvYujing;
    private List<MCityInfo> list = null;
    private CMPreference preference;
    private TextView textViewTitle;
    private View noData;
    private MCityInfo cityInfo;
    private YujingAdapter adapter;
    private String[] dddd = new String[]{"54534", "54533", "54532", "54531",
            "54437", "54539", "54434", "54522", "54535", "54429", "54439",
            "54538", "50000"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        preference = new CMPreference(context);
        cityInfo = (MCityInfo) getArguments().getSerializable("city");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.getInstance().mContent = this;
        // checkeTitle();
    }

    //
    // private void checkeTitle() {
    // // TODO Auto-generated method stub
    // String title = textViewTitle.getText().toString();
    // String nadad = preference.getDefautlCityInfo().getsName();
    // if (!title.equals(nadad)) {
    // textViewTitle.setText(nadad);
    // getData();
    // }
    // }

    private void configTitle() {

        ImageView imageView = (ImageView) viewParent
                .findViewById(R.id.ivHeaderImageView);
        imageView.setBackgroundResource(R.drawable.back);

        textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
        String name = cityInfo.getsName();
        String locationCity = preference.getLocationCityInfo().getsName();
        // String defaldaf = preference.getDefautlCityInfo().getsName();
        textViewTitle.setText(name);
        // if (defaldaf.equals(name)) {
        // Drawable drawable = getResources().getDrawable(R.drawable.homda);
        // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
        // drawable.getIntrinsicHeight());
        // textViewTitle.setCompoundDrawables(null, null, null, null);
        // } else
        //
        if (locationCity.equals(name)) {
            Drawable drawable = getResources().getDrawable(R.drawable.location);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            textViewTitle.setCompoundDrawables(drawable, null, null, null);
        } else {
            textViewTitle.setCompoundDrawables(null, null, null, null);
        }

        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        getFragmentManager().popBackStack();
                    }
                });
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_yujing, null);
        initView();

        return viewParent;
    }

    private void initView() {
        configTitle();
        lvYujing = (ListView) viewParent.findViewById(R.id.lvYujing);
        noData = viewParent.findViewById(R.id.noData);
        noData.setVisibility(View.GONE);
        getData();
    }

    private void getData() {
        // TODO Auto-generated method stub
        if (list == null) {
            list = new ArrayList<MCityInfo>();
        } else {
            list.clear();
        }
        showDialog(context);
        RequestParams params = new RequestParams();
        for (int i = 0; i < dddd.length; i++) {
            params.put("stationNum", dddd[i]);
            CMHttpClient.getInstance().get(Gloable.YUJINGURL, params,
                    new CMHttpResponseHandler(true) {
                        @Override
                        public void onSuccess(int code, Header[] header,
                                              byte[] data) {
                            // TODO Auto-generated method stub
                            super.onSuccess(code, header, data);
                            dismissDialog();
                            configData(new String(data));
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
    }

    protected void configData(String string) {
        // TODO Auto-generated method stub
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("alarm");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < jsonArray.length(); i++) {
                MCityInfo cityInfo = new MCityInfo();
                JSONObject object = jsonArray.getJSONObject(i);
                Date date = format.parse(object.getString("date"));
                cityInfo.setsNum(dateFormat.format(date));
                cityInfo.setsName(object.getString("releaser")
                        + object.getString("state") + object.getString("type")
                        + object.getString("level") + "预警");

                cityInfo.setId(object.getString("type"));
                cityInfo.setContent(object.getString("level"));
                cityInfo.setCategory(object.getString("desc"));
                list.add(cityInfo);
                cityInfo = null;
            }
            if (list.size() > 0) {
                noData.setVisibility(View.GONE);
            } else {
                noData.setVisibility(View.VISIBLE);
            }
            if (adapter == null) {
                adapter = new YujingAdapter(YujingFragment.this, list);
                lvYujing.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
