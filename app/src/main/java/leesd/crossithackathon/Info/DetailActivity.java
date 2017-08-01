package leesd.crossithackathon.Info;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.Window;

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
