package newsfeed.hindu.goku.com.thehindu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by goku on 17-01-2015.
 */
public class About extends ActionBarActivity {

    private Toolbar toolbar;
    KenBurnsView kenBurnsView;
    Intent dial = new Intent(Intent.ACTION_DIAL);
    Intent emailInt=new Intent(Intent.ACTION_SENDTO);
    Intent web=new Intent(Intent.ACTION_VIEW);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.phone_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dial.setData(Uri.parse("tel:9659172403"));
                startActivity(dial);

            }
        });
        findViewById(R.id.gmail_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInt.setData(Uri.parse("mailto:"));
                emailInt.putExtra(Intent.EXTRA_EMAIL,new String[]{"gokulprabhu.droid@gmail.com"});
                startActivity(emailInt);
            }
        });
        findViewById(R.id.face_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.setData(Uri.parse("https://facebook.com/gokul.prabhu.927?refid=8"));
                startActivity(web);

            }
        });

        findViewById(R.id.phone_fb2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dial.setData(Uri.parse("tel:9092155088"));
                startActivity(dial);

            }
        });
        findViewById(R.id.gmail_fb2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInt.setData(Uri.parse("mailto:"));
                emailInt.putExtra(Intent.EXTRA_EMAIL,new String[]{"catchsabaribalan@gmail.com"});
                startActivity(emailInt);
            }
        });
        findViewById(R.id.face_fb2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.setData(Uri.parse("https://www.facebook.com/catchsabaribalan?ref=ts&fref=ts"));
                startActivity(web);

            }
        });
        findViewById(R.id.phone_fb3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dial.setData(Uri.parse("tel:9840490886"));
                startActivity(dial);

            }
        });
        findViewById(R.id.gmail_fb3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInt.setData(Uri.parse("mailto:"));
                emailInt.putExtra(Intent.EXTRA_EMAIL,new String[]{"rkmohanchn@gmail.com"});
                startActivity(emailInt);
            }
        });
        findViewById(R.id.face_fb3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.setData(Uri.parse("https://www.facebook.com/profile.php?id=100006540018379"));
                startActivity(web);

            }
        });
    }
}
