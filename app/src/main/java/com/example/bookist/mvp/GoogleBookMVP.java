package com.example.bookist.mvp;

import com.example.bookist.model.pojo.SearchResult;

import rx.Observable;

public interface GoogleBookMVP {

    interface Model {
        Observable<SearchResult> searchListOfBooks(String query);
    }

    interface View {
        void searhByQuery(String query);
        void showError();
        void showNoResults();
        void updateListView(SearchResult searchResult);
    }

    interface Presenter {
        void searchListOfBooks(String query);
    }

}
