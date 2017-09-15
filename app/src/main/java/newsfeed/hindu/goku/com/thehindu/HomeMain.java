package newsfeed.hindu.goku.com.thehindu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * Created by goku on 31-01-2015.
 */
public class HomeMain extends ActionBarActivity {

    private Toolbar toolbar;
    ImageView headerImage;
    KenBurnsView k;
    private static Drawable mActionBarBackgroundDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.hindu_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        k=(KenBurnsView)findViewById(R.id.image_header);
        k.setResourceIds(R.drawable.ken1,R.drawable.ken2,R.drawable.ken3);




        mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ab);
        mActionBarBackgroundDrawable.setAlpha(0);


        ImageView fake = (ImageView) findViewById(R.id.fake_toolbar);
        fake.setImageDrawable(mActionBarBackgroundDrawable);

        headerImage = (ImageView) findViewById(R.id.header_logo);
        ((NotifyingScrollView) findViewById(R.id.notififying_scrollview)).setOnScrollChangedListener(mOnScrollChangedListener);


    }

    int button = 0, change = 1;

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            final int headerHeight = findViewById(R.id.image_header).getHeight() - 56;
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            headerImage.setTranslationY(Math.max(-newAlpha, -250));
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
            if (button - 175 > t && change == 0) {
                findViewById(R.id.settings_fb).animate().cancel();
                findViewById(R.id.settings_fb).animate().translationYBy(-350).setDuration(80);
                findViewById(R.id.saved_fb).animate().cancel();
                findViewById(R.id.saved_fb).animate().translationYBy(-350).setDuration(80);
                change = 1;
                button = t;
            }
            if (button + 175 < t && change == 1) {
                findViewById(R.id.settings_fb).animate().cancel();
                findViewById(R.id.settings_fb).animate().translationYBy(350).setDuration(100);
                findViewById(R.id.saved_fb).animate().cancel();
                findViewById(R.id.saved_fb).animate().translationYBy(350).setDuration(100);
                change = 0;
                button = t;
            }


            Log.d("va", button + "  " + "  " + t);
            headerImage.setScaleY((float) Math.max(1 - ratio, 0.55));
            headerImage.setScaleX((float) Math.max(1 - ratio, 0.55));
        }
    };


    public void opensave(View v) {
        startActivity(new Intent(HomeMain.this, SavedArticles.class));
    }
    public void opensettings(View v) {
        startActivity(new Intent(HomeMain.this, Settings.class));
    }


    public void callactivity(View v) {
        String url = new String();
        String title = new String();
        if (v.getId() == R.id.cardhome) {
            url = "http://www.thehindu.com/?service=rss";
            title = "The Hindu";
        }
        if (v.getId() == R.id.cardinternational) {
            url = "http://www.thehindu.com/news/international/?service=rss";
            title = "International";
        }

        if (v.getId() == R.id.cardnational) {
            url = "http://www.thehindu.com/news/national/?service=rss";
            title = "National";
        }
        if (v.getId() == R.id.cardbusiness) {
            url = "http://www.thehindu.com/business/?service=rss";
            title = "Bunsiness";
        }
        if (v.getId() == R.id.cardsport) {
            url = "http://www.thehindu.com/sport/?service=rss";
            title = "Sport";
        }

        if (v.getId() == R.id.cardcriket) {
            title = "Cricket";
            url = "http://www.thehindu.com/sport/cricket/?service=rss";
        }
        if (v.getId() == R.id.cardfootball) {
            url = "http://www.thehindu.com/sport/football/?service=rss";
            title = "Footbal";
        }

        if (v.getId() == R.id.cardsci_tech) {
            title = "Science and Technology";
            url = "http://www.thehindu.com/sci-tech/?service=rss";
        }

        if (v.getId() == R.id.cardcinimaplus) {
            url = "http://www.thehindu.com/features/cinema/?service=rss";
            title = "Ciniplus";
        }

        if (v.getId() == R.id.cardeducation) {
            url = "http://www.thehindu.com/features/education/?service=rss";
            title = "Education";
        }

        if (v.getId() == R.id.cardmetro) {
            url = "http://www.thehindu.com/features/metroplus/?service=rss";
            title = "Metroplus";
        }

        if (v.getId() == R.id.cardentertainment) {
            url = "http://www.thehindu.com/entertainment/?service=rss";
            title = "Entertainment";

        }

        if (v.getId() == R.id.cardbooks) {
            title = "Books";
            url = "http://www.thehindu.com/books/?service=rss";
        }

        if (v.getId() == R.id.cardopinion) {
            title = "Opinion";
            url = "http://www.thehindu.com/opinion/?service=rss";
        }
        Intent main = new Intent(HomeMain.this, MainActivity.class);
        main.putExtra("url", url);
        main.putExtra("title", title);
        startActivity(main);
        finish();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}
