package newsfeed.hindu.goku.com.thehindu;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by goku on 25-01-2015.
 */
public class MyService extends IntentService {
    PostData[] listData = new PostData[0];


    String url = "http://www.thehindu.com/?service=rss";


    public MyService() {
        super("MyServiceName");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("MyService", "About to execute MyTask");
        new MyTask().execute(url);
        Toast.makeText(getApplicationContext(),"gokul",Toast.LENGTH_SHORT).show();

    }
    private enum RSSXMLTag {
        TITLE, DATE, LINK, CONTENT, IGNORETAG;
    }

    private class MyTask extends AsyncTask<String, Integer, ArrayList<PostData>> {
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
                Log.d("debug", "notification response is: " + response);
                try{
                    is = connection.getInputStream();

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

                                            pdData.news_content = content;

                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }


                        eventType = xpp.next();
                    }

                }finally {
                    connection.disconnect();
                }

                Log.v("tst", String.valueOf((postDataList.size())));
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


                }
                int count = (int)(Math.random()*(result.size()));
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyService.this);
                if(prefs.getBoolean("notification_option", true))
                  sendNotification(getApplicationContext(), listData[count].title, listData[count].date, listData[count].news_content, listData[count].url);




            }

        }
    }

    private void sendNotification(Context context,String title,String date,String content,String url) {
       // Intent notificationIntent = new Intent(context, MainActivity.class);

        Intent i = new Intent(context, PostNotificationActivity.class);
        i.putExtra("content",url);
        i.putExtra("title",title);
        i.putExtra("backupurl", url);
        i.putExtra("backuptitle", title);
        Log.d("tst", "title" +title+ "NEWS  " +url);

        PendingIntent contentIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(),i, 0);
        NotificationManager notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_launcher, "Refresh", System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(context,title ,content, contentIntent);
        notificationMgr.notify(0, notification);
    }
}