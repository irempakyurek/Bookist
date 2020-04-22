package com.example.bookist.model.realm.po;

import io.realm.RealmObject;

public class RealmString2 extends RealmObject {

    private String value;

    public RealmString2() {
    }

    public RealmString2(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
