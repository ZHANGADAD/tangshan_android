package com.tangshan.gui.ui.shenghuo;

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
import com.tangshan.gui.adapter.ShenghuoQiXiangAdapter;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ShengHuoQiFragment extends BaseFragment implements OnClickListener {
    private ListView lvJintian, lvMingtian, lvHoutian;
    private List<MCityInfo> listJintian = null, listMingtian = null,
            listHoutian = null;
    private TextView textViewTitle;
    private CMPreference preference;
    private int index;
    private View remenView, toutiaoView, zhuantiView;
    private ShenghuoQiXiangAdapter adapterJintian = null,
            adapterMingtian = null, adapterHoutian = null;

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
//		checkeTitle();
    }

//	private void checkeTitle() {
//		// TODO Auto-generated method stub
//		String title = textViewTitle.getText().toString();
//		String nadad = preference.getDefautlCityInfo().getsName();
//		if (!title.equals(nadad)) {
//			textViewTitle.setText(nadad);
//			getData(index);
//		}
//	}

    private void configTitle() {

//		String locationCity = preference.getLocationCityInfo().getsName();
        ImageView imageView = (ImageView) viewParent
                .findViewById(R.id.ivHeaderImageView);
        imageView.setBackgroundResource(R.drawable.back);

        textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
//		Drawable drawable = getResources().getDrawable(R.drawable.location);
//		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
//				drawable.getIntrinsicHeight());
//		if (locationCity.equals(textViewTitle.getText().toString())) {
//			textViewTitle.setCompoundDrawables(drawable, null, null, null);
//		} else {
//			textViewTitle.setCompoundDrawables(null, null, null, null);
//		}
        textViewTitle.setText("唐山");
        textViewTitle.setCompoundDrawables(null, null, null, null);
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

        viewParent = inflater.inflate(R.layout.cm_shenghuoqixiang, null);
        initView();

        return viewParent;
    }

    private void initView() {
        configTitle();
        lvJintian = (ListView) viewParent.findViewById(R.id.lvJintian);
        lvMingtian = (ListView) viewParent.findViewById(R.id.lvMingtian);
        lvHoutian = (ListView) viewParent.findViewById(R.id.lvHoutian);

        remenView = viewParent.findViewById(R.id.remenView);
        toutiaoView = viewParent.findViewById(R.id.toutiaoView);
        zhuantiView = viewParent.findViewById(R.id.zhuantiView);

        viewParent.findViewById(R.id.btRemenzixun).setOnClickListener(this);
        viewParent.findViewById(R.id.btYongchetoutiao).setOnClickListener(this);
        viewParent.findViewById(R.id.btChemizhuanti).setOnClickListener(this);

        changeTab(0);
    }

    private void getData(int index) {
        // TODO Auto-generated method stub
        this.index = index;
        showDialog(context);
        RequestParams params = new RequestParams();
        CMHttpClient.getInstance().get(Gloable.SHENGHUOQIXIANGURL, params,
                new CMHttpResponseHandler(true) {
                    @Override
                    public void onSuccess(int code, Header[] header, byte[] data) {
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

    private void changeTab(int category) {
        // TODO Auto-generated method stub
        if (category == 0) {

            remenView.setVisibility(View.INVISIBLE);
            toutiaoView.setVisibility(View.INVISIBLE);
            zhuantiView.setVisibility(View.INVISIBLE);

            lvJintian.setVisibility(View.VISIBLE);
            lvMingtian.setVisibility(View.INVISIBLE);
            lvHoutian.setVisibility(View.INVISIBLE);

        } else if (category == 1) {

            toutiaoView.setVisibility(View.VISIBLE);
            remenView.setVisibility(View.INVISIBLE);
            zhuantiView.setVisibility(View.INVISIBLE);

            lvJintian.setVisibility(View.INVISIBLE);
            lvMingtian.setVisibility(View.VISIBLE);
            lvHoutian.setVisibility(View.INVISIBLE);

        } else {

            zhuantiView.setVisibility(View.VISIBLE);
            remenView.setVisibility(View.INVISIBLE);
            toutiaoView.setVisibility(View.INVISIBLE);

            lvJintian.setVisibility(View.INVISIBLE);
            lvMingtian.setVisibility(View.INVISIBLE);
            lvHoutian.setVisibility(View.VISIBLE);

        }
        if (isShouldReload(category)) {
            getData(category);
        }
    }

    private boolean isShouldReload(int category) {
        if (category == 0) {
            if (listJintian != null && listJintian.size() > 0) {
                return false;
            } else {
                return true;
            }
        } else if (category == 1) {
            if (listMingtian != null && listMingtian.size() > 0) {
                return false;
            } else {
                return true;
            }
        } else {
            if (listHoutian != null && listHoutian.size() > 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    protected void configData(String string) {
        // TODO Auto-generated method stub
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("index");
            List<MCityInfo> infos = new ArrayList<MCityInfo>();
            for (int i = 0; i < jsonArray.length(); i++) {
                MCityInfo cityInfo = new MCityInfo();
                JSONObject object = jsonArray.getJSONObject(i);
                String ndaff = object.getString("indexName");
                cityInfo.setsNum(ndaff + ":   " + object.getString("brf"));
                cityInfo.setsName(object.getString("txt"));
                if (ndaff.contains("晾晒")) {
                    cityInfo.setCategory(R.drawable.liangshai + "");
                } else if (ndaff.contains("晨练")) {
                    cityInfo.setCategory(R.drawable.chenlian + "");
                } else if (ndaff.contains("紫外")) {
                    cityInfo.setCategory(R.drawable.ziwai + "");
                } else if (ndaff.contains("旅游")) {
                    cityInfo.setCategory(R.drawable.lvyou + "");
                } else if (ndaff.contains("洗车")) {
                    cityInfo.setCategory(R.drawable.xiche + "");
                } else if (ndaff.contains("冠心")) {
                    cityInfo.setCategory(R.drawable.guanxin + "");
                } else if (ndaff.contains("蓝天")) {
                    cityInfo.setCategory(R.drawable.lantian + "");
                } else if (ndaff.contains("穿衣")) {
                    cityInfo.setCategory(R.drawable.chuanyi + "");
                } else if (ndaff.contains("高")) {
                    cityInfo.setCategory(R.drawable.zuigao + "");
                } else if (ndaff.contains("舒适度")) {
                    cityInfo.setCategory(R.drawable.shushi + "");
                } else if (ndaff.contains("闷热")) {
                    cityInfo.setCategory(R.drawable.menre + "");
                } else if (ndaff.contains("低")) {
                    cityInfo.setCategory(R.drawable.zuidi + "");
                } else if (ndaff.contains("火险")) {
                    cityInfo.setCategory(R.drawable.huoxian + "");
                }

                infos.add(cityInfo);
                cityInfo = null;
            }

            if (index == 0) {
                if (listJintian != null) {
                    listJintian.clear();
                }
                listJintian = infos;

                if (adapterJintian == null) {
                    adapterJintian = new ShenghuoQiXiangAdapter(
                            ShengHuoQiFragment.this, listJintian);
                    lvJintian.setAdapter(adapterJintian);
                } else {
                    adapterJintian.notifyDataSetChanged();
                }
            } else if (index == 1) {
                if (listMingtian != null) {
                    listMingtian.clear();
                }
                listMingtian = infos;
                if (adapterMingtian == null) {
                    adapterMingtian = new ShenghuoQiXiangAdapter(
                            ShengHuoQiFragment.this, listMingtian);
                    lvMingtian.setAdapter(adapterMingtian);
                } else {
                    adapterMingtian.notifyDataSetChanged();
                }
            } else {
                if (listHoutian != null) {
                    listHoutian.clear();
                }
                listHoutian = infos;
                if (adapterHoutian == null) {
                    adapterHoutian = new ShenghuoQiXiangAdapter(
                            ShengHuoQiFragment.this, listHoutian);
                    lvHoutian.setAdapter(adapterHoutian);
                } else {
                    adapterHoutian.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btRemenzixun:
                changeTab(0);
                break;
            case R.id.btYongchetoutiao:
                changeTab(1);
                break;
            case R.id.btChemizhuanti:
                changeTab(2);
                break;
        }
    }
}
