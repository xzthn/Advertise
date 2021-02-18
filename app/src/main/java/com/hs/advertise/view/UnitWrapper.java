package com.hs.advertise.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.hs.advertise.biz.ClickActionBiz;
import com.hs.advertise.biz.MyPakcageInstallObserver;
import com.hs.advertise.model.MessageModel;
import com.hs.advertise.mvp.model.bean.ClickAction;
import com.lunzn.download.util.BaseDownloadInfoCallBack;
import com.lunzn.download.util.IDownloadInfoCallBack;
import com.lunzn.systool.pkg.ApkManageBiz;
import com.lunzn.tool.log.LogUtil;
import com.lunzn.tool.toast.LzToast;

import java.io.File;

/**
 * 每一个ItemView的外部统一格式包裹视图
 *
 * @author renweiming
 * @version [版本号]
 * @date 2017年1月16日
 * @project COM.LZ.M02.LAUNCHER
 * @package com.lz.m02.launcher.layout.view
 * @package UnitWrapper.java
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class UnitWrapper {

    private static final String TAG = UnitWrapper.class.getSimpleName();
    private final ClickAction mClickActionData;

    /**
     * 上下文
     */
    private Context mContext = null;


    private Handler mHandler = null;

    BaseDownloadInfoCallBack downloadCallback = new BaseDownloadInfoCallBack(mContext, new IDownloadInfoCallBack() {

        @Override
        public void onDownloadProgressCallback(long downloadsize, long totlesize) {

            int lastPrecent = 0;
            int currentPrecent = (int) (((float) downloadsize / totlesize) * 100);
            if (currentPrecent==0){
                Message msg = Message.obtain();
                msg.what = MessageModel.MSG_START_DOWNLOAD;
                mHandler.sendMessage(msg);
            }
            if (currentPrecent > lastPrecent && currentPrecent - lastPrecent > 5) {
                Message msg = Message.obtain();
                msg.what = MessageModel.MSG_DOWNLOAD_PRECENT_REFRESH;
                msg.obj = currentPrecent;
                mHandler.sendMessage(msg);
                lastPrecent = currentPrecent;
            }
        }


        @Override
        public void onDownloadCompleteCallback(String savePath) {
            LogUtil.i("savePath" + savePath);
            mHandler.sendEmptyMessage(MessageModel.MSG_DOWNLOAD_COMPELETE);
            //去安装
            //            Utils.installApk(mContext, savePath);
            //静默强制安装
            MyPakcageInstallObserver observer = new MyPakcageInstallObserver(mContext, mHandler, savePath);
            try {
                LogUtil.i("下载完成  静默安装 ");
                mHandler.sendEmptyMessage(MessageModel.MSG_INSTALL_APK);
                ApkManageBiz.autoInstallApk(mContext, savePath, mClickActionData.getAppPkgName(), observer);
            } catch (Exception e) {
                //安装完 删除本地文件
                if (savePath != null) {
                    File file = new File(savePath);
                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(MessageModel.MSG_INSTALL_FAILED);
                } else {
                    mHandler.sendEmptyMessage(MessageModel.MSG_DWONLOAD_FAIL);
                }
                e.printStackTrace();
            }
        }

        @Override
        public void onDownloadSpaceNotEnough(String downloadPath) {
            LogUtil.i(TAG, "onDownloadSpaceNotEnough downloadPath = " + downloadPath);
            mHandler.sendEmptyMessage(MessageModel.MSG_DWONLOAD_FAIL_NO_STORAGE);
        }

        @Override
        public void onDownloadFail() {
            LogUtil.i(TAG, "onDownloadFail ");
            mHandler.sendEmptyMessage(MessageModel.MSG_DWONLOAD_FAIL);
        }
    });


    public UnitWrapper(final Context context , ClickAction data) {
        mContext = context;
        mHandler=new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MessageModel.MSG_START_DOWNLOAD:
                        LogUtil.i(TAG, "后台下载中");
                        LzToast.showToast(mContext, "后台下载中", 2000);
                        break;
                    case MessageModel.MSG_INSTALL_APK:
                        LogUtil.i(TAG, "在后台安装中");
                        LzToast.showToast(mContext, "在后台安装中", 2000);
                        break;
                    case MessageModel.MSG_DOWNLOAD_COMPELETE:
                        //下载完成
                        LogUtil.i(TAG, "下载完成");
                        LzToast.showToast(mContext, "下载完成", 2000);
                        break;
                    case MessageModel.MSG_DOWNLOAD_PRECENT_REFRESH:
                        //下载进度更新
                        break;
                    case MessageModel.MSG_INSTALL_COMPELETE:
                        //安装完成
                        LogUtil.i(TAG, "安装完成");
                        LzToast.showToast(mContext, "安装完成", 2000);
                        break;
                    case MessageModel.MSG_DWONLOAD_FAIL:
                        LogUtil.i(TAG, "应用下载失败");
                        LzToast.showToast(mContext, "应用下载失败", 2000);
                        //下载失败
                        break;
                    case MessageModel.MSG_INSTALL_FAILED:
                        LogUtil.i(TAG, "应用安装失败");
                        LzToast.showToast(mContext, "应用安装失败", 2000);
                        //安装失败
                        break;
                    case MessageModel.MSG_DWONLOAD_FAIL_NO_STORAGE:
                        LogUtil.i(TAG, "可用空间不足，应用下载失败");
                        LzToast.showToast(mContext, "可用空间不足，应用下载失败", 2000);
                        //安装失败
                        break;
                }
            }
        };
        mClickActionData=data;
    }



    public void onClick() {
        LogUtil.i("clickActionBiz onClick");
        if (mClickActionData != null) {
            try {
                // 每种数据点击后的不同处理
                ClickActionBiz.onClick(mClickActionData, mContext, downloadCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
