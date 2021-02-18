package com.hs.advertise.mvp.model.bean;

import java.io.Serializable;

/**
 * 广告资源
 * <功能详细描述>
 *
 * @author lunzn-gxr
 * @version [版本号, 2020年3月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AdResource implements Serializable {

    /**
     * 自增id
     */
    private long id;

    /**
     * 广告资源id
     */
    private String resourceId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源地址
     */
    private String url;

    /**
     * 资源描述
     */
    private String desc;

    /**
     * CRC Code
     */
    private long crcCode;

    /**
     * 打开动作
     */
    private int action;

    /**
     * 打开动作参数
     */
    private String clickParam;

    /**
     * 顺序
     */
    private int index;

    /**
     * 广告位置
     * 1:单元格
     * 2：应用启动
     * 3：全屏
     */
    private int adPosition;

    private long duration;


    public ClickAction getClickAction() {

        return clickAction;
    }

    public void setClickAction(ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    /**
     * 点击对象
     */
    private ClickAction clickAction;



    public int getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(int adPosition) {
        this.adPosition = adPosition;
    }



    public void setDuration(long duration) {
        this.duration = duration;
    }




    public long getDuration() {
        return duration;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(long crcCode) {
        this.crcCode = crcCode;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getClickParam() {
        return clickParam;
    }

    public void setClickParam(String clickParam) {
        this.clickParam = clickParam;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "AdResource{" +
                "id=" + id +
                ", resourceId='" + resourceId + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", desc='" + desc + '\'' +
                ", crcCode=" + crcCode +
                ", action=" + action +
                ", clickParam='" + clickParam + '\'' +
                ", index=" + index +
                ", adPosition=" + adPosition +
                ", duration=" + duration +
                '}';
    }
}
