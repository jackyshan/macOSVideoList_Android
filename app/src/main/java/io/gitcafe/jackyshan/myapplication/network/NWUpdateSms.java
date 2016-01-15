package io.gitcafe.jackyshan.myapplication.network;

import android.content.Context;

import java.util.Map;

/**
 * Created by apple on 1/14/16.
 */
public class NWUpdateSms extends BaseNetwork {
    public NWUpdateSms(Context context) {
        super(context);
    }

    @Override
    public void startRequest(Map map) {
        super.startRequest(map);

        path = "smsreceivehelper/app/newsms/update";
        params = map;
        startPost();
    }

    @Override
    protected void dealComplete(Boolean succ, String json) {
        super.dealComplete(succ, json);

        if (succ) {

            listener.onResult(true, null);
        }
    }
}
