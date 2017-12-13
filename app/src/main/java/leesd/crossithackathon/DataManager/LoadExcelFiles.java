package leesd.crossithackathon.DataManager;

import android.app.Activity;
import android.content.Context;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

//Excel 파일 읽어오기
public class LoadExcelFiles extends Activity {

    //String checkString = "";
    Workbook workbook = null;
    Sheet sheet = null;

    int nRowTotal = 0;                  //전체 ROW 갯수
    int nColTotal = 0;                  //전체 COLUMN 갯수

    public LoadExcelFiles(Context context, String fileName) {

        try {
            //파일 가져오기
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            workbook = Workbook.getWorkbook(inputStream);

            //첫번째 시트 가져오기
            sheet = workbook.getSheet(0);

            if(sheet != null){

                nRowTotal = sheet.getRows();
                nColTotal = sheet.getColumns();
            }
            else{
                //checkString = checkString + "Fail to load the sheet";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(workbook != null) {
                //workbook.close();
            }
        }
    }

    public LoadExcelFiles(Context context, String fileName, int sheetNum) {

        try {
            //파일 가져오기
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            workbook = Workbook.getWorkbook(inputStream);

            //특정 시트 가져오기
            sheet = workbook.getSheet(sheetNum);

            if(sheet != null){

                nRowTotal = sheet.getRows();
                nColTotal = sheet.getColumns();
            }
            else{
                //checkString = checkString + "Fail to load the sheet";
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
    public String getCell(int colIndex, int rowIndex){
        return sheet.getCell(colIndex, rowIndex).getContents();
    }
}
