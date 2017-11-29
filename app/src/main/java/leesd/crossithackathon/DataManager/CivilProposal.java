package leesd.crossithackathon.DataManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by hbLee on 2017-11-29.
 */

public class CivilProposal {

    private final String showListURL;
    private final String showDetailURL;

    public CivilProposal(){

        showListURL = "https://www.epeople.go.kr/jsp/user/pp/UPpProposOpenList.paid";
        showDetailURL = "https://www.epeople.go.kr/jsp/user/pp/UPpProposNiceRead.paid";

    }

    public HashMap<String,ArrayList<String>> getList(String year, int pageNo){ //리스트 목록 따오는 메소드

        HashMap<String,ArrayList<String>> keyword1 = new HashMap<String,ArrayList<String>>();//reg
        HashMap<String,ArrayList<String>> keyword2 = new HashMap<String,ArrayList<String>>();//search
        HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();

        SimpleDateFormat sdf = new SimpleDateFormat();SimpleDateFormat sdf2 = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");sdf2.applyPattern("yyyyMMdd");
        Calendar cal = Calendar.getInstance();

        ArrayList<String> tmp_reg = new ArrayList<String>();//detail 구분 데이터
        ArrayList<String> tmp_hidden = new ArrayList<String>();//detail 구분 데이터
        ArrayList<String> tmp_pageNo = new ArrayList<String>();//detail 구분 데이터
        ArrayList<String> tmp_title = new ArrayList<String>();//글 제목
        ArrayList<String> tmp_writingNo = new ArrayList<String>();//글 넘버
        ArrayList<String> tmp_institution = new ArrayList<String>();//글 기관
        ArrayList<String> tmp_date = new ArrayList<String>();//글 날짜
        ArrayList<String> tmp_hits = new ArrayList<String>();//글 조회수


        ArrayList<String> reg_2017 = new ArrayList<String>();
        ArrayList<String> reg_2016 = new ArrayList<String>();
        ArrayList<String> reg_2015 = new ArrayList<String>();
        ArrayList<String> ser_2017 = new ArrayList<String>();
        ArrayList<String> ser_2016 = new ArrayList<String>();
        ArrayList<String> ser_2015 = new ArrayList<String>();

        reg_2017.add("2017-01-01");reg_2017.add(sdf.format(cal.getTime()));
        keyword1.put("2017",reg_2017);
        reg_2016.add("2016-01-01");reg_2016.add("2016-12-31");
        keyword1.put("2016",reg_2016);
        reg_2015.add("2015-01-01");reg_2015.add("2015-12-31");
        keyword1.put("2015",reg_2015);

        ser_2017.add("20170101");ser_2017.add(sdf2.format(cal.getTime()));
        keyword2.put("2017",ser_2017);
        ser_2016.add("20160101");ser_2016.add("20161231");
        keyword2.put("2016",ser_2016);
        ser_2015.add("20150101");ser_2015.add("20151231");
        keyword2.put("2015",ser_2015);

        if(pageCounting(year) < pageNo || pageNo < 1){

            System.out.println("정확한 페이지 수 요구.");
            return null;

        }


        try {

            Document doc = Jsoup.connect(showListURL)
                    .data("flag","N")
                    .data("pageNo",pageNo+"")
                    .data("s_date",keyword2.get(year).get(0))
                    .data("e_date",keyword2.get(year).get(1))
                    .data("reg_d_s",keyword1.get(year).get(0))
                    .data("reg_d_e",keyword1.get(year).get(1))
                    .data("keyfield","petiTitle")
                    .timeout(5000)
                    .post();


            Elements td = doc.select(".listForm1.mt10 td");

            for(int i = 0; i < td.size(); i++){

                if(i % 5==0)
                    tmp_writingNo.add(td.get(i).text());
                else if((i-1) % 5==0)
                    tmp_title.add(td.get(i).text());
                else if((i-2) % 5==0)
                    tmp_institution.add(td.get(i).text());
                else if((i-3) % 5==0)
                    tmp_date.add(td.get(i).text());
                else if((i-4) % 5==0)
                    tmp_hits.add(td.get(i).text());
                else
                    continue;
            }

            result.put("title",tmp_title);
            result.put("writingNo", tmp_writingNo);
            result.put("institution", tmp_institution);
            result.put("request_date", tmp_date);
            result.put("hits", tmp_hits);

            tmp_reg.add(keyword2.get(year).get(0));tmp_reg.add(keyword2.get(year).get(1));
            result.put("reg_date",tmp_reg); // reg date 구하기 0 -> start 1 -> end

            tmp_pageNo.add(pageNo+"");
            result.put("pageNo",tmp_pageNo);//pageNo 구하기

            Elements hidden = doc.select(".taL input[type=hidden]");

            for(Element e : hidden)
                tmp_hidden.add(e.attr("value"));

            ArrayList<String> petiNo = new ArrayList<String>();
            ArrayList<String> ancCode = new ArrayList<String>();

            for(int i=0; i<tmp_hidden.size(); i++)
            {
                if(i % 2 == 0)
                    petiNo.add(tmp_hidden.get(i));
                else
                    ancCode.add(tmp_hidden.get(i));
            }

            result.put("petiNo", petiNo);
            result.put("ancCode", ancCode);

            ///result에는 title,petiNo,ancCode,reg_Date,pageNo 이 들어 가 있음.

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;


    }


    public String getDetail(String petiNo,String ancCode,String pageNo,String reg_d_s,String reg_d_e){ //게시글 상세보기 메소드 (반환형 html 소스형태)

        String result="";

        try {

            InetAddress local = InetAddress.getLocalHost();
            String userIp = local.getHostAddress(); // userIp

            Document doc = Jsoup.connect(showDetailURL)
                    .data("petiNo",petiNo)
                    .data("ancCode",ancCode)
                    .data("userIp",userIp)
                    .data("pageNo",pageNo)
                    .data("reg_d_s",reg_d_s)
                    .data("reg_d_e",reg_d_e)
                    .timeout(10000)
                    .post();

            Elements t = doc.select(".contestView");
            result = t.html();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }


    public int pageCounting(String year){ //페이지 수 가져오는 메소드

        int numOfPaging=0;

        HashMap<String,ArrayList<String>> keyword1 = new HashMap<String,ArrayList<String>>();//reg
        HashMap<String,ArrayList<String>> keyword2 = new HashMap<String,ArrayList<String>>();//search

        ArrayList<String> reg_2017 = new ArrayList<String>();
        ArrayList<String> reg_2016 = new ArrayList<String>();
        ArrayList<String> reg_2015 = new ArrayList<String>();
        ArrayList<String> ser_2017 = new ArrayList<String>();
        ArrayList<String> ser_2016 = new ArrayList<String>();
        ArrayList<String> ser_2015 = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat();SimpleDateFormat sdf2 = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");sdf2.applyPattern("yyyyMMdd");
        Calendar cal = Calendar.getInstance();

        reg_2017.add("2017-01-01");reg_2017.add(sdf.format(cal.getTime()));
        keyword1.put("2017",reg_2017);
        reg_2016.add("2016-01-01");reg_2016.add("2016-12-31");
        keyword1.put("2016",reg_2016);
        reg_2015.add("2015-01-01");reg_2015.add("2015-12-31");
        keyword1.put("2015",reg_2015);

        ser_2017.add("20170101");ser_2017.add(sdf2.format(cal.getTime()));
        keyword2.put("2017",ser_2017);
        ser_2016.add("20160101");ser_2016.add("20161231");
        keyword2.put("2016",ser_2016);
        ser_2015.add("20150101");ser_2015.add("20151231");
        keyword2.put("2015",ser_2015);

        Document doc;
        try {


            doc = Jsoup.connect(showListURL)
                    .data("flag","N")
                    .data("pageNo","1")
                    .data("s_date",keyword2.get(year).get(0))
                    .data("e_date",keyword2.get(year).get(1))
                    .data("reg_d_s",keyword1.get(year).get(0))
                    .data("reg_d_e",keyword1.get(year).get(1))
                    .data("keyfield","petiTitle")
                    .timeout(5000)
                    .post();

            Elements td = doc.select(".listForm1.mt10 td");

            numOfPaging = (Integer.parseInt(td.get(0).text())/10)+1;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return numOfPaging;
    }

}
