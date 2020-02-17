package dataModels;

import io.realm.RealmObject;

public class TestModel  {
    String name ;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TestModel(String name) {
        this.name = name;
    }
}
