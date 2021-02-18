package com.hs.advertise.ui.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.hs.advertise.MyApplication;
import com.hs.advertise.R;
import com.hs.advertise.business.PlayLogicUtil;
import com.hs.advertise.business.UpdateTempLog;
import com.hs.advertise.config.IntentValue;
import com.hs.advertise.config.TypeParam;
import com.hs.advertise.model.MessageModel;
import com.hs.advertise.model.PlayLogJson;
import com.hs.advertise.mvp.model.bean.AdInfo;
import com.hs.advertise.mvp.model.bean.AdResource;
import com.hs.advertise.mvp.model.bean.ClickAction;
import com.hs.advertise.receiver.BroadcastAction;
import com.hs.advertise.service.InterfaceService;
import com.hs.advertise.service.NetWorkService;
import com.hs.advertise.thread.ThreadManager;
import com.hs.advertise.ui.iterfaces.IViewUpdate;
import com.hs.advertise.utils.ActivityUtil;
import com.hs.advertise.utils.FileUtil;
import com.hs.advertise.utils.RxjavaUtils;
import com.lunzn.tool.autofit.AutoTextView;
import com.lunzn.tool.log.LogUtil;
import com.lunzn.tool.video.LunznMediaPlayer;
import com.lunzn.tool.video.MediaEventCallback;
import com.smart.localfile.LocalFileCRUDUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc: 视频播放页面
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.ui.PhotoActivity
 * ProjectName: Advertise
 * Date: 2020/3/11 10:07
 */
public class VideoActivity extends BaseActivity implements IViewUpdate {

    private static final String TAG = "Tag_" + VideoActivity.class.getSimpleName();
    private static final int UPDATE = 10001;
    private static final int START_TASK = 10002;

    private AutoTextView tvSkip;
    private AutoTextView tvDetail;
    private AutoTextView tvTime;
    private FrameLayout simpleExoPlayerView;

    private long currentTime;
    private long resumePosition;
    private long mDuration;
    private boolean isRemoteUpdate;
    private boolean isNetURL = false;
    private boolean isPause;

