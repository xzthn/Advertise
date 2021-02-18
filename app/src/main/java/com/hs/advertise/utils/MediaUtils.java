package com.hs.advertise.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.lunzn.tool.log.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.utils
 * ProjectName: trunk
 * Date: 2020/4/7 17:37
 */

public class MediaUtils
{
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static File file;

    /**
     * Create a file Uri for saving an image or video
     */
    public static Uri getOutputMediaFileUri(Context context, int type)
    {
        Uri uri = null;
        //适配Android N
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));
        } else
        {
            return Uri.fromFile(getOutputMediaFile(type));
        }
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "image");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else
        {
            return null;
        }
        file = mediaFile;
        return mediaFile;
    }


    /**
     * 获取视频的第一帧图片
     */
    public static Bitmap getImageBitmap(String videoPath)
    {
        LogUtil.i("=====================");
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if (videoPath.startsWith("http")){
            //获取网络视频第一帧图片
            mmr.setDataSource(videoPath, new HashMap());
        } else{
            //本地视频
            mmr.setDataSource(videoPath,new HashMap());
        }
        Bitmap bitmap = mmr.getFrameAtTime();
        mmr.release();
        LogUtil.i("--------------------");
        return bitmap;
    }

    /**
     * 获取视频的第一帧图片
     */
    public static void getImageForVideo(String videoPath, OnLoadVideoImageListener listener)
    {
        LoadVideoImageTask task = new LoadVideoImageTask(listener);
        task.execute(videoPath);
    }

    public static class LoadVideoImageTask extends AsyncTask<String, Integer, Bitmap>
    {
        private OnLoadVideoImageListener listener;

        public LoadVideoImageTask(OnLoadVideoImageListener listener)
        {
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            String path = params[0];
            if (path.startsWith("http")){
                //获取网络视频第一帧图片
                mmr.setDataSource(path, new HashMap());
            } else{
                //本地视频
                mmr.setDataSource(path,new HashMap());
            }

            Bitmap bitmap = mmr.getFrameAtTime();
            mmr.release();
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);
            if (listener != null)
            {
                listener.onLoadImage(bitmap);
            }
        }
    }

    public interface OnLoadVideoImageListener
    {
        void onLoadImage(Bitmap bitmap);
    }
}
