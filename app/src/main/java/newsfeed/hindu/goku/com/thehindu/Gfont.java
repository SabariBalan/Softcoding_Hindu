package newsfeed.hindu.goku.com.thehindu;

/**
 * Created by goku on 01-02-2015.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by goku on 23-12-2014.
 */
public class Gfont extends TextView {

    /*
     * Caches typefaces based on their file path and name, so that they don't have to be created every time when they are referenced.
     */
    private static Typeface mTypeface;

    public Gfont(final Context context) {
        this(context, null);
    }

    public Gfont(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Gfont(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/title_bold.otf");
        }
        setTypeface(mTypeface);
    }

}