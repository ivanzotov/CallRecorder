package com.callrecorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.callrecorder.service.RecService;
import com.facebook.react.HeadlessJsTaskService;
import android.telephony.TelephonyManager;

public final class RecReceiver extends BroadcastReceiver {
    private static boolean incomingCall = false;

    public final void onReceive(Context context, Intent intent) {
        Intent recIntent = new Intent(context, RecService.class);
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            recIntent.putExtra("action", "phone_state");
            String phoneState = intent.getStringExtra("state");
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumber = intent.getStringExtra("incoming_number");
                incomingCall = true;
                recIntent.putExtra("state", "extra_state_ringing");
                recIntent.putExtra("incoming_call", true);
                recIntent.putExtra("number", phoneNumber);
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                if (incomingCall) {
                    incomingCall = false;
                }
                recIntent.putExtra("state", "extra_state_offhook");
                recIntent.putExtra("incoming_call", false);
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                if (incomingCall) {
                    incomingCall = false;
                }
                recIntent.putExtra("state", "extra_state_idle");
                recIntent.putExtra("incoming_call", false);
            }
        } else {
            recIntent.putExtra("action", "new_outgoing_call");
        }
        context.startService(recIntent);
        HeadlessJsTaskService.acquireWakeLockNow(context);
    }
}
