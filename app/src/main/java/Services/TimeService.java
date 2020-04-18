package Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class TimeService extends Service {
    private final  IBinder iBinder = new MaBinder() ;
    public TimeService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder ;
    }

    public String getTime(){

        SimpleDateFormat dateformat =
                new SimpleDateFormat("HH:mm   MM/dd/yyyy",
                        Locale.US);
        return (dateformat.format(new Date()));
    }

    public class MaBinder extends Binder {

      public  TimeService  getTimeService (){

          return TimeService.this ;
        }

    }
}
