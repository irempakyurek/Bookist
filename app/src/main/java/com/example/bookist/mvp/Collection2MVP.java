package com.example.bookist.mvp;

import android.view.View;

import com.example.bookist.model.pojo.Book;
import com.example.bookist.model.realm.po.RealmBook2;

import io.realm.RealmResults;
import rx.Observable;

public interface Collection2MVP {

    interface Model {
        int PERSIST_PROBLEM = -1;
        int PERSIST_OK = 1;
        int BOOK_EXISTS_NOT_EXISTS = 0;

        int saveBook(Book book);
        RealmBook2 getBook(RealmBook2 book);
        Observable<RealmResults<RealmBook2>> getCollection();
        int removeBook(RealmBook2 realmBook);
        void openRealm();
        void closeRealm();
    }

    interface AddView {
        void addBookToCollection(View view);
    }

    interface GridView {
        void updateGridView();
        void removeBookFromMyCollection(RealmBook2 realmBook);
        void showError();
    }

    interface AddPresenter {
        int saveBook(Book book);
        void closeRealm();
    }

    interface GridPresenter {
        void getMyCollection();
        int removeBook(RealmBook2 realmBook);
        void closeRealm();
    }

}
