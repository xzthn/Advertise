package com.hs.advertise.ui.activity.test;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.hs.advertise.R;
import com.hs.advertise.mvp.model.iterfaces.UserInfoBiz;
import com.hs.advertise.mvp.present.UserInfoPresent;
import com.hs.advertise.utils.ActivityUtil;
import com.lunzn.tool.log.LogUtil;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.ui
 * ProjectName: Advertise
 * Date: 2020/3/10 15:14
 */
public class TestActivity extends Activity implements UserInfoBiz {

    private static final String TAG = TestActivity.class.getSimpleName();
    private UserInfoPresent mUserInfoPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ActivityUtil.getInstance().addActivity(this);
        mUserInfoPresent = new UserInfoPresent(this, this);
        PackageManager packageManager = getPackageManager();
        LogUtil.i(TAG,"禁止了--------------");
//        packageManager.setApplicationEnabledSetting("tv.yuyin", PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);
//        RxjavaUtils.countdown(20, new RxjavaUtils.IRxNext() {
//            @Override
//            public void doNext(long number) {
//                LogUtil.i(TAG, "number："+number);
//            }
//
//            @Override
//            public void onComplete() {
//                LogUtil.i(TAG, "onComplete");
//                finish();
//            }
//        });

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(TAG,"==================");
                PackageManager packageManager = getPackageManager();
                LogUtil.i(TAG,"禁止了--------------");
                packageManager.setApplicationEnabledSetting("tv.yuyin", PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);

            }
        });

        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                LogUtil.i(TAG,"开启了此广播");
                packageManager.setApplicationEnabledSetting("tv.yuyin", PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                packageManager.setApplicationEnabledSetting("com.iflytek.xiri2.system", PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG, "onResume");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG,"开启了--------------");
        PackageManager packageManager = getPackageManager();
        packageManager.setApplicationEnabledSetting("tv.yuyin", PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onSuccess(String url, Object object) {
        LogUtil.i(TAG, "response:" + JSONObject.toJSONString(object));
    }

    @Override
    public void onError(Throwable e) {

    }

}
