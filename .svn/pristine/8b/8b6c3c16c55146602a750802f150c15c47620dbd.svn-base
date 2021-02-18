package com.hs.advertise.mvp.progress;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by liukun on 16/3/10.
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private Context context;
    private boolean mCancelable;

    public ProgressDialogHandler(Context context, boolean cancelable) {

        super();
        this.context = context;
        this.mCancelable = cancelable;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                CustomProgress.show(context, "", mCancelable);
                break;
            case DISMISS_PROGRESS_DIALOG:
                CustomProgress.disMiss();
                break;
        }
    }
}