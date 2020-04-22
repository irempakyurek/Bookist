package com.example.bookist.model.realm.po;

import io.realm.RealmObject;

public class RealmString3 extends RealmObject {

    private String value;

    public RealmString3() {
    }

    public RealmString3(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
