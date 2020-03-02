package com.example.bookist.presenter;

import android.util.Log;

import com.example.bookist.model.http.GoogleBooksService;
import com.example.bookist.model.pojo.SearchResult;
import com.example.bookist.mvp.GoogleBookMVP;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GoogleBooksPresenter implements GoogleBookMVP.Presenter {

    private GoogleBookMVP.View view;

    private GoogleBookMVP.Model model;

    public GoogleBooksPresenter(GoogleBookMVP.View view) {
        this.view = view;
        this.model = new GoogleBooksService();
    }

    @Override
    public void searchListOfBooks(String query) {
        Observable<SearchResult> result = model.searchListOfBooks(query);
        result
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        searchResult -> EventBus.getDefault().postSticky(searchResult),
                        throwable -> {
                            view.showError();
                            throwable.printStackTrace();
                        },
                        () -> Log.d("LOG", "searchListOfBooks complete!")
                );
    }
}
