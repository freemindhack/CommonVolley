package com.iwillow.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * 一个九宫格华东解锁自定义视图
 * @author Eric Gao
 *
 */
@SuppressLint("NewApi")
public class LockView extends View {

	/**
	 * 默认密码
	 */
	private final String PASSWORD_DEFAULT = "12589";
	/**
	 * 默认颜色
	 */
	private static final int COLOR_DEFAULT = 0xff8bc5ba;
	/**
	 * 密码校验通过时的颜色
	 */
	private static final int COLOR_ACTIVE = 0xff33ffdd;
	/**
	 * 密码不正确的颜色
	 */
	private static final int COLOR_ERROR = 0xffdc143c;
	/**
	 * 连接线默认时的状态
	 */
	private static final int LINE_DEFAULT = 1;
	/**
	 * 密码正确时连接线状态
	 */
	private static final int LINE_ACTIVE = 2;
	/**
	 * 密码错误是连线的状态
	 */
	private static final int LINE_ERROR = 3;
	/**
	 * 连接线的状态
	 */
	private int lineState = LINE_DEFAULT;
	/**
	 * 九宫格点
	 */
	private CirclePoint[] mPointArray = new CirclePoint[9];

	/**
	 * 采集的点
	 */
	private List<CirclePoint> mPointList = new ArrayList<CirclePoint>();
	/**
	 * 点是已经否初始化
	 */
	private boolean pointArrayInited;
	/**
	 * 每一个圆的半径
	 */
	private int mRadius;

	/**
	 * 记录手指滑动所到之处的点的X坐标
	 */
	private int eventX;
	/**
	 * 记录手指滑动所到之处的点的Y坐标
	 */
	private int eventY;
	/**
	 * 记录手指滑动轨迹的点
	 */
	private CirclePoint mMousePoint = new CirclePoint();

	/**
	 * 只有当用户点击了九宫格某一个点的时候才能开始绘制
	 */
	private boolean canCollectPoints = false;

	/**
	 * 点是否绘采集完毕，也就是判定手机是否脱离屏幕
	 */
	private boolean isFinished = false;

