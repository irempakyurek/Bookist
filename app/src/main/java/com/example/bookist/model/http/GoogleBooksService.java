package com.example.bookist.model.http;

import com.example.bookist.model.pojo.SearchResult;
import com.example.bookist.mvp.GoogleBookMVP;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class GoogleBooksService implements GoogleBookMVP.Model {

    public Observable<SearchResult> searchListOfBooks(String query) {

        RxJavaCallAdapterFactory rxAdapter =
                RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GoogleBooksAPI.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        GoogleBooksAPI googleBooksService = retrofit.create(GoogleBooksAPI.class);
        Observable<SearchResult> searchResultObservable = googleBooksService.searchBook(query);

        return searchResultObservable;
    }

}