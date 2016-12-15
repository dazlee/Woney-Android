package com.woney.fragment;

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
import com.woney.data.WoneyKey;
import com.woney.req.GameLastDrawReq;
import com.woney.req.UserPhotoReq;
import com.woney.util.RestClient;

public class EarnWinnerFragment extends Fragment {

    private static LastDrawData lastDrawData;

    private static TextView topPrize;
    private static TextView firstReward;
    private static TextView secReward;

    private static ImageView firstWinPhoto;
    private static TextView firstWinName;

    private static TextView []secWinnerName;
    private static ImageView []secWinnerPhoto;
    private static UserPhotoReq []photoReqs;

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void setupView(View view) {
        topPrize = (TextView) view.findViewById(R.id.winner_top_winner_text);
        firstReward = (TextView) view.findViewById(R.id.winner_top_reward_text);
        firstWinPhoto = (ImageView) view.findViewById(R.id.winner_top_winner_fb_img);
        firstWinName = (TextView) view.findViewById(R.id.winner_top_fb_name);
        secReward = (TextView) view.findViewById(R.id.winner_sec_reward);

        secWinnerName = new TextView[WoneyKey.SEC_WINNERS_COUNT];
        secWinnerPhoto = new ImageView[WoneyKey.SEC_WINNERS_COUNT];
        // TBD
        secWinnerName[0] = (TextView) view.findViewById(R.id.winner_sec_name_0);
        secWinnerName[1] = (TextView) view.findViewById(R.id.winner_sec_name_1);
        secWinnerName[2] = (TextView) view.findViewById(R.id.winner_sec_name_2);
        secWinnerName[3] = (TextView) view.findViewById(R.id.winner_sec_name_3);
        secWinnerName[4] = (TextView) view.findViewById(R.id.winner_sec_name_4);
        secWinnerName[5] = (TextView) view.findViewById(R.id.winner_sec_name_5);
        secWinnerName[6] = (TextView) view.findViewById(R.id.winner_sec_name_6);
        secWinnerName[7] = (TextView) view.findViewById(R.id.winner_sec_name_7);
        secWinnerName[8] = (TextView) view.findViewById(R.id.winner_sec_name_8);
        secWinnerName[9] = (TextView) view.findViewById(R.id.winner_sec_name_9);
        secWinnerPhoto[0] = (ImageView) view.findViewById(R.id.winner_sec_pic_0);
        secWinnerPhoto[1] = (ImageView) view.findViewById(R.id.winner_sec_pic_1);
        secWinnerPhoto[2] = (ImageView) view.findViewById(R.id.winner_sec_pic_2);
        secWinnerPhoto[3] = (ImageView) view.findViewById(R.id.winner_sec_pic_3);
        secWinnerPhoto[4] = (ImageView) view.findViewById(R.id.winner_sec_pic_4);
        secWinnerPhoto[5] = (ImageView) view.findViewById(R.id.winner_sec_pic_5);
        secWinnerPhoto[6] = (ImageView) view.findViewById(R.id.winner_sec_pic_6);
        secWinnerPhoto[7] = (ImageView) view.findViewById(R.id.winner_sec_pic_7);
        secWinnerPhoto[8] = (ImageView) view.findViewById(R.id.winner_sec_pic_8);
        secWinnerPhoto[9] = (ImageView) view.findViewById(R.id.winner_sec_pic_9);
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

            firstWinName.setText(firstUser.getShowName());
            UserPhotoReq firstReq = new UserPhotoReq(firstUser.getPhotoUrl(), firstWinPhoto);
            firstReq.execute();

            UserData[] secWinners = lastDrawData.getCommonWinners();
            photoReqs = new UserPhotoReq[secWinners.length];
            if (secWinners.length <= secWinnerName.length) {
                for (int i=0; i<secWinners.length; i++) {
                    secWinnerName[i].setText(secWinners[i].getShowName());

                    String urlStr = secWinners[i].getPhotoUrl();
                    if (urlStr != null && urlStr != "") {
                        photoReqs[i] = new UserPhotoReq(secWinners[i].getPhotoUrl(), secWinnerPhoto[i]);
                        photoReqs[i].execute();
                    }
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
