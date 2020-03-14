package mobin.io.weatherapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DbHelper extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("mydb.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);


    }
}
