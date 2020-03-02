package com.example.bookist.model.http;

import com.example.bookist.model.pojo.SearchResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GoogleBooksAPI {

    public static final String URL_BASE = "https://www.googleapis.com/books/v1/volumes/";

    @GET("./")
    Observable<SearchResult> searchBook(@Query("q") String q);

}