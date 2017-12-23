package leesd.crossithackathon.DataManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import java.util.HashMap;


/* < DETAILS 기관세부설명 >
     DT_AGENCY      (기관)
     DT_TYPE        (유형)
     DT_ADDR        (주소)
     DT_PHONE       (전화번호)
     DT_INFO        (설명)
     DT_URL         (홈페이지) 으로 구성 */

@SuppressLint("Registered")
public class DetailsExcelFile extends Activity {

    LoadExcelFiles load = null;

    int agencyColumnIndex = 0;      //기관 칼럼번호
    int typeColumnIndex = 1;        //유형 칼럼번호
    int addrColumnIndex = 2;        //주소 칼럼번호
    int phoneColumnIndex = 3;       //전화번호 칼럼번호
    int infoColumnIndex = 4;        //설명 칼럼번호
    int urlColumnIndex = 5;         //홈페이지 칼럼번호

    int nRowTotal = 0;              //전체 ROW 갯수
    int nColTotal = 0;              //전체 COLUMN 갯수

    public DetailsExcelFile(Context context){

        load = new LoadExcelFiles(context, "detailOfAgency.xls");
        nRowTotal = load.nRowTotal;
        nColTotal = load.nColTotal;
    }

    //DT_AGENCY(기관명)으로 해당 기관 정보 가져오기
    public HashMap<String, String> selectByAgency(String name){

        HashMap<String, String> hashMap = new HashMap<>();

        for(int i=0 ; i<load.nRowTotal ; i++){
            if(name.equals(load.getCell(agencyColumnIndex, i))){
                hashMap.put("DT_AGENCY", load.getCell(agencyColumnIndex, i));
                hashMap.put("DT_TYPE", load.getCell(typeColumnIndex, i));
                hashMap.put("DT_ADDR", load.getCell(addrColumnIndex, i));
                hashMap.put("DT_PHONE", load.getCell(phoneColumnIndex, i));
                hashMap.put("DT_INFO", load.getCell(infoColumnIndex, i));
                hashMap.put("DT_URL", load.getCell(urlColumnIndex, i));
                break;
            }
        }
        return hashMap;
    }
}
