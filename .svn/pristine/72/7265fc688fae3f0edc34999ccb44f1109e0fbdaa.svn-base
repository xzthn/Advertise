package com.hs.advertise.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.hs.advertise.R;
import com.hs.advertise.business.PlayLogicUtil;
import com.hs.advertise.config.IntentValue;
import com.hs.advertise.config.TypeParam;
import com.hs.advertise.mvp.model.bean.AdInfo;
import com.hs.advertise.service.InterfaceService;
import com.lunzn.tool.log.LogUtil;


/**
 * Desc: 点击应用启动页面
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.ui.activity
 * ProjectName: Advertise
 * Date: 2020/3/11 10:40
 */
public class SplashActivity extends Activity {

    private static final String TAG = "Tag_" + SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LogUtil.e(TAG, this.getClass().getSimpleName() + ",onCreate");
        goHome();
    }


    private void goHome() {
        AdInfo mFullScreenAds = PlayLogicUtil.getFullScreenAds();
        if (mFullScreenAds != null) {
            LogUtil.i(TAG, "type:" + mFullScreenAds.getType());
            //服务器type字段区分启动那个activity
            if (mFullScreenAds.getType() == TypeParam.TYPE_SINGLE_PHOTO || mFullScreenAds.getType() == TypeParam.TYPE_MULTIPLE_PHOTO) {
                LogUtil.i(TAG, "SplashActivity photo");
                PlayLogicUtil.startActivity(getApplicationContext(), PhotoActivity.class, mFullScreenAds, IntentValue.AD_POSITION_BOOT);
            } else {
                int frontCount = mFullScreenAds.getAdResources().size();
                if (InterfaceService.hasCachVideo(this, mFullScreenAds)) {
                    PlayLogicUtil.playDefault(this);
                } else {
                    LogUtil.i(TAG, "SplashActivity video");
                    PlayLogicUtil.startActivity(getApplicationContext(), VideoActivity.class, mFullScreenAds, IntentValue.AD_POSITION_BOOT);
                }

            }
        } else {
            LogUtil.i(TAG, "SplashActivity defoult");
            PlayLogicUtil.playDefault(this);
        }
        LogUtil.i(TAG, "SplashActivity finish end");
        SplashActivity.this.finish();
    }
}
