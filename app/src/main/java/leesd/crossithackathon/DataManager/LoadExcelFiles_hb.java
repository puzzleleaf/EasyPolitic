package leesd.crossithackathon.DataManager;

import android.app.Activity;
import android.content.Context;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

//Excel 파일 읽어오기
public class LoadExcelFiles_hb extends Activity {

    //String checkString = "";
    Workbook workbook = null;
    Sheet sheet_2016_1 = null;
    Sheet sheet_2016_2 = null;
    Sheet sheet_2016_3 = null;
    Sheet sheet_2016_4 = null;
    Sheet sheet_2017_1 = null;
    Sheet sheet_2017_2 = null;
    Sheet sheet_2017_3 = null;

    int nRowTotal_1 = 1;                  //전체 ROW 갯수
    int nColTotal_1 = 1;                  //전체 COLUMN 갯수
    int nRowTotal_2 = 0;                  //전체 ROW 갯수
    int nColTotal_2 = 0;                  //전체 COLUMN 갯수
    int nRowTotal_3 = 0;                  //전체 ROW 갯수
    int nColTotal_3 = 0;                  //전체 COLUMN 갯수
    int nRowTotal_4 = 0;                  //전체 ROW 갯수
    int nColTotal_4 = 0;                  //전체 COLUMN 갯수
    int nRowTotal_5 = 0;                  //전체 ROW 갯수
    int nColTotal_5 = 0;                  //전체 COLUMN 갯수
    int nRowTotal_6 = 0;                  //전체 ROW 갯수
    int nColTotal_6 = 0;                  //전체 COLUMN 갯수
    int nRowTotal_7 = 0;                  //전체 ROW 갯수
    int nColTotal_7 = 0;                  //전체 COLUMN 갯수

    public LoadExcelFiles_hb(Context context, String fileName) {

        try {
            //파일 가져오기
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            workbook = Workbook.getWorkbook(inputStream);

            sheet_2016_1 = workbook.getSheet(0);
            sheet_2016_2 = workbook.getSheet(1);
            sheet_2016_3 = workbook.getSheet(2);
            sheet_2016_4 = workbook.getSheet(3);
            sheet_2017_1 = workbook.getSheet(4);
            sheet_2017_2 = workbook.getSheet(5);
            sheet_2017_3 = workbook.getSheet(6);

            if(sheet_2016_1 != null){

                nRowTotal_1 = sheet_2016_1.getRows();
                nColTotal_1 = sheet_2016_1.getColumns();
            }
            if(sheet_2016_2 != null){

                nRowTotal_2 = sheet_2016_2.getRows();
                nColTotal_2 = sheet_2016_2.getColumns();
            }
            if(sheet_2016_3 != null){

                nRowTotal_3 = sheet_2016_3.getRows();
                nColTotal_3 = sheet_2016_3.getColumns();
            }
            if(sheet_2016_4 != null){

                nRowTotal_4 = sheet_2016_4.getRows();
                nColTotal_4 = sheet_2016_4.getColumns();
            }
            if(sheet_2017_1 != null){

                nRowTotal_5 = sheet_2017_1.getRows();
                nColTotal_5 = sheet_2017_1.getColumns();
            }
            if(sheet_2017_2 != null){

                nRowTotal_6 = sheet_2017_2.getRows();
                nColTotal_6 = sheet_2017_2.getColumns();
            }
            if(sheet_2017_3 != null){

                nRowTotal_7 = sheet_2017_3.getRows();
                nColTotal_7 = sheet_2017_3.getColumns();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(workbook != null) {
                //workbook.close();
            }
        }
    }

    //셀 정보 가져오기
    public String getCell(int colIndex, int rowIndex, int sheet_info){
        if(sheet_info==1)
            return sheet_2016_1.getCell(colIndex, rowIndex).getContents();
        else if(sheet_info==2)
            return sheet_2016_2.getCell(colIndex, rowIndex).getContents();
        else if(sheet_info==3)
            return sheet_2016_3.getCell(colIndex, rowIndex).getContents();
        else if(sheet_info==4)
            return sheet_2016_4.getCell(colIndex, rowIndex).getContents();
        else if(sheet_info==5)
            return sheet_2017_1.getCell(colIndex, rowIndex).getContents();
        else if(sheet_info==6)
            return sheet_2017_2.getCell(colIndex, rowIndex).getContents();
        else
            return sheet_2017_3.getCell(colIndex, rowIndex).getContents();
    }
}
