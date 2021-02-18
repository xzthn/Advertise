package com.danikula.videocache.file;

import android.text.TextUtils;

import com.danikula.videocache.ProxyCacheUtils;
import com.lunzn.tool.log.LogUtil;

/**
 * Implementation of {@link FileNameGenerator} that uses MD5 of url as file name
 *
 *
 */
public class Md5FileNameGenerator implements FileNameGenerator {

    private static final int MAX_EXTENSION_LENGTH = 4;

    @Override
    public String generate(String url) {
        String extension = getExtension(url);
        String name = ProxyCacheUtils.computeMD5(url);
        LogUtil.i("test","url:"+url);
        LogUtil.i("test","name:"+name);
        return TextUtils.isEmpty(extension) ? name : name + "." + extension;
    }

    private String getExtension(String url) {
        int dotIndex = url.lastIndexOf('.');
        int slashIndex = url.lastIndexOf('/');
        return dotIndex != -1 && dotIndex > slashIndex && dotIndex + 2 + MAX_EXTENSION_LENGTH > url.length() ?
                url.substring(dotIndex + 1, url.length()) : "";
    }
}
