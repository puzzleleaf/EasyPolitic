package leesd.crossithackathon.Info;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by leesd on 2017-08-07.
 */

public class TabAdapter_Detail extends FragmentStatePagerAdapter {

    private int tabCount;

    public TabAdapter_Detail(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        switch (position) {
            case 0:
                DetailIntroTab tabFragment1 = new DetailIntroTab();
                return tabFragment1;
            case 1:
                DetailDataTab tabFragment2 = new DetailDataTab();
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
