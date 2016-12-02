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
                .addNormal(Typekit.createFromAsset(this, "Slabo-Regular.otf"))
                .addBold(Typekit.createFromAsset(this, "NotoSansKR-Bold.otf"))
                .add("Slabo", Typekit.createFromAsset(this, "Slabo-Regular.ttf"));
    }
}
