package com.rock.android.rocklibrary.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.rock.android.rocklibrary.NetWork.BaseNetWorkHelper;
import com.rock.android.rocklibrary.NetWork.Controller;
import com.rock.android.rocklibrary.R;
import com.rock.android.rocklibrary.Utils.Image.CameraCore;

public class MainActivity extends TitleBarBaseActivity implements BaseNetWorkHelper.IhttpResponseHandler,CameraCore.CameraResult{

    private String url;
    private CameraCore mCameraCore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewRes(R.layout.content_main);
        setTitleText("我是Title");
        mTitleBar.getTitleText().setTextColor(Color.WHITE);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        mCameraCore = new CameraCore(this,this);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Uri uri = Uri.parse("test.png");
////                mCameraCore.getPhotoFromAlbum(uri);
//                mCameraCore.getAlbumData(MainActivity.this);
//            }
//        });
        url = "http://www.weather.com.cn/adat/sk/101010100.html";
        Controller.requestTest(url,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCameraCore.onResult(requestCode,resultCode,data);
    }

    @Override
    public void onHttpSuccess(String tag, Object response) {
        if(tag.equals(url)){
            System.out.println("response=====>"+response);
        }
    }

    @Override
    public void onHttpError(String errorCode, String errorResponse, String tag) {

    }

    @Override
    public void onSuccess(String path) {
        System.out.println("success==>"+path);
    }

    @Override
    public void onFail(String error) {
        System.out.println("error===>"+error);
    }
}
