package com.atlantbh.boristomic.movieapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.DrawerAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.MovieSearchAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.ViewPagerAdapter;
import com.atlantbh.boristomic.movieapplication.fragments.MovieListFragment;
import com.atlantbh.boristomic.movieapplication.listeners.DrawerMenuItemClicked;
import com.atlantbh.boristomic.movieapplication.models.DrawerItem;
import com.atlantbh.boristomic.movieapplication.models.rest.Movie;
import com.atlantbh.boristomic.movieapplication.models.rest.MoviesResponse;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Connection;
import com.atlantbh.boristomic.movieapplication.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private String[] drawerItemTitles;
    private TypedArray drawerItemIcons;
    private List<DrawerItem> drawerItems;
    private DrawerAdapter drawerAdapter;

    private ListView searchListResults;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static Context context;
    private List<Movie> searchedMovies = new ArrayList<>();

    private int screenWidth;

    /**
     * Sets context to this activity, sets toolbar and it's title.
     * Finds search list results view and gets api services,
     * then populates category lists with appropriate data from api.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        searchListResults = (ListView) findViewById(R.id.search_results_list);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        // new stuff
        drawerTitle = getTitle();
        drawerItemTitles = getResources().getStringArray(R.array.drawer_menu_titles);
        drawerItemIcons = getResources().obtainTypedArray(R.array.drawer_menu_icons);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItem(drawerItemTitles[0], drawerItemIcons.getResourceId(0, -1)));
        drawerItems.add(new DrawerItem(drawerItemTitles[1], drawerItemIcons.getResourceId(1, -1)));
        drawerItems.add(new DrawerItem(drawerItemTitles[2], drawerItemIcons.getResourceId(2, -1)));
        drawerItemIcons.recycle();
        drawerAdapter = new DrawerAdapter(drawerItems);
        drawerList.setAdapter(drawerAdapter);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawerList.setOnItemClickListener(new DrawerMenuItemClicked(drawerLayout, getContext()));

        if (!Connection.isConnected(this)) {
            Toast.makeText(this, "You don't have internet connection, browse favourites.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, FavouriteMoviesActivity.class));
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MovieListFragment.newInstance(Constants.MOST_POPULAR_MOVIES_LIST), "Most Popular Movies");
        adapter.addFragment(MovieListFragment.newInstance(Constants.TOP_RATED_MOVIES_LIST), "Top Rated Movies");
        adapter.addFragment(MovieListFragment.newInstance(Constants.UPCOMING_MOVIES_LIST), "Upcoming Movies");
        adapter.addFragment(MovieListFragment.newInstance(Constants.MOST_POPULAR_TV_SHOWS_LIST), "Most Popular TV Shows");
        adapter.addFragment(MovieListFragment.newInstance(Constants.TOP_RATED_TV_SHOWS_LIST), "Top Rated TV Shows");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        searchedMovies.clear();
        super.onBackPressed();
    }


    /**
     * Finds menu from menu resources, finds search view and creates adapter for it with list of movies.
     * When view is opened query text listener is setup and on every typed key api request is processed,
     * if search query is submitted and some data is found most relevant is displayed, if nothing is found
     * toast with message is displayed.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem searchItem = menu.findItem(R.id.search_movie);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        final MovieSearchAdapter searchAdapter = new MovieSearchAdapter(searchedMovies);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        if (!searchView.isShown()) {
            searchView.onActionViewCollapsed();
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(final String newText) {
                    searchMovies(newText);
                    searchListResults.setAdapter(searchAdapter);
                    searchListResults.bringToFront();
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchMovies(query);
                    if (searchedMovies != null && searchedMovies.size() > 1) {
                        Intent intent = new Intent(context, MovieActivity.class);
                        intent.putExtra(Constants.INTENT_KEY, searchedMovies.get(0).getId());
                        startActivity(intent);
                        return true;
                    }
                    Toast.makeText(context, "Nothing found", Toast.LENGTH_LONG).show();
                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Calls api method that searches for movies.
     * If search is successful list of movies is updated, otherwise list of movies is deleted.
     *
     * @param newText <code>String</code> type value of query
     */
    private void searchMovies(String newText) {

        RestService.get().findAnyMovie(newText, new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                searchedMovies.clear();
                searchedMovies.addAll(moviesResponse.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                searchedMovies.clear();
                Log.e(LOG_TAG, "Failed to find any films", error);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns context for static use
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }


}

