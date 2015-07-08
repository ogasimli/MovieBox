package org.ogasimli.MovieBox;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie movie;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie = extras.getParcelable(MainActivity.PACKAGE_NAME);
            //Log.e("Detail - movieTitle", movie.getMovieTitle());
        } else {
            throw new NullPointerException("No movie found in extras");
        }

        DetailFragment fragment = DetailFragment.getInstance(movie);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_container, fragment)
                .commit();
    }

    public static class DetailFragment extends Fragment {

        private String movieTitle;
        private String movieGenre;
        private String posterPath;
        private String backdropPath;
        private String movieId;
        private String movieOverview;
        private String movieReleaseDate;
        private double movieRating;
        private Context context;
        private Movie mMovie;

        public static DetailFragment getInstance(Movie movie) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(MainActivity.PACKAGE_NAME, movie);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mMovie = getArguments().getParcelable(MainActivity.PACKAGE_NAME);
            if (mMovie == null) {
                throw new NullPointerException("Movie object should be put into fragment arguments.");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            ImageView detailBackdropImage = (ImageView) rootView.findViewById(R.id.backdrop_image);
            ImageView detailPosterImage = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
            TextView detailMovieTitle = (TextView) rootView.findViewById(R.id.detail_title_text);
            TextView detailMovieGenre = (TextView) rootView.findViewById(R.id.detail_genre_text);
            TextView detailMovieRelease = (TextView) rootView.findViewById(R.id.detail_release_text);
            TextView detailMovieRating = (TextView) rootView.findViewById(R.id.detail_rating_text);
            RatingBar detailRatingBar = (RatingBar) rootView.findViewById(R.id.detail_rating_bar);
            TextView detailMovieOverview = (TextView) rootView.findViewById(R.id.detail_overview_text);
            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

            //Change the color of ratingBar
            LayerDrawable stars = (LayerDrawable) detailRatingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(rootView.getResources().getColor(R.color.accent_color), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(rootView.getResources().getColor(R.color.light_primary_color), PorterDuff.Mode.SRC_ATOP);

/*            String packageName = MainActivity.PACKAGE_NAME;
            Intent intent = getActivity().getIntent();
            if (intent!=null){
                movieTitle = intent.getStringExtra(packageName + ".movieTitle");
                movieGenre = intent.getStringExtra(packageName + ".movieGenre");
                posterPath = intent.getStringExtra(packageName + ".posterPath");
                backdropPath = intent.getStringExtra(packageName + ".backdropPath");
                movieId = intent.getStringExtra(packageName + ".movieId");
                movieOverview = intent.getStringExtra(packageName + ".movieOverview");
                movieReleaseDate = intent.getStringExtra(packageName + ".movieReleaseDate");
                movieRating = intent.getDoubleExtra(packageName + ".movieRating", 0);
            }*/


            detailMovieTitle.setText(mMovie.getMovieTitle());
            detailMovieGenre.setText(mMovie.getMovieGenre());
            detailMovieRelease.setText(mMovie.getMovieReleaseDate());
            String rating = String.format(rootView.getResources().getString(R.string.detail_rating),String.valueOf(mMovie.getMovieRating()));
            detailMovieRating.setText(rating);
            detailRatingBar.setRating((float) mMovie.getMovieRating());
            context = detailPosterImage.getContext();
            Glide.with(context).
                    load(context.getString(R.string.base_poster_link) + "w185/" + mMovie.getPosterPath()).
                    diskCacheStrategy(DiskCacheStrategy.ALL).
                    into(detailPosterImage);
            context = detailBackdropImage.getContext();
            Glide.with(context).
                    load(context.getString(R.string.base_poster_link) + "w500/" + mMovie.getBackdropPath()).
                    diskCacheStrategy(DiskCacheStrategy.ALL).
                    into(detailBackdropImage);
            detailMovieOverview.setText(mMovie.getMovieOverview());

            Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.grow);
            fab.startAnimation(animation);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            context.getString(R.string.fab_message),
                            Toast.LENGTH_SHORT).show();
                }
            });

            return rootView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Reverse the scene transition by home button press
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
