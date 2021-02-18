package com.hs.advertise.ui.activity.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.hs.advertise.R;
import com.hs.advertise.ui.activity.test.inter.IWrtVideoPlayer;
import com.hs.advertise.utils.LogPrint;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ganxinrong on 2018/3/27.
 * 多媒体门禁终端的视频广告界面
 */

public class VideoAdvertiseActivity extends Activity {
    private static final String TAG = "VideoAdvertisementFragment";
    private final int MAX_PIC_SIZE = 6;
    private SurfaceView mSurfaceView;
    private IWrtVideoPlayer mVideoPlayer;
    private volatile boolean mIsSurfaceCreated = false;
    private List<String>videoList=new ArrayList<>();
    private static int REFRESH_TYPE_INIT_STATE = 0;
    protected final int MSG_COPY_AD_FILES_FROM_SYSTEM_FINISH = 1;
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_COPY_AD_FILES_FROM_SYSTEM_FINISH:
                    LogPrint.d(TAG,"copy ad files from system finish msg.");
                    stopAdPlay();
                    startAdPlay();
                    break;
            }
        }
    };


    @Override
    public void onCreate( Bundle savedInstanceState) {
        LogPrint.d(TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        videoList.add("mnt/sdcard"+"/ad_video4.mp4");
        videoList.add("mnt/sdcard"+"/ad_video3.mp4");
        videoList.add("mnt/sdcard"+"/ad_video2.mp4");
        initViews();
        if (mVideoPlayer == null) {
            mVideoPlayer = new WrtVideoPlayer();
        }
    }

    private void startAdPlay() {
        if (!mIsSurfaceCreated) {
            LogPrint.e(TAG, "startAdPlay mIsSurfaceCreated=false, return.");
            return;
        }
        playVideoAdExternal();
    }

    // 播放sdcard的广告
    private boolean playVideoAdExternal() {
        if (mVideoPlayer == null) {
            LogPrint.e(TAG,"playVideoAdExternal mVideoPlayer=null, return.");
            return false;
        }
        LogPrint.d(TAG,"play sdcard external video ad...");
        return mVideoPlayer.play(videoList,true);
    }

    private void stopAdPlay() {
        LogPrint.d(TAG, "stopAdPlay");
        if (mVideoPlayer != null)
            mVideoPlayer.stop();
    }

    @Override
    public void onPause() {
        // 不能使用videoView.isPlaying()判断，刚开始播放时不准确
        //if (videoView.isPlaying()){
        LogPrint.d(TAG, "onPause...");
        if (mVideoPlayer != null) {
            mVideoPlayer.pause(); // 停止播放
        }
        //}
        super.onPause();
    }

    @Override
    public void onResume() {
        LogPrint.d(TAG,"onResume...");
        if (mVideoPlayer != null)
            LogPrint.d(TAG, "isPlaying=" + mVideoPlayer.isPlaying());
        else
            LogPrint.e(TAG, "mVideoPlayer=null");

        if (mVideoPlayer != null && !mVideoPlayer.isPlaying()){
            mVideoPlayer.start();; // 重新播放
        }
        super.onResume();
    }

    public void startPlayADVideo() {
        LogPrint.d(TAG,"startPlayADVideo...");
        if (mVideoPlayer != null)
            LogPrint.d(TAG, "isPlaying=" + mVideoPlayer.isPlaying());
        else
            LogPrint.e(TAG, "mVideoPlayer=null");
        if (mVideoPlayer != null && !mVideoPlayer.isPlaying()){
            mVideoPlayer.start(); // 重新播放
        }
    }

    public void stopPlayADVideo() {
        LogPrint.d(TAG,"stopPlayADVideo...");
        if (mVideoPlayer != null) {
            mVideoPlayer.pause(); // 停止播放
        }
    }


    @Override
    public void onDestroy() {
        LogPrint.d(TAG,"onDestroy...");
        super.onDestroy();
      if (mHandler != null) {
          mHandler.removeCallbacksAndMessages(null);
          mHandler = null;
      }
    }

    private void initViews() {
        mSurfaceView = (SurfaceView)findViewById(R.id.screensaver_videoView);
        mSurfaceView.getHolder().addCallback(mSurfaceCallback);
    }

    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogPrint.d(TAG,"广告视频surfaceCreated");
            mIsSurfaceCreated = true;
            initVideoPlayer(holder);
            stopAdPlay();
            startAdPlay();
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LogPrint.d(TAG,"广告视频surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            LogPrint.d(TAG,"广告视频surfaceDestroyed");
            mIsSurfaceCreated = false;
            if (mVideoPlayer != null) {
                mVideoPlayer.stop();
                mVideoPlayer = null;
            }
        }
    };

    private void initVideoPlayer(SurfaceHolder holder) {
        if (mVideoPlayer == null) {
            mVideoPlayer = new WrtVideoPlayer();
        }
        mVideoPlayer.init(holder, new IWrtVideoPlayer.IPlayListener() {
            @Override
            public void onPlayALoopComplete() {
                LogPrint.d(TAG,"onPlayALoopComplete");
                playVideoAdExternal();
            }
            @Override
            public void onVideoSizeChanged(int videoWidth, int videoHeight) {
                int surfaceWidth = getResources().getDisplayMetrics().widthPixels;
                int surfaceHeight = getResources().getDisplayMetrics().heightPixels;
                if (surfaceWidth == 0 || surfaceWidth == 0) {
                    return;
                }
                if (videoWidth > 0 && videoHeight > 0) {
                    if ( videoWidth * surfaceHeight  > surfaceWidth * videoHeight ) {
                        surfaceHeight = surfaceWidth * videoHeight / videoWidth;
                    } else if ( videoWidth * surfaceHeight  < surfaceWidth * videoHeight ) {
                        surfaceWidth = surfaceHeight * videoWidth / videoHeight;
                    }
                }

                if (surfaceWidth > 0 && surfaceHeight > 0) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)mSurfaceView.getLayoutParams();
                    layoutParams.width = surfaceWidth;
                    layoutParams.height = surfaceHeight;
                    mSurfaceView.setLayoutParams(layoutParams);
                }
            }
            @Override
            public boolean canPlay() {
                if (!VideoAdvertiseActivity.this.isFinishing()) {
                    return true;
                }
                return false;
            }
        });
    }


}
