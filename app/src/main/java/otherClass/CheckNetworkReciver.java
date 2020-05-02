package otherClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

public class CheckNetworkReciver extends BroadcastReceiver {
        Context  mainContext ;
        boolean isconnect = true ;
    @Override
    public void onReceive(Context context, Intent intent) {
        mainContext = context ;

    }
    public boolean isConnect (){

        ConnectivityManager connectivityManager = (ConnectivityManager) mainContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        assert connectivityManager != null;
        connectivityManager.registerNetworkCallback(builder.build() ,
                                                        new ConnectivityManager.NetworkCallback(){
                                                            @Override
                                                            public void onAvailable(@NonNull Network network) {
                                                                super.onAvailable(network);
                                                                isconnect = false ;
                                                            }

                                                            @Override
                                                            public void onLosing(@NonNull Network network, int maxMsToLive) {
                                                                super.onLosing(network, maxMsToLive);
                                                                isconnect = false ;
                                                            }

                                                            @Override
                                                            public void onLost(@NonNull Network network) {
                                                                super.onLost(network);
                                                                isconnect = false ;
                                                            }

                                                            @Override
                                                            public void onUnavailable() {
                                                                super.onUnavailable();
                                                                isconnect = false ;
                                                            }
                                                        });



        return isconnect ;
    }


}
