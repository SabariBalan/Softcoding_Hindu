package newsfeed.hindu.goku.com.thehindu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by goku on 13-01-2015.
 */
public class CustomList extends ArrayAdapter<PostData> {
    private Activity context;

    SharedPreferences prefs;
    private PostData[] datas;
    Typeface title_font,content_font,date_font;




    public CustomList(Activity context,
                      int id, PostData[] obj) {
        super(context, id, obj);
        prefs= PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
         title_font = Typeface.createFromAsset(context.getAssets(), "fonts/title_bold.otf");
         content_font = Typeface.createFromAsset(context.getAssets(),"fonts/content_light.ttf");
         date_font= Typeface.createFromAsset(context.getAssets(),"fonts/semibold.ttf");
        datas = obj;


    }

    static class ViewHolder {
        TextView title_text;
        TextView content_text;
        TextView date_text;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        View rowView;
        ViewHolder v;
        if (view == null) {

            LayoutInflater inflater = context.getLayoutInflater();

            view = inflater.inflate(R.layout.list_item, null);
            v = new ViewHolder();
            v.title_text = (TextView) view.findViewById(R.id.title_text);
            v.content_text = (TextView) view.findViewById(R.id.content_text);
            v.date_text = (TextView) view.findViewById(R.id.date_text);
            view.setTag(v);
        } else {
            v = (ViewHolder) view.getTag();
        }
        rowView = view;


        v.title_text.setText(datas[position].title);
        v.date_text.setText(datas[position].date);
        v.content_text.setText(datas[position].news_content);

        TextView title_text = v.title_text;
        TextView content_text = v.content_text;
        TextView date_text = v.date_text;
        title_text.setTypeface(title_font);
        content_text.setTypeface(content_font);
        date_text.setTypeface(date_font);

        if(prefs.getBoolean("app_theme",false)) {

            view.findViewById(R.id.list_item_linarlayout_1).setBackgroundColor(context.getResources().getColor(R.color.list_linear1));
            view.findViewById(R.id.divider_listitem).setBackgroundColor(context.getResources().getColor(R.color.list_divider));
            view.findViewById(R.id.relativeLayout).setBackgroundColor(context.getResources().getColor(R.color.list_relative1));
            view.findViewById(R.id.listitem_relativelayout).setBackgroundColor(context.getResources().getColor(R.color.list_relative2));


            title_text.setTextColor(context.getResources().getColor(R.color.list_title));
            title_text.setBackgroundColor(context.getResources().getColor(R.color.list_titlebg));

            date_text.setTextColor(context.getResources().getColor(R.color.list_date));
            date_text.setBackgroundColor(context.getResources().getColor(R.color.list_datebg));

            content_text.setTextColor(context.getResources().getColor(R.color.list_content));
            content_text.setBackgroundColor(context.getResources().getColor(R.color.list_contentbg));



        }

        String textsize = prefs.getString("text_size","2");
        switch(textsize){
            case "1":
                title_text.setTextSize(21);
                content_text.setTextSize(16);
                break;
            case "3":
                title_text.setTextSize(23);
                content_text.setTextSize(18);
                break;
            case "4":
                title_text.setTextSize(24);
                content_text.setTextSize(19);
                break;
        }




        return rowView;
    }
}