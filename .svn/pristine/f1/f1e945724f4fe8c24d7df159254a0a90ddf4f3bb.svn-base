package com.hs.advertise.utils;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.utils
 * ProjectName: trunk
 * Date: 2020/3/26 16:19
 */
public class ActivityUtil {

    private static final String TAG = "ActivityUtil";
    /**
     * 单一实例
     */
    private static ActivityUtil sActivityUtil;
    /**
     * Activity堆栈 Stack:线程安全
     */
    public Stack<WeakReference<Activity>> mActivityStack = new Stack<>();

    /**
     * 私有构造器 无法外部创建
     */
    private ActivityUtil() {
    }

    /**
     * 获取单一实例 双重锁定
     *
     * @return this
     */
    public static ActivityUtil getInstance() {
        if (sActivityUtil == null) {
            synchronized (ActivityUtil.class) {
                if (sActivityUtil == null) {
                    sActivityUtil = new ActivityUtil();
                }
            }
        }
        return sActivityUtil;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        mActivityStack.add(new WeakReference<>(activity));
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {

        for (int i = mActivityStack.size() - 1; i >= 0; i--) {

            if (mActivityStack.get(i) != null) {
                if (mActivityStack.get(i).get() != null) {
                    mActivityStack.get(i).get().finish();
                }
            }
        }
        mActivityStack.clear();
    }

    /*    *//**
     * 结束指定的Activity
     *
     * @param activity Activity
     *//*
    public void finishActivity(WeakReference<Activity> activity) {
        if (activity != null && mActivityStack.contains(activity)) {
            mActivityStack.remove(activity);
            LogUtil.e(TAG, "finish " + activity.getClass().getName());
            activity.finish();
        }
    }

    *//**
     * 结束指定类名的Activity
     *
     * @param clazz Activity.class
     *//*
    public void finishActivity(Class<?> clazz) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(clazz)) {
                finishActivity(activity);
                break;
            }
        }
    }

    *//**
     * 获取指定类名的Activity
     *//*
    public Activity getActivity(Class<?> cls) {
        if (mActivityStack != null) {
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    *//**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     *//*
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }*/
}

