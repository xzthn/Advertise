package com.hs.advertise.ui.activity.test;

import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.SurfaceHolder;

import com.hs.advertise.ui.activity.test.inter.IWrtVideoPlayer;
import com.hs.advertise.utils.LogPrint;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ganxinrong on 2019/5/15.
 * 广告视频播放器
 */
public class WrtVideoPlayer implements IWrtVideoPlayer {
    private static final String TAG = "WrtVideoPlayer";
    //android播放器
    private MediaPlayer player;

    //视频渲染界面
    private SurfaceHolder surfaceHolder;

    //播放事件回调
    private IPlayListener listener;

    //播放视频列表
    private List<String> videoFilePaths;

    //播放视频索引
    private int videoIndex;

    //是否循环播放
    private boolean isLooping;


    public WrtVideoPlayer() {
        videoFilePaths = new LinkedList<>();
    }

    @Override
    public boolean init(SurfaceHolder holder, IWrtVideoPlayer.IPlayListener listener) {
        this.listener = listener;
        surfaceHolder = holder;
        return true;
    }

    @Override
    public boolean play(List<String> filePaths, boolean loop) {
        isLooping = loop;
        videoFilePaths.clear();
        videoFilePaths.addAll(filePaths);
        videoIndex = 0;
        if (player == null) {
            createPlayer();
        }
       return playNext();
    }

    @Override
    public boolean isPlaying() {
        if (player != null){
            return player.isPlaying();
        }

        return false;
    }

    @Override
    public void start() {
        LogPrint.d(TAG,"start");
        if (player != null){
            LogPrint.d(TAG,"播放器开始！");
            player.start();
        }
    }

    @Override
    public void pause() {
        LogPrint.d(TAG,"pause");
        if (player != null && player.isPlaying()) {
            LogPrint.d(TAG,"播放器暂停！");
            player.pause();
        }
    }

    @Override
    public boolean stop() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.reset();
            player.release();
            LogPrint.d(TAG,"销毁播放器！");
        }
        player = null;
        return false;
    }

    private void createPlayer() {
        if (surfaceHolder == null) {
            return;
        }
        try {
            player = new MediaPlayer();
            player.setDisplay(surfaceHolder);
            player.setOnCompletionListener(onCompletionListener);
            player.setOnPreparedListener(onPreparedListener);
            player.setOnErrorListener(onErrorListener);
            player.setOnVideoSizeChangedListener(mSizeChangedListener);
            LogPrint.d(TAG,"创建播放器成功！");
        } catch (Exception e) {
            e.printStackTrace();
            LogPrint.e(TAG,"创建播放器失败！" + e.getLocalizedMessage());
        }
    }

    private boolean playNext() {
        if (listener != null && !listener.canPlay()) {
            LogPrint.e(TAG,"调用者不允许播放！");
            return false;
        }

        if (surfaceHolder == null) {
            LogPrint.e(TAG,"surfaceHolder为空！");
            return false;
        }

        if (videoFilePaths.size() == 0) {
            LogPrint.e(TAG,"没有视频列表！");
            return false;
        }

        if (player == null) {
            LogPrint.e(TAG,"播放器未创建！");
            return false;
        }

        if (videoIndex >= videoFilePaths.size()) {
            videoIndex = 0;
        }

        String videoPath = videoFilePaths.get(videoIndex);
        if (TextUtils.isEmpty(videoPath)) {
            LogPrint.e(TAG,"视频地址为空！");
            return false;
        }
        try {
            if (player.isPlaying()) {
                player.stop();
            }
            player.reset();
            player.setDataSource(videoPath);
            player.prepareAsync();
            LogPrint.d(TAG,"prepareAsync file: " + videoPath);
        } catch (Exception e) {
            e.printStackTrace();
            LogPrint.e(TAG,"播放出错！"+ e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            LogPrint.e(TAG,"播放器出错了！what=" + what + ",extra=" + extra);
            return false;
        }
    };

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            try {
                player.start();
                LogPrint.d(TAG,"准备完成，开始播放!");
            } catch (Exception e) {
                e.printStackTrace();
                LogPrint.e(TAG,"准备完成，播放出错!" + e.getLocalizedMessage());
            }
        }
    };

    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
            new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    LogPrint.d(TAG,"video size change: "  + width + "X" + height);
                    changeVideoSize();
                }
            };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (player == null) {
                LogPrint.d(TAG,"play complete,but player already destroyed...");
                return;
            }
            videoIndex++;
            if (videoIndex >= videoFilePaths.size() && listener != null) {
                listener.onPlayALoopComplete();
            } else {
                playNext();
            }
        }
    };

    public void changeVideoSize() {
        int videoWidth = player.getVideoWidth();
        int videoHeight = player.getVideoHeight();
        if (listener != null) {
            listener.onVideoSizeChanged(videoWidth,videoHeight);
        }
    }

}
