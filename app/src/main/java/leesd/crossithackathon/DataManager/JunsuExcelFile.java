package leesd.crossithackathon.DataManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import java.util.HashMap;


/* < JUNSU 민원준수율 >
     JS_AGENCY          (기관)
     JS_TOTAL           (총접수건수)
     JS_IN_SUCCESS      (기간내처리건수)
     JS_OUT_SUCCESS     (기간초과처리건수)
     JS_OUT_FAIL        (기간초과미처리건수)
     JS_RATIO           (처리기간준수율)     으로 구성 */

@SuppressLint("Registered")
public class JunsuExcelFile extends Activity {

    LoadExcelFiles load = null;

    int agencyColumnIndex = 0;      //기관 칼럼번호
    int totalColumnIndex = 1;       //총접수건수 칼럼번호
    int inSuccessColumnIndex = 2;   //기간내처리건수 칼럼번호
    int outSuccessColumnIndex = 3;  //기간초과처리건수 칼럼번호
    int outFailColumnIndex = 4;     //기간초과미처리건수 칼럼번호
    int ratioColumnIndex = 5;       //처리기간준수율 칼럼번호

    int nRowTotal = 0;                  //전체 ROW 갯수
    int nColTotal = 0;                  //전체 COLUMN 갯수

    public JunsuExcelFile(Context context, String yearSemester){

        int sheetNum;

        switch (yearSemester){
            case "2016_1":
                sheetNum = 0;
                break;
            case "2016_2":
                sheetNum = 1;
                break;
            case "2016_3":
                sheetNum = 2;
                break;
            case "2016_4":
                sheetNum = 3;
                break;
            case "2017_1":
                sheetNum = 4;
                break;
            case "2017_2":
                sheetNum = 5;
                break;
            case "2017_3":
                sheetNum = 6;
                break;
            default:
                sheetNum = 0;
                break;
        }

        load = new LoadExcelFiles(context, "CivilComplaintRate.xls", sheetNum);
        nRowTotal = load.nRowTotal;
        nColTotal = load.nColTotal;
    }

    //JS_AGENCY(기관명)으로 해당 기관 정보 가져오기
    public HashMap<String, String> selectByAgency(String name){

        HashMap<String, String> hashMap = new HashMap<>();

        for(int i=0 ; i<load.nRowTotal ; i++){
            if(name.equals(load.getCell(agencyColumnIndex, i))){
                hashMap.put("JS_AGENCY", load.getCell(agencyColumnIndex, i));
                hashMap.put("JS_TOTAL", load.getCell(totalColumnIndex, i));
                hashMap.put("JS_IN_SUCCESS", load.getCell(inSuccessColumnIndex, i));
                hashMap.put("JS_OUT_SUCCESS", load.getCell(outSuccessColumnIndex, i));
                hashMap.put("JS_OUT_FAIL", load.getCell(outFailColumnIndex, i));
                hashMap.put("JS_RATIO", load.getCell(ratioColumnIndex, i));
                break;
            }
        }
        return hashMap;
    }
}
