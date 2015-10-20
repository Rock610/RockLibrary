package com.rock.android.rocklibrary.NetWork;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.DownloadRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rock on 15/10/14.
 */
public class BaseNetWorkHelper implements INetWorkHelper {

    private static RequestQueue mRequestQueue;
    private long currRequestExpiry;
    private Map<String, String> headers;
    private String mUrl;
    private Map<String,Object> mParams;
    private String tag;

    private IhttpResponseHandler mHttpResponseHandler;

    public interface IhttpResponseHandler {

        /**
         * 服务器访问成功
         *
         * @param tag  请求返回码
         * @param response 返回的数据
         */
        void onHttpSuccess(String tag, Object response);

        /**
         * 网络连接错误， timeout, no network
         *
         * @param tag 请求返回码
         */
        void onHttpError(String errorCode,String errorResponse,String tag);
    }


    public BaseNetWorkHelper(String mUrl, Map<String, Object> mParams,IhttpResponseHandler mHttpResponseHandler) {
        this.mUrl = mUrl;
        this.mParams = mParams;
        this.tag = mUrl;
        this.mHttpResponseHandler = mHttpResponseHandler;
    }

    public static void start(Context ctx){
        mRequestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
    }

    private void doRequest(int method) {

        Map<String, Object> params = mParams;
        String url = mUrl;
        final Map<String, String> p = getRequestParam(params);

        ExpireStringRequest request = new ExpireStringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                MLog.e(TAG, "请求成功------>" + url);
//                if (s != null)
//                    MLog.e(TAG, s);

                preParseResponse(s);
                mHttpResponseHandler.onHttpSuccess(tag, s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                MLog.e(TAG, "请求失败------>" + url);
//                MLog.e(TAG,
//                        "onFailure------>" + volleyError.getMessage() + ":" + volleyError.toString() + "++"
//                                + volleyError.networkResponse.statusCode);
                preParseError(volleyError);
                mHttpResponseHandler.onHttpError(String.valueOf(volleyError.networkResponse.statusCode), parseErrorResponse(volleyError), tag);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return p;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                if(volleyGetHeaders() == null){
                    return super.getHeaders();
                }
                return volleyGetHeaders();
            }
        };
        request.setTag(tag);

        request.setExpire(this.currRequestExpiry);

        addToQueue(request);
    }

    private Map<String, String> getRequestParam(Map<String, Object> map) {
        if(map == null) return null;
        Map<String, String> param = new HashMap<>();
        for (String key : map.keySet()) {
            param.put(key, String.valueOf(map.get(key)));
        }

        return param;
    }

    public void addToQueue(Request request) {
        if (mRequestQueue == null) {
            Log.e("BaseNetWorkHelper","未初始化RequestQueue!!!,请先调用start函数");
            return;
        }
        mRequestQueue.add(request);
    }

    @Override
    public void getRequest() {
        doRequest(Request.Method.GET);
    }

    @Override
    public void postRequest() {
        doRequest(Request.Method.POST);
    }

    @Override
    public void deleteRequest() {
        doRequest(Request.Method.DELETE);
    }

    @Override
    public void putRequest() {
        doRequest(Request.Method.PUT);
    }

    @Override
    public void download(String downloadPath,final FileDownloadListener fileDownloadListener) {

        final String url = mUrl;
        DownloadRequest request = new DownloadRequest(url, downloadPath, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                MLog.e(TAG, "请求成功------>" + url);
//                if (s != null)
//                    MLog.e(TAG, s);
                preParseResponse(s);
                fileDownloadListener.onSuccess(tag, s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                MLog.e(TAG, "请求失败------>" + url);
//                if (volleyError != null && volleyError.networkResponse != null) {
//                    MLog.e(TAG,
//                            "onFailure------>" + volleyError.getMessage() + ":" + volleyError.toString() + "++"
//                                    + volleyError.networkResponse.statusCode);
//                }

                preParseError(volleyError);
                fileDownloadListener.onFailed(tag,volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return volleyGetHeaders() == null?super.getHeaders():volleyGetHeaders();
            }
        };

        request.setOnProgressListener(new Response.ProgressListener() {
            @Override
            public void onProgress(long l, long l1) {
                if (l == 0) {
                    fileDownloadListener.onStart();
                }
                fileDownloadListener.onProgress(l, l1);
            }
        });

        request.setTag(tag);

        addToQueue(request);
    }

    @Override
    public void upload(String path, String fileKey) {
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                MLog.e(TAG, "请求成功------>" + url);
//                if (s != null)
//                    MLog.e(TAG, s);
                preParseResponse(s);
                mHttpResponseHandler.onHttpSuccess(tag, s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                MLog.e(TAG, "请求失败------>" + url);
//                MLog.e(TAG,
//                        "onFailure------>" + volleyError.getMessage() + ":" + volleyError.toString() + "++"
//                                + volleyError.networkResponse.statusCode);
                preParseError(volleyError);
                mHttpResponseHandler.onHttpError(volleyError.getMessage(),parseErrorResponse(volleyError), tag);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return volleyGetHeaders() == null?super.getHeaders():volleyGetHeaders();
            }
        };

        request.addFile("file", path);
        String contentTye = "multipart/form-data";
        if(mParams != null){
            for (String key : mParams.keySet()) {
                request.addMultipartParam(key, contentTye, String.valueOf(mParams.get(key)));
            }
        }

        request.setTag(tag);

        addToQueue(request);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> volleyGetHeaders() {
        return headers;
    }


    public void setCurrRequestExpiry(long currRequestExpiry) {
        this.currRequestExpiry = currRequestExpiry;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setParams(Map<String, Object> mParams) {
        this.mParams = mParams;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**预解析返回的内容*/
    protected void preParseResponse(String s){

    }

    /**预解析错误内容*/
    protected void preParseError(VolleyError volleyError){

    }

    private String parseErrorResponse(VolleyError volleyError) {
        String errorResponse = "";
        if (volleyError.networkResponse == null) {
            return "";
        }

        try {
            errorResponse = new String(volleyError.networkResponse.data, "utf-8");
        } catch (Exception e) {
//            MLog.e(TAG, e);
        }
        return  errorResponse;
    }
}
