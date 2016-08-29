package com.codepath.apps.tweetsapp.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.codepath.apps.tweetsapp.R;

/**
 * Created by carolinewong on 8/28/16.
 */
public class BaseActivity extends AppCompatActivity {
    MenuItem miActionProgressItem;
    MenuItem miActionSearch;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        miActionSearch = menu.findItem(R.id.miActionSearch);
        miActionProgressItem = menu.findItem(R.id.miActionProgress);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(miActionSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                Intent i = new Intent(BaseActivity.this, SearchActivity.class);
                i.putExtra("search", query);
                startActivity(i);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
    }

    public void hideProgressBar() {
        // Hide progress item
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(false);
        }
    }

    public void onProfileView(MenuItem menuItem) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void goToHomeTimeline(MenuItem menuItem) {
        Intent i = new Intent(this, TimelineActivity.class);
        startActivity(i);
    }
}
