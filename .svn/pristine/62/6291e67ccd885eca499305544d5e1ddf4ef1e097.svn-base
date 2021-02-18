package com.hs.advertise.biz;

import android.content.Context;
import android.os.Handler;

import com.hs.advertise.model.MessageModel;
import com.lunzn.systool.pkg.ApkManageBiz;
import com.lunzn.tool.log.LogUtil;

import java.io.File;

/**
 * 静默安装观察者
 *
 * @author Administrator
 * @version [版本号]
 * @date 2017年6月9日
 * @project COM.LZ.M02.LAUNCHER
 * @package com.lz.m02.launcher.biz
 * @package MyPakcageInstallObserver.java
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MyPakcageInstallObserver implements ApkManageBiz.PackageInstallListener {
    Context mContext;

    //文件名
    String apkFilepath;

    //包名
    String pkname;

    private Handler mHandler;

    public MyPakcageInstallObserver(Context mContext, Handler mHandler, String apkFilepath) {
        this.mHandler = mHandler;
        this.mContext = mContext;
        this.apkFilepath = apkFilepath;
    }

    @Override
    public void packageInstalled(String packageName, int returnCode) {
        LogUtil.d("====静默安装回调=====" + packageName + "   " + returnCode);
        if (returnCode == INSTALL_SUCCEEDED) {
            mHandler.sendEmptyMessage(MessageModel.MSG_INSTALL_COMPELETE);
            //安装完 删除本地文件
            if (apkFilepath != null) {
                File file = new File(apkFilepath);
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (returnCode == INSTALL_FAILED_ALREADY_EXISTS
                || returnCode == INSTALL_FAILED_UPDATE_INCOMPATIBLE) {
            MyPackageDeleteObserver deleteObserver = new MyPackageDeleteObserver(mContext, mHandler, apkFilepath);
            //安装不成功   则可能是文件冲突或文件损坏
            try {
                ApkManageBiz.autoUninstallApk(mContext, packageName, deleteObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
