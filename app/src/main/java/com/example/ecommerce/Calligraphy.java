package com.example.ecommerce;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class Calligraphy extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ViewPump.init(ViewPump.builder()
        .addInterceptor(new CalligraphyInterceptor(
                new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/lily_script_one_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()))
        .build());

    }
}
