package com.hs.advertise.utils;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.utils
 * ProjectName: trunk
 * Date: 2020/4/14 18:47
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lunzn.tool.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 动画工具类
 *@createDate 2018/11/20 16:16
 *@description
 **/
public class AnimatorUtils {

    public static final String ALPHA="Alpha";
    public static final String TRANSX="TranslationX";
    public static final String TRANSY="TranslationY";
    public static final String SCALEX="ScaleX";
    public static final String SCALEY="ScaleY";
    public static final String ROTATION="Rotation";
    public static final String ROTATIONX="RotationX";
    public static final String ROTATIONY="RotationY";

    /**
     * 默认的TimeInterpolator,前后减速，中间加速
     */
    private static final TimeInterpolator sDefaultInterpolator =
            new LinearInterpolator();

    public static AnimatorSetWrap createAnimator() {
        return new AnimatorSetWrap();
    }

    /**
     * @param interpolator 默认的TimeInterpolator
     * @return
     */
    public static AnimatorSetWrap createAnimator(TimeInterpolator interpolator) {
        return new AnimatorSetWrap(interpolator);
    }

    /**
     * 属性动画组合
     * 属性动画组合对应的是AnimatorSet类，我们只需要new他就好。
     *
     * 它对应的主要有这四个方法，play，before，with，after。
     * 这四个方法里面全都是填入往后儿们的animator类，
     * 但是先后执行顺序不一样，分别对应着开启，最后，同步，最开始执行。
     * 我们注意到他是先执行的after，然后是play和with同时执行，最后执行的before。
     * 所以大家记住这个顺序，无论怎么写，都是这个执行顺序。
     *
     */
    public static class AnimatorSetWrap{

        private View mView;
        /**
         * 不设置默认插值器时，工具类自带的默认插值器
         */
        private TimeInterpolator mTimeInterpolator;
        /**
         * 判断play方法只允许执行一次的布尔值
         */
        boolean mIsPlaying=false;
        /**
         * 联合动画的动画容器
         */
        private AnimatorSet mAnimatorSet;
        /**
         * 联合动画的动画构造器
         */
        private AnimatorSet.Builder mAnimatorBuilder;
        /**
         * 默认执行时间
         */
        private int mDuration=1000;
        /**
         * play的监听器类
         */
        PlayAnimationListener playListener;
        /**
         * before的监听器类
         */
        BeforeAnimationListener beforeListener;
        /**
         * with的监听器类
         */
        WithAnimationListener withListener;
        /**
         * after的监听器类
         */
        AfterAnimationListener afterListener;
        /**
         * then的监听器类
         */
        ThenAnimationListener thenListener;
        /**
         * 顺序播放或者同时播放时存储动画的列表容器
         */
        List<Animator> mAnimatorList;
        /**
         * 是否已经初始化then动画
         */
        boolean mHasInitThenAnim=false;

        private AnimatorSetWrap(){
            this(sDefaultInterpolator);
        }

        /**
         * 构造方法
         * 主要是负责
         * 1.初始化默认的插值器 mTimeInterpolator
         * 2.初始化联合动画Set mAnimatorSet
         * 3.初始化顺序或同时播放动画容器 mAnimatorList
         * @param interpolator
         */
        private AnimatorSetWrap(TimeInterpolator interpolator) {
            mTimeInterpolator = interpolator;
            mAnimatorSet = new AnimatorSet();
            mAnimatorList=new ArrayList<>(16);
        }

        /**
         * Play动画的监听启动方法
         * 如果要监听play动画先调用这个方法
         * @return
         */
        public PlayAnimationListener toAddPlayListener(){
            playListener=new PlayAnimationListener(this);
            return playListener;
        }
        /**
         * Before动画的监听启动方法
         * 如果要监听Before动画先调用这个方法
         * @return
         */
        public BeforeAnimationListener toAddBeforeListener(){
            beforeListener=new BeforeAnimationListener(this);
            return beforeListener;
        }
        /**
         * With动画的监听启动方法
         * 如果要监听With动画先调用这个方法
         * @return
         */
        public WithAnimationListener toAddWithListener(){
            withListener=new WithAnimationListener(this);
            return withListener;
        }
        /**
         * After动画的监听启动方法
         * 如果要监听After动画先调用这个方法
         * @return
         */
        public AfterAnimationListener toAddAfterListener(){
            afterListener=new AfterAnimationListener(this);
            return afterListener;
        }

