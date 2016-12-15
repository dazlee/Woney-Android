package com.woney.req;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.net.URL;

/**
 * Created by houan on 2016/12/14.
 */

public class UserPhotoReq extends AsyncTask<Void, Void, Bitmap> {

    private String urlStr;
    private ImageView imageView;

    public UserPhotoReq(String urlStr, ImageView imageView) {
        this.urlStr = urlStr;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap myPicture = null;
        URL url = null;
        try {
            url = new URL(urlStr);
            myPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            Log.e("Photo", "Load picture failed! Url: " + url, e);
        }
        return myPicture;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
