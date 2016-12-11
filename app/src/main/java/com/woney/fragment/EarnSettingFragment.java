package com.woney.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.woney.R;
import com.woney.activity.MainActivity;
import com.woney.data.UserData;

public class EarnSettingFragment extends Fragment {
    private static TextView textFbName;
    private static ProfilePictureView pic;
    private static LinearLayout loginArea;

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

        setupLoginView(view);
        return view;
    }

    public static void setupLoginView(View view) {
        UserData user = MainActivity.getUser();
        if (user.isFbLogin()) {
            textFbName.setText(user.getName());
            pic.setProfileId(user.getFacebookID());
            loginArea.setVisibility(View.GONE);
        }
    }

    public static void setupLogoutView(View view) {
        textFbName.setText(view.getResources().getString(R.string.setting_fb_gusee));
        pic.setProfileId(null);
        loginArea.setVisibility(View.VISIBLE);
    }
}
