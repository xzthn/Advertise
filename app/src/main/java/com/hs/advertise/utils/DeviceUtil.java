package com.hs.advertise.utils;

import com.lunzn.systool.util.SystemUtil;
import com.lunzn.tool.log.DebugUtil;
import com.smart.data.DataFormat;
import com.smart.localfile.LocalFileCRUDUtils;

/**
 * Desc: TODO
 * <p>
 * Author: xiezhitao
 * PackageName: com.lzui.apkupgrade.util
 * ProjectName: APKCheck_haisi
 * Date: 2020/3/5 11:40
 */
public class DeviceUtil {

    /**
     * 获取MAC地址
     *
     * @return String mac地址
     */
    public static String getMacAddress() {
        String result = null;

        // 读取默认地址下的mac地址，读完之后将其写入共享区域内
        result = LocalFileCRUDUtils.readFileNoNull("/sys/class/net/eth0/address");
        DebugUtil.i("getMacAddress mac " + result);
        return result;
    }

    /**
     * 获取无线MAC地址
     */
    public static String getWifiMac() {
        return LocalFileCRUDUtils.readFile("/sys/class/net/wlan0/address");
    }

    /**
     * 获取Rom固件版本号 ,南传规则：客户-版型-机型-软件版本
     *
     * @return 固件版本号 LZ-R1-M01-V01001，岚正规则： 品牌（对应南传客户）-芯片（版型）-型号（机型）-版本（软件版本）
     */
    public static String getSoftwareVersion() {
        String softVersion = null;
        try {
            softVersion = SystemUtil.getSystemProperties("ro.build.display.id", "unKnow");

        } catch (Exception e) {
            e.printStackTrace();
        }
        DebugUtil.i("getSoftwareVersion:  " + softVersion);
        return softVersion;
    }

    /**
     * 获取盒子的sn，如果本地没有则从服务器获取
     *
     * @return
     */
    public static String getSN() {

        String sn = null;
        String mac = getMacAddress();
        String wifiMac = getWifiMac();
        if (!DataFormat.isEmpty(mac) && !DataFormat.isEmpty(wifiMac)) {

            sn = mac.concat(wifiMac).replace(":", "");
        }
        DebugUtil.i("getsn mem sn:  " + sn);
        return sn;
    }

    /**
     * 获取盒子的sn，如果本地没有则从服务器获取
     *
     * @return
     */
    public static String getADSN() {

        String sn = "";
        String mac = getMacAddress();
        if (!DataFormat.isEmpty(mac)) {

            sn = mac.replace(":", "");
        }
        DebugUtil.i("getADSN:  " + sn);
        return sn;
    }
}
