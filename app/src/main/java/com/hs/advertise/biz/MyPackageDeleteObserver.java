package com.hs.advertise.biz;

import android.content.Context;
import android.os.Handler;

import com.lunzn.systool.pkg.ApkManageBiz;
import com.lunzn.tool.log.LogUtil;

/**
 * 静默卸载观察者
 *
 * @author Administrator
 * @version [版本号]
 * @date 2017年6月9日
 * @project COM.LZ.M02.LAUNCHER
 * @package com.lz.m02.launcher.biz
 * @package MyPackageDeleteObserver.java
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MyPackageDeleteObserver implements ApkManageBiz.PackageDeleteListener {
    //上下文对象
    Context mContext;

    //卸载app名字
    String pkname;

    String apkFilepath;

    private Handler mHandler;

    public MyPackageDeleteObserver(Context mContext, Handler mHandler, String apkFilepath) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.apkFilepath = apkFilepath;
    }

    @Override
    public void packageDeleted(String packageName, int returnCode) {
        LogUtil.d("returnCode = " + returnCode + ",packageName:" + packageName);// 返回1代表卸载成功
        // 返回1代表卸载成功
        if (returnCode == DELETE_SUCCEEDED) {
            ApkManageBiz.PackageInstallListener observer = new MyPakcageInstallObserver(mContext, mHandler, apkFilepath);
            try {
                LogUtil.i("静默卸载完成  静默安装 ");
                ApkManageBiz.autoInstallApk(mContext, apkFilepath, packageName, observer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
