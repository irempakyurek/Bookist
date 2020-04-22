package com.example.bookist.presenter;

import com.example.bookist.model.dao.Collection2DAO;
import com.example.bookist.model.pojo.Book;
import com.example.bookist.mvp.Collection2MVP;

public class AddCollectionReadingPresenter implements Collection2MVP.AddPresenter {

    private Collection2MVP.Model model;

    public AddCollectionReadingPresenter() {
        this.model = new Collection2DAO();
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
