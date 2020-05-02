package Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class TempService extends Service  {
    private ArrayList<String>temps = new ArrayList<>() ;

    private final IBinder iBinder = new Mybinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class Mybinder extends Binder{

        public TempService boundService ()
        {
            return TempService.this ;
        }
    }

    public ArrayList<String> updatedTemp( ArrayList<String> citiesName){

        return temps ;
    }


}
