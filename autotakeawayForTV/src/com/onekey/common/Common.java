package com.onekey.common;

import java.text.DecimalFormat;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public final class Common {

	public static int DelayTime = 2000;
	

	public static DecimalFormat df = new DecimalFormat("######0.00");
	
	public static DisplayMetrics s_Metric;
	public static SettingPreferences s_SettingPreferences;
	
	public static String getDoorCodeCustom(String doorCode) {
		String code = null;
		if (doorCode != null && doorCode.length() >= 6) {
			String s1 = doorCode.substring(1, 2);
			String s2 = doorCode.substring(3, 4);
			String s3 = doorCode.substring(5, 6);
			code = s1 + s2 + s3;
		}
		return code;
	}
	
	/**
	 * dialog弹出设置window属性
	 * @param window
	 * @param view dialog显示的view
	 * @param bShowInput 是否需要显示键盘
	 */
	public static void showWindow(Window window, View view, boolean bShowInput) {
		if (view != null) {
			window.setContentView(view);
		}
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = Common.s_Metric.widthPixels * 10 / 11;
		params.height = LayoutParams.WRAP_CONTENT;
		if (bShowInput) {
			params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
		}
		params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params.dimAmount = 0.5f;
		window.setAttributes(params);
	}
	
	public static void showWindow(Window window, boolean bShowInput) {
		showWindow(window, null, bShowInput);
	}
	
	public static void showWindow(Window window) {
		showWindow(window, null, false);
	}
	
	public static void showWindow(Window window, View view) {
		showWindow(window, view, false);
	}
	
	/**
	 * @param PopupWindow
	 * @param view PopupWindow弹出在哪个view上
	 * @param window
	 */
	public static void showWindow(PopupWindow PopupWindow, View view, Window window) {
		showWindow(PopupWindow, 0, view, window, false);
	}

	public static void showWindow(PopupWindow PopupWindow, int w, View view, Window window) {
		showWindow(PopupWindow, w, view, window, false);
	}
	
	/**
	 * PopupWindow弹出设置window属性
	 * @param PopupWindow
	 * @param w PopupWindow 宽度
	 * @param view PopupWindow弹出在哪个view上
	 * @param window
	 * @param bShowInput 是否需要弹出键盘
	 */
	public static void showWindow(PopupWindow PopupWindow, int w, View view, Window window, boolean bShowInput) {
		WindowManager.LayoutParams params = window.getAttributes();
		if (w == 0) {
			PopupWindow.setWidth(Common.s_Metric.widthPixels * 10 / 11);
		} else {
			PopupWindow.setWidth(w);
		}

//		PopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		if (Build.VERSION.SDK_INT < 24)
        {
			PopupWindow.showAsDropDown(view);
        }
        else
        {
            // 适配 android 7.0
            int[] location = new int[2];
            view.getLocationInWindow(location);
            int x = location[0];
            int y = location[1];
            PopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y + view.getHeight());
        }
//		PopupWindow.showAsDropDown(view);
		if (bShowInput) {
			params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
		}
//		params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//		params.alpha = 0.5f;
//		window.setAttributes(params);
//		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
	
	public static void dismissWindow(Window window) {
		WindowManager.LayoutParams params = window.getAttributes();
		params.dimAmount = 1.0f;
		params.alpha = 1.0f;
		params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(params);
//		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
	
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static interface Callback{
		void callback(Object... object);
	}
}
