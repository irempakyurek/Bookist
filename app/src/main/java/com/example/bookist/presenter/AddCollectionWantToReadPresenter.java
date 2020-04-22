package com.example.bookist.presenter;

import com.example.bookist.model.dao.Collection3DAO;
import com.example.bookist.model.dao.CollectionDAO;
import com.example.bookist.model.pojo.Book;
import com.example.bookist.mvp.Collection3MVP;
import com.example.bookist.mvp.CollectionMVP;

public class AddCollectionWantToReadPresenter implements Collection3MVP.AddPresenter {

    private Collection3MVP.Model model;

    public AddCollectionWantToReadPresenter() {
        this.model = new Collection3DAO();
    }

    @Override
    public int saveBook(Book book) {
        return this.model.saveBook(book);
    }

    @Override
    public void closeRealm() {
        this.model.closeRealm();
    }

}
