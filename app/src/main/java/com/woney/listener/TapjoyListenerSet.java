package com.woney.listener;

import android.util.Log;

import com.tapjoy.TJGetCurrencyBalanceListener;

/**
 * Created by houan on 2016/12/16.
 */

public class TapjoyListenerSet {

    public static TJGetCurrencyBalanceListener getBalanceListener = new TJGetCurrencyBalanceListener() {
        @Override
        public void onGetCurrencyBalanceResponse(String currencyName, int balance) {
            Log.i("Tapjoy", "getCurrencyBalance returned " + currencyName + ":" + balance);
        }

        @Override
        public void onGetCurrencyBalanceResponseFailure(String error) {
            Log.i("Tapjoy", "getCurrencyBalance error:  " + error);
        }
    };
}
