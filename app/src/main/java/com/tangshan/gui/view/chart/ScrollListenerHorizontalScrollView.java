package com.tangshan.gui.view.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class ScrollListenerHorizontalScrollView extends HorizontalScrollView {
	public interface OnScrollListener {
		public void onScrollChanged(
                ScrollListenerHorizontalScrollView scrollView, int x,
                int offSet, int range, int extent);

		public void onEndScroll(ScrollListenerHorizontalScrollView scrollView);
	}

	private boolean mIsScrolling;
	private boolean mIsTouching;
	private Runnable mScrollingRunnable;
	private OnScrollListener mOnScrollListener;

	public ScrollListenerHorizontalScrollView(Context context) {
		this(context, null, 0);
	}

	public ScrollListenerHorizontalScrollView(Context context,
			AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollListenerHorizontalScrollView(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();

		if (action == MotionEvent.ACTION_MOVE) {
			mIsTouching = true;
			mIsScrolling = true;
		} else if (action == MotionEvent.ACTION_UP) {
			if (mIsTouching && !mIsScrolling) {
				if (mOnScrollListener != null) {
					mOnScrollListener.onEndScroll(this);
				}
			}

			mIsTouching = false;
		}

		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldX, int oldY) {
		super.onScrollChanged(x, y, oldX, oldY);

		if (Math.abs(oldX - x) > 0) {
			if (mScrollingRunnable != null) {
				removeCallbacks(mScrollingRunnable);
			}

			mScrollingRunnable = new Runnable() {
				public void run() {
					if (mIsScrolling && !mIsTouching) {
						if (mOnScrollListener != null) {
							mOnScrollListener
									.onEndScroll(ScrollListenerHorizontalScrollView.this);
						}
					}

					mIsScrolling = false;
					mScrollingRunnable = null;
				}
			};

			postDelayed(mScrollingRunnable, 200);
		}

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollChanged(this, x,
					computeHorizontalScrollOffset(),
					computeHorizontalScrollRange(),
					computeHorizontalScrollExtent());
		}
	}

	public OnScrollListener getOnScrollListener() {
		return mOnScrollListener;
	}

	public void setOnScrollListener(OnScrollListener mOnEndScrollListener) {
		this.mOnScrollListener = mOnEndScrollListener;
	}

}
