package com.rock.android.rocklibrary.NetWork;

import com.android.volley.error.VolleyError;

/**
 * Created by rock on 15/10/14.
 */
public interface INetWorkHelper {

    void getRequest();

    void postRequest();

    void deleteRequest();

    void putRequest();

    void download(String downloadPath,final FileDownloadListener listener);

    void upload(String path, String fileKey);


    public interface FileDownloadListener {
        void onStart();

        void onProgress(long transferredBytes, long totalSize);

        void onSuccess(String tag, String data);

        void onFailed(String tag,VolleyError volleyError);
    }

}
