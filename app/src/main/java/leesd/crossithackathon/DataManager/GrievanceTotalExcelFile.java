package leesd.crossithackathon.DataManager;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;


/* < GrievanceTotal     고충민원(전체) >
     GT_ID              (식별번호)
     GT_YEAR            (년도)
     GT_RECEIVE         (접수)
     GT_PROCESS         (처리 = 고충민원 + 비고충민원)
     GT_GRIEVANCE       (고충민원)
     GT_NOT_GRIEVANCE   (비고충민원)
     GT_QUOTE           (인용)
     GT_QUOTE_PERCENT   (인용률)
     GT_DATE            (평균처리일)
     GT_SATISFACTION    (만족도) 으로 구성 */

public class GrievanceTotalExcelFile extends Activity {

    LoadExcelFiles load = null;

    int idColumnIndex = 0;              //식별번호 칼럼번호
    int yearColumnIndex = 1;            //년도 칼럼번호
    int receiveColumnIndex = 2;         //접수 칼럼번호
    int processColumnIndex = 3;         //처리 칼럼번호
    int grievanceColumnIndex = 4;       //고충민원 칼럼번호
    int notGrievanceColumnIndex = 5;    //비고충민원 칼럼번호
    int quoteColumnIndex = 6;           //인용 칼럼번호
    int percentColumnIndex = 7;         //인용률 칼럼번호
    int dateColumnIndex = 8;            //평균처리일 칼럼번호
    int satisfyColumnIndex = 9;         //만족도 칼럼번호

    int nRowTotal = 0;                  //전체 ROW 갯수
    int nColTotal = 0;                  //전체 COLUMN 갯수

    public GrievanceTotalExcelFile(Context context){

        load = new LoadExcelFiles(context, "grievanceTotal.xls");
        nRowTotal = load.nRowTotal;
        nColTotal = load.nColTotal;
    }

    //SF_YEAR(년도)으로 해당 고충민원 정보 가져오기
    public HashMap<String, String> selectByYear(int year){

        HashMap<String, String> hashMap = new HashMap<>();

        for(int i=1 ; i<load.nRowTotal ; i++){
            if(year == Integer.parseInt(load.getCell(yearColumnIndex, i))){
                hashMap.put("GT_ID", load.getCell(idColumnIndex, i));
                hashMap.put("GT_YEAR", load.getCell(yearColumnIndex, i));
                hashMap.put("GT_RECEIVE", load.getCell(receiveColumnIndex, i));
                hashMap.put("GT_PROCESS", load.getCell(processColumnIndex, i));
                hashMap.put("GT_GRIEVANCE", load.getCell(grievanceColumnIndex, i));
                hashMap.put("GT_NOT_GRIEVANCE", load.getCell(notGrievanceColumnIndex, i));
                hashMap.put("GT_QUOTE", load.getCell(quoteColumnIndex, i));
                hashMap.put("GT_QUOTE_PERCENT", load.getCell(percentColumnIndex, i));
                hashMap.put("GT_DATE", load.getCell(dateColumnIndex, i));
                hashMap.put("GT_SATISFACTION", load.getCell(satisfyColumnIndex, i));
                break;
            }
        }
        return hashMap;
    }

    //정보 출력(임시)
    public String setText(int year){

        String text = "";
        HashMap <String, String> target = selectByYear(year);

        if(target.get("GT_YEAR") != null){
            text += "년도: " + target.get("GT_YEAR");
            text += "\n접수: " + target.get("GT_RECEIVE");
            text += " 처리: " + target.get("GT_PROCESS");
            text += "\n고충민원: " + target.get("GT_GRIEVANCE");
            text += " 비고충민원: " + target.get("GT_NOT_GRIEVANCE");
            text += "\n인용: " + target.get("GT_QUOTE");
            text += " 인용률: " + target.get("GT_QUOTE_PERCENT");
            text += "\n평균처리일: " + target.get("GT_DATE");
            text += " 만족도: " + target.get("GT_SATISFACTION");
        }
        else{
            text = "해당 정보 없음";
        }

        return text;
    }
}
