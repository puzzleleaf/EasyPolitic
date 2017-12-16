package leesd.crossithackathon.Info;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import leesd.crossithackathon.DataManager.DetailsExcelFile;
import leesd.crossithackathon.R;

/*
 * Created by qlyh8 on 2017-12-14.
 * 기관 세부설명 화면
 */

public class DetailIntroTab extends Fragment{

    private TextView agencyText, addressText, phoneText, infoText;
    private View goHomepage, goChat;

    String markerData;
    HashMap<String, String> hashMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_intro, container, false);

        agencyText = (TextView) view.findViewById(R.id.detail_intro_agency);
        addressText = (TextView)view.findViewById(R.id.detail_intro_address);
        phoneText = (TextView)view.findViewById(R.id.detail_intro_phone);
        infoText = (TextView)view.findViewById(R.id.detail_intro_info);
        goHomepage = view.findViewById(R.id.detail_intro_homepage);
        goChat = view.findViewById(R.id.detail_intro_chat);

        markerData = getActivity().getIntent().getStringExtra("markerData");
        hashMap = getAgencyDetail(markerData);

        agencyText.setText(markerData);
        addressText.setText(hashMap.get("DT_ADDR"));
        phoneText.setText(hashMap.get("DT_PHONE"));
        infoText.setText(hashMap.get("DT_INFO"));

        goHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = hashMap.get("DT_URL");
                if(!url.equals("no url")){
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    //엑셀에서 기관 세부 정보 가져오기
    public HashMap<String, String> getAgencyDetail(String agency){

        DetailsExcelFile detailsExcelFile = new DetailsExcelFile(getContext());
        HashMap<String, String> HMap = detailsExcelFile.selectByAgency(agency);

        if(HMap.get("DT_ADDR").equals(""))
            HMap.put("DT_ADDR", "No address");
        if(HMap.get("DT_PHONE").equals(""))
            HMap.put("DT_PHONE", "No number");
        if(HMap.get("DT_INFO").equals(""))
            HMap.put("DT_INFO", "No information");
        if(HMap.get("DT_URL").equals(""))
            HMap.put("DT_URL", "no url");

        return HMap;
    }
}
