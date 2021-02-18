package com.hs.advertise.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.hs.advertise.R;
import com.hs.advertise.business.PlayLogicUtil;
import com.hs.advertise.config.IntentValue;
import com.hs.advertise.config.TypeParam;
import com.hs.advertise.model.MessageModel;
import com.hs.advertise.model.PlayLogJson;
import com.hs.advertise.mvp.model.bean.AdInfo;
import com.hs.advertise.mvp.model.bean.AdResource;
import com.hs.advertise.mvp.model.bean.ClickAction;
import com.hs.advertise.receiver.BroadcastAction;
import com.hs.advertise.thread.ThreadManager;
import com.hs.advertise.ui.iterfaces.IViewUpdate;
import com.hs.advertise.utils.FileUtil;
import com.hs.advertise.utils.RxjavaUtils;
import com.hs.advertise.view.NetworkImageHolderView;
import com.lunzn.tool.log.LogUtil;

import java.util.List;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.ui.PhotoActivity
 * ProjectName: Advertise
 * Date: 2020/3/11 9:58
 */
public class PhotoActivity extends BaseActivity implements IViewUpdate {

    private static final String TAG = "Tag_" + PhotoActivity.class.getSimpleName();
    private ConvenientBanner convenientBanner;
    private ImageView ivDefault;
    private MyBroadcastReceiver receiver;
    private OnPageChangeListener mOnPageChangeListener;


    @Override
    protected int getLayoutresID() {
        return R.layout.photo_fragment;
    }

    @Override
    protected void initView() {
        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        ivDefault = (ImageView) findViewById(R.id.iv_default);
    }


    @Override
    public void initData(Intent intent) {
        super.initData(intent);
        initViewPager();
    }

