package com.uos.leitner;

import android.app.Application;
import com.tsengvn.typekit.Typekit;

/**
 * Created by HANJU on 2016. 12. 2..
 */

public class LeitnerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/Slabo-Regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NotoSansKR-Bold.otf"))
                .add("Slabo", Typekit.createFromAsset(this, "fonts/Slabo-Regular.ttf"))
                .add("Fontawesome", Typekit.createFromAsset(this, "fonts/fontawesome-webfont.ttf"));
    }
}
