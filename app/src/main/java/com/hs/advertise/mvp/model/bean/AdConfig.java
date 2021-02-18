package com.hs.advertise.mvp.model.bean;

import java.util.List;
import java.util.Map;

/**
 * 广告配置
 * <功能详细描述>
 *
 * @author lunzn-gxr
 * @version [版本号, 2020年3月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AdConfig {

    private DataBean data;
    private int retCode;

    @Override
    public String toString() {
        return "AdConfig{" +
                "data=" + data +
                ", retCode=" + retCode +
                ", retMsg='" + retMsg + '\'' +
                '}';
    }

    private String retMsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public static class DataBean {

        /**
         * app启动广告配置
         */
        private Map<String, AdInfo> appLaunchAds;

        /**
         * 默认APP启动广告
         */
        private AdInfo defaultAppLaunchAds;

        /**
         * 无启动广告应用包名
         */
        private List<String> noLaunchAdAppList;

        /**
         * 全屏广告配置
         */
        private AdInfo fullScreenAds;

        /**
         * 数据版本号
         */
        private long dataVersion;


        public Map<String, AdInfo> getAppLaunchAds() {
            return appLaunchAds;
        }

        public void setAppLaunchAds(Map<String, AdInfo> appLaunchAds) {
            this.appLaunchAds = appLaunchAds;
        }

        public AdInfo getDefaultAppLaunchAds() {
            return defaultAppLaunchAds;
        }

        public void setDefaultAppLaunchAds(AdInfo defaultAppLaunchAds) {
            this.defaultAppLaunchAds = defaultAppLaunchAds;
        }

        public List<String> getNoLaunchAdAppList() {
            return noLaunchAdAppList;
        }

        public void setNoLaunchAdAppList(List<String> noLaunchAdAppList) {
            this.noLaunchAdAppList = noLaunchAdAppList;
        }

        public AdInfo getFullScreenAds() {
            return fullScreenAds;
        }

        public void setFullScreenAds(AdInfo fullScreenAds) {
            this.fullScreenAds = fullScreenAds;
        }

        public long getDataVersion() {
            return dataVersion;
        }

        public void setDataVersion(long dataVersion) {
            this.dataVersion = dataVersion;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "appLaunchAds=" + appLaunchAds +
                    ", defaultAppLaunchAds=" + defaultAppLaunchAds +
                    ", noLaunchAdAppList=" + noLaunchAdAppList +
                    ", fullScreenAds=" + fullScreenAds +
                    ", dataVersion=" + dataVersion +
                    '}';
        }
    }


}
