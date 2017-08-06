package leesd.crossithackathon;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import leesd.crossithackathon.data.MapList;


/**
 * Created by leesd on 2017-07-31.
 */

public class SplashActivity extends AwesomeSplash {

    private final int duration = 2000;
    private final int DELAY = 2000;

    @Override
    public void initSplash(ConfigSplash configSplash) {
        mapInit();
        configSplash.setBackgroundColor(R.color.colorSplash);
        configSplash.setAnimCircularRevealDuration(duration);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.splash_logo);
        configSplash.setAnimLogoSplashDuration(duration);

        configSplash.setTitleSplash("CROSS IT");
        configSplash.setTitleTextSize(25);
        configSplash.setAnimTitleDuration(1000);
        configSplash.setTitleFont("fonts/To The Point.ttf");
    }

    @Override
    public void animationsFinished() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY);
    }

    private void mapInit(){
        MapList.mapInit();
    }
}
