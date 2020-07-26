package Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import otherClass.NetworkUtils;

public class InternetConnection extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String status = NetworkUtils.getConnectivityStatusString(context );
        if (status.isEmpty()){
            Toast.makeText(context, "your internet is not connected", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }
}
