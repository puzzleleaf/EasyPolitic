package leesd.crossithackathon.Info;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.HashMap;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import leesd.crossithackathon.DataManager.CivilComplaintRate;
import leesd.crossithackathon.DataManager.CivilComplaintRateVO;
import leesd.crossithackathon.DataManager.GrievanceFieldExcelFile;
import leesd.crossithackathon.DataManager.GrievanceTotalExcelFile;
import leesd.crossithackathon.DataManager.SatisfactionExcelFile;
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
    private TextView linkButton;

    //마커 클릭시, 기관 이름 가져옴
    String markerData;

    //민원만족도 설명 다이얼로그
    LovelyInfoDialog info;

    //처리율
    private double complaintRatio;
    private ImageView complaintYearNext;
    private ImageView complaintYearPrev;
    private TextView complaintYearSemester;
    private TextView txt1, txt2, txt3, txt4;
    private int complaintYear, complaintSemester;
    private String yearSemester;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        markerData = getIntent().getStringExtra("markerData");

        linkButton = (TextView)findViewById(R.id.link);

        complaintYear = 2017;
        complaintSemester = 2;
        yearSemester = complaintYear + "_" + complaintSemester;
        complaintYearNext = (ImageView)findViewById(R.id.complaint_year_semester_next);
        complaintYearPrev = (ImageView)findViewById(R.id.complaint_year_semester_prev);
        complaintYearSemester = (TextView)findViewById(R.id.complaint_year_semester);
        complaintYearSemester.setText(yearSemester);

        complaintYearNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearNextCheck();
                pageRefresh();
            }
        });

        complaintYearPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearPrevCheck();
                pageRefresh();
            }
        });

        info = new LovelyInfoDialog(this);


        titleInit();
        ratingBarInit();
        imageInit();
        complaintRatio = complaintRatioInit(markerData,yearSemester);
        setPieView(complaintRatio);

        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String uri = getString(MapList.urlList.get(markerData));
                Uri u = Uri.parse(uri);
                intent.setData(u);
                startActivity(intent);
            }
        });

        //민원만족도 설명
        findViewById(R.id.detail_satisfaction).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               info.setTopColorRes(R.color.colorBlue)
                       .setIcon(R.drawable.info2)
                       .setTitle("Satisfaction Level\nof Civil Petitions")
                       .setMessage("It is an indicator (S, A, B, C, D)\nthat calculates\nthe satisfaction of the complainant\nand the effort of the agency.")
                       .setConfirmButtonText("OK")
                       .show();
           }
        });
    }

    public void setPieView(double complaintRatio){
        PieView pieView = (PieView) findViewById(R.id.pieView);
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView.setInnerText(String.valueOf(complaintRatio));
        pieView.setPercentage((float)complaintRatio);
        PieView animatedPie = (PieView) findViewById(R.id.pieView);

        PieAngleAnimation animation = new PieAngleAnimation(animatedPie);
        animation.setDuration(3000); //This is the duration of the animation in millis
        animatedPie.startAnimation(animation);

    }

    public double complaintRatioInit(String institutionName, String yearSemester){

        txt1 = (TextView)findViewById(R.id.total_receipt);
        txt2 = (TextView)findViewById(R.id.in_period_processing);
        txt3 = (TextView)findViewById(R.id.overdue_processing);
        txt4 = (TextView)findViewById(R.id.overdue_unprocessing);

        CivilComplaintRate ccr = new CivilComplaintRate(getBaseContext());
        CivilComplaintRateVO ccrv = ccr.extractCellData(yearSemester, institutionName);

        txt1.setText(String.valueOf(ccrv.getTotal_register()));
        txt2.setText(String.valueOf(ccrv.getIn_date_handling()));
        txt3.setText(String.valueOf(ccrv.getOut_date_handling()));
        txt4.setText(String.valueOf(ccrv.getOut_date_failure()));


        return ccrv.getHandling_rate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    //Title
    private void titleInit(){
        //Excel에서 Data 가져오기
        SatisfactionExcelFile sf = new SatisfactionExcelFile(getBaseContext());
        HashMap<String, String> hashMap = sf.selectByName(markerData);

        TextView title = (TextView)findViewById(R.id.institutionName);
        title.setText(hashMap.get("SF_AGENCY"));
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
    private void ratingBar(){
        //Excel에서 Data 가져오기
        SatisfactionExcelFile sf = new SatisfactionExcelFile(getBaseContext());
        TextView text1 = (TextView)findViewById(R.id.gradeText1);
        TextView text2 = (TextView)findViewById(R.id.gradeText2);
        TextView text3 = (TextView)findViewById(R.id.gradeText3);

        if(markerData != null) {
            HashMap<String, String> hashMap = sf.selectByName(markerData);
            ratingBarFir.setRating(sf.number(hashMap.get("SF_2014")));
            ratingBarSec.setRating(sf.number(hashMap.get("SF_2015")));
            ratingBarThr.setRating(sf.number(hashMap.get("SF_2016")));

            //번역
            text1.setText(sf.translate(hashMap.get("SF_2014")));
            text2.setText(sf.translate(hashMap.get("SF_2015")));
            text3.setText(sf.translate(hashMap.get("SF_2016")));
        }
        else{
            ratingBarFir.setRating(0);
            ratingBarSec.setRating(0);
            ratingBarThr.setRating(0);
            text1.setText(" -");
            text2.setText(" -");
            text3.setText(" -");
        }
    }

    private void ratingBarInit(){
        ratingBarFir = (RatingBar)findViewById(R.id.detail_rating_bar_fir);
        ratingBarSec = (RatingBar)findViewById(R.id.detail_rating_bar_sec);
        ratingBarThr = (RatingBar)findViewById(R.id.detail_rating_bar_thr);

        ratingBarColorInit(ratingBarFir);
        ratingBarColorInit(ratingBarSec);
        ratingBarColorInit(ratingBarThr);

        ratingBar();
    }

    private void ratingBarColorInit(RatingBar ratingBar){
        LayerDrawable stars = (LayerDrawable)ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    private void pageRefresh(){
        yearSemester = complaintYear + "_"+complaintSemester;
        yearSemesterTextSet();
        complaintRatio = complaintRatioInit(markerData,yearSemester);
        setPieView(complaintRatio);
    }
    private void yearSemesterTextSet(){
        complaintYearSemester.setText(yearSemester);
    }
    private void dataYearPrevCheck(){
        if(complaintYear==2016){
            if(complaintSemester>1)
                complaintSemester--;
        }
        else if(complaintYear==2017) {
            if (complaintSemester == 1) {
                complaintYear--;
                complaintSemester = 4;
            } else
                complaintSemester--;
        }
    }
    private void dataYearNextCheck(){
        if(complaintYear==2016){
            if(complaintSemester<4)
                complaintSemester++;
            else {
                complaintYear++;
                complaintSemester = 1;
            }
        }
        else if(complaintYear==2017){
            if(complaintSemester<2)
                complaintSemester++;
        }
    }
}
