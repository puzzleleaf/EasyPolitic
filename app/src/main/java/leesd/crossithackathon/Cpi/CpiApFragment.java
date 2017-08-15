package leesd.crossithackathon.Cpi;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import leesd.crossithackathon.DataManager.CpiExcelFile;
import leesd.crossithackathon.R;


public class CpiApFragment extends Fragment {

    CpiExcelFile cpi;
    HashMap<String, Object> hashMap;

    private static int countryTotal = 30;                                           //나라 총 개수
    public final static String[] years = {"2012", "2013", "2014", "2015", "2016"};  //년도

    private LineChartView chart;
    private LineChartData data;

    private int numberOfLines = 3;
    private int maxNumberOfLines = 3;
    private int numberOfPoints = years.length;
    int[][] Lines = new int[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;

    public CpiApFragment() {
    }

    LovelyInfoDialog info;              //설명 다이얼로그

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_cpi_ap, container, false);
        cpi = new CpiExcelFile(getContext());
        hashMap = cpi.selectByYear(getArguments().getInt("year"));

        chart = (LineChartView) view.findViewById(R.id.cpi_chart);

        generateValues("Korea (South)");
        generateData("Korea (South)");
        toggleLabels("Korea (South)");
        prepareDataAnimation();
        chart.startDataAnimation();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);

        resetViewport();

        //리스트뷰
        listItem(view);

        //설명
        info = new LovelyInfoDialog(getContext());
        view.findViewById(R.id.cpi_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setTopColorRes(R.color.colorBlue)
                        .setIcon(R.drawable.info2)
                        .setTitle("CPI for Asia Pacific")
                        .setMessage("A index released by\nTransparency International\nthat the degree of corruption\nin the public and political sectors")
                        .setConfirmButtonText("OK")
                        .show();
            }
        });

        return view;
    }

    private void generateValues(String country) {

        HashMap<String, Object> hashMap = cpi.selectByCountry(country);
        for (int i = 0, j = 2012 ; i < numberOfPoints ; ++i, j++) {
            Lines[0][i] = (int) hashMap.get("CP_RANK" + j);
            Lines[1][i] = (int) hashMap.get("CR_RANK" + j);
            Lines[2][i] = (int) hashMap.get("CP_SCORE" + j);
        }
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void generateData(String country) {

        List<AxisValue> axisValues = new ArrayList<>();
        List<Line> lines = new ArrayList<>();

        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<>();

            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, (float) Math.random() * 100));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            //line.setHasGradientToTransparent(hasGradientToTransparent);
            if (pointsHaveDifferentColor){
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        for (int i = 0; i < numberOfPoints; ++i) {
            axisValues.add(new AxisValue(i).setLabel(years[i]));
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues).setHasLines(true);
            Axis axisY = new Axis().setHasLines(true);

            if (hasAxesNames) {
                axisX.setName(country);
                axisY.setName("Axis Y");
            }
            data.setAxisXBottom(axisX);
            //data.setAxisYLeft(axisY);
            data.setAxisYLeft(null);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
    }

    private void toggleLabels(String country) {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }
        generateData(country);
    }

    private void prepareDataAnimation() {

        List<Line> tempLines = data.getLines();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> tempPoints = tempLines.get(i).getValues();
            for (int j = 0; j < numberOfPoints; ++j) {
                tempPoints.get(j).setTarget(tempPoints.get(j).getX(), Lines[i][j]);
            }
        }
    }

    public void listItem(View view){

        final ListView listView = (ListView) view.findViewById(R.id.cpi_list);

        String[] from = new String[]{"CountryItem", "RankItem1", "RankItem2", "ScoreItem"};
        int[] to = new int[]{R.id.cpi_list_country, R.id.cpi_list_rank1, R.id.cpi_list_rank2, R.id.cpi_list_score};

        final ArrayList<HashMap<String, Object>> itemList = new ArrayList<>();

        for (int i = 1 ;  i <= countryTotal ;  i++) {
            HashMap<String, Object> itemHMap = new HashMap<>();
            itemHMap.put("CountryItem", hashMap.get("CP_COUNTRY" + i));
            itemHMap.put("RankItem1", hashMap.get("CP_RANK" + i));
            itemHMap.put("RankItem2", hashMap.get("CR_RANK" + i));
            itemHMap.put("ScoreItem", hashMap.get("CP_SCORE" + i));
            itemList.add(itemHMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), itemList, R.layout.activity_cpi_list, from, to);
        listView.setAdapter(simpleAdapter);

        listView.setDivider(new ColorDrawable(Color.DKGRAY));    //구분선 지정
        listView.setDividerHeight(1);                           //구분선 높이 지정

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                generateValues((String) itemList.get(position).get("CountryItem"));
                generateData((String) itemList.get(position).get("CountryItem"));
                prepareDataAnimation();
                chart.startDataAnimation();
            }
        });
    }
}
