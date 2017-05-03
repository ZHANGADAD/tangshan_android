package com.tangshan.gui.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.MApplication;
import com.tangshan.gui.R;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.bean.SearchCity;

public class Util {

	public static boolean isEmpty(String content) {

		if (TextUtils.isEmpty(content) || content.equalsIgnoreCase("null")
				|| content.equalsIgnoreCase(" ") || content.length() <= 0) {
			return true;
		} else {
			return false;
		}
	}

	public static List<MCityInfo> getCityList() {
		List<MCityInfo> list = null;
		String adaFS = getFromAssets("stationNum-Name.json");
		if (!Util.isEmpty(adaFS)) {
			try {
				list = new ArrayList<MCityInfo>();
				JSONArray jsonArray = new JSONArray(adaFS);
				for (int i = 0; i < jsonArray.length(); i++) {
					MCityInfo info = new MCityInfo();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					info.setsNum(jsonObject.getString("sNum"));
					info.setsName(jsonObject.getString("sName"));
					list.add(info);
					info = null;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static List<MCityInfo> getHbeiCityList() {
		List<MCityInfo> list = null;
		String adaFS = getFromAssets("hebistationName.json");
		if (!Util.isEmpty(adaFS)) {
			try {
				list = new ArrayList<MCityInfo>();
				JSONObject jsonObject1 = new JSONObject(adaFS);
				JSONArray jsonArray = jsonObject1.getJSONArray("city");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					JSONArray jsonArrayCountry = jsonObject
							.getJSONArray("county");
					for (int j = 0; j < jsonArrayCountry.length(); j++) {
						MCityInfo cityInfo = new MCityInfo();
						JSONObject object = jsonArrayCountry.getJSONObject(j);
						cityInfo.setsName(object.getString("name"));
						cityInfo.setsNum(object.getString("stationid"));
						list.add(cityInfo);
						cityInfo = null;
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static List<SearchCity> getSearchCityList() {
		List<SearchCity> list = null;
		String adaFS = getFromAssets("stationName.json");
		if (!Util.isEmpty(adaFS)) {
			try {
				list = new ArrayList<SearchCity>();
				JSONArray jsonArray = new JSONArray(adaFS);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String prorrr = jsonObject.getString("province");
					JSONArray array = jsonObject.getJSONArray("city");
					for (int j = 0; j < array.length(); j++) {
						JSONObject object = array.getJSONObject(j);
						String cityNme = object.getString("name");
						JSONArray array2 = object.getJSONArray("county");
						SearchCity city = new SearchCity();
						city.setName(prorrr + " " + cityNme);
						List<MCityInfo> infos = new ArrayList<MCityInfo>();
						for (int m = 0; m < array2.length(); m++) {
							JSONObject jsonObject2 = array2.getJSONObject(m);
							MCityInfo cityInfo = new MCityInfo();
							cityInfo.setsName(jsonObject2.getString("name"));
							cityInfo.setsNum(jsonObject2.getString("stationid"));
							infos.add(cityInfo);
							cityInfo = null;
						}
						city.setInfos(infos);
						list.add(city);
						city = null;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	protected static String getFromAssets(String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(MApplication
					.getInstance().getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isSpecialUrl(String phone) {
		String check = "^[A-Za-z0-9]*$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(phone);
		return matcher.matches();
	}

	public static boolean isPhone(String phone) {
		String check = "1[3-9]{1}[0-9]{9}";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(phone);
		return matcher.matches();
	}

	public static boolean isChePai(String chepai) {
		String check = "^[京津沪渝宁琼粤川藏青贵闽吉陕蒙晋甘桂鄂苏浙赣新鲁皖湘黑辽云豫冀][A-Za-z][A-Za-z0-9]{5}";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(chepai);
		return matcher.matches();
	}

	public static void setTempreTure(String content, TextView textView,
			String txt_n) {
		String adff = txt_n + "\n" + content;
		SpannableString superSpan = new SpannableString(adff + "o");
		superSpan.setSpan(new SuperscriptSpan(), adff.length(),
				adff.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		superSpan.setSpan(new AbsoluteSizeSpan(8, true), adff.length(),
				adff.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(superSpan);
	}

	public static void setTempreTure(String content, TextView textView) {
		SpannableString superSpan = new SpannableString(content + "o");
		superSpan.setSpan(new SuperscriptSpan(), content.length(),
				content.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		superSpan.setSpan(new AbsoluteSizeSpan(10, true), content.length(),
				content.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(superSpan);
	}

	/*
	 * Compute the sample size as a function of minSideLength and
	 * maxNumOfPixels. minSideLength is used to specify that minimal width or
	 * height of a bitmap. maxNumOfPixels is used to specify the maximal size in
	 * pixels that are tolerable in terms of memory usage.
	 * 
	 * The function returns a sample size based on the constraints. Both size
	 * and minSideLength can be passed in as IImage.UNCONSTRAINED, which
	 * indicates no care of the corresponding constraint. The functions prefers
	 * returning a sample size that generates a smaller bitmap, unless
	 * minSideLength = IImage.UNCONSTRAINED.
	 */

	public static Bitmap transform(Matrix scaler, Bitmap source,
			int targetWidth, int targetHeight, boolean scaleUp) {

		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
					- dstY);
			c.drawBitmap(source, src, dst, null);
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to mFilter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
				targetHeight);

		if (b1 != source) {
			b1.recycle();
		}

		return b2;
	}

	public static void closeSilently(Closeable c) {

		if (c == null)
			return;
		try {
			c.close();
		} catch (Throwable t) {
			// do nothing
		}
	}

	// Returns Options that set the puregeable flag for Bitmap decode.
	public static BitmapFactory.Options createNativeAllocOptions() {

		BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inNativeAlloc = true;
		return options;
	}

	// Thong added for rotate
	public static Bitmap rotateImage(Bitmap src, float degree) {
		// create new matrix
		Matrix matrix = new Matrix();
		// setup rotation degree
		matrix.postRotate(degree);
		Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
				src.getHeight(), matrix, true);
		return bmp;
	}

	public static int getOrientationInDegree(Activity activity) {

		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		return degrees;
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	public static String getVersionCode() {

		try {
			Context context = MApplication.getInstance();
			PackageManager pm = context.getPackageManager();

			PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			int versionCode = pinfo.versionCode;
			return versionCode + "";
		} catch (NameNotFoundException e) {
			return "010000";
		}

	}

	/**
	 * 获取渠道号
	 * 
	 * @author ：王治国 创建的日期 ：2013-8-18
	 * @param context
	 * @return 作用说明 ：
	 */
	public static String getChannel() {
		MApplication app = MApplication.getInstance();
		ApplicationInfo appInfo;
		String msg = null;
		try {
			appInfo = app.getPackageManager().getApplicationInfo(
					app.getPackageName(), PackageManager.GET_META_DATA);
			msg = appInfo.metaData.get("UMENG_CHANNEL").toString();
			if (msg != null) {
				msg = msg.trim();
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = "Umeng_ERR";
		}
		return msg;
	}

	public static void shake(View view) {
		if (view == null) {
			return;
		}
		Animation shake = AnimationUtils.loadAnimation(
				MApplication.getInstance(), R.anim.shake);
		view.startAnimation(shake);
	}

	public static String file2String(String filePath) {
		try {
			InputStream inputStream = new FileInputStream(filePath);
			ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
			byte[] str_b = new byte[1024];
			int i = -1;
			while ((i = inputStream.read(str_b)) > 0) {
				outputstream.write(str_b, 0, i);
			}
			inputStream.close();
			outputstream.flush();
			return outputstream.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static final String getFormatString(String timeFormat) {
		long time = Long.parseLong(timeFormat) * 1000;
		StringBuffer sb = new StringBuffer();
		final long now = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		calendar.setTimeInMillis(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		final long yesterday = calendar.getTimeInMillis();
		calendar.set(Calendar.DAY_OF_YEAR, 1);

		calendar.setTimeInMillis(time);

		if (time > yesterday) {
			final long sub = now - time;
			if (sub < 5 * 60 * 1000) {
				sb.append("刚刚");
			} else if (sub < 60 * 60 * 1000) {
				sb.append(sub / 60 / 1000 + "分钟前");
			} else if (sub >= 60 * 60 * 1000) {
				sb.append(sub / 60 / 60 / 1000 + "小时前");
			}
		} else if (time > yesterday - 24 * 60 * 60 * 1000) {
			sb.append("1天前");
		} else if (time > yesterday - 2 * 24 * 60 * 60 * 1000) {
			sb.append("2天前");
		} else if (time > yesterday - 3 * 24 * 60 * 60 * 1000) {
			sb.append("3天前");
		} else {
			sb.append(calendar.get(Calendar.YEAR));
			sb.append("-");
			int month = calendar.get(Calendar.MONTH) + 1;
			sb.append(month);
			sb.append("-");
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			sb.append(day);
			sb.append(" ");
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			sb.append(zeroFill(hour));
			sb.append(":");
			int minute = calendar.get(Calendar.MINUTE);
			sb.append(zeroFill(minute));
			int second = calendar.get(Calendar.SECOND);
			sb.append(":");
			sb.append(zeroFill(second));
		}

		return sb.toString();

	}

	public static final String getDetailsFormatString(String timeFormat) {
		long time = Long.parseLong(timeFormat) * 1000;
		StringBuffer sb = new StringBuffer();
		final long now = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		calendar.setTimeInMillis(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		final long yesterday = calendar.getTimeInMillis();
		calendar.set(Calendar.DAY_OF_YEAR, 1);

		calendar.setTimeInMillis(time);
		if (time > yesterday) {
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			sb.append(zeroFill(hour));
			sb.append(":");
			int minute = calendar.get(Calendar.MINUTE);
			sb.append(zeroFill(minute));
			int second = calendar.get(Calendar.SECOND);
			sb.append(":");
			sb.append(zeroFill(second));

		} else {
			sb.append(calendar.get(Calendar.YEAR));
			sb.append("-");
			int month = calendar.get(Calendar.MONTH) + 1;
			sb.append(month);
			sb.append("-");
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			sb.append(day);
			sb.append(" ");
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			sb.append(zeroFill(hour));
			sb.append(":");
			int minute = calendar.get(Calendar.MINUTE);
			sb.append(zeroFill(minute));
			int second = calendar.get(Calendar.SECOND);
			sb.append(":");
			sb.append(zeroFill(second));
		}
		return sb.toString();

	}

	private static String zeroFill(int value) {
		String ret = "";
		if (value < 10) {
			ret = "0" + value;
		} else {
			ret = value + "";
		}
		return ret;
	}

	public static String getVersionName(boolean isadd) {
		try {
			Context context = MApplication.getInstance();
			PackageManager pm = context.getPackageManager();

			PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			String versionCode = pinfo.versionName;
			if (isadd) {
				return versionCode;
			} else {
				return "版本号 : " + versionCode;
			}
		} catch (NameNotFoundException e) {
			if (isadd) {
				return "1.0";
			} else {
				return "版本号 : 1.0";
			}
		}

	}

	public static String getFormatDistance(double aasd) {
		// TODO Auto-generated method stub
		if (aasd < 1000) {
			return String.format("%.2f m", aasd);
		} else if (aasd < 10000 && aasd >= 1000) {
			return String.format("%.2f km", aasd / 1000);
		} else {
			return (int) (aasd / 1000) + " km";
		}
	}

	public static String getFormatDistanceKm(double aasd) {
		// TODO Auto-generated method stub
		if (aasd < 1000) {
			return String.format("%.2f 米", aasd);
		} else if (aasd < 10000 && aasd >= 1000) {
			return String.format("%.2f 公里", aasd / 1000);
		} else {
			return (int) (aasd / 1000) + " 公里";
		}
	}

	public static boolean getNetworkType() {
		ConnectivityManager connectivityManager = (ConnectivityManager) MApplication
				.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else {
			return false;
		}
	}

	public static String getFormatTimeString(int duration) {
		// TODO Auto-generated method stub
		if (duration < 60) {
			return duration + "秒";
		} else if (duration >= 60 && duration < 60 * 60) {
			return getFormatMin(duration);
		} else if (duration >= 60 * 60 && duration < 60 * 60 * 24) {
			int day = duration / 3600;
			int mind = duration % 3600;
			if (mind == 0) {
				return day + "小时";
			} else {
				return day + "小时" + getFormatMin(mind);
			}
		} else {
			return (duration / 3600 / 24) + "天";
		}
	}

	public static String getFormatMin(int duration) {
		int min = duration / 60;
		int sec = duration % 60;
		if (sec == 0) {
			return min + "分";
		} else {
			return min + "分" + sec + "秒";
		}
	}

	public static boolean isBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	protected static String getadadf(Date date, boolean isZhou) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		String afasf = null;
		if (isZhou) {
			afasf = "周一";
			switch (week) {
			case 3:
				afasf = "周二";
				break;
			case 4:
				afasf = "周三";
				break;
			case 5:
				afasf = "周四";
				break;
			case 6:
				afasf = "周五";
				break;
			case 7:
				afasf = "周六";
				break;
			case 1:
				afasf = "周日";
				break;
			}
		} else {
			afasf = "星期一";
			switch (week) {
			case 3:
				afasf = "星期二";
				break;
			case 4:
				afasf = "星期三";
				break;
			case 5:
				afasf = "星期四";
				break;
			case 6:
				afasf = "星期五";
				break;
			case 7:
				afasf = "星期六";
				break;
			case 1:
				afasf = "星期日";
				break;
			}
		}

		return afasf;

	}

	public static String getCurrentWeek() {
		Date date = new Date();
		return getadadf(date, false);
	}

	public static String getCurrentDay() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String Adfsf = month + "";
		if (month < 10) {
			Adfsf = "0" + month;
		}
		String safsg = day + "";
		if (day < 10) {
			safsg = "0" + day;
		}
		return year + "年" + Adfsf + "月" + safsg + "日";
	}

	public static String getWeekByDayString(String day) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date daaaaa = dateFormat.parse(day);
			return getadadf(daaaaa, true);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<MCityInfo> getHotCityList() {

		List<MCityInfo> list = new ArrayList<MCityInfo>();
		MCityInfo info = new MCityInfo();
		info.setsNum("54511");
		info.setsName("北京");
		list.add(info);
		info = null;
		MCityInfo info1 = new MCityInfo();
		info1.setsNum("58362");
		info1.setsName("上海");
		list.add(info1);
		info1 = null;

		MCityInfo info2 = new MCityInfo();
		info2.setsNum("59287");
		info2.setsName("广州");
		list.add(info2);
		info2 = null;

		MCityInfo info3 = new MCityInfo();
		info3.setsNum("59493");
		info3.setsName("深圳");
		list.add(info3);
		info3 = null;

		MCityInfo info4 = new MCityInfo();
		info4.setsNum("57494");
		info4.setsName("武汉");
		list.add(info4);
		info4 = null;

		MCityInfo info5 = new MCityInfo();
		info5.setsNum("58238");
		info5.setsName("南京");
		list.add(info5);
		info5 = null;

		MCityInfo info6 = new MCityInfo();
		info6.setsNum("57036");
		info6.setsName("西安");
		list.add(info6);
		info6 = null;

		MCityInfo info7 = new MCityInfo();
		info7.setsNum("56294");
		info7.setsName("成都");
		list.add(info7);
		info7 = null;

		MCityInfo info8 = new MCityInfo();
		info8.setsNum("58457");
		info8.setsName("杭州");
		list.add(info8);
		info8 = null;

		MCityInfo info9 = new MCityInfo();
		info9.setsNum("57083");
		info9.setsName("郑州");
		list.add(info9);
		info9 = null;

		MCityInfo info10 = new MCityInfo();
		info10.setsNum("57516");
		info10.setsName("重庆");
		list.add(info10);
		info10 = null;

		MCityInfo info11 = new MCityInfo();
		info11.setsNum("54342");
		info11.setsName("沈阳");
		list.add(info11);
		info11 = null;
		return list;
	}

	public static List<MCityInfo> getMoreCityInfos() {
		// TODO Auto-generated method stub
		List<MCityInfo> infos = getCityList();
		List<MCityInfo> hotInfos = getHotCityList();
		List<MCityInfo> cityInfos = new ArrayList<MCityInfo>();
		for (MCityInfo hotInfo : hotInfos) {
			for (MCityInfo mCityInfo : infos) {
				if (!mCityInfo.getsNum().equals(hotInfo.getsNum())) {
					cityInfos.add(mCityInfo);
				}
			}
		}
		return cityInfos;
	}

	public static ArrayList<Integer> getLineNumers(List<String> minNumbers) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void setQingYinImage(ImageView imageView, String qing,
			boolean isDay) {
		if (Util.isEmpty(qing)) {
			imageView.setBackgroundResource(R.drawable.qingicon);
		} else {
			if (isDay) {
				if (qing.contains("晴")) {
					imageView.setBackgroundResource(R.drawable.qingicon);
				}

				if (qing.contains("多云")) {
					imageView.setBackgroundResource(R.drawable.qingzhuanyuicon);
				}
			} else {

				if (qing.contains("晴")) {
					imageView.setBackgroundResource(R.drawable.qingnn);
				}

				if (qing.contains("多云")) {
					imageView.setBackgroundResource(R.drawable.duoyunicon);
				}

			}

			if (qing.contains("阴")) {
				imageView.setBackgroundResource(R.drawable.yinicon);
			} else if (qing.contains("小雪")) {
				imageView.setBackgroundResource(R.drawable.xiaoxueicon);
			} else if (qing.contains("中雪")) {
				imageView.setBackgroundResource(R.drawable.zhongxueicon);
			} else if (qing.contains("大雪")) {
				imageView.setBackgroundResource(R.drawable.daxueicon);
			} else if (qing.contains("暴雪")) {
				imageView.setBackgroundResource(R.drawable.baoxueicon);
			} else if (qing.contains("特大暴雪")) {
				imageView.setBackgroundResource(R.drawable.tedabaoxueicon);
			} else if (qing.contains("雷")) {
				imageView.setBackgroundResource(R.drawable.leiicon);
			} else if (qing.contains("雷阵雨")) {
				imageView.setBackgroundResource(R.drawable.leizhenyuicon);
			} else if (qing.contains("晴转雨")) {
				imageView.setBackgroundResource(R.drawable.qingzhuanyuicon);
			} else if (qing.contains("晴转雨")) {
				imageView.setBackgroundResource(R.drawable.qingzhuanyuicon);
			} else if (qing.contains("中雨")) {
				imageView.setBackgroundResource(R.drawable.zhongyuicon);
			} else if (qing.contains("大雨")) {
				imageView.setBackgroundResource(R.drawable.dayuicon);
			} else if (qing.contains("阵雨")) {
				imageView.setBackgroundResource(R.drawable.zhenyuicon);
			} else if (qing.contains("阵雪")) {
				imageView.setBackgroundResource(R.drawable.zhenxueicon);
			} else if (qing.contains("霾")) {
				imageView.setBackgroundResource(R.drawable.wumaiicon);
			} else if (qing.contains("雨")) {
				imageView.setBackgroundResource(R.drawable.yuicon);
			} else if (qing.contains("雾")) {
				imageView.setBackgroundResource(R.drawable.wuicon);
			}

		}

	}

	public static Drawable setQingYinImage1(MCityInfo qingdddd, int index,
			int indexDay) {
		String qing = null;
		if (index == 0) {
			qing = qingdddd.getTianqi();
		} else if (index == 1) {
			qing = qingdddd.getFeng();
		} else if (index == 2) {
			qing = qingdddd.getJiangshui();
		}

		if (Util.isEmpty(qing)) {
			return null;
		} else {
			if (index == 0) {
				if (qing.contains("00")) {
					if (indexDay >= 18 || indexDay <= 6) {

						return MApplication.getInstance().getResources()
								.getDrawable(R.drawable.qingnns);
					} else {
						return MApplication.getInstance().getResources()
								.getDrawable(R.drawable.qingicons);
					}

				} else if (qing.contains("02")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.yinicons);
				} else if (qing.contains("01")) {
					if (indexDay >= 18 || indexDay <= 6) {

						return MApplication.getInstance().getResources()
								.getDrawable(R.drawable.duoyunicons);
					} else {
						return MApplication.getInstance().getResources()
								.getDrawable(R.drawable.qingzhuanyuicons);
					}
				} else if (qing.contains("14")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.xiaoxueicons);
				} else if (qing.contains("15")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.zhongxueicons);
				} else if (qing.contains("16") || qing.contains("26")
						|| qing.contains("27") || qing.contains("28")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.daxueicons);
				} else if (qing.contains("17")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.baoxueicons);
				} else if (qing.contains("特大暴雪")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.tedabaoxueicons);
				} else if (qing.contains("雷")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.leiicons);
				} else if (qing.contains("04")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.leizhenyuicons);
				} else if (qing.contains("晴转雨")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.qingzhuanyuicons);
				} else if (qing.contains("08") || qing.contains("21")
						|| qing.contains("22") || qing.contains("23")
						|| qing.contains("24") || qing.contains("25")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.zhongyuicons);
				} else if (qing.contains("09") || qing.contains("10")
						|| qing.contains("11") || qing.contains("12")
						|| qing.contains("19")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.dayuicons);
				} else if (qing.contains("03")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.zhenyuicons);
				} else if (qing.contains("13")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.zhenxueicons);
				} else if (qing.contains("53") || qing.contains("54")
						|| qing.contains("55")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.wumaiicons);
				} else if (qing.contains("07") || qing.contains("06")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.yuicons);
				} else if (qing.contains("18")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.wuicons);
				}

			} else if (index == 1) {
				if (qing.equals("东北风")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.dongbei);
				} else if (qing.equals("东南风")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.dongnan);
				} else if (qing.equals("西南风")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.xinan);
				} else if (qing.equals("西北风")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.xibei);
				} else if (qing.contains("北")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.pianbei);
				} else if (qing.contains("东")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.piandong);
				} else if (qing.contains("南")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.piannan);
				} else if (qing.contains("西")) {
					return MApplication.getInstance().getResources()
							.getDrawable(R.drawable.pianxi);
				}
			} else if (index == 2) {

			}
		}
		return null;
	}
}
