package com.hs.advertise.mvp.progress;

import android.app.Dialog;
import android.content.Context;

public class CustomProgress extends Dialog {

    private static CustomProgress dialog;

    public CustomProgress(Context context) {

        super(context);
    }

    public CustomProgress(Context context, int theme) {

        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

//		QMUILoadingView imageView = findViewById(R.id.spinnerImageView);
//		imageView.start();
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context 上下文
     *                提示
     *                是否按返回键取消
     */
    public static void show(Context context) {

        show(context, "");
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context 上下文
     *                提示
     *                是否按返回键取消
     */
    public static void show(Context context, String title) {

        show(context, title, false);
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context 上下文
     * @param title   提示
     * @return
     */
    public static void show(Context context, String title, boolean cancelable) {

/*		if (context == null) {
			return;
		}
		if (dialog == null) {
			int identifier = context.getResources().getIdentifier("Custom_Progress", "style", context.getPackageName());
			dialog = new CustomProgress(context, identifier);
		}
		dialog.setTitle("");
		dialog.setCancelable(cancelable);
		dialog.setContentView(R.layout.progress_custom);
		if (title == null || title.length() == 0) {
			dialog.findViewById(R.id.message).setVisibility(View.GONE);
		} else {
			int identifier = context.getResources().getIdentifier("progress_custom_bg", "drawable", context.getPackageName());
			TextView txt = dialog.findViewById(R.id.message);
			dialog.findViewById(R.id.ll_backgorund).setBackgroundResource(identifier);
			txt.setText(title);
		}
		// 监听返回键处理
//		if (listener != null) {
//			dialog.setOnCancelListener(listener);
//		}
		// 设置居中
		if (dialog.getWindow() != null) {
			dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			// 设置背景层透明度
			lp.dimAmount = 0.2f;
			dialog.getWindow().setAttributes(lp);
			dialog.show();
		}*/
    }

    public static boolean disMiss() {

        boolean isOk = false;
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
                isOk = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOk;
    }
}