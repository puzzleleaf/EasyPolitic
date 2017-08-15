package leesd.crossithackathon.DataManager;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;


/* < Cpi                부패인식지수  >
     CP_COUNTRY1..n     ( 나라이름 )
     CP_RANK1..n        ( 나라등수 )
     CR_RANK1..n        ( 전체등급 )
     CP_SCORE1..n       ( 나라점수 ) 으로 구성 */

public class CpiExcelFile extends Activity {

    LoadExcelFiles load = null;
    LoadExcelFiles load2 = null;

    int nRowTotal = 0;                                      //전체 ROW 갯수
    int nColTotal = 0;                                      //전체 COLUMN 갯수

    public CpiExcelFile(Context context){

        load = new LoadExcelFiles(context, "cpi.xls");
        load2 = new LoadExcelFiles(context, "cpiRank.xls");
        nRowTotal = load.nRowTotal;
        nColTotal = load.nColTotal;
    }

    //년도로 해당 나라 정보 가져오기
    public HashMap<String, Object> selectByYear(int year){

        HashMap<String, Object> hashMap = new HashMap<>();
        String[] strArr = new String[nRowTotal-1];              //나라 이름
        int[] intArr = new int[nRowTotal-1];                    //나라 점수
        int[] intArr2 = new int[nRowTotal-1];                   //나라 전체등급

        //값 가져오기
        for(int i = 1 ; i < nColTotal ; i++){
            if(year == Integer.parseInt(load.getCell(i, 0))){
                for(int j = 1 ; j < nRowTotal ; j++){
                    strArr[j-1] = load.getCell(0, j);
                    intArr[j-1] = Integer.parseInt(load.getCell(i, j));
                    intArr2[j-1] = Integer.parseInt(load2.getCell(i, j));
                }
                break;
            }
        }

        //내림차순 정렬
        swapDesc(intArr, strArr, intArr2);

        //정렬한 값 넣기
        for(int i = 1 ; i < nRowTotal ; i++){
            hashMap.put("CP_RANK" + i, i);
            hashMap.put("CP_COUNTRY" + i, strArr[i-1]);
            hashMap.put("CP_SCORE" + i, intArr[i-1]);
            hashMap.put("CR_RANK" + i, intArr2[i-1]);
        }

        return hashMap;
    }

    //나라 정보 가져오기(2012-2016)
    public HashMap<String, Object> selectByCountry(String country){

        HashMap<String, Object> hashMap = new HashMap<>();

        for(int i = 2012 ; i <= 2016 ; i++){
            HashMap<String, Object> yearHMap = selectByYear(i);

            for(int j = 1 ; j < nRowTotal ; j++){
                if(country.equals(yearHMap.get("CP_COUNTRY"+ j))){
                    hashMap.put("CP_RANK" + i, yearHMap.get("CP_RANK" + j));
                    hashMap.put("CP_SCORE" + i, yearHMap.get("CP_SCORE" + j));
                    hashMap.put("CR_RANK" + i, yearHMap.get("CR_RANK" + j));
                    break;
                }
            }
        }

        return hashMap;
    }

    //내림차순 정렬
    public void swapDesc(int[] intArr, String[] strArr, int[] intArr2){

        int temp1, temp2;
        String tmp;

        for(int i=0 ; i<intArr.length ; i++){
            for(int j=i+1 ; j<intArr.length ; j++){
                if(intArr[i] < intArr[j]){
                    temp1 = intArr[i];
                    intArr[i] = intArr[j];
                    intArr[j] = temp1;

                    tmp = strArr[i];
                    strArr[i] = strArr[j];
                    strArr[j] = tmp;

                    temp2 = intArr2[i];
                    intArr2[i] = intArr2[j];
                    intArr2[j] = temp2;
                }
            }
        }
    }
}
