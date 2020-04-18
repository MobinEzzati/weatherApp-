package Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UpdateTempService extends Service {
    TimerTask timerTask ;
    Timer timer = new Timer() ;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "service is started", Toast.LENGTH_SHORT).show();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("tag", "run: hallow");
            }
        };
        timer.schedule(timerTask , 1, 5000);
        return START_STICKY ;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: service is destroye");
    }



}
