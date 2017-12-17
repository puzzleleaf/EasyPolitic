package leesd.crossithackathon.Info;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by cmtyx on 2017-12-17.
 */


public class TabAdapter_Detail_Review extends FragmentStatePagerAdapter {


    private int tabCount;

    public TabAdapter_Detail_Review(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                DetailReviewContentsTab tabFragment2 = new DetailReviewContentsTab();
                return tabFragment2;
            default:
            case 0:
                DetailReviewStarTab tabFragment1 = new DetailReviewStarTab();
                return tabFragment1;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
