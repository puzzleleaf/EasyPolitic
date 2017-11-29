package leesd.crossithackathon.Info;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leesd.crossithackathon.R;

/**
 * Created by user on 2017-11-19.
 */

public class DetailIntroTab extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_intro, container, false);


        return view;
    }
}
