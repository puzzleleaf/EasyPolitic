package leesd.crossithackathon.junsu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import leesd.crossithackathon.R;

//민원 준수율
public class JunsuActivity  extends AppCompatActivity {

    @BindView(R.id.junsu_top_back) ImageView goBack;    //이전 버튼
    @BindView(R.id.junsu_top_agency_text) TextView agencyName;  //기관 이름

    String markerData;  //마커 클릭시, 기관 이름 가져옴


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junsu);
        ButterKnife.bind(this);

        //이전으로 돌아가기
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //기관 이름 가져오기
        Intent intent = getIntent();
        agencyName.setText(intent.getStringExtra("markerData"));

    }
}
