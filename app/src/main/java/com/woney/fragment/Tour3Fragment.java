package com.woney.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.woney.R;
import com.woney.util.SystemUtil;

public class Tour3Fragment extends Fragment {

    public Tour3Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tour3, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button = (Button) getView().findViewById(R.id.tour_btn_start);
        if (!SystemUtil.isFirstStarted(getActivity().getBaseContext())) {
            button.setVisibility(View.INVISIBLE);
        }
    }
}
