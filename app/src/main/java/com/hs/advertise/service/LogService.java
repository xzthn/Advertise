package com.hs.advertise.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hs.advertise.business.UploadTotalLog;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.service
 * ProjectName: trunk
 * Date: 2020/3/31 17:02
 */
public class LogService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UploadTotalLog.getUploadTotalLog(this).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UploadTotalLog.getUploadTotalLog(this).stop();
    }
}
