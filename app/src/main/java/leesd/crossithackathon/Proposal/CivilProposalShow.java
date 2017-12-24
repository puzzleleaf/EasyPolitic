package leesd.crossithackathon.Proposal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import leesd.crossithackathon.DataManager.CivilProposal;
import leesd.crossithackathon.Grievance.TabAdapter;
import leesd.crossithackathon.R;

/**
 * Created by leesd on 2017-12-23.
 */

public class CivilProposalShow extends Activity implements AdapterView.OnItemClickListener {

    private final String showListURL = "https://www.epeople.go.kr/jsp/user/pp/UPpProposOpenList.paid";
    private final String showDetailURL = "https://www.epeople.go.kr/jsp/user/pp/UPpProposNiceRead.paid";

    //로딩창
    private Handler mHandler;
    private ProgressDialog mProgressDialog;

    private ImageView uSuYearNext;
    private ImageView uSuYearPrev;
    private TextView uSuYear;
    private int dataYear;
    private ImageView uSuPageNext;
    private ImageView uSuPagePrev;
    private TextView uSuPage;
    private int dataPage;

    private int pageNumber; // 총 몇페이지 있는지

    String html = "";
    ListView listView;
    ProposalListAdapter proposalListAdapter = new ProposalListAdapter();
    CivilProposal civilProposal = new CivilProposal();
    HashMap<String, ArrayList<String>> pageList = new HashMap<String, ArrayList<String>>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civil_proposal);

        mHandler = new Handler(); // 로딩창 핸들러 생성

        listView = (ListView) findViewById(R.id.proposal_list);
        proposalListAdapter = new ProposalListAdapter();
        listView.setAdapter(proposalListAdapter);


        dataYear = 2017;
        uSuYear = (TextView)findViewById(R.id.usu_year);
        uSuYear.setText(String.valueOf(dataYear));
        uSuYearNext = (ImageView)findViewById(R.id.usu_year_next);
        uSuYearPrev = (ImageView)findViewById(R.id.usu_year_prev);

        dataPage = 1;
        uSuPage = (TextView)findViewById(R.id.usu_page);
        uSuPage.setText(String.valueOf(dataPage));
        uSuPageNext = (ImageView)findViewById(R.id.usu_page_next);
        uSuPagePrev = (ImageView)findViewById(R.id.usu_page_prev);

        Integer[] integer = {dataYear, dataPage};

        uSuYearNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearNextCheck();
            }
        });
        uSuYearPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataYearPrevCheck();
            }
        });
        uSuPageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPageNextCheck();
            }
        });
        uSuPagePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPagePrevCheck();
            }
        });

        listView.setOnItemClickListener(this);

        new RoadDataTask().execute(integer);

        // System.out.println(result.get("writingNo").get(i)+"//"+result.get("title").get(i)+"//"+result.get("institution").get(i)+"//"+result.get("request_date").get(i)+"//"+result.get("hits").get(i));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       ProposalItem proposalItem = (ProposalItem)parent.getItemAtPosition(position); // listview에서 click한 item의 정보를 가져온다.
        new RoadHtmlTask().execute(proposalItem);
    }

    private class RoadDataTask extends AsyncTask<Integer, String, String>{
        @Override
        protected String doInBackground(Integer... integers) {
            loading();
            String nowYear = String.valueOf(integers[0]);
            int pageNum = integers[1];
            HashMap<String, ArrayList<String>> pageList = new HashMap<String, ArrayList<String>>();
            pageList = getList(nowYear,pageNum);
            for (int i = 0; i < pageList.get("writingNo").size(); i++)
                proposalListAdapter.addItem(pageList.get("writingNo").get(i), pageList.get("title").get(i), pageList.get("institution").get(i), pageList.get("request_date").get(i),
                        pageList.get("petiNo").get(i), pageList.get("ancCode").get(i), pageList.get("pageNo").get(0), pageList.get("reg_date").get(0), pageList.get("reg_date").get(1));
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
        }
        @Override
        protected void onPostExecute(String result) {
            listRefresh();
        }


    }
    private class RoadHtmlTask extends AsyncTask<ProposalItem, String, String>{

        @Override
        protected String doInBackground(ProposalItem... proposalItems) {
            loading();
            html = getDetail(proposalItems[0].getPetiNo(), proposalItems[0].getAnaCode(), proposalItems[0].getPageNo(), proposalItems[0].getReg_date1(), proposalItems[0].getReg_date2());
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
        }
        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), DetailView.class);
            intent.putExtra("html", html);
            startActivity(intent);
        }



    }

    private void yearTextSet(){
        uSuYear.setText(String.valueOf(dataYear));
    }
    private void dataYearPrevCheck(){
        if(dataYear>2012){ // 2013 ~ 2017 데이터만 다룬다
            dataPage = 1;
            dataYear--;
            Integer[] integer = {dataYear, dataPage};
            new RoadDataTask().execute(integer);
            listRefresh();
        }
    }
    private void dataYearNextCheck(){
        if(dataYear<2017){
            dataPage = 1;
            dataYear++;
            Integer[] integer = {dataYear, dataPage};
            new RoadDataTask().execute(integer);
            listRefresh();
        }
    }

    private void pageTextSet(){
        uSuPage.setText(String.valueOf(dataPage));
    }
    private void dataPagePrevCheck(){
        if(dataPage>1){
            dataPage--;
            Integer[] integer = {dataYear, dataPage};
            new RoadDataTask().execute(integer);
            listRefresh();
        }
    }
    private void dataPageNextCheck(){
        if(dataPage<pageNumber){
            dataPage++;
            Integer[] integer = {dataYear, dataPage};
            new RoadDataTask().execute(integer);
            listRefresh();
        }
    }

    private void listRefresh(){
        yearTextSet();
        pageTextSet();
        listView.setAdapter(proposalListAdapter);
        proposalListAdapter = new ProposalListAdapter();
        mProgressDialog.dismiss();
    }

    public void loading(){
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mProgressDialog = ProgressDialog.show(CivilProposalShow.this,"",
                        "데이터를 가져오는 중입니다.",true);
                mHandler.postDelayed( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            if (mProgressDialog!=null&&mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }
                        catch ( Exception e )
                        {
                            e.printStackTrace();
                        }
                    }
                }, 100000);
            }
        } );
    }
    public HashMap<String, ArrayList<String>> getList(String year, int pageNo) { //리스트 목록 따오는 메소드

        /*
        *
        *
        for(int i=0;i<result.size();i++)
		System.out.println(result.get("writingNo").get(i)+"//"+result.get("title").get(i)+"//"+result.get("institution").get(i)+"//"+result.get("request_date").get(i)+"//"+result.get("hits").get(i));
		여기서 writingNo와 title institution request_date hits의 값을 얻어 올수 있다.
		이는 맨처음 게시판에서 리스트를 출력 할때 필요한 텍스트들을 가져온 값으로써  각 리스트의 게시물 마다 값이 필요하기 떄문에 get(i)형태로
		값을 얻어 올 수 있다.
        * */
        HashMap<String, ArrayList<String>> keyword1 = new HashMap<String, ArrayList<String>>();//reg
        HashMap<String, ArrayList<String>> keyword2 = new HashMap<String, ArrayList<String>>();//search
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();

        SimpleDateFormat sdf = new SimpleDateFormat();
        SimpleDateFormat sdf2 = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        sdf2.applyPattern("yyyyMMdd");
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
        ArrayList<String> reg_2014 = new ArrayList<String>();
        ArrayList<String> reg_2013 = new ArrayList<String>();


        ArrayList<String> ser_2017 = new ArrayList<String>();
        ArrayList<String> ser_2016 = new ArrayList<String>();
        ArrayList<String> ser_2015 = new ArrayList<String>();
        ArrayList<String> ser_2014 = new ArrayList<String>();
        ArrayList<String> ser_2013 = new ArrayList<String>();

        reg_2017.add("2017-01-01");
        reg_2017.add(sdf.format(cal.getTime()));
        keyword1.put("2017", reg_2017);
        reg_2016.add("2016-01-01");
        reg_2016.add("2016-12-31");
        keyword1.put("2016", reg_2016);
        reg_2015.add("2015-01-01");
        reg_2015.add("2015-12-31");
        keyword1.put("2015", reg_2015);
        reg_2016.add("2014-01-01");
        reg_2016.add("2014-12-31");
        keyword1.put("2014", reg_2014);
        reg_2015.add("2013-01-01");
        reg_2015.add("2013-12-31");
        keyword1.put("2013", reg_2013);

        ser_2017.add("20170101");
        ser_2017.add(sdf2.format(cal.getTime()));
        keyword2.put("2017", ser_2017);
        ser_2016.add("20160101");
        ser_2016.add("20161231");
        keyword2.put("2016", ser_2016);
        ser_2015.add("20150101");
        ser_2015.add("20151231");
        keyword2.put("2015", ser_2015);
        ser_2016.add("20140101");
        ser_2016.add("20141231");
        keyword2.put("2014", ser_2014);
        ser_2015.add("20130101");
        ser_2015.add("20131231");
        keyword2.put("2013", ser_2013);

        if (pageCounting(year) < pageNo || pageNo < 1) {

            System.out.println("정확한 페이지 수 요구.");
            return null;

        }


        try {

            Document doc = Jsoup.connect(showListURL)
                    .data("flag", "N")
                    .data("pageNo", pageNo + "")
                    .data("s_date", keyword2.get(year).get(0))
                    .data("e_date", keyword2.get(year).get(1))
                    .data("reg_d_s", keyword1.get(year).get(0))
                    .data("reg_d_e", keyword1.get(year).get(1))
                    .data("keyfield", "petiTitle")
                    .timeout(10000)
                    .post();


            Elements td = doc.select(".listForm1.mt10 td");

            for (int i = 0; i < td.size(); i++) {

                if (i % 5 == 0)
                    tmp_writingNo.add(td.get(i).text());
                else if ((i - 1) % 5 == 0)
                    tmp_title.add(td.get(i).text());
                else if ((i - 2) % 5 == 0)
                    tmp_institution.add(td.get(i).text());
                else if ((i - 3) % 5 == 0)
                    tmp_date.add(td.get(i).text());
                else if ((i - 4) % 5 == 0)
                    tmp_hits.add(td.get(i).text());
                else
                    continue;
            }

            result.put("title", tmp_title);
            result.put("writingNo", tmp_writingNo);
            result.put("institution", tmp_institution);
            result.put("request_date", tmp_date);
            result.put("hits", tmp_hits);

            tmp_reg.add(keyword2.get(year).get(0));
            tmp_reg.add(keyword2.get(year).get(1));
            result.put("reg_date", tmp_reg); // reg date 구하기 0 -> start 1 -> end

            tmp_pageNo.add(pageNo + "");
            result.put("pageNo", tmp_pageNo);//pageNo 구하기

            Elements hidden = doc.select(".taL input[type=hidden]");

            for (Element e : hidden)
                tmp_hidden.add(e.attr("value"));

            ArrayList<String> petiNo = new ArrayList<String>();
            ArrayList<String> ancCode = new ArrayList<String>();

            for (int i = 0; i < tmp_hidden.size(); i++) {
                if (i % 2 == 0)
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
    public int pageCounting(String year){ //페이지 수 가져오는 메소드 (년도가 입력되면 그 기간 내에 필요한 페이지수를 구하는 함수 이다.)


        int numOfPaging=0;

        HashMap<String,ArrayList<String>> keyword1 = new HashMap<String,ArrayList<String>>();//reg
        HashMap<String,ArrayList<String>> keyword2 = new HashMap<String,ArrayList<String>>();//search

        ArrayList<String> reg_2017 = new ArrayList<String>();
        ArrayList<String> reg_2016 = new ArrayList<String>();
        ArrayList<String> reg_2015 = new ArrayList<String>();
        ArrayList<String> reg_2014 = new ArrayList<String>();
        ArrayList<String> reg_2013 = new ArrayList<String>();


        ArrayList<String> ser_2017 = new ArrayList<String>();
        ArrayList<String> ser_2016 = new ArrayList<String>();
        ArrayList<String> ser_2015 = new ArrayList<String>();
        ArrayList<String> ser_2014 = new ArrayList<String>();
        ArrayList<String> ser_2013 = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat();SimpleDateFormat sdf2 = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");sdf2.applyPattern("yyyyMMdd");
        Calendar cal = Calendar.getInstance();


        reg_2017.add("2017-01-01");reg_2017.add(sdf.format(cal.getTime()));
        keyword1.put("2017",reg_2017);
        reg_2016.add("2016-01-01");reg_2016.add("2016-12-31");
        keyword1.put("2016",reg_2016);
        reg_2015.add("2015-01-01");reg_2015.add("2015-12-31");
        keyword1.put("2015",reg_2015);
        reg_2016.add("2014-01-01");reg_2016.add("2014-12-31");
        keyword1.put("2014",reg_2016);
        reg_2015.add("2013-01-01");reg_2015.add("2013-12-31");
        keyword1.put("2013",reg_2015);

        ser_2017.add("20170101");ser_2017.add(sdf2.format(cal.getTime()));
        keyword2.put("2017",ser_2017);
        ser_2016.add("20160101");ser_2016.add("20161231");
        keyword2.put("2016",ser_2016);
        ser_2015.add("20150101");ser_2015.add("20151231");
        keyword2.put("2015",ser_2015);
        ser_2016.add("20140101");ser_2016.add("20141231");
        keyword2.put("2014",ser_2016);
        ser_2015.add("20130101");ser_2015.add("20131231");
        keyword2.put("2013",ser_2015);

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

        pageNumber = numOfPaging;
        return numOfPaging;
    }

    public String getDetail(String petiNo,String ancCode,String pageNo,String reg_d_s,String reg_d_e){ //게시글 상세보기 메소드 (반환형 html 소스형태) 글의 상세보기의 값을 가져옴.

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


}
