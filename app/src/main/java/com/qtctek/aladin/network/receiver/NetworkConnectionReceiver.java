package com.qtctek.aladin.network.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.qtctek.aladin.view.post_news.activity.MainActivity;

import java.io.IOException;

import static android.content.Context.LOCATION_SERVICE;

public class NetworkConnectionReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
        if(!isConnected()){
            if(MainActivity.NETWORK_CONNECTION_DIALOG != null){
                MainActivity.NETWORK_CONNECTION_DIALOG.show();
            }
        }
        else{
            if(MainActivity.NETWORK_CONNECTION_DIALOG != null){
                MainActivity.NETWORK_CONNECTION_DIALOG.dismiss();
            }
        }
    }

    public boolean isConnected() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { Log.e("ERROR", "IOException",e); }
        catch (InterruptedException e) { Log.e("ERROR", "InterruptedException",e); }

        return false;
    }

}
