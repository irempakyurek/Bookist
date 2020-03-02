package com.example.bookist.presenter;

import android.util.Log;

import com.example.bookist.model.dao.CollectionDAO;
import com.example.bookist.model.realm.po.RealmBook;
import com.example.bookist.mvp.CollectionMVP;

import org.greenrobot.eventbus.EventBus;

import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class MyCollectionPresenter implements CollectionMVP.GridPresenter {

    private CollectionMVP.Model model;

    private CollectionMVP.GridView view;

    public MyCollectionPresenter(CollectionMVP.GridView view) {
        this.model = new CollectionDAO();
        this.view = view;
    }

    @Override
    public void getMyCollection() {
        Observable<RealmResults<RealmBook>> results = this.model.getCollection();
        results
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmResults -> EventBus.getDefault().postSticky(realmResults),
                        throwable -> {
                            view.showError();
                            throwable.printStackTrace();
                        },
                        () -> Log.d("LOG", "getMyCollection complete!")
                );
    }

    @Override
    public int removeBook(RealmBook realmBook) {
        return this.model.removeBook(realmBook);
    }

    @Override
    public void closeRealm() {
        this.model.closeRealm();
    }

}
