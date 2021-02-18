package com.danikula.videocache;

import android.text.TextUtils;
import android.util.Log;

import com.danikula.videocache.headers.EmptyHeadersInjector;
import com.danikula.videocache.headers.HeaderInjector;
import com.danikula.videocache.sourcestorage.SourceInfoStorage;
import com.danikula.videocache.sourcestorage.SourceInfoStorageFactory;
import com.lunzn.tool.log.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.danikula.videocache.Preconditions.checkNotNull;
import static com.danikula.videocache.ProxyCacheUtils.DEFAULT_BUFFER_SIZE;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_PARTIAL;


public class OkHttpUrlSource implements Source {

    private static final String TAG = "OkHttpUrlSource";

    private static final int MAX_REDIRECTS = 5;
    public final String url;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private Call requestCall = null;
    private InputStream inputStream;
    private volatile long length = Integer.MIN_VALUE;
    private volatile String mime;
    private Map<String, String> headers;

    private final SourceInfoStorage sourceInfoStorage;
    private final HeaderInjector headerInjector;
    private SourceInfo sourceInfo;


    public OkHttpUrlSource(String url) {
        this(url, SourceInfoStorageFactory.newEmptySourceInfoStorage());
    }

    public OkHttpUrlSource(String url, SourceInfoStorage sourceInfoStorage) {
        this(url, sourceInfoStorage, new EmptyHeadersInjector());
    }

    public OkHttpUrlSource(String url, SourceInfoStorage sourceInfoStorage, HeaderInjector headerInjector) {
        this.sourceInfoStorage = checkNotNull(sourceInfoStorage);
        this.headerInjector = checkNotNull(headerInjector);
        SourceInfo sourceInfo = sourceInfoStorage.get(url);
        this.sourceInfo = sourceInfo != null ? sourceInfo :
                new SourceInfo(url, Integer.MIN_VALUE, ProxyCacheUtils.getSupposablyMime(url));
        this.url = this.sourceInfo.url;
    }

    public OkHttpUrlSource(OkHttpUrlSource source) {
        this.sourceInfo = source.sourceInfo;
        this.sourceInfoStorage = source.sourceInfoStorage;
        this.headerInjector = source.headerInjector;
        this.url = this.sourceInfo.url;
    }




    @Override
    public synchronized long length() throws ProxyCacheException {
        if (sourceInfo.length == Integer.MIN_VALUE) {
            fetchContentInfo();
        }
        LogUtil.i("TAG" + Thread.currentThread().getName(), "length: " + sourceInfo.length);
        return sourceInfo.length;
    }

    @Override
    public void open(long offset) throws ProxyCacheException {
        try {
            Response response = openConnection(offset, -1);
            mime = response.header("Content-Type");
            inputStream = new BufferedInputStream(response.body().byteStream(), DEFAULT_BUFFER_SIZE);
            length = readSourceAvailableBytes(response, (int) offset, response.code());
            this.sourceInfo = new SourceInfo(sourceInfo.url, length, mime);
            this.sourceInfoStorage.put(sourceInfo.url, sourceInfo);
        } catch (IOException e) {
            throw new ProxyCacheException("Error opening okHttpClient for " + url + " with offset " + offset, e);
        }
    }

    private long readSourceAvailableBytes(Response response, int offset, int responseCode) throws IOException {
        int contentLength = Integer.valueOf(response.header("Content-Length", "-1"));

        return responseCode == HTTP_OK ? contentLength
                : responseCode == HTTP_PARTIAL ? contentLength + offset : length;
    }

    @Override
    public void close() throws ProxyCacheException {
        if (okHttpClient != null && inputStream != null && requestCall != null) {
            try {
                inputStream.close();
                requestCall.cancel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int read(byte[] buffer) throws ProxyCacheException {
        if (inputStream == null) {
            throw new ProxyCacheException("Error reading data from " + url + ": okHttpClient is absent!");
        }
        try {
            int read = inputStream.read(buffer, 0, buffer.length);
            return read;
        } catch (InterruptedIOException e) {
            throw new InterruptedProxyCacheException("Reading source " + url + " is interrupted", e);
        } catch (IOException e) {
            throw new ProxyCacheException("Error reading data from " + url, e);
        }
    }

    private void fetchContentInfo() throws ProxyCacheException {
        LogUtil.d(TAG, "Read content info from " + url);
        Response response = null;
        try {
            response = openConnectionForHeader( 20000);
            if (response!=null){
                length = Integer.valueOf(response.header("Content-Length", "-1"));
                mime = response.header("Content-Type");
                this.sourceInfo = new SourceInfo(sourceInfo.url, length, mime);
                this.sourceInfoStorage.put(sourceInfo.url, sourceInfo);
                LogUtil.d(TAG, "Content info for `" + url + "`: mime: " + mime + ", content-length: " + length);
            }
        } catch (IOException e) {
            LogUtil.e( "Error fetching info from " + url, e);
        } finally {
            if (response != null) {
                requestCall.cancel();
                response.close();
            }
        }
    }


    private Response openConnectionForHeader(int timeout) throws IOException, ProxyCacheException {
        String url = this.url;
        Response response = null;
        try {
            Request request = null;
            Request.Builder builder = new Request.Builder();
            builder.head().url(url);
            if(headers != null) {
                //设置请求头
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    LogUtil.i( "请求头信息 key:" + entry.getKey() +" Value" + entry.getValue());
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            request = builder.build();
            requestCall = okHttpClient.newCall(request);
            response = requestCall.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }

    private Response openConnection(long offset, int timeout) throws IOException {
        String url = this.url;
        Request request = null;
        //do {
        Request.Builder builder = new Request.Builder();
        builder.url(url);


        if(headers != null) {
            //设置请求头
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                LogUtil.i( "请求头信息 key:" + entry.getKey() +" Value" + entry.getValue());
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (offset > 0) {
            builder.addHeader("Range", "bytes=" + offset + "-");
        }

        request = builder.build();
        requestCall = okHttpClient.newCall(request);
        return requestCall.execute();
    }

    public synchronized String getMime() throws ProxyCacheException {
        if (TextUtils.isEmpty(mime)) {
            fetchContentInfo();
        }
        return mime;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "HttpUrlSource{url='" + url + "}";
    }
}