	public LockView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}

	public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public LockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public LockView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!pointArrayInited) {
			initPoint();
		}
		drawCircle(canvas);
		if (mPointList.size() > 0) {
			CirclePoint a = mPointList.get(0);
			CirclePoint b = null;
			// 将所有采集到的点首尾相连
			for (int i = 1; i < mPointList.size(); i++) {
				b = mPointList.get(i);
				drawLine(a, b, canvas);
				a = b;
			}
			// 在手未脱离手机屏幕的时候，继续绘制轨迹线
			if (!isFinished) {
				drawLine(a, mMousePoint, canvas);
			}
		}
	}

	/**
	 * 初始化所有的九宫格点
	 */
	private void initPoint() {
		mPointArray = new CirclePoint[9];
		int witdh = getWidth();
		int height = getHeight();
		/*
		 * 竖屏时，九宫格的的宽度与高度为屏幕宽度的1/2为基准 。 横屏时，九宫格的的宽度与高度为屏幕高度的1/2为基准。
		 */
		int benchmark = witdh <= height ? witdh : height;
		/*
		 * 点之间的间距
		 */
		int pointOffset = benchmark / 4;
		if (witdh <= height) {// 竖屏
			for (int i = 0; i < 3; i++) {
				mPointArray[i] = new CirclePoint();
				mPointArray[i + 3] = new CirclePoint();
				mPointArray[i + 6] = new CirclePoint();
				mPointArray[i].index = i + 1;
				mPointArray[i + 3].index = i + 4;
				mPointArray[i + 6].index = i + 7;
				mPointArray[i].x = pointOffset * (i + 1);
				mPointArray[i + 3].x = pointOffset * (i + 1);
				mPointArray[i + 6].x = pointOffset * (i + 1);
				mPointArray[i].y = (height - witdh) / 2 + pointOffset;
				mPointArray[i + 3].y = (height - witdh) / 2 + 2 * pointOffset;
				mPointArray[i + 6].y = (height - witdh) / 2 + 3 * pointOffset;
				mPointArray[i].state = CirclePoint.STATE_DEFAULT;
				mPointArray[i + 3].state = CirclePoint.STATE_DEFAULT;
				mPointArray[i + 6].state = CirclePoint.STATE_DEFAULT;
			}
		} else {// 横屏
			for (int i = 0; i < 3; i++) {
				mPointArray[i] = new CirclePoint();
				mPointArray[i + 3] = new CirclePoint();
				mPointArray[i + 6] = new CirclePoint();
				mPointArray[i].index = i + 1;
				mPointArray[i + 3].index = i + 4;
				mPointArray[i + 6].index = i + 7;
				mPointArray[i].x = (witdh - height) / 2 + pointOffset * (i + 1);
				mPointArray[i + 3].x = (witdh - height) / 2 + pointOffset
						* (i + 1);
				mPointArray[i + 6].x = (witdh - height) / 2 + pointOffset
						* (i + 1);
				mPointArray[i].y = pointOffset;
				mPointArray[i + 3].y = 2 * pointOffset;
				mPointArray[i + 6].y = 3 * pointOffset;
				mPointArray[i].state = CirclePoint.STATE_DEFAULT;
				mPointArray[i + 3].state = CirclePoint.STATE_DEFAULT;
				mPointArray[i + 6].state = CirclePoint.STATE_DEFAULT;
			}
		}
		/*
		 * 
		 * 两点之间的六分之一作为圆的半径
		 */
		mRadius = pointOffset / 6;
		pointArrayInited = true;
	}

	/**
	 * 绘制九宫格点
	 * 
	 * @param canvas
	 */
	private void drawCircle(Canvas canvas) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);// 绘图为填充模式
		for (CirclePoint point : mPointArray) {
			if (point.state == CirclePoint.STATE_ACTIVE) {
				paint.setColor(COLOR_ACTIVE);// 激活颜色
			} else if (point.state == CirclePoint.STATE_ERROR) {
				paint.setColor(COLOR_ERROR);// 错误颜色
			} else {
				paint.setColor(COLOR_DEFAULT);// 默认颜色
			}
			canvas.drawCircle(point.x, point.y, mRadius, paint);
		}
	}

	/**
	 * 绘制线
	 * 
	 * @param startPoint
	 *            起始点
	 * @param stopPoint
	 *            结束点
	 * @param canvas
	 */
	private void drawLine(CirclePoint startPoint, CirclePoint stopPoint,
			Canvas canvas) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);// 默认绘图为填充模式
		paint.setStrokeWidth(mRadius / 2);
		if (lineState == LINE_ACTIVE) {
			paint.setColor(COLOR_ACTIVE);
		} else if (lineState == LINE_ERROR) {
			paint.setColor(COLOR_ERROR);
		} else {
			paint.setColor(COLOR_DEFAULT);
		}
		canvas.drawLine(startPoint.x, startPoint.y, stopPoint.x, stopPoint.y,
				paint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		eventX = (int) event.getX();
		eventY = (int) event.getY();
		mMousePoint.x = eventX;
		mMousePoint.y = eventY;
		CirclePoint point = null;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isFinished = false;
			lineState = LINE_DEFAULT;
			mPointList.clear();
			for (CirclePoint p : mPointArray) {
				p.state = CirclePoint.STATE_DEFAULT;
			}
			// 只有当从九宫格的某个点开始滑动时才采集点
			point = isInLockArea(eventX, eventY);
			if (point != null) {
				canCollectPoints = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:

			if (canCollectPoints) {
				point = isInLockArea(eventX, eventY);
			}
			break;
		case MotionEvent.ACTION_UP:
			// 当手指从屏幕抬起时，不再采集点
			canCollectPoints = false;
			isFinished = true;
			if (mPointList.size() > 3) {
				String psd = "";
				for (int i = 0; i < mPointList.size(); i++) {
					psd += mPointList.get(i).index;
				}
				Log.d(VIEW_LOG_TAG, "psd:" + psd);
				if (psd.equals(PASSWORD_DEFAULT)) {
					for (int i = 0; i < mPointList.size(); i++) {
						mPointList.get(i).state = CirclePoint.STATE_ACTIVE;
					}
					lineState = LINE_ACTIVE;
					Toast.makeText(getContext(), "解锁成功", Toast.LENGTH_SHORT)
							.show();
				} else {
					for (int i = 0; i < mPointList.size(); i++) {
						mPointList.get(i).state = CirclePoint.STATE_ERROR;
					}
					lineState = LINE_ERROR;
					Toast.makeText(getContext(), "密码不正确", Toast.LENGTH_SHORT)
							.show();
				}
			} else if (mPointList.size() > 1 && mPointList.size() <= 3) {
				for (int i = 0; i < mPointList.size(); i++) {
					mPointList.get(i).state = CirclePoint.STATE_ERROR;
				}
				lineState = LINE_ERROR;
				Toast.makeText(getContext(), "至少选择四个点", Toast.LENGTH_SHORT)
						.show();
			} else if (mPointList.size() == 1) {
				mPointList.get(0).state = CirclePoint.STATE_DEFAULT;
				lineState = LINE_DEFAULT;
			}
			break;
		default:
			break;
		}
		if (point != null && canCollectPoints) {
			boolean existed = false;
			// 过滤掉重复的点
			for (int i = 0; i < mPointList.size(); i++) {
				if (mPointList.get(i).equals(point)) {
					existed = true;
					break;
				}
			}
			if (!existed) {
				mPointList.add(point);
			}
		}
		// 通知重绘
		postInvalidate();
		return true;
	}

	/**
	 * @param x
	 *            触摸点的X坐标
	 * @param y
	 *            触摸点的Y坐标
	 * @return 返回在本触摸点选中的某一个九宫格点
	 */
	private CirclePoint isInLockArea(int x, int y) {
		for (int i = 0; i < mPointArray.length; i++) {
			int r = (int) Math.sqrt((mPointArray[i].x - x)
					* (mPointArray[i].x - x) + (mPointArray[i].y - y)
					* (mPointArray[i].y - y));
			if (r < mRadius) {
				return mPointArray[i];
			}
		}

		return null;
	}

	class CirclePoint implements Comparable<CirclePoint> {
		/**
		 * 默认状态
		 */
		static final int STATE_DEFAULT = 0;
		/**
		 * 激活状态
		 */
		static final int STATE_ACTIVE = 1;
		/**
		 * 错误状态
		 */
		static final int STATE_ERROR = 2;
		/**
		 * 记录每一个点的索引
		 */
		int index;
		/**
		 * 每一个点的X坐标
		 */
		int x;
		/**
		 * 每一个点的Y坐标
		 */
		int y;
		/**
		 * 每一个点的状态（分为默认、激活、错误三种状态）
		 */
		int state = STATE_DEFAULT;

		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			if (o == null) {
				return false;
			}
			if (this == o) {
				return true;
			}
			if (o instanceof CirclePoint) {
				CirclePoint point = (CirclePoint) o;
				if (this.x == point.x && this.y == point.y) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		}

		@Override
		public int compareTo(CirclePoint another) {
			// TODO Auto-generated method stub
			if (this.x == another.x && this.y == another.y) {
				return 0;
			}
			return -1;
		}

	}
}
