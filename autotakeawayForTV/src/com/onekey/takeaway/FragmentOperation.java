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
import com.onekey.takeawayForTV.R;

public class FragmentOperation extends Fragment {
	TabView tabView;
	public static final String tab1 = "炒饭机";
	public static final String tab2 = "售汤机";
	public static final String tab4 = "智能配餐柜";
	public static final String tab5 = "修改配餐柜";

	public static FragmentOperation newInstance(String text) {
		FragmentOperation fragmentCommon = new FragmentOperation();
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
				.inflate(R.layout.fragment_operation, container, false);
		tabView = (TabView) view.findViewById(R.id.tabView);
		tabView.setFragmentId(1002);
		
		final FragmentOrder FragmentOrder1 = FragmentOrder.newInstance(tab1);
		final FragmentOrder FragmentOrder2 = FragmentOrder.newInstance(tab2);
		final FragmentOrder FragmentOrder4 = FragmentOrder.newInstance(tab4);
		// start add data
		List<TabViewChild> tabViewChildList = new ArrayList<TabViewChild>();
		TabViewChild tabViewChild01 = new TabViewChild(R.drawable.tab03_sel,
				R.drawable.tab03_unsel, tab1, FragmentOrder1);
		TabViewChild tabViewChild02 = new TabViewChild(R.drawable.tab02_sel,
				R.drawable.tab02_unsel, tab2, FragmentOrder2);
		TabViewChild tabViewChild04 = new TabViewChild(R.drawable.tab03_sel,
				R.drawable.tab03_unsel, tab4, FragmentOrder4);
		TabViewChild tabViewChild05 = new TabViewChild(R.drawable.tab03_sel,
				R.drawable.tab03_unsel, tab5, FragmentCabinet.newInstance(tab5));
		tabViewChildList.add(tabViewChild01);
		tabViewChildList.add(tabViewChild02);
		tabViewChildList.add(tabViewChild04);
		tabViewChildList.add(tabViewChild05);
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
				case 0:
					FragmentOrder1.refreshData();
					break;
				case 1:
					FragmentOrder2.refreshData();
					break;
				case 2:
					FragmentOrder4.refreshData();
					break;
				}
			}
		});
		return view;
	}
}
