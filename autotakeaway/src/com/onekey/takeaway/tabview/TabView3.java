package com.onekey.takeaway.tabview;

import android.content.Context;
import android.util.AttributeSet;

import com.onekey.takeaway.R;

public class TabView3 extends TabView {

	public TabView3(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public TabView3(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	protected int getTabViewId() {
		return R.id.tabview_fragment_container3;
	}
	
}
