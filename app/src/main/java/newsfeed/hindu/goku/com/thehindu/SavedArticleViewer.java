package newsfeed.hindu.goku.com.thehindu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by goku on 01-02-2015.
 */
public class SavedArticleViewer extends ActionBarActivity {

    TextView tv, tv2;
    String title, content, url;
    SharedPreferences prefs;
    long dataId;
    private SaveDataSource datasource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postview);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tv = (TextView) findViewById(R.id.webTitle);
        tv2 = (TextView) findViewById(R.id.webText);
        datasource = new SaveDataSource(this);
        datasource.open();
        changetheme();
        changetext();
        Typeface title_font = Typeface.createFromAsset(getAssets(), "fonts/title_bold.otf");
        Typeface content_font = Typeface.createFromAsset(getAssets(), "fonts/content_light.ttf");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("The Hindu");
        Bundle bundle = this.getIntent().getExtras();

        title = bundle.getString("Title");
        content = bundle.getString("Content");
        url = bundle.getString("Url");
        dataId = bundle.getLong("Id");

        TextView t = (TextView) findViewById(R.id.webTitle);
        t.setText(title);
        t.setTypeface(title_font);
        tv2.setText(content);
        tv2.setTypeface(content_font);
        ImageView imageView = (ImageView) findViewById(R.id.webImage);
        Picasso.with(this).load(url).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viewer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {


            datasource.deleteComment(dataId);
            Toast.makeText(this,"Article Deleted",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this,SavedArticles.class);
            startActivity(i);
            this.finish();
        }


        return super.onOptionsItemSelected(item);
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

}
