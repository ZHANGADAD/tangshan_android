package com.tangshan.gui.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.R;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.ui.BaseFragment;

public class CMBuySettingFragment extends BaseFragment implements
        OnClickListener {

    private CMPreference preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
        preference = new CMPreference(context);
    }

    public void showBuySuccess() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setMessage("购买成功!").setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });
        alertDialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewParent = inflater.inflate(R.layout.cm_settings_buy, null);
        initView();

        return viewParent;
    }

    private void configTitle() {
        // TODO Auto-generated method stub
        TextView textView = (TextView) viewParent
                .findViewById(R.id.tvHeaderTitle);
        textView.setText("购买");
        textView.setCompoundDrawables(null, null, null, null);

        ((ImageView) viewParent.findViewById(R.id.ivHeaderImageView))
                .setBackgroundResource(R.drawable.back);
        viewParent.findViewById(R.id.btBack).setVisibility(View.VISIBLE);
        viewParent.findViewById(R.id.btBack).setOnClickListener(this);
        viewParent.findViewById(R.id.btShare).setVisibility(View.GONE);
        viewParent.findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
        viewParent.findViewById(R.id.btBuy).setOnClickListener(this);
    }

    private void initView() {
        configTitle();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btBack:
                getFragmentManager().popBackStack();
                break;
            case R.id.btBuy:
                Intent intent = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("smsto:10086"));
                intent.putExtra("sms_body", "唐山气象好");
                startActivityForResult(intent, 10086);
                break;
            default:
                break;
        }
    }

}
