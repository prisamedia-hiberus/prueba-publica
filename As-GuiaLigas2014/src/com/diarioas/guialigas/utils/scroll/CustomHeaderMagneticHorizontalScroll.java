package com.diarioas.guialigas.utils.scroll;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.FontUtils;

public class CustomHeaderMagneticHorizontalScroll extends
		CustomMagneticHorizontalScroll {

	private static final int DEFAULT_MAIN_COLOR = Color.RED;
	private static final int DEFAULT_SECOND_COLOR = Color.GRAY;

	private int mainColor;
	private int secondColor;
	private LayoutInflater inflater;
	private LinearLayout linear;
	private String font;
	private int width;

	// private ArrayList<Integer> widths;

	public CustomHeaderMagneticHorizontalScroll(Context context) {
		super(context);
		// mContext = context;
		init();
	}

	public CustomHeaderMagneticHorizontalScroll(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		// mContext = context;
		init();
	}

	public CustomHeaderMagneticHorizontalScroll(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// mContext = context;
		init();
	}

	private void init() {

		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		this.mainColor = DEFAULT_MAIN_COLOR;
		this.secondColor = DEFAULT_SECOND_COLOR;
		// widths = new ArrayList<Integer>();
		linear = new LinearLayout(getContext());
		this.addView(linear);
	}

	public void addViews(final ArrayList<String> strings) {

		linear.removeAllViews();

		for (int i = 0; i < strings.size(); i++) {
			final RelativeLayout customTabView = (RelativeLayout) inflater
					.inflate(R.layout.custom_tab_view, null);
			TextView tabTitle = (TextView) customTabView
					.findViewById(R.id.title);
			tabTitle.setMaxLines(1);
			tabTitle.setText(strings.get(i));
			if (font != null)
				FontUtils.setCustomfont(getContext(), tabTitle, font);

			customTabView.setTag(i);
			customTabView.getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {

						@SuppressLint("NewApi")
						@Override
						public void onGlobalLayout() {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
								customTabView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							} else {
								customTabView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							}
							if (((Integer) customTabView.getTag()) == 0) {
								int padding = width / 2
										- customTabView.getWidth() / 2;
								customTabView.findViewById(R.id.title)
										.setPadding(padding, 0, 0, 0);
							} else if (((Integer) customTabView.getTag()) == strings
									.size() - 1) {
								int padding = width / 2
										- customTabView.getWidth() / 2;
								customTabView.findViewById(R.id.title)
										.setPadding(0, 0, padding, 0);
							} else {
							}

						}
					});

			customTabView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					// if (scrollEndListener != null) {
					 Integer position = (Integer) v.getTag();
					 goToPosition(position);
					// setHeaderPosition(position);
					// if (scrollEndListener != null
					// && currentOldPosition != currentPosition) {
					// currentOldPosition = currentPosition;
					// scrollEndListener.onItemClicked(position);
					// }
					//
					// }
				}
			});

			linear.addView(customTabView);
		}
	}

	protected void resetHeader(int itemSelected) {

		int numChilds = linear.getChildCount();
		for (int j = 0; j < numChilds; j++) {

			RelativeLayout relativeTab = (RelativeLayout) linear.getChildAt(j);
			TextView tabTitle = (TextView) relativeTab.findViewById(R.id.title);
			if ((tabTitle != null) && (j == itemSelected)) {
				tabTitle.setTextColor(mainColor);
			} else if (tabTitle != null) {
				tabTitle.setTextColor(secondColor);
			}
		}
	}

	public void setScreenWidth(int width) {
		this.width = width;
		this.setItemWidth(width / 2);
		this.setOffset(width/2);
	}

	public void setFont(String font) {
		this.font = font;
	}

	public void setMainColor(int color) {
		this.mainColor = color;
	}

	public void setSecondColor(int color) {
		this.secondColor = color;
	}

	public void setHeaderPosition(int position) {
		this.smoothScrollTo(position);
	}

}