        /**
         * 顺序或同时播放动画执行时的监听方法
         * 要先于Then方法进行调用
         * @return
         */
        public ThenAnimationListener toAddThenListener(){
            thenListener=new ThenAnimationListener(this);
            return thenListener;
        }

        /**
         * 顺序或者同时播放动画时的调用方法
         * 在其内部生成一个Animator并将该Animator加入到mAnimatorList中备用
         * @param view 动画执行的主体View
         * @param animName 动画类型
         * @param interpolator 动画插值器 如果不设置就用默认的
         * @param repeatCount 重复次数
         * @param duration 执行时间
         * @param values 动画执行的值
         * @return
         */
        public AnimatorSetWrap then(View view, String animName, @Nullable TimeInterpolator interpolator, @Size(min = 0,max=Integer.MAX_VALUE) int repeatCount, @Size(min = 0,max=Integer.MAX_VALUE) int duration, float... values){
            LogUtil.i("addThen");
            if(view==null){
                throw new RuntimeException("view 不能为空");
            }
            mIsPlaying = true;
            mView = view;
            ObjectAnimator thenAnimator = ObjectAnimator.ofFloat(view,animName,values);
            thenAnimator.setInterpolator(interpolator==null?mTimeInterpolator:interpolator);
            thenAnimator.setRepeatCount(repeatCount<0?0:repeatCount);
            thenAnimator.setDuration(duration<0?mDuration:duration);
            if (thenListener!=null&&thenListener.animatorListener != null) {
                thenAnimator.addListener(thenListener.animatorListener);
            }
            if(thenListener!=null&&thenListener.updateListener!=null){
                thenAnimator.addUpdateListener(thenListener.updateListener);
            }
            if(thenListener!=null&&thenListener.pauseListener!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    thenAnimator.addPauseListener(thenListener.pauseListener);
                }else{
                    throw new RuntimeException("SDK最小要求19");
                }
            }
            mAnimatorList.add(thenAnimator);
            return this;
        }

        public AnimatorSetWrap then(Animator animator) {
            mAnimatorList.add(animator);
            return this;
        }

        public AnimatorSetWrap then(AnimatorSetWrap animator) {
            mAnimatorList.add(animator.getAnimatorSet());
            return this;
        }

