package newsfeed.hindu.goku.com.thehindu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by goku on 25-01-2015.
 */
public class MyStartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            Intent dailyUpdater = new Intent(context, MyService.class);
            context.startService(dailyUpdater);



       Log.d("AlarmReceiver", "Called context.startService from AlarmReceiver.onReceive");
    }
}