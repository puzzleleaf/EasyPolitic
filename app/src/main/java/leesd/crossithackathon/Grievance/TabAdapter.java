package leesd.crossithackathon.Grievance;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by leesd on 2017-08-07.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private int year;

    public TabAdapter(FragmentManager fm, int tabCount, int year) {
        super(fm);
        this.tabCount = tabCount;
        this.year =year;
    }
    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        Bundle bundle = new Bundle(1);
        bundle.putInt("year",year);
        switch (position) {
            case 0:
                GrievanceTotalFragment tabFragment1 = new GrievanceTotalFragment();
                tabFragment1.setArguments(bundle);
                return tabFragment1;
            case 1:
                GrievanceFieldFragment tabFragment2 = new GrievanceFieldFragment();
                tabFragment2.setArguments(bundle);
                return tabFragment2;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
