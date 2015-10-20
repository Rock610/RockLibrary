package com.rock.android.rocklibrary.NetWork;

/**
 * Created by rock on 15/10/19.
 */
public class Controller {

    public static void requestTest(String url,BaseNetWorkHelper.IhttpResponseHandler handler){
        BaseNetWorkHelper helper = new BaseNetWorkHelper(url,null,handler);
        helper.getRequest();
    }
}
