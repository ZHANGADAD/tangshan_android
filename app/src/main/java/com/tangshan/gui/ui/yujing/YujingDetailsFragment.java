package com.tangshan.gui.ui.yujing;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;

public class YujingDetailsFragment extends BaseFragment {

    private MCityInfo cityInfo;
    private TextView textViewTitle, tvYujingTitle, tvYujingTime,
            tvYujingContent, tvFangyu, tvFangyuContent;
    private CMPreference preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        preference = new CMPreference(context);
        cityInfo = (MCityInfo) getArguments().getSerializable("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_yujing_details, null);
        initView();

        return viewParent;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MainActivity.getInstance().mContent = this;
    }

    private void initView() {
        tvYujingTitle = (TextView) viewParent.findViewById(R.id.tvYujingTitle);
        tvYujingTime = (TextView) viewParent.findViewById(R.id.tvYujingTime);
        tvYujingContent = (TextView) viewParent
                .findViewById(R.id.tvYujingContent);
        tvFangyu = (TextView) viewParent.findViewById(R.id.tvFangyu);
        tvFangyuContent = (TextView) viewParent
                .findViewById(R.id.tvFangyuContent);
        configTitle();
        initWithData();
    }

    private void configTitle() {
        ImageView imageView = (ImageView) viewParent
                .findViewById(R.id.ivHeaderImageView);
        imageView.setBackgroundResource(R.drawable.back);

        textViewTitle = (TextView) viewParent.findViewById(R.id.tvHeaderTitle);
        textViewTitle.setText(preference.getDefautlCityInfo().getsName());
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getFragmentManager().popBackStack();
                    }
                });
        viewParent.findViewById(R.id.btShare).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        postShare();
                    }

                });
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
    }

    private void postShare() {
        // TODO Auto-generated method stub

    }

    private void initWithData() {
        Drawable drawable = getYujingDrawable(cityInfo);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        tvYujingTitle.setCompoundDrawables(drawable, null, null, null);
        tvYujingTitle.setText(cityInfo.getsName());
        tvYujingTime.setText(cityInfo.getsNum());
        tvYujingContent.setText(cityInfo.getCategory());
    }

}
