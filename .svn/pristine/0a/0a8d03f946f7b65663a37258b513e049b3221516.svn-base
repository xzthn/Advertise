package com.hs.advertise.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hs.advertise.MyApplication;
import com.hs.advertise.config.URLs;
import com.smart.data.DataFormat;

import java.util.List;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.utils
 * ProjectName: Advertise
 * Date: 2020/3/12 17:53
 */
public class PackageUtil {
    private static String LAUNCHER_CHANNEL = "";
    private static String LAUNCHER_VERSION = "";

    public static String getLauncherChannel() {
        Context context = MyApplication.getInstance().getApplicationContext();
        if (DataFormat.isEmpty(LAUNCHER_CHANNEL)) {
            try {
                String launcher = "";
                if (MyApplication.getInstance().mChannel.equalsIgnoreCase(URLs.LZ)) {

                    launcher = "com.lzui.launcher";
                } else {
                    launcher = "hs.launcher";
                }
                ApplicationInfo apInfo = context.getPackageManager().getApplicationInfo(launcher, PackageManager.GET_META_DATA);
                LAUNCHER_CHANNEL = apInfo.metaData.getString("COVERSION");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return LAUNCHER_CHANNEL;
    }

    /**
     * 获取launcher版本信息
     *
     * @return
     */
    public static String getLauncherVsnInfo() {
        Context context = MyApplication.getInstance().getApplicationContext();
        PackageInfo pkInfo;
        if (DataFormat.isEmpty(LAUNCHER_VERSION)) {
            try {
                String launcher = "";
                if (MyApplication.getInstance().mChannel.equalsIgnoreCase(URLs.LZ)) {

                    launcher = "com.lzui.launcher";
                } else {
                    launcher = "hs.launcher";
                }
                pkInfo = context.getPackageManager().getPackageInfo(launcher, 0);
                if (pkInfo != null) {
                    LAUNCHER_VERSION = pkInfo.versionName;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return LAUNCHER_VERSION;
    }

    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = manager.getRunningServices(200);
        if (serviceInfoList.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo info : serviceInfoList) {
            if (info.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
