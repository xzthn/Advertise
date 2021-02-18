package com.hs.advertise.ui.activity.test.inter;

import android.view.SurfaceHolder;

import java.util.List;

/**
 * Created by ganxinrong on 2019/5/15.
 * 视频播放器接口
 */
public interface IWrtVideoPlayer {

    /**
     * 初始化播放器
     * @param holder 视频渲染界面
     * @param listener 播放事件回调
     * @return
     */
    boolean init(SurfaceHolder holder, IPlayListener listener);

    /**
     * 开始播放
     * @param filePaths 视频列表
     * @param loop 是否循环播放
     * @return
     */
    boolean play(List<String> filePaths, boolean loop);

    /**
     * @return 视频是否在播放中
     */
    public boolean isPlaying();

    /**
     *  开始播放
     */
    public void start();

    /**
     * 暂停播放
     */
    public void pause();

    /**
     * 销毁播放器
     * @return
     */
    boolean stop();

    public interface IPlayListener {
        void onPlayALoopComplete();
        void onVideoSizeChanged(int width, int height);
        boolean canPlay();
    }
}
