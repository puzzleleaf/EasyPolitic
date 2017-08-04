package leesd.crossithackathon.Info;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.Window;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import leesd.crossithackathon.R;

/**
 * Created by leesd on 2017-08-01.
 */

public class DetailActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);


        // 임시 그래프
        PieView pieView = (PieView) findViewById(R.id.pieView);
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView.setInnerText("A");
        PieView animatedPie = (PieView) findViewById(R.id.pieView);

        PieAngleAnimation animation = new PieAngleAnimation(animatedPie);
        animation.setDuration(5000); //This is the duration of the animation in millis
        animatedPie.startAnimation(animation);


        PieView pieView2 = (PieView) findViewById(R.id.pieView2);
        pieView2.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView2.setInnerText("A");
        PieView animatedPie2 = (PieView) findViewById(R.id.pieView2);

        PieAngleAnimation animation2 = new PieAngleAnimation(animatedPie2);
        animation2.setDuration(5000); //This is the duration of the animation in millis
        animatedPie2.startAnimation(animation2);
    }
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    //Dialog 배경을 반투명하게 설정한다. 얼만큼 투명하게 할 지는 xml의 background에서 설정
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first){
        super.onApplyThemeResource(theme,resid,first);
        theme.applyStyle(android.R.style.Theme_Panel,true);
    }
}
