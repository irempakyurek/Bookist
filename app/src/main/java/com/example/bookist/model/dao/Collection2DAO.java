package com.example.bookist.model.dao;

import android.util.Log;

import com.example.bookist.model.pojo.Book;
import com.example.bookist.model.realm.po.RealmBook2;
import com.example.bookist.model.realm.util.RealmUtil2;
import com.example.bookist.mvp.Collection2MVP;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

public class Collection2DAO implements Collection2MVP.Model {

    private Realm realm;
    private boolean stateRealm;

    @Override
    public int saveBook(Book book) {
        this.openRealm();
        int result = BOOK_EXISTS_NOT_EXISTS;

        RealmBook2 realmBook = RealmUtil2.convertParcelableBookToPOBook(book);

        if (this.getBook(realmBook) == null) {

            try {
                this.realm.executeTransaction(
                        realm -> realm.copyToRealmOrUpdate(realmBook)
                );

                result = PERSIST_OK;
            } catch (Exception e) {
                Log.e("ERROR", "CollectionDAO2 - saveBook");
                e.printStackTrace();
                result = PERSIST_PROBLEM;
            }

        }

        return result;
    }

    @Override
    public RealmBook2 getBook(RealmBook2 book) {
        this.openRealm();
        RealmResults<RealmBook2> books =
                this.realm
                        .where(RealmBook2.class)
                        .equalTo(RealmBook2.ID, book.getId())
                        .findAll();

        if (!books.isEmpty()) {
            return books.first();
        }

        return null;
    }

    @Override
    public Observable<RealmResults<RealmBook2>> getCollection() {
        this.openRealm();

        return  Observable.just(this.realm.where(RealmBook2.class).findAll());
    }

    @Override
    public int removeBook(RealmBook2 realmBook) {
        this.openRealm();
        int result = PERSIST_PROBLEM;

        try {
            this.realm.executeTransaction(realm -> {
                realm.where(RealmBook2.class)
                        .equalTo(RealmBook2.ID, realmBook.getId())
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