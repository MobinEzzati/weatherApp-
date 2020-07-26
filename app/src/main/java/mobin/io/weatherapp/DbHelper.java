package mobin.io.weatherapp;

import android.app.Application;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class DbHelper extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("mydb.realm")
                .schemaVersion(1)
                .migration((realm, oldVersion, newVersion) -> {
                    RealmSchema schema = realm.getSchema() ;

                    if ( oldVersion == 1){
                        schema.get("WeatherModel")
                                .addField("imageUri" ,String.class);

                    }
                })
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);


    }
}
