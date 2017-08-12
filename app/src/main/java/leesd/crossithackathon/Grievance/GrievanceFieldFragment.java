package leesd.crossithackathon.Grievance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import leesd.crossithackathon.DataManager.GrievanceFieldExcelFile;
import leesd.crossithackathon.R;


public class GrievanceFieldFragment extends Fragment {

    GrievanceFieldExcelFile gf;
    HashMap<String, Object> hashMap;


    private int year;
    public final static String[] fields = new String[10];
    public final static Object[] fieldNumbers = new Object[10];

    private ColumnChartView chartBottom;

    LovelyInfoDialog info;              //분야별 고충민원 설명 다이얼로그

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_grievance_field,container,false);
        year = getArguments().getInt("year");
        gf = new GrievanceFieldExcelFile(getContext());
        hashMap = gf.rank(year);

        for(int i = 0, j=1 ; i < 10 ; i++, j++){ //Field 데이터 세팅
            //String temp1 = "TOP10_FIELD" + j;
            String temp2 = "TOP10_NUMBER" + j;

            //fields[i] = (String)hashMap.get(temp1);
            fields[i] = "" + j;
            fieldNumbers[i] = hashMap.get(temp2);
        }


        chartBottom = (ColumnChartView) view.findViewById(R.id.chart_bottom);
        generateColumnData();


        swipeData(view);


        //분야별 고충민원 설명
        info = new LovelyInfoDialog(getContext());
        view.findViewById(R.id.field_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setTopColorRes(R.color.colorBlue)
                        .setIcon(R.drawable.info2)
                        .setTitle("Civil Petition for Grievance\nby Field")
                        .setMessage("Major handling fields (25)\nfor civil petitions for grievance\nexcept other complaints")
                        .setConfirmButtonText("OK")
                        .show();
            }
        });

        return view;
    }

    public void generateColumnData() {

        int numSubcolumns = 1;
        int numColumns = fields.length;

        List<AxisValue> axisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;

        for (int i = 0, j = 1; i < numColumns; ++i, ++j) {

            values = new ArrayList<>();
            for (int k = 0; k < numSubcolumns; ++k) {
                values.add(new SubcolumnValue((int)fieldNumbers[i], ChartUtils.pickColor()));
            }

            axisValues.add(new AxisValue(i).setLabel(fields[i]));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        ColumnChartData columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));

        chartBottom.setColumnChartData(columnData);

        // Set selection mode to keep selected month column highlighted.
        chartBottom.setValueSelectionEnabled(true);

        chartBottom.setZoomType(ZoomType.HORIZONTAL);
    }

    public void swipeData(View view) {

        SwipeSelector swipeSelector1 = (SwipeSelector) view.findViewById(R.id.swipe1);
        swipeSelector1.setItems(
                new SwipeItem(0,  "1st. " + hashMap.get("RANK_FIELD1"), "Cases: " + hashMap.get("RANK_NUMBER1") + " " + hashMap.get("RANK_DIFF1") + "\n   Rate: " + hashMap.get("RANK_PERCENT1") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT1"))
        );
        SwipeSelector swipeSelector2 = (SwipeSelector) view.findViewById(R.id.swipe2);
        swipeSelector2.setItems(
                new SwipeItem(0, "2nd. " + hashMap.get("RANK_FIELD2"), "Cases: " + hashMap.get("RANK_NUMBER2") + " " + hashMap.get("RANK_DIFF2") + "\n   Rate: " + hashMap.get("RANK_PERCENT2") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT2"))
        );
        SwipeSelector swipeSelector3 = (SwipeSelector) view.findViewById(R.id.swipe3);
        swipeSelector3.setItems(
                new SwipeItem(0, "3rd. " + hashMap.get("RANK_FIELD3"), "Cases: " + hashMap.get("RANK_NUMBER3") + " " + hashMap.get("RANK_DIFF3") + "\n   Rate: " + hashMap.get("RANK_PERCENT3") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT3"))
        );
        SwipeSelector swipeSelector4 = (SwipeSelector) view.findViewById(R.id.swipe4);
        swipeSelector4.setItems(
                new SwipeItem(0, "4th. " + hashMap.get("RANK_FIELD4"), "Cases: " + hashMap.get("RANK_NUMBER4") + " " + hashMap.get("RANK_DIFF4") + "\n   Rate: " + hashMap.get("RANK_PERCENT4") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT4"))
        );
        SwipeSelector swipeSelector5 = (SwipeSelector) view.findViewById(R.id.swipe5);
        swipeSelector5.setItems(
                new SwipeItem(0, "5th. " + hashMap.get("RANK_FIELD5"), "Cases: " + hashMap.get("RANK_NUMBER5") + " " + hashMap.get("RANK_DIFF5") + "\n   Rate: " + hashMap.get("RANK_PERCENT5") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT5"))
        );
        SwipeSelector swipeSelector6 = (SwipeSelector) view.findViewById(R.id.swipe6);
        swipeSelector6.setItems(
                new SwipeItem(0, "6th. " + hashMap.get("RANK_FIELD6"), "Cases: " + hashMap.get("RANK_NUMBER6") + " " + hashMap.get("RANK_DIFF6") + "\n   Rate: " + hashMap.get("RANK_PERCENT6") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT6"))
        );
        SwipeSelector swipeSelector7 = (SwipeSelector) view.findViewById(R.id.swipe7);
        swipeSelector7.setItems(
                new SwipeItem(0, "7th. " + hashMap.get("RANK_FIELD7"), "Cases: " + hashMap.get("RANK_NUMBER7") + " " + hashMap.get("RANK_DIFF7") + "\n   Rate: " + hashMap.get("RANK_PERCENT7") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT7"))
        );
        SwipeSelector swipeSelector8 = (SwipeSelector) view.findViewById(R.id.swipe8);
        swipeSelector8.setItems(
                new SwipeItem(0, "8th. " + hashMap.get("RANK_FIELD8"), "Cases: " + hashMap.get("RANK_NUMBER8") + " " + hashMap.get("RANK_DIFF8") + "\n   Rate: " + hashMap.get("RANK_PERCENT8") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT8"))
        );
        SwipeSelector swipeSelector9 = (SwipeSelector) view.findViewById(R.id.swipe9);
        swipeSelector9.setItems(
                new SwipeItem(0, "9th. " + hashMap.get("RANK_FIELD9"), "Cases: " + hashMap.get("RANK_NUMBER9") + " " + hashMap.get("RANK_DIFF9") + "\n   Rate: " + hashMap.get("RANK_PERCENT9") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT9"))
        );
        SwipeSelector swipeSelector10 = (SwipeSelector) view.findViewById(R.id.swipe10);
        swipeSelector10.setItems(
                new SwipeItem(0, "10th. " + hashMap.get("RANK_FIELD10"), "Cases: " + hashMap.get("RANK_NUMBER10") + " " + hashMap.get("RANK_DIFF10") + "\n   Rate: " + hashMap.get("RANK_PERCENT10") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT10"))
        );
        SwipeSelector swipeSelector11 = (SwipeSelector) view.findViewById(R.id.swipe11);
        swipeSelector11.setItems(
                new SwipeItem(0, "11th. " + hashMap.get("RANK_FIELD11"), "Cases: " + hashMap.get("RANK_NUMBER11") + " " + hashMap.get("RANK_DIFF11") + "\n   Rate: " + hashMap.get("RANK_PERCENT11") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT11"))
        );
        SwipeSelector swipeSelector12 = (SwipeSelector) view.findViewById(R.id.swipe12);
        swipeSelector12.setItems(
                new SwipeItem(0, "12th. " + hashMap.get("RANK_FIELD12"), "Cases: " + hashMap.get("RANK_NUMBER12") + " " + hashMap.get("RANK_DIFF12") + "\n   Rate: " + hashMap.get("RANK_PERCENT12") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT12"))
        );
        SwipeSelector swipeSelector13 = (SwipeSelector) view.findViewById(R.id.swipe13);
        swipeSelector13.setItems(
                new SwipeItem(0, "13th. " + hashMap.get("RANK_FIELD13"), "Cases: " + hashMap.get("RANK_NUMBER13") + " " + hashMap.get("RANK_DIFF13") + "\n   Rate: " + hashMap.get("RANK_PERCENT13") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT13"))
        );
        SwipeSelector swipeSelector14 = (SwipeSelector) view.findViewById(R.id.swipe14);
        swipeSelector14.setItems(
                new SwipeItem(0, "14th. " + hashMap.get("RANK_FIELD14"), "Cases: " + hashMap.get("RANK_NUMBER14") + " " + hashMap.get("RANK_DIFF14") + "\n   Rate: " + hashMap.get("RANK_PERCENT14") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT14"))
        );
        SwipeSelector swipeSelector15 = (SwipeSelector) view.findViewById(R.id.swipe15);
        swipeSelector15.setItems(
                new SwipeItem(0, "15th. " + hashMap.get("RANK_FIELD15"), "Cases: " + hashMap.get("RANK_NUMBER15") + " " + hashMap.get("RANK_DIFF15") + "\n   Rate: " + hashMap.get("RANK_PERCENT15") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT15"))
        );
        SwipeSelector swipeSelector16 = (SwipeSelector) view.findViewById(R.id.swipe16);
        swipeSelector16.setItems(
                new SwipeItem(0, "16th. " + hashMap.get("RANK_FIELD16"), "Cases: " + hashMap.get("RANK_NUMBER16") + " " + hashMap.get("RANK_DIFF16") + "\n   Rate: " + hashMap.get("RANK_PERCENT16") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT16"))
        );
        SwipeSelector swipeSelector17 = (SwipeSelector) view.findViewById(R.id.swipe17);
        swipeSelector17.setItems(
                new SwipeItem(0, "17th. " + hashMap.get("RANK_FIELD17"), "Cases: " + hashMap.get("RANK_NUMBER17") + " " + hashMap.get("RANK_DIFF17") + "\n   Rate: " + hashMap.get("RANK_PERCENT17") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT17"))
        );
        SwipeSelector swipeSelector18 = (SwipeSelector) view.findViewById(R.id.swipe18);
        swipeSelector18.setItems(
                new SwipeItem(0, "18th. " + hashMap.get("RANK_FIELD18"), "Cases: " + hashMap.get("RANK_NUMBER18") + " " + hashMap.get("RANK_DIFF18") + "\n   Rate: " + hashMap.get("RANK_PERCENT18") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT18"))
        );
        SwipeSelector swipeSelector19 = (SwipeSelector) view.findViewById(R.id.swipe19);
        swipeSelector19.setItems(
                new SwipeItem(0, "19th. " + hashMap.get("RANK_FIELD19"), "Cases: " + hashMap.get("RANK_NUMBER19") + " " + hashMap.get("RANK_DIFF19") + "\n   Rate: " + hashMap.get("RANK_PERCENT19") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT19"))
        );
        SwipeSelector swipeSelector20 = (SwipeSelector) view.findViewById(R.id.swipe20);
        swipeSelector20.setItems(
                new SwipeItem(0, "20th. " + hashMap.get("RANK_FIELD20"), "Cases: " + hashMap.get("RANK_NUMBER20") + " " + hashMap.get("RANK_DIFF20") + "\n   Rate: " + hashMap.get("RANK_PERCENT20") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT20"))
        );
        SwipeSelector swipeSelector21 = (SwipeSelector) view.findViewById(R.id.swipe21);
        swipeSelector21.setItems(
                new SwipeItem(0, "21th. " + hashMap.get("RANK_FIELD21"), "Cases: " + hashMap.get("RANK_NUMBER21") + " " + hashMap.get("RANK_DIFF21") + "\n   Rate: " + hashMap.get("RANK_PERCENT21") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT21"))
        );
        SwipeSelector swipeSelector22 = (SwipeSelector) view.findViewById(R.id.swipe22);
        swipeSelector22.setItems(
                new SwipeItem(0, "22th. " + hashMap.get("RANK_FIELD22"), "Cases: " + hashMap.get("RANK_NUMBER22") + " " + hashMap.get("RANK_DIFF22") + "\n   Rate: " + hashMap.get("RANK_PERCENT22") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT22"))
        );
        SwipeSelector swipeSelector23 = (SwipeSelector) view.findViewById(R.id.swipe23);
        swipeSelector23.setItems(
                new SwipeItem(0, "23th. " + hashMap.get("RANK_FIELD23"), "Cases: " + hashMap.get("RANK_NUMBER23") + " " + hashMap.get("RANK_DIFF23") + "\n   Rate: " + hashMap.get("RANK_PERCENT23") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT23"))
        );
        SwipeSelector swipeSelector24 = (SwipeSelector) view.findViewById(R.id.swipe24);
        swipeSelector24.setItems(
                new SwipeItem(0, "24th. " + hashMap.get("RANK_FIELD24"), "Cases: " + hashMap.get("RANK_NUMBER24") + " " + hashMap.get("RANK_DIFF24") + "\n   Rate: " + hashMap.get("RANK_PERCENT24") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT24"))
        );
        SwipeSelector swipeSelector25 = (SwipeSelector) view.findViewById(R.id.swipe25);
        swipeSelector25.setItems(
                new SwipeItem(0, "25th. " + hashMap.get("RANK_FIELD25"), "Cases: " + hashMap.get("RANK_NUMBER25") + " " + hashMap.get("RANK_DIFF25") + "\n   Rate: " + hashMap.get("RANK_PERCENT25") + "%"),
                new SwipeItem(1, "Handling Details", (String)hashMap.get("RANK_CONTENT25"))
        );

    }
}