        /**
         * AnimatorSet的Play方法，整个动画过程只能调用一次
         * 并且一旦执行play方法将会清除掉mAnimatorList中存储的顺序或同时执行的方法实例
         * @param view 方法主体
         * @param animName 动画类型
         * @param interpolator 插值器
         * @param repeatCount 重复次数
         * @param duration 动画时长
         * @param values 动画执行值
         * @return
         */
        public AnimatorSetWrap play(View view, String animName, @Nullable TimeInterpolator interpolator, @Size(min = 0,max=Integer.MAX_VALUE) int repeatCount, @Size(min = 0,max=Integer.MAX_VALUE) int duration, float... values){
            LogUtil.i("play");
            if(mIsPlaying){
                throw new RuntimeException("AnimatorSetWrap.play()方法只能调用一次");
            }
            if(view==null){
                throw new RuntimeException("view 不能为空");
            }
            mIsPlaying = true;
            mView = view;
            ObjectAnimator playAnimator = ObjectAnimator.ofFloat(view,animName,values);
            playAnimator.setInterpolator(interpolator==null?mTimeInterpolator:interpolator);
            playAnimator.setRepeatCount(repeatCount<0?0:repeatCount);
            playAnimator.setDuration(duration<0?mDuration:duration);
            if (playListener!=null&&playListener.animatorListener != null) {
                playAnimator.addListener(playListener.animatorListener);
            }
            if(playListener!=null&&playListener.updateListener!=null){
                playAnimator.addUpdateListener(playListener.updateListener);
            }
            if(playListener!=null&&playListener.pauseListener!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    playAnimator.addPauseListener(playListener.pauseListener);
                }else{
                    throw new RuntimeException("SDK最小要求19");
                }
            }
            mAnimatorList.clear();
            mAnimatorBuilder=mAnimatorSet.play(playAnimator);
            return this;
        }

        public AnimatorSetWrap play(Animator animator) {
            mAnimatorList.clear();
            mAnimatorBuilder = mAnimatorSet.play(animator);
            return this;
        }

        public AnimatorSetWrap play(AnimatorSetWrap animator) {
            mAnimatorList.clear();
            mAnimatorBuilder = mAnimatorSet.play(animator.getAnimatorSet());
            return this;
        }

        /**
         * AnimatorSet的Before方法
         * @param view 动画执行的主体View
         * @param animName 动画类型
         * @param interpolator 插值器
         * @param repeatCount 重复次数
         * @param duration 动画执行时长
         * @param values 动画执行数值
         * @return
         */
        public AnimatorSetWrap before(View view, String animName,@Nullable TimeInterpolator interpolator, @Size(min = 0,max=Integer.MAX_VALUE) int repeatCount,@Size(min = 0,max=Integer.MAX_VALUE)int duration, float... values){
            LogUtil.i("before");
            if(view==null){
                throw new RuntimeException("view 不能为空");
            }
            ObjectAnimator beforeAnimator = ObjectAnimator.ofFloat(view,
                    animName, values).setDuration(duration);
            beforeAnimator.setInterpolator(interpolator==null?mTimeInterpolator:interpolator);
            beforeAnimator.setRepeatCount(repeatCount<0?0:repeatCount);
            beforeAnimator.setDuration(duration<0?mDuration:duration);
            if (beforeListener!=null&&beforeListener.animatorListener != null) {
                beforeAnimator.addListener(beforeListener.animatorListener);
            }
            if(beforeListener!=null&&beforeListener.updateListener!=null){
                beforeAnimator.addUpdateListener(beforeListener.updateListener);
            }
            if(beforeListener!=null&&beforeListener.pauseListener!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    beforeAnimator.addPauseListener(beforeListener.pauseListener);
                }else{
                    throw new RuntimeException("SDK最小要求19");
                }
            }
            mAnimatorBuilder = mAnimatorBuilder.before(beforeAnimator);
            return this;
        }

        public AnimatorSetWrap before(Animator animator) {
            mAnimatorBuilder = mAnimatorBuilder.before(animator);
            return this;
        }

        public AnimatorSetWrap before(AnimatorSetWrap animator) {
            mAnimatorBuilder = mAnimatorBuilder.before(animator.getAnimatorSet());
            return this;
        }


        public AnimatorSetWrap with(View view, String animName,@Nullable TimeInterpolator interpolator,@Size(min = 0,max=Integer.MAX_VALUE) int repeatCount,@Size(min = 0,max=Integer.MAX_VALUE)int duration, float... values){
            LogUtil.i("with");
            if(view==null){
                throw new RuntimeException("view 不能为空");
            }
            ObjectAnimator withAnimator = ObjectAnimator.ofFloat(view,
                    animName, values).setDuration(duration);
            withAnimator.setInterpolator(interpolator==null?mTimeInterpolator:interpolator);
            withAnimator.setRepeatCount(repeatCount<0?0:repeatCount);
            withAnimator.setDuration(duration<0?mDuration:duration);
            if (withListener!=null&&withListener.animatorListener != null) {
                withAnimator.addListener(withListener.animatorListener);
            }
            if(withListener!=null&&withListener.updateListener!=null){
                withAnimator.addUpdateListener(withListener.updateListener);
            }
            if(withListener!=null&&withListener.pauseListener!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    withAnimator.addPauseListener(withListener.pauseListener);
                }else{
                    throw new RuntimeException("SDK最小要求19");
                }
            }
            mAnimatorBuilder = mAnimatorBuilder.with(withAnimator);
            return this;
        }

        public AnimatorSetWrap with(Animator animator) {
            mAnimatorBuilder = mAnimatorBuilder.with(animator);
            return this;
        }

        public AnimatorSetWrap with(AnimatorSetWrap animator) {
            mAnimatorBuilder = mAnimatorBuilder.with(animator.getAnimatorSet());
            return this;
        }



        public AnimatorSetWrap after(View view, String animName,@Nullable TimeInterpolator interpolator,@Size(min = 0,max=Integer.MAX_VALUE) int repeatCount,@Size(min = 0,max=Integer.MAX_VALUE) int duration, float... values){
            LogUtil.i("after");
            if(view==null){
                throw new RuntimeException("view 不能为空");
            }
            ObjectAnimator afterAnimator = ObjectAnimator.ofFloat(view,
                    animName, values).setDuration(duration);
            afterAnimator.setInterpolator(interpolator==null?mTimeInterpolator:interpolator);
            afterAnimator.setRepeatCount(repeatCount<0?0:repeatCount);
            afterAnimator.setDuration(duration<0?mDuration:duration);
            if (afterListener!=null&&afterListener.animatorListener != null) {
                afterAnimator.addListener(afterListener.animatorListener);
            }
            if(afterListener!=null&&afterListener.updateListener!=null){
                afterAnimator.addUpdateListener(afterListener.updateListener);
            }
            if(afterListener!=null&&afterListener.pauseListener!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    afterAnimator.addPauseListener(afterListener.pauseListener);
                }else{
                    throw new RuntimeException("SDK最小要求19");
                }
            }
            mAnimatorBuilder = mAnimatorBuilder.after(afterAnimator);
            return this;
        }

        public AnimatorSetWrap after(Animator animator) {
            mAnimatorBuilder = mAnimatorBuilder.after(animator);
            return this;
        }

        public AnimatorSetWrap after(AnimatorSetWrap animator) {
            mAnimatorBuilder = mAnimatorBuilder.after(animator.getAnimatorSet());
            return this;
        }


        public AnimatorSetWrap after(long delay) {
            mAnimatorBuilder.after(delay);
            return this;
        }

        /**
         * 直接执行动画，该动画操作主要用作执行AnimatorSet的组合动画
         * 如果mAnimatorList不为0 则执行逐一播放动画
         */
        public void playAnim() {
            if(mAnimatorList.size()>0){
                readyThen(true);
            }
            mAnimatorSet.start();
        }

        /**
         * 在一定时长内运行完该组合动画
         * 如果mAnimatorList不为0 则执行逐一播放动画
         * @param duration 动画时长
         */
        public void playAnim(long duration) {
            if(mAnimatorList.size()>0){
                readyThen(true);
            }
            mAnimatorSet.setDuration(duration);
            mAnimatorSet.start();
        }

        /**
         * 延迟一定时长播放动画
         * 如果mAnimatorList不为0 则执行逐一播放动画
         * @param delay 延迟时长
         */
        public void playAnimDelay(long delay) {
            if(mAnimatorList.size()>0){
                readyThen(true);
            }
            mAnimatorSet.setStartDelay(delay);
            mAnimatorSet.start();
        }

        /**
         * 直接执行动画，该动画操作主要用作执行AnimatorSet的组合动画
         */
        public void playAnim(boolean isSequentially) {
            readyThen(isSequentially);
            mAnimatorSet.start();
        }

        /**
         * 在一定时长内运行完该组合动画
         * @param duration 动画时长
         */
        public void playAnim(boolean isSequentially,long duration) {
            readyThen(isSequentially);
            mAnimatorSet.setDuration(duration);
            mAnimatorSet.start();
        }

        /**
         * 延迟一定时长播放动画
         * @param delay 延迟时长
         */
        public void playAnimDelay(boolean isSequentially,long delay) {
            readyThen(isSequentially);
            mAnimatorSet.setStartDelay(delay);
            mAnimatorSet.start();
        }

        /**
         * 顺序播放动画
         * @param isSequentially 是逐一播放还是同时播放
         */
        private void readyThen(boolean isSequentially){
            // 只在第一次启动时初始化
            if (mHasInitThenAnim) {
                return;
            }
            mHasInitThenAnim = true;
            if (mAnimatorList.size() > 0) {
                AnimatorSet set = new AnimatorSet();
                if(isSequentially){
                    set.playSequentially(mAnimatorList);
                }else{
                    set.playTogether(mAnimatorList);
                }
                mAnimatorBuilder.before(set);
            }
        }
        /**
         * 取消动画
         */
        public void cancel() {
            mAnimatorSet.cancel();
            mAnimatorList.clear();
        }

        /**
         * 获取AnimatorSet的实例
         * @return
         */
        private AnimatorSet getAnimatorSet() {
            return mAnimatorSet;
        }

        /**
         * 为AnimatorSet设置监听
         * @param listener
         * @return
         */
        public AnimatorSetWrap setAnimatorSetListener(Animator.AnimatorListener listener) {
            mAnimatorSet.addListener(listener);
            return this;
        }

        /**
         * 取消AnimatorSet的监听
         * @param listener
         */
        public void removeSetListner(Animator.AnimatorListener listener) {
            mAnimatorSet.removeListener(listener);
        }

        /**
         * 取消全部AnimatorSet的监听
         */
        public void removeAllLSetisteners() {
            mAnimatorSet.removeAllListeners();
        }

        /**
         * 判断一个View是否在当前的屏幕中可见（肉眼真实可见）
         * @param mView
         * @return 返回true则可见
         */
        public static boolean isVisibleOnScreen(View mView) {
            if (mView == null) {
                return false;
            }
            return mView.getWindowVisibility() == View.VISIBLE
                    && mView.getVisibility() == View.VISIBLE && mView.isShown();
        }
    }

    /**
     * Play方法对应的ObjectAnimator的监听实例
     */
    public static class PlayAnimationListener implements IAnimatorListener<PlayAnimationListener>{

        private Animator.AnimatorListener animatorListener;
        private ValueAnimator.AnimatorUpdateListener updateListener;
        private Animator.AnimatorPauseListener pauseListener;

        public AnimatorSetWrap animatorSetWrap;
        public PlayAnimationListener(AnimatorSetWrap animatorSetWrap){
            this.animatorSetWrap=animatorSetWrap;
        }
        @Override
        public PlayAnimationListener setAnimatorListener(Animator.AnimatorListener animatorListener) {
            this.animatorListener=animatorListener;
            return this;
        }

        @Override
        public PlayAnimationListener setUpdateListener(ValueAnimator.AnimatorUpdateListener animatorListener) {
            this.updateListener=animatorListener;
            return this;
        }

        @Override
        public PlayAnimationListener setPauseListener(Animator.AnimatorPauseListener animatorListener) {
            this.pauseListener=animatorListener;
            return this;
        }
        @Override
        public AnimatorSetWrap and(){
            return animatorSetWrap;
        }
    }


    public static class BeforeAnimationListener implements IAnimatorListener<BeforeAnimationListener>{

        private Animator.AnimatorListener animatorListener;
        private ValueAnimator.AnimatorUpdateListener updateListener;
        private Animator.AnimatorPauseListener pauseListener;

        public AnimatorSetWrap animatorSetWrap;
        public BeforeAnimationListener(AnimatorSetWrap animatorSetWrap){
            this.animatorSetWrap=animatorSetWrap;
        }
        @Override
        public AnimatorSetWrap and() {
            return animatorSetWrap;
        }

        @Override
        public BeforeAnimationListener setAnimatorListener(Animator.AnimatorListener listener) {
            this.animatorListener=listener;
            return this;
        }

        @Override
        public BeforeAnimationListener setUpdateListener(ValueAnimator.AnimatorUpdateListener listener) {
            this.updateListener=listener;
            return this;
        }

        @Override
        public BeforeAnimationListener setPauseListener(Animator.AnimatorPauseListener listener) {
            this.pauseListener=listener;
            return this;
        }
    }


    public static class WithAnimationListener implements IAnimatorListener<WithAnimationListener>{

        private Animator.AnimatorListener animatorListener;
        private ValueAnimator.AnimatorUpdateListener updateListener;
        private Animator.AnimatorPauseListener pauseListener;

        public AnimatorSetWrap animatorSetWrap;
        public WithAnimationListener(AnimatorSetWrap animatorSetWrap){
            this.animatorSetWrap=animatorSetWrap;
        }
        @Override
        public AnimatorSetWrap and() {
            return animatorSetWrap;
        }

        @Override
        public WithAnimationListener setAnimatorListener(Animator.AnimatorListener listener) {
            this.animatorListener=listener;
            return this;
        }

        @Override
        public WithAnimationListener setUpdateListener(ValueAnimator.AnimatorUpdateListener listener) {
            this.updateListener=listener;
            return this;
        }

        @Override
        public WithAnimationListener setPauseListener(Animator.AnimatorPauseListener listener) {
            this.pauseListener=listener;
            return this;
        }
    }

    public static class AfterAnimationListener implements IAnimatorListener<AfterAnimationListener>{

        private Animator.AnimatorListener animatorListener;
        private ValueAnimator.AnimatorUpdateListener updateListener;
        private Animator.AnimatorPauseListener pauseListener;

        public AnimatorSetWrap animatorSetWrap;
        public AfterAnimationListener(AnimatorSetWrap animatorSetWrap){
            this.animatorSetWrap=animatorSetWrap;
        }
        @Override
        public AnimatorSetWrap and() {
            return animatorSetWrap;
        }

        @Override
        public AfterAnimationListener setAnimatorListener(Animator.AnimatorListener listener) {
            this.animatorListener=listener;
            return this;
        }

        @Override
        public AfterAnimationListener setUpdateListener(ValueAnimator.AnimatorUpdateListener listener) {
            this.updateListener=listener;
            return this;
        }

        @Override
        public AfterAnimationListener setPauseListener(Animator.AnimatorPauseListener listener) {
            this.pauseListener=listener;
            return this;
        }
    }


    public static class ThenAnimationListener implements IAnimatorListener<ThenAnimationListener>{

        private Animator.AnimatorListener animatorListener;
        private ValueAnimator.AnimatorUpdateListener updateListener;
        private Animator.AnimatorPauseListener pauseListener;

        public AnimatorSetWrap animatorSetWrap;
        public ThenAnimationListener(AnimatorSetWrap animatorSetWrap){
            this.animatorSetWrap=animatorSetWrap;
        }
        @Override
        public AnimatorSetWrap and() {
            return animatorSetWrap;
        }

        @Override
        public ThenAnimationListener setAnimatorListener(Animator.AnimatorListener listener) {
            this.animatorListener=listener;
            return this;
        }

        @Override
        public ThenAnimationListener setUpdateListener(ValueAnimator.AnimatorUpdateListener listener) {
            this.updateListener=listener;
            return this;
        }

        @Override
        public ThenAnimationListener setPauseListener(Animator.AnimatorPauseListener listener) {
            this.pauseListener=listener;
            return this;
        }
    }

    /**
     * 动画监听的公用模板接口
     * @param <T>
     */
    interface IAnimatorListener<T>{
        /**
         * 设置AnimatorListener的方法
         * @param listener
         * @return
         */
        T setAnimatorListener(Animator.AnimatorListener listener);

        /**
         * 设置AnimatorUpdateListener的方法
         * @param listener
         * @return
         */
        T setUpdateListener(ValueAnimator.AnimatorUpdateListener listener);

        /**
         * 设置AnimatorPauseListener的方法
         * @param listener
         * @return
         */
        T setPauseListener(Animator.AnimatorPauseListener listener);

        /**
         * 桥接动画监听与动画工具类的方法
         * @return
         */
        AnimatorSetWrap and();
    }

}