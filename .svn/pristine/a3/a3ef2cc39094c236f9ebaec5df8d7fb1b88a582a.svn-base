package com.hs.advertise.service;

import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.lunzn.tool.log.LogUtil;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class NetWorkService extends Service {
    public final static String TAG = "NetWorkService";
    public final static String NET_SPEED_RECEIVER_ACTION = "com.ridgepm.network_speed_action";
    private Handler handler = new Handler(Looper.getMainLooper());
    private Timer timer;
    private long rxtxTotal = 0;
    private boolean isNetBad = false;
    private int time;
    private double rxtxSpeed = 1.0f;
    private DecimalFormat showFloatFormat = new DecimalFormat("0.00");
    private Intent receiverIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0L, (long) 2000);
        }
        receiverIntent = new Intent();
        receiverIntent.setAction(NET_SPEED_RECEIVER_ACTION);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        if (timer != null) {

            timer.cancel();
            timer = null;
        }
    }

    //定时任务
    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            isNetBad = false;
            long tempSum = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
            long rxtxLast = tempSum - rxtxTotal;
            double tempSpeed = rxtxLast * 1000 / 2000;
            rxtxTotal = tempSum;
            if ((tempSpeed / 1024d) < 20 && (rxtxSpeed / 1024d) < 20) {
                time += 1;
            } else {
                time = 0;
            }
            rxtxSpeed = tempSpeed;
            LogUtil.i(TAG, showFloatFormat.format(tempSpeed / 1024d) + "kb/s");
            if (time >= 4) {//连续四次检测网速都小于20kb/s  断定网速很差.
                isNetBad = true;
                LogUtil.i(TAG, "网速差 " + isNetBad);
                time = 0; //重新检测
            }
            if (isNetBad) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        receiverIntent.putExtra("is_slow_net_speed", isNetBad);
                        sendBroadcast(receiverIntent);//发送广播去取消这次请求.

                    }
                });
            }
        }

    }
}

