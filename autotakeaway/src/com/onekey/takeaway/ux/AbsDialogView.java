package com.onekey.takeaway.ux;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.onekey.takeaway.R;

public class AbsDialogView {
	
	public Context mContext;
	ViewGroup mRootView;
	
	TextView mConfirmBtn, mCancelBtn, mTitleBtn;
	LinearLayout mLlParent;
	
	/**
	 * 属性存放 
	 */
	OnClickListener mConfirmListener;
	OnClickListener mCancelListener;
	ConfirmCheckListener mConfirmCheckListener;
	View mCustomView;
	String mCustomTitle;
	String mCustomCancel;
	String mCustomConfirm;
	int mColorId = -1;
	
	enum ViewType {
		Dialog, PopupWindow
	}
	
	ViewType mViewType;
	Dialog mDialog;
	PopupWindow mPopupWindow;
	
	public AbsDialogView(Context context) {
		mContext = context;

		mRootView = inflateRootView();
		mTitleBtn = (TextView) mRootView.findViewById(R.id.tv_title);
		mConfirmBtn = (TextView) mRootView.findViewById(R.id.textView_confirm);
		mCancelBtn = (TextView) mRootView.findViewById(R.id.textView_cancle);
		mLlParent = (LinearLayout) mRootView.findViewById(R.id.ll_parent);
		InitRootLayout();
	}
	
	public View getConfirmView() {
		return mConfirmBtn;
	}
	
	public void setTitleVisibility(int status) {
		mTitleBtn.setVisibility(status);
	}
	
	public ViewGroup getParentView() {
		return mLlParent;
	}
	
	public ViewGroup getRootView() {
		return mRootView;
	}
	
	public AbsDialogView setOnConfirmListener(OnClickListener confirmListener) {
		mConfirmListener = confirmListener;
		return this;
	}
	
	public AbsDialogView setOnConfirmCheckListener(ConfirmCheckListener ConfirmCheckListener) {
		mConfirmCheckListener = ConfirmCheckListener;
		return this;
	}
	
	public AbsDialogView setOnClickListener(OnClickListener confirmListener, OnClickListener cancelListener) {
		mConfirmListener = confirmListener;
		mCancelListener = cancelListener;
		return this;
	}
	
	public AbsDialogView setTitle(int titleId) {
		mCustomTitle = mContext.getString(titleId);
		return this;
	}
	
	public AbsDialogView setCancelText(int cancelId) {
		mCustomCancel = mContext.getString(cancelId);
		return this;
	}
	
	public AbsDialogView setConfirmText(int confirmId) {
		mCustomConfirm = mContext.getString(confirmId);
		return this;
	}
	
	public void setConfirmButtonStatus(boolean isEnable) {
		mConfirmBtn.setEnabled(isEnable);
	}
	
	public AbsDialogView setCancelTextColor(int colorId) {
		mColorId = colorId;
		return this;
	}
	
	public AbsDialogView setTitle(String title) {
		mCustomTitle = title;
		return this;
	}
	
	public AbsDialogView setView(View view) {
		mCustomView = view;
		return this;
	}
	
	protected void InitRootLayout() {
	}
	
	protected void confirmDismiss() {
		
	}
	
	protected void InitBottomLayout(final ViewType type, final Dialog dialog, final PopupWindow popupWindow) {
		if (mCustomCancel != null) {
			mCancelBtn.setText(mCustomCancel);
		}
		if (mColorId != -1) {
			mCancelBtn.setTextColor(mColorId);
		}
		
		if (mCustomConfirm != null) {
			mConfirmBtn.setText(mCustomConfirm);
		}
		if (mConfirmListener == null) {
			mConfirmBtn.setVisibility(View.GONE);
		} else {
			mConfirmBtn.setVisibility(View.VISIBLE);
			mConfirmBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/*确认监听因为包含有OnMultiClickListener，所以可以不用写成OnMultiClickListener*/
					if (mConfirmCheckListener == null || (mConfirmCheckListener != null && mConfirmCheckListener.onClick())) {
						confirmDismiss();
						mConfirmListener.onClick(v);
					}
				}
			});
			mCancelBtn.setOnClickListener(mCancelListener != null ? mCancelListener : new OnMultiClickListener.OnClick() {

				@Override
				public void onMultiClick(View v) {
					switch (type) {
					case Dialog:
						dialog.dismiss();
						break;
					case PopupWindow:
						popupWindow.dismiss();
						break;
					}
				}
			});
		}
	}
	
	public void dismiss() {
		ViewType type = mViewType;
		switch (type) {
			case Dialog:
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				break;
			case PopupWindow:
				if (mPopupWindow != null && mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				break;
			}
	}
	
	protected ViewGroup inflateRootView() {

		return (ViewGroup) LayoutInflater.from(mContext).inflate( R.layout.abs_dialog , null);
	}
	
	private void initView(final ViewType type, final Dialog dialog, final PopupWindow popupWindow) {
		mViewType = type;
		mDialog = dialog;
		mPopupWindow = popupWindow;
		if (mCustomView == null) {
			if (mLlParent.getChildCount() == 0) {
				mLlParent.setVisibility(View.GONE);
			}
		} else {
			mLlParent.removeAllViews();
			mLlParent.addView(mCustomView);
		}

		if (mCustomTitle != null) {
			mTitleBtn.setText(mCustomTitle);
		}

		InitBottomLayout(type, dialog, popupWindow);
		
	}
	
    public void initView(Dialog dialog) {
    	initView(ViewType.Dialog, dialog, null);
	}
    
    public void initView(PopupWindow popupWindow) {
    	initView(ViewType.PopupWindow, null, popupWindow);
    }
    
    public interface ConfirmCheckListener {
    	
    	/**
    	 * 返回true，检查通过，dialog消失，检查false，dialog不消失。
    	 * @return
    	 */
    	public boolean onClick();
    }
}
