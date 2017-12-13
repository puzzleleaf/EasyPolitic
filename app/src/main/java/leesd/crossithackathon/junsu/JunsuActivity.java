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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import leesd.crossithackathon.DataManager.JunsuExcelFile;
import leesd.crossithackathon.R;

/*
 * Created by qlyh8 on 2017-11-30.
 */

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

    @BindView(R.id.junsu_column_chart) ColumnChartView columnChart; //년도별 준수율 칼럼 그래프
    @BindView(R.id.junsu_line_chart) LineChartView lineChart; //년도별 준수율 선 그래프

    private int year = 2017;    //기준년도
    private int term = 3;       //기준분기

    private String markerAgency;    //기관명
    private double junsuRatio;  //처리 준수율
    private int total, inPeriodProcessing, overdueProcessing, overdueUnprocessing; //총 접수, 기간내처리건수, 기간초과처리건수, 기간초과미처리건수
    private int prevTotal, prevInPeriodProcessing, prevOverdueProcessing, prevOverdueUnprocessing;  //이전 분기 데이터

    private ColumnChartData columnData;
    public final static String[] fieldName = new String[4]; //년도별 준수율 그래프 칼럼 이름 (1-4분기)
    public final static Double[] fieldNumbers = new Double[4];    //년도별 준수율 그래프 칼럼 데이터
    private int duration = 2000;

    private LineChartData lineData;
    private int numberOfLines = 4;
    private int numberOfPoints = 4;
    private float[][] detailFieldNumbers = new float[numberOfLines][numberOfPoints]; //년도별 준수율 상세처리건수 데이터

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

        /* 화면 초기화 */
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

        /* 칼럼 그래프 */
        generateColumnData();
        prepareDataAnimation();
        columnChart.startDataAnimation(duration);

        /* 라인 그래프 */
        generateValues();
        generateData();
        linePrepareDataAnimation();
        lineChart.startDataAnimation(duration);
        ////////////////////////////////////////////////////////////////////////

        //탭 별 페이지 버튼 이벤트
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

        generateColumnData();
        prepareDataAnimation();
        columnChart.startDataAnimation(duration);

        generateValues();
        generateData();
        linePrepareDataAnimation();
        lineChart.startDataAnimation(duration);
    }

    //데이터 가져오기
    @SuppressLint("SetTextI18n")
    public double junsuDataInit(String institutionName, String yearSemester){

        JunsuExcelFile junsuExcelFile = new JunsuExcelFile(getBaseContext(), yearSemester);
        HashMap<String, String> hashMap = junsuExcelFile.selectByAgency(institutionName);

        total = Integer.parseInt(hashMap.get("JS_TOTAL"));
        inPeriodProcessing = Integer.parseInt(hashMap.get("JS_IN_SUCCESS"));
        overdueProcessing = Integer.parseInt(hashMap.get("JS_OUT_SUCCESS"));
        overdueUnprocessing = Integer.parseInt(hashMap.get("JS_OUT_FAIL"));

        totalText.setText("총 "+ NumberFormat.getNumberInstance(Locale.getDefault()).format(total) + " 건");
        calculatePrevNThisData();

        return Double.parseDouble(hashMap.get("JS_RATIO"));
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

        JunsuExcelFile junsuExcelFile = new JunsuExcelFile(getBaseContext(), thisYear+"_"+thisTerm);
        HashMap<String, String> hashMap = junsuExcelFile.selectByAgency(institutionName);

        prevTotal = Integer.parseInt(hashMap.get("JS_TOTAL"));
        prevInPeriodProcessing = Integer.parseInt(hashMap.get("JS_IN_SUCCESS"));
        prevOverdueProcessing  = Integer.parseInt(hashMap.get("JS_OUT_SUCCESS"));
        prevOverdueUnprocessing = Integer.parseInt(hashMap.get("JS_OUT_FAIL"));
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

    //년도별 준수율 그래프 초기화
    public void generateColumnData() {

        int numSubcolumns = 1;
        int numColumns = 4;

        List<AxisValue> axisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        HashMap<String, Double> hashMap = getYearJunsuRatio(markerAgency);

        //칼럼에 들어갈 Field 데이터 세팅
        for(int i = 0, j = 1 ; i < 4 ; i++, j++){
            fieldName[i] = j + "분기";
            fieldNumbers[i] = hashMap.get(year+"_"+j);
        }

        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                //values.add(new SubcolumnValue(fieldNumbers[i].floatValue(), ChartUtils.pickColor()));
                values.add(new SubcolumnValue((float) Math.random() * 100, ChartUtils.nextColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);

            axisValues.add(new AxisValue(i).setLabel(fieldName[i]));
        }

        columnData = new ColumnChartData(columns);
        columnData.setValueLabelBackgroundAuto(true);

        columnData.setAxisXBottom(new Axis(axisValues));
        columnData.setAxisYLeft(new Axis().setHasLines(true));

        columnChart.setColumnChartData(columnData);
        columnChart.setValueSelectionEnabled(true);
    }

    private void prepareDataAnimation() {

        int i = 0;
        for (Column column : columnData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setTarget(fieldNumbers[i++].floatValue());
                //value.setTarget((float) Math.random() * 100);
            }
        }
    }

    //년도별 준수율 데이터 가져오기
    private HashMap<String, Double> getYearJunsuRatio(String institutionName){

        HashMap<String, Double> hashMap = new HashMap<>();
        String yearSemester;

        for(int i = 1 ; i <= 4 ; i++){
            yearSemester = year + "_" + i;
            if(year == 2017 && i == 4) {
                hashMap.put(yearSemester, 0d);
            }
            else{
                JunsuExcelFile junsuExcelFile = new JunsuExcelFile(getBaseContext(), yearSemester);
                HashMap<String, String> HMap = junsuExcelFile.selectByAgency(institutionName);
                hashMap.put(yearSemester, Double.parseDouble(HMap.get("JS_RATIO")));
            }
        }

        return hashMap;
    }

    //년도별 상세처리건수 데이터 가져오기
    private HashMap<String, Integer> getYearJunsuDetail(String institutionName){

        HashMap<String, Integer> hashMap = new HashMap<>();
        String yearSemester;

        for(int i = 1 ; i <= 4 ; i++){
            yearSemester = year + "_" + i;
            if(year == 2017 && i == 4) {
                hashMap.put(yearSemester+"_TOTAL", 0);
                hashMap.put(yearSemester+"_IN_SUCCESS", 0);
                hashMap.put(yearSemester+"_OUT_SUCCESS", 0);
                hashMap.put(yearSemester+"_OUT_FAIL", 0);
            }
            else{
                JunsuExcelFile junsuExcelFile = new JunsuExcelFile(getBaseContext(), yearSemester);
                HashMap<String, String> HMap = junsuExcelFile.selectByAgency(institutionName);

                hashMap.put(yearSemester+"_TOTAL", Integer.parseInt(HMap.get("JS_TOTAL")));
                hashMap.put(yearSemester+"_IN_SUCCESS", Integer.parseInt(HMap.get("JS_IN_SUCCESS")));
                hashMap.put(yearSemester+"_OUT_SUCCESS",Integer.parseInt(HMap.get("JS_OUT_SUCCESS")));
                hashMap.put(yearSemester+"_OUT_FAIL",Integer.parseInt(HMap.get("JS_OUT_FAIL")));
            }
        }

        return hashMap;
    }

    //년도별 상세처리건수 데이터 초기화
    private void generateValues() {

        HashMap<String, Integer> hashMap = getYearJunsuDetail(markerAgency);

        for (int i = 0; i < numberOfLines; ++i) {
            switch (i){
                case 0:
                    for (int j = 0; j < numberOfPoints; ++j) {
                        int k = j+1;
                        detailFieldNumbers[i][j] = hashMap.get(year+"_"+k+"_TOTAL");
                    }
                    break;
                case 1:
                    for (int j = 0; j < numberOfPoints; ++j) {
                        int k = j+1;
                        detailFieldNumbers[i][j] = hashMap.get(year+"_"+k+"_IN_SUCCESS");
                    }
                    break;
                case 2:
                    for (int j = 0; j < numberOfPoints; ++j) {
                        int k = j+1;
                        detailFieldNumbers[i][j] = hashMap.get(year+"_"+k+"_OUT_SUCCESS");
                    }
                    break;
                case 3:
                    for (int j = 0; j < numberOfPoints; ++j) {
                        int k = j+1;
                        detailFieldNumbers[i][j] = hashMap.get(year+"_"+k+"_OUT_FAIL");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //년도별 상세처리건수 그래프
    private void generateData() {

        List<Line> lines = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();

        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<>();

            for (int j = 0; j < numberOfPoints; ++j) {
                //values.add(new PointValue(j, detailFieldNumbers[i][j]));
                values.add(new PointValue(j,(float) Math.random() * 100000));
                axisValues.add(new AxisValue(i).setLabel(fieldName[i]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(ValueShape.CIRCLE);
            line.setCubic(false);
            line.setFilled(false);
            line.setHasLabels(true);
            line.setHasLabelsOnlyForSelected(true);
            line.setHasLines(true);
            line.setHasPoints(true);
            line.setPointColor(ChartUtils.COLORS[i]);
            //line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);

            lines.add(line);
        }

        lineData = new LineChartData(lines);


        Axis axisY = new Axis().setHasLines(true).setMaxLabelChars(6);

        lineData.setAxisXBottom(new Axis(axisValues));
        lineData.setAxisYLeft(axisY);

        lineData.setBaseValue(Float.NEGATIVE_INFINITY);
        lineChart.setLineChartData(lineData);
        lineChart.setValueSelectionEnabled(true);
    }

    private void linePrepareDataAnimation() {

        int i = 0 , j = 0;
        for (Line line : lineData.getLines()) {
            for (PointValue value : line.getValues()) {
                // Here I modify target only for Y values but it is OK to modify X targets as well.
                value.setTarget(value.getX(), detailFieldNumbers[i][j++]);
            }
            i++;
            j=0;
        }
    }
}
