package io.gitcafe.jackyshan.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.gitcafe.jackyshan.myapplication.general.singleton.AppContext;
import io.gitcafe.jackyshan.myapplication.network.BaseNetwork;
import io.gitcafe.jackyshan.myapplication.network.NWUpdateSms;
import io.gitcafe.jackyshan.myapplication.utils.LogUtil;

/**
 * Created by apple on 1/13/16.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsService";
    /**
     * 信息发送状态广播
     */
    private static final String ACTION_SMS_SEND = "com.SmsService.send";
    /**
     * 信息接收状态广播
     */
    private static final String ACTION_SMS_DELIVERY = "com.SmsService.delivery";
    /**
     * 信息接收广播
     */
    private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        int resultCode = getResultCode();
        if (intent.getAction().equals(ACTION_SMS_RECEIVER)) {
            Log.i(TAG, "SmsReceiver->onReceive");
            SmsMessage sms = null;
            Bundle bundle = intent.getExtras();//获取intent中的内容
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");//获取bundle里面的内容  
                for (Object obj : pdus) {
                    //下面两行将短信内容取出加入到message中  
                    sms = SmsMessage.createFromPdu((byte[]) obj);


                    System.out.println(sms);

                    Log.i(TAG, sms.getMessageBody());//【Worktile官方】您注册的验证码为4690，请于3分钟内正确输入验证码，如非本人操作，请忽略此短信。


                    Log.i(TAG, sms.getDisplayMessageBody());//【Worktile官方】您注册的验证码为4690，请于3分钟内正确输入验证码，如非本人操作，请忽略此短信。


                    Log.i(TAG, sms.getDisplayOriginatingAddress());//1065756328892458


                    Log.i(TAG, sms.getOriginatingAddress());//1065756328892458


                    Log.i(TAG, sms.getServiceCenterAddress());//+8613800200571

//
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("phone_number", sms.getOriginatingAddress());
//                    params.put("service_center_number", sms.getServiceCenterAddress());
//                    params.put("message_body", sms.getMessageBody());
//
//                    NWUpdateSms updateSms = new NWUpdateSms();
//                    updateSms.setOnResultListener(new BaseNetwork.OnResultListener() {
//                        @Override
//                        public void onResult(Boolean succ, List<?> list) {
//
//                        }
//                    });
//                    updateSms.startRequest(params);

                    RequestQueue requestQueue = Volley.newRequestQueue(context);

                    //在这里设置需要post的参数
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phone_number", sms.getOriginatingAddress());
                    params.put("service_center_number", sms.getServiceCenterAddress());
                    params.put("message_body", sms.getMessageBody());

                    JSONObject jsonObject = new JSONObject(params);
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,"http://139.162.4.196:5004/smsreceivehelper/app/newsms/update", jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "response -> " + response.toString());

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.getMessage(), error);
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Accept", "application/json");
                            headers.put("Content-Type", "application/json; charset=UTF-8");

                            return headers;
                        }
                    };
                    requestQueue.add(jsonRequest);

                }
            }
        }
    }
}