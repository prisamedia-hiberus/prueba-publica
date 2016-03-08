package com.diarioas.guialigas.utils.scroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.FontUtils;

import java.util.ArrayList;

public class CustomHoizontalScroll extends HorizontalScrollView {

    public interface ScrollEndListener {

        void onScrollEnd(int x, int y, int oldx, int oldy, int pos);

        void onItemClicked(int position);

    }

    private static final int DEFAULT_MAIN_COLOR = Color.RED;

    private static final int DEFAULT_SECOND_COLOR = Color.GRAY;

    private ScrollEndListener scrollEndListener = null;
    private boolean currentlyScrolling;
    private boolean currentlyTouching;

    private ArrayList<Integer> widths;
    private int width;
    private final Context mContext;
    private LinearLayout linear;
    private LayoutInflater inflater;
    private int currentPosition = 0;
    private int currentOldPosition = -1;
    private String font;
    private int mainColor;
    private int secondColor;
    private boolean isScrollContainer = false;

    private int initPosition;

    public CustomHoizontalScroll(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CustomHoizontalScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CustomHoizontalScroll(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        widths = new ArrayList<Integer>();
        linear = new LinearLayout(mContext);
        this.addView(linear);
        this.mainColor = DEFAULT_MAIN_COLOR;
        this.secondColor = DEFAULT_SECOND_COLOR;

    }

    @SuppressLint("NewApi")
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
                FontUtils.setCustomfont(mContext, tabTitle, font);

            customTabView.setTag(i);
            customTabView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                customTabView.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            } else {
                                customTabView.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }

