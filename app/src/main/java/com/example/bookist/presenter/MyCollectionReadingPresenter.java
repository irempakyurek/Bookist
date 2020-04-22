package com.example.bookist.presenter;
import android.util.Log;

import com.example.bookist.model.dao.Collection2DAO;
import com.example.bookist.model.realm.po.RealmBook2;
import com.example.bookist.mvp.Collection2MVP;

import org.greenrobot.eventbus.EventBus;

import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class MyCollectionReadingPresenter implements Collection2MVP.GridPresenter {

    private Collection2MVP.Model model;

    private Collection2MVP.GridView view;

    public MyCollectionReadingPresenter(Collection2MVP.GridView view) {
        this.model = new Collection2DAO();
        this.view = view;
    }

    @Override
    public void getMyCollection() {
        Observable<RealmResults<RealmBook2>> results2 = this.model.getCollection();
        results2
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmResults -> EventBus.getDefault().postSticky(realmResults),
                        throwable -> {
                            view.showError();
                            throwable.printStackTrace();
                        },
                        () -> Log.d("LOG", "getMyCollection2 complete!")
                );
    }

    @Override
    public int removeBook(RealmBook2 realmBook) {
        return this.model.removeBook(realmBook);
    }

    @Override
    public void closeRealm() {
        this.model.closeRealm();
    }

}
