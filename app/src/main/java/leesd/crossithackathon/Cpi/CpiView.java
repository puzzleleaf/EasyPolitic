    package leesd.crossithackathon.Cpi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import leesd.crossithackathon.R;

public class CpiView extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewAdapter viewAdapter;
    //
    private ImageView cpiYearNext;
    private ImageView cpiYearPrev;
    private TextView cpiYear;
    private int dataYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpi);

        //Year Data
        dataYear = 2016;
        cpiYear = (TextView)findViewById(R.id.cpi_year);
        cpiYear.setText(String.valueOf(dataYear));
        cpiYearNext = (ImageView)findViewById(R.id.cpi_year_next);
        cpiYearPrev = (ImageView)findViewById(R.id.cpi_year_prev);

        cpiYearPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearPrevCheck();
                pagerRefresh();
            }
        });
        cpiYearNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearNextCheck();
                pagerRefresh();
            }
        });

        viewPager = (ViewPager)findViewById(R.id.cpi_view_pager);
        viewAdapter = new ViewAdapter(getSupportFragmentManager(), dataYear);
        viewPager.setAdapter(viewAdapter);
    }


    private void yearTextSet(){
        cpiYear.setText(String.valueOf(dataYear));
    }
    private void dataYearPrevCheck(){
        if(dataYear>2012){
            dataYear--;
        }
    }
    private void dataYearNextCheck(){
        if(dataYear<2016){
            dataYear++;
        }
    }
    private void pagerRefresh(){
        yearTextSet();
        viewAdapter = new ViewAdapter(getSupportFragmentManager(), dataYear);
        viewPager.setAdapter(viewAdapter);
        //viewPager.setCurrentItem(0);
    }
}