                            if (isScrollContainer) {
                                if (((Integer) customTabView.getTag()) == 0) {
                                    int padding = width / 2
                                            - customTabView.getWidth() / 2;
                                    customTabView.findViewById(R.id.title)
                                            .setPadding(padding, 0, 0, 0);
                                    widths.add(customTabView.getWidth() + padding);
                                } else if (((Integer) customTabView.getTag()) == strings
                                        .size() - 1) {
                                    int padding = width / 2
                                            - customTabView.getWidth() / 2;
                                    customTabView.findViewById(R.id.title)
                                            .setPadding(0, 0, padding, 0);
                                    widths.add(customTabView.getWidth() + padding);
                                } else {
                                    widths.add(customTabView.getWidth());
                                }
                            } else {
                                int customTabViewWidth = customTabView.getWidth();
                                if (((Integer) customTabView.getTag()) != (strings
                                        .size() - 1)) {
                                    int padding = (width - customTabViewWidth) / 2;
                                    customTabView.findViewById(R.id.title)
                                            .setPadding(padding, 0, 0, 0);
                                    //widths.add(customTabView.getWidth() + padding);
                                    widths.add(customTabViewWidth);
                                } else {

                                    int padding = (width - customTabViewWidth) / 2;
                                    customTabView.findViewById(R.id.title)
                                            .setPadding(padding, 0, padding, 0);
                                    //widths.add(customTabView.getWidth() + 2*padding);
                                    widths.add(customTabViewWidth);
                                }
                            }


                        }
                    });
            customTabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (scrollEndListener != null) {
                        Integer position = (Integer) v.getTag();
                        setHeaderPosition(position);
                        if (scrollEndListener != null
                                && currentOldPosition != currentPosition) {
                            currentOldPosition = currentPosition;
                            scrollEndListener.onItemClicked(position);
                        }

                    }
                }
            });

            linear.addView(customTabView);
        }
        resetHeaderPosition();

        if (initPosition >= 0) {
            linear.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                linear.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            } else {
                                linear.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setHeaderPosition(initPosition);
                                }
                            }, 100L);

                        }

                    });
        }
    }

    /**
     * Reset the color of the header tabs
     *
     * @param position
     */
    private void resetHeaderPosition() {

        int numChilds = linear.getChildCount();
        for (int j = 0; j < numChilds; j++) {

            RelativeLayout relativeTab = (RelativeLayout) linear.getChildAt(j);
            TextView tabTitle = (TextView) relativeTab.findViewById(R.id.title);
            if ((tabTitle != null) && (j == currentPosition)) {
                tabTitle.setTextColor(mainColor);
            } else if (tabTitle != null) {
                tabTitle.setTextColor(secondColor);
            }
        }
    }

    /**
     * set the header tabs to position
     *
     * @param position
     */
    public void setHeaderPosition(int position) {
        /*
		if (currentPosition != position) {

			int w = 0;
			if (widths.size() > position) {
				currentPosition = position;
				resetHeaderPosition();
				// Find the position
				for (int i = 0; i < position; i++) {
					int customTabViewWidth = widths.get(i); 
					int padding = (width - customTabViewWidth) / 2;
					int buttonWidth = customTabViewWidth+padding;
					w += buttonWidth;
				}
				// Center in the screen
				//w -= (width - widths.get(position)) / 2;
				Log.d("HeaderPosition","Hay que colocar el scroll en la posición: "+position+" (X:"+w+")");
				Log.d("HeaderPosition","El Array de anchuras es: ["+widths.toString()+"]");
				Log.d("HeaderPosition","El Ancho de pantalla es: "+width);
				//this.scrollTo(w, this.getScrollY());
				
				final int positionXToScroll = w;
				final int positionYToScroll = this.getBottom();
				
				this.post(new Runnable() { 
			        public void run() {
			        	scrollTo(positionXToScroll,positionYToScroll);			             
			        } 
			});
			}


			// this.smoothScrollTo(w, this.getScrollY());
		}*/

        int w = 0;
        if (widths.size() > position) {
            currentPosition = position;
            resetHeaderPosition();
            // Find the position
            for (int i = 0; i < position; i++) {
                int customTabViewWidth = widths.get(i);
                int padding = (width - customTabViewWidth) / 2;
                int buttonWidth = customTabViewWidth + padding;
                w += buttonWidth;
            }
            // Center in the screen
            //w -= (width - widths.get(position)) / 2;
            Log.d("HeaderPosition", "Hay que colocar el scroll en la posición: " + position + " (X:" + w + ")");
            Log.d("HeaderPosition", "El Array de anchuras es: [" + widths.toString() + "]");
            Log.d("HeaderPosition", "El Ancho de pantalla es: " + width);
            //this.scrollTo(w, this.getScrollY());

            final int positionXToScroll = w;
            final int positionYToScroll = this.getBottom();

            this.post(new Runnable() {
                public void run() {
                    scrollTo(positionXToScroll, positionYToScroll);
                }
            });
        }
    }

    public void addScrollEndListener(ScrollEndListener scrollEndListener) {
        this.scrollEndListener = scrollEndListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

        if (Math.abs(x - oldx) > 1) {
            currentlyScrolling = true;
        } else {
            currentlyScrolling = false;
            if (!currentlyTouching) {
                onScrollFinish(x, y, oldx, oldy);
            }
        }
        super.onScrollChanged(x, y, oldx, oldy);

    }

    private void onScrollFinish(int x, int y, int oldx, int oldy) {
        Log.d("SCROLL", "Finish: " + x);
        int pos = -1;
        int w = 0;
        for (int i = 0; i < widths.size(); i++) {
			/*int customTabViewWidth = widths.get(i); 
			int padding = (width - customTabViewWidth) / 2;
			int buttonWidth = customTabViewWidth+padding;
			w += buttonWidth;
			if (w > (x + width / 2)) {
				pos = i;
				break;
			}*/
			/*
			int customTabViewWidth = widths.get(i); 
			int padding = (width - customTabViewWidth) / 2;
			int buttonWidth = customTabViewWidth/2+padding;
			int oldW = w;
			w += buttonWidth;
			if (w > x) {
				
				int absPosPrevious = Math.abs(oldW-x);
				int absPosNext=Math.abs(w-x);
				if (absPosPrevious<=absPosNext) {
					pos=i-1;
					if (pos<-1)
						pos=0;
					break;
				} else {
					pos=i;
					break;
				}
				
			}*/
            int customTabViewWidth = widths.get(i);
            int padding = (width - customTabViewWidth) / 2;

            w += padding;
            if (w > x) {
                pos = i;
                break;
            }

            w += customTabViewWidth;

        }
        if (pos == -1) {
            pos = currentPosition;
        }

        setHeaderPosition(pos);
        //if (scrollEndListener != null && currentOldPosition != currentPosition) {
        if (scrollEndListener != null) {
            currentOldPosition = currentPosition;
            scrollEndListener.onScrollEnd(x, y, oldx, oldy, pos);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentlyTouching = true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentlyTouching = false;
                if (!currentlyScrolling) {
                    // I handle the release from a drag here
                    // return true;
                }
        }
        return super.onTouchEvent(event);
    }

    public void setScreenWidth(int width) {
        this.width = width;
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

    /**
     * @param firstPosition
     */
    public void setInitPosition(int firstPosition) {
        initPosition = firstPosition;

    }

    public void setScrollContainer(boolean scroll) {
        this.isScrollContainer = scroll;
    }
}
