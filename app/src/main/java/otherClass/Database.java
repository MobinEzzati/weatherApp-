package otherClass;

import android.app.Application;

import io.realm.Realm;

public class Database extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
