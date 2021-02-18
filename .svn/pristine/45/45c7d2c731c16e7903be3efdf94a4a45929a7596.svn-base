package com.danikula.videocache.file;

import com.danikula.videocache.Cache;
import com.danikula.videocache.ProxyCacheException;
import com.lunzn.tool.log.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;

/**
 * {@link Cache} that uses file for storing data.
 */
public class FileCache implements Cache {

    private static final String TAG = "FileCache";
    private static final String TEMP_POSTFIX = ".download";

    private final DiskUsage diskUsage;
    public File file;
    private RandomAccessFile dataFile;

    public FileCache(File file) throws ProxyCacheException {
        this(file, new UnlimitedDiskUsage());
    }

    public FileCache(File file, DiskUsage diskUsage) throws ProxyCacheException {
        try {
            if (diskUsage == null) {
                throw new NullPointerException();
            }
            this.diskUsage = diskUsage;
            File directory = file.getParentFile();
            Files.makeDir(directory);
            boolean completed = file.exists();
            this.file = completed ? file : new File(file.getParentFile(), file.getName() + TEMP_POSTFIX);
            this.dataFile = new RandomAccessFile(this.file, completed ? "r" : "rw");
        } catch (IOException e) {
            throw new ProxyCacheException("Error using file " + file + " as disc cache", e);
        }
    }

    @Override
    public synchronized long available() throws ProxyCacheException {
        try {
            return (int) dataFile.length();
        } catch (IOException e) {
            throw new ProxyCacheException("Error reading length of file " + file, e);
        }
    }

    @Override
    public synchronized int read(byte[] buffer, long offset, int length) throws ProxyCacheException {
        try {
            dataFile.seek(offset);
            return dataFile.read(buffer, 0, length);
        } catch (IOException e) {
            String format = "Error reading %d bytes with offset %d from file[%d bytes] to buffer[%d bytes]";
            throw new ProxyCacheException(String.format(format, length, offset, available(), buffer.length), e);
        }
    }

    @Override
    public synchronized void append(byte[] data, int length) throws ProxyCacheException {
        try {
            if (isCompleted()) {
                throw new ProxyCacheException("Error append cache: cache file " + file + " is completed!");
            }
            dataFile.seek(available());
            dataFile.write(data, 0, length);
        } catch (IOException e) {
            String format = "Error writing %d bytes to %s from buffer with size %d";
            throw new ProxyCacheException(String.format(format, length, dataFile, data.length), e);
        }
    }

    @Override
    public synchronized void close() throws ProxyCacheException {
        try {
            dataFile.close();
            diskUsage.touch(file);
        } catch (IOException e) {
            throw new ProxyCacheException("Error closing file " + file, e);
        }
    }

    private long crccode;

    /**
     * 检测文件下载流包校验码是否合法
     *
     * @param in
     * @param checkCode
     * @return
     */
    public static boolean checkFileStreamLocal(InputStream in, long checkCode) {
        boolean result = false;
        if (in != null) {
            try {
                CRC32 crc32 = new CRC32();
                byte[] data = new byte[1024];
                int read = 0;
                while ((read = in.read(data)) != -1) {
                    crc32.update(data, 0, read);
                }
                long localCode = crc32.getValue();
                if (checkCode == localCode) {
                    result = true;
                }
                LogUtil.i(TAG, "checkFileStreamLocal localCode:  " + localCode + ";  checkCode:  " + checkCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setCrccode(long crccode) {
        LogUtil.i(TAG, "setCrccode  " + crccode);
        this.crccode = crccode;
    }

    @Override
    public synchronized boolean isCompleted() {
        return !isTempFile(file);
    }

    /**
     * Returns file to be used fo caching. It may as original file passed in constructor as some temp file for not completed cache.
     *
     * @return file for caching.
     */
    public File getFile() {
        return file;
    }

    private boolean isTempFile(File file) {
        return file.getName().endsWith(TEMP_POSTFIX);
    }

    @Override
    public synchronized void complete() throws ProxyCacheException {
        if (isCompleted()) {
            return;
        }
        close();
        LogUtil.i(TAG, "will to check and rename ");
        if (crccode != 0 && !checkCrcCede(crccode)) {
            boolean delete = file.delete();
            LogUtil.i(TAG, "crccode is not same ,so delete " + delete);
            throw new ProxyCacheException(" crccode is not same ,so delete ");
        }


        String fileName = file.getName().substring(0, file.getName().length() - TEMP_POSTFIX.length());
        File completedFile = new File(file.getParentFile(), fileName);
        boolean renamed = file.renameTo(completedFile);
        if (!renamed) {
            throw new ProxyCacheException("Error renaming file " + file + " to " + completedFile + " for completion!");
        }
        file = completedFile;
        try {
            dataFile = new RandomAccessFile(file, "r");
            diskUsage.touch(file);
        } catch (IOException e) {
            throw new ProxyCacheException("Error opening " + file + " as disc cache", e);
        }
    }

    private boolean checkCrcCede(long crcCede) {
        boolean isLocalVideoOk = true;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
            LogUtil.i(TAG, "准备检查 crccode:  " + file.getAbsolutePath());
            isLocalVideoOk = checkFileStreamLocal(fis, crcCede);
            LogUtil.i(TAG, "crccode is ok ?  " + isLocalVideoOk);

            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isLocalVideoOk;
    }
}
