package com.hs.advertise.service.mqtt;

/**
 * 作者:HONGYI
 * 包名:com.lz.m02.launcher.mqtt
 * 工程名:CQ_launcher
 * 时间:2018/7/24 9:53
 * 说明:
 */
public class MessagePushObj {

    /**
     * 告警消息id
     */
    private long id;
    /**
     * 消息名称
     */
    private String name;
    /**
     * 告警消息类型  1 全屏告警 2右下角弹框告警  3 跑马灯告警
     */
    private Integer showType;
    /**
     * 告警发送时间
     */
    private String sendTime;
    /**
     * 告警发送类型  0 预约发送  1 立即发送
     */
    private Integer sendType;
    /**
     * 告警消息内容 跑马灯内容
     */
    private String content;
    /**
     * 告警消息内容Url  全屏和右下角弹框内容
     */
    private String contentUrl;
    /**
     * 发送状态   0 就绪 1 发送成功 2 取消发送 3 发送失败
     */
    private Integer state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MessagePushObj{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", showType=" + showType +
                ", sendTime='" + sendTime + '\'' +
                ", sendType=" + sendType +
                ", content='" + content + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", state=" + state +
                '}';
    }
}
