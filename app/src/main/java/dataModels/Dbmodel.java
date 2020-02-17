package dataModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Dbmodel extends RealmObject {
    @PrimaryKey
    int ID ;
    String temp ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
