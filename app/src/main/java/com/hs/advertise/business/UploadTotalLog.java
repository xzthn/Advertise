package com.hs.advertise.business;

import android.content.Context;
import android.os.Environment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hs.advertise.config.CloudConfig;
import com.hs.advertise.config.URLs;
import com.hs.advertise.model.MessageModel;
import com.hs.advertise.mvp.model.bean.AdConfigMap;
import com.hs.advertise.mvp.model.iterfaces.UserInfoBiz;
import com.hs.advertise.mvp.present.UserInfoPresent;
import com.hs.advertise.thread.DelecteLogThread;
import com.hs.advertise.utils.FileUtil;
import com.lunzn.tool.log.LogUtil;
import com.smart.util.Utils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 5分钟1次，不断从向服务器上传log
 */
public class UploadTotalLog implements UserInfoBiz {

    private static final String TAG = "Tag_" + UploadTotalLog.class.getSimpleName();
    private static UploadTotalLog uploadLog;

    private Context context;
    private ScheduledThreadPoolExecutor scheduled;
    private UserInfoPresent mUserInfoPresent = new UserInfoPresent(context, this);

    private UploadTotalLog(Context context) {
        this.context = context;
    }

    public static UploadTotalLog getUploadTotalLog(Context context) {
        if (uploadLog == null) {
            uploadLog = new UploadTotalLog(context);
        }
        return uploadLog;
    }

    /**
     * 5分钟1次，不断上传最新数据
     */
    public void start() {
        LogUtil.i(TAG, "uploadTotalLog starting");
        stop();
        scheduled = new ScheduledThreadPoolExecutor(1);
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetConnected(context)) {
                    LogUtil.i(TAG, "上传total日志中........");
                    uploadTotalLog();
                }
            }
        }, 3, 5 * 60, TimeUnit.SECONDS);
    }

    /**
     * 停止定时器
     */
    public void stop() {
        if (scheduled != null) {
            scheduled.shutdownNow();
            scheduled = null;
        }
    }

    public boolean isTaskRun() {
        if (scheduled != null) {
            if (!scheduled.isTerminated()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 上传total播放日志
     */
    public void uploadTotalLog() {
        JSONArray content = FileUtil.readFileToJson(MessageModel.JSON_LOCAL_PATH);

//        File file = new File(Environment.getExternalStorageDirectory().getPath(), "launcher_ad.json");
        JSONArray content1 = FileUtil.readFileToJson(Environment.getExternalStorageDirectory().getPath() + "/launcher_ad.json");//launcher 的广告日志
        content.addAll(content1);

//        LogUtil.i(TAG, "上传日志的内容：" + content);
        long dataVersion = CloudConfig.getCloudConfig().getLong(context, CloudConfig.KEY_DATA_VERSION);
        AdConfigMap map = new AdConfigMap();
        map.setDataJson(content.toString());
        map.setDataVersion(-1);
        mUserInfoPresent.commonOperate(URLs.BASE_URL + URLs.AD_UPLOAD_URL, map.getMapData(), "post");
    }

    @Override
    public void onSuccess(String url, Object object) {
        String data = JSONObject.toJSONString(object);
        LogUtil.i(TAG, "上传日志 " + data);
        JSONObject jb = JSONObject.parseObject(data);
        if (jb.getIntValue("retCode") == 0) {
            LogUtil.i(TAG, "上传日志成功");
            // 开启删除日志文件线程
            new Thread(new DelecteLogThread()).start();
        }
    }

    @Override
    public void onError(Throwable e) {

    }
}