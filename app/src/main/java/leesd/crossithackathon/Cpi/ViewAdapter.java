package leesd.crossithackathon.Cpi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class ViewAdapter extends FragmentStatePagerAdapter {

    private int year;

    public ViewAdapter(FragmentManager fm, int year) {
        super(fm);
        this.year = year;
    }
    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        Bundle bundle = new Bundle(1);
        bundle.putInt("year", year);

        CpiApFragment fragment = new CpiApFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
