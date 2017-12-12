package leesd.crossithackathon.serviceEval;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.view.PieChartView;
import leesd.crossithackathon.DataManager.SatisfactionExcelFile;
import leesd.crossithackathon.R;

/*
 * Created by YUNHEE on 2017-12-12.
 */

public class ServiceEvalActivity extends AppCompatActivity {

    @BindView(R.id.service_eval_top_back) ImageView goBack;    //이전 버튼
    @BindView(R.id.service_eval_top_agency_text) TextView agencyName;  //기관 이름

    @BindView(R.id.service_eval_year_prev) ImageView tabYearPrev;   //년도별탭 이전 버튼
    @BindView(R.id.service_eval_year_next) ImageView tabYearNext;   //년도별탭 다음 버튼
    @BindView(R.id.service_eval_year_text) TextView tabYearText;    //년도별탭 텍스트

    @BindView(R.id.service_eval_rating_bar) RatingBar ratingBar;    //평가등급 바
    @BindView(R.id.service_eval_grade_text) TextView gradeText; //평가등급 텍스트

    @BindView(R.id.service_eval_chart) PieChartView pieChart;  //파이 그래프

    @BindView(R.id.service_eval_info) View info;    //설명 버튼

    private int year = 2016;    //기준년도
    private String markerAgency;    //기관명


    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_eval);
        ButterKnife.bind(this);

        //이전으로 돌아가기
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //기관 이름 가져오기
        markerAgency = getIntent().getStringExtra("markerData");
        agencyName.setText(markerAgency);

        tabYearText.setText(year + "년");
        //탭 별 페이지 버튼 이벤트
        tabYearPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearPrevCheck();
                yearPageRefresh();
            }
        });

        tabYearNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearNextCheck();
                yearPageRefresh();
            }
        });

        //등급 바
        ratingBarColorInit(ratingBar);
        ratingBar(year);
    }

    //년도별 탭 년도 체크
    private void yearPrevCheck(){
        if(year != 2014)
            year--;
    }

    private void yearNextCheck(){
        if(year != 2016)
            year++;
    }

    //년도별 탭 새로고침
    @SuppressLint("SetTextI18n")
    private void yearPageRefresh(){
        tabYearText.setText(year + "년");
        ratingBarColorInit(ratingBar);
        ratingBar(year);
    }

    //등급 바
    private void ratingBar(int thisYear){
        //Excel에서 Data 가져오기
        SatisfactionExcelFile sf = new SatisfactionExcelFile(ServiceEvalActivity.this);

        if(markerAgency != null) {
            HashMap<String, String> hashMap = sf.selectByName(markerAgency);
            ratingBar.setRating(sf.number(hashMap.get("SF_"+thisYear)));
            gradeText.setText(sf.translate(hashMap.get("SF_"+thisYear)));
        }
        else{
            ratingBar.setRating(0);
            gradeText.setText(" -");
        }
    }

    private void ratingBarColorInit(RatingBar ratingBar){
        LayerDrawable stars = (LayerDrawable)ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(ServiceEvalActivity.this,R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }
}
