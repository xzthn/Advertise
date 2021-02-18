package com.hs.advertise.model;

import com.alibaba.fastjson.JSONObject;
import com.hs.advertise.MyApplication;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.model
 * ProjectName: Advertise
 * Date: 2020/3/12 9:53
 */
public class PlayLogJson {

    /**
     * 设备sn
     */
    private String sn;
    /**
     * 记录时间戳
     */
    private long time;
    /**
     * 资源id
     */
    private String resourceId;
    /**
     * 广告位置
     */
    private int adPosition;
    /**
     * 当前时长
     */
    private long duration;


    public String getJson() {
        JSONObject jb = new JSONObject();
        jb.put("sn", MyApplication.getInstance().getSn());//SystemUtil.getSN(),新板子，该字段为空
        jb.put("time", System.currentTimeMillis());
        jb.put("resourceId", getResourceId());
        jb.put("adPosition", getAdPosition());
        jb.put("duration", getDuration());
        return jb.toString();
    }


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public int getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(int adPosition) {
        this.adPosition = adPosition;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


}
