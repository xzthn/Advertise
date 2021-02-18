package com.hs.advertise.config;

/**
 * 配置信息
 * 以键值对的方式，通过getSharedPreferences保存
 *
 * @author ganxinrong  单例模式
 */
public class CloudConfig extends SuperConfig {

    private static CloudConfig cloudConfig;

    /**
     * url
     */
    public static final String KEY_ADCONFIG = "key_ad_config";

    /**
     * DataVersion
     */
    public static final String KEY_DATA_VERSION = "key_dataVersion";


    @Override
    public String getConfig() {
        return "account_config_info";
    }

    private CloudConfig() {
    }

    public static CloudConfig getCloudConfig() {
        if (cloudConfig == null) {
            cloudConfig = new CloudConfig();
        }
        return cloudConfig;
    }

}
