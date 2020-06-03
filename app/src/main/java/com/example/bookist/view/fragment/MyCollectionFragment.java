package com.example.bookist.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookist.R;
import com.example.bookist.databinding.FragmentMyCollectionBinding;
import com.example.bookist.model.realm.po.RealmBook;
import com.example.bookist.mvp.CollectionMVP;
import com.example.bookist.presenter.MyCollectionPresenter;
import com.example.bookist.view.activity.MainActivity;
import com.example.bookist.view.adapter.CollectionRecyclerViewAdapter;
import com.example.bookist.view.listener.ClickListener;
import com.example.bookist.view.listener.LongClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import io.realm.RealmResults;

public class MyCollectionFragment extends Fragment
        implements CollectionMVP.GridView {

    private static CollectionMVP.GridView viewInstance;
    private static CollectionMVP.GridPresenter presenter;
    private RealmResults<RealmBook> realmBooks;
    private FragmentMyCollectionBinding binding;

    public static MyCollectionFragment getViewInstance() {
        if (viewInstance == null) {
            viewInstance = new MyCollectionFragment();

            if (presenter == null) {
                presenter = new MyCollectionPresenter(viewInstance);
            }
        }

        return (MyCollectionFragment) viewInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_my_collection,
                container,
                false
        );
        //((MainActivity) getActivity()).alreadyReadFragment = this;
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        presenter.getMyCollection();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(RealmResults<RealmBook> realmBooks) {
        this.realmBooks = realmBooks;
        this.updateGridView();
    }

    @Override
    public void updateGridView() {
        if (this.realmBooks != null) {

            CollectionRecyclerViewAdapter adapter = new CollectionRecyclerViewAdapter(
                    this.realmBooks,
                    realmBook -> {
                        if (getActivity() instanceof LongClickListener) {
                            LongClickListener listener = (LongClickListener) getActivity();
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
            this.binding.alreadyReadRvCollection.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            this.binding.alreadyReadRvCollection.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void removeBookFromMyCollection(RealmBook realmBook) {
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