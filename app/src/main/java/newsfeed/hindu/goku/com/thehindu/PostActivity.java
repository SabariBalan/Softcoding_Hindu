package newsfeed.hindu.goku.com.thehindu;

/**
 * Created by goku on 13-01-2015.
 */

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
import android.util.Log;
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

public class PostActivity extends ActionBarActivity {


    private Menu menu;
    String url, title, backupurl, backuptitle;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences prefs;
    private Toolbar toolbar;
    TextView tv, tv2;
    Title task;
    private ProgressDialog progress;
    Intent intent2 = new Intent(Intent.ACTION_SEND);

    String desc = "";

    String imgSrc;
    private SaveDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        datasource = new SaveDataSource(this);
        datasource.open();

        this.setContentView(R.layout.postview);
        tv = (TextView) findViewById(R.id.webTitle);
        tv2 = (TextView) findViewById(R.id.webText);
        Typeface title_font = Typeface.createFromAsset(getAssets(), "fonts/title_bold.otf");


        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() &&
                networkInfo.isConnected()) {

        } else {
            Toast.makeText(this, "Please refresh,Error while connecting to internet", Toast.LENGTH_SHORT).show();
        }
        changetheme();
        changetext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setAlpha(100);

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

        task = new Title();
        task.execute(url);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (task.getStatus().equals(AsyncTask.Status.RUNNING)) {
                    Log.d("debug", "post Activity thread canceled ");

                    task.cancel(true);
                }

                task = new Title();
                task.execute(url);

            }
        });


    }

    @Override
    protected void onStop() {
        if (task.getStatus().equals(AsyncTask.Status.RUNNING)) {
            Log.d("debug", "post Activity thread canceled ");

            task.cancel(true);
        }
        Log.d("debug", "post Activ on stop ");
        super.onStop();
    }


    private void changetext() {

        String textsize = prefs.getString("text_size", "2");
        switch (textsize) {
            case "1":
                tv.setTextSize(21);
                tv2.setTextSize(16);
                break;
            case "3":
                tv.setTextSize(23);
                tv2.setTextSize(18);
                break;
            case "4":
                tv.setTextSize(24);
                tv2.setTextSize(19);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
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
            Intent main = new Intent(PostActivity.this, MainActivity.class);
            main.putExtra("url", backupurl);
            main.putExtra("title", backuptitle);
            startActivity(main);
        }
        if (id == R.id.action_save) {

            Save save = null;
            save = datasource.createSave(title,desc,imgSrc);
            Toast.makeText(this, "Content saved", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // Title AsyncTask
    private class Title extends AsyncTask<String, Void, Void> {
        String title;


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
                    if (e.text().length() > 2)
                        desc += "\n\t\t" + e.text().trim() + "\n";
                }

                // Locate the content attribute
                //  desc = description.attr("content");
                // Get the html document title
                // title = document.title();
                Elements img = document.getElementsByClass("main-image");
                // Locate the src attribute
                if (img.size() != 0) {
                    imgSrc = img.attr("src");
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

            Picasso.with(PostActivity.this).load(imgSrc).placeholder(R.drawable.hindu).into(imageView);

            if (desc.length() > 0) {

                menu.clear();

                getMenuInflater().inflate(R.menu.menu_save, menu);
            } else {
                Toast.makeText(PostActivity.this, "Please refresh", Toast.LENGTH_SHORT).show();
            }


            txttitle.setText("\t\t" + desc.trim() + "\n");
            if (progress.isShowing())
                progress.dismiss();
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        }
    }


}
