package com.hs.advertise.business;

import android.content.Context;
import android.widget.VideoView;

import com.hs.advertise.model.MessageModel;
import com.hs.advertise.utils.FileUtil;
import com.hs.advertise.utils.RxjavaUtils;
import com.lunzn.tool.log.LogUtil;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 更新临时日志
 */
public class UpdateTempLog {

    private static final String TAG = "Tag_" + UpdateTempLog.class.getSimpleName();
    private static UpdateTempLog updateTempLog;

    private Context context;
    private ScheduledThreadPoolExecutor scheduled;


    private UpdateTempLog(Context context) {
        this.context = context;
    }

    public static UpdateTempLog getUpdateTempLog(Context context) {
        if (updateTempLog == null) {
            updateTempLog = new UpdateTempLog(context);
        }
        return updateTempLog;
    }


    /**
     * 1分钟1次，不断更新temp日志
     */
    public void start(final VideoView videoView, final long currentTime) {
        stop();
        scheduled = new ScheduledThreadPoolExecutor(1);
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (videoView != null && videoView.isPlaying()) {
                    FileUtil.modifyTemp(videoView.getCurrentPosition(), MessageModel.JSON_TEMP_LOCAL_PATH + "/temp_" + currentTime + ".json");
                }
            }
        }, 3, 60, TimeUnit.SECONDS);

    }


    public void start(final long currentTime) {
        stop();
        scheduled = new ScheduledThreadPoolExecutor(1);
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                RxjavaUtils.doOnUIThread(new RxjavaUtils.UITask() {
                    @Override
                    public void doOnUI() {
                        RxjavaUtils.doOnThread(new RxjavaUtils.ThreadTask() {
                            @Override
                            public void doOnThread() {
                                LogUtil.i(TAG, "更新temp文件当前时长字段 currentTime " + currentTime);
                                if (currentTime > 0) {
                                    FileUtil.modifyTemp(currentTime, MessageModel.JSON_TEMP_LOCAL_PATH + "/temp_" + currentTime + ".json");
                                }
                            }
                        });
                    }
                });

            }
        }, 3, 30, TimeUnit.SECONDS);

    }


    /**
     * 停止定时器
     */
    public void stop() {

        if (scheduled != null) {
            LogUtil.i(TAG, "停止temp定时器");
            try {
                scheduled.shutdownNow();
                scheduled = null;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.i(TAG, "停止temp定时器异常，" + e.getMessage());
            }

        }
    }

}
