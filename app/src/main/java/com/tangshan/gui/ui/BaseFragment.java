package com.tangshan.gui.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.util.Util;
import com.tangshan.gui.view.CMProgressDialog;

public class BaseFragment extends Fragment {

	public Context context;

	public View viewParent = null;

	public CMProgressDialog dialog = null;

	public void showDialog(Context context) {
		dismissDialog();
		dialog = new CMProgressDialog(context);
		dialog.show();
	}

	public Drawable getYujingDrawable(MCityInfo cityInfo) {
		String daolujiebing = cityInfo.getId();
		String huangse = cityInfo.getContent();
		if (daolujiebing.contains("台风")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.taifenglans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.taifenghuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.taifengchengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.taifenghongs);
			}
		} else if (daolujiebing.contains("暴雨")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.baoyulans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.baoyuhuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.baoyuchengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.baoyuhongs);
			}
		} else if (daolujiebing.contains("暴雪")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.baoxuelans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.baoxuehuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.baoxuechengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.baoxuehongs);
			}
		} else if (daolujiebing.contains("寒潮")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.hanchaolans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.hanchaohuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.hanchaochengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.hanchaohongs);
			}
		}else if (daolujiebing.contains("大风")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.dafenglans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.dafenghuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.dafengchengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.dafenghongs);
			}
		}else if (daolujiebing.contains("沙尘暴")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.shalans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.shahuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.shachengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.shahongs);
			}
		}else if (daolujiebing.contains("高温")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.gaolans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.gaohuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.gaochengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.gaohongs);
			}
		}else if (daolujiebing.contains("干旱")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.ganlans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.ganhuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.ganchengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.ganhongs);
			}
		}else if (daolujiebing.contains("雷电")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.leilans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.leihuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.leichengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.leihongs);
			}
		}else if (daolujiebing.contains("冰雹")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.binglans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.binghuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.bingchengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.binghongs);
			}
		}else if (daolujiebing.contains("霜冻")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.shuanglans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.shuanghuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.shuangchengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.shuanghongs);
			}
		}else if (daolujiebing.contains("大雾")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.wulans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.wuhuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.wuchengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.wuhongs);
			}
		}else if (daolujiebing.contains("霾")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.mailans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.maihuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.maichengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.maihongs);
			}
		}else if (daolujiebing.contains("道路结冰")) {
			if (huangse.contains("蓝色")) {
				return getResources().getDrawable(R.drawable.llans);
			} else if (huangse.contains("黄色")) {
				return getResources().getDrawable(R.drawable.lhuangs);
			} else if (huangse.contains("橙色")) {
				return getResources().getDrawable(R.drawable.lchengs);
			} else if (huangse.contains("红色")) {
				return getResources().getDrawable(R.drawable.lhongs);
			}
		}

		return getResources().getDrawable(R.drawable.taifenglans);
	}

	public void dismissDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public ArrayList<Integer> getLineNumers(List<String> minNumbers) {
		List<Integer> integers = new ArrayList<Integer>();
		for (String adaf : minNumbers) {
			if (Util.isEmpty(adaf)) {
				integers.add(0);
			} else {
				integers.add(Math.round(Float.parseFloat(adaf)));
			}
		}
		int SDAAD = integers.get(0);
		for (int i = 1; i < integers.size(); i++) {
			int adfas = integers.get(i);
			if (SDAAD > adfas) {
				SDAAD = adfas;
			}
		}
		ArrayList<Integer> integersP = new ArrayList<Integer>();
		for (int i = 0; i < integers.size(); i++) {
			integersP.add(integers.get(i) - SDAAD);
		}
		return integersP;
	}

	public List<String> getQujian(List<String> minNumbers) {
		List<Float> floats = new ArrayList<Float>();
		for (String fffsf : minNumbers) {
			if(Util.isEmpty(fffsf)){
				floats.add(0.0f);
			}else{
				floats.add(Float.valueOf(fffsf));
			}
		}
		int minString = getMinString(floats, true);
		int mxnString = getMinString(floats, false);
		List<String> adad = new ArrayList<String>();
		adad.add(minNumbers.get(minString));
		adad.add(minNumbers.get(mxnString));
		return adad;
	}

	protected int getMinString(List<Float> floats, boolean isMin) {
		Float miadasf = floats.get(0);
		int index = 0;
		for (int i = 1; i < floats.size(); i++) {
			Float float1 = floats.get(i);
			if (isMin) {
				if (float1 < miadasf) {
					index = i;
					miadasf = float1;
				}
			} else {
				if (float1 > miadasf) {
					index = i;
					miadasf = float1;
				}
			}
		}
		return index;
	}

	/**
	 * 显示一条toast
	 * 
	 * @param message
	 *            toast内容
	 */
	public void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
