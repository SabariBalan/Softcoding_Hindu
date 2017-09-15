package newsfeed.hindu.goku.com.thehindu;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

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
import java.util.Random;

/**
 * Created by goku on 29-01-2015.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";
    static PostData[] listData = new PostData[0];

    PendingIntent sender;
    MyTask myTask;

    Context c;
    String urlhome = "http://www.thehindu.com/?service=rss";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        ComponentName thisWidget = new ComponentName(context,
                MyWidgetProvider.class);
        c = context;

         myTask=new MyTask();
         myTask.execute(context);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);


        for (int widgetId : allWidgetIds) {
            // create some random data
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            Log.d("WidgetExample", String.valueOf(number));


            // Set the text
            // remoteViews.setTextViewText(R.id.update, String.valueOf(number));

            // Register an onClickListener
            //Intent intent = new Intent(context, MyWidgetProvider.class);

            // intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            // PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
            //         0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);

            remoteViews.setOnClickPendingIntent(R.id.widget_refresh, getPendingSelfIntent(context, "refresh"));

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private enum RSSXMLTag {
        TITLE, DATE, LINK, CONTENT, IGNORETAG;
    }

    private class MyTask extends AsyncTask<Context, Integer, ArrayList<PostData>> {
        private RSSXMLTag currentTag;
        Context taskc;

        @Override
        protected ArrayList<PostData> doInBackground(Context... params) {
            // TODO Auto-generated method stub

            taskc=params[0];
            InputStream is = null;

            ArrayList<PostData> postDataList = new ArrayList<PostData>();
            try {
                URL url = new URL(urlhome);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setReadTimeout(20 * 1000);
                connection.setConnectTimeout(20 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("debug", "Widget response is: " + response);
                is =new BufferedInputStream(connection.getInputStream());

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

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(taskc);

                RemoteViews remoteViews;
                ComponentName watchWidget;

                remoteViews = new RemoteViews(taskc.getPackageName(), R.layout.widget_layout);
                watchWidget = new ComponentName(taskc, MyWidgetProvider.class);

                remoteViews.setTextViewText(R.id.widget_title, listData[count].title);
                remoteViews.setTextViewText(R.id.widget_content, listData[count].news_content);

                Intent intent = new Intent(taskc, PostNotificationActivity.class);

                intent.putExtra("content", listData[count].url);
                intent.putExtra("title", listData[count].title);
                intent.putExtra("backupurl", listData[count].url);
                intent.putExtra("backuptitle", listData[count].title);
                PendingIntent pendingIntent = PendingIntent.getActivity(taskc, (int) System.currentTimeMillis(), intent, 0);
              //  Toast.makeText(taskc, "widget updated" +count, Toast.LENGTH_SHORT).show();

                remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

                appWidgetManager.updateAppWidget(watchWidget, remoteViews);


            }

        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);

        if ("refresh".equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);


            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            watchWidget = new ComponentName(context, MyWidgetProvider.class);

            remoteViews.setTextViewText(R.id.widget_title,"Loading");
            remoteViews.setTextViewText(R.id.widget_content,"Please wait");
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

            myTask=new MyTask();
            myTask.execute(context);


        }

    }


    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);



    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);



    }


}
