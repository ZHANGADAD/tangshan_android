package com.tangshan.gui.ui.nongye;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.adapter.NongyeAdapter;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;

import cz.msebera.android.httpclient.Header;

public class NongyeFragment extends BaseFragment implements OnClickListener {
    private ListView lvYujing;
    private List<MCityInfo> list = null;
    private TextView textViewTitle;
    private CMPreference preference;

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

        viewParent = inflater.inflate(R.layout.cm_yujing, null);
        initView();

        return viewParent;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.getInstance().mContent = this;
        checkeTitle();
    }

    private void checkeTitle() {
        // TODO Auto-generated method stub
        String title = textViewTitle.getText().toString();
        String nadad = preference.getDefautlCityInfo().getsName();
        if (!title.equals(nadad)) {
            textViewTitle.setText(nadad);
            getData();
        }
    }

    private void initView() {
        configTitle();
        lvYujing = (ListView) viewParent.findViewById(R.id.lvYujing);
        getData();
    }

    private void configTitle() {

        ImageView imageView = (ImageView) viewParent
                .findViewById(R.id.ivHeaderImageView);
        imageView.setBackgroundResource(R.drawable.back);

        textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
        textViewTitle.setText(preference.getDefautlCityInfo().getsName());
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
    }

    private void getData() {
        // TODO Auto-generated method stub
        showDialog(context);
        RequestParams params = new RequestParams();
        CMHttpClient.getInstance().get(Gloable.NONGYEURL, params,
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

    protected void configData(String string) {
        // TODO Auto-generated method stub
        NongyeAdapter adapter = new NongyeAdapter(context, list);
        lvYujing.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.btBack) {
            getFragmentManager().popBackStack();
        } else if (v.getId() == R.id.btShare) {

        }
    }
}
