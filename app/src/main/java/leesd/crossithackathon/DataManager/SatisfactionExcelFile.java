package leesd.crossithackathon.DataManager;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;


/* < SATISFACTION 민원만족도 >
     SF_ID        (식별번호)
     SF_AGENCY    (기관)
     SF_TYPE      (기관유형: 중앙행정기관, 교육청, 광역지자체)
     SF_2016      (2016년도 평가등급)
     SF_2015      (2015년도 평가등급)
     SF_2014      (2014년도 평가등급) 으로 구성 */

public class SatisfactionExcelFile extends Activity {

    LoadExcelFiles load = null;

    int idColumnIndex = 0;              //식별번호 칼럼번호
    int agencyColumnIndex = 1;          //기관 칼럼번호
    int typeColumnIndex = 2;            //유형 칼럼번호
    int year1ColumnIndex = 3;           //년도1 칼럼번호
    int year2ColumnIndex = 4;           //년도2 칼럼번호
    int year3ColumnIndex = 5;           //년도3 칼럼번호

    int nRowTotal = 0;                  //전체 ROW 갯수
    int nColTotal = 0;                  //전체 COLUMN 갯수

    public SatisfactionExcelFile(Context context){

        load = new LoadExcelFiles(context, "satisfaction.xls");
        nRowTotal = load.nRowTotal;
        nColTotal = load.nColTotal;
    }

    //SF_AGENCY(기관명)으로 해당 기관 정보 가져오기
    public HashMap<String, String> selectByName(String name){

        HashMap<String, String> hashMap = new HashMap<>();

        for(int i=0 ; i<load.nRowTotal ; i++){
            if(name.equals(load.getCell(agencyColumnIndex, i))){
                hashMap.put("SF_ID", load.getCell(idColumnIndex, i));
                hashMap.put("SF_AGENCY", load.getCell(agencyColumnIndex, i));
                hashMap.put("SF_TYPE", load.getCell(typeColumnIndex, i));
                hashMap.put("SF_2016", load.getCell(year1ColumnIndex, i));
                hashMap.put("SF_2015", load.getCell(year2ColumnIndex, i));
                hashMap.put("SF_2014", load.getCell(year3ColumnIndex, i));
                break;
            }
        }
        return hashMap;
    }

    //SF_ID로 해당 기관 정보 가져오기
    public HashMap<String, String> selectById(int id){

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("SF_ID", load.getCell(idColumnIndex, id-1));
        hashMap.put("SF_AGENCY", load.getCell(agencyColumnIndex, id-1));
        hashMap.put("SF_TYPE", load.getCell(typeColumnIndex, id-1));
        hashMap.put("SF_2016", load.getCell(year1ColumnIndex, id-1));
        hashMap.put("SF_2015", load.getCell(year2ColumnIndex, id-1));
        hashMap.put("SF_2014", load.getCell(year3ColumnIndex, id-1));

        return hashMap;
    }

    //해당 기관의 유형이 속한 기관의 개수
    public int countByAgencyType(HashMap target){

        int count = 0;

        for(int rowIndex = 0 ; rowIndex < nRowTotal ; rowIndex++){
            if(target.get("SF_TYPE").equals(load.getCell(typeColumnIndex, rowIndex))){
                count++;
            }
        }

        return count;
    }

    //특정 년도에 해당 기관과 같은 유형, 같은 등급을 지닌 기관 가져오기
    public HashMap<Integer, String> selectSameGrade (HashMap target, int targetYear){

        HashMap<Integer, String> hashMap = new HashMap<>();

        int targetId = Integer.parseInt((String) target.get("SF_ID"));      //해당 기관 ID
        String yearColumnName = "SF_" + targetYear;                         //년도 칼럼 이름
        int yearColumnIndex = -1;                                           //년도 칼럼 번호
        int count = 0;                                                      //같은 등급을 지닌 기관 수

        //비교할 년도 칼럼 번호 구하기
        switch (targetYear){
            case 2016:
                yearColumnIndex = year1ColumnIndex;
                break;
            case 2015:
                yearColumnIndex = year2ColumnIndex;
                break;
            case 2014:
                yearColumnIndex = year3ColumnIndex;
                break;
            default:
                break;
        }

        for(int rowIndex = 0 ; rowIndex < nRowTotal ; rowIndex++){
            //해당기관은 비교제외
            if(rowIndex != targetId-1){
                //해당 기관의 등급이 "NULL"이 아니고, 기관유형이 같으며, 해당 년도에 같은 평가 등급을 받았을 경우
                if((!(target.get(yearColumnName).equals("NULL"))
                        && target.get("SF_TYPE").equals(load.getCell(typeColumnIndex, rowIndex)))
                        && (target.get(yearColumnName).equals(load.getCell(yearColumnIndex, rowIndex)))){
                        count++;
                        hashMap.put(count, load.getCell(agencyColumnIndex, rowIndex));
                }
            }
        }
        return hashMap;
    }

    //등급에 대한 별 갯수 환산
    public int number(String value){

        int num = 0;

        switch (value){
            case "매우우수":
                num = 5;
                break;
            case "우수":
                num = 4;
                break;
            case "보통":
                num = 3;
                break;
            case "미흡":
                num = 2;
                break;
            case "매우미흡":
                num = 1;
                break;
            case "NULL":
                num = 0;
                break;
            default:
                break;
        }
        return num;
    }

    //해당 기관의 민원만족도 결과 출력 (나중에 그래프 등으로 교체)
    public String setText(HashMap target){

        String text = "";
        HashMap <Integer, String> hashMap = selectSameGrade(target, 2016);                      //최신년도 같은 등급 기관
        int sameGradeTotal = hashMap.size() + 1;                                                //같은 등급 기관 수
        float percent = (float) (Math.round((float)(sameGradeTotal) / (float)countByAgencyType(target) * 1000)/10.0);    //백분위

        text += star((String) target.get("SF_2016"));
        text += "\n'" + target.get("SF_2016") +"' 등급\n\n";
        text += target.get("SF_AGENCY") + " 포함\n";
        text += "'" + target.get("SF_2016") +"' 기관 ("+ sameGradeTotal +") (" + percent + "%)\n";

        for(int i = 1 ; i <= hashMap.size() ; i++){

            if(i != hashMap.size())
                text += hashMap.get(i) + ",\n";
            else
                text += hashMap.get(i);
        }

        text += "\n\n년도별 평가\n2016년 " + star((String) target.get("SF_2016"));
        text += "\n2015년 " + star((String) target.get("SF_2015"));
        text += "\n2014년 " + star((String) target.get("SF_2014"));
        //text += "\n\n총 " + countByAgencyType(target) + "개의 " + target.get("SF_TYPE") + "\n";

        return text;
    }

    //만족도 평가등급 출럭 (나중에 그래프로 대체)
    public String star(String value){

        String star = "";

        switch (value){
            case "매우우수":
                star = "★★★★★";
                break;
            case "우수":
                star = "★★★★☆";
                break;
            case "보통":
                star = "★★★☆☆";
                break;
            case "미흡":
                star = "★★☆☆☆";
                break;
            case "매우미흡":
                star = "★☆☆☆☆";
                break;
            case "NULL":
                star = "-";
                break;
            default:
                break;
        }
        return star;
    }
}
