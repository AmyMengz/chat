package com.huodong.im.chat.view;

import com.huodong.im.chat.util.PixelUtil;
import com.huodong.im.chatdemo.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/** 通讯录锟揭诧拷锟斤拷俟锟斤拷锟斤拷锟�   * @ClassName: MyLetterView
  * @Description: TODO
  * @author smile
  * @date 2014-6-7 锟斤拷锟斤拷1:20:33
  */
public class MyLetterView extends View {
	// 锟斤拷锟斤拷锟铰硷拷
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26锟斤拷锟斤拷母
	public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	private int choose = -1;// 选锟斤拷
	private Paint paint = new Paint();

	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public MyLetterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyLetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLetterView(Context context) {
		super(context);
	}

	/**
	 * 锟斤拷写锟斤拷锟斤拷锟斤拷锟�	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 锟斤拷取锟斤拷锟斤拷谋浔筹拷锟斤拷锟缴�
		int height = getHeight();// 锟斤拷取锟斤拷应锟竭讹拷
		int width = getWidth(); // 锟斤拷取锟斤拷应锟斤拷锟�		
		int singleHeight = height / b.length;// 获取每一个字母的高度
		for (int i = 0; i < b.length; i++) {
			paint.setColor(getResources().getColor(R.color.color_bottom_text_normal));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(PixelUtil.sp2px(12));
			paint.setColor(getResources().getColor(R.color.sky_56abe4));
			// 选锟叫碉拷状态
			if (i == choose) {
				paint.setColor(Color.parseColor("#ffffff"));
				paint.setFakeBoldText(true);
			}
			// x锟斤拷锟斤拷锟斤拷锟叫硷拷-锟街凤拷锟饺碉拷一锟斤拷.
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();// 锟斤拷锟矫伙拷锟斤拷
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 锟斤拷锟統锟斤拷锟�		
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);// 锟斤拷锟統锟斤拷锟斤拷锟秸硷拷芨叨鹊谋锟斤拷锟�b锟斤拷锟斤拷某锟斤拷染偷锟斤拷诘锟斤拷b锟叫的革拷锟斤拷.

		switch (action) {
		case MotionEvent.ACTION_UP:
//			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			//锟斤拷锟斤拷锟揭诧拷锟斤拷母锟叫憋拷[A,B,C,D,E....]锟侥憋拷锟斤拷锟斤拷色
//			setBackgroundResource(R.drawable.v2_sortlistview_sidebar_background);
			if (oldChoose != c) {
				if (c >= 0 && c < b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					
					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	/**
	 * 锟斤拷锟解公锟斤拷锟侥凤拷锟斤拷
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 锟接匡拷
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}
