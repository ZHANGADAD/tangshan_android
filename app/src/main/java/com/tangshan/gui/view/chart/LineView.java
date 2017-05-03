package com.tangshan.gui.view.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.util.CMLog;
import com.tangshan.gui.util.Util;

/**
 * Created by Dacer on 11/4/13. Edited by Lee youngchan 21/1/14 Edited by dector
 * 30-Jun-2014
 */
public class LineView extends View {
	private int mViewHeight;
	// drawBackground
	private boolean autoSetDataOfGird = true;
	private boolean autoSetGridWidth = true;
	private int dataOfAGird = 10;
	private int bottomTextHeight = 0;
	private ArrayList<String> bottomTextList = new ArrayList<String>();
	private ArrayList<ArrayList<Integer>> dataLists;
	private List<String> showDataList;
	private List<MCityInfo> showDataListData;

	private ArrayList<Integer> xCoordinateList = new ArrayList<Integer>();
	private ArrayList<Integer> yCoordinateList = new ArrayList<Integer>();

	private ArrayList<ArrayList<Dot>> drawDotLists = new ArrayList<ArrayList<Dot>>();

	private Paint bottomTextPaint = new Paint();
	private int bottomTextDescent, parseColor, indexTab;

	// popup
	private Paint popupTextPaint = new Paint();
	private Paint popupTextPaintDot = new Paint();
	private final int bottomTriangleHeight = 12;
	public boolean showPopup = true;

	private Dot pointToSelect;
	private Dot selectedDot;

	private int viewHeightContent = MyUtils.dip2px(getContext(), 27);

	private int topLineLength = MyUtils.dip2px(getContext(), 12);; // | | ←this
																	// -+-+-
	private int sideLineLength = MyUtils.dip2px(getContext(), 45) / 3 * 2;// --+--+--+--+--+--+--
																			// ↑this
	private int backgroundGridWidth = MyUtils.dip2px(getContext(), 45);

	// Constants
	private final int popupTopPadding = MyUtils.dip2px(getContext(), 2);
	private final int popupBottomMargin = MyUtils.dip2px(getContext(), 5);
	private final int bottomTextTopMargin = MyUtils.sp2px(getContext(), 5);
	private final int bottomLineLength = MyUtils.sp2px(getContext(), 22);
	private final int DOT_INNER_CIR_RADIUS = MyUtils.dip2px(getContext(), 2);
	private final int DOT_OUTER_CIR_RADIUS = MyUtils.dip2px(getContext(), 5);
	private final int MIN_TOP_LINE_LENGTH = MyUtils.dip2px(getContext(), 12);
	private final int MIN_VERTICAL_GRID_NUM = 4;
	private final int MIN_HORIZONTAL_GRID_NUM = 1;
	private final int BACKGROUND_LINE_COLOR = Color.parseColor("#99FFFFFF");
	private final int BOTTOM_TEXT_COLOR = Color.parseColor("#99FFFFFF");

	public static final int SHOW_POPUPS_All = 1;
	public static final int SHOW_POPUPS_MAXMIN_ONLY = 2;
	public static final int SHOW_POPUPS_NONE = 3;

	private int showPopupType = SHOW_POPUPS_NONE;

	public void setShowPopup(int popupType) {
		this.showPopupType = popupType;
	}

	private Boolean drawDotLine = true;
	private String[] colorArray = { "#FFFFFF", "#FFFFFF", "#FFFFFF" };

	// onDraw optimisations
	private final Point tmpPoint = new Point();

	public void setDrawDotLine(Boolean drawDotLine) {
		this.drawDotLine = drawDotLine;
	}

	private Runnable animator = new Runnable() {
		@Override
		public void run() {
			boolean needNewFrame = false;
			for (ArrayList<Dot> data : drawDotLists) {
				for (Dot dot : data) {
					dot.update();
					if (!dot.isAtRest()) {
						needNewFrame = true;
					}
				}
			}
			if (needNewFrame) {
				postDelayed(this, 25);
			}
			invalidate();
		}
	};

	public LineView(Context context) {
		this(context, null);
	}

