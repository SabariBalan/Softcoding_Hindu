package newsfeed.hindu.goku.com.thehindu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    PostData[] listData = new PostData[0];
    private ProgressDialog progress;

    String url, title;
    ListView listView;
    SharedPreferences prefs;
    CustomList adapter;
    RssDataController r;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("app_theme", false)) {
            changeTheme();
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable() &&
                networkInfo.isConnected()){

        }else
        {
            Toast.makeText(this,"Please refresh,Error while connecting to internet",Toast.LENGTH_SHORT).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView = (ListView) findViewById(R.id.listView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeView);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.blue, R.color.green);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

        Bundle urlBunble = getIntent().getExtras();
        if (urlBunble != null) {
            url = urlBunble.getString("url", "http://www.thehindu.com/?service=rss");
            title = urlBunble.getString("title", "The Hindu");
        }
        getSupportActionBar().setTitle(title);


        r = new RssDataController();
        r.execute(url);


        adapter = new CustomList(this, R.layout.list_item, listData);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, PostActivity.class);
                i.putExtra("content", listData[position].url);
                i.putExtra("title", listData[position].title);
                i.putExtra("backupurl", url + "");
                i.putExtra("backuptitle", title);
                startActivity(i);

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (r.getStatus().equals(AsyncTask.Status.RUNNING)) {
                    Log.d("debug", "Main Activity thread canceled ");

                    r.cancel(true);
                }

                r = new RssDataController();
                r.execute(url);

            }
        });


    }

    @Override
    protected void onStop() {
        if (r.getStatus().equals(AsyncTask.Status.RUNNING)) {
            Log.d("debug", "Main Activity thread canceled ");

            r.cancel(true);
        }
        Log.d("debug", "Main Activ on stop ");
        super.onStop();
    }

    private void changeTheme() {


        findViewById(R.id.main_relativelayout_1).setBackgroundColor(getResources().getColor(R.color.main_relative1));
        findViewById(R.id.listView).setBackgroundColor(getResources().getColor(R.color.main_listview));
        findViewById(R.id.swipeView).setBackgroundColor(getResources().getColor(R.color.main_swipeview));


    }


    private enum RSSXMLTag {
        TITLE, DATE, LINK, CONTENT, IGNORETAG;
    }


    private class RssDataController extends
            AsyncTask<String, Integer, ArrayList<PostData>> {
        private RSSXMLTag currentTag;

        @Override
        protected ArrayList<PostData> doInBackground(String... params) {
            // TODO Auto-generated method stub
            String urlStr = params[0];
            InputStream is = null;
            ArrayList<PostData> postDataList = new ArrayList<PostData>();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(10 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("debug", "Main Activity response " + response);
                try {
                    is = new BufferedInputStream(connection.getInputStream());

                    // parse xml after getting the data
                    XmlPullParserFactory factory = XmlPullParserFactory
                            .newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(is, null);

                    int eventType = xpp.getEventType();
                    PostData pdData = null;
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "EEE, DD MMM yyyy HH:mm:ss");
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {

                        } else if (eventType == XmlPullParser.START_TAG) {
                            //  Log.d("start jk Tag",xpp.getName());

                            if (xpp.getName().equals("item")) {
                                pdData = new PostData();
                                //Log.d("Item set","new data");
                                currentTag = RSSXMLTag.IGNORETAG;
                            }
                            if (xpp.getName().equals("title")) {
                                //Log.d("Item set",xpp.getName());
                                currentTag = RSSXMLTag.TITLE;
                            }
                            if (xpp.getName().equals("link")) {
                                // Log.d("Item set",xpp.getName());
                                currentTag = RSSXMLTag.LINK;
                            }
                            if (xpp.getName().equals("pubDate")) {
                                //Log.d("Item set",xpp.getName());
                                currentTag = RSSXMLTag.DATE;
                            }
                            if (xpp.getName().equals("description")) {
                                //  Log.d("Item set",xpp.getName());
                                currentTag = RSSXMLTag.CONTENT;
                            }
                        } else if (eventType == XmlPullParser.END_TAG) {

                            //Log.d("End item",xpp.getName());
                            if (xpp.getName().equals("item")) {
                                // format the data here, otherwise format data in
                                // Adapter

                                Log.d("End item", xpp.getName());
                                postDataList.add(pdData);


                            } else {

                                currentTag = RSSXMLTag.IGNORETAG;
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            String content = xpp.getText();
                            content = content.trim();


                            if (pdData != null) {
                                switch (currentTag) {
                                    case TITLE:
                                        if (content.length() != 0) {
                                            pdData.title = content;
                                        }
                                        break;
                                    case LINK:
                                        if (content.length() != 0) {

                                            pdData.url = content;

                                        }
                                        break;
                                    case DATE:
                                        if (content.length() != 0) {

                                            Date postDate = dateFormat.parse(content);

                                            pdData.date = dateFormat.format(postDate);


                                        }
                                        break;
                                    case CONTENT:
                                        if (content.length() != 0) {


                                            pdData.news_content = content.replaceAll("\\<[^>]*>", "");
                                            pdData.news_content = pdData.news_content.replaceAll("&amp;", "&");
                                            pdData.news_content = pdData.news_content.replaceAll("&nbsp;", "\'");
                                            pdData.news_content = pdData.news_content.replaceAll("&quot;", "\'");

                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }


                        eventType = xpp.next();
                    }
                } finally {
                    connection.disconnect();
                    Log.d("debug", "main disconnect ");

                }


                Log.d("length", String.valueOf((postDataList.size())));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            //catch (java.text.ParseException e) {
            ////     e.printStackTrace();
            //  }


            return postDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<PostData> result) {
            // TODO Auto-generated method stub

            if (result.size() > 0) {
                listData = new PostData[result.size()];

                for (int i = 0; i < result.size(); i++) {


                    listData[i] = result.get(i);

                    Log.d("tst", "DATE  " + listData[i].date + "TITlE  " + listData[i].title + "NEWS  " + listData[i].news_content);
                }

            } else {
                listData = new PostData[1];
                PostData temp = new PostData();

                temp.title = "No items to show";
                temp.news_content = "Swipe to refresh";
                temp.date = "";
                temp.url = "";
                listData[0] = temp;
            }

            listView.setAdapter(new CustomList(MainActivity.this, R.layout.list_item, listData));

            if (progress.isShowing())
                progress.dismiss();
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            Intent main = new Intent(this, HomeMain.class);

            startActivity(main);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
