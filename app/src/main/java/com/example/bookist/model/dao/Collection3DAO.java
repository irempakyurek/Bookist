package com.example.bookist.model.dao;

import android.util.Log;

import com.example.bookist.model.pojo.Book;
import com.example.bookist.model.realm.po.RealmBook;
import com.example.bookist.model.realm.po.RealmBook3;
import com.example.bookist.model.realm.util.RealmUtil;
import com.example.bookist.model.realm.util.RealmUtil3;
import com.example.bookist.mvp.Collection3MVP;
import com.example.bookist.mvp.CollectionMVP;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

public class Collection3DAO implements Collection3MVP.Model {

    private Realm realm;
    private boolean stateRealm;

    @Override
    public int saveBook(Book book) {
        this.openRealm();
        int result = BOOK_EXISTS_NOT_EXISTS;

        RealmBook3 realmBook = RealmUtil3.convertParcelableBookToPOBook(book);

        if (this.getBook(realmBook) == null) {

            try {
                this.realm.executeTransaction(
                        realm -> realm.copyToRealmOrUpdate(realmBook)
                );

                result = PERSIST_OK;
            } catch (Exception e) {
                Log.e("ERROR", "CollectionDAO - saveBook");
                e.printStackTrace();
                result = PERSIST_PROBLEM;
            }

        }

        return result;
    }

    @Override
    public Observable<RealmResults<RealmBook3>> getCollection() {
        this.openRealm();

        return  Observable.just(this.realm.where(RealmBook3.class).findAll());
    }

    @Override
    public RealmBook3 getBook(RealmBook3 realmBook) {
        this.openRealm();
        RealmResults<RealmBook3> books =
                this.realm
                        .where(RealmBook3.class)
                        .equalTo(RealmBook3.ID, realmBook.getId())
                        .findAll();

        if (!books.isEmpty()) {
            return books.first();
        }

        return null;
    }

    @Override
    public int removeBook(RealmBook3 realmBook) {
        this.openRealm();
        int result = PERSIST_PROBLEM;

        try {
            this.realm.executeTransaction(realm -> {
                realm.where(RealmBook3.class)
                        .equalTo(RealmBook3.ID, realmBook.getId())
                        .findAll()
                        .deleteAllFromRealm();
            });

            result = PERSIST_OK;
        } catch (Exception e) {
            Log.e("ERROR", "CollectionDAO - removeBook");
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void openRealm() {
        this.stateRealm = true;
        if (this.realm == null) {
            this.realm = Realm.getDefaultInstance();
        }
    }

    @Override
    public void closeRealm() {
        if (this.stateRealm) {
            this.realm.close();
            this.realm = null;
        }
    }

}