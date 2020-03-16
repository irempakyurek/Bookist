package com.example.bookist.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bookist.R;
import com.example.bookist.model.pojo.Book;
import com.example.bookist.model.realm.po.RealmBook;
import com.example.bookist.view.fragment.GoogleBooksListFragment;
import com.example.bookist.view.fragment.MyCollectionFragment;
import com.example.bookist.view.listener.ClickListener;
import com.example.bookist.view.listener.LongClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.parceler.Parcels;

import androidx.appcompat.widget.Toolbar;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener,
        ClickListener, LongClickListener {

    public static final String SEARCH_ACTIVE = "searchActive";

    private SearchView searchView;
    private MyCollectionFragment myCollectionFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.myCollectionFragment = MyCollectionFragment.getViewInstance();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
        }
/*
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        this.showMyCollection();
    }

    private void showMyCollection() {
        if (!getIntent().getBooleanExtra(SEARCH_ACTIVE, false)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list, myCollectionFragment, "myCollection")
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
                showMyCollection();
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

    public void onFloatButtonClick(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    public void onBackPressed() {
/*        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/


        if (getIntent().getBooleanExtra(SEARCH_ACTIVE, false)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list, myCollectionFragment, "myCollection")
                    .commit();

            getIntent().putExtra(SEARCH_ACTIVE, false);
        } else {
            super.onBackPressed();
        }
    }

/*    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item_one) {
            Toast.makeText(getApplicationContext(), "Okunmuşları seçtiniz", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_item_two) {
            Toast.makeText(getApplicationContext(), "Şuanda okunanları seçtiniz.", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_item_three) {
            Toast.makeText(getApplicationContext(), "Okunmak istenenleri seçtiniz.", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

}
