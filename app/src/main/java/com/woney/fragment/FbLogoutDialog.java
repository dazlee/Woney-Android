package com.woney.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.woney.R;
import com.woney.activity.MainActivity;
import com.woney.util.UserUtil;

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
                    if (UserUtil.getAccessToken() != null) {
                        new GraphRequest(UserUtil.getAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                                .Callback() {
                            @Override
                            public void onCompleted(GraphResponse graphResponse) {
                                LoginManager.getInstance().logOut();
                                MainActivity.setupFbLogout();
                            }
                        }).executeAsync();

                        //Toast.makeText(getApplicationContext(), "Facebook Logout", Toast.LENGTH_LONG).show();
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
