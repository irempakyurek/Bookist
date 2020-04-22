package com.example.bookist.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.bookist.R;
import com.example.bookist.databinding.FragmentMyReadingCollectionBinding;
import com.example.bookist.model.realm.po.RealmBook2;
import com.example.bookist.mvp.Collection2MVP;
import com.example.bookist.presenter.MyCollectionReadingPresenter;
import com.example.bookist.view.activity.MainActivity;
import com.example.bookist.view.adapter.CollectionReadingRecyclerViewAdapter;
import com.example.bookist.view.listener.ClickListener;
import com.example.bookist.view.listener.LongClickListener2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.RealmResults;

public class MyCollectionReadingFragment extends Fragment
        implements Collection2MVP.GridView {

    private static Collection2MVP.GridView viewInstance;
    private static Collection2MVP.GridPresenter presenter;
    private RealmResults<RealmBook2> realmBooks;
    private FragmentMyReadingCollectionBinding binding;

    public static MyCollectionReadingFragment getViewInstance() {
        if (viewInstance == null) {
            viewInstance = new MyCollectionReadingFragment();

            if (presenter == null) {
                presenter = new MyCollectionReadingPresenter(viewInstance);
            }
        }

        return (MyCollectionReadingFragment) viewInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_my_reading_collection,
                container,
                false
        );
        ((MainActivity) getActivity()).readingFragment = this;
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        presenter.getMyCollection();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(RealmResults<RealmBook2> realmBooks) {
        this.realmBooks = realmBooks;
        this.updateGridView();
    }


    @Override
    public void updateGridView() {
        if (this.realmBooks != null) {

            CollectionReadingRecyclerViewAdapter adapter = new CollectionReadingRecyclerViewAdapter(
                    this.realmBooks,
                    realmBook -> {
                        if (getActivity() instanceof LongClickListener2) {
                            LongClickListener2 listener = (LongClickListener2) getActivity();
                            listener.onBookLongClick(realmBook);
                        }
                    },
                    (book, bundle) -> {
                        if (getActivity() instanceof ClickListener) {
                            ClickListener listener = (ClickListener) getActivity();
                            listener.onBookClick(book, bundle);
                        }
                    }
            );
            this.binding.readingRvCollection.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            this.binding.readingRvCollection.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void removeBookFromMyCollection(RealmBook2 realmBook) {
        presenter.removeBook(realmBook);
        updateGridView();
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity(), R.string.msg_error_search_books, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.closeRealm();
    }

}