package com.app.woney.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.app.woney.data.WoneyKey;

import java.net.URL;

/**
 * Created by houan on 2016/12/14.
 */

public class LoadFbPhotoTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;

    public LoadFbPhotoTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... ids) {
        Bitmap myPicture = null;
        URL url = null;
        try {
            url = new URL(WoneyKey.fbUrlFront + ids[0] + WoneyKey.fbUrlEnd);
            myPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            Log.e("Photo", "Load picture failed! Url: " + url, e);
        }
        return myPicture;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
