package genius.com.callremainder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallListener extends BroadcastReceiver {

    private String mobileNUmber;
    private int callState;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        OwnPhonseStateListener ownStateListener = new OwnPhonseStateListener();
        telephony.listen(ownStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        Bundle bundle = intent.getExtras();
        mobileNUmber = bundle.getString("incoming_number");
    }

    private boolean isRinging, isReceived;

    public class OwnPhonseStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    callState = state;
                    isRinging = true;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    callState = state;
                    isReceived = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if(isRinging && !isReceived){
                        System.out.println("Call Missed " + mobileNUmber);
                        makeAlarm();
                    }
                    break;
            }
        }
    }

    private void makeAlarm(){
        try {
            Intent intent = new Intent(mContext, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                    123, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am = (AlarmManager)mContext.getSystemService(Activity.ALARM_SERVICE);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                    15*60*60*1000,pendingIntent);
            showNotification(mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(Context mContext) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 90, new Intent(mContext, HomeActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_call_missed_black_24dp)
                .setContentTitle("Missed Call Remainder")
                .setContentText(mobileNUmber)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);

        NotificationManager notiManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notiManager.notify(1, builder.build());
    }
}
