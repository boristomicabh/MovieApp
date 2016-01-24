package com.atlantbh.boristomic.movieapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.MovieAdapter;
import com.atlantbh.boristomic.movieapplication.models.Actor;
import com.atlantbh.boristomic.movieapplication.models.ActorImages;
import com.atlantbh.boristomic.movieapplication.models.Backdrop;
import com.atlantbh.boristomic.movieapplication.models.Credits;
import com.atlantbh.boristomic.movieapplication.models.Images;
import com.atlantbh.boristomic.movieapplication.services.MovieAPI;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.parchment.widget.adapterview.listview.ListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActorActivity extends AppCompatActivity {

    private final String LOG_TAG = ActorActivity.class.getSimpleName();
    private MovieAPI api;
    private long movieId;

    @Bind(R.id.actor_first_image)
    ImageView actorFirstImage;
    @Bind(R.id.actor_name)
    TextView actorName;
    @Bind(R.id.actor_year_location)
    TextView actorAgeLocation;
    @Bind(R.id.actor_poster)
    ImageView actorPoster;
    @Bind(R.id.actor_biography)
    TextView actorBiography;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        ButterKnife.bind(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        api = RestService.get();

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
//
//        Log.v("VELICINA EKRANA", "sirina: " + String.valueOf(width) + " visina: " + String.valueOf(height));



        final Intent intent = getIntent();
        long actorId = intent.getLongExtra(Constants.INTENT_KEY, -1);
        movieId = intent.getLongExtra(Constants.INTENT_KEY_TYPE_MOVIE, -1);

        api.findActorImages(actorId, new Callback<ActorImages>() {

            @Override
            public void success(ActorImages actorImages, Response response) {
                List<Backdrop> bc = actorImages.getResults();
                Images images = new Images();
                images.setBackdrops(bc);
                //ImageAdapter imageAdapter = new ImageAdapter(images, 100);
                //final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.actor_image_gallery);
                //horizontalListView.setAdapter(imageAdapter);

                for (Backdrop b : bc) {
                    if(b.getWidth() > b.getHeight()) {
                        Picasso.with(ActorActivity.this).load(Constants.URL_BASE_IMG + Constants.BACKDROP_SIZE_W1280 + b.getFilePath()).into(actorFirstImage);
                        break;
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load actor images", error);
            }
        });

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


        api.findActorMovies(actorId, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                MovieAdapter creditsAdapter = new MovieAdapter(null, null, credits, Constants.ACTOR_MOVIES);
                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.actor_movies_list);
                horizontalListView.setAdapter(creditsAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load actor", error);

            }
        });

        api.findActorTVShows(actorId, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                MovieAdapter creditsAdapter = new MovieAdapter(null, null, credits, Constants.ACTOR_TV_SHOWS);
                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.actor_tv_shows_list);
                horizontalListView.setAdapter(creditsAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load actor", error);

            }
        });


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
