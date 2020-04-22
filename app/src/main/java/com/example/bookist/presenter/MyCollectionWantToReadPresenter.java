package com.example.bookist.presenter;
import android.util.Log;

import com.example.bookist.model.dao.Collection3DAO;
import com.example.bookist.model.dao.CollectionDAO;
import com.example.bookist.model.realm.po.RealmBook;
import com.example.bookist.model.realm.po.RealmBook3;
import com.example.bookist.mvp.Collection3MVP;
import com.example.bookist.mvp.CollectionMVP;

import org.greenrobot.eventbus.EventBus;

import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class MyCollectionWantToReadPresenter implements Collection3MVP.GridPresenter {

    private Collection3MVP.Model model;

    private Collection3MVP.GridView view;

    public MyCollectionWantToReadPresenter(Collection3MVP.GridView view) {
        this.model = new Collection3DAO();
        this.view = view;
    }

    @Override
    public void getMyCollection() {
        Observable<RealmResults<RealmBook3>> results = this.model.getCollection();
        results
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmResults -> EventBus.getDefault().postSticky(realmResults),
                        throwable -> {
                            view.showError();
                            throwable.printStackTrace();
                        },
                        () -> Log.d("LOG", "getMyCollection3 complete!")
                );
    }

    @Override
    public int removeBook(RealmBook3 realmBook) {
        return this.model.removeBook(realmBook);
    }

    @Override
    public void closeRealm() {
        this.model.closeRealm();
    }

}
