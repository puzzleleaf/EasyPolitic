package leesd.crossithackathon.serviceEval;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import leesd.crossithackathon.R;

/*
 * Created by YUNHEE on 2017-12-13.
 * 민원서비스평가 안내 화면
 */
@SuppressLint("Registered")
public class ServiceEvalInfoActivity extends AppCompatActivity {

    @BindView(R.id.service_eval_info_top_back) ImageView goBack;    //이전 버튼

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_eval_info);
        ButterKnife.bind(this);

        //이전으로 돌아가기
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
