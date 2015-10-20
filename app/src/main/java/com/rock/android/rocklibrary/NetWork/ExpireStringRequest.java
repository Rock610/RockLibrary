package com.rock.android.rocklibrary.NetWork;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Created by rock on 15/9/24.
 */
public class ExpireStringRequest extends StringRequest{

    private long expire;

    public ExpireStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public ExpireStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException var4) {
            parsed = new String(response.data);
        }

        return Response.success(parsed, HttpHeaderParser.parseIgnoreCacheHeaders(response, this.expire, this.expire));

    }

    public void setExpire(long time){
        this.expire = time;
    }

    public long getExpire(){
        return this.expire;
    }
}
