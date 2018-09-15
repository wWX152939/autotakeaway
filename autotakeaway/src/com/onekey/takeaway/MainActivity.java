package com.onekey.takeaway;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.onekey.common.Common;
import com.onekey.common.CrashHandler;
import com.onekey.common.LogUtils;
import com.onekey.common.SettingPreferences;
import com.onekey.http.CloudManager;
import com.onekey.takeaway.tabview.TabView;
import com.onekey.takeaway.tabview.TabViewChild;

public class MainActivity extends FragmentActivity {

	TabView tabView;
	String tab1 = "操作";
	String tab2 = "库存";
	String tab3 = "统计";
	
	private TextView mTextView;
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 1:

//				byte[] data = new byte[4096];
//				SocketHelper.getInstance().readDataToDataThread(data);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	LogUtils.d("ll1 onCreate");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		CrashHandler.getInstance().init(this);

		Common.s_Metric = new DisplayMetrics();
		
		getWindowManager().getDefaultDisplay().getRealMetrics(Common.s_Metric);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quick_start_activity);
		
		mTextView = (TextView) findViewById(R.id.tv_title);
		
		mHandler.sendEmptyMessageDelayed(1, 1000);

		tabView = (TabView) findViewById(R.id.tabView);
		tabView.setFragmentId(1001);
		
		// start add data
		List<TabViewChild> tabViewChildList = new ArrayList<TabViewChild>();
		TabViewChild tabViewChild01 = new TabViewChild(R.drawable.tab03_sel,
				R.drawable.tab03_unsel, tab1, FragmentOperation.newInstance(tab1));
		TabViewChild tabViewChild02 = new TabViewChild(R.drawable.tab02_sel,
				R.drawable.tab02_unsel, tab2, FragmentStore.newInstance(tab2));
		TabViewChild tabViewChild03 = new TabViewChild(R.drawable.tab03_sel,
				R.drawable.tab03_unsel, tab3, FragmentCount.newInstance(tab3));
		tabViewChildList.add(tabViewChild01);
		tabViewChildList.add(tabViewChild02);
		tabViewChildList.add(tabViewChild03);
		// end add data
		tabView.setTabViewDefaultPosition(0);
		tabView.setTextViewSelectedColor(getResources().getColor(R.color.title));
		tabView.setTabViewChild(tabViewChildList, getFragmentManager());
		tabView.setOnTabChildClickListener(new TabView.OnTabChildClickListener() {
			@Override
			public void onTabChildClick(int position,
					ImageView currentImageIcon, TextView currentTextView) {
//				Toast.makeText(getApplicationContext(), "position:" + position,
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
	}
	
	public void onResume() {
		super.onResume();
    	LogUtils.d("ll1 onResume");
	}
	

	public void onPause() {
		super.onPause();
    	LogUtils.d("ll1 onPause");
	}
	
	public void onDestroy() {
		super.onDestroy();
    	LogUtils.d("ll1 onDestroy");
    	mHandler.removeMessages(1);
//		SocketHelper.getInstance().setReadDataToDataThreadStop();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

}
