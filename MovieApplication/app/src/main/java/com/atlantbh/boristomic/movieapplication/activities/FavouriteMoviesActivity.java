package com.atlantbh.boristomic.movieapplication.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.DrawerAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.FavouriteMovieAdapter;
import com.atlantbh.boristomic.movieapplication.models.DrawerItem;
import com.atlantbh.boristomic.movieapplication.models.MovieDB;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class FavouriteMoviesActivity extends AppCompatActivity {

    private final String LOG_TAG = FavouriteMoviesActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerItemTitles;
    private TypedArray drawerItemIcons;
    private List<DrawerItem> drawerItems;
    private DrawerAdapter drawerAdapter;

    private Toolbar toolbar;

    private List<MovieDB> movies;
    private FavouriteMovieAdapter favouriteMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerItemTitles = getResources().getStringArray(R.array.drawer_menu_titles);
        drawerItemIcons = getResources().obtainTypedArray(R.array.drawer_menu_icons);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItem(drawerItemTitles[0], drawerItemIcons.getResourceId(0, -1)));
        drawerItems.add(new DrawerItem(drawerItemTitles[1], drawerItemIcons.getResourceId(1, -1)));
        drawerItems.add(new DrawerItem(drawerItemTitles[2], drawerItemIcons.getResourceId(2, -1)));
        drawerItemIcons.recycle();
        drawerAdapter = new DrawerAdapter(drawerItems);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new SlideMenuClickListener());

        movies = MovieDB.findAllFavouriteMovies(this);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.favourite_movies_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouriteMovieAdapter = new FavouriteMovieAdapter(movies, this);
        recyclerView.setAdapter(favouriteMovieAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            MovieDB temp = favouriteMovieAdapter.getMovie(viewHolder.getAdapterPosition());
            Realm realm = Realm.getInstance(FavouriteMoviesActivity.this);
            MovieDB.updateMovieFavourite(realm, temp);
            favouriteMovieAdapter.updateUI();
            if (temp.getName() == null) {
                Toast.makeText(FavouriteMoviesActivity.this, temp.getTitle() + " removed from favourites.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FavouriteMoviesActivity.this, temp.getName() + " removed from favourites.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                drawerLayout.closeDrawers();
                startActivity(new Intent(FavouriteMoviesActivity.this, MainActivity.class));
                break;
            case 1:
                drawerLayout.closeDrawers();
                break;
            case 2:
                //              fragment = new PhotosFragment();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home) {
//            Log.v("navigacija", "dasdadada");
//            NavUtils.navigateUpFromSameTask(this);
//            return true;
//        }
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.v("navigacija", "dasdadada");

                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    // This activity is NOT part of this app's task, so create a new task
//                    // when navigating up, with a synthesized back stack.
//                    TaskStackBuilder.create(this)
//                            // Add all of this activity's parents to the back stack
//                            .addNextIntentWithParentStack(upIntent)
//                                    // Navigate up to the closest parent
//                            .startActivities();
//                } else {
//                    // This activity is part of this app's task, so simply
//                    // navigate up to the logical parent activity.
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
