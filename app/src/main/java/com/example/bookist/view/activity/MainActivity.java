package com.example.bookist.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.bookist.R;
import com.example.bookist.model.pojo.Book;
import com.example.bookist.model.realm.po.RealmBook;
import com.example.bookist.model.realm.po.RealmBook2;
import com.example.bookist.model.realm.po.RealmBook3;
import com.example.bookist.view.fragment.GoogleBooksListFragment;
import com.example.bookist.view.fragment.MyCollectionFragment;
import com.example.bookist.view.fragment.MyCollectionReadingFragment;
import com.example.bookist.view.fragment.MyCollectionWantToReadFragment;
import com.example.bookist.view.listener.ClickListener;
import com.example.bookist.view.listener.LongClickListener;
import com.example.bookist.view.listener.LongClickListener2;
import com.example.bookist.view.listener.LongClickListener3;
import com.google.android.material.navigation.NavigationView;

import org.parceler.Parcels;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener,
        ClickListener, LongClickListener, LongClickListener2, LongClickListener3, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String SEARCH_ACTIVE = "searchActive";

    private SearchView searchView;
    private MyCollectionFragment myCollectionFragment;
    private MyCollectionReadingFragment myCollectionReadingFragment;
    private MyCollectionWantToReadFragment myCollectionWantToReadFragment;
    private DrawerLayout drawerLayout;
    public Fragment alreadyReadFragment, readingFragment, wantToReadFragment;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.myCollectionReadingFragment = MyCollectionReadingFragment.getViewInstance();
        this.myCollectionFragment = MyCollectionFragment.getViewInstance();
        this.myCollectionWantToReadFragment = MyCollectionWantToReadFragment.getViewInstance();


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
        }

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, myToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(this);

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
   //     this.showMyCollection();
    }

   private void showMyCollection() {
        if (!getIntent().getBooleanExtra(SEARCH_ACTIVE, false)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list, myCollectionFragment, "myCollection")
                    .commit();
        }

    }
    private void showMyCollection2() {
        if (!getIntent().getBooleanExtra(SEARCH_ACTIVE, false)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list, myCollectionReadingFragment, "myCollection2")
                    .commit();
        }
    }

    private void showMyCollection3() {
        if (!getIntent().getBooleanExtra(SEARCH_ACTIVE, false)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list, myCollectionWantToReadFragment, "myCollection3")
                    .commit();
        }
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getIntent().putExtra(SEARCH_ACTIVE, true);
        GoogleBooksListFragment bookListFragment = GoogleBooksListFragment.getViewInstance();

        if (!bookListFragment.isVisible() || !bookListFragment.isResumed()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list, bookListFragment, "searchList")
                    .commit();

            bookListFragment.searhByQuery(query);
        }

        this.searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                getIntent().putExtra(SEARCH_ACTIVE, false);

                if(alreadyReadFragment instanceof MyCollectionFragment) {
                        showMyCollection();
                }else if(readingFragment instanceof MyCollectionReadingFragment){
                    showMyCollection2();
                }else if(wantToReadFragment instanceof MyCollectionWantToReadFragment) {
                    showMyCollection3();
                }
                return true;

            }
        });
        this.searchView = (SearchView) searchItem.getActionView();
        this.searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public void onBookClick(Book book, boolean bundle) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        Parcelable parcelable = Parcels.wrap(book);
        intent.putExtra(BookDetailActivity.BOOK_OBJECT, parcelable);
        intent.putExtra(BookDetailActivity.SEARCH_DETAIL, bundle);
        startActivity(intent);
    }


    @Override
    public void onBookLongClick(RealmBook realmBook) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(String.format(
                getString(R.string.dialog_message), realmBook.getTitle()));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                (dialog, which) -> myCollectionFragment.removeBookFromMyCollection(realmBook));

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBookLongClick(RealmBook2 realmBook2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(String.format(
                getString(R.string.dialog_message), realmBook2.getTitle()));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                (dialog, which) -> myCollectionReadingFragment.removeBookFromMyCollection(realmBook2));

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onBookLongClick(RealmBook3 realmBook3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(String.format(
                getString(R.string.dialog_message), realmBook3.getTitle()));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                (dialog, which) -> myCollectionWantToReadFragment.removeBookFromMyCollection(realmBook3));

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onFloatButtonClick(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

            if (getIntent().getBooleanExtra(SEARCH_ACTIVE, false)) {
                if(alreadyReadFragment instanceof MyCollectionFragment) {
                    showMyCollection();
                }else if(readingFragment instanceof MyCollectionReadingFragment){
                    showMyCollection2();
                }else if(wantToReadFragment instanceof MyCollectionWantToReadFragment){
                    showMyCollection3();
                }
                getIntent().putExtra(SEARCH_ACTIVE, false);
            } else {
                super.onBackPressed();
            }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_alreadyRead:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_list, myCollectionFragment, "myCollection")
                            .commit();
                    break;
            case R.id.nav_reading:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_list, myCollectionReadingFragment, "myCollection2")
                        .commit();

                break;
            case R.id.nav_wantToRead:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_list, myCollectionWantToReadFragment, "myCollection3")
                        .commit();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //If logout button was clicked
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
}