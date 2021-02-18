package com.hs.advertise.utils;

import com.lunzn.tool.log.LogUtil;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.utils
 * ProjectName: trunk
 * Date: 2020/4/22 10:09
 */
public class FileFileter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        LogUtil.e("FileFileter","name:"+name);
        return name.endsWith(".mp4");
    }
}