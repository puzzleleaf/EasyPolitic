package leesd.crossithackathon.Grievance;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by leesd on 2017-08-07.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public TabAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        switch (position) {
            case 0:
                GrievanceTotalFragment tabFragment1 = new GrievanceTotalFragment();
                return tabFragment1;
            case 1:
                GrievanceFieldFragment tabFragment2 = new GrievanceFieldFragment();
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
