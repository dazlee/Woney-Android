package com.app.woney.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.app.woney.R;
import com.app.woney.activity.MainActivity;
import com.app.woney.data.UserData;

/**
 * Created by houan on 2016/12/3.
 */

public class FbLogoutDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.alert_fb_logout)
            .setPositiveButton(R.string.alert_fb_logout_btn_true, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final UserData user = MainActivity.getUser();
                    if (user.getAccessToken() != null) {
                        new GraphRequest(user.getAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse graphResponse) {
                                LoginManager.getInstance().logOut();
                                UserData user = MainActivity.getUser();
                                user.logoutFb();
                                user.clearData();
                                MainActivity.setupFbLogoutView();
                                MainActivity.setupWoneyCreditView();
                            }
                        }).executeAsync();
                    }
                }
            })
            .setNegativeButton(R.string.alert_fb_logout_btn_false, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("FB", "Logout cancel");
                }
            });
        return builder.create();
    }
}
