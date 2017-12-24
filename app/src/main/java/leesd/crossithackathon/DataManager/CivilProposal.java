package leesd.crossithackathon.DataManager;

import android.os.AsyncTask;

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

public class CivilProposal extends AsyncTask {

    private final String showListURL;
    private final String showDetailURL;

    public CivilProposal(){

        showListURL = "https://www.epeople.go.kr/jsp/user/pp/UPpProposOpenList.paid";
        showDetailURL = "https://www.epeople.go.kr/jsp/user/pp/UPpProposNiceRead.paid";

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    public HashMap<String,ArrayList<String>> getList(String year, int pageNo){ //리스트 목록 따오는 메소드

        /*
        *
        *
        for(int i=0;i<result.size();i++)
		System.out.println(result.get("writingNo").get(i)+"//"+result.get("title").get(i)+"//"+result.get("institution").get(i)+"//"+result.get("request_date").get(i)+"//"+result.get("hits").get(i));
		여기서 writingNo와 title institution request_date hits의 값을 얻어 올수 있다.
		이는 맨처음 게시판에서 리스트를 출력 할때 필요한 텍스트들을 가져온 값으로써  각 리스트의 게시물 마다 값이 필요하기 떄문에 get(i)형태로
		값을 얻어 올 수 있다.
        * */
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
                    .timeout(10000)
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


    public String getDetail(String petiNo,String ancCode,String pageNo,String reg_d_s,String reg_d_e){ //게시글 상세보기 메소드 (반환형 html 소스형태) 글의 상세보기의 값을 가져옴.
/**   for(int i=0; i<result.size();i++){

 html = test.getDetail(result.get("petiNo").get(i), result.get("ancCode").get(i), result.get("pageNo").get(0), result.get("reg_date").get(0), result.get("reg_date").get(1));

 }
 게시물 별로 인덱싱 되어 있음. 현재 페이지 리스트에 있는 글의 상세보기를 보기 위해서 위의 값들이 필요함.
 이때 petiNo 과 ancCode는 각 게시물의 고유번호임. 리스트 순서대로 위의 예시 처럼 값을 넣어주면됨(get(i)형태로)
 pageNo 과 reg_d_s 과 reg_d_e 는 현재 페이지의 넘버와 2016년의 값을 가져올 경우 reg_d_s는 2016-01-01 reg_d_end 는 2017-01-01이 됨.
 방금 말한 세개의 값은 고정된것 이기 때문에 "pageNo"의 경우 get(0)에 값이 들어 있고, "reg_date"의 경우 get(0) <- reg_d_s // get(1) <- reg_d_e
 에 값이 들어있음. 한마디로 앞의 두 값은 게시물의 고유 번호이기 때문에 인덱스 i로 값을 get하는 거고 뒤의 pageNo reg_d_s reg_d_e 는 값이 고정 되어 있기
 때문에 값을 0 이나 1로 get 해오는것임.

        public class App {

            public static void main( String[] args )
            {


                WebCrawling test = new WebCrawling();
                HashMap<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
                String html = "";

                int numOfPaging = test.pageCounting("2016");

                result = test.getList("2016",1);


                for(int i=0; i<result.size();i++){

                    html = test.getDetail(result.get("petiNo").get(i), result.get("ancCode").get(i), result.get("pageNo").get(0), result.get("reg_date").get(0), result.get("reg_date").get(1));

                }

                for(int i=0;i<result.size();i++)
                    System.out.println(result.get("writingNo").get(i)+"//"+result.get("title").get(i)+"//"+result.get("institution").get(i)+"//"+result.get("request_date").get(i)+"//"+result.get("hits").get(i));

                System.out.println(html);

            }

        }

*/


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


    public int pageCounting(String year){ //페이지 수 가져오는 메소드 (년도가 입력되면 그 기간 내에 필요한 페이지수를 구하는 함수 이다.)


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
                    .timeout(10000)
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





/*

안드로이드 웹뷰에 HTML적용 시키기.
package com.example.hblee.map2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class trends_ssu_news_webview extends Activity {
    WebView w;
    trends_ssu_news_parse parse;
    String getdata=null;
    String getlink=null;
    String curdata=null;
    String previous_link=null;

    final Handler handlser = new Handler() {
        public void handleMessage(Message msg) {
            update();
        }
    };
    public void update()
    {
        if(curdata==null) {
            getdata=parse.get_data;
            curdata=getdata;
            w.loadDataWithBaseURL(null, creHtml(), "text/html", "utf-8", null); // 여기서 Html값을 넣어주면 된다.
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trends_ssu_news_webview);
        w= (WebView)findViewById(R.id.ssu_news_webview);
        getIntent();
        getlink=getIntent().getStringExtra("news_link");
        previous_link=getIntent().getStringExtra("previous_link");
        parse=new trends_ssu_news_parse(getlink,handlser,this);
        getdata=parse.get_data;
        WebSettings setting= w.getSettings();//web 설정할 수 있다.
        setting.setJavaScriptEnabled(true);///중용함!!!!!
        w.setWebViewClient(new WebViewClient());
        w.setHorizontalScrollBarEnabled(false);
        w.setVerticalScrollBarEnabled(false);

    }
    public String creHtml() // 구성 이렇게해서 보여주면됨.
    {
        StringBuffer sb= new StringBuffer("<HTML>");
        sb.append("<HEAD>");
        //sb.append("<meta name=viewport"+"content=initial-scale=1.0; minimum-scale=1.0; user-scalable=yes;/>");
        sb.append("</HEAD>");
        sb.append("<BODY style='margin:0;'>");
        sb.append(getdata); // 여기에다가 위에서 얻어온 html 값을 넣어주면 된다.
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }
}


*/