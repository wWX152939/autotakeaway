package com.onekey.takeaway.ux;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnMultiClickListener {
	// 两次点击按钮之间的点击间隔不能少于500毫秒
	public static final int MIN_CLICK_DELAY_TIME = 500;
	public static long lastClickTime;
	
	public static void updateLastClickTime() {
		lastClickTime = System.currentTimeMillis();
	}

	public static abstract class OnClick implements View.OnClickListener {
		
		public abstract void onMultiClick(View v);
		
		@Override
		public void onClick(View v) {
			long curClickTime = System.currentTimeMillis();
			long time = curClickTime - lastClickTime;
			Log.v("ccc","-----间隔时间=:"+time+"-----");
			if (time >= MIN_CLICK_DELAY_TIME) {
				// 超过点击间隔后再将lastClickTime重置为当前点击时间
				Log.v("ccc","-----到500ms,执行----");
				lastClickTime = curClickTime;
				onMultiClick(v);
			} else {
				Log.v("ccc","-----未到500ms,不响应-----");
			}
		}
	}
	
	public static abstract class OnItemClick implements OnItemClickListener {
		
		public abstract void onMultiClick(AdapterView<?> parent, View view, int position, long id);
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			long curClickTime = System.currentTimeMillis();
			if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
				// 超过点击间隔后再将lastClickTime重置为当前点击时间
				lastClickTime = curClickTime;
				onMultiClick(parent, view, position, id);
			}
		}
	}
	
}