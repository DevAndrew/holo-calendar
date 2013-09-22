package com.vdesmet.lib.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.viewpagerindicator.TitlePageIndicator;

import java.util.Calendar;

public class MultiCalendarView extends AbstractCalendarView {

    private boolean mShowIndicator;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private MultiCalendarAdapter mAdapter;
    private int mViewPagerPosition = -1;
    private TitlePageIndicator mIndicator;
    private OnCalendarLoadedListener mOnCalendarLoadedListener;

    public MultiCalendarView(final Context context) {
        super(context);
        init();
    }

    public MultiCalendarView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiCalendarView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        mShowIndicator = true;
        mIsViewInitialized = false;
        mFirstDayOfWeek = Calendar.MONDAY;
        mLastDayOfWeek = Calendar.SUNDAY;

        // Add ViewPager + TitlePageIndicator
        final ViewPager viewPager = new ViewPager(getContext());
        final MultiCalendarAdapter adapter = new MultiCalendarAdapter(getContext(), this);
        adapter.setTypeface(mTypeface);
        viewPager.setAdapter(adapter);

        TitlePageIndicator indicator = new TitlePageIndicator(getContext());
        if(mTypeface != null) {
            indicator.setTypeface(mTypeface);
        }
        indicator.setViewPager(viewPager);

        addView(indicator);

        // Add view to layout
        addView(viewPager);

        mAdapter = adapter;
        mViewPager = viewPager;
        mIndicator = indicator;
    }

    public void setIndicatorVisible(boolean visible) {
       this.mShowIndicator = visible;
    }

    @Override
    public void notifyDataSetChanged() {
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mAdapter);
            mIndicator.setViewPager(mViewPager);
        }
    }
    public ViewPager getViewPager() {
        return  mViewPager;
    }

    @Override
    protected void initView() {
        if(mFirstValidDay != null) {

            // If indicator is visible while it shouldn't, or visa versa
            if(mShowIndicator  != (getChildAt(0) == mIndicator)) {
                // Show or hide the view
                if(mShowIndicator) {
                   mIndicator.setVisibility(View.VISIBLE);
                } else {
                   mIndicator.setVisibility(View.GONE);
                }
            }
            if(mOnPageChangeListener != null) {
                mViewPager.setOnPageChangeListener(mOnPageChangeListener);
            }

            if(mViewPagerPosition != -1) {
                // We need to change the ViewPager position
                mViewPager.setCurrentItem(mViewPagerPosition);
                mViewPagerPosition = -1;
            }

            mIsViewInitialized = true;

            if(mOnCalendarLoadedListener != null) {
                mOnCalendarLoadedListener.onCalendarLoaded(this);
            }
        }
    }

    public void setOnPageChangeListener(final ViewPager.OnPageChangeListener onPageChangeListener) {
        if(mViewPager != null) {
            mViewPager.setOnPageChangeListener(onPageChangeListener);
        }
        mOnPageChangeListener = onPageChangeListener;
    }

    public void setOnCalendarLoadedListener(final OnCalendarLoadedListener listener) {
        this.mOnCalendarLoadedListener = listener;
    }
    public void setViewPagerPosition(final int viewPagerPosition) {
        mViewPagerPosition = viewPagerPosition;
    }
}