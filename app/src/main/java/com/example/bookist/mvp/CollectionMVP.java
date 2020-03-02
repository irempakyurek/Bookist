package com.example.bookist.mvp;

import android.view.View;

import com.example.bookist.model.pojo.Book;
import com.example.bookist.model.realm.po.RealmBook;

import io.realm.RealmResults;
import rx.Observable;

public interface CollectionMVP {

    interface Model {
        int PERSIST_PROBLEM = -1;
        int PERSIST_OK = 1;
        int BOOK_EXISTS_NOT_EXISTS = 0;

        int saveBook(Book book);
        RealmBook getBook(RealmBook book);
        Observable<RealmResults<RealmBook>> getCollection();
        int removeBook(RealmBook realmBook);
        void openRealm();
        void closeRealm();
    }

    interface AddView {
        void addBookToCollection(View view);
    }

    interface GridView {
        void updateGridView();
        void removeBookFromMyCollection(RealmBook realmBook);
        void showError();
    }

    interface AddPresenter {
        int saveBook(Book book);
        void closeRealm();
    }

    interface GridPresenter {
        void getMyCollection();
        int removeBook(RealmBook realmBook);
        void closeRealm();
    }

}
