package newsfeed.hindu.goku.com.thehindu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PostNotificationActivity extends ActionBarActivity {


    String url, title, backupurl, backuptitle;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences prefs;
    private Toolbar toolbar;
    private Menu menu;
    TextView tv, tv2;
    private ProgressDialog progress;
    Intent intent2 = new Intent(Intent.ACTION_SEND);
    private SaveDataSource datasource;

    String desc = "";
    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        datasource = new SaveDataSource(this);
        datasource.open();


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.setContentView(R.layout.postview);
        tv = (TextView) findViewById(R.id.webTitle);
        tv2 = (TextView) findViewById(R.id.webText);
        changetheme();
        changetext();
        Typeface title_font = Typeface.createFromAsset(getAssets(), "fonts/title_bold.otf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() &&
                networkInfo.isConnected()) {

        } else {
            Toast.makeText(this, "Please refresh,Error while connecting to internet", Toast.LENGTH_SHORT).show();
        }

        getSupportActionBar().setTitle("The Hindu");
        Bundle bundle = this.getIntent().getExtras();
        url = bundle.getString("content");
        backupurl = bundle.getString("backupurl");
        backuptitle = bundle.getString("backuptitle");
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();


        title = bundle.getString("title");
        TextView t = (TextView) findViewById(R.id.webTitle);
        t.setText(title);
        t.setTypeface(title_font);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeView);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.blue, R.color.green);
        swipeRefreshLayout.setRefreshing(true);

        new Title().execute(url);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Title().execute(url);

            }
        });


    }

    private void changetext() {

        String textsize = prefs.getString("text_size", "2");
        switch (textsize) {
            case "1":
                tv.setTextSize(19);
                tv2.setTextSize(14);
                break;
            case "3":
                tv.setTextSize(21);
                tv2.setTextSize(16);
                break;
            case "4":
                tv.setTextSize(22);
                tv2.setTextSize(17);
                break;
        }
    }


    private void changetheme() {
        if (prefs.getBoolean("app_theme", true)) {

            findViewById(R.id.postview_linearlayout1).setBackgroundColor(getResources().getColor(R.color.post_linear));

            findViewById(R.id.swipeView).setBackgroundColor(getResources().getColor(R.color.post_swipe));
            findViewById(R.id.postview_scroll).setBackgroundColor(getResources().getColor(R.color.post_scroll));
            findViewById(R.id.postview_linearlayout2).setBackgroundColor(getResources().getColor(R.color.post_linear2));


            tv.setBackgroundColor(getResources().getColor(R.color.post_titlebg));
            tv.setTextColor(getResources().getColor(R.color.post_title));

            tv2.setBackgroundColor(getResources().getColor(R.color.post_textbg));
            tv2.setTextColor(getResources().getColor(R.color.post_text));

            findViewById(R.id.view_postview).setBackgroundColor(getResources().getColor(R.color.post_divider));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, "Read this article at  " + url);
            startActivity(Intent.createChooser(intent2, "Share via"));
            return true;
        }
        if (id == android.R.id.home) {
            Intent main = new Intent(PostNotificationActivity.this, HomeMain.class);
            main.putExtra("url", backupurl);
            main.putExtra("title", backuptitle);
            startActivity(main);
        }
        if (id == R.id.action_save) {

            Save save = null;
            save = datasource.createSave(title, desc, imgUrl);
            Toast.makeText(this, "Content saved", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    // Title AsyncTask
    private class Title extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(params[0]).timeout(10 * 1000).userAgent("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36").get();


                Elements description = document.select("p.body");
                for (Element e : description) {
                    desc += "\n\t\t" + e.text().trim() + "\n";
                }

                // Locate the content attribute
                //  desc = description.attr("content");
                // Get the html document title
                // title = document.title();
                Elements img = document.getElementsByClass("main-image");
                // Locate the src attribute
                if (img.size() != 0) {
                    imgUrl = img.attr("src");
                    // Download image from URL

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            TextView txttitle = (TextView) findViewById(R.id.webText);
            ImageView imageView = (ImageView) findViewById(R.id.webImage);

            Typeface content_font = Typeface.createFromAsset(getAssets(), "fonts/content_light.ttf");

            txttitle.setTypeface(content_font);
            if (desc.length() > 0) {

                menu.clear();

                getMenuInflater().inflate(R.menu.menu_save, menu);
            } else {
                Toast.makeText(PostNotificationActivity.this, "Please refresh", Toast.LENGTH_SHORT).show();
            }

            Picasso.with(PostNotificationActivity.this).load(imgUrl).placeholder(R.drawable.hindu).into(imageView);


            txttitle.setText("\t\t" + desc.trim() + "\n");
            if (progress.isShowing())
                progress.dismiss();
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        }
    }


}
