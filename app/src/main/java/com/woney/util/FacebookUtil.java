package com.woney.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

/**
 * Created by houan on 2016/11/13.
 */

public class FacebookUtil {

    private static AccessToken accessToken;
    private static LoginManager loginManager;
    private static Profile profile;
    private static Bitmap myPicture = null;
    private static int imgSize = 100;

    static {
        accessToken = AccessToken.getCurrentAccessToken();
        profile = Profile.getCurrentProfile();
    }

    public static boolean isLogin() {
        return accessToken != null ? true : false;
    }

    public static String getId() {
        return profile.getId();
    }

    public static FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();

            Log.d("FB", "Access success");
            /*
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {

                        //當RESPONSE回來的時候

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            //讀出姓名 ID FB個人頁面連結

                            Log.d("FB", "complete");
                            Log.d("FB", object.optString("name"));
                            Log.d("FB", object.optString("link"));
                            Log.d("FB", object.optString("id"));

                        }
                    });

            //包入你想要得到的資料 送出request
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();
            */
        }

        @Override
        public void onCancel() {
            Log.d("FB", "Cancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FB", error.toString());
        }
    };

    public static void setMyPicture(Bitmap myPicture) {
        FacebookUtil.myPicture = myPicture;
    }

    public static String getFbName() {
        return (profile != null) ? profile.getName() : null;
    }

    public static Uri getPictureUri() {
        return profile.getProfilePictureUri(imgSize, imgSize);
    }

    public static Bitmap getProfilePicture(ContentResolver cr) {
        if (myPicture != null) {
            return myPicture;
        }

        return null;
    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
