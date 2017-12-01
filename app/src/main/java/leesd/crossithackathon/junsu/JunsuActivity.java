package leesd.crossithackathon.junsu;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import butterknife.BindView;
import butterknife.ButterKnife;
import leesd.crossithackathon.DataManager.CivilComplaintRate;
import leesd.crossithackathon.DataManager.CivilComplaintRateVO;
import leesd.crossithackathon.R;

//민원 준수율
public class JunsuActivity  extends AppCompatActivity {

    @BindView(R.id.junsu_top_back) ImageView goBack;    //이전 버튼
    @BindView(R.id.junsu_top_agency_text) TextView agencyName;  //기관 이름

    @BindView(R.id.junsu_framelayout1) View frameLayout1;   //분기별 화면
    @BindView(R.id.junsu_framelayout2) View frameLayout2;   //년도별 화면

    @BindView(R.id.junsu_tab_term) View tabTerm;    //분기별통계 탭
    @BindView(R.id.junsu_tab_year) View tabYear;    //년도별통계 탭
    @BindView(R.id.junsu_term_img) ImageView termImg;    //분기별 탭 이미지
    @BindView(R.id.junsu_year_img) ImageView yearImg;    //분기별 탭 이미지
    @BindView(R.id.junsu_title_term_text) TextView titleTermText;   //분기별 탭 텍스트
    @BindView(R.id.junsu_title_year_text) TextView titleYearText;   //년도별 탭 텍스트

    @BindView(R.id.junsu_term_prev) ImageView tabTermPrev;   //분기별탭 이전 버튼
    @BindView(R.id.junsu_term_next) ImageView tabTermNext;   //분기별탭 다음 버튼
    @BindView(R.id.junsu_term_text) TextView tabTermText;    //분기별탭 텍스트
    @BindView(R.id.junsu_year_prev) ImageView tabYearPrev;   //년도별탭 이전 버튼
    @BindView(R.id.junsu_year_next) ImageView tabYearNext;   //년도별탭 다음 버튼
    @BindView(R.id.junsu_year_text) TextView tabYearText;    //년도별탭 텍스트

    @BindView(R.id.junsu_pie_view1) PieView pieView1;    //준수율 그래프
    @BindView(R.id.junsu_pie_view2) PieView pieView2;    //기간내처리 그래프
    @BindView(R.id.junsu_pie_view3) PieView pieView3;    //기간초과처리 그래프
    @BindView(R.id.junsu_pie_view4) PieView pieView4;    //기간초과미처리 그래프

    @BindView(R.id.junsu_text_total) TextView totalText;    //전체 건수 텍스트
    @BindView(R.id.junsu_text_total2) TextView totalText2;  //전과 비교한 전체 건수 텍스트
    @BindView(R.id.junsu_text_subdata1) TextView inPeriodProcessingText;    //기간내처리 텍스트
    @BindView(R.id.junsu_text_subdata2) TextView overdueProcessingText;     //기간초과처리 텍스트
    @BindView(R.id.junsu_text_subdata3) TextView overdueUnprocessingText;   //기간초과미처리 텍스트

    private int year = 2017;    //기준년도
    private int term = 3;       //기준분기

