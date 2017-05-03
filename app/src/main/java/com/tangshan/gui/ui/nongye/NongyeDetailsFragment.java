package com.tangshan.gui.ui.nongye;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;

public class NongyeDetailsFragment extends BaseFragment implements
        OnClickListener {

    private String id;
    private CMPreference preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        id = getArguments().getString("id");
        preference = new CMPreference(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_nongye_details, null);
        initView();

        return viewParent;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.getInstance().mContent = this;
    }

    private void configTitle() {
        // TODO Auto-generated method stub
        ((TextView) viewParent.findViewById(R.id.tvHeaderTitle))
                .setText(preference.getDefautlCityInfo().getsName());
        ((ImageView) viewParent.findViewById(R.id.ivHeaderImageView))
                .setBackgroundResource(R.drawable.daohangicon);
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
        viewParent.findViewById(R.id.btShare).setOnClickListener(this);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(
                View.VISIBLE);

    }

    private void initView() {
        configTitle();
        getData();
    }

    private void getData() {
        // TODO Auto-generated method stub

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
