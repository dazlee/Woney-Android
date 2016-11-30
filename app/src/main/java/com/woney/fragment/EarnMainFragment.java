package com.woney.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woney.R;
import com.woney.util.ScreenUtil;

public class EarnMainFragment extends Fragment {

    public EarnMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earn_main, container, false);
        return view;
    }

    private void initView(View view) {
        float scale = ScreenUtil.getScale(getActivity().getApplicationContext());

        TextView textEarnPrice = (TextView) view.findViewById(R.id.earn_top_price);
        float textSize = getResources().getDimension(R.dimen.earn_top_price);
        //textEarnPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize * scale);
    }
}
