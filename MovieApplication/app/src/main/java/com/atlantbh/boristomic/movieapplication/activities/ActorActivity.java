package com.atlantbh.boristomic.movieapplication.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.DrawerAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.HorizontalListAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.SlideshowPagerAdapter;
import com.atlantbh.boristomic.movieapplication.listeners.DrawerMenuItemClicked;
import com.atlantbh.boristomic.movieapplication.models.DrawerItem;
import com.atlantbh.boristomic.movieapplication.models.rest.Actor;
import com.atlantbh.boristomic.movieapplication.models.rest.ActorImages;
import com.atlantbh.boristomic.movieapplication.models.rest.Backdrop;
import com.atlantbh.boristomic.movieapplication.models.rest.Credits;
import com.atlantbh.boristomic.movieapplication.services.MovieAPI;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Connection;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActorActivity extends AppCompatActivity {

    private final String LOG_TAG = ActorActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerItemTitles;
    private TypedArray drawerItemIcons;
    private List<DrawerItem> drawerItems;
    private DrawerAdapter drawerAdapter;

    private MovieAPI api;

    @Bind(R.id.actor_name)
    protected TextView actorName;
    @Bind(R.id.actor_poster)
    protected ImageView actorPoster;
    @Bind(R.id.actor_biography)
    protected TextView actorBiography;
    @Bind(R.id.actor_year_location)
    protected TextView actorAgeLocation;
    @Bind(R.id.view_pager_backdrop_image)
    protected ViewPager viewPager;
    @Bind(R.id.pager_indicator)
    protected CirclePageIndicator indicator;
    @Bind(R.id.default_backdrop_image)
    protected ImageView defaultBackdrop;
    @Bind(R.id.actor_movies_list)
    protected RecyclerView moviesList;
    @Bind(R.id.actor_tv_shows_list)
    protected RecyclerView tvShowList;
    @Bind(R.id.view_pager_layout)
    protected RelativeLayout viewPagerLayout;

    private Toolbar toolbar;

    /**
     * Sets layout activity_actor.xml, adds toolbar with up button action.
     * Then gets API connection and actor id from intent on witch the search will be executed.
     * Searches for actor images, specifically images where actor was tagged and uses only first landscape image.
     * Searches for actor and setups basic information about him/her.
     * Searches for movies were actor appeared and shows them in a list,
     * then searches for tv shows where actor appeared and also shows them in a list.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        api = RestService.get();

        final Intent intent = getIntent();
        long actorId = intent.getLongExtra(Constants.INTENT_KEY, -1);

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
        drawerList.setOnItemClickListener(new DrawerMenuItemClicked(drawerLayout, getBaseContext()));

        if (!Connection.isConnected(this)) {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
        } else {
            populateActorBackdropImage(actorId);
            populateActorData(actorId);
            populateActorMovies(actorId);
            populateActorTvShows(actorId);
        }
    }

    /**
     * Calls api method that finds all tv shows for given actor id, and then sets shows into a list
     *
     * @param actorId <code>long</code> type value of actor id
     */
    private void populateActorTvShows(long actorId) {

        api.findActorTVShows(actorId, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                tvShowList.setLayoutManager(new LinearLayoutManager(ActorActivity.this, LinearLayoutManager.HORIZONTAL, false));
                tvShowList.setAdapter(new HorizontalListAdapter(credits.getCast(), Constants.TV_SHOWS, ActorActivity.this));
//                MovieAdapterOld creditsAdapter = new MovieAdapterOld(null, null, credits, Constants.ACTOR_TV_SHOWS, 0);
//                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.actor_tv_shows_list);
//                horizontalListView.setAdapter(creditsAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load actor", error);

            }
        });
    }

    /**
     * Calls api method that finds all movie for given actor id, and then sets movies into a list
     *
     * @param actorId <code>long</code> type value of actor id
     */
    private void populateActorMovies(long actorId) {

        api.findActorMovies(actorId, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                moviesList.setLayoutManager(new LinearLayoutManager(ActorActivity.this, LinearLayoutManager.HORIZONTAL, false));
                moviesList.setAdapter(new HorizontalListAdapter(credits.getCast(), Constants.MOVIE, ActorActivity.this));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load actor", error);

            }
        });
    }

    /**
     * Calls api method that finds actor data for given actor id, and then sets received data into appropriate views
     *
     * @param actorId <code>long</code> type value of actor id
     */
    private void populateActorData(long actorId) {

        api.findActor(actorId, new Callback<Actor>() {
            @Override
            public void success(Actor actor, Response response) {
                toolbar.setTitle(actor.getName());
                actorName.setText(actor.getName());
                actorAgeLocation.setText(MovieUtils.getActorBirthData(actor));
                Picasso.with(ActorActivity.this).load(Constants.URL_BASE_IMG + Constants.POSTER_SIZE_W342 + actor.getProfilePath()).into(actorPoster);
                actorBiography.setText(actor.getBiography());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load actor", error);
            }
        });
    }

    /**
     * Calls api method that finds all actor tagged images for given actor id, and then sets first landscape image into a view
     *
     * @param actorId <code>long</code> type value of actor id
     */
    private void populateActorBackdropImage(long actorId) {

        api.findActorImages(actorId, new Callback<ActorImages>() {

            @Override
            public void success(ActorImages actorImages, Response response) {
                if (actorImages.getResults().size() == 0) {
                    setupDefaultBackdrop();
                } else {
                    setupRealBackdrop(actorImages);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load actor images", error);
            }
        });
    }

    /**
     * Sets the default backdrop image and disables view pager
     */
    private void setupDefaultBackdrop() {
        defaultBackdrop.setVisibility(View.VISIBLE);
        viewPagerLayout.setVisibility(View.GONE);
        Picasso.with(ActorActivity.this).load(R.drawable.backdrop_default).into(defaultBackdrop);
    }

    /**
     * Upon retrieving data from API, only landscape images are taken into a list,
     * then a list is checked, and if there is not images, default one is shown.
     *
     * @param actorImages <code>ActorImages</code> type object retrieved from API
     */
    private void setupRealBackdrop(ActorImages actorImages) {
        List<Backdrop> bc = new ArrayList<>();
        for (Backdrop b : actorImages.getResults()) {
            if (b.getWidth() > b.getHeight()) {
                bc.add(b);
            }
        }

        if (bc.size() == 0) {
            setupDefaultBackdrop();
        } else {
            List<Object> objects = new ArrayList<>();
            objects.addAll(bc);
            SlideshowPagerAdapter slideshowPagerAdapter = new SlideshowPagerAdapter(ActorActivity.this, objects, getSupportFragmentManager());
            viewPager.setAdapter(slideshowPagerAdapter);
            indicator.setViewPager(viewPager);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
