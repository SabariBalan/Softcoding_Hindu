package newsfeed.hindu.goku.com.thehindu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sabari on 23-12-2014.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.l2r);
        Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.r2l);
        Animation animation3 = AnimationUtils.loadAnimation(this,R.anim.b2t);
       // ImageView k=(ImageView)findViewById(R.id.k);
        ImageView i1=(ImageView)findViewById(R.id.i2);
        ImageView i2=(ImageView)findViewById(R.id.i3);
        ImageView i3=(ImageView)findViewById(R.id.fal);
        TextView t1=(TextView)findViewById(R.id.t1);
        TextView t2=(TextView)findViewById(R.id.t2);
        i3.startAnimation(animation3);
        i1.startAnimation(animation1);
        i2.startAnimation(animation2);
        t1.startAnimation(animation3);
        t2.startAnimation(animation3);
        Thread thread=new Thread(){
            @Override
            public void run() {
            try{sleep(2000);


            }catch (InterruptedException e)
            {
                e.printStackTrace();

            }
                finally {
                Intent openMain = new Intent(SplashScreen.this,HomeMain.class);
                startActivity(openMain);
                finish();
            }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
