package com.tangshan.gui.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.ui.common.CommonWebFragment;
import com.tangshan.gui.ui.jiaotong.JiaoTongFragment;
import com.tangshan.gui.ui.shenghuo.ShengHuoQiFragment;
import com.tangshan.gui.ui.tiqiyubao.QushiyubaoFragment;
import com.tangshan.gui.ui.wangri.WangtiPagerFragment;
import com.tangshan.gui.ui.yujing.YujingFragment;

public class FuwuAdapter extends BaseAdapter {

    private Context context;
    private List<MCityInfo> list;
    private LayoutInflater layoutInflater;

    public FuwuAdapter(Context context, List<MCityInfo> list) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public MCityInfo getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.cm_fuwu_itens, null);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MCityInfo cityInfo = getItem(position);
        viewHolder.imageView.setBackgroundResource(Integer.parseInt(cityInfo
                .getsNum()));
        viewHolder.textView.setText(cityInfo.getsName());

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                handleAction(position);
            }
        });
        return convertView;
    }

    protected void handleAction(int position) {
        switch (position) {
            case 0:
                // 趋势预报
                if (context instanceof MainActivity) {
                    QushiyubaoFragment fragment = new QushiyubaoFragment();
                    // Bundle args = new Bundle();
                    // MCityInfo info = preference.getDefautlCityInfo();
                    // args.putSerializable("city", info);
                    // fragment.setArguments(args);
                    ((MainActivity) context).switchContent(fragment, true);
                }

                break;
            case 1:
                // 逐小时(未来24小时)
                if (context instanceof MainActivity) {
                    WangtiPagerFragment fragment = new WangtiPagerFragment(true);
                    ((MainActivity) context).switchContent(fragment, true);
                }

                break;
            case 2:
                // 天气实况(过去24小时)
                if (context instanceof MainActivity) {
                    WangtiPagerFragment fragment = new WangtiPagerFragment(false);
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            case 3:
                // 气象预警
                if (context instanceof MainActivity) {
                    YujingFragment fragment = new YujingFragment();
                    Bundle args = new Bundle();
                    MCityInfo info = new MCityInfo();
                    info.setsName("唐山");
                    info.setsNum("54534");
                    args.putSerializable("city", info);
                    fragment.setArguments(args);
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
            case 4:
                // 生活气象

                if (context instanceof MainActivity) {
                    ShengHuoQiFragment fragment = new ShengHuoQiFragment();
                    ((MainActivity) context).switchContent(fragment, true);
                }

                break;
            case 5:
                // 交通气象
                if (context instanceof MainActivity) {
                    JiaoTongFragment fragment = new JiaoTongFragment();
                    ((MainActivity) context).switchContent(fragment, true);
                }

                break;
            // case 6:
            // // 农业气象
            // if (context instanceof MainActivity) {
            // CommonWebFragment fragment = new CommonWebFragment(3);
            // ((MainActivity) context).switchContent(fragment, true);
            // }
            //
            // break;
            case 6:
                // 旅游气象
                if (context instanceof MainActivity) {
                    CommonWebFragment fragment = new CommonWebFragment(0);
                    ((MainActivity) context).switchContent(fragment, true);
                }

                break;
            case 7:
                // 气象科普
                if (context instanceof MainActivity) {
                    CommonWebFragment fragment = new CommonWebFragment(2);
                    ((MainActivity) context).switchContent(fragment, true);
                }
                break;
//		case 7:
//			// 世园会气象
//			if (context instanceof MainActivity) {
//				CommonWebFragment fragment = new CommonWebFragment(1);
//				((MainActivity) context).switchContent(fragment, true);
//			}
//			break;
//		case 8:
//			// 气象科普
//			if (context instanceof MainActivity) {
//				CommonWebFragment fragment = new CommonWebFragment(2);
//				((MainActivity) context).switchContent(fragment, true);
//			}
//			break;

        }
    }

    private static final class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
