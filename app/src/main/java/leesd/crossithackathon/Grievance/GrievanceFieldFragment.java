package leesd.crossithackathon.Grievance;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import leesd.crossithackathon.DataManager.GrievanceFieldExcelFile;
import leesd.crossithackathon.R;

/**
 * Created by leesd on 2017-08-07.
 */

public class GrievanceFieldFragment extends Fragment {
    TextView[] fieldInfo= new TextView[26];
    TextView titleYear, total;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_grievance_field,container,false);


        textInit(view);
        infoInit();


        return view;
    }

    private void textInit(View view){
        fieldInfo[0] = (TextView)view.findViewById(R.id.fieldInfo1);
        fieldInfo[1] = (TextView)view.findViewById(R.id.fieldInfo2);
        fieldInfo[2] = (TextView)view.findViewById(R.id.fieldInfo3);
        fieldInfo[3] = (TextView)view.findViewById(R.id.fieldInfo4);
        fieldInfo[4] = (TextView)view.findViewById(R.id.fieldInfo5);
        fieldInfo[5] = (TextView)view.findViewById(R.id.fieldInfo6);
        fieldInfo[6] = (TextView)view.findViewById(R.id.fieldInfo7);
        fieldInfo[7] = (TextView)view.findViewById(R.id.fieldInfo8);
        fieldInfo[8] = (TextView)view.findViewById(R.id.fieldInfo9);
        fieldInfo[9] = (TextView)view.findViewById(R.id.fieldInfo10);
        fieldInfo[10] = (TextView)view.findViewById(R.id.fieldInfo11);
        fieldInfo[11] = (TextView)view.findViewById(R.id.fieldInfo12);
        fieldInfo[12] = (TextView)view.findViewById(R.id.fieldInfo13);
        fieldInfo[13] = (TextView)view.findViewById(R.id.fieldInfo14);
        fieldInfo[14] = (TextView)view.findViewById(R.id.fieldInfo15);
        fieldInfo[15] = (TextView)view.findViewById(R.id.fieldInfo16);
        fieldInfo[16] = (TextView)view.findViewById(R.id.fieldInfo17);
        fieldInfo[17] = (TextView)view.findViewById(R.id.fieldInfo18);
        fieldInfo[18] = (TextView)view.findViewById(R.id.fieldInfo19);
        fieldInfo[19] = (TextView)view.findViewById(R.id.fieldInfo20);
        fieldInfo[20] = (TextView)view.findViewById(R.id.fieldInfo21);
        fieldInfo[21] = (TextView)view.findViewById(R.id.fieldInfo22);
        fieldInfo[22] = (TextView)view.findViewById(R.id.fieldInfo23);
        fieldInfo[23] = (TextView)view.findViewById(R.id.fieldInfo24);
        fieldInfo[24] = (TextView)view.findViewById(R.id.fieldInfo25);
        fieldInfo[25] = (TextView)view.findViewById(R.id.fieldInfo26);

        titleYear = (TextView)view.findViewById(R.id.year);
        total = (TextView)view.findViewById(R.id.total);
    }

    private void infoInit(){
        GrievanceFieldExcelFile gf = new GrievanceFieldExcelFile(getContext());

        HashMap<String, String> hashMap = gf.selectByYear(getActivity().getIntent().getExtras().getInt("year"));

        for(int i = 0, j=1 ; i < 26 ; i++, j++){ //Field 데이터 세팅
            String temp = "GF_FIELD";
            temp+=j;
            fieldInfo[i].setText(hashMap.get(temp));
        }
        titleYear.setText(String.valueOf(getActivity().getIntent().getExtras().getInt("year"))+"년");
        total.setText(hashMap.get("GF_TOTAL"));
    }
}
