package leesd.crossithackathon.Grievance;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import leesd.crossithackathon.DataManager.GrievanceTotalExcelFile;
import leesd.crossithackathon.R;

/**
 * Created by leesd on 2017-08-07.
 */

public class GrievanceTotalFragment extends Fragment {
    TextView info1, info2, info3, info4, info5, info6, info7;
    float processRate, quoteRate; //처리율, 인용률
    private int year;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_grievance_total,container,false);
        year = getArguments().getInt("year");

        info1 = (TextView)view.findViewById(R.id.infoText1);
        info2 = (TextView)view.findViewById(R.id.infoText2);
        info3 = (TextView)view.findViewById(R.id.infoText3);
        info4 = (TextView)view.findViewById(R.id.infoText4);
        info5 = (TextView)view.findViewById(R.id.infoText5);
        info6 = (TextView)view.findViewById(R.id.infoText6);
        info7 = (TextView)view.findViewById(R.id.infoText7);

        infoInit();

        PieView pieView = (PieView)view.findViewById(R.id.pieView);
        pieView.setPercentageBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorGraph));
        pieView.setPercentage(processRate);
        pieView.setInnerText(String.valueOf((int)processRate));
        PieView animatedPie = (PieView)view.findViewById(R.id.pieView);

        PieAngleAnimation animation = new PieAngleAnimation(animatedPie);
        animation.setDuration(3000); //This is the duration of the animation in millis
        animatedPie.startAnimation(animation);


        PieView pieView2 = (PieView)view.findViewById(R.id.pieView2);
        pieView2.setPercentageBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorGraph));
        pieView2.setPercentage(quoteRate);
        pieView2.setInnerText(String.valueOf(quoteRate));
        PieView animatedPie2 = (PieView)view.findViewById(R.id.pieView2);

        PieAngleAnimation animation2 = new PieAngleAnimation(animatedPie2);
        animation2.setDuration(3000); //This is the duration of the animation in millis
        animatedPie2.startAnimation(animation2);


        return view;
    }


    private void infoInit(){
        GrievanceTotalExcelFile gt = new GrievanceTotalExcelFile(getContext());


        HashMap<String, String> hashMap =  gt.selectByYear(year);


        info1.setText(hashMap.get("GT_RECEIVE"));
        info2.setText(hashMap.get("GT_PROCESS"));
        info3.setText(hashMap.get("GT_GRIEVANCE"));
        info4.setText(hashMap.get("GT_NOT_GRIEVANCE"));
        info5.setText(hashMap.get("GT_QUOTE"));
        info6.setText(hashMap.get("GT_DATE"));
        info7.setText(hashMap.get("GT_SATISFACTION"));

        processRate = (Float.parseFloat(hashMap.get("GT_PROCESS").replace(",","").trim())/Float.parseFloat(hashMap.get("GT_RECEIVE").replace(",","").trim()))*100;//','문자열 지우고 온전한 정수형으로 받아온다.
        quoteRate = Float.parseFloat(hashMap.get("GT_QUOTE_PERCENT").replace("%","").trim());

    }
}
