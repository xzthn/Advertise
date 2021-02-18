package com.hs.advertise.mvp.model.bean;

import java.io.Serializable;

/**
 * 
 * 点击动作
 * <功能详细描述>
 * 
 * @author  lunzn-wzc
 * @version  [版本号, 2020年3月26日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ClickAction implements Serializable
{
    /**
     * 点击该广告时，要跳转app的包名
     */
    private String pkgName;
    
    /**
     * 点击该广告时，要跳转app的指定类名，或者指定动作
     */
    private String action;
    
    /**
     * 点击该广告时，要跳转app时，需要携带的参数
     */
    private String param;
    
    /**
     * 额外参数(action对应的param)
     */
    private String extraParam;
    
    /**
     * 当是推荐应用时，该值表示所推荐的应用包名
     */
    private String appPkgName;
    
    /**
     * 当是推荐应用时，该推荐应用的标识字段 目前有4个标识： bit0表示是否可删除 bit1表示是否开机下载 bit2表示下载完成后是否安装
     * bit3表示安装后是否打开
     */
    private int appsign = 0;
    
    public String getPkgName()
    {
        return pkgName;
    }
    
    public void setPkgName(String pkgName)
    {
        this.pkgName = pkgName;
    }
    
    public String getAction()
    {
        return action;
    }
    
    public void setAction(String action)
    {
        this.action = action;
    }
    
    public String getParam()
    {
        return param;
    }
    
    public void setParam(String param)
    {
        this.param = param;
    }
    
    public String getExtraParam()
    {
        return extraParam;
    }
    
    public void setExtraParam(String extraParam)
    {
        this.extraParam = extraParam;
    }
    
    public String getAppPkgName()
    {
        return appPkgName;
    }
    
    public void setAppPkgName(String appPkgName)
    {
        this.appPkgName = appPkgName;
    }
    
    public int getAppsign()
    {
        return appsign;
    }
    
    public void setAppsign(int appsign)
    {
        this.appsign = appsign;
    }
    
}
