package com.hs.advertise.view;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hs.advertise.R;
import com.hs.advertise.mvp.model.bean.AdResource;
import com.lunzn.tool.log.LogUtil;

/**
 * Desc:
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.view
 * ProjectName: Advertise
 * Date: 2020/3/11 10:00
 */
public class NetworkImageHolderView extends Holder<AdResource> {

    private static final String TAG = "Tag_" + NetworkImageHolderView.class.getSimpleName();

    private Activity activity;
    private ImageView imageView;

    public NetworkImageHolderView(View itemView, Activity activity) {
        super(itemView);
        this.activity = activity;
    }


    @Override
    protected void initView(View itemView) {
        imageView = (ImageView) itemView.findViewById(R.id.iv_banner);
    }

    /**
     * @param data
     */
    @Override
    public void updateUI(AdResource data) {
        if (data == null || activity == null || imageView == null) {
            LogUtil.e(TAG, "data or activity or imageView,one of them is null ,do not load image");
            return;
        }

        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.default_icon)//图片加载出来前，显示的图片
                .fallback(R.drawable.default_icon) //url为空的时候,显示的图片
                .error(R.drawable.default_icon)//图片加载失败后，显示的图片;
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

        Glide.with(activity).load(data.getUrl()).apply(options).into(imageView);//从3.7版本的glide换为4.8的，直接应用启动第一次加载显示图片不变形了。
    }
}