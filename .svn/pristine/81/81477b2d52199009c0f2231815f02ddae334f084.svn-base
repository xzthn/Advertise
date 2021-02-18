package com.hs.advertise.thread;


import android.os.Environment;

import com.hs.advertise.model.MessageModel;
import com.lunzn.tool.log.LogUtil;
import com.smart.localfile.LocalFileCRUDUtils;

public class DelecteLogThread implements Runnable {
    private static final String TAG = "Tag_"+ DelecteLogThread.class.getSimpleName();
    @Override
    public void run() {
        // 删除日志文件
        boolean is = LocalFileCRUDUtils.deleteFile(MessageModel.JSON_LOCAL_PATH);
        LogUtil.i(TAG, "删除文件是否成功" + is);

        is = LocalFileCRUDUtils.deleteFile(Environment.getExternalStorageDirectory().getPath() + "/launcher_ad.json");
        LogUtil.i(TAG, "删除 launcher 文件是否成功" + is);

    }

}
