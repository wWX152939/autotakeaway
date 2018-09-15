package com.onekey.takeaway;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onekey.common.LogUtils;
import com.onekey.takeaway.tabview.TabView;
import com.onekey.takeaway.tabview.TabViewChild;

public class FragmentStore extends Fragment {
	TabView tabView;
	public static final String tab1 = "炒饭机";
	public static final String tab2 = "售汤机";
	public static final String tab3 = "人工菜品";
	public static final String tab4 = "智能配餐柜";

	public static FragmentStore newInstance(String text) {
		FragmentStore fragmentCommon = new FragmentStore();
		Bundle bundle = new Bundle();
		bundle.putString("text", text);
		fragmentCommon.setArguments(bundle);
		return fragmentCommon;
	}

    public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		LogUtils.d("ll1 hidden=" + hidden);
		if (!hidden) {
		}
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_store, container, false);
		tabView = (TabView) view.findViewById(R.id.tabView);
		tabView.setFragmentId(1003);
		
		// start add data
		List<TabViewChild> tabViewChildList = new ArrayList<TabViewChild>();
		TabViewChild tabViewChild01 = new TabViewChild(R.drawable.tab03_sel,
				R.drawable.tab03_unsel, tab1, FragmentDevices.newInstance(tab1));
		TabViewChild tabViewChild02 = new TabViewChild(R.drawable.tab02_sel,
				R.drawable.tab02_unsel, tab2, FragmentDevices.newInstance(tab2));
		TabViewChild tabViewChild03 = new TabViewChild(R.drawable.tab03_sel,
				R.drawable.tab03_unsel, tab3, FragmentFood.newInstance(tab3));
//		TabViewChild tabViewChild04 = new TabViewChild(R.drawable.tab03_sel,
//				R.drawable.tab03_unsel, tab4, FragmentCabinet.newInstance(tab4));
		tabViewChildList.add(tabViewChild01);
		tabViewChildList.add(tabViewChild02);
		tabViewChildList.add(tabViewChild03);
//		tabViewChildList.add(tabViewChild04);
		// end add data
		tabView.setTabViewDefaultPosition(0);
		tabView.setTextViewSelectedColor(getResources().getColor(R.color.title));
		tabView.setTabViewGravity(Gravity.BOTTOM);
		tabView.setTabViewChild(tabViewChildList, getFragmentManager());
		tabView.setOnTabChildClickListener(new TabView.OnTabChildClickListener() {
			@Override
			public void onTabChildClick(int position,
					ImageView currentImageIcon, TextView currentTextView) {
//				Toast.makeText(getActivity(), "position:" + position,
//						Toast.LENGTH_SHORT).show();
				switch (position) {
//				case 0:
//					mTextView.setText("状态");
//					break;
//				case 1:
//					mTextView.setText("信号");
//					break;
//				case 2:
//					mTextView.setText("网口");
//					break;
				}
			}
		});
		return view;
	}
}
