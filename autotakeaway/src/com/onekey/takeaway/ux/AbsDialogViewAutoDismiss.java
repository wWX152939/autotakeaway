package com.onekey.takeaway.ux;

import android.content.Context;

public class AbsDialogViewAutoDismiss extends AbsDialogView{

	public AbsDialogViewAutoDismiss(Context context) {
		super(context);
	}
	
	protected void confirmDismiss() {
		dismiss();
	}
}
