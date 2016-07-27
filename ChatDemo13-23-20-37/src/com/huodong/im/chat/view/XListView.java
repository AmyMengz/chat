package com.huodong.im.chat.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.huodong.im.chat.util.MyRotateAnimation;
import com.huodong.im.chatdemo.R;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class XListView extends ListView implements OnScrollListener, android.widget.AdapterView.OnItemClickListener {

	public static final int PULL_DOWN_REFRESH = 1;// 下拉刷新
	public static final int RELEASE_REFRESH = 2;// 松开刷新
	public static final int REFRESHING = 3;// 正在刷新

	private View mHeaderView;// 头部根布局
	public View mFooterView;// 脚布局
	private int mHeaderHeight;// 头布局高度
	private int mFooterHeight;// 脚布局高度

	private int mCurrentState = PULL_DOWN_REFRESH;// 当前下拉刷新的状态

	private int startY = -1;// 起始Y坐标

	private ImageView ivArrow;// 箭头图标
	public ImageView head_iv_progress;// 进度条
	private TextView tvTitle;// 下拉刷新文字
	private FrameLayout head_fl_progress;
	private TextView tvTime;// 下拉刷新时间
	private RotateAnimation animUp;
	private RotateAnimation animDown;

	public RefreshListener mListener;// 下拉刷新监听
	public boolean isLoadMore = false;// 表示是否正在加载更多

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public XListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}

	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_pull_list_header);
		head_iv_progress = (ImageView) mHeaderView.findViewById(R.id.iv_progress);
		head_fl_progress = (FrameLayout) mHeaderView.findViewById(R.id.fl_progress);
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_pull_list_header_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_pull_list_header_time);

		tvTime.setText(getCurrentTime());

		this.addHeaderView(mHeaderView);

		mHeaderView.measure(0, 0);// 测量View
		mHeaderHeight = mHeaderView.getMeasuredHeight();// 获取View的高度
		mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);// 隐藏头布局

		initAnimation();
	}

	public ImageView iv_progress;

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
		iv_progress = (ImageView) mFooterView.findViewById(R.id.iv_progress);
		this.addFooterView(mFooterView);

		mFooterView.measure(0, 0);// 测量View
		mFooterHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mFooterHeight, 0, 0);// 隐藏头布局
		setOnScrollListener(this);
	}

	/**
	 * 初始化箭头的旋转动画
	 */
	private void initAnimation() {
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);
	}

	boolean is_intercept = true;
	private long firstClickTime;

	private void intercept() {
		if (firstClickTime > 0) {// 发现之前点击过一次
			long interval = System.currentTimeMillis() - firstClickTime;
			if (interval < 500) {// 判断两次点击是否小于500毫秒
				firstClickTime = 0;// 重置时间, 重新开始
				is_intercept = false;
				return;
			}
		}
		firstClickTime = System.currentTimeMillis();
	}

	int dY;
	public boolean has_head_view = true;

	public boolean isHas_head_view() {
		return has_head_view;
	}

	public void setHas_head_view(boolean has_head_view) {
		this.has_head_view = has_head_view;
		if(!has_head_view){
			if(mHeaderView!=null){
				this.removeHeaderView(mHeaderView); // 暂时注释
			}
		}
	}

	int lastY;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int pos = this.getFirstVisiblePosition();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = (int) ev.getY();
			intercept();
			is_intercept = true;
			startY = (int) ev.getY();
			 break;
		case MotionEvent.ACTION_MOVE:

			// 如果当前正在刷新, 不做任何处理
			if (mCurrentState == REFRESHING) {
				break;
			}

			int endY = (int) ev.getY();
			int move = endY - lastY;
			lastY = endY;
			int firstVisiblePosition = getFirstVisiblePosition();// 查看第一个显示的item属于第几个
			if (move > 0 && firstVisiblePosition == 0 && !has_head_view)
				break;
			dY = endY - startY;// 移动偏移量
			// LogUtils.d("firstVisiblePosition=" + firstVisiblePosition);
			int lastVisiblePos = getLastVisiblePosition();// 查看第最后一个显示的item属于第几个
			if (dY > 0 && firstVisiblePosition == 0) {// 向下移动
				int paddingTop = dY - mHeaderHeight;
				if (paddingTop > 0 && mCurrentState != RELEASE_REFRESH) {// 进入松开刷新的状态
					mCurrentState = RELEASE_REFRESH;
					refreshHeaderViewState();
				} else if (paddingTop < 0 && mCurrentState != PULL_DOWN_REFRESH) {// 进入下拉刷新状态
					mCurrentState = PULL_DOWN_REFRESH;
					refreshHeaderViewState();
				}
				mHeaderView.setPadding(0, paddingTop, 0, 0);// 设置头布局padding
				// return true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (mCurrentState == RELEASE_REFRESH) {
				// 将当前状态更新为正在刷新
				mCurrentState = REFRESHING;
				mHeaderView.setPadding(0, 0, 0, 0);
				refreshHeaderViewState();
			} else if (mCurrentState == PULL_DOWN_REFRESH) {
				mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);// 隐藏头布局
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 根据当前状态, 更新下拉刷新界面
	 */
	private void refreshHeaderViewState() {
		switch (mCurrentState) {
		case PULL_DOWN_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			head_fl_progress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animDown);
			break;
		case RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);
			head_fl_progress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animUp);
			break;
		case REFRESHING:
			ivArrow.clearAnimation();// 必须清除动画, 否则View.INVISIBLE不起作用
			tvTitle.setText("正在刷新...");
			ivArrow.setVisibility(View.INVISIBLE);
			head_fl_progress.setVisibility(View.VISIBLE);

			if (mListener != null) {
				mListener.onRefresh();// 下拉刷新回调
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 获取格式化后的当前时间
	 */
	public String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * 当刷新完成后,隐藏下拉刷新控件, 初始化各项数据
	 */
	public void onRefreshComplete(boolean needUpdateTime) {
		if (isLoadMore) {
			isLoadMore = false;
			mFooterView.setPadding(0, -mFooterHeight, 0, 0);// 隐藏脚布局
		} else {
			mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);
			// tvTitle.setTextColor(getResources().getColor(R.color.blue06));
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			head_fl_progress.setVisibility(View.INVISIBLE);

			if (needUpdateTime) {
				tvTime.setText(getCurrentTime());
			}

			mCurrentState = PULL_DOWN_REFRESH;
		}
	}

	public void headOnRefreshCompleteOk() {
		tvTitle.setTextColor(Color.RED);
		tvTitle.setText("刷新成功");
		head_fl_progress.setVisibility(View.INVISIBLE);
		new RefreshCompleteAsyncTask().execute();
	}

	public void headOnRefreshCompleteFail() {
		tvTitle.setTextColor(Color.RED);
		tvTitle.setText("刷新失败");
		head_fl_progress.setVisibility(View.INVISIBLE);
		new RefreshCompleteAsyncTask().execute();
	}

	class RefreshCompleteAsyncTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			onRefreshComplete(false);
		}

	}

	/**
	 * 第一次初始化数据时, 显示下拉刷新控件
	 */
	public void setRefreshing() {
		tvTitle.setText("正在刷新...");
		ivArrow.setVisibility(View.INVISIBLE);
		head_fl_progress.setVisibility(View.VISIBLE);
		mHeaderView.setPadding(0, 0, 0, 0);
	}

	/**
	 * 设置下拉刷新监听
	 * 
	 * @param listener
	 */
	public void setOnRefreshListener(RefreshListener listener) {
		mListener = listener;
	}

	/**
	 * 下拉刷新的回调接口
	 * 
	 * @author Kevin
	 * 
	 */
	public interface RefreshListener {
		/**
		 * 下拉刷新的回调方法
		 */
		public void onRefresh();

		/**
		 * 加载更多的回调方法
		 */
		public void onLoadMore();
	}

	private boolean foot_no_data = false;
	
	

	public boolean isFoot_no_data() {
		return foot_no_data;
	}

	public void setFoot_no_data(boolean foot_no_data) {
		this.foot_no_data = foot_no_data;
	}

	/**
	 * 滑动状态发生变化
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 快速滑动或者静止时
		if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
			if (getLastVisiblePosition() == getCount() - 1 && !isLoadMore && !foot_no_data) {
				isLoadMore = true;
				mFooterView.setPadding(0, 0, 0, 0);
				this.setAdapter(getAdapter());
				this.setSelection(this.getCount());
				MyRotateAnimation.rotateAnimation(iv_progress);
				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}
	}

	public boolean isLoadMore() {
		return isLoadMore;
	}

	public void setLoadMore(boolean isLoadMore) {
		this.isLoadMore = isLoadMore;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	OnItemClickListener mItemClickListener;

	/**
	 * 响应Item点击
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mItemClickListener != null) {
			mItemClickListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);// 将原始position减去HeaderView的数量,才是准确的position
		}
	}

	/**
	 * 处理Item点击事件
	 */
	@Override
	public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
		mItemClickListener = listener;
		super.setOnItemClickListener(this);
	}
}