package com.example.bookist.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bookist.R;
import com.example.bookist.databinding.ActivityDetailBookBinding;
import com.example.bookist.model.pojo.Book;
import com.example.bookist.mvp.CollectionMVP;
import com.example.bookist.presenter.AddCollectionPresenter;

import org.parceler.Parcels;

public class BookDetailActivity extends AppCompatActivity
        implements CollectionMVP.AddView, AdapterView.OnItemSelectedListener {

    public static final String BOOK_OBJECT = "bookObject";

    public static final String SEARCH_DETAIL = "searchDetail";

    private static CollectionMVP.AddPresenter presenter;

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBookBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_detail_book);

        this.book = Parcels.unwrap(getIntent().getParcelableExtra(BOOK_OBJECT));
        binding.setBook(book);

        if (!getIntent().getBooleanExtra(SEARCH_DETAIL, false)) {
            binding.btAddCollection.setVisibility(View.INVISIBLE);
        }

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.selection_status_for_book, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void addBookToCollection(View view) {
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = "Okunmuş";
        String text2 = "Şuanda okunan";
        String text3 = "Okunmak istenen";
        if(position == 0){
            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        }else if(position == 1){
            Toast.makeText(parent.getContext(), text2, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(parent.getContext(), text3, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
