package leesd.crossithackathon.Info;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import leesd.crossithackathon.R;
import leesd.crossithackathon.data.MapList;

/**
 * Created by leesd on 2017-08-01.
 */

public class DetailActivity extends AppCompatActivity {

    //별
    private RatingBar ratingBarFir;
    private RatingBar ratingBarSec;
    private RatingBar ratingBarThr;

    //이미지
    private ImageView logoImage;
    String markerData;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        markerData = getIntent().getStringExtra("markerData");


        ratingBarInit();
        imageInit();

        // 임시 그래프
        PieView pieView = (PieView) findViewById(R.id.pieView);
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView.setInnerText("A");
        PieView animatedPie = (PieView) findViewById(R.id.pieView);

        PieAngleAnimation animation = new PieAngleAnimation(animatedPie);
        animation.setDuration(3000); //This is the duration of the animation in millis
        animatedPie.startAnimation(animation);

    }

    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    //Image
    private void imageInit(){
        logoImage = (ImageView)findViewById(R.id.detail_image);
        String resUri = null;
        try{
            resUri = getString(MapList.mapList.get(markerData));
        }catch (NullPointerException e){

        }
        if(resUri!=null) {
            Glide.with(this).load(resUri).into(logoImage);
        }

    }

    //RatingBar
    private void ratingBarTemp(){
        ratingBarFir.setRating(3);
        ratingBarSec.setRating(4);
        ratingBarThr.setRating(2);
    }

    private void ratingBarInit(){
        ratingBarFir = (RatingBar)findViewById(R.id.detail_rating_bar_fir);
        ratingBarSec = (RatingBar)findViewById(R.id.detail_rating_bar_sec);
        ratingBarThr = (RatingBar)findViewById(R.id.detail_rating_bar_thr);

        ratingBarColorInit(ratingBarFir);
        ratingBarColorInit(ratingBarSec);
        ratingBarColorInit(ratingBarThr);

        ratingBarTemp();
    }

    private void ratingBarColorInit(RatingBar ratingBar){
        LayerDrawable stars = (LayerDrawable)ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    private void mapList(){

    }

}
