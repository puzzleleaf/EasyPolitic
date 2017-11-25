package leesd.crossithackathon.Info;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.HashMap;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import leesd.crossithackathon.DataManager.CivilComplaintRate;
import leesd.crossithackathon.DataManager.CivilComplaintRateVO;
import leesd.crossithackathon.DataManager.SatisfactionExcelFile;
import leesd.crossithackathon.R;
import leesd.crossithackathon.SurveyActivity;
import leesd.crossithackathon.data.MapList;

/**
 * Created by user on 2017-11-19.
 */

public class DetailDataTab extends Fragment {
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

    //소개
    private TextView introduce;

    //처리율
    private double complaintRatio;
    private ImageView complaintYearNext;
    private ImageView complaintYearPrev;
    private TextView complaintYearSemester;
    private TextView txt1, txt2, txt3, txt4;
    private int complaintYear, complaintSemester;
    private String yearSemester;

    //설문지
    private TextView survey;

    private View view_public;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_data, container, false);
        view_public = view;
        survey = (TextView)view.findViewById(R.id.survey);

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SurveyActivity.class);
                getActivity().startActivity(intent);
            }
        });


        markerData = getActivity().getIntent().getStringExtra("markerData");

        linkButton = (TextView)view.findViewById(R.id.link);

        complaintYear = 2017;
        complaintSemester = 2;
        yearSemester = complaintYear + "_" + complaintSemester;
        complaintYearNext = (ImageView)view.findViewById(R.id.complaint_year_semester_next);
        complaintYearPrev = (ImageView)view.findViewById(R.id.complaint_year_semester_prev);
        complaintYearSemester = (TextView)view.findViewById(R.id.complaint_year_semester);
        complaintYearSemester.setText(yearSemester);
        introduce = (TextView)view.findViewById(R.id.introduce);
        introduceInit();



        complaintYearNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearNextCheck();
                pageRefresh(view_public);
            }
        });

        complaintYearPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearPrevCheck();
                pageRefresh(view_public);
            }
        });

        info = new LovelyInfoDialog(view.getContext());


        titleInit(view);
        ratingBarInit(view);
        imageInit(view);
        complaintRatio = complaintRatioInit(markerData,yearSemester,view);
        setPieView(complaintRatio,view);

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
        view.findViewById(R.id.detail_satisfaction).setOnClickListener(new View.OnClickListener() {
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


        return view;
    }


    private void introduceInit() {
        if(markerData.equals("국방부")){
            introduce.setText("\n" +
                    "The Ministry of National Defense is the central administrative agency that oversees the defense related affairs. On November 13, 1945, it was established under the name of the National Defense Command to oversee the affairs of military affairs, command and military affairs related to defense. With the establishment of the government on August 15, 1948, it was expanded to the Ministry of National Defense according to the government establishment and government organizing law. It aims to protect the country from external military threats and invasions, support peaceful reunification, contribute to regional stability and world peace.");
        }else if(markerData.equals("경찰청")){
            introduce.setText("The Ministry of Public Security and Security (IRS) and the Security Bureau are in charge of maintaining the social order, and the spokesperson, the planning coordinator, the personnel affairs planner, the inspectors, We are in charge of administrative support.");
        }else if(markerData.equals("병무청")){
            introduce.setText("The MMA is an administrative agency under the Ministry of National Defense that oversees the military service. It manages and manages military service resources, imposes duties such as conscription and mobilization of soldiers, mobilization of exhibition forces, and organization and management of local reserve forces, support of industrial manpower, and permission to travel abroad.");
        }
    }
    public void setPieView(double complaintRatio, View view){
        PieView pieView = (PieView) view.findViewById(R.id.pieView);
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView.setInnerText(String.valueOf(complaintRatio));
        pieView.setPercentage((float)complaintRatio);
        PieView animatedPie = (PieView) view.findViewById(R.id.pieView);

        PieAngleAnimation animation = new PieAngleAnimation(animatedPie);
        animation.setDuration(3000); //This is the duration of the animation in millis
        animatedPie.startAnimation(animation);

    }

    public double complaintRatioInit(String institutionName, String yearSemester, View view){

        txt1 = (TextView)view.findViewById(R.id.total_receipt);
        txt2 = (TextView)view.findViewById(R.id.in_period_processing);
        txt3 = (TextView)view.findViewById(R.id.overdue_processing);
        txt4 = (TextView)view.findViewById(R.id.overdue_unprocessing);

        CivilComplaintRate ccr = new CivilComplaintRate(getActivity());
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
    private void titleInit(View view){
        //Excel에서 Data 가져오기
        SatisfactionExcelFile sf = new SatisfactionExcelFile(getActivity());
        HashMap<String, String> hashMap = sf.selectByName(markerData);

        TextView title = (TextView)view.findViewById(R.id.institutionName);
        title.setText(hashMap.get("SF_AGENCY"));
    }

    //Image
    private void imageInit(View view){
        logoImage = (ImageView)view.findViewById(R.id.detail_image);
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
    private void ratingBar(View view){
        //Excel에서 Data 가져오기
        SatisfactionExcelFile sf = new SatisfactionExcelFile(getActivity());
        TextView text1 = (TextView)view.findViewById(R.id.gradeText1);
        TextView text2 = (TextView)view.findViewById(R.id.gradeText2);
        TextView text3 = (TextView)view.findViewById(R.id.gradeText3);

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

    private void ratingBarInit(View view){
        ratingBarFir = (RatingBar)view.findViewById(R.id.detail_rating_bar_fir);
        ratingBarSec = (RatingBar)view.findViewById(R.id.detail_rating_bar_sec);
        ratingBarThr = (RatingBar)view.findViewById(R.id.detail_rating_bar_thr);

        ratingBarColorInit(ratingBarFir);
        ratingBarColorInit(ratingBarSec);
        ratingBarColorInit(ratingBarThr);

        ratingBar(view);
    }

    private void ratingBarColorInit(RatingBar ratingBar){
        LayerDrawable stars = (LayerDrawable)ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    private void pageRefresh(View view){
        yearSemester = complaintYear + "_"+complaintSemester;
        yearSemesterTextSet();
        complaintRatio = complaintRatioInit(markerData,yearSemester,view);
        setPieView(complaintRatio,view);
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
