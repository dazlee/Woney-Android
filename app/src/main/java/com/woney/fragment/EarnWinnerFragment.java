package com.woney.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woney.R;
import com.woney.data.LastDrawData;
import com.woney.data.UserData;
import com.woney.req.GameLastDrawReq;
import com.woney.util.RestClient;
import com.woney.util.SystemUtil;

public class EarnWinnerFragment extends Fragment {

    private static LastDrawData lastDrawData;

    private static TextView topPrize;
    private static TextView firstReward;
    private static ImageView firstWinPhoto;
    private static TextView firstWinName;
    private static TextView secReward;

    public EarnWinnerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earn_winner, container, false);
        view.setTag(R.string.main_tab_winner);

        setupView(view);

        loadLastDraw();
        return view;
    }

    private void setupView(View view) {
        topPrize = (TextView) view.findViewById(R.id.winner_top_winner_text);
        firstReward = (TextView) view.findViewById(R.id.winner_top_reward_text);
        firstWinPhoto = (ImageView) view.findViewById(R.id.winner_top_winner_fb_img);
        firstWinName = (TextView) view.findViewById(R.id.winner_top_fb_name);
        secReward = (TextView) view.findViewById(R.id.winner_sec_reward);
    }

    private void loadLastDraw() {
        GameLastDrawReq req = new GameLastDrawReq();
        RestClient restClient = new RestClient(req);
        restClient.execute();
    }

    public static void setupLastDrawView() {
        if (lastDrawData != null) {
            UserData firstUser = lastDrawData.getFirstWinner();
            topPrize.setText(lastDrawData.getFormatedTopText());
            firstReward.setText(String.valueOf(lastDrawData.getReward()));
            secReward.setText(lastDrawData.getFormatedSecReward());

            String name = firstUser.getName();
            if (name != null) {
                firstWinName.setText(name);
            } else {
                firstWinName.setText(firstUser.getLastName() + " " + firstUser.getFirstName());
            }

            String photoUrl = firstUser.getPhotoUrl();
            if (photoUrl != null) {
                Bitmap photo = SystemUtil.loadPicture(photoUrl);
                if (photo != null) {
                    firstWinPhoto.setImageBitmap(photo);
                }
            }
        }
    }

    public static LastDrawData getLastDrawData() {
        return lastDrawData;
    }

    public static void setLastDrawData(LastDrawData lastDrawData) {
        EarnWinnerFragment.lastDrawData = lastDrawData;
    }
}
