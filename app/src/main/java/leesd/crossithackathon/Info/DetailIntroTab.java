package leesd.crossithackathon.Info;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import leesd.crossithackathon.R;

/*
 * Created by qlyh8 on 2017-12-14.
 */

public class DetailIntroTab extends Fragment{

    private TextView agencyText, addressText, phoneText, infoText;
    private View goHomepage;

    String markerData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_intro, container, false);

        agencyText = (TextView) view.findViewById(R.id.detail_intro_agency);
        addressText = (TextView)view.findViewById(R.id.detail_intro_address);
        phoneText = (TextView)view.findViewById(R.id.detail_intro_phone);
        infoText = (TextView)view.findViewById(R.id.detail_intro_info);
        goHomepage = view.findViewById(R.id.detail_intro_homepage);

        markerData = getActivity().getIntent().getStringExtra("markerData");

        agencyText.setText(markerData);

        return view;
    }
}
