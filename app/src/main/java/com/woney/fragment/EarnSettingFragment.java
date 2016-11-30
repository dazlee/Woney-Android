package com.woney.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woney.R;
import com.woney.util.FacebookUtil;
import com.woney.view.ProfilePictureView;

public class EarnSettingFragment extends Fragment {

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
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        if (FacebookUtil.isLogin()) {
            TextView textFbName = (TextView) view.findViewById(R.id.setting_fb_name);
            textFbName.setText(FacebookUtil.getFbName());

            ProfilePictureView pic = (ProfilePictureView) view.findViewById(R.id.setting_fb_img);
            pic.setProfileId(FacebookUtil.getId());
        }
    }
}
