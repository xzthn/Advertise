package com.hs.advertise.mvp.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 广告信息
 * <功能详细描述>
 *
 * @author lunzn-gxr
 * @version [版本号, 2020年3月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 *
 *
 * 	"fullScreenAds": {
 * 				"id": 1,
 * 				"type": 3,
 * 				"skipTime": 0,
 * 				"canQuit": 1,
 * 				"interval": 0,
 * 				"totalDuration": 125000,
 * 				"adResources": [
 * 					{
 * 						"id": 1,
 * 						"resourceId": "2020072112182915167766",
 * 						"name": "中懋宣传视频",
 * 						"url": "http://download.tviyun.com/rom-ad-operation/videos/bak_video_20200721120352911.mp4",
 * 						"desc": null,
 * 						"crcCode": 2105241667,
 * 						"clickAction": null,
 * 						"duration": 125400,
 * 						"index": 1
 * 					}
 * 				]
 * 			}
 */
public class AdInfo implements Serializable {
    /**
     * 自增id
     */
    private long id;

    /**
     * 1、  图片
     * 2、  轮播图片
     * 3、  视频
     * 4、  轮播视频
     */
    private int type;

    /**
     * 跳过时间  0为不跳过
     */
    private long skipTime;

    /**
     * 0:不能
     * 1：能
     */
    private int canQuit;

    /**
     * 类型1、2时必填 单位ms
     */
    private long interval;

    /**
     * 广告资源列表
     */
    private List<AdResource> adResources;

    /**
     * 广告资源视频时长
     */
    private long totalDuration;


    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }


    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    private String pkg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSkipTime() {
        return skipTime;
    }

    public void setSkipTime(long skipTime) {
        this.skipTime = skipTime;
    }

    public int getCanQuit() {
        return canQuit;
    }

    public void setCanQuit(int canQuit) {
        this.canQuit = canQuit;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public List<AdResource> getAdResources() {
        return adResources;
    }

    public void setAdResources(List<AdResource> adResources) {
        this.adResources = adResources;
    }

    @Override
    public String toString() {
        return "AdInfo{" +
                "id=" + id +
                ", type=" + type +
                ", skipTime=" + skipTime +
                ", canQuit=" + canQuit +
                ", interval=" + interval +
                ", adResources=" + adResources +
                ", totalDuration=" + totalDuration +
                ", pkg='" + pkg + '\'' +
                '}';
    }
}
