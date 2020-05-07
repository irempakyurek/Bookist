package com.example.bookist.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bookist.R;
import com.example.bookist.databinding.ActivityDetailBookBinding;
import com.example.bookist.model.pojo.Book;
import com.example.bookist.mvp.Collection2MVP;
import com.example.bookist.mvp.Collection3MVP;
import com.example.bookist.mvp.CollectionMVP;
import com.example.bookist.presenter.AddCollectionPresenter;
import com.example.bookist.presenter.AddCollectionReadingPresenter;
import com.example.bookist.presenter.AddCollectionWantToReadPresenter;

import org.parceler.Parcels;

public class BookDetailActivity extends AppCompatActivity
        implements CollectionMVP.AddView, Collection2MVP.AddView , Collection3MVP.AddView, AdapterView.OnItemSelectedListener {

    public static final String BOOK_OBJECT = "bookObject";

    public static final String SEARCH_DETAIL = "searchDetail";

    private static CollectionMVP.AddPresenter presenter;
    private static Collection2MVP.AddPresenter presenter2;
    private static Collection3MVP.AddPresenter presenter3;

    private Book book;
    Button btnAdd, btnAdd2, btnAdd3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBookBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_detail_book);

        this.book = Parcels.unwrap(getIntent().getParcelableExtra(BOOK_OBJECT));
        binding.setBook(book);


        if (!getIntent().getBooleanExtra(SEARCH_DETAIL, false)) {
            binding.btAddCollection.setVisibility(View.INVISIBLE);
            binding.btAddCollection2.setVisibility(View.INVISIBLE);
            binding.btAddCollection3.setVisibility(View.INVISIBLE);
        }

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.selection_status_for_book, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        btnAdd =  findViewById(R.id.btAddCollection);
        btnAdd2 = findViewById(R.id.btAddCollection2);
        btnAdd3 = findViewById(R.id.btAddCollection3);
    }

    @Override
    public void addBookToCollection(View view) {
        btnAdd.setVisibility(View.VISIBLE);
        if (presenter == null) {
            presenter = new AddCollectionPresenter();
        }

        int status = presenter.saveBook(this.book);
        switch (status) {
            case CollectionMVP.Model.PERSIST_OK:
                Toast.makeText(this, R.string.msg_success_adding_collection, Toast.LENGTH_SHORT).show();
                break;
            case CollectionMVP.Model.PERSIST_PROBLEM:
                Toast.makeText(this, R.string.msg_error_adding_collection, Toast.LENGTH_SHORT).show();
                break;
            case CollectionMVP.Model.BOOK_EXISTS_NOT_EXISTS:
                Toast.makeText(this, R.string.msg_book_already_in_collection, Toast.LENGTH_SHORT).show();
                break;
        }

        presenter.closeRealm();
    }
    @Override
    public void addBookToCollection2(View view) {
        if (presenter2 == null) {
            presenter2 = new AddCollectionReadingPresenter();
        }

        int status2 = presenter2.saveBook(this.book);

        switch (status2) {
            case Collection2MVP.Model.PERSIST_OK:
                Toast.makeText(this, R.string.msg_success_adding_collection, Toast.LENGTH_SHORT).show();
                break;
            case Collection2MVP.Model.PERSIST_PROBLEM:
                Toast.makeText(this, R.string.msg_error_adding_collection, Toast.LENGTH_SHORT).show();
                break;
            case Collection2MVP.Model.BOOK_EXISTS_NOT_EXISTS:
                Toast.makeText(this, R.string.msg_book_already_in_collection, Toast.LENGTH_SHORT).show();
                break;
        }

        presenter2.closeRealm();
    }
    @Override
    public void addBookToCollection3(View view) {
        if (presenter3 == null) {
            presenter3 = new AddCollectionWantToReadPresenter();
        }

        int status2 = presenter3.saveBook(this.book);

        switch (status2) {
            case Collection2MVP.Model.PERSIST_OK:
                Toast.makeText(this, R.string.msg_success_adding_collection, Toast.LENGTH_SHORT).show();
                break;
            case Collection2MVP.Model.PERSIST_PROBLEM:
                Toast.makeText(this, R.string.msg_error_adding_collection, Toast.LENGTH_SHORT).show();
                break;
            case Collection2MVP.Model.BOOK_EXISTS_NOT_EXISTS:
                Toast.makeText(this, R.string.msg_book_already_in_collection, Toast.LENGTH_SHORT).show();
                break;
        }

        presenter3.closeRealm();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position == 0){
            btnAdd2.setVisibility(View.GONE);
            btnAdd3.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }else if(position == 1){
            btnAdd.setVisibility(View.GONE);
            btnAdd3.setVisibility(View.GONE);
            btnAdd2.setVisibility(View.VISIBLE);
        }else if(position == 2) {
            btnAdd.setVisibility(View.GONE);
            btnAdd2.setVisibility(View.GONE);
            btnAdd3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
