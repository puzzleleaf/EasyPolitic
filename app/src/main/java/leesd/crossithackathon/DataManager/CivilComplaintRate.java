package leesd.crossithackathon.DataManager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Created by hbLee on 2017-08-06.
 */

public class CivilComplaintRate extends Activity {

    LoadExcelFiles_hb load = null;



    CivilComplaintRateVO output_data;

    public CivilComplaintRate(Context context){

        load = new LoadExcelFiles_hb(context,"CivilComplaintRate.xls");


    }

    public CivilComplaintRateVO extractCellData(String year, String name){//연도,기관명
        int totalRow=0,totalCol=0;
        int sheet_info=0;


        switch (year){

            case "2016_1":
                totalCol= load.nColTotal_1;
                totalRow= load.nRowTotal_1;
                sheet_info=1;
                break;
            case "2016_2":
                totalCol= load.nColTotal_2;
                totalRow= load.nRowTotal_2;
                sheet_info=2;
                break;
            case "2016_3":
                totalCol= load.nColTotal_3;
                totalRow= load.nRowTotal_3;
                sheet_info=3;
                break;
            case "2016_4":
                totalCol= load.nColTotal_4;
                totalRow= load.nRowTotal_4;
                sheet_info=4;
                break;
            case "2017_1":
                totalCol= load.nColTotal_5;
                totalRow= load.nRowTotal_5;
                sheet_info=5;
                break;
            case "2017_2":
                totalCol= load.nColTotal_6;
                totalRow= load.nRowTotal_6;
                sheet_info=6;
                break;
            default:
                break;
        }
        return extractCellDataByName(name,totalRow,totalCol,sheet_info);


    }

    public CivilComplaintRateVO extractCellDataByName(String name, int totalRow, int totalCol, int sheet_info){


        for(int i=0;i<totalRow;i++){
            Log.d("mytag",totalRow+"");
            Log.d("mytag",""+load.getCell(0,i,sheet_info).equals(name));
            if(load.getCell(0,i,sheet_info).equals(name))
            {


                try {
                    output_data = new CivilComplaintRateVO(load.getCell(0, i, sheet_info), Integer.parseInt(load.getCell(1, i, sheet_info).replaceAll("[^\\d]", "")), Integer.parseInt(load.getCell(2, i, sheet_info).replaceAll("[^\\d]", "")), Integer.parseInt(load.getCell(3, i, sheet_info).replaceAll("[^\\d]", "")), Integer.parseInt(load.getCell(4, i, sheet_info).replaceAll("[^\\d]", "")), Double.parseDouble(load.getCell(5, i, sheet_info).substring(0,5)));

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else
                continue;
        }

        return output_data;

    }




}
