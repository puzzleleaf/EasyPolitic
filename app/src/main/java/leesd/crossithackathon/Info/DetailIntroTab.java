package leesd.crossithackathon.Info;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import leesd.crossithackathon.DataManager.DetailsExcelFile;
import leesd.crossithackathon.Fb.FbObject;
import leesd.crossithackathon.R;

/*
 * Created by qlyh8 on 2017-12-14.
 * 기관 세부설명 화면
 */

public class DetailIntroTab extends Fragment{

    private TextView agencyText, addressText, phoneText, infoText;
    private View goHomepage, goChat;
    private RatingBar ratingBar;
    private TextView ratingText;
    private TextView ratingTotal;

    String markerData;
    HashMap<String, String> hashMap;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_intro, container, false);

        agencyText = (TextView) view.findViewById(R.id.detail_intro_agency);
        addressText = (TextView)view.findViewById(R.id.detail_intro_address);
        phoneText = (TextView)view.findViewById(R.id.detail_intro_phone);
        infoText = (TextView)view.findViewById(R.id.detail_intro_info);
        goHomepage = view.findViewById(R.id.detail_intro_homepage);
        goChat = view.findViewById(R.id.detail_intro_chat);
        ratingBar = (RatingBar)view.findViewById(R.id.intro_rating);
        ratingText = (TextView)view.findViewById(R.id.intro_rating_text);
        ratingTotal = (TextView)view.findViewById(R.id.intro_rating_total);

        ratingBarInit(ratingBar);


        markerData = getActivity().getIntent().getStringExtra("markerData");
        hashMap = getAgencyDetail(markerData);

        agencyText.setText(markerData);
        addressText.setText(hashMap.get("DT_ADDR"));
        phoneText.setText(hashMap.get("DT_PHONE"));
        infoText.setText(hashMap.get("DT_INFO"));

        goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() ,DetailChatActivity.class);
                intent.putExtra("markerData",markerData);
                startActivity(intent);
            }
        });

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
        ratingLoad();

        return view;
    }

    private void ratingLoad(){
        FbObject.database.getReference().child("Rating").child(markerData).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ReviewTotalRating reviewTotalRating = dataSnapshot.getValue(ReviewTotalRating.class);
                if(reviewTotalRating == null) {
                    ratingBar.setRating(0);
                    ratingText.setText(String.format("%.2f",0.0));
                    ratingTotal.setText("0명");
                } else {
                    ratingBar.setRating(reviewTotalRating.getRating()/reviewTotalRating.getTotal());
                    ratingText.setText(String.format("%.2f", ratingBar.getRating()));
                    ratingTotal.setText(String.valueOf(reviewTotalRating.getTotal())+"명");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ratingBarInit(RatingBar ratingBar) {
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getContext(), R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    //엑셀에서 기관 세부 정보 가져오기
    public HashMap<String, String> getAgencyDetail(String agency){

        DetailsExcelFile detailsExcelFile = new DetailsExcelFile(getContext());
        HashMap<String, String> HMap = null;
        try{
            HMap = detailsExcelFile.selectByAgency(agency);
            if(HMap.get("DT_ADDR").equals(""))
                HMap.put("DT_ADDR", "No address");
            if(HMap.get("DT_PHONE").equals(""))
                HMap.put("DT_PHONE", "No number");
            if(HMap.get("DT_INFO").equals(""))
                HMap.put("DT_INFO", "No information");
            if(HMap.get("DT_URL").equals(""))
                HMap.put("DT_URL", "no url");
        } catch (Exception e) {
            Toast.makeText(getContext(),"데이터를 불러오는데 실패했습니다.",Toast.LENGTH_SHORT).show();
        }finally {
            if(HMap == null) {
                HMap = new HashMap<>();
            }
        }

        return HMap;
    }
}