	public LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		popupTextPaint.setAntiAlias(true);
		popupTextPaint.setColor(Color.WHITE);
		popupTextPaint.setTextSize(MyUtils.sp2px(getContext(), 13));
		popupTextPaint.setStrokeWidth(5);
		popupTextPaint.setTextAlign(Paint.Align.CENTER);

		bottomTextPaint.setAntiAlias(true);
		bottomTextPaint.setTextSize(MyUtils.sp2px(getContext(), 12));
		bottomTextPaint.setTextAlign(Paint.Align.CENTER);
		bottomTextPaint.setStyle(Paint.Style.FILL);
		bottomTextPaint.setColor(BOTTOM_TEXT_COLOR);

		popupTextPaintDot.setColor(Color.WHITE);
		popupTextPaintDot.setAntiAlias(true);
		popupTextPaintDot.setTextSize(MyUtils.sp2px(getContext(), 6));
	}

	/**
	 * dataList will be reset when called is method.
	 * 
	 * @param bottomTextList
	 *            The String ArrayList in the bottom.
	 */
	public void setBottomTextList(ArrayList<String> bottomTextList) {
		this.bottomTextList = bottomTextList;

		Rect r = new Rect();
		int longestWidth = 0;
		String longestStr = "";
		bottomTextDescent = 0;
		for (String s : bottomTextList) {
			bottomTextPaint.getTextBounds(s, 0, s.length(), r);
			if (bottomTextHeight < r.height()) {
				bottomTextHeight = r.height();
			}
			if (autoSetGridWidth && (longestWidth < r.width())) {
				longestWidth = r.width();
				longestStr = s;
			}
			if (bottomTextDescent < (Math.abs(r.bottom))) {
				bottomTextDescent = Math.abs(r.bottom);
			}
		}

		if (autoSetGridWidth) {
			if (backgroundGridWidth < longestWidth) {
				backgroundGridWidth = longestWidth
						+ (int) bottomTextPaint.measureText(longestStr, 0, 1);
			}
			if (sideLineLength < longestWidth / 2) {
				sideLineLength = longestWidth / 2;
			}
		}

		refreshXCoordinateList(getHorizontalGridNum());
	}

	/**
	 * 
	 * @param dataLists
	 *            The Integer ArrayLists for showing, dataList.size() must <
	 *            bottomTextList.size()
	 */
	public void setDataList(ArrayList<ArrayList<Integer>> dataLists) {
		selectedDot = null;
		this.dataLists = dataLists;
		for (ArrayList<Integer> list : dataLists) {
			if (list.size() > bottomTextList.size()) {
				throw new RuntimeException("dacer.LineView error:"
						+ " dataList.size() > bottomTextList.size() !!!");
			}
		}
		int biggestData = 0;
		for (ArrayList<Integer> list : dataLists) {
			if (autoSetDataOfGird) {
				for (Integer i : list) {
					if (biggestData < i) {
						biggestData = i;
					}
				}
			}
			dataOfAGird = 1;
			while (biggestData / 10 > dataOfAGird) {
				dataOfAGird *= 10;
			}
		}

		refreshAfterDataChanged();
		showPopup = true;
		setMinimumWidth(0); // It can help the LineView reset the Width,
							// I don't know the better way..
		postInvalidate();
	}

	private void refreshAfterDataChanged() {
		int verticalGridNum = getVerticalGridlNum();
		refreshTopLineLength(verticalGridNum);
		refreshYCoordinateList(verticalGridNum);
		refreshDrawDotList(verticalGridNum);
	}

	private int getVerticalGridlNum() {
		int verticalGridNum = MIN_VERTICAL_GRID_NUM;
		if (dataLists != null && !dataLists.isEmpty()) {
			for (ArrayList<Integer> list : dataLists) {
				for (Integer integer : list) {
					if (verticalGridNum < (integer + 1)) {
						verticalGridNum = integer + 1;
					}
				}
			}
		}
		return verticalGridNum;
	}

	private int getHorizontalGridNum() {
		int horizontalGridNum = bottomTextList.size() - 1;
		if (horizontalGridNum < MIN_HORIZONTAL_GRID_NUM) {
			horizontalGridNum = MIN_HORIZONTAL_GRID_NUM;
		}
		return horizontalGridNum;
	}

	private void refreshXCoordinateList(int horizontalGridNum) {
		xCoordinateList.clear();
		if (horizontalGridNum < 10) {
			for (int i = 0; i < (horizontalGridNum + 1); i++) {
				backgroundGridWidth = (MyUtils.getWidth(getContext()) - sideLineLength * 2)
						/ horizontalGridNum;
				xCoordinateList.add(sideLineLength + backgroundGridWidth * i);
			}
		} else {
			for (int i = 0; i < (horizontalGridNum + 1); i++) {
				xCoordinateList.add(sideLineLength + backgroundGridWidth * i);
			}
		}

	}

	private void refreshYCoordinateList(int verticalGridNum) {
		yCoordinateList.clear();
		for (int i = 0; i < (verticalGridNum + 1); i++) {
			yCoordinateList.add(topLineLength
					+ ((mViewHeight - viewHeightContent - topLineLength
							- bottomTextHeight - bottomTextTopMargin
							- bottomLineLength - bottomTextDescent)
							* i / (verticalGridNum)));
		}
	}

	private void refreshDrawDotList(int verticalGridNum) {
		if (dataLists != null && !dataLists.isEmpty()) {
			if (drawDotLists.size() == 0) {
				for (int k = 0; k < dataLists.size(); k++) {
					drawDotLists.add(new ArrayList<Dot>());
				}
			}
			for (int k = 0; k < dataLists.size(); k++) {
				int drawDotSize = drawDotLists.get(k).isEmpty() ? 0
						: drawDotLists.get(k).size();

				for (int i = 0; i < dataLists.get(k).size(); i++) {
					int x = xCoordinateList.get(i);
					int index = verticalGridNum - dataLists.get(k).get(i);
					int y = yCoordinateList.get(index);
					if (i > drawDotSize - 1) {
						drawDotLists.get(k)
								.add(new Dot(x, 0, x, y, dataLists.get(k)
										.get(i), k));
					} else {
						drawDotLists.get(k).set(
								i,
								drawDotLists
										.get(k)
										.get(i)
										.setTargetData(x, y,
												dataLists.get(k).get(i), k));
					}
				}

				int temp = drawDotLists.get(k).size() - dataLists.get(k).size();
				for (int i = 0; i < temp; i++) {
					drawDotLists.get(k).remove(drawDotLists.get(k).size() - 1);
				}
			}
		}
		removeCallbacks(animator);
		post(animator);
	}

	private void refreshTopLineLength(int verticalGridNum) {
		// For prevent popup can't be completely showed when
		// backgroundGridHeight is too small.
		// But this code not so good.
		if ((mViewHeight - viewHeightContent - topLineLength - bottomTextHeight - bottomTextTopMargin)
				/ (verticalGridNum + 2) < getPopupHeight()) {
			topLineLength = getPopupHeight() + DOT_OUTER_CIR_RADIUS
					+ DOT_INNER_CIR_RADIUS + 2;
		} else {
			topLineLength = MIN_TOP_LINE_LENGTH;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackgroundLines(canvas);
		drawLines(canvas);
		drawDots(canvas);

		for (int k = 0; k < drawDotLists.size(); k++) {
			int MaxValue = Collections.max(dataLists.get(k));
			int MinValue = Collections.min(dataLists.get(k));

			for (int i = 0; i < drawDotLists.get(k).size(); i++) {
				Dot d = drawDotLists.get(k).get(i);
				if (showPopupType == SHOW_POPUPS_All)
					drawPopup(canvas, showDataList.get(i),
							d.setupPoint(tmpPoint), 0);
				else if (showPopupType == SHOW_POPUPS_MAXMIN_ONLY) {
					if (d.data == MaxValue)
						drawPopup(canvas, showDataList.get(i),
								d.setupPoint(tmpPoint), 0);
					if (d.data == MinValue)
						drawPopup(canvas, showDataList.get(i),
								d.setupPoint(tmpPoint), 0);
				}

			}
		}
		// 画Tip
		if (showPopup && selectedDot != null) {
			drawPopup(canvas, String.valueOf(selectedDot.data),
					selectedDot.setupPoint(tmpPoint), 0);
		}
	}

	/**
	 * 
	 * @param canvas
	 *            The canvas you need to draw on.
	 * @param point
	 *            The Point consists of the x y coordinates from left bottom to
	 *            right top. Like is
	 * 
	 *            3 2 1 0 1 2 3 4 5
	 */
	private void drawPopup(Canvas canvas, String num, Point point, int color) {
		// boolean singularNum = (num.length() == 1);
		// int sidePadding = MyUtils.dip2px(getContext(), singularNum ? 8 : 5);
		if (bottomTextList.size() > 10)
			return;
		int x = point.x;
		int y = point.y - MyUtils.dip2px(getContext(), 5);
		Rect popupTextRect = new Rect();
		popupTextPaint.getTextBounds(num, 0, num.length(), popupTextRect);
		// Rect r = new Rect(x - popupTextRect.width() / 2 - sidePadding, y
		// - popupTextRect.height() - bottomTriangleHeight
		// - popupTopPadding * 2 - popupBottomMargin, x
		// + popupTextRect.width() / 2 + sidePadding, y + popupTopPadding
		// - popupBottomMargin);
		// NinePatchDrawable popup = (NinePatchDrawable) getResources()
		// .getDrawable(PopupColor);
		// popup.setBounds(r);
		// popup.draw(canvas);
		canvas.drawText(num, x, y - bottomTriangleHeight - popupBottomMargin
				- 2, popupTextPaint);
		Rect popupTextRectDot = new Rect();
		popupTextPaintDot.getTextBounds("O", 0, "O".length(), popupTextRectDot);
		canvas.drawText(
				"O",
				x + popupTextPaint.measureText(num) / 2,
				y - bottomTriangleHeight - popupBottomMargin - 2
						- MyUtils.dip2px(getContext(), 6), popupTextPaintDot);
	}

	private int getPopupHeight() {
		Rect popupTextRect = new Rect();
		popupTextPaint.getTextBounds("9", 0, 1, popupTextRect);
		Rect r = new Rect(-popupTextRect.width() / 2, -popupTextRect.height()
				- bottomTriangleHeight - popupTopPadding * 2
				- popupBottomMargin, +popupTextRect.width() / 2,
				+popupTopPadding - popupBottomMargin);
		return r.height();
	}

	// 画点
	private void drawDots(Canvas canvas) {
		Paint bigCirPaint = new Paint();
		bigCirPaint.setAntiAlias(true);
		if (drawDotLists != null && !drawDotLists.isEmpty()) {
			for (int k = 0; k < drawDotLists.size(); k++) {
				bigCirPaint.setColor(Color.parseColor(colorArray[k % 3]));
				for (Dot dot : drawDotLists.get(k)) {
					canvas.drawCircle(dot.x, dot.y, DOT_OUTER_CIR_RADIUS,
							bigCirPaint);
				}
			}
		}
	}

	// 画线
	private void drawLines(Canvas canvas) {
		Paint linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(MyUtils.dip2px(getContext(), 2));
		for (int k = 0; k < drawDotLists.size(); k++) {
			linePaint.setColor(parseColor);
			if (drawDotLists.get(k).size() < 10) {
				for (int i = 1; i < drawDotLists.get(k).size() - 1; i++) {
					canvas.drawLine(drawDotLists.get(k).get(i).x, drawDotLists
							.get(k).get(i).y, drawDotLists.get(k).get(i + 1).x,
							drawDotLists.get(k).get(i + 1).y, linePaint);
				}

				Paint adsf = new Paint();
				adsf.setAntiAlias(true);
				adsf.setStrokeWidth(MyUtils.dip2px(getContext(), 2));
				PathEffect effects = new DashPathEffect(new float[] { 10, 5 },
						MyUtils.dip2px(getContext(), 2));
				adsf.setPathEffect(effects);
				adsf.setColor(parseColor);
				canvas.drawLine(drawDotLists.get(k).get(0).x,
						drawDotLists.get(k).get(0).y, drawDotLists.get(k)
								.get(1).x, drawDotLists.get(k).get(1).y, adsf);
			} else {
				for (int i = 0; i < drawDotLists.get(k).size() - 1; i++) {
					canvas.drawLine(drawDotLists.get(k).get(i).x, drawDotLists
							.get(k).get(i).y, drawDotLists.get(k).get(i + 1).x,
							drawDotLists.get(k).get(i + 1).y, linePaint);
					int indexDay = Integer.parseInt(this.bottomTextList.get(i)
							.split(":")[0]);
					Drawable drawable = Util.setQingYinImage1(
							showDataListData.get(i), indexTab,indexDay);
					if (drawable != null) {
						Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
						int x = drawDotLists.get(k).get(i).x
								- drawable.getIntrinsicWidth() / 2;
						int y = drawDotLists.get(k).get(i).y
								- drawable.getIntrinsicHeight() - 5;
						canvas.drawBitmap(bitmap, x, y, null);
					}
				}
				int i=drawDotLists.get(k).size() - 1;
				int indexDay = Integer.parseInt(this.bottomTextList.get(i)
						.split(":")[0]);
				Drawable drawable = Util.setQingYinImage1(
						showDataListData.get(i), indexTab,indexDay);
				if (drawable != null) {
					Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
					int x = drawDotLists.get(k).get(i).x
							- drawable.getIntrinsicWidth() / 2;
					int y = drawDotLists.get(k).get(i).y
							- drawable.getIntrinsicHeight() - 5;
					canvas.drawBitmap(bitmap, x, y, null);
				}
			}

		}
	}

	private void drawBackgroundLines(Canvas canvas) {
		if (bottomTextList.size() < 10)
			return;
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(MyUtils.dip2px(getContext(), 1f));
		paint.setColor(BACKGROUND_LINE_COLOR);
		PathEffect effects = new DashPathEffect(new float[] { 10, 5, 10, 5 }, 1);
		// draw dotted lines
		paint.setPathEffect(effects);
		Path dottedPath = new Path();
		int size = yCoordinateList.size();
		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				dottedPath.moveTo(0, yCoordinateList.get(i));
				dottedPath.lineTo(getWidth(), yCoordinateList.get(i));
				canvas.drawPath(dottedPath, paint);
			} else if (i == 1) {
				Integer adafBotttom = yCoordinateList.get(0);
				Integer addadTop = yCoordinateList.get(size - 1);
				Integer adddMiddle = (addadTop - adafBotttom) / 2 + adafBotttom;
				dottedPath.moveTo(0, adddMiddle);
				dottedPath.lineTo(getWidth(), adddMiddle);
				canvas.drawPath(dottedPath, paint);
			} else {
				dottedPath.moveTo(0, yCoordinateList.get(size - 1));
				dottedPath.lineTo(getWidth(), yCoordinateList.get(size - 1));
				canvas.drawPath(dottedPath, paint);
			}
		}

		// draw bottom text
		for (int i = 0; i < bottomTextList.size(); i++) {
			canvas.drawText(bottomTextList.get(i), sideLineLength
					+ backgroundGridWidth * i, mViewHeight - bottomTextDescent,
					bottomTextPaint);
		}

		if (!drawDotLine) {
			// draw solid lines
			for (int i = 0; i < yCoordinateList.size(); i++) {
				if ((yCoordinateList.size() - 1 - i) % dataOfAGird == 0) {
					canvas.drawLine(0, yCoordinateList.get(i), getWidth(),
							yCoordinateList.get(i), paint);
				}
			}
		}
		Paint paint2 = new Paint();
		paint2.setStyle(Paint.Style.STROKE);
		paint2.setStrokeWidth(MyUtils.dip2px(getContext(), 1f));
		paint2.setColor(Color.WHITE);
		Path dottedPath1 = new Path();
		dottedPath1.moveTo(
				0,
				mViewHeight - bottomTextDescent
						- MyUtils.dip2px(getContext(), 20));
		dottedPath1.lineTo(getWidth(), mViewHeight - bottomTextDescent
				- MyUtils.dip2px(getContext(), 20));
		canvas.drawPath(dottedPath1, paint2);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mViewWidth = measureWidth(widthMeasureSpec);
		mViewHeight = measureHeight(heightMeasureSpec);
		// mViewHeight = MeasureSpec.getSize(measureSpec);
		refreshAfterDataChanged();
		setMeasuredDimension(mViewWidth, mViewHeight);
	}

	private int measureWidth(int measureSpec) {
		int horizontalGridNum = getHorizontalGridNum();
		if (horizontalGridNum < 10) {
			return getMeasurement(measureSpec, MyUtils.getWidth(getContext()));
		} else {
			int preferred = backgroundGridWidth * horizontalGridNum
					+ sideLineLength * 2;
			return getMeasurement(measureSpec, preferred);
		}

	}

	private int measureHeight(int measureSpec) {
		int preferred = 0;
		return getMeasurement(measureSpec, preferred);
	}

	private int getMeasurement(int measureSpec, int preferred) {
		int specSize = MeasureSpec.getSize(measureSpec);
		int measurement;
		switch (MeasureSpec.getMode(measureSpec)) {
		case MeasureSpec.EXACTLY:
			measurement = specSize;
			break;
		case MeasureSpec.AT_MOST:
			measurement = Math.min(preferred, specSize);
			break;
		default:
			measurement = preferred;
			break;
		}
		return measurement;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			pointToSelect = findPointAt((int) event.getX(), (int) event.getY());
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (pointToSelect != null) {
				selectedDot = pointToSelect;
				pointToSelect = null;
				postInvalidate();
			}
		}

		return true;
	}

	private Dot findPointAt(int x, int y) {
		if (drawDotLists.isEmpty()) {
			return null;
		}

		final int width = backgroundGridWidth / 2;
		final Region r = new Region();

		for (ArrayList<Dot> data : drawDotLists) {
			for (Dot dot : data) {
				final int pointX = dot.x;
				final int pointY = dot.y;

				r.set(pointX - width, pointY - width, pointX + width, pointY
						+ width);
				if (r.contains(x, y)) {
					return dot;
				}
			}
		}

		return null;
	}

	class Dot {
		int x;
		int y;
		int data;
		int targetX;
		int targetY;
		int linenumber;
		int velocity = MyUtils.dip2px(getContext(), 18);

		Dot(int x, int y, int targetX, int targetY, Integer data, int linenumber) {
			this.x = x;
			this.y = y;
			this.linenumber = linenumber;
			setTargetData(targetX, targetY, data, linenumber);
		}

		Point setupPoint(Point point) {
			point.set(x, y);
			return point;
		}

		Dot setTargetData(int targetX, int targetY, Integer data, int linenumber) {
			this.targetX = targetX;
			this.targetY = targetY;
			this.data = data;
			this.linenumber = linenumber;
			return this;
		}

		boolean isAtRest() {
			return (x == targetX) && (y == targetY);
		}

		void update() {
			x = updateSelf(x, targetX, velocity);
			y = updateSelf(y, targetY, velocity);
		}

		private int updateSelf(int origin, int target, int velocity) {
			if (origin < target) {
				origin += velocity;
			} else if (origin > target) {
				origin -= velocity;
			}
			if (Math.abs(target - origin) < velocity) {
				origin = target;
			}
			return origin;
		}
	}

	public void setShowDataList(List<String> showDataList, int indexTab) {
		// TODO Auto-generated method stub
		this.indexTab = indexTab;
		this.showDataList = showDataList;
	}

	public void setShowDataListData(List<MCityInfo> showDataList) {
		// TODO Auto-generated method stub
		this.showDataListData = showDataList;
	}

	public void setLineColor(int parseColor) {
		// TODO Auto-generated method stub
		this.parseColor = parseColor;
	}
}
