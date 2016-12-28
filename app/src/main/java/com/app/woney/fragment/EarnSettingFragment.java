package com.app.woney.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.app.woney.R;
import com.app.woney.activity.MainActivity;
import com.app.woney.data.UserData;

public class EarnSettingFragment extends Fragment {
    private static TextView textFbName;
    private static ProfilePictureView pic;
    private static LinearLayout loginArea;
    private static LinearLayout settingLayout;
    private static ScrollView scrollView;

    public EarnSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earn_setting, container, false);
        view.setTag(R.string.main_tab_setting);
        textFbName = (TextView) view.findViewById(R.id.setting_fb_name);
        pic = (ProfilePictureView) view.findViewById(R.id.setting_fb_img);
        loginArea = (LinearLayout) view.findViewById(R.id.setting_fb_login_layout);
        settingLayout = (LinearLayout) view.findViewById(R.id.setting_layout);
        scrollView = (ScrollView) view.findViewById(R.id.setting_scroll_layout);

        setupFbLoginView();
        return view;
    }

    public static void setupFbLoginView() {
        UserData user = MainActivity.getUser();
        if (user.isFbLogin() && textFbName != null) {
            textFbName.setText(user.getDisplayName());
            pic.setProfileId(user.getFacebookID());
            loginArea.setVisibility(View.GONE);
        } else {
            Log.d("setupFbLoginView", "FB still not login.");
        }
    }

    public static void setupFbLogoutView(View view) {
        textFbName.setText(view.getResources().getString(R.string.setting_fb_gusee));
        pic.setProfileId(null);
        loginArea.setVisibility(View.VISIBLE);
    }

    public static void clickHow() {
        if (settingLayout.getVisibility() == View.VISIBLE) {
            settingLayout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    public static boolean pressBack() {
        if (isHowItWorkShow()) {
            settingLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isHowItWorkShow() {
        return scrollView != null && scrollView.getVisibility() == View.VISIBLE ? true : false;
    }
}
