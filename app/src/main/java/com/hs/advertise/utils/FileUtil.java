package com.hs.advertise.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hs.advertise.model.MessageModel;
import com.lunzn.tool.log.LogUtil;
import com.lunzn.tool.util.CommonUtil;
import com.smart.localfile.LocalFileCRUDUtils;
import com.smart.util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.utils
 * ProjectName: Advertise
 * Date: 2020/3/11 16:58
 */
public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public static void main(String[] args) {


    }


    /**
     * 修改临时文件的当前时长duration
     *
     * @param duration
     * @param tempPath
     */
    public static void modifyTemp(long duration, String tempPath) {
        String tempStr = LocalFileCRUDUtils.readFile(tempPath); // 读取临时文件
        JSONObject tempJb = JSONObject.parseObject(tempStr);
        if (tempJb != null) {
            tempJb.put("duration", duration);//替换当前时长字段即可
            writeJsonFileOnlyLine(tempJb.toJSONString(), tempPath);
        }
    }


    /**
     * 复制临时文件Temp的内容到total文件中
     *
     * @param duration
     * @param tempPath
     */
    public static boolean copyToTotal(long duration, String tempPath) {
        // 读取临时文件,应该一条数据的,两条测试有问题
        String temp = LocalFileCRUDUtils.readFile(tempPath);
        if (temp == null) {
            return false;
        }
        try {
            LogUtil.i(TAG, "从tempPath:" + tempPath + "中读取内容，" + temp);
            LogUtil.i(TAG, "duration:" + duration);
            JSONObject tempJb = JSONObject.parseObject(temp);
            //替换当前时长字段即可
            tempJb.put("duration", duration);
            return writeJsonFile(tempJb.toJSONString(), MessageModel.JSON_LOCAL_PATH);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.i(TAG, "文件异常e:" + e.getMessage());
        }

        return false;
    }


    /**
     * 复制临时文件Temp的内容到total文件中
     *
     * @param tempPath
     */
    public static boolean copyDirectToTotal(String tempPath) {
        String temp = LocalFileCRUDUtils.readFile(tempPath);
        if (temp == null) {
            return false;
        }
        try {
            LogUtil.i(TAG, "从tempPath:" + tempPath + "中读取内容," + temp);
            JSONObject tempJb = JSONObject.parseObject(temp);
            if (CommonUtil.isEmpty(tempJb)) {
                return false;
            }
            return writeJsonFile(tempJb.toJSONString(), MessageModel.JSON_LOCAL_PATH);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.i(TAG, "文件异常e:" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i(TAG, "文件异常e:" + e.getMessage());
        }

        return false;
    }

    /**
     * 将JSON数据格式化并保存到文件中
     *
     * @param jsonData 需要输出的json数
     * @param filePath
     * @return
     */
    public static boolean writeJsonFile(String jsonData, String filePath) {
        // 标记文件生成是否成功
        boolean flag = true;
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(filePath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdir();
            }
            if (file.isDirectory()) {
                file.delete();
            }
            if (!file.exists()) {// 如果文件不存在，创建文件
                file.createNewFile();
            }
            // 将格式化后的字符串写入文件
            FileWriter write = new FileWriter(file, true);
            //注意按行写入
            write.write(jsonData + "\n");
            LogUtil.i(TAG, filePath + "日志文件中写入了:" + jsonData);
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 将JSON数据格式化并保存到文件中
     *
     * @param jsonData 需要输出的json数
     * @param filePath
     * @return
     */
    public static boolean writeJsonFileOnlyLine(String jsonData, String filePath) {
        // 标记文件生成是否成功
        boolean flag = true;
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(filePath);
            LogUtil.d(TAG, "==##file " + file.getAbsolutePath());
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.isDirectory()) {
                file.delete();
            }
            if (!file.exists()) {// 如果文件不存在，创建文件
                file.createNewFile();
            }
            // 将格式化后的字符串写入文件
            FileWriter write = new FileWriter(file, false);
            write.write(jsonData);//注意按行写入
            LogUtil.i(TAG, "writeJsonFileOnlyLine--" + filePath + "临时日志文件中写入了:" + jsonData);
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 读取total本地文件
     *
     * @param localFilePath 文件绝对路径
     * @return 返回字符串，如果失败，返回null
     */
    public static JSONArray readFileToJson(String localFilePath) {
        BufferedReader br = null;
        JSONArray ja = new JSONArray();
        File file = new File(localFilePath);
        LogUtil.i(TAG, "readFile file " + file.getAbsolutePath() + ", exists " + file.exists());
        if (file.exists()) {
            try {
                br = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!CommonUtil.isEmpty(line)) {
                        ja.add(JSONObject.parseObject(line.trim()));
                    }
                }
//                LogUtil.i(TAG, "读取总的日志文件了" + ja.toString());
            } catch (Exception e) {
                LogUtil.i(TAG, "readFile e " + e);
                e.printStackTrace();
            } finally {
                Utils.close(br);
            }
        }
        return ja;
    }
}