    @Override
    protected void initListener() {
        LogUtil.i(TAG, "initListener");
        mOnPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.i(TAG, "onPageSelected:" + position + " mCurrentPlayPosition:" + mCurrentPlayPosition);
                if (mCurrentPlayPosition != position || mCurrentPlayPosition == 0) {

                    mCurrentPlayPosition = position;
                    showClickActionView();
                    writeTotal(position);
                }
            }
        };

        convenientBanner.setOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    protected void initReceiver() {
        // 初始化广播过滤，监听服务器数据更新
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.ACTION_UPDATE);
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }

    private void initViewPager() {
        if (mAdInfo != null) {
            if (convenientBanner.pageAdapter == null) {

                convenientBanner.setPages(new CBViewHolderCreator() {
                    @Override
                    public NetworkImageHolderView createHolder(View itemView) {
                        return new NetworkImageHolderView(itemView, PhotoActivity.this);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_banner;
                    }
                }, mAdInfo.getAdResources());
            } else {
                convenientBanner.pageAdapter.setDatas(mAdInfo.getAdResources());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG, "onResume mCurrenTime：" + mCurrenSkipTime);
        LogUtil.i(TAG, "onResume mCurrenTotalTime：" + mCurrenTotalTime);
        photoPlay();
    }


    private void photoPlay() {
        if (mAdInfo == null || mAdInfo.getAdResources() == null || mAdInfo.getAdResources().size() == 0) {
            ivDefault.setVisibility(View.VISIBLE);
            convenientBanner.setVisibility(View.GONE);
            return;
        } else {
            ivDefault.setVisibility(View.GONE);
            convenientBanner.setVisibility(View.VISIBLE);
        }
        showClickActionView();
        writeTotal(mCurrentPlayPosition);

        convenientBanner.setFirstItemPos(mCurrentPlayPosition);
        convenientBanner.setCanLoop(mAdPosition == IntentValue.AD_POSITION_BOOT);
        convenientBanner.startTurning(mAdInfo.getInterval());

        if (canQuit == 0) {
            //如果不能跳过则隐藏此按钮
            if (convenientBanner.getTvSkipView() != null) {
                convenientBanner.getTvSkipView().setVisibility(View.GONE);
            }
        } else if (canQuit == 1) {
            if (convenientBanner.getTvSkipView() != null) {
                convenientBanner.getTvSkipView().setVisibility(View.VISIBLE);
            }
            starSkipTime();
        }
        if (mAdPosition == IntentValue.AD_POSITION_APP) {
            if (convenientBanner.getTvTimeView() != null) {
                convenientBanner.getTvTimeView().setVisibility(View.VISIBLE);
            }
            starTotalTime();
        }

    }

    private void showClickActionView() {
        ClickAction mClickAction = mAdInfo.getAdResources().get(mCurrentPlayPosition).getClickAction();
        if (mClickAction == null) {
            LogUtil.e(TAG, "动作为空");
            convenientBanner.getTvDetailView().setVisibility(View.GONE);
        } else {
            convenientBanner.getTvDetailView().setVisibility(View.VISIBLE);
            LogUtil.e(TAG, "动作不为空");
        }
    }


    /**
     * 开始播放的同时把日志写入total文件中
     */
    private void writeTotal(int position) {
        LogUtil.i(TAG, "photo writeTotal");
        //已经在外层判断了
        if (mAdInfo != null) {
            List<AdResource> mAdResources = mAdInfo.getAdResources();
            AdResource adResource = mAdResources.get(position);
            final PlayLogJson logJson = new PlayLogJson();
            logJson.setAdPosition(mAdPosition);
            logJson.setDuration(mAdInfo.getInterval());
            logJson.setResourceId(adResource.getResourceId());
            ThreadManager.getShortPool().execute(new Runnable() {
                @Override
                public void run() {
                    //  LogUtil.i(TAG, "photo logJson:" + logJson.getJson());
                    FileUtil.writeJsonFile(logJson.getJson(), MessageModel.JSON_LOCAL_PATH);
                }
            });
        }
    }

    private void starSkipTime() {
        //跳过是1，总的是2
        starSkipTime(this, 1);
    }

    private void starTotalTime() {
        starTotalTime(this, 2);
    }

    /**
     * @param number
     * @param type
     */
    @Override
    public void doProgress(long number, int type) {
        if (type == 1) {
            if (convenientBanner.getTvSkipView() != null) {
                convenientBanner.getTvSkipView().setText(number + "秒后 按【返回键】可跳过此广告");
            }
        } else if (type == 2) {
            if (convenientBanner.getTvDetailView() != null) {
                convenientBanner.getTvDetailView().setText("按【OK键】查看详情");
            }
            if (convenientBanner.getTvTimeView() != null) {
                convenientBanner.getTvTimeView().setText(number + "秒");
            }
        }
    }

    @Override
    public void onComplete(int type) {
        if (type == 1) {
            if (convenientBanner.getTvSkipView() != null) {
                convenientBanner.getTvSkipView().setText("按【返回键】可跳过此广告");
            }
        } else if (type == 2) {
            if (convenientBanner.getTvDetailView() != null) {
                convenientBanner.getTvDetailView().setText("按【OK键】查看详情");
            }
            if (convenientBanner.getTvTimeView() != null) {
                convenientBanner.getTvTimeView().setVisibility(View.GONE);
            }
        }
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastAction.ACTION_UPDATE)) {
                LogUtil.i(TAG, "资源更新完毕，你只要加载播放图片即可 mAdPosition " + mAdPosition);
                if (mAdPosition == IntentValue.AD_POSITION_BOOT) {
                    AdInfo mFullScreenAds = PlayLogicUtil.getFullScreenAds();
                    if (mFullScreenAds != null) {
                        mAdInfo = mFullScreenAds;
                        if (mFullScreenAds.getType() == TypeParam.TYPE_SINGLE_PHOTO || mFullScreenAds.getType() == TypeParam.TYPE_MULTIPLE_PHOTO) {

                            if (mAdInfo.getAdResources().size() > 0) {
                                initCanQuit(true);
                                initViewPager();
                                photoPlay();

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
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(TAG, "photo onPause()");
        convenientBanner.stopTurning();
        RxjavaUtils.cancel();//取消定时器
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i(TAG, "photo onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "onDestroy()");
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener = null;
        }
    }

}
