package leesd.crossithackathon.Grievance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import leesd.crossithackathon.R;

/**
 * Created by leesd on 2017-08-07.
 */

public class GrievanceView extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabAdapter pagerAdapter;
    //
    private ImageView grievanceYearNext;
    private ImageView grievanceYearPrev;
    private TextView grievanceYear;
    private int dataYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance);

        //Year Data
        dataYear = 2016;
        grievanceYear = (TextView)findViewById(R.id.grievance_year);
        grievanceYear.setText(String.valueOf(dataYear));
        grievanceYearNext = (ImageView)findViewById(R.id.grievance_year_next);
        grievanceYearPrev = (ImageView)findViewById(R.id.grievance_year_prev);

        grievanceYearPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearPrevCheck();
                pagerRefresh();
            }
        });
        grievanceYearNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearNextCheck();
                pagerRefresh();
            }
        });


        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        // Creating TabPagerAdapter adapter
        pagerAdapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), dataYear);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager,true);

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void yearTextSet(){
        grievanceYear.setText(String.valueOf(dataYear));
    }
    private void dataYearPrevCheck(){
        if(dataYear>2010){
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
        pagerAdapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), dataYear);
        viewPager.setAdapter(pagerAdapter);
    }
}
