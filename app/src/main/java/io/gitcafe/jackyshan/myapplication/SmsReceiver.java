package io.gitcafe.jackyshan.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by apple on 1/13/16.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG ="SmsService";
    /**
     * 信息发送状态广播
     */
    private static final String ACTION_SMS_SEND 	= "com.SmsService.send";
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
        if (intent.getAction().equals(ACTION_SMS_RECEIVER)){
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


                    Log.i(TAG, sms.getEmailBody());

                }
            }
        }    //接收信息接收状态
        else if (intent.getAction().equals(ACTION_SMS_DELIVERY)){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.i(TAG, "短信接收成功");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Log.i(TAG, "短信接收失败:GENERIC_FAILURE");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Log.i(TAG, "短信接收失败:NO_SERVICE");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Log.i(TAG, "短信接收失败:NULL_PDU");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Log.i(TAG, "短信接收失败:RADIO_OFF");
                    break;
            }
        }
    }
}