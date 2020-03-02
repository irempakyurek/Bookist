package com.example.bookist.presenter;

import com.example.bookist.model.dao.CollectionDAO;
import com.example.bookist.model.pojo.Book;
import com.example.bookist.mvp.CollectionMVP;

public class AddCollectionPresenter implements CollectionMVP.AddPresenter {

    private CollectionMVP.Model model;

    public AddCollectionPresenter() {
        this.model = new CollectionDAO();
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
