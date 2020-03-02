package com.example.bookist.view.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookist.R;
import com.example.bookist.databinding.ActivityDetailBookBinding;
import com.example.bookist.databinding.FragmentListBookBinding;
import com.example.bookist.model.pojo.Book;
import com.example.bookist.model.pojo.SearchResult;
import com.example.bookist.mvp.GoogleBookMVP;
import com.example.bookist.presenter.GoogleBooksPresenter;
import com.example.bookist.view.adapter.GoogleBookAdapter;
import com.example.bookist.view.listener.ClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;


public class GoogleBooksListFragment extends Fragment
        implements GoogleBookMVP.View {

    private static GoogleBookMVP.View viewInstance;
    private static GoogleBookMVP.Presenter presenter;
    private List<Book> listBooks;
    private GoogleBookAdapter adapter;
    private FragmentListBookBinding binding;

    public static GoogleBooksListFragment getViewInstance() {
        if (viewInstance == null) {
            viewInstance = new GoogleBooksListFragment();

            if (presenter == null) {
                presenter = new GoogleBooksPresenter(viewInstance);
            }
        }

        return (GoogleBooksListFragment) viewInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        listBooks = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_list_book,
                container,
                false);
        adapter = new GoogleBookAdapter(listBooks,
                (book, bundle) -> {
                    if (getActivity() instanceof ClickListener) {
                        ClickListener listener = (ClickListener) getActivity();
                        listener.onBookClick(book, bundle);
                    }
                });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void searhByQuery(String query){
        presenter.searchListOfBooks(query);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(SearchResult event) {
        this.updateListView(event);
        EventBus.getDefault().removeStickyEvent(this);
    }

    @Override
    public void updateListView(SearchResult searchResult){
        if (searchResult != null) {
            this.listBooks.clear();

            if (searchResult.getListBooks() != null && searchResult.getListBooks().size() > 0) {
                this.listBooks.addAll(searchResult.getListBooks());
                binding.rvBooks.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                this.showNoResults();
            }

            return;
        }

        this.showError();
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity(), R.string.msg_error_search_books, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoResults() {
        Toast.makeText(getActivity(), R.string.msg_warning_no_results, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
