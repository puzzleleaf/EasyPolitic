package leesd.crossithackathon.junsu;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @BindView(R.id.junsu_text_subdata1) TextView inPeriodProcessingText;    //기간내처리 텍스트
    @BindView(R.id.junsu_text_subdata2) TextView overdueProcessingText;     //기간초과처리 텍스트
    @BindView(R.id.junsu_text_subdata3) TextView overdueUnprocessingText;   //기간초과미처리 텍스트

    private int year = 2017;    //2017년
    private int term = 3;       //3분기

    private String markerAgency;    //기관명
    private double junsuRatio;  //처리 준수율
    private int total, inPeriodProcessing, overdueProcessing, overdueUnprocessing; //총 접수, 기간내처리건수, 기간초과처리건수, 기간초과미처리건수

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

        totalText.setText("총 "+ String.valueOf(total) + " 건");
        inPeriodProcessingText.setText("△ 50");
        overdueProcessingText.setText(String.valueOf("▽ 3"));
        overdueUnprocessingText.setText(String.valueOf("- 0"));

        return ccrv.getHandling_rate();
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
        int data1Ratio = (int)((float)inPeriodProcessing / total * 100);
        //pieView2.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView2.setPercentage((float)data1Ratio);
        pieView2.setInnerText(String.valueOf(inPeriodProcessing) + "건");

        PieAngleAnimation animation2 = new PieAngleAnimation(pieView2);
        animation2.setDuration(3500);
        pieView2.startAnimation(animation2);

        //기간초과처리율 그래프
        int data2Ratio = (int)((float)overdueProcessing / total * 100);
        //int data2Ratio = (int)((float)overdueProcessing / total * 10000);
        //pieView3.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        if(overdueProcessing == 0)
            pieView3.setPercentage(0);
        else
            pieView3.setPercentage(2);
        pieView3.setInnerText(String.valueOf(overdueProcessing) + "건");

        PieAngleAnimation animation3 = new PieAngleAnimation(pieView3);
        animation3.setDuration(3500);
        pieView3.startAnimation(animation3);

        //기간초과미처리율 그래프
        int data3Ratio = (int)((float)overdueUnprocessing / total * 100);
        //pieView4.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        if(overdueUnprocessing == 0)
            pieView4.setPercentage(0);
        else
            pieView4.setPercentage(1);
        pieView4.setInnerText(String.valueOf(overdueUnprocessing) + "건");

        PieAngleAnimation animation4 = new PieAngleAnimation(pieView4);
        animation4.setDuration(3500);
        pieView4.startAnimation(animation4);
    }
}