    //视频缓存代理
    private HttpProxyCacheServer proxy;
    //服务器请求获取到的视频URL列表
    private List<AdResource> mAdResources = new ArrayList<>();
    private MyBroadcastReceiver receiver;
    private ArrayList<String> urlList = new ArrayList();
    private LunznMediaPlayer mExoPlayer;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE) {

                LogUtil.i(TAG, "数据更新，重新开始播放 isPause" + isPause + " isRemoteUpdate " + isRemoteUpdate);
                if (isPause || !isRemoteUpdate) {
                    LogUtil.i(TAG, "activity isPause or is not update,so return " + isPause);
                    return;
                }
                isRemoteUpdate = false;
                writeTotal();
                mExoPlayer.stop();
                initCanQuit(true);
                showClickActionView();
                initExoplayer();
            } else if (msg.what == START_TASK) {
                startTask();
            }
        }
    };
    // 播放回调
    private MediaEventCallback mCallback = new MediaEventCallback() {
        @Override
        public void onPlayEvent(int eventType, Bundle bundle) {
            if (eventType == MediaEventCallback.EVENT_MEDIA_PLAY_ERROR) {
                LogUtil.i(TAG, "EVENT_MEDIA_PLAY_ERROR playVideo");
                Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_LONG).show();
                ActivityUtil.getInstance().finishAllActivity();


            } else if (eventType == MediaEventCallback.EVENT_MEDIA_PLAY_COMPLETE) {
                resumePosition = 0;
                LogUtil.i(TAG, "播放完一个视频 mAdPosition " + mAdPosition + " 当前位置 " + mCurrentPlayPosition);
                boolean isLast = mCurrentPlayPosition >= urlList.size() - 1;
                LogUtil.i(TAG, "播放完一个视频 isLast " + isLast);
                if (isLast && mAdPosition == IntentValue.AD_POSITION_APP) {//应用启动视频，播放到最后一个退出
                    writeTotal();
                    finishJump();
                    return;
                } else if (isLast && mAdPosition == IntentValue.AD_POSITION_BOOT) {//全屏视频，播放到最后一个循环播放
                    urlList.clear();
                    for (int i = 0; i < mAdResources.size(); i++) {
                        Uri videoUri = getUri(mAdResources.get(i).getUrl(), mAdResources.get(i).getCrcCode());
                        urlList.add(videoUri.toString());
                    }
                    mCurrentPlayPosition = 0;
                } else {
                    mCurrentPlayPosition++;
                }


                LogUtil.i(TAG, " 计算后位置 " + mCurrentPlayPosition + " uri " + urlList.get(mCurrentPlayPosition));
                if (urlList.get(mCurrentPlayPosition) != null) {
                    mExoPlayer.playUrl(urlList.get(mCurrentPlayPosition), false);
                }

            } else if (eventType == MediaEventCallback.EVENT_MEDIA_PREPARED) {
                LogUtil.i(TAG, "event callback prepared");
                if (mExoPlayer != null) {
                    if (resumePosition > 0) {

                        mExoPlayer.setTime(resumePosition);
                    }
                    mDuration = mExoPlayer.getLength();
//                    startTask();
                    mHandler.sendEmptyMessage(START_TASK);
                }

            } else if (eventType == MediaEventCallback.EVENT_MEDIA_PLAY_START_OK) {
                LogUtil.i(TAG, "event callback PLAY_START_OK");
                if (mExoPlayer != null) {
                    if (resumePosition > 0) {

                        mExoPlayer.setTime(resumePosition);
                    }
//                    startTask();
                    mDuration = mExoPlayer.getLength();
                    mHandler.sendEmptyMessage(START_TASK);
                }

            }
        }
    };

    @Override
    protected int getLayoutresID() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        return R.layout.activity_video;
    }

    @Override
    public void initData(Intent intent) {
        super.initData(intent);
        mAdResources = mAdInfo.getAdResources();
        showClickActionView();
        initExoplayer();
    }

    @Override
    protected void initListener() {
        LogUtil.i(TAG, "initListener");
    }

    @Override
    protected void initView() {
        simpleExoPlayerView = (FrameLayout) findViewById(R.id.simpleExoPlayerView);
        tvDetail = (AutoTextView) findViewById(R.id.tv_detail);
        tvSkip = (AutoTextView) findViewById(R.id.tv_skip);
        tvTime = (AutoTextView) findViewById(R.id.tv_time);
        tvDetail.setAlpha((float) 0.5);
        tvSkip.setAlpha((float) 0.5);
        tvTime.setAlpha((float) 0.5);
        tvDetail.setTextSize(14);
        tvSkip.setTextSize(14);
        tvTime.setTextSize(14);
    }

    @Override
    protected void initReceiver() {
        // 初始化广播过滤，监听服务器数据更新
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.ACTION_UPDATE_VIDEO);
        intentFilter.addAction(BroadcastAction.ACTION_UPDATE);
        intentFilter.addAction(NetWorkService.NET_SPEED_RECEIVER_ACTION);
        intentFilter.addAction(BroadcastAction.NET_CHANGE);
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "onDestroy()");
        destroyPlayer();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private Uri getUri(String url, long crcCode) {
        proxy.crcCodeMap.put(url, crcCode);
        String proxyUrl = proxy.getProxyUrl(url);
        LogUtil.i(TAG, "proxyUrl:" + proxyUrl);
        if (proxyUrl.startsWith("http")) {
            isNetURL = true;
        }
        return Uri.parse(proxyUrl);
    }

    private void initExoplayer() {
        urlList.clear();
        proxy = MyApplication.getProxy(getApplication());
        for (int i = 0; i < mAdResources.size(); i++) {
            Uri videoUri = getUri(mAdResources.get(i).getUrl(), mAdResources.get(i).getCrcCode());
            urlList.add(videoUri.toString());
        }
        if (mExoPlayer == null) {
            mExoPlayer = new LunznMediaPlayer(this, simpleExoPlayerView, mCallback, null);
        }

        LogUtil.e(TAG, "uri " + urlList.get(mCurrentPlayPosition));
        if (urlList.get(mCurrentPlayPosition) != null) {
            mExoPlayer.playUrl(urlList.get(mCurrentPlayPosition), false);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG, "onResume resumePosition:" + resumePosition + " isPause " + isPause);
        if (isPause) {

            isPause = false;
            if (mExoPlayer == null) {
                LogUtil.i(TAG, "onResume(),mExoPlayer is null");

                initCanQuit(false);
                showClickActionView();
                initExoplayer();
            } else {
//            mExoPlayer.addSurfaceView();
                mExoPlayer.play();
                if (resumePosition != 0) {

                    mExoPlayer.setTime(resumePosition);
                }
            }
        }

    }

    private void showClickActionView() {
        ClickAction mClickAction = mAdInfo.getAdResources().get(mCurrentPlayPosition).getClickAction();
        if (mClickAction == null) {
            LogUtil.i(TAG, "动作为空");
            tvDetail.setVisibility(View.GONE);
        } else {
            tvDetail.setVisibility(View.VISIBLE);
            tvDetail.setText("按【OK键】查看详情");
            LogUtil.i(TAG, "动作不为空");
        }
    }

    public void startTask() {

        startTempLog();
        LogUtil.i(TAG, "canQuit " + canQuit);
        if (canQuit == 0) {
            //如果不能跳过则隐藏此按钮
            tvSkip.setVisibility(View.GONE);
        } else if (canQuit == 1) {
            if (mAdPosition == IntentValue.AD_POSITION_APP) {
                starSkipTime();
            } else {
                tvSkip.setVisibility(View.VISIBLE);
                tvSkip.setText("按【返回键】可跳过此广告");
            }
        }
        if (mAdPosition == IntentValue.AD_POSITION_APP) {
            starTotalTime();
        } else {
            tvTime.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(TAG, "onPause()");
        RxjavaUtils.cancel();
        UpdateTempLog.getUpdateTempLog(getApplicationContext()).stop();
        isPause = true;
        if (mExoPlayer != null) {

            mExoPlayer.pause();
//            mExoPlayer.removeSurfaceView();
        }
        LogUtil.i(TAG, "onPause end");

    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i(TAG, "onStop()");
        if (mExoPlayer != null) {
            resumePosition = Math.max(0, mExoPlayer.getTime());
            LogUtil.i(TAG, "onStop resumePosition:" + resumePosition);
            writeTotal();
            destroyPlayer();
        }
    }

    private void startTempLog() {
        currentTime = System.currentTimeMillis();
        //开启一个间隔一分钟更新temp文件的定时任务
        UpdateTempLog.getUpdateTempLog(getApplicationContext()).start(currentTime);
        writeTemp();
    }

    /**
     * 开始播放的同时把日志写入temp文件中
     */
    private void writeTemp() {
        LogUtil.i(TAG, "video writeTemp ,mExoPlayer.getDuration() " + mDuration);
        if (mAdInfo != null && mDuration > 0 && mCurrentPlayPosition < mAdResources.size()) {
            AdResource adResource = mAdResources.get(mCurrentPlayPosition);
            LogUtil.i(TAG, "adResource name:" + adResource.getName());
            final PlayLogJson logJson = new PlayLogJson();
            logJson.setAdPosition(mAdPosition);
            logJson.setDuration(mDuration);
            logJson.setResourceId(adResource.getResourceId());
            ThreadManager.getShortPool().execute(new Runnable() {
                @Override
                public void run() {
                    FileUtil.writeJsonFileOnlyLine(logJson.getJson(), MessageModel.JSON_TEMP_LOCAL_PATH + "/temp_" + currentTime + ".json");
                }
            });
        }
    }

    /**
     * 结束播放一条视频时把日志写到总的日志文件中
     */
    private void writeTotal() {
        LogUtil.i(TAG, "video writeTotal()");
        //把值赋给另外一个变量，防止currentTime被改变
//        final long time = currentTime;
//        final long duration = mDuration;
        ThreadManager.getShortPool().execute(new Runnable() {
            @Override
            public void run() {
                String tempPath = MessageModel.JSON_TEMP_LOCAL_PATH + "/temp_" + currentTime + ".json";
                //复制temp文件的内容到总的日志文件
                if (FileUtil.copyToTotal(mDuration, tempPath)) {
                    // 删除temp文件
                    LocalFileCRUDUtils.deleteFile(tempPath);
                }
            }
        });
    }

    @Override
    public void doProgress(long number, int type) {
        if (type == 1) {
            tvSkip.setVisibility(View.VISIBLE);
            tvSkip.setText(number + "秒后 按【返回键】可跳过此广告");
        } else if (type == 2) {
//            LogUtil.i(TAG, "doProgress：" + number);
            tvTime.setVisibility(View.VISIBLE);
            tvTime.setText(number + "秒");

//            tvDetail.setVisibility(View.VISIBLE);
//            tvDetail.setText("按【OK键】查看详情");
        }
    }

    @Override
    public void onComplete(int type) {
        if (type == 1) {
            tvSkip.setVisibility(View.VISIBLE);
            tvSkip.setText("按【返回键】可跳过此广告");
        } else if (type == 2) {
            tvTime.setVisibility(View.GONE);
//            tvDetail.setVisibility(View.VISIBLE);
//            tvDetail.setText("按【OK键】查看详情");
        }
    }

    private void starSkipTime() {
        //跳过是1，总的是2
        starSkipTime(this, 1);
    }

    private void starTotalTime() {
        starTotalTime(this, 2);
    }

    public void destroyPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.destroy();
            mExoPlayer = null;
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastAction.ACTION_UPDATE)) {
                LogUtil.i(TAG, "视频源更新完毕，你只要加载播放即可 mAdPosition " + mAdPosition);
                if (mAdPosition == IntentValue.AD_POSITION_BOOT) {
                    AdInfo mFullScreenAds = PlayLogicUtil.getFullScreenAds();
                    if (mFullScreenAds != null) {
                        mAdInfo = mFullScreenAds;
                        if (mFullScreenAds.getType() == TypeParam.TYPE_MULTIPLE_VIDEO || mFullScreenAds.getType() == TypeParam.TYPE_SINGLE_VIDEO) {
                            int size = mAdInfo.getAdResources().size();
                            LogUtil.i(TAG, " 更新完毕 size," + size);
                            if (size > 0) {
//                                mAdResources.clear();
                                mAdResources = mAdInfo.getAdResources();
                                isRemoteUpdate = true;
                                mHandler.sendEmptyMessage(UPDATE);
                            } else {
                                finish();
                            }
                        } else {
                            PlayLogicUtil.bootStartActivity(getApplicationContext(), mFullScreenAds);
                            finish();
                        }
                    } else {
                        finish();
                    }
                }
            } else if (NetWorkService.NET_SPEED_RECEIVER_ACTION.equalsIgnoreCase(action)) {
                if (isNetURL) {
                    Toast.makeText(getApplicationContext(), "当前网速较差，请切换网络", Toast.LENGTH_LONG).show();
                }

            } else if (BroadcastAction.NET_CHANGE.equalsIgnoreCase(action)) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (InterfaceService.hasCachVideo(getApplicationContext(), mAdInfo)) {
                            Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_LONG).show();
                            writeTotal();
                            finishJump();

                        }
                    }
                }, 5000);

            }
        }
    }
}