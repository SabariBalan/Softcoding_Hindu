package newsfeed.hindu.goku.com.thehindu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by goku on 01-02-2015.
 */
public class CustomListForSavedItems extends ArrayAdapter<Save> {
    private Activity context;

    SharedPreferences prefs;
    private List<Save> datas;
    Typeface title_font, content_font, date_font;


    public CustomListForSavedItems(Activity context,
                                   int id, List<Save> obj) {
        super(context, id, obj);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
        title_font = Typeface.createFromAsset(context.getAssets(), "fonts/title_bold.otf");
        content_font = Typeface.createFromAsset(context.getAssets(), "fonts/content_light.ttf");
        date_font = Typeface.createFromAsset(context.getAssets(), "fonts/semibold.ttf");
        datas = obj;


    }

    static class ViewHolder {
        TextView title_text;
        ImageView imageView;
        RelativeLayout r;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        View rowView;
        ViewHolder v;
        if (view == null) {

            LayoutInflater inflater = context.getLayoutInflater();

            view = inflater.inflate(R.layout.save_list_item, null);
            v = new ViewHolder();
            v.title_text = (TextView) view.findViewById(R.id.saved_title_text);
            v.imageView = (ImageView) view.findViewById(R.id.saved_image);
            v.r=(RelativeLayout)view.findViewById(R.id.relativeLayout);

            view.setTag(v);
        } else {
            v = (ViewHolder) view.getTag();
        }
        rowView = view;


        v.title_text.setText(datas.get(position).getTitle());
        Picasso.with(context).load(datas.get(position).getUrl()).placeholder(R.drawable.hindu_small).into(v.imageView);



        TextView title_text = v.title_text;
        title_text.setTypeface(title_font);

        if (prefs.getBoolean("app_theme", false)) {

            v.title_text.setBackgroundColor(Color.WHITE);
            v.title_text.setTextColor(Color.BLACK);
            v.r.setBackgroundColor(Color.WHITE);
        }



        return rowView;
    }
}
