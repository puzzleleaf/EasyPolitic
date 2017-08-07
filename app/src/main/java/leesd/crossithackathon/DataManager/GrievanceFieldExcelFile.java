package leesd.crossithackathon.DataManager;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.Set;


/* < GrievanceField     분야별고충민원 >
     GF_ID              (식별번호)
     GF_YEAR            (년    도)
     GF_FIELD1          (건    축)    GF_FIELD2           (경    찰)
     GF_FIELD3          (교    육)    GF_FIELD4           (교    통)
     GF_FIELD5          (국    방)    GF_FIELD6           (군    사)
     GF_FIELD7          (노    동)    GF_FIELD8           (농    림)
     GF_FIELD9          (도    로)    GF_FIELD10          (도    시)
     GF_FIELD11         (문화관광)    GF_FIELD12          (민사법무)
     GF_FIELD13         (방송통신)    GF_FIELD14          (보건복지)
     GF_FIELD15         (보    훈)    GF_FIELD16          (산업자원)
     GF_FIELD17         (세    무)    GF_FIELD18          (수 자 원)
     GF_FIELD19         (외무통일)    GF_FIELD20          (인사행정)
     GF_FIELD21         (재    정)    GF_FIELD22          (주    택)
     GF_FIELD23         (해양수산)    GF_FIELD24          (행정안전)
     GF_FIELD25         (환    경)    GF_FIELD26          (기    타)
     GF_TOTAL           (총    합) 으로 구성 */

public class GrievanceFieldExcelFile extends Activity {

    LoadExcelFiles load = null;
    int fieldTotal = 26;                                    //분야 개수
    String[] strArr = new String[fieldTotal-1];             //분야 (기타제외)
    int[] intArr = new int[fieldTotal-1];                   //분야별 건수 (기타제외)

    int nRowTotal = 0;                  //전체 ROW 갯수
    int nColTotal = 0;                  //전체 COLUMN 갯수

    public GrievanceFieldExcelFile(Context context){

        load = new LoadExcelFiles(context, "grievanceField.xls");
        nRowTotal = load.nRowTotal;
        nColTotal = load.nColTotal;
    }

    //SF_YEAR(년도)으로 해당 고충민원 정보 가져오기
    public HashMap<String, String> selectByYear(int year){

        HashMap<String, String> hashMap = new HashMap<>();

        for(int i=1 ; i<load.nRowTotal ; i++){
            if(year == Integer.parseInt(load.getCell(1, i))){
                hashMap.put("GF_ID", load.getCell(0, i));
                hashMap.put("GF_YEAR", load.getCell(1, i));
                hashMap.put("GF_TOTAL", load.getCell(28, i));

                for(int j=2, n=1 ; j<load.nColTotal-1 ; j++, n++){
                    hashMap.put("GF_FIELD" + n, load.getCell(j, i));
                }
                break;
            }
        }
        return hashMap;
    }

    //고충민원 10대 분야, 건수별 순위, 비율(%)
    public HashMap<String, Object> rank(int year){

        HashMap<String, String> hashMap = selectByYear(year);   //해당 년도의 고충민원 정보
        HashMap<String, Object> resultHMap = new HashMap<>();   //10대분야 정보 (분야,분야별건수)

        strArr = new String[fieldTotal-1];                      //분야 (기타제외)
        intArr = new int[fieldTotal-1];                         //분야별 건수 (기타제외)

        for(int i=0, n=1 ; i<fieldTotal-1 ; i++, n++){          //값 넣기
            intArr[i] = Integer.parseInt(hashMap.get("GF_FIELD"+ n));
            strArr[i] = load.getCell(i+2, 0);
        }
        swapDesc(intArr, strArr);                               //내림차순 정렬

        //10대 분야
        for(int i=1 ; i<=10 ; i++){                             //정렬한 값 넣기
            resultHMap.put("TOP10_FIELD" + i, strArr[i-1]);
            resultHMap.put("TOP10_NUMBER" + i, intArr[i-1]);
        }

        //건수별 순위, 비율
        for(int i=1 ; i<=intArr.length ; i++){                   //정렬한 값 넣기
            resultHMap.put("RANK_SEQ" + i, i);
            resultHMap.put("RANK_FIELD" + i, strArr[i-1]);
            resultHMap.put("RANK_NUMBER" + i, intArr[i-1]);
            resultHMap.put("RANK_PERCENT" + i, (float) (Math.round((float)(intArr[i-1]) / Float.parseFloat(hashMap.get("GF_TOTAL")) * 1000)/10.0));
        }

        return resultHMap;
    }

    //내림차순 정렬
    public void swapDesc(int[] intArr, String[] strArr){

        int temp;
        String tmp;

        for(int i=0 ; i<intArr.length ; i++){
            for(int j=i+1 ; j<intArr.length ; j++){
                if(intArr[i] < intArr[j]){
                    temp = intArr[i];
                    intArr[i] = intArr[j];
                    intArr[j] = temp;

                    tmp = strArr[i];
                    strArr[i] = strArr[j];
                    strArr[j] = tmp;
                }
            }
        }
    }

    //정보 출력(임시)
    public String setText(int year){

        String text = "";
        HashMap <String, String> target = selectByYear(year);
        HashMap <String, Object> target2 = rank(year);

        if(target.get("GF_YEAR") != null){
        //    text += "년도: " + target.get("GF_YEAR");
        //  text += "\n총합: " + target.get("GF_TOTAL");
        //  text += " 건축: " + target.get("GF_FIELD1");//
        //  text += "\n경찰: " + target.get("GF_FIELD2");
        //  text += " 환경: " + target.get("GF_FIELD25");
        //  text += "\n기타: " + target.get("GF_FIELD26");
            text += "\n" + target2.get("TOP10_FIELD1") + ": "  + target2.get("TOP10_NUMBER1");
            text += "\n" + target2.get("RANK_SEQ1") + ": " + target2.get("RANK_FIELD1");
            text += ": " + target2.get("RANK_NUMBER1") + ": " + target2.get("RANK_PERCENT1") + "%";
        }
        else{
            text = "해당 정보 없음";
        }

        return text;
    }
}
