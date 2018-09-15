package com.onekey.takeaway.ux;

import android.app.Dialog;
import android.content.Context;

import com.onekey.common.Common;
import com.onekey.takeaway.R;

public class AbsDialog extends Dialog {

	
	public AbsDialog(Context context) {
		this(context, R.style.customDialog);
	}
	
	public AbsDialog(Context context, int theme) {
		super(context, theme);
		setCanceledOnTouchOutside(false);
	}
	
    public void show() {
		super.show();
    	getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    
    public void setContextView(AbsDialogView AbsDialogView) {
    	AbsDialogView.initView(this);
    	super.setContentView(AbsDialogView.getRootView());
    	
    }
    
    public void show(AbsDialogView AbsDialogView) {
    	AbsDialogView.initView(this);
		Common.showWindow(getWindow(), AbsDialogView.getRootView());
    	
		super.show();
    	getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    
}