    private String markerAgency;    //기관명
    private double junsuRatio;  //처리 준수율
    private int total, inPeriodProcessing, overdueProcessing, overdueUnprocessing; //총 접수, 기간내처리건수, 기간초과처리건수, 기간초과미처리건수
    private int prevTotal, prevInPeriodProcessing, prevOverdueProcessing, prevOverdueUnprocessing;  //이전 분기 데이터

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junsu);
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

        //화면 초기화
        frameLayout1.setVisibility(LinearLayout.VISIBLE);
        frameLayout2.setVisibility(LinearLayout.INVISIBLE);
        termImg.setImageResource(R.drawable.junsu_icon1_click);
        yearImg.setImageResource(R.drawable.junsu_icon2);
        titleTermText.setTextColor(Color.rgb(68, 104, 152));
        titleYearText.setTextColor(Color.rgb(0, 0, 0));
        prevJunsuDataInit(markerAgency, year, term);
        tabTermText.setText(year + "년 " + term + "분기");
        junsuRatio = junsuDataInit(markerAgency, year+"_"+term);
        setPieView(junsuRatio);

        //분기별 탭 클릭
        tabTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termImg.setImageResource(R.drawable.junsu_icon1_click);
                yearImg.setImageResource(R.drawable.junsu_icon2);
                titleTermText.setTextColor(Color.rgb(68, 104, 152));
                titleYearText.setTextColor(Color.rgb(0, 0, 0));

                frameLayout1.setVisibility(LinearLayout.VISIBLE);
                frameLayout2.setVisibility(LinearLayout.INVISIBLE);

                tabTermText.setText(year + "년 " + term + "분기");

                tabTermPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        termPrevCheck();
                        termPageRefresh();
                    }
                });

                tabTermNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        termNextCheck();
                        termPageRefresh();
                    }
                });
            }
        });

        //년도별 탭 클릭
        tabYear.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                termImg.setImageResource(R.drawable.junsu_icon1);
                yearImg.setImageResource(R.drawable.junsu_icon2_click);
                titleTermText.setTextColor(Color.rgb(0, 0, 0));
                titleYearText.setTextColor(Color.rgb(68, 104, 152));

                frameLayout1.setVisibility(LinearLayout.INVISIBLE);
                frameLayout2.setVisibility(LinearLayout.VISIBLE);

                tabYearText.setText(year + "년 ");

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
            }
        });
    }

    //분기별 탭 분기 체크
    private void termPrevCheck(){
        if(year == 2016){
            if(term > 1)
                term--;
        }
        else if(year == 2017) {
            if (term == 1) {
                year--;
                term = 4;
            } else
                term--;
        }
    }

    private void termNextCheck(){
        if(year == 2016){
            if(term < 4)
                term++;
            else {
                year++;
                term = 1;
            }
        }
        else if(year == 2017){
            if(term < 3)
                term++;
        }
    }

    //분기별 탭 새로고침
    @SuppressLint("SetTextI18n")
    private void termPageRefresh(){
        prevJunsuDataInit(markerAgency, year, term);
        tabTermText.setText(year + "년 " + term + "분기");
        junsuRatio = junsuDataInit(markerAgency, year+"_"+term);
        setPieView(junsuRatio);
    }

    //년도별 탭 년도 체크
    private void yearPrevCheck(){
        if(year == 2017)
            year--;
    }

    private void yearNextCheck(){
        if(year == 2016)
            year++;
    }

    //년도별 탭 새로고침
    @SuppressLint("SetTextI18n")
    private void yearPageRefresh(){
        tabYearText.setText(year + "년 ");
        //junsuRatio = junsuDataInit(markerAgency, year+"_"+term);
        //setPieView(junsuRatio);
    }

    //데이터 가져오기
    @SuppressLint("SetTextI18n")
    public double junsuDataInit(String institutionName, String yearSemester){

        CivilComplaintRate ccr = new CivilComplaintRate(getBaseContext());
        CivilComplaintRateVO ccrv = ccr.extractCellData(yearSemester, institutionName);

        total = ccrv.getTotal_register();
        inPeriodProcessing = ccrv.getIn_date_handling();
        overdueProcessing = ccrv.getOut_date_handling();
        overdueUnprocessing = ccrv.getOut_date_failure();

        totalText.setText("총 "+ NumberFormat.getNumberInstance(Locale.getDefault()).format(total) + " 건");
        calculatePrevNThisData();

        return ccrv.getHandling_rate();
    }

    //이전 분기 데이터 가져오기
    public void prevJunsuDataInit(String institutionName, int thisYear, int thisTerm){

        if(thisYear == 2016){
            if(thisTerm > 1)
                thisTerm--;
        }
        else if(thisYear == 2017) {
            if (thisTerm == 1) {
                thisYear--;
                thisTerm = 4;
            } else
                thisTerm--;
        }

        CivilComplaintRate ccr = new CivilComplaintRate(getBaseContext());
        CivilComplaintRateVO ccrv = ccr.extractCellData(thisYear+"_"+thisTerm, institutionName);

        prevTotal = ccrv.getTotal_register();
        prevInPeriodProcessing = ccrv.getIn_date_handling();
        prevOverdueProcessing = ccrv.getOut_date_handling();
        prevOverdueUnprocessing = ccrv.getOut_date_failure();
    }

    //이전 분기와 비교
    @SuppressLint("SetTextI18n")
    public void calculatePrevNThisData(){

        //총접수건수
        if(prevTotal < total)
            totalText2.setText("(△ " + NumberFormat.getNumberInstance(Locale.getDefault()).format(Math.abs(prevTotal - total))+ ")");
        else if(prevTotal > total)
            totalText2.setText("(▽ " + NumberFormat.getNumberInstance(Locale.getDefault()).format(Math.abs(prevTotal - total))+ ")");
        else
            totalText2.setText("(-)");

        //기간내처리건수
        if(prevInPeriodProcessing < inPeriodProcessing)
            inPeriodProcessingText.setText("△ " + NumberFormat.getNumberInstance(Locale.getDefault()).format(Math.abs(prevInPeriodProcessing - inPeriodProcessing)));

        else if(prevInPeriodProcessing > inPeriodProcessing)
            inPeriodProcessingText.setText("▽ " + NumberFormat.getNumberInstance(Locale.getDefault()).format(Math.abs(prevInPeriodProcessing - inPeriodProcessing)));
        else
            inPeriodProcessingText.setText("-");

        //기간초과처리건수
        if(prevOverdueProcessing < overdueProcessing)
            overdueProcessingText.setText("△ " + NumberFormat.getNumberInstance(Locale.getDefault()).format(Math.abs(prevOverdueProcessing - overdueProcessing)));

        else if(prevOverdueProcessing > overdueProcessing)
            overdueProcessingText.setText("▽ " + NumberFormat.getNumberInstance(Locale.getDefault()).format(Math.abs(prevOverdueProcessing - overdueProcessing)));
        else
            overdueProcessingText.setText("-");

        //기간초과미처리건수
        if(prevOverdueUnprocessing < overdueUnprocessing)
            overdueUnprocessingText.setText("△ " + NumberFormat.getNumberInstance(Locale.getDefault()).format(Math.abs(prevOverdueUnprocessing - overdueUnprocessing)));

        else if(prevOverdueUnprocessing > overdueUnprocessing)
            overdueUnprocessingText.setText("▽ " + NumberFormat.getNumberInstance(Locale.getDefault()).format(Math.abs(prevOverdueUnprocessing - overdueUnprocessing)));
        else
            overdueUnprocessingText.setText("-");
    }

    //그래프 초기화
    public void setPieView(double junsuRatio){

        //준수율 그래프
        pieView1.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView1.setInnerText(String.valueOf(junsuRatio));
        pieView1.setPercentage((float)junsuRatio);

        PieAngleAnimation animation = new PieAngleAnimation(pieView1);
        animation.setDuration(3000);
        pieView1.startAnimation(animation);

        //기간내처리율 그래프
        //pieView2.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView2.setPercentage(100);
        pieView2.setInnerText(NumberFormat.getNumberInstance(Locale.getDefault()).format(inPeriodProcessing) + "건");

        PieAngleAnimation animation2 = new PieAngleAnimation(pieView2);
        animation2.setDuration(3500);
        pieView2.startAnimation(animation2);

        //기간초과처리율 그래프
        pieView3.setPercentageBackgroundColor(getResources().getColor(R.color.colorPiePurle));
        pieView3.setPercentage(100);
        pieView3.setInnerText(NumberFormat.getNumberInstance(Locale.getDefault()).format(overdueProcessing) + "건");

        PieAngleAnimation animation3 = new PieAngleAnimation(pieView3);
        animation3.setDuration(3600);
        pieView3.startAnimation(animation3);

        //기간초과미처리율 그래프
        pieView4.setPercentageBackgroundColor(getResources().getColor(R.color.colorPieRed));
        pieView4.setPercentage(100);
        pieView4.setInnerText(NumberFormat.getNumberInstance(Locale.getDefault()).format(overdueUnprocessing) + "건");

        PieAngleAnimation animation4 = new PieAngleAnimation(pieView4);
        animation4.setDuration(3700);
        pieView4.startAnimation(animation4);
    }
}
