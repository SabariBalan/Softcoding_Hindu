package newsfeed.hindu.goku.com.thehindu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by goku on 01-02-2015.
 */
public class SavedArticles extends ActionBarActivity {

    private SaveDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        datasource = new SaveDataSource(this);
        datasource.open();
        final List<Save> values = datasource.getAllSave();

        ListView savedList = (ListView) findViewById(R.id.saved_list);

        CustomListForSavedItems adapter = new CustomListForSavedItems(this, R.layout.save_list_item, values);

        savedList.setAdapter(adapter);
        savedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent viewer = new Intent(SavedArticles.this, SavedArticleViewer.class);
                viewer.putExtra("Title", values.get(position).getTitle());
                viewer.putExtra("Content", values.get(position).getContnet());
                viewer.putExtra("Url", values.get(position).getUrl());
                viewer.putExtra("Id",values.get(position).getId());
                startActivity(viewer);


            }
        });
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean("app_theme", false)) {


            savedList.setBackgroundColor(Color.rgb(228, 228, 228));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
